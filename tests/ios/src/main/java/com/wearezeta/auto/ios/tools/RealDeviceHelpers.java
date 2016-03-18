package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.wearezeta.auto.common.CommonUtils.getIOSToolsRoot;

public class RealDeviceHelpers {
    private static Logger log = ZetaLogger.getLog(RealDeviceHelpers.class.getSimpleName());


    private static final int APP_UNINSTALL_TIMEOUT_SECONDS = 10;
    private static final int DEVICE_RECONNECT_TIMEOUT_SECONDS = 30;

    /**
     * This will return only UDID of the first connected iDevice
     *
     * @return udid string
     * @throws Exception
     */
    public static Optional<String> getUDID() throws Exception {
        for (String deviceName : new String[]{"iPhone", "iPad"}) {
            final String result = CommonUtils.executeOsXCommandWithOutput(new String[]{
                    "/bin/bash",
                    "-c",
                    "system_profiler SPUSBDataType | sed -n '/"
                            + deviceName
                            + "/,/Serial/p' | grep 'Serial Number:' | awk -F ': ' '{print $2}'"}).trim();
            if (result.length() > 0) {
                return Optional.of(result);
            }
        }
        return Optional.empty();
    }

    private static void waitUntilIsConnected(String udid, int timeoutSeconds) throws Exception {
        log.debug(String.format("Waiting %s seconds until the device is connected...",
                DEVICE_RECONNECT_TIMEOUT_SECONDS));
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000) {
            final Process p = new ProcessBuilder(
                    "/usr/local/bin/idevicediagnostics", "-u", udid, "diagnostics", "WiFi"
            ).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start();
            final int retCode = p.waitFor();
            if (retCode == 0) {
                return;
            } else {
                Thread.sleep(2000);
            }
        }
        throw new IllegalStateException(String.format(
                "iOS device with UDID %s has NOT been successfully reconnected after %s seconds", udid, timeoutSeconds));
    }

    private static final String RECONNECT_DEVICE_SCRIPT_NAME = "reconnectIDevice.py";

    public static void uninstallApp(final String udid, final String bundleId) throws Exception {
        final Process p = new ProcessBuilder(
                "/usr/local/bin/ideviceinstaller", "-u", udid, "-U", bundleId
        ).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start();
        if (!p.waitFor(APP_UNINSTALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            // FIXME: Workaround for https://github.com/appium/appium/issues/5039
            p.destroy();

            if (CommonUtils.isRunningInJenkinsNetwork()) {
                log.warn("Seems like ideviceinstaller has frozen. Trying to reconnect the IDevice to its VM...");
                new ProcessBuilder("/usr/bin/python",
                        getIOSToolsRoot(IOSSimulatorHelper.class) + File.separator + RECONNECT_DEVICE_SCRIPT_NAME
                ).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start().waitFor();
                waitUntilIsConnected(udid, DEVICE_RECONNECT_TIMEOUT_SECONDS);
                final Process p1 = new ProcessBuilder(
                        "/usr/local/bin/ideviceinstaller", "-u", udid, "-U", bundleId
                ).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start();
                if (!p1.waitFor(APP_UNINSTALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("ideviceinstaller has failed to perform application uninstall.\n" +
                            "Please try to reconnect the device.");
                }
            } else {
                throw new IllegalStateException("ideviceinstaller has failed to perform application uninstall.\n" +
                        "Please try to reconnect the device.");
            }
        }
    }
}
