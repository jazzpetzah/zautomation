package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import static com.wearezeta.auto.web.common.Lifecycle.DRIVER_INIT_TIMEOUT;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestContext {
    
    private static final Logger LOG = ZetaLogger.getLog(TestContext.class.getSimpleName());
    
    static Future<ZetaWebAppDriver> COMPAT_WEB_DRIVER;

    private final Platform currentPlatform = Platform.Web;
    private final ScheduledExecutorService ping = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> pinger;

    private final String testname;
    private final CommonSteps commonSteps;
    private final ClientUsersManager userManager;
    private final SEBridge deviceManager;
    private final CommonCallingSteps2 callingManager;
    private final WebappPagesCollection pagesCollection;
    private final Future<? extends RemoteWebDriver> driver;

    public TestContext(String uniqueTestname, Future<? extends RemoteWebDriver> driver) throws Exception {
        this.testname = uniqueTestname;
        this.driver = driver;
        this.userManager = new ClientUsersManager();
        this.deviceManager = new SEBridge();
        this.commonSteps = new CommonSteps(userManager, deviceManager);
        this.callingManager = new CommonCallingSteps2(userManager);
        this.pagesCollection = new WebappPagesCollection();
    }

    /**
     * Constructor for downward compatibility with cucumber execution mechanisms
     */
    public TestContext() {
        this.testname = null;
        this.driver = COMPAT_WEB_DRIVER;
        this.userManager = ClientUsersManager.getInstance();
        this.deviceManager = SEBridge.getInstance();
        this.commonSteps = CommonSteps.getInstance();
        this.callingManager = CommonCallingSteps2.getInstance();
        this.pagesCollection = WebappPagesCollection.getInstance();
    }

    public void startPinging() {
        pinger = ping.scheduleAtFixedRate(() -> {
            try {
                if (driver.isCancelled()) {
                    pinger.cancel(true);
                }
                if (driver.isDone()) {
                    LOG.info("Ping");
                    driver.get(10, TimeUnit.SECONDS).getPageSource();
                }
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                LOG.warn("Could not ping driver because it's not initialized yet");
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    public void stopPinging() {
        pinger.cancel(true);
    }

    public String getTestname() {
        return testname;
    }

    public CommonSteps getCommonSteps() {
        return commonSteps;
    }

    public ClientUsersManager getUserManager() {
        return userManager;
    }

    public SEBridge getDeviceManager() {
        return deviceManager;
    }

    public CommonCallingSteps2 getCallingManager() {
        return callingManager;
    }

    public WebappPagesCollection getPagesCollection() {
        return pagesCollection;
    }

    public Future<? extends RemoteWebDriver> getFutureDriver() {
        return driver;
    }

    public RemoteWebDriver getDriver() throws InterruptedException, ExecutionException, TimeoutException {
        return driver.get(DRIVER_INIT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public Platform getCurrentPlatform() {
        return currentPlatform;
    }
}
