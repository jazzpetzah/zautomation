package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConversationViewPage extends AndroidTabletPage {
	public static final String idRootLocator = "fl__message_stream_land_container";

	public static final Function<String, String> xpathSystemMessageByContent = content -> String
			.format("//*[@id='ltv__row_conversation__message' and contains(@value, '%s')]",
					content.toUpperCase());

	public static final Function<String, String> xpathChatHeaderMessageByContent = content -> String
			.format("//*[@id='ttv__row_conversation__connect_request__chathead_footer__label' and contains(@value, '%s')]",
					content);

	@FindBy(id = DialogPage.idParticipantsBtn)
	private WebElement showDetailsButton;

	@FindBy(id = idEditText)
	private WebElement inputField;

	public static final Function<String, String> xpathInputFieldByValue = value -> String
			.format("//*[@id='%s' and @value='%s']",
					idEditText, value);

	public static final Function<String, String> xpathConversationMessageByValue = value -> String
			.format("//*[@id='ltv__row_conversation__message' and @value='%s']",
					value);

	public static final String idIsTypingAvatar = "civ__cursor__self_user_avatar";

	public TabletConversationViewPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private DialogPage getDialogPage() throws Exception {
		return (DialogPage) this.getAndroidPageInstance(DialogPage.class);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idRootLocator));
	}

	public void tapShowDetailsButton() throws Exception {
		showDetailsButton.click();
	}

	public boolean waitForSystemMessageContains(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathSystemMessageByContent
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitForChatHeaderMessageContains(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathChatHeaderMessageByContent
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapTextInput() {
		inputField.click();
	}

	public void typeMessage(String message) {
		inputField.sendKeys(message);
	}

	public void sendMessage() throws Exception {
		inputField.sendKeys("\n");
	}

	public boolean waitUntilMessageIsVisible(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathConversationMessageByValue
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void swipeLeftOnTextInput() throws Exception {
		getDialogPage().swipeOnCursorInput();
	}

	public void tapPingButton() throws Exception {
		getDialogPage().tapPingBtn();
	}

	public boolean waitUntilPingMessageIsVisible(String expectedMessage)
			throws Exception {
		return getDialogPage().waitForPingMessageWithText(expectedMessage);
	}

	public boolean waitUntilAPictureAppears() throws Exception {
		final By locator = By.id(DialogPage.idDialogImages);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapIsTypingAvatar() throws Exception {
		final By locator = By.id(idIsTypingAvatar);
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : "IsTyping avatar is not visible in the conversation view";
		getDriver().findElement(locator).click();
	}
}
