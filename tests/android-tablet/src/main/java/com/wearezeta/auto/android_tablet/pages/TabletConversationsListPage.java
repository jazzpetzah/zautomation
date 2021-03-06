package com.wearezeta.auto.android_tablet.pages;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.ConversationsListPage;
import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConversationsListPage extends AndroidTabletPage {
    private static final int PLAY_PAUSE_BUTTON_WIDTH_PERCENTAGE = 15;

    public TabletConversationsListPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private ConversationsListPage getConversationListPage() throws Exception {
        return this.getAndroidPageInstance(ConversationsListPage.class);
    }

    public void verifyConversationsListIsLoaded() throws Exception {
        if (AndroidTabletTestContextHolder.getInstance().getTestContext().getScreenOrientationHelper()
                .fixOrientation(getDriver()) == ScreenOrientation.PORTRAIT) {
            try {
                DriverUtils.swipeByCoordinates(getDriver(), 1500, 45, 50, 90, 50);
                getConversationListPage().verifyContactListIsFullyLoaded();
            } catch (IllegalStateException e) {
                DriverUtils.swipeByCoordinates(getDriver(), 1500, 45, 50, 90, 50);
                getConversationListPage().verifyContactListIsFullyLoaded();
            }
        }
    }

    public boolean waitUntilConversationIsVisible(String name) throws Exception {
        final By locator = By.xpath(ConversationsListPage.xpathStrContactByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 40);
    }

    public boolean waitUntilConversationIsInvisible(String name) throws Exception {
        final By locator = By.xpath(ConversationsListPage.xpathStrContactByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapConversation(String name) throws Exception {
        final By locator = By.xpath(ConversationsListPage.xpathStrContactByName.apply(name));
        getElement(locator,
                String.format("The conversation '%s' does not exist in the conversations list", name)).click();
    }

    public boolean waitUntilConversationIsSilenced(String name) throws Exception {
        final By locator = By.xpath(ConversationsListPage.xpathStrMutedIconByConvoName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilConversationIsNotSilenced(String name) throws Exception {
        final By locator = By.xpath(ConversationsListPage.xpathStrMutedIconByConvoName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isAnyConversationVisible() throws Exception {
        return getConversationListPage().isAnyConversationVisible();
    }

    public boolean isNoConversationsVisible() throws Exception {
        return getConversationListPage().isNoConversationsVisible();
    }

    public boolean waitUntilMissedCallNotificationVisible(String convoName) throws Exception {
        return getConversationListPage().waitUntilMissedCallNotificationVisible(convoName);
    }

    public boolean waitUntilMissedCallNotificationInvisible(String convoName) throws Exception {
        return getConversationListPage().waitUntilMissedCallNotificationInvisible(convoName);
    }

    public void doSwipeLeft() throws Exception {
        DriverUtils.swipeByCoordinates(getDriver(), 1000, 95, 50, 10, 50);
    }

    public boolean waitUntilPlayPauseButtonVisibleNextTo(String convoName) throws Exception {
        try {
            return getConversationListPage().isPlayPauseMediaButtonVisible(convoName);
        } catch (InvalidElementStateException e) {
            // Workaround for Selendroid (or application) bug
            return true;
        }
    }

    public Optional<BufferedImage> getScreenshotOfPlayPauseButton(Rectangle elementCoord) throws Exception {
        final BufferedImage fullScreenshot = this.takeScreenshot().orElseThrow(IllegalStateException::new);
        return Optional.of(fullScreenshot.getSubimage(elementCoord.x,
                elementCoord.y, elementCoord.width, elementCoord.height));
    }

    public void tapPlayPauseMediaButton(Rectangle elementCoord) throws Exception {
        this.getDriver().tap(1, elementCoord.x + elementCoord.width / 2,
                elementCoord.y + elementCoord.height / 2, 1);
    }

    public Rectangle calcPlayPauseButtonCoordinates(String convoName) throws Exception {
        final Rectangle result = new Rectangle();
        final WebElement convoElement = getElement(By.xpath(ConversationsListPage.xpathStrContactByName.apply(convoName)));
        final int playPauseButtonWidth = convoElement.getSize().width * PLAY_PAUSE_BUTTON_WIDTH_PERCENTAGE / 100;
        result.setLocation(convoElement.getLocation().x + convoElement.getSize().width
                - playPauseButtonWidth, convoElement.getLocation().y);
        result.setSize(playPauseButtonWidth, convoElement.getSize().height);
        return result;
    }

    public void doLongSwipeUp() throws Exception {
        getConversationListPage().doLongSwipeUp();
    }

    public void tapListActionsAvatar() throws Exception {
        getConversationListPage().tapListActionsAvatar();
    }

    public void tapListSettingsButton() throws Exception {
        getConversationListPage().tapListSettingsButton();
    }

    public void longTapOnName(final String name, int durationMilliseconds) throws Exception {
        getConversationListPage().longTapOnName(name, durationMilliseconds);
    }
}
