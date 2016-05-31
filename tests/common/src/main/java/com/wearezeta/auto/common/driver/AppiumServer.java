package com.wearezeta.auto.common.driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.common.process.AsyncProcess;
import com.wearezeta.common.process.UnixProcessHelpers;
import org.apache.log4j.Logger;
import org.openqa.selenium.net.UrlChecker;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class AppiumServer {
    private static final Logger log = ZetaLogger.getLog(AppiumServer.class.getSimpleName());

    private static AppiumServer instance = null;

    private AppiumServer() {
    }

    public synchronized static AppiumServer getInstance() {
        if (instance == null) {
            instance = new AppiumServer();
        }
        return instance;
    }

    private static final int PORT = 4723;
    private static final int SELENDROID_PORT = 4444;
    private static final int RESTART_TIMEOUT = 30000; // milliseconds
    private static final String SERVER_URL = String.format("http://127.0.0.1:%d/wd/hub", PORT);

    private boolean waitUntilIsStopped(long millisecondsTimeout) throws Exception {
        final URL status = new URL(SERVER_URL + "/status");
        try {
            new UrlChecker().waitUntilUnavailable(millisecondsTimeout, TimeUnit.MILLISECONDS, status);
            return true;
        } catch (UrlChecker.TimeoutException e) {
            return false;
        }
    }

    private boolean waitUntilIsRunning(long millisecondsTimeout) throws Exception {
        final URL status = new URL(SERVER_URL + "/status");
        try {
            new UrlChecker().waitUntilAvailable(millisecondsTimeout, TimeUnit.MILLISECONDS, status);
            return true;
        } catch (UrlChecker.TimeoutException e) {
            return false;
        }
    }

    private static final String MAIN_EXECUTABLE_PATH = "/usr/local/bin/appium";
    public static final int DEFAULT_COMMAND_TIMEOUT = 500; // in seconds
    private static final String LOG_PATH = "/usr/local/var/log/appium/appium.log";

    private static final String[] DEFAULT_CMDLINE = new String[]{
            MAIN_EXECUTABLE_PATH,
            "--port", Integer.toString(PORT),
            "--session-override",
            "--selendroid-port", Integer.toString(SELENDROID_PORT),
            "--log", LOG_PATH,
            "--log-timestamp"
    };

    private void ensureParentDirExistence(String filePath) throws Exception {
        final File log = new File(filePath);
        if (!log.getParentFile().exists()) {
            if (!log.getParentFile().mkdirs()) {
                throw new RuntimeException(String.format("The script has failed to create '%s' folder for Appium logs. " +
                                "Please make sure your account has correct access permissions on the parent folder(s)",
                        log.getParentFile().getCanonicalPath()));
            }
        }
    }

    private void ensureAppiumExecutableExistence() throws Exception {
        if (!new File(MAIN_EXECUTABLE_PATH).exists()) {
            throw new RuntimeException(
                    String.format("The script is unable to find main Appium executable at the path '%s'. " +
                                    "Please make sure it is properly installed (`npm install -g appium`)",
                            MAIN_EXECUTABLE_PATH));
        }
    }

    public synchronized void resetIOS() throws Exception {
        UnixProcessHelpers.killProcessesGracefully("osascript",
                "Simulator", "configd_sim", "xpcproxy_sim", "backboardd",
                "platform_launch_", "companionappd", "ids_simd", "launchd_sim",
                "CoreSimulatorBridge", "SimulatorBridge", "SpringBoard",
                "locationd", "MobileGestaltHelper", "cfprefsd",
                "assetsd", "fileproviderd", "mediaremoted",
                "routined", "assetsd", "mstreamd", "healthd", "MobileCal",
                "callservicesd", "revisiond", "touchsetupd", "calaccessd",
                "ServerFileProvider", "mobileassetd", "IMDPersistenceAgent",
                "itunesstored", "profiled", "passd", "carkitd", "instruments");
        restart();
    }

    public void restart() throws Exception {
        final String hostname = InetAddress.getLocalHost().getHostName();
        log.warn(String.format("Trying to (re)start Appium server on %s:%s...", hostname, PORT));

        UnixProcessHelpers.killProcessesGracefully("node");
        waitUntilIsStopped(RESTART_TIMEOUT / 2);

        ensureAppiumExecutableExistence();
        ensureParentDirExistence(LOG_PATH);

        final AsyncProcess appiumProcess = new AsyncProcess(DEFAULT_CMDLINE, false, false).start();
        log.info(String.format("Waiting for Appium to be (re)started on %s:%s...", hostname, PORT));
        final long msStarted = System.currentTimeMillis();
        if (!waitUntilIsRunning(RESTART_TIMEOUT)) {
            throw new IllegalStateException(String.format(
                    "Appium server has failed to start after %s seconds timeout on server '%s'.\n" +
                            "Please make sure that NodeJS and Appium packages are installed properly on this machine.\n" +
                            "Appium logs:\n\n%s\n\n%s\n\n\n",
                    RESTART_TIMEOUT / 1000, hostname, appiumProcess.getStderr(), appiumProcess.getStdout()));
        }

        log.info(String.format("Appium server has been successfully (re)started after %.1f seconds " +
                "and now is listening on %s:%s", (System.currentTimeMillis() - msStarted) / 1000.0, hostname, PORT));
    }

    public boolean isRunning() throws Exception {
        return waitUntilIsRunning(1);
    }

    public Optional<String> getLog() {
        final File logFile = new File(LOG_PATH);
        if (logFile.exists()) {
            try {
                return Optional.of(new String(Files.readAllBytes(logFile.toPath()), Charset.forName("UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
}
