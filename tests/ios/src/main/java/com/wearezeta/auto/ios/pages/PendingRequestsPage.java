package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class PendingRequestsPage extends IOSPage {

	@FindBy(how = How.NAME, using = IOSLocators.namePendingRequestIgnoreButton)
	private WebElement ignoreRequestButton;

	@FindBy(how = How.NAME, using = IOSLocators.namePendingRequestConnectButton)
	private WebElement connectRequestButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPendingRequesterName)
	private WebElement requesterName;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPendingRequestMessage)
	private WebElement pendingMessage;

	private String autoHelloMessage = CommonSteps.CONNECTION_MESSAGE;

	public PendingRequestsPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public ContactListPage clickIgnoreButton() throws Exception {
		ContactListPage page = null;
		ignoreRequestButton.click();
		page = new ContactListPage(this.getLazyDriver());
		return page;
	}

	public ContactListPage clickIgnoreButtonMultiple(int clicks)
			throws Exception {
		ContactListPage page = null;
		for (int i = 0; i < clicks; i++) {
			DriverUtils.waitUntilLocatorAppears(this.getDriver(),
					By.name(IOSLocators.namePendingRequestIgnoreButton));
			this.getWait().until(
					ExpectedConditions
							.elementToBeClickable(ignoreRequestButton));
			ignoreRequestButton.click();
			DriverUtils.waitUntilLocatorAppears(this.getDriver(),
					By.name(IOSLocators.namePendingRequestIgnoreButton));
		}
		page = new ContactListPage(this.getLazyDriver());
		return page;
	}

	public ContactListPage clickConnectButton() throws Exception {
		ContactListPage page = null;
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				connectRequestButton);
		connectRequestButton.click();
		page = new ContactListPage(this.getLazyDriver());
		return page;
	}

	public ContactListPage clickConnectButtonMultiple(int clicks)
			throws Exception {
		ContactListPage page = null;
		for (int i = 0; i < clicks; i++) {
			DriverUtils.waitUntilLocatorAppears(this.getDriver(),
					By.name(IOSLocators.namePendingRequestConnectButton));
			this.getWait().until(
					ExpectedConditions
							.elementToBeClickable(connectRequestButton));
			connectRequestButton.click();
			DriverUtils.waitUntilLocatorAppears(this.getDriver(),
					By.name(IOSLocators.namePendingRequestConnectButton));
		}
		page = new ContactListPage(this.getLazyDriver());
		return page;
	}

	public boolean isConnectButtonDisplayed() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.namePendingRequestConnectButton));
	}

	public String getRequesterName() {
		return requesterName.getText();
	}

	public String getRequestMessage() {
		return pendingMessage.getText();
	}

	public boolean isAutoMessageCorrect() {
		return getRequestMessage().equals(autoHelloMessage);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {

		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			break;
		}
		case UP: {
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

}
