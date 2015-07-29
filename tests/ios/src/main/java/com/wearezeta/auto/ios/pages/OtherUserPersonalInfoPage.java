package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class OtherUserPersonalInfoPage extends IOSPage {

	@FindBy(how = How.NAME, using = IOSLocators.nameOtherUserEmailField)
	private WebElement otherUserEmail;

	@FindBy(how = How.NAME, using = IOSLocators.nameRemoveFromConversation)
	private WebElement removeFromChat;

	@FindBy(how = How.NAME, using = IOSLocators.nameComfirmRemoveButton)
	private WebElement confirmRemove;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddContactToChatButton)
	private WebElement addButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameOtherUserAddContactToChatButton)
	private WebElement addOtherUserButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameContinueButton)
	private WebElement continueButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameExitOtherUserPersonalInfoPageButton)
	private WebElement exitOtherPersonalInfoPageButton;

	@FindBy(how = How.XPATH, using = IOSLocators.DialogInfoPage.xpathArchiveButton)
	private WebElement archiveButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherPersonalInfoPageNameField)
	private WebElement nameField;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherPersonalInfoPageEmailField)
	private WebElement emailField;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddContactToChatButton)
	private WebElement startDialogButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameOtherUserConversationMenu)
	private WebElement otherUserConversationMenuButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConversationMenu)
	private WebElement conversationMenuButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameSilenceConversationButton)
	private WebElement silenceMenuButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSilenceConversationButton)
	private WebElement menuSilenceButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameUnsilenceConversationButton)
	private WebElement notifyMenuButton;

	public OtherUserPersonalInfoPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void catchContinueAlert() throws Exception {
		try {
			WebElement el = this.getDriver().findElementByName(
					IOSLocators.nameContinueButton);
			el.click();
		} catch (NoSuchElementException ex) {
			// do nothing
		}
	}

	public void openEllipsisMenu() throws Exception {
		openConversationMenu();
	}

	public void clickArchiveMenuButton() {
		archiveButton.click();
	}

	public IOSPage leavePageToGroupInfoPage() throws Exception {
		exitOtherPersonalInfoPageButton.click();
		return new GroupChatInfoPage(this.getLazyDriver());
	}

	public DialogPage leavePageToDialogPage() throws Exception {
		exitOtherPersonalInfoPageButton.click();
		return new DialogPage(this.getLazyDriver());
	}

	public PeoplePickerPage addContactToChat() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(IOSLocators.nameAddContactToChatButton), 2)) {
			addButton.click();
		} else {
			addOtherUserButton.click();
		}
		catchContinueAlert();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public boolean isOtherUserProfileEmailVisible(String name) throws Exception {
		WebElement otherUserEmail = getDriver().findElementByXPath(
				String.format(IOSLocators.xpathOtherUserName,
						name.toUpperCase()));
		return otherUserEmail.isDisplayed();
	}

	public boolean isOtherUserProfileNameVisible(String name) throws Exception {
		WebElement otherUserName = getDriver().findElementByName(name);
		return otherUserName.isEnabled();
	}

	public void continueToAddUser() {
		continueButton.click();
	}

	public void removeFromConversation() throws Exception {
		DriverUtils.mobileTapByCoordinates(this.getDriver(), removeFromChat);
	}

	public boolean isRemoveFromConversationAlertVisible() {
		return confirmRemove.isDisplayed();
	}

	public void confirmRemove() {
		confirmRemove.click();
	}

	public String getNameFieldValue() {
		return nameField.getAttribute("value");
	}

	public String getEmailFieldValue() throws Exception {
		String result = "";
		try {
			DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(IOSLocators.xpathOtherPersonalInfoPageEmailField));
			result = emailField.getAttribute("value");
		} catch (NoSuchElementException ex) {

		}
		return result;
	}

	public DialogPage clickOnStartDialogButton() throws Throwable {
		DialogPage page = null;
		this.getDriver().tap(
				1,
				this.getDriver().findElementByName(
						IOSLocators.nameOtherUserAddContactToChatButton), 1);
		page = new DialogPage(this.getLazyDriver());
		return page;
	}

	public void openConversationMenu() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(IOSLocators.nameConversationMenu), 2)) {
			conversationMenuButton.click();
		} else {
			otherUserConversationMenuButton.click();
		}
		Thread.sleep(2000);
	}

	public void clickSilenceMenuButton() throws InterruptedException {
		menuSilenceButton.click();
	}

	public void clickNotifyMenuButton() throws InterruptedException {
		notifyMenuButton.click();
		Thread.sleep(2000);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			page = new DialogPage(this.getLazyDriver());
			break;
		}
		case UP: {
			return this;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			break;
		}
		}
		return page;
	}

}
