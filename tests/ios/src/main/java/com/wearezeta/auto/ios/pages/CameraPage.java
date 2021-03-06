package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.device_helpers.IOSSimulatorHelpers;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class CameraPage extends IOSPage {

    private static final By xpathCameraRollButton =
            By.xpath("//XCUIElementTypeButton[@name='cameraLibraryButton' or @name='CameraLibraryButton']");

    private static final By xpathTakePhotoButton =
            By.xpath("//XCUIElementTypeButton[@name='cameraShutterButton' or @name='cameraButton' or @name='PhotoCapture']");

    private static final By nameCloseButton = MobileBy.AccessibilityId("cameraCloseButton");

    private boolean isTestImageUploaded = false;

    public CameraPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private By getButtonByName(String name) {
        switch (name.toLowerCase()) {
            case "take photo":
                return xpathTakePhotoButton;
            case "camera roll":
                return xpathCameraRollButton;
            case "close":
                return nameCloseButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonByName(name);
        if (!isTestImageUploaded && locator.equals(xpathCameraRollButton) &&
                CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelpers.uploadImage();
            isTestImageUploaded = true;
        }
        getElement(getButtonByName(name)).click();
    }

    public boolean isButtonVisible(String name) throws Exception {
        final By locator = getButtonByName(name);
        return isLocatorDisplayed(locator);
    }
}
