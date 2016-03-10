package com.wearezeta.auto.ios.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import static com.wearezeta.auto.common.CommonUtils.getDeviceName;
import static com.wearezeta.auto.common.CommonUtils.getIOSToolsRoot;

public class IOSSimulatorHelper {
    public static final int SIMULATOR_INTERACTION_TIMEOUT = 3 * 60; //seconds

    private static Logger log = ZetaLogger.getLog(IOSSimulatorHelper.class.getSimpleName());

    public IOSSimulatorHelper() {
    }

    private static final String SWIPE_SCRIPT_NAME = "swipeInWindow.py";
    private static final String CLICK_SCRIPT_NAME = "clickInWindow.py";

    /**
     * @param startX 0 <= startX <= 1
     * @param startY 0 <= startY <= 1
     * @param endX   0 <= endX <= 1
     * @param endY   0 <= endY <= 1
     * @throws Exception
     */
    public static void swipe(double startX, double startY, double endX, double endY) throws Exception {
        CommonUtils.executeUIShellScript(new String[]{
                String.format("/usr/bin/python '%s/%s' %.2f %.2f %.2f %.2f",
                        getIOSToolsRoot(IOSSimulatorHelper.class), SWIPE_SCRIPT_NAME,
                        startX, startY, endX, endY)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * @param startX         0 <= startX <= 1
     * @param startY         0 <= startY <= 1
     * @param endX           0 <= endX <= 1
     * @param endY           0 <= endY <= 1
     * @param durationMillis swipe duration in milliseconds
     * @throws Exception
     */
    public static void swipe(double startX, double startY, double endX, double endY,
                             long durationMillis) throws Exception {
        CommonUtils.executeUIShellScript(new String[]{
                String.format("/usr/bin/python '%s/%s' %.2f %.2f %.2f %.2f %d",
                        getIOSToolsRoot(IOSSimulatorHelper.class), SWIPE_SCRIPT_NAME,
                        startX, startY, endX, endY, durationMillis)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void swipeDown() throws Exception {
        swipe(0.8, 0.2, 0.8, 0.95);
    }

    public static void swipeRight() throws Exception {
        swipe(0.2, 0.8, 0.9, 0.8);
    }

    public static void clickAt(String relativeX, String relativeY, String durationSeconds) throws Exception {
        CommonUtils.executeUIShellScript(new String[]{
                String.format("/usr/bin/python '%s/%s' %s %s %s",
                        getIOSToolsRoot(IOSSimulatorHelper.class), CLICK_SCRIPT_NAME, relativeX, relativeY, durationSeconds)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * Type a string using keyboard and avoiding autocorrection
     *
     * @param str the string to enter
     * @throws Exception
     */
    public static void typeString(String str, boolean useAutocompleteWorkaround) throws Exception {
        activateWindow();
        String[] script;
        if (useAutocompleteWorkaround) {
            // FIXME: Quote string for bash script
            script = new String[]{
                    "str_to_enter='" + str + "'",
                    "for (( i=0; i<${#str_to_enter}; i++ )); do",
                    "    current_char=\"${str_to_enter:$i:1}\"",
                    "    /usr/bin/osascript \\",
                    "         -e \"tell application \\\"System Events\\\" to keystroke \\\"${current_char}\\\"\" \\",
                    "    sleep 0.3",
                    "    /usr/bin/osascript -e 'tell application \"System Events\" to key code 53'",
                    "done",
                    "sleep 2",
            };
        } else {
            script = new String[]{
                    "str_to_enter='" + str + "'",
                    "for (( i=0; i<${#str_to_enter}; i++ )); do",
                    "    current_char=\"${str_to_enter:$i:1}\"",
                    "    /usr/bin/osascript \\",
                    "         -e \"tell application \\\"System Events\\\" to keystroke \\\"${current_char}\\\"\" \\",
                    "    sleep 0.3",
                    "done",
                    "sleep 2",
            };
        }
        CommonUtils.executeUIShellScript(script).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void typeStringAndPressEnter(String str, boolean useAutocompleteWorkaround) throws Exception {
        activateWindow();
        String[] script;
        if (useAutocompleteWorkaround) {
            // FIXME: Quote string for bash script
            script = new String[]{
                    "str_to_enter='" + str + "'",
                    "for (( i=0; i<${#str_to_enter}; i++ )); do",
                    "    current_char=\"${str_to_enter:$i:1}\"",
                    "    /usr/bin/osascript \\",
                    "         -e \"tell application \\\"System Events\\\" to keystroke \\\"${current_char}\\\"\" \\",
                    "    sleep 0.3",
                    "    /usr/bin/osascript -e 'tell application \"System Events\" to key code 53'",
                    "done",
                    "/usr/bin/osascript -e 'tell application \"System Events\" to keystroke return'",
                    "sleep 2",
            };
        } else {
            script = new String[]{
                    "str_to_enter='" + str + "'",
                    "for (( i=0; i<${#str_to_enter}; i++ )); do",
                    "    current_char=\"${str_to_enter:$i:1}\"",
                    "    /usr/bin/osascript \\",
                    "         -e \"tell application \\\"System Events\\\" to keystroke \\\"${current_char}\\\"\" \\",
                    "    sleep 0.3",
                    "done",
                    "/usr/bin/osascript -e 'tell application \"System Events\" to keystroke return'",
                    "sleep 2",
            };
        }
        CommonUtils.executeUIShellScript(script).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    private static void activateWindow() throws Exception {
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to tell application process \"Simulator\"",
                "set frontmost to false",
                "set frontmost to true",
                "end tell"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
        // To make sure the window is really activated
        Thread.sleep(500);
    }

    public static void switchAppsList() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\"",
                "keystroke \"h\" using {command down, shift down}",
                "keystroke \"h\" using {command down, shift down}",
                "end tell"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void lock() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke \"l\" using {command down}"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void goHome() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke \"h\" using {command down, shift down}"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void pressEnterKey() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke return"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void toggleSoftwareKeyboard() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke \"k\" using {command down}"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static String getId() throws Exception {
        return CommonUtils.executeOsXCommandWithOutput(new String[]{
                "/bin/bash",
                "-c",
                "xcrun simctl list devices | grep -v 'unavailable' | grep -i '"
                        + getDeviceName(IOSSimulatorHelper.class)
                        + " (' | tail -n 1 | cut -d '(' -f2 | cut -d ')' -f1"}).trim();
    }

    private final static String APP_CRASHES_MARKER = "Wire_";
    private final static String USED_APP_CRASHES_MARKER = "_";

    private static String getRecentWireCrashReports() throws Exception {
        final StringBuilder result = new StringBuilder();
        final File reportsRoot = new File(String.format("%s/Library/Logs/DiagnosticReports",
                System.getProperty("user.home")));
        if (reportsRoot.exists() && reportsRoot.isDirectory()) {
            final File[] allFiles = reportsRoot.listFiles();
            for (File f : allFiles) {
                if (f.isFile() && f.getName().endsWith(".crash") && f.getName().startsWith(APP_CRASHES_MARKER)) {
                    result.append(new String(Files.readAllBytes(f.toPath()), Charset.forName("UTF-8"))).append("\n\n");
                    final File newPath = new File(
                            String.format("%s/%s%s", f.getParent(), USED_APP_CRASHES_MARKER, f.getName()));
                    if (newPath.exists()) {
                        newPath.delete();
                    }
                    if (!f.renameTo(newPath)) {
                        f.delete();
                    }
                }
            }
        }
        return result.toString();
    }

    public static String getLogsAndCrashes() throws Exception {
        final String simId = getId();
        final File logFile = new File(String.format("%s/Library/Logs/CoreSimulator/%s/system.log",
                System.getProperty("user.home"), simId));
        final StringBuilder result = new StringBuilder();
        if (logFile.exists()) {
            result.append(new String(Files.readAllBytes(logFile.toPath()), Charset.forName("UTF-8")));
        }
        final String crashReports = getRecentWireCrashReports();
        if (!crashReports.isEmpty()) {
            result.append("\n\n\n\n\n").append(crashReports);
        }
        return result.toString();
    }

    private static final String XCRUN_PATH = "/usr/bin/xcrun";
    private static final int XCRUN_TIMEOUT_SECONDS = 60;

    private static String executeSimctl(String[] cmd) throws Exception {
        final String[] firstCmdPart = new String[]{XCRUN_PATH, "simctl"};
        final String[] fullCmd = ArrayUtils.addAll(firstCmdPart, cmd);
        log.debug(String.format("Executing: %s", Arrays.toString(fullCmd)));
        final Process process = new ProcessBuilder(fullCmd).redirectErrorStream(true).start();
        process.waitFor(XCRUN_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        final StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        final String output = builder.toString().trim();
        log.debug(String.format("Command output: %s", output));
        return output;
    }

    public static void kill() throws Exception {
        log.debug("Force killing Simulator app...");
        new ProcessBuilder("/usr/bin/killall", "-9", "Simulator").start().waitFor();
        Thread.sleep(2000);
    }

    public static void reset() throws Exception {
        kill();
        executeSimctl(new String[]{"erase", getId()});
    }


    public static void installApp(File appPath) throws Exception {
        executeSimctl(new String[]{"install", getId(), appPath.getCanonicalPath()});
    }

    public static void installIpa(File ipaPath) throws Exception {
        final File app = IOSCommonUtils.extractAppFromIpa(ipaPath);
        try {
            installApp(app);
        } finally {
            FileUtils.deleteDirectory(app);
        }
    }
}
