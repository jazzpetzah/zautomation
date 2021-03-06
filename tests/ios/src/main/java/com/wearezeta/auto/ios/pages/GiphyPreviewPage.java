package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class GiphyPreviewPage extends IOSPage {
    // TODO: assign a name to Giphy image element
    private static final By classImagePreview = By.className("XCUIElementTypeImage");

    private static final By fbClassCellPreview = FBBy.className("XCUIElementTypeCell");

    private static final By nameCancelButton = MobileBy.AccessibilityId("CANCEL");

    public static final By nameSendButton = MobileBy.AccessibilityId("SEND");

    private static final By fbNamePreviewGrid = FBBy.AccessibilityId("giphyCollectionView");

    public GiphyPreviewPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    public boolean isPreviewVisible() throws Exception {
        return selectVisibleElements(classImagePreview).size() > 0;
    }

    public boolean isGridVisible() throws Exception {
        return isLocatorDisplayed(fbNamePreviewGrid);
    }

    public void selectFirstItem() throws Exception {
        getElement(getElement(fbNamePreviewGrid), fbClassCellPreview).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    private By getButtonByName(String name) {
        switch (name.toLowerCase()) {
            case "send":
                return nameSendButton;
            case "cancel":
                return nameCancelButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s' on Giphy preview page",
                        name));
        }
    }

    public void tapButton(String btnName) throws Exception {
        final By locator = getButtonByName(btnName);
        getElement(locator).click();
    }

    public boolean isButtonVisible(String btnName) throws Exception {
        final By locator = getButtonByName(btnName);
        return isLocatorDisplayed(locator);
    }
}