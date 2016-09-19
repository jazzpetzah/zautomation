package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class VideoPlayerPage extends IOSPage {
    private static final By xpathVideoMainPage = By.className("WebView");

    private static final By nameVideoDoneButton = MobileBy.AccessibilityId("Done");
    private static final By fbNameVideoDoneButton = FBBy.FBAccessibilityId("Done");

    private static final By nameVideoPauseButton = MobileBy.AccessibilityId("PauseButton");

    public VideoPlayerPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVideoPlayerPageOpened() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), xpathVideoMainPage);
    }

    public void tapVideoPage() throws Exception {
        getElement(xpathVideoMainPage).click();
    }

    public void clickVideoDoneButton() throws Exception {
        final FBElement videoDoneButton = (FBElement) getElement(fbNameVideoDoneButton);
        this.tapAtTheCenterOfElement(videoDoneButton);
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), fbNameVideoDoneButton, 5)) {
            this.tapAtTheCenterOfElement(videoDoneButton);
        }
    }

    public void clickPauseButton() throws Exception {
        final Optional<WebElement> videoPauseButton = getElementIfDisplayed(nameVideoPauseButton);
        if (videoPauseButton.isPresent()) {
            videoPauseButton.get().click();
        } else {
            tapVideoPage();
            getElement(nameVideoPauseButton).click();
        }
    }

    public boolean isVideoMessagePlayerPageDoneButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), nameVideoDoneButton, 20);
    }
}
