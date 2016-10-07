package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.SelfProfilePage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class SelfProfilePageSteps {

	private final TestContext context;
        
    public SelfProfilePageSteps() {
        this.context = new TestContext();
    }

    public SelfProfilePageSteps(TestContext context) {
        this.context = context;
    }

	/**
	 * Clicks the corresponding item from "gear" menu
	 * 
	 * @step. ^I select (.*) menu item on self profile page$
	 * 
	 * @param name
	 *            the name of menu item
	 * @throws Exception
	 */
	@And("^I select (.*) menu item on self profile page$")
	public void ISelectGearMenuItem(String name) throws Exception {
		context.getPagesCollection().getPage(SelfProfilePage.class)
				.selectGearMenuItem(name);
	}

	/**
	 * Verifies that correct user name is shown on self profile page
	 * 
	 * @step. ^I see user name on self profile page (.*)$
	 * 
	 * @param name
	 *            name of the user
	 * @throws Exception
	 */
	@And("^I see user name on self profile page (.*)$")
	public void ISeeUserNameOnSelfProfilePage(String name) throws Exception {
		name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		boolean nameCorrect = context.getPagesCollection().getPage(
				SelfProfilePage.class).checkNameInSelfProfile(name);
		Assert.assertTrue(nameCorrect);
	}

	/**
	 * Verifies that correct phone number is shown on self profile page
	 * 
	 * @step. ^I see user phone number on self profile page (.*)$
	 * 
	 * @param phoneNumber
	 *            phone number of the user
	 * @throws Exception
	 */
	@And("^I see user phone number on self profile page (.*)$")
	public void ISeeUserPhoneNumberOnSelfProfilePage(String phoneNumber)
			throws Exception {
		phoneNumber = context.getUserManager().replaceAliasesOccurences(phoneNumber,
				FindBy.PHONENUMBER_ALIAS);
		assertThat(context.getPagesCollection().getPage(SelfProfilePage.class)
				.getUserPhoneNumber(), equalTo(phoneNumber));
	}

	/**
	 * Verifies that correct user email is shown on self profile page
	 * 
	 * @step. ^I see user email on self profile page (.*)$
	 * 
	 * @param email
	 *            email of the user
	 * 
	 * @throws NoSuchUserException
	 */
	@And("^I see user email on self profile page (.*)$")
	public void ISeeUserEmailOnSelfProfilePage(String email)
			throws NoSuchUserException, Exception {
		try {
			email = context.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {

		}

		String actualEmail = context.getPagesCollection().getPage(
				SelfProfilePage.class).getUserMail();
		Assert.assertEquals(email, actualEmail);
	}

	/**
	 * Set new username on self profile page
	 * 
	 * @step. ^I change username to (.*)
	 * 
	 * @param name
	 *            new username string
	 */
	@And("^I change username to (.*)")
	public void IChangeUserNameTo(String name) throws Exception {
		context.getPagesCollection().getPage(SelfProfilePage.class).setUserName(name);
		context.getUserManager().getSelfUserOrThrowError().setName(name);
	}

	/**
	 * Set accent color on self profile page
	 * 
	 * @step. ^I set my accent color to (\\w+)$
	 * 
	 * @param colorName
	 *            one of these colors: StrongBlue, StrongLimeGreen,
	 *            BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * 
	 * @throws Exception
	 */
	@Then("^I set my accent color to (\\w+)$")
	public void ISetMyAccentColorTo(String colorName) throws Exception {
		context.getPagesCollection().getPage(SelfProfilePage.class).selectAccentColor(
				colorName);
	}

	/*
	 * Verify my accent color in color picker is equal to expected color
	 * 
	 * @step. ^I verify my accent color in color picker is set to (\\w+) color$
	 * 
	 * @param colorName one of these colors: StrongBlue, StrongLimeGreen,
	 * BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * 
	 * @throws Exception
	 */
	@Then("^I verify my accent color in color picker is set to (\\w+) color$")
	public void IVerifyMyAccentColor(String colorName) throws Exception {
		final int expectedColorId = AccentColor.getByName(colorName).getId();
		final int actualColorId = context.getPagesCollection().getPage(
				SelfProfilePage.class).getCurrentAccentColorId();
		Assert.assertTrue("my actual accent color is not set",
				actualColorId == expectedColorId);
	}

	/**
	 * Click camera button on Self Profile page
	 * 
	 * @step. ^I click camera button$
	 * 
	 * @throws Exception
	 */
	@And("^I click camera button$")
	public void IClickCameraButton() throws Exception {
		context.getPagesCollection().getPage(SelfProfilePage.class)
				.clickCameraButton();
	}

	/*
	 * Verify my avatar background color is set to expected color
	 * 
	 * @step. ^I verify my avatar background color is set to (\\w+) color$
	 * 
	 * @param colorName one of these colors: StrongBlue, StrongLimeGreen,
	 * BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * 
	 * @throws Exception
	 */

	@Then("^I verify my avatar background color is set to (\\w+) color$")
	public void IVerifyMyAvatarColor(String colorName) throws Exception {
		final AccentColor expectedColor = AccentColor.getByName(colorName);
		final AccentColor avatarColor = context.getPagesCollection().getPage(
				SelfProfilePage.class).getCurrentAvatarAccentColor();
		Assert.assertTrue("my avatar background accent color is not set",
				avatarColor == expectedColor);
	}

	/**
	 * Emulates drop of existing picture into self profile
	 * 
	 * @step. ^I drop picture (.*) to self profile$
	 * 
	 * @param pictureName
	 *            the name of existing image in ~/Documents folder
	 * @throws Exception
	 */
	@When("^I drop picture (.*) to self profile$")
	public void IDropPicture(String pictureName) throws Exception {
		context.getPagesCollection().getPage(SelfProfilePage.class).dropPicture(
				pictureName);
	}

	/**
	 * Emulates upload of existing picture into self profile
	 * 
	 * @step. ^I upload picture (.*) to self profile$
	 * 
	 * @param pictureName
	 *            the name of existing image in ~/Documents folder
	 * @throws Exception
	 */
	@When("^I upload picture (.*) to self profile$")
	public void IUploadPicture(String pictureName) throws Exception {
		context.getPagesCollection().getPage(SelfProfilePage.class).uploadPicture(pictureName);
	}

}
