package com.wearezeta.auto.web.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import com.wearezeta.auto.common.driver.DriverUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import java.util.concurrent.TimeUnit;

public class WebCommonUtils extends CommonUtils {

    private static final Logger log = ZetaLogger.getLog(WebCommonUtils.class.getSimpleName());
    public static final String TMP_ROOT = "/tmp";

    public static final class Scripts {
        public static final String SAFARI_SEND_PICTURE_SCRIPT = "safari_choose_image.txt";
        public static final String SAFARI_OPEN_TAB_SCRIPT = "safari_open_tab.txt";
        public static final String SAFARI_CLOSE_ALL_ADDITIONAL_TABS_SCRIPT = "safari_close_all_additional_tabs.txt";
        public static final String SAFARI_CLEAR_HISTORY_SCRIPT = "safari_clear_history.txt";
        public static final String RESOURCES_SCRIPTS_ROOT = "scripts";
    }

    public static String getHubHostFromConfig(Class<?> c) throws Exception {
        return getValueFromConfig(c, "hubHost");
    }

    public static int getHubPortFromConfig(Class<?> c) throws Exception {
        return Integer.parseInt(getValueFromConfig(c, "hubPort"));
    }

    public static String getScriptsTemplatesPath() {
        return String.format("%s/Documents/scripts/",
                System.getProperty("user.home"));
    }

    public static String getPicturesPath() {
        return String.format("%s/Documents", System.getProperty("user.home"));
    }

    private static String getSshKeyPath() throws URISyntaxException {
        URI sshKeyUri = new URI(WebCommonUtils.class.getResource(
                "/ssh/jenkins_ssh_key").toString());

        return sshKeyUri.getPath();
    }

    public static String getFullPicturePath(String pictureName)
            throws URISyntaxException {
        String path = null;
        URL url = WebCommonUtils.class.getResource("/images/" + pictureName);
        if (url != null) {
            URI uri = new URI(url.toString());
            path = uri.getPath();
        }
        log.debug("Full picture path: " + path);
        return path;
    }

    public static String getFullFilePath(String fileName)
            throws URISyntaxException {
        String path = null;
        URL url = WebCommonUtils.class.getResource("/" + fileName);
        if (url != null) {
            URI uri = new URI(url.toString());
            path = uri.getPath();
        }
        log.debug("Full file path: " + path);
        return path;
    }

    public static String getTextFromFile(String fileName)
            throws URISyntaxException, IOException {
        String path = getFullFilePath(fileName);
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }

    public static void putFileOnExecutionNode(String node, File srcFile,
            String dstPath) throws Exception {
        setCorrectPermissionsOfKeyFile();
        // check if file exists
        assert srcFile.exists() : "There's no file by path "
                + srcFile.getCanonicalPath() + " on your local file system";

        String commandTemplate = "scp -i %s -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no "
                + "%s %s@%s:%s";
        String command = String.format(commandTemplate, getSshKeyPath(),
                srcFile.getCanonicalPath(),
                getJenkinsSuperUserLogin(CommonUtils.class), node, dstPath);
        WebCommonUtils
                .executeOsXCommand(new String[]{"bash", "-c", command});
    }

    private static void setCorrectPermissionsOfKeyFile()
            throws URISyntaxException {
        File keyFile = new File(getSshKeyPath());
        if (!keyFile.setReadable(false, false)
                || !keyFile.setReadable(true, true)) {
            log.info(String.format(
                    "Failed to make SSH File '%s' readable by owner",
                    getSshKeyPath()));
        }
        if (!keyFile.setWritable(false, false)
                || !keyFile.setWritable(true, true)) {
            log.info(String.format(
                    "Failed to make SSH File '%s' writable by owner",
                    getSshKeyPath()));
        }
    }

    public static void executeCommandOnNode(String node, String cmd)
            throws Exception {
        setCorrectPermissionsOfKeyFile();
        String commandTemplate = "ssh -i %s -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no "
                + "%s@%s %s";
        String command = String.format(commandTemplate, getSshKeyPath(),
                getJenkinsSuperUserLogin(CommonUtils.class), node, cmd);
        WebCommonUtils
                .executeOsXCommand(new String[]{"bash", "-c", command});
    }

    public static void executeAppleScriptFileOnNode(String node,
            String scriptPath) throws Exception {
        setCorrectPermissionsOfKeyFile();
        String commandTemplate = "ssh -i %s -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no "
                + "%s@%s osascript %s";
        String command = String.format(commandTemplate, getSshKeyPath(),
                getJenkinsSuperUserLogin(CommonUtils.class), node, scriptPath);
        if (WebCommonUtils.executeOsXCommand(new String[]{"bash", "-c",
            command}) == 255) {
            WebCommonUtils.executeOsXCommand(new String[]{"bash", "-c",
                command});
        }
    }

