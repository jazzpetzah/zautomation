package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class BringYourFriendsPopoverPage extends WebPage {

	@FindBy(css = PopoverLocators.BringYourFriendsPopover.cssInvitationTextarea)
	private WebElement invitationTextarea;

	@FindBy(css = PopoverLocators.BringYourFriendsPopover.cssShareContactsButton)
	private WebElement shareContactsButton;

	@FindBy(css = PopoverLocators.BringYourFriendsPopover.cssInvitePeopleButton)
	private WebElement invitePeopleButton;

	@FindBy(css = PopoverLocators.BringYourFriendsPopover.cssGmailImportButton)
	private WebElement gmailButton;

	@FindBy(id = PopoverLocators.BringYourFriendsPopover.idRootLocator)
	private WebElement root;

	public BringYourFriendsPopoverPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public String getInvitationText() {
		return invitationTextarea.getAttribute("value");
	}

	public String parseInvitationLink() {
		final String invitationText = this.getInvitationText();
		final String regex = "(https://\\S+)";
		final Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		final Matcher urlMatcher = p.matcher(invitationText);
		if (urlMatcher.find()) {
			return urlMatcher.group(1);
		} else {
			throw new RuntimeException(String.format(
					"Invitation link could not be parsed from this text: '%s'",
					invitationText));
		}
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.id(PopoverLocators.BringYourFriendsPopover.idRootLocator));
	}

	public boolean isNotVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(PopoverLocators.BringYourFriendsPopover.idRootLocator));
	}

	public void waitUntilGmailImportButtonIsNotVisible() throws Exception {
		getDriver();
		assert DriverUtils
				.waitUntilLocatorDissapears(
						getDriver(),
						By.cssSelector(PopoverLocators.BringYourFriendsPopover.cssGmailImportButton)) : "Gmail Import button is not visible after "
				+ this.getDriverInitializationTimeout() + " ms timeout";
	}

	public boolean isShareContactsButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				shareContactsButton);
	}

	public boolean isShareContactsButtonNotVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorDissapears(
						getDriver(),
						By.cssSelector(PopoverLocators.BringYourFriendsPopover.cssShareContactsButton));
	}

	public void clickShareContactsButton() {
		shareContactsButton.click();
	}

	public void clickInvitePeopleButton() {
		invitePeopleButton.click();
	}
}
