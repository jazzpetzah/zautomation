package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class GroupChatPage extends DialogPage {
    @FindBy(how = How.XPATH, using = IOSLocators.xpathNewGroupConversationNameChangeTextField)
    private WebElement newGroupConversationNameChangeTextField;

    @FindBy(how = How.NAME, using = IOSLocators.nameYouHaveLeft)
    private WebElement youLeft;

    @FindBy(how = How.NAME, using = IOSLocators.nameYouLeftMessage)
    private WebElement youLeftMessage;

    @FindBy(how = How.NAME, using = IOSLocators.nameYouRenamedConversationMessage)
    private WebElement yourRenamedMessage;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathStartConversationAfterDelete)
    private WebElement startConvAfterDeleteMessage;

    public GroupChatPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean areRequiredContactsAddedToChat(List<String> names) {
        final String lastMsg = getStartedtChatMessage();
        for (String name : names) {
            if (!lastMsg.toLowerCase().contains(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public boolean areContactsAddedAfterDeleteContent(List<String> names) {
        final String lastMsg = getStartedtChatMessageAfterDeleteContent();
        for (String name : names) {
            if (!lastMsg.toLowerCase().contains(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    private String getStartedtChatMessageAfterDeleteContent() {
        return startConvAfterDeleteMessage.getText();
    }

    public boolean areRequired3ContactsAddedToChat(String name1, String name2,
                                                   String name3) {
        String lastMessage = getAddedtoChatMessage();
        boolean flag = lastMessage.contains(name1.toUpperCase())
                && lastMessage.contains(name2.toUpperCase())
                && lastMessage.contains(name3.toUpperCase());
        return flag;
    }

    public boolean isGroupChatPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.name(IOSLocators.nameConversationCursorInput));
    }

    public boolean isYouAddedUserMessageShown(String user) throws Exception {
        String message = String.format(
                IOSLocators.xpathYouAddetToGroupChatMessage, user.toUpperCase());
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(message));
    }

    public boolean isYouRenamedConversationMessageVisible(String name) {
        return getRenamedMessage().equals(
                String.format(IOSLocators.nameYouRenamedConversationMessage,
                        name));
    }

    @Override
    public IOSPage openConversationDetailsClick() throws Exception {
        for (int i = 0; i < 3; i++) {
            if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                    By.name(IOSLocators.namePlusButton))) {
                plusButton.click();
                openConversationDetails.click();
                DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                        By.name(IOSLocators.nameAddContactToChatButton), 5);
            }
            if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                    By.name(IOSLocators.nameAddContactToChatButton))) {
                break;
            } else {
                swipeUp(1000);
            }
        }

        return new GroupChatInfoPage(this.getLazyDriver());
    }

    @Override
    public IOSPage swipeUp(int time) throws Exception {
        WebElement element = getDriver().findElement(
                By.name(nameMainWindow));

        Point coords = element.getLocation();
        Dimension elementSize = element.getSize();
        this.getDriver().swipe(coords.x + elementSize.width / 2,
                coords.y + elementSize.height - 170,
                coords.x + elementSize.width / 2, coords.y + 40, time);
        return returnBySwipe(SwipeDirection.UP);
    }

    @Override
    public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
        IOSPage page = null;
        switch (direction) {
            case DOWN: {
                page = this;
                break;
            }
            case UP: {
                page = new GroupChatInfoPage(this.getLazyDriver());
                break;
            }
            case LEFT: {
                break;
            }
            case RIGHT: {
                page = new ContactListPage(this.getLazyDriver());
                break;
            }
        }
        return page;
    }

    public boolean isYouLeftMessageShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.name(IOSLocators.nameYouLeftMessage));
    }

}
