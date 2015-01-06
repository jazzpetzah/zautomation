package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.ConnectToPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class ConnectToPageSteps {
	@When("^I see connect to (.*) dialog$")
	public void WhenISeeConnectToUserDialog(String name) throws Throwable {
		Assert.assertTrue("Connection input is not visible",
				PagesCollection.connectToPage.isConnectToUserDialogVisible());
	}

	@When("^I input message in connect to dialog$")
	public void WhenIInputMessageInConnectToDialog() {
		PagesCollection.connectToPage.fillTextInConnectDialog();
	}

	@When("I click Send button on connect to dialog")
	public void IClickSendButtonConnectDialog() throws Throwable {
		PagesCollection.peoplePickerPage = PagesCollection.connectToPage
				.clickSendButton();
	}

	@When("I click Connect button on connect to dialog")
	public void IClickConnectButtonConnectDialog() throws Throwable {
		PagesCollection.peoplePickerPage = PagesCollection.connectToPage
				.sendInvitation();
	}

	@When("^I input message in connect to dialog and click Send button$")
	public void WhenIInputMessageInConnectDialogAndClickSendButton(String name)
			throws Throwable {
		PagesCollection.iOSPage = PagesCollection.connectToPage
				.sendInvitation(name);
	}

	@When("^I see connection request from (.*)$")
	public void IReceiveInvitationMessage(String contact) throws Throwable {
		// Not needed since we auto accept all alerts
		ContactListPageSteps clSteps = new ContactListPageSteps();
		clSteps.ISeeUserNameFirstInContactList(IOSLocators.xpathPendingRequest);
	}

	@When("^I confirm connection request$")
	public void IAcceptInvitationMessage() throws IOException {
		ContactListPageSteps clSteps = new ContactListPageSteps();
		clSteps.WhenITapOnContactName(IOSLocators.xpathPendingRequest);
		PagesCollection.connectToPage = new ConnectToPage(
				CommonUtils.getIosAppiumUrlFromConfig(ConnectToPage.class),
				CommonUtils
						.getIosApplicationPathFromConfig(ConnectToPage.class));
		PagesCollection.connectToPage.acceptInvitation();
		// Not needed since we auto accept all alerts
	}

}
