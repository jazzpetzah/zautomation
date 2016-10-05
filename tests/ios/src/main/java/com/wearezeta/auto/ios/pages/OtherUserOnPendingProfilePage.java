package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class OtherUserOnPendingProfilePage extends IOSPage {

    private static final By xpathOtherProfileCancelRequestButton =
            By.xpath("//XCUIElementTypeButton[@label='CANCEL REQUEST']");

    private static final By xpathCancelRequestYesButton = By.xpath(
            "//XCUIElementTypeStaticText[@name='Cancel Request?']/following::XCUIElementTypeButton[@name='YES']");

    private static final By nameRightActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final By nameLeftActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerLeftButton");

    private static final By nameOtherProfilePageCloseButton = MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    private static final By nameConnectConfirmButton = MobileBy.AccessibilityId("CONNECT");

    public OtherUserOnPendingProfilePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isClosePageButtonVisible() throws Exception {
        return isElementDisplayed(nameOtherProfilePageCloseButton);
    }

    public boolean isCancelRequestButtonVisible() throws Exception {
        return isElementDisplayed(xpathOtherProfileCancelRequestButton);
    }

    public void tapCancelRequestButton() throws Exception {
        getElement(xpathOtherProfileCancelRequestButton).click();
    }

    public void confirmCancelRequest() throws Exception {
        getElement(xpathCancelRequestYesButton).click();
    }

    public void tapStartConversationButton() throws Exception {
        getElement(nameLeftActionButton).click();
    }

    public boolean isUserNameDisplayed(String name) throws Exception {
        return isElementDisplayed(MobileBy.AccessibilityId(name), 10);
    }

    public boolean isRemoveFromGroupConversationVisible() throws Exception {
        return isElementDisplayed(nameRightActionButton);
    }

    public void tapConnectButton() throws Exception {
        getElement(nameLeftActionButton).click();
    }

    public void confirmConnect() throws Exception {
        getElement(nameConnectConfirmButton).click();
    }

    public void tapCloseButton() throws Exception {
        getElement(nameOtherProfilePageCloseButton).click();
    }
}
