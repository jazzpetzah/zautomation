package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.*;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver.SurfaceOrientation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

import javax.imageio.ImageIO;

import static com.wearezeta.auto.common.driver.ZetaAndroidDriver.ADB_PREFIX;

public class CommonUtils {

    public static final int MAX_PARALLEL_USER_CREATION_TASKS = 25;

    private static final String USER_IMAGE = "userpicture_landscape.jpg";

    private static final Random rand = new Random();

    private static final Logger log = ZetaLogger.getLog(CommonUtils.class.getSimpleName());

    public static boolean executeOsCommandWithTimeout(String[] cmd, long timeoutSeconds) throws Exception {
        Process process = Runtime.getRuntime().exec(cmd);
        log.debug("Process started for cmdline " + Arrays.toString(cmd));
        outputErrorStreamToLog(process.getErrorStream());
        return process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
    }

    /**
     * Be careful when using this method - it will block forever if the commands stucks and fails to terminate
     *
     * @param cmd command arguments array
     * @return command return code
     * @throws Exception
     */
    public static int executeOsXCommand(String[] cmd) throws Exception {
        Process process = Runtime.getRuntime().exec(cmd);
        log.debug("Process started for cmdline " + Arrays.toString(cmd));
        outputErrorStreamToLog(process.getErrorStream());
        return process.waitFor();
    }

    private static final int DEFAULT_COMMAND_TIMEOUT = 60; // seconds

    public static String executeOsXCommandWithOutput(String[] cmd) throws Exception {
        return executeOsXCommandWithOutput(cmd, DEFAULT_COMMAND_TIMEOUT);
    }

    public static String executeOsXCommandWithOutput(String[] cmd, int timeoutSeconds) throws Exception {
        Process process = Runtime.getRuntime().exec(cmd);
        log.debug("Process started for cmdline " + Arrays.toString(cmd));
        String output;
        try (InputStream stream = process.getInputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder("\n");
            String s;
            while ((s = br.readLine()) != null) {
                sb.append("\t").append(s).append("\n");
            }
            output = sb.toString();
        }
        outputErrorStreamToLog(process.getErrorStream());
        process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
        return output;
    }

    public static void outputErrorStreamToLog(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder("\n");
        String s;
        while ((s = br.readLine()) != null) {
            sb.append("\t").append(s).append("\n");
        }
        String output = sb.toString();
        if (!output.trim().isEmpty()) {
            log.debug(output);
        }
        stream.close();
    }

    public static String getBackendType(Class<?> c) throws Exception {
        return getValueFromConfig(c, "backendType");
    }

    public static boolean getOtrOnly(Class<?> c) throws Exception {
        return Boolean.parseBoolean(getOptionalValueFromConfig(c, "otrOnly").orElse("false"));
    }

    public static String getDeviceName(Class<?> c) throws Exception {
        return getValueFromConfig(c, "deviceName");
    }

    public static String getImagePath(Class<?> c) throws Exception {
        return getValueFromConfig(c, "defaultImagesPath") + USER_IMAGE;
    }

    public static String getImagesPath(Class<?> c) throws Exception {
        return getValueFromConfig(c, "defaultImagesPath");
    }

