package com.wearezeta.auto.ios.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BaseUserDetailsOverlay extends BaseDetailsOverlay {
    protected static final By nameXButton = MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    private static final By nameRightActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final By nameLeftActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerLeftButton");

    public BaseUserDetailsOverlay(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    protected void switchToTab(String tabName) throws Exception {
        getElement(MobileBy.AccessibilityId(tabName.toUpperCase())).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    public boolean isNameVisible(String value) throws Exception {
        final By locator = MobileBy.AccessibilityId(value);
        return selectVisibleElements(locator).size() > 0;
    }

    public boolean isNameInvisible(String value) throws Exception {
        final By locator = MobileBy.AccessibilityId(value);
        return selectVisibleElements(locator).size() == 0;
    }

    public boolean isEmailVisible(String value) throws Exception {
        final By locator = MobileBy.AccessibilityId(value.toUpperCase());
        return selectVisibleElements(locator).size() > 0;
    }

    public boolean isEmailInvisible(String value) throws Exception {
        final By locator = MobileBy.AccessibilityId(value.toUpperCase());
        return selectVisibleElements(locator).size() == 0;
    }

    @Override
    protected By getLeftActionButtonLocator() {
        return nameLeftActionButton;
    }

    @Override
    protected By getRightActionButtonLocator() {
        return nameRightActionButton;
    }
}
