package com.wearezeta.auto.osx.common;

import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaOSXWebAppDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.rc.BasicScenarioResultToTestrailTransformer;
import com.wearezeta.auto.common.testrail.TestrailSyncUtilities;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.clearAddressbookPermission;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.clearAppData;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.killAllApps;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.startAppium4Mac;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.APPIUM_HUB_URL;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.KEEP_DATABASE;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.WIRE_APP_PATH;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import static com.wearezeta.auto.osx.steps.CommonOSXSteps.LOG;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.RegistrationPage;
import com.wearezeta.auto.web.pages.WebPage;
import static com.wearezeta.auto.web.steps.CommonWebAppSteps.log;
import com.wire.picklejar.gherkin.model.Step;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Lifecycle {

    private WrapperTestContext context;
    private WrapperTestContext compatContext;
    private ChromeDriverService service;
    private String testname;

    /**
     * The context is fully initialized after setting up the testcase
     *
     * @return
     */
    public WrapperTestContext getContext() {
        return context;
    }

    // #### START ############################################################ COMPATIBILITY INSTRUCTIONS
    @Before("~@performance")
    public void setUp(Scenario scenario) throws Exception {
        String id = scenario.getId().substring(
                scenario.getId().lastIndexOf(";") + 1);
        setUp(scenario.getName() + "_" + id);
    }

    @After
    public void tearDown(Scenario scenario) throws Exception {
        tearDown();
    }
    // #### END ############################################################## COMPATIBILITY INSTRUCTIONS
    
    private Future<ZetaWebAppDriver> createWebDriver(
            Future<ZetaOSXDriver> osxDriver) throws IOException {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        // simulate a fake webcam and mic for testing
        options.addArguments("use-fake-device-for-media-stream");
        // allow skipping the security prompt for sharing the media device
        options.addArguments("use-fake-ui-for-media-stream");
        options.addArguments("disable-web-security");
        options.addArguments("env=" + OSXExecutionContext.ENV_URL);
        options.addArguments("enable-logging");
        options.setBinary(WIRE_APP_PATH + OSXExecutionContext.ELECTRON_SUFFIX);

        // allow skipping the security prompt for notifications in chrome 46++
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings.notifications", 1);
        options.setExperimentalOption("prefs", prefs);

        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability("platformName",
                OSXExecutionContext.CURRENT_SECONDARY_PLATFORM.name());

        setExtendedLoggingLevel(capabilities,
                OSXExecutionContext.EXTENDED_LOGGING_LEVEL);

        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(
                        new File(OSXExecutionContext.CHROMEDRIVER_PATH))
                .usingAnyFreePort().build();
        service.start();
        final ExecutorService pool = Executors.newFixedThreadPool(1);

        Callable<ZetaWebAppDriver> callableWebAppDriver = () -> new ZetaOSXWebAppDriver(
                service.getUrl(), capabilities, osxDriver.get());

        return pool.submit(callableWebAppDriver);
    }

    private Future<ZetaOSXDriver> createOSXDriver() throws MalformedURLException {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(CapabilityType.PLATFORM, "MAC");
        capabilities.setCapability("platformName", "Mac");

        final ExecutorService pool = Executors.newFixedThreadPool(1);

        Callable<ZetaOSXDriver> callableOSXDriver = () -> {
            ZetaOSXDriver zetaOSXDriver = new ZetaOSXDriver(new URL(APPIUM_HUB_URL), capabilities);
            // necessary for calculating the size of the window etc. because this is not supported by AppiumForMac
            zetaOSXDriver.setWindowLocator(By.xpath(OSXLocators.MainWirePage.xpathWindow));
            return zetaOSXDriver;
        };

        return pool.submit(callableOSXDriver);
    }

    private static void setExtendedLoggingLevel(
            DesiredCapabilities capabilities, String loggingLevelName) {
        final LoggingPreferences logs = new LoggingPreferences();
        // set it to SEVERE by default
        Level level = Level.ALL;
        try {
            level = Level.parse(loggingLevelName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Just continue with the default logging level
        }
        logs.enable(LogType.BROWSER, level);
        // logs.enable(LogType.CLIENT, Level.ALL);
        // logs.enable(LogType.DRIVER, Level.ALL);
        // logs.enable(LogType.PERFORMANCE, Level.ALL);
        // logs.enable(LogType.PROFILER, Level.ALL);
        // logs.enable(LogType.SERVER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
        log.debug("Browser logging level has been set to '" + level.getName()
                + "'");
    }

    public void startApp() throws Exception {
        Future<ZetaOSXDriver> osxDriverFuture;
        Future<ZetaWebAppDriver> webDriverFuture;

        LOG.debug("Create OS X Driver");
        osxDriverFuture = createOSXDriver();
        LOG.debug("Init OS X Driver");
        final ZetaOSXDriver osxDriver = osxDriverFuture.get();
        LOG.debug("Create Chrome Driver");
        webDriverFuture = createWebDriver(osxDriverFuture);
        LOG.debug("Init Chrome Driver");
        final ZetaWebAppDriver webappDriver = webDriverFuture.get();
        
        LOG.debug("Waiting for OS X App to be started");
        
        LOG.debug("Setting formatter");
        ZetaFormatter.setLazyDriver(osxDriverFuture);
        
        /**
         * #### START ############################################################ COMPATIBILITY INSTRUCTIONS
         */
        WrapperTestContext.COMPAT_WEB_DRIVER = webDriverFuture;
        WrapperTestContext.COMPAT_OSX_DRIVER = osxDriverFuture;
        
        compatContext = new WrapperTestContext();
        LOG.debug("COMPAT: Setting first OS X Page");
        compatContext.getOSXPagesCollection().setFirstPage(new MainWirePage(osxDriverFuture));
        LOG.debug("COMPAT: Setting first Webapp Page");
        compatContext.getWebappPagesCollection().setFirstPage(new RegistrationPage(webDriverFuture));
        /**
         * #### END ############################################################## COMPATIBILITY INSTRUCTIONS
         */

        context = new WrapperTestContext(testname, webDriverFuture, osxDriverFuture);
        LOG.debug("Setting first OS X Page");
        context.getOSXPagesCollection().setFirstPage(new MainWirePage(osxDriverFuture));
        LOG.debug("Setting first Webapp Page");
        context.getWebappPagesCollection().setFirstPage(new RegistrationPage(webDriverFuture));

        LOG.debug("Opening app");
        // necessary to enable the driver
        context.getOSXDriver().navigate().to(WIRE_APP_PATH);// open app
    }

    public void setUp(String testname) throws Exception {
        this.testname = testname;
        try {
            startAppium4Mac();
            killAllApps();
            if (!KEEP_DATABASE) {
                clearAppData();
            }
            clearAddressbookPermission();
        } catch (Exception e) {
            LOG.error(e);
        }
        startApp();
    }

    public void tearDown(com.wire.picklejar.gherkin.model.Scenario scenario) throws Exception {
        try {
            Set<String> tagSet = scenario.getTags().stream()
                    .map((tag) -> tag.getName())
                    .collect(Collectors.toSet());
            TestrailSyncUtilities.syncExecutedScenarioWithTestrail(scenario.getName(),
                    new BasicScenarioResultToTestrailTransformer(mapScenario(scenario)).transform(), tagSet);
        } catch (Exception e) {
            log.warn(e);
        }
        tearDown();
    }

    private Map<String, String> mapScenario(com.wire.picklejar.gherkin.model.Scenario scenario) {
        Map<String, String> stepResultMap = new LinkedHashMap<>();
        for (Step step : scenario.getSteps()) {
            stepResultMap.put(step.getName(), step.getResult().getStatus());
        }
        return stepResultMap;
    }

    public void tearDown() throws Exception {
        try {
            ZetaWebAppDriver driver = (ZetaWebAppDriver) context.getWebDriver();
            // save browser console if possible
            if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
                writeBrowserLogsIntoMainLog(context);
            }
            if (driver instanceof ZetaWebAppDriver) {
                // logout with JavaScript because otherwise backend will block
                // us because of top many login requests
                String logoutScript = "(typeof wire !== 'undefined') && wire.auth.repository.logout();";
                driver.executeScript(logoutScript);
            }
        } catch (Exception e) {
            log.warn(e);
        } finally {
            /**
             * #### START ############################################################ COMPATIBILITY INSTRUCTIONS
             */
            try {
                log.debug("COMPAT: Releasing devices");
                log.debug(compatContext.getUserManager().getCreatedUsers());
                compatContext.getDeviceManager().releaseDevicesOfUsers(compatContext.getUserManager().getCreatedUsers());
            } catch (Exception e) {
                log.warn(e);
            }
            /**
             * #### END ############################################################## COMPATIBILITY INSTRUCTIONS
             */
            try {
                log.debug("Releasing devices");
                log.debug(context.getUserManager().getCreatedUsers());
                context.getDeviceManager().releaseDevicesOfUsers(context.getUserManager().getCreatedUsers());
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Closing webdriver");
                context.getWebDriver().quit();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Closing osxdriver");
                context.getOSXDriver().quit();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Cleaning up calling instances");
                context.getCallingManager().cleanup();
                log.debug("COMPAT: Cleaning up calling instances");
                compatContext.getCallingManager().cleanup();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Clearing pages collection");
                context.getPagesCollection().clearAllPages();
                log.debug("COMPAT: Clearing pages collection");
                compatContext.getPagesCollection().clearAllPages();
                WebPage.clearPagesCollection();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Resetting users");
                context.getUserManager().resetUsers();
                log.debug("COMPAT: Resetting users");
                compatContext.getUserManager().resetUsers();
            } catch (Exception e) {
                log.warn(e);
            }
        }
        
        if (service != null && service.isRunning()) {
            service.stop();
        }
    }

    private void writeBrowserLogsIntoMainLog(TestContext context) throws InterruptedException, ExecutionException, TimeoutException {
        List<LogEntry> logEntries = context.getBrowserLog();
        if (!logEntries.isEmpty()) {
            log.debug("BROWSER CONSOLE LOGS:");
            for (LogEntry logEntry : logEntries) {
                log.debug(logEntry.getMessage().replaceAll("^.*z\\.", "z\\."));
            }
            log.debug("--- END OF LOG ---");
        }
    }
}