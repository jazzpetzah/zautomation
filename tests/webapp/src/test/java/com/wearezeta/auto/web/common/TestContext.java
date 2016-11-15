package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.driver.ZetaWinDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestContext {
    
    private static final Logger LOG = ZetaLogger.getLog(TestContext.class.getSimpleName());
    
    // IDLE_TIMEOUT 90s https://www.browserstack.com/automate/timeouts
    private static final long DRIVER_INIT_TIMEOUT = 360; //seconds
    private final List<LogEntry> BROWSER_LOG = new ArrayList<>();
    
    public static Future<ZetaWebAppDriver> COMPAT_WEB_DRIVER;
    public static Future<ZetaWinDriver> COMPAT_WIN_DRIVER;

    private final Platform currentPlatform = Platform.Web;
    private final Pinger pinger;

    private final String testname;
    private final CommonSteps commonSteps;
    private final ClientUsersManager userManager;
    private final SEBridge deviceManager;
    private final CommonCallingSteps2 callingManager;
    private final AbstractPagesCollection<? extends BasePage> pagesCollection;
    private final Future<? extends RemoteWebDriver> driver;
    private final ConversationStates conversationStates;

    public TestContext(String uniqueTestname, Future<? extends RemoteWebDriver> driver) throws Exception {
        this.testname = uniqueTestname;
        this.driver = driver;
        this.userManager = new ClientUsersManager();
        this.deviceManager = SEBridge.getInstance();
        this.commonSteps = new CommonSteps(userManager, deviceManager);
        this.callingManager = new CommonCallingSteps2(userManager);
        this.pagesCollection = new WebappPagesCollection();
        this.conversationStates = new ConversationStates();
        this.pinger = new Pinger(driver);
    }

    /**
     * Constructor for downward compatibility with cucumber execution mechanisms
     */
    public TestContext() {
        this.testname = "";
        this.driver = COMPAT_WEB_DRIVER;
        this.userManager = ClientUsersManager.getInstance();
        this.deviceManager = SEBridge.getInstance();
        this.commonSteps = CommonSteps.getInstance();
        this.callingManager = CommonCallingSteps2.getInstance();
        this.pagesCollection = WebappPagesCollection.getInstance();
        this.conversationStates = new ConversationStates();
        this.pinger = new Pinger(driver);
    }
    
    private TestContext(Pinger pinger, String testname, CommonSteps commonSteps, ClientUsersManager userManager,
            SEBridge deviceManager, CommonCallingSteps2 callingManager, AbstractPagesCollection<? extends BasePage> pagesCollection,
            Future<? extends RemoteWebDriver> driver, ConversationStates conversationStates) {
        this.pinger = pinger;
        this.testname = testname;
        this.commonSteps = commonSteps;
        this.userManager = userManager;
        this.deviceManager = deviceManager;
        this.callingManager = callingManager;
        this.pagesCollection = pagesCollection;
        this.driver = driver;
        this.conversationStates = conversationStates;
    }
    
    
    public TestContext fromPrimaryContext(Future<? extends RemoteWebDriver> driver, AbstractPagesCollection<? extends BasePage> pagesCollection) throws Exception {
        return new TestContext(new Pinger(driver), this.testname, this.commonSteps, this.userManager, this.deviceManager, this.callingManager,
                pagesCollection, driver, conversationStates);
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
        return (WebappPagesCollection) pagesCollection;
    }
    
    public <T extends AbstractPagesCollection<?>> T getPagesCollection(Class<T> type) {
        return (T)pagesCollection;
    }

    public Future<? extends RemoteWebDriver> getFutureDriver() {
        return driver;
    }

    public RemoteWebDriver getDriver() throws InterruptedException, ExecutionException, TimeoutException {
        return driver.get(DRIVER_INIT_TIMEOUT, TimeUnit.SECONDS);
    }

    public Platform getCurrentPlatform() {
        return currentPlatform;
    }

    public ConversationStates getConversationStates() {
        return conversationStates;
    }
    
    public void startPinging() {
        pinger.startPinging();
    }
    public void stopPinging() {
        pinger.stopPinging();
    }
    
    @SuppressWarnings("unchecked")
    public List<LogEntry> getBrowserLog() throws InterruptedException, ExecutionException, TimeoutException {
        BROWSER_LOG.addAll(IteratorUtils.toList((Iterator<LogEntry>) getDriver().manage().logs().get(LogType.BROWSER).iterator()));
        return BROWSER_LOG;
    }
    
}
