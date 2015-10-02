package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.SettingsPage;

import cucumber.api.java.en.Then;

public class SettingsPageSteps {

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
		.getInstance();

	private SettingsPage getSettingsPage() throws Exception {
		return (SettingsPage) pagesCollection.getPage(SettingsPage.class);
	}

	/**
	 * Checks to see that the settings page is visible
	 * 
	 * @step. ^I see settings page$
	 * 
	 * @throws Throwable
	 */
	@Then("^I see settings page$")
	public void ISeeSettingsPage() throws Throwable {
		Assert.assertTrue("Settings page is not visible", getSettingsPage()
			.isSettingsPageVisible());
	}

	/**
	 * Check that change password item is present in settings menu
	 * 
	 * @step. ^I see change password item$
	 * 
	 * @throws Throwable
	 */
	@Then("^I see change password item$")
	public void ISeeSettingsChangePassword() throws Throwable {
		Assert.assertTrue(getSettingsPage().isChangePasswordVisible());
	}

	/**
	 * Tap services button
	 * 
	 * @step. ^I tap services button$
	 * 
	 * @throws Throwable
	 */
	@Then("^I tap services button$")
	public void IClickTheServicesButton() throws Throwable {
		getSettingsPage().clickServicesButton();
	}
}