    public static String getPictureResultsPathFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "pictureResultsPath");
    }

    private final static Semaphore sem = new Semaphore(1);
    private final static Map<String, Optional<String>> cachedConfig = new HashMap<>();

    private static Optional<String> getValueFromConfigFile(Class<?> c, String key, String resourcePath) throws Exception {
        final String configKey = String.format("%s:%s", resourcePath, key);
        if (cachedConfig.containsKey(configKey)) {
            return cachedConfig.get(configKey);
        }

        final int maxTry = 3;
        int tryNum = 0;
        Exception savedException = null;
        do {
            InputStream configFileStream = null;
            sem.acquire();
            try {
                final ClassLoader classLoader = c.getClassLoader();
                configFileStream = classLoader.getResourceAsStream(resourcePath);
                Properties p = new Properties();
                p.load(configFileStream);
                if (p.containsKey(key)) {
                    cachedConfig.put(configKey, Optional.of((String) p.get(key)));
                } else {
                    cachedConfig.put(configKey, Optional.empty());
                }
                return cachedConfig.get(configKey);
            } catch (Exception e) {
                savedException = e;
            } finally {
                if (configFileStream != null) {
                    configFileStream.close();
                }
                sem.release();
            }
            Thread.sleep(100 + rand.nextInt(2000));
            tryNum++;
        } while (tryNum < maxTry);
        throw savedException;
    }

    private static final String PROJECT_CONFIG = "Configuration.cnf";

    public static Optional<String> getOptionalValueFromConfig(Class<?> c, String key) throws Exception {
        return getValueFromConfigFile(c, key, PROJECT_CONFIG);
    }

    public static String getValueFromConfig(Class<?> c, String key) throws Exception {
        final Optional<String> value = getValueFromConfigFile(c, key, PROJECT_CONFIG);
        if (value.isPresent()) {
            return value.get();
        } else {
            throw new RuntimeException(
                    String.format("There is no '%s' property in the '%s' configuration file", key, PROJECT_CONFIG));
        }
    }

    private static final String COMMON_CONFIG = "CommonConfiguration.cnf";

    public static Optional<String> getOptionalValueFromCommonConfig(Class<?> c, String key) throws Exception {
        return getValueFromConfigFile(c, key, COMMON_CONFIG);
    }

    public static String getValueFromCommonConfig(Class<?> c, String key) throws Exception {
        final Optional<String> value = getValueFromConfigFile(c, key, COMMON_CONFIG);
        if (value.isPresent()) {
            return value.get();
        } else {
            throw new RuntimeException(
                    String.format("There is no '%s' property in the '%s' common configuration file", key, COMMON_CONFIG));
        }
    }

    public static String getDefaultEmailFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "defaultEmail");
    }

    public static String getSpecialEmailFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "specialEmail");
    }

    public static String getSpecialPasswordFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "specialPassword");
    }

    public static String getDefaultEmailServerFromConfig(Class<?> c) throws Exception {
        return getValueFromCommonConfig(c, "defaultEmailServer");
    }

    public static String getDriverTimeoutFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "driverTimeoutSeconds");
    }

    public static String getDefaultPasswordFromConfig(Class<?> c) throws Exception {
        return getValueFromCommonConfig(c, "defaultPassword");
    }

    public static String getDefaultBackEndUrlFromConfig(Class<?> c) throws Exception {
        final String currentBackendType = getBackendType(c);
        switch (currentBackendType.toLowerCase()) {
            case "edge":
                return getValueFromCommonConfig(c, "edgeBackendUrl");
            case "staging":
            case "benny":
            case "gregor":
            case "rene":
            case "lipis":
            case "chris":
                return getValueFromCommonConfig(c, "stagingBackendUrl");
            case "production":
                return getValueFromCommonConfig(c, "productionBackendUrl");
            default:
                throw new RuntimeException(String.format("Non supported backend type '%s'", currentBackendType));
        }
    }

    public static String getAndroidAppiumUrlFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "androidAppiumUrl");
    }

    public static String getIosAppiumUrlFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "iosAppiumUrl");
    }

    public static Boolean getIsSimulatorFromConfig(Class<?> c) throws Exception {
        return (getValueFromConfig(c, "isSimulator").equals("true"));
    }

    public static String getIOSToolsRoot(Class<?> c) throws Exception {
        return getValueFromConfig(c, "iOSToolsRoot");
    }

    public static String getWebAppApplicationPathFromConfig(Class<?> c) throws Exception {
        String applicationPath = getValueFromConfig(c, "webappApplicationPath");
        if (!applicationPath.equals("")) {
            return applicationPath;
        } else {
            final String currentBackendType = getBackendType(c);
            switch (currentBackendType.toLowerCase()) {
                case "edge":
                    return getValueFromConfig(c, "webappEdgeApplicationPath");
                case "staging":
                    return getValueFromConfig(c, "webappStagingApplicationPath");
                case "production":
                    return getValueFromConfig(c, "webappProductionApplicationPath");
                case "benny":
                    return getValueFromConfig(c, "webappBennyApplicationPath");
                case "gregor":
                    return getValueFromConfig(c, "webappGregorApplicationPath");
                case "rene":
                    return getValueFromConfig(c, "webappReneApplicationPath");
                case "lipis":
                    return getValueFromConfig(c, "webappLipisApplicationPath");
                case "chris":
                    return getValueFromConfig(c, "webappChrisApplicationPath");
                default:
                    throw new RuntimeException(String.format("Non supported backend type '%s'", currentBackendType));
            }
        }
    }

    public static String getAndroidApplicationPathFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "androidApplicationPath");
    }

    public static String getIosApplicationPathFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "iosApplicationPath");
    }

    public static String getAndroidMainActivityFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "mainActivity");
    }

    public static boolean getAndroidShowLogcatFromConfig(Class<?> c) throws Exception {
        return Boolean.parseBoolean(getOptionalValueFromConfig(c, "showLogcat").orElse("true"));
    }

    public static String getAndroidConvoListActivityFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "convoListActivity");
    }

    public static String getAndroidLoginActivityFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "loginActivity");
    }

    public static String getOldAppPathFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "oldAppPath");
    }

    public static String getSimulatorImagesPathFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "iosImagesPath");
    }

    public static String getAndroidPackageFromConfig(Class<?> c) {
        try {
            return getValueFromConfig(c, "package");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateGUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateRandomString(int lengh) {
        return RandomStringUtils.randomAlphanumeric(lengh);
    }

    public static String generateRandomNumericString(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static boolean getIsTabletFromConfig(Class<?> c) throws Exception {
        final Optional<String> value = getOptionalValueFromConfig(c, "isTablet");
        return value.isPresent() ? Boolean.valueOf(value.get()) : false;
    }

    public static String getJenkinsSuperUserLogin(Class<?> c) throws Exception {
        return getValueFromCommonConfig(c, "jenkinsSuLogin");
    }

    public static Optional<String> getRCNotificationsRecepients(Class<?> c) throws Exception {
        return getOptionalValueFromConfig(c, "rcNotificationsRecepients");
    }

    public static String getJenkinsSuperUserPassword(Class<?> c) throws Exception {
        return getValueFromCommonConfig(c, "jenkinsSuPassword");
    }

    public static String getDefaultCallingServiceUrlFromConfig(Class<?> c) throws Exception {
        return getValueFromCommonConfig(c, "defaultCallingServiceUrl");
    }

    public static void defineNoHeadlessEnvironment() {
        System.setProperty("java.awt.headless", "false");
    }

    public static String encodeSHA256Base64(String item) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(item.getBytes("UTF-8"));
        final byte[] digest = md.digest();
        return Base64.encodeBase64String(digest);
    }

    public static String getDefaultEmailListenerServiceHostFromConfig(Class<?> c) throws Exception {
        return getValueFromCommonConfig(c, "defaultEmailListenerServiceHost");
    }

    public static String getDefaultEmailListenerServicePortFromConfig(Class<?> c) throws Exception {
        return getValueFromCommonConfig(c, "defaultEmailListenerServicePort");
    }

    public static boolean getMakeScreenshotsFromConfig(Class<?> c) throws Exception {
        return Boolean.valueOf(getValueFromCommonConfig(c, "makeScreenshots"));
    }

    public static boolean getMakeScreenshotOnPassedStepsFromConfig(Class<?> c) throws Exception {
        return Boolean.valueOf(getValueFromCommonConfig(c, "makeScreenshotOnPassedSteps"));
    }

    public static boolean getInitNoteIpFromConfig(Class<?> c) throws Exception {
        return Boolean.valueOf(getValueFromCommonConfig(c, "initNodeIp"));
    }

    public static String getTestrailServerUrlFromConfig(Class<?> c) throws Exception {
        return getValueFromCommonConfig(c, "testrailServerUrl");
    }

    public static String getTestrailUsernameFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "testrailUser");
    }

    public static String getTestrailTokenFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "testrailToken");
    }

    public static Optional<String> getTestrailProjectNameFromConfig(Class<?> c) throws Exception {
        return getOptionalValueFromCommonConfig(c, "testrailProjectName");
    }

    public static Optional<String> getTestrailPlanNameFromConfig(Class<?> c) throws Exception {
        return getOptionalValueFromCommonConfig(c, "testrailPlanName");
    }

    public static Optional<String> getTestrailRunNameFromConfig(Class<?> c) throws Exception {
        return getOptionalValueFromCommonConfig(c, "testrailRunName");
    }

    public static Optional<String> getTestrailRunConfigNameFromConfig(Class<?> c) throws Exception {
        return getOptionalValueFromCommonConfig(c, "testrailRunConfigName");
    }

    public static boolean getSyncIsAutomated(Class<?> c) throws Exception {
        return getValueFromCommonConfig(c, "syncIsAutomated").toLowerCase().equals("true");
    }

    public static boolean getSyncIsMuted(Class<?> c) throws Exception {
        return getValueFromCommonConfig(c, "syncIsMuted").toLowerCase().equals("true");
    }

    public static Optional<String> getAdbPrefixFromConfig(Class<?> c) throws Exception {
        return getOptionalValueFromConfig(c, "adbPrefix");
    }

    public static String generateRandomXdigits(int i) {
        Random rand = new Random();
        long random = (long) (Math.pow(10, i - 1)) * (rand.nextInt(8) + 1) + (long) rand.nextInt((int) (Math.pow(10, i - 1)));
        return Long.toString(Math.abs(random));
    }

    public static String getPlatformVersionFromConfig(Class<?> cls) throws Exception {
        return getValueFromConfig(cls, "platformVersion");
    }

    public static Optional<String> getIsUseNativeInstrumentsLibFromConfig(Class<?> cls) throws Exception {
        return getOptionalValueFromConfig(cls, "useNativeInstrumentsLib");
    }

    public static String getIOSAppName(Class<?> cls) throws Exception {
        return getValueFromConfig(cls, "appName");
    }

    public static int getCachedOtrDevicesCount(Class<?> cls) throws Exception {
        return Integer.parseInt(getValueFromCommonConfig(cls, "cachedOtrDevicesCount"));
    }

    public static final int SCREENSHOT_TIMEOUT_SECONDS = 5;

    public static void takeIOSSimulatorScreenshot(File output) throws Exception {
        executeUIShellScript(new String[]{String.format("mkdir -p $(dirname \"%s\")", output.getCanonicalPath()),
                String.format("%s/simshot \"%s\"", getIOSToolsRoot(CommonUtils.class), output.getCanonicalPath())})
                .get(SCREENSHOT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    private static BufferedImage fixScreenshotOrientation(final BufferedImage initialScreenshot,
                                                          SurfaceOrientation currentOrientation) throws IOException {
        if (currentOrientation != SurfaceOrientation.ROTATION_0) {
            return ImageUtil.tilt(initialScreenshot, -currentOrientation.getCode() * Math.PI / 2);
        }
        return initialScreenshot;
    }

    private static final int MAX_PHONE_SCREENSHOT_WIDTH = 480;
    private static final int MAX_PHONE_SCREENSHOT_HEIGHT = 800;
    private static final int MAX_TABLET_SCREENSHOT_WIDTH = 1600;
    private static final int MAX_TABLET_SCREENSHOT_HEIGHT = 900;


    public static void takeAndroidScreenshot(ZetaAndroidDriver driver, File resultScreenshot, boolean shouldApplyScale)
            throws Exception {
        final String pathOnPhone = String.format("/sdcard/%s.png", generateGUID().replace("-", "").substring(0, 8));
        final String adbCommandsChain = String.format(ADB_PREFIX + "adb shell screencap -p %1$s; " + ADB_PREFIX
                        + "adb pull %1$s %2$s; " + ADB_PREFIX + "adb shell rm %1$s", pathOnPhone,
                resultScreenshot.getCanonicalPath());
        Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", adbCommandsChain}).waitFor();
        byte[] output = FileUtils.readFileToByteArray(resultScreenshot);
        final boolean isTablet = getIsTabletFromConfig(CommonUtils.class);
        BufferedImage resultImg = ImageIO.read(new ByteArrayInputStream(output));
        if (isTablet) {
            SurfaceOrientation currentOrientation = SurfaceOrientation.ROTATION_0;
            try {
                currentOrientation = driver.getSurfaceOrientation();
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.debug(String.format("Current screen orientation value -> %s", currentOrientation.getCode()));
            resultImg = fixScreenshotOrientation(resultImg, currentOrientation);
            if (shouldApplyScale) {
                resultImg = ImageUtil.scaleTo(resultImg,
                        MAX_TABLET_SCREENSHOT_WIDTH, MAX_TABLET_SCREENSHOT_HEIGHT);
            }
        } else {
            if (shouldApplyScale) {
                resultImg = ImageUtil.scaleTo(resultImg,
                        MAX_PHONE_SCREENSHOT_WIDTH, MAX_PHONE_SCREENSHOT_HEIGHT);
            }
        }
        ImageUtil.storeImage(resultImg, resultScreenshot);
    }

    public static Optional<String> getRcTestsCommentPathFromCommonConfig(Class<?> c) throws Exception {
        return getOptionalValueFromCommonConfig(c, "rcTestsCommentPath");
    }

    private static class UIScriptExecutionMonitor implements Callable<Void> {

        private File flag;
        private File script;

        public UIScriptExecutionMonitor(File flag, File script) {
            this.flag = flag;
            this.script = script;
        }

        @Override
        public Void call() throws Exception {
            try {
                do {
                    Thread.sleep(300);
                } while (this.flag.exists());
                return null;
            } finally {
                this.script.delete();
            }
        }
    }

    /**
     * It is highly recommended to use these methods if it is necessary to interact with UI from a script. Otherwise it will be
     * blocked by Mac OS as unsecure, because only Terminal.app is explicitly authorized to interact with UI.
     *
     * @param content the full script content, WITHOUT shebang
     * @return monitoring Future. Use it to block execution until shell script execution is done
     * @throws Exception
     */
    public static Future<Void> executeUIShellScript(String[] content) throws Exception {
        final File result = File.createTempFile("script", ".sh");

        final File executionFlag = File.createTempFile("execution", ".flag");
        final List<String> scriptContent = new ArrayList<>();
        scriptContent.add("#!/bin/bash");
        Collections.addAll(scriptContent, content);
        scriptContent.add(String.format("rm -f %s", executionFlag.getCanonicalPath()));

        try (Writer output = new BufferedWriter(new FileWriter(result))) {
            output.write(String.join("\n", scriptContent));
        }
        Runtime.getRuntime().exec(new String[]{"chmod", "u+x", result.getCanonicalPath()}).waitFor();
        Runtime.getRuntime().exec(new String[]{"/usr/bin/open", "-a", "Terminal", result.getCanonicalPath(), "-g"})
                .waitFor();
        return Executors.newSingleThreadExecutor().submit(new UIScriptExecutionMonitor(executionFlag, result));
    }

    public static Future<Void> executeUIAppleScript(String[] content) throws Exception {
        final List<String> scriptContent = new ArrayList<>();
        scriptContent.add("/usr/bin/osascript \\");
        for (int idx = 0; idx < content.length; idx++) {
            if (idx < content.length - 1) {
                scriptContent.add(String.format("  -e '%s' \\", content[idx]));
            } else {
                scriptContent.add(String.format("  -e '%s'", content[idx]));
            }
        }
        String[] asArray = new String[scriptContent.size()];
        return executeUIShellScript(scriptContent.toArray(asArray));
    }

    private static final String TIME_SERVER = "time-a.nist.gov";

    public static Future<Long> getPreciseTime() throws Exception {
        final Callable<Long> task = () -> {
            final NTPUDPClient timeClient = new NTPUDPClient();
            final InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
            return new Date(timeClient.getTime(inetAddress).getReturnTime()).getTime();
        };
        return Executors.newSingleThreadExecutor().submit(task);
    }

    public static void cleanupOutdatedMavenSnapshots(File pluginJar) {
        try {
            if (!pluginJar.getCanonicalPath().contains(".m2")) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final File allVersionsRoot = pluginJar.getParentFile().getParentFile();
        final String currentLibVersion = pluginJar.getParentFile().getName();
        for (String versionRootName : allVersionsRoot.list()) {
            try {
                final File versionRoot = new File(allVersionsRoot.getCanonicalPath() + File.separator + versionRootName);
                if (versionRoot.isDirectory() && !versionRoot.getName().equals(currentLibVersion)) {
                    log.debug(String.format("Cleaning outdated SE library by path %s (current lib version is %s)...",
                            versionRoot.getAbsolutePath(), currentLibVersion));
                    FileUtils.deleteDirectory(versionRoot);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isRunningInJenkinsNetwork() throws UnknownHostException {
        final String prevPropValue = System.getProperty("java.net.preferIPv4Stack");
        try {
            try {
                System.setProperty("java.net.preferIPv4Stack", "true");
            } catch (Exception e) {
                // skip silently
            }
            return InetAddress.getLocalHost().getHostAddress().startsWith("192.168.2.");
        } finally {
            try {
                System.setProperty("java.net.preferIPv4Stack", prevPropValue);
            } catch (Exception e) {
                // skip silently
            }
        }
    }

    public static void setStringValueInSystemClipboard(String val) throws Exception {
        CommonUtils.executeUIAppleScript(new String[]{
                String.format("set the clipboard to \"%s\"", val)
        }).get(DEFAULT_COMMAND_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void pressCmdVByAppleScript() throws Exception {
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke \"v\" using {command down}"
        }).get(DEFAULT_COMMAND_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * Create Random Access File
     *
     * @param filePath the file path include the file name
     * @param size     the expected file size, such as 5MB, 10KB, or 4.00MB or 10.00KB or 10kb, 10Kb
     * @throws Exception
     */
    public static void createRandomAccessFile(String filePath, String size) throws Exception {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "rws")) {
            String[] sizeParts = size.split("(?<=\\d)(?=[a-zA-Z])");
            int fileSize = Double.valueOf(sizeParts[0]).intValue();
            String type = sizeParts.length > 1 ? sizeParts[1] : "";
            switch (type.toUpperCase()) {
                case "MB":
                    file.setLength(fileSize * 1024 * 1024);
                    break;
                case "KB":
                    file.setLength(fileSize * 1024);
                    break;
                default:
                    file.setLength(fileSize);
            }
        }
    }
}