    private static void formatTextInFileAndSave(InputStream fis,
            String dstFile, Object[] params) throws IOException {
        String script = "";
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String t;
            while ((t = br.readLine()) != null) {
                if (!t.trim().isEmpty()) {
                    script += t + "\n";
                }
            }
            script = String.format(script, params);
            File dstFileInstance = new File(dstFile);
            if (dstFileInstance.exists()) {
                dstFileInstance.delete();
            }
            dstFileInstance.createNewFile();
            PrintWriter out = new PrintWriter(dstFile);
            out.write(script);
            out.close();
        } finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
        }
    }

    public static String getOperaProfileRoot() throws Exception {
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            return String
                    .format("C:\\Users\\%s\\AppData\\Roaming\\Opera Software\\Opera Stable\\",
                            CommonUtils
                                    .getJenkinsSuperUserLogin(WebCommonUtils.class));
        } else {
            // Should be Mac OS otherwise ;)
            return String
                    .format("/Users/%s/Library/Application Support/Opera Software/Opera Stable/",
                            CommonUtils
                                    .getJenkinsSuperUserLogin(WebCommonUtils.class));
        }
    }

    public static boolean isElementFocused(RemoteWebDriver driver, WebElement element) {
        final String isFocusedScript = "return arguments[0] === document.activeElement;";
        return (Boolean) driver.executeScript(isFocusedScript, element);
    }

    public static void setFocusToElement(RemoteWebDriver driver, WebElement element) {
        final String setFocusScript = "arguments[0].focus();";
        driver.executeScript(setFocusScript, element);
    }

    private static void openNewTabInSafari(String url, String nodeIp)
            throws Exception {
        final ClassLoader classLoader = WebCommonUtils.class.getClassLoader();
        final InputStream scriptStream = classLoader.getResourceAsStream(String
                .format("%s/%s",
                        Scripts.RESOURCES_SCRIPTS_ROOT,
                        Scripts.SAFARI_OPEN_TAB_SCRIPT));
        final String srcScriptPath = String.format("%s/%s",
                TMP_ROOT,
                Scripts.SAFARI_OPEN_TAB_SCRIPT);
        URI uri = new URI(WebCommonUtils.class.getResource(srcScriptPath)
                .toString());
        File srcScript = new File(uri.getPath());
        try {
            formatTextInFileAndSave(scriptStream, srcScriptPath,
                    new String[]{url});
        } finally {
            if (scriptStream != null) {
                scriptStream.close();
            }
        }
        final String dstScriptPath = srcScriptPath;
        try {
            putFileOnExecutionNode(nodeIp, srcScript, dstScriptPath);
        } finally {
            new File(srcScriptPath).delete();
        }

        executeAppleScriptFileOnNode(nodeIp, dstScriptPath);
    }

    private static Set<String> previousHandles = null;

    /**
     * Opens a new tab for the given URL
     *
     * http://stackoverflow.com/questions/6421988/webdriver-open-new-tab
     * https://code.google.com/p/selenium/issues/detail?id=7518
     *
     * @param url The URL to
     * @throws Exception
     * @throws RuntimeException If unable to open tab
     */
    public static void openUrlInNewTab(RemoteWebDriver driver, String url,
            String nodeIp) throws Exception {
        previousHandles = driver.getWindowHandles();
        if (WebAppExecutionContext.getBrowser() == Browser.Safari) {
            openNewTabInSafari(url, nodeIp);
        } else {
            String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
            Object element = driver.executeScript(String.format(script, url));
            if (element instanceof WebElement) {
                WebElement anchor = (WebElement) element;
                anchor.click();
                driver.executeScript(
                        "var a=arguments[0];a.parentNode.removeChild(a);",
                        element);
            } else {
                throw new RuntimeException("Unable to open a new tab");
            }
        }
        Set<String> currentHandles = driver.getWindowHandles();
        if (previousHandles.equals(currentHandles)) {
            throw new RuntimeException("Unable to open a new tab");
        }
        currentHandles.removeAll(previousHandles);
        final String newTabHandle = currentHandles.iterator().next();
        driver.switchTo().window(newTabHandle);
    }

    public static void switchToPreviousTab(RemoteWebDriver driver) {
        Set<String> currentHandles = driver.getWindowHandles();
        if (previousHandles.equals(currentHandles)) {
            return;
        }
        currentHandles.retainAll(previousHandles);
        final String oldTabHandle = currentHandles.iterator().next();
        driver.switchTo().window(oldTabHandle);
    }

    /**
     * Workaround for https://code.google.com/p/selenium/issues/detail?id=4220
     *
     * @param pictureName the path to the original picture to be uploaded into conversation
     * @throws Exception
     */
    public static void sendPictureInSafari(String pictureName, String nodeIp)
            throws Exception {
        // send picture through ssh
        final File srcPicture = new File(pictureName);
        putFileOnExecutionNode(nodeIp, srcPicture,
                "/tmp/" + srcPicture.getName());

        // check if script is really there
        String genericScriptPath = String.format("/%s/%s",
                Scripts.RESOURCES_SCRIPTS_ROOT,
                Scripts.SAFARI_SEND_PICTURE_SCRIPT);
        InputStream genericScript = null;
        try {
            genericScript = WebCommonUtils.class
                    .getResourceAsStream(genericScriptPath);
        } catch (NullPointerException e) {
            throw new AssertionError("There's no script by path "
                    + genericScriptPath + " in your resources");
        }

        // create individual oascript
        final String individualScriptName = "upload_" + srcPicture.getName()
                + ".txt";
        File individualScript = new File(individualScriptName);
        final File dstPicture = new File(String.format("%s/%s",
                TMP_ROOT, srcPicture.getName()));
        try {
            formatTextInFileAndSave(
                    genericScript,
                    individualScriptName,
                    new String[]{dstPicture.getParent(), dstPicture.getName()});
        } finally {
            if (genericScript != null) {
                genericScript.close();
            }
        }

        // send script and execute through ssh
        final String dstScriptPath = "/tmp/" + individualScriptName;
        try {
            putFileOnExecutionNode(nodeIp, individualScript, dstScriptPath);
        } finally {
            new File(individualScriptName).delete();
        }
        executeAppleScriptFileOnNode(nodeIp, dstScriptPath);
    }

    public static void loadCustomJavascript(RemoteWebDriver driver,
            String scriptContent) {
        final String[] loaderJS = new String[]{
            "var scriptElt = document.createElement('script');",
            "scriptElt.type = 'text/javascript';",
            "scriptElt.innerHTML = '"
            + StringEscapeUtils.escapeEcmaScript(scriptContent)
            + "';", "$('head').append(scriptElt);"};
        driver.executeScript(StringUtils.join(loaderJS, "\n"));
    }

    public static void clearHistoryInSafari(String nodeIp) throws Exception {
        final String srcScriptPath = String.format("/%s/%s",
                Scripts.RESOURCES_SCRIPTS_ROOT,
                Scripts.SAFARI_CLEAR_HISTORY_SCRIPT);
        final String dstScriptPath = String.format("%s/%s",
                TMP_ROOT,
                Scripts.SAFARI_CLEAR_HISTORY_SCRIPT);
        // get file via resources
        URL url = WebCommonUtils.class.getResource(srcScriptPath);
        assert url != null : "There's no file by path " + srcScriptPath
                + " on your resources";
        File srcScript = new File(new URI(url.toString()).getPath());
        putFileOnExecutionNode(nodeIp, srcScript, dstScriptPath);
        executeAppleScriptFileOnNode(nodeIp, dstScriptPath);
    }

    public static void closeAllAdditionalTabsInSafari(String nodeIp)
            throws Exception {
        final String srcScriptPath = String
                .format("/%s/%s",
                        Scripts.RESOURCES_SCRIPTS_ROOT,
                        Scripts.SAFARI_CLOSE_ALL_ADDITIONAL_TABS_SCRIPT);
        final String dstScriptPath = String
                .format("%s/%s",
                        TMP_ROOT,
                        Scripts.SAFARI_CLOSE_ALL_ADDITIONAL_TABS_SCRIPT);
        // get file via resources
        URL url = WebCommonUtils.class.getResource(srcScriptPath);
        assert url != null : "There's no file by path " + srcScriptPath
                + " on your resources";
        File srcScript = new File(new URI(url.toString()).getPath());
        putFileOnExecutionNode(nodeIp, srcScript, dstScriptPath);
        executeAppleScriptFileOnNode(nodeIp, dstScriptPath);
    }

    public static void hoverOverElement(RemoteWebDriver driver, WebElement element) {
        if (WebAppExecutionContext.getBrowser().isSupportingNativeMouseActions()) {
            // native mouse over
            DriverUtils.moveMouserOver(driver, element);
        } else {
            driver.executeScript("var evt = new MouseEvent('mouseover', {view: window});"
                    + "arguments[0].dispatchEvent(evt);", element);
        }
    }

    public static void highlightElement(RemoteWebDriver driver, WebElement element, int duration, TimeUnit unit) {
        if (WebAppExecutionContext.getBrowser().isSupportingAccessToJavascriptContext()) {
            String original_style = element.getAttribute("style");
            driver.executeScript(
                    "arguments[0].setAttribute(arguments[1], arguments[2])",
                    element, "style", "border: 2px solid red; border-style: dashed;");
            if (duration > 0) {
                try {
                    Thread.sleep(unit.toMillis(duration));
                } catch (InterruptedException ex) {
                }
                driver.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])",
                        element, "style", original_style);
            }
        }
    }

    /**
     * The UI uses left zero padding for device IDs but for internal processing we need a format without leading zeros. Note:
     * The RemoteDevicesManager is returning device IDs with left zero padding as well
     *
     * @param deviceId device ID with leading zeros
     * @return device ID without leading zeros
     */
    public static String removeDeviceIdPadding(String deviceId) {
        // we have to strip leading zeros since we don't want to use the padding for UI
        int limit = deviceId.length();
        while (deviceId.startsWith("0") && limit >= 0) {
            deviceId = deviceId.substring(1);
            limit--;
        }
        return deviceId;
    }
}
