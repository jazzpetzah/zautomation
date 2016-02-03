package com.wearezeta.auto.web.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginPageSteps {

	private static final Logger log = ZetaLogger.getLog(LoginPageSteps.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	    /**
     * Enters user email and password into corresponding fields on sign in screen then taps "Sign In" button
     * 
     * @step. ^I Sign in using login (.*) and password (.*)$
     * 
     * @param login user login string
     * @param password user password string
     * 
     * @throws AssertionError if login operation was unsuccessful
     */
	@Given("^I Sign in using login (.*) and password (.*)$")
	public void ISignInUsingLoginAndPassword(String login, String password)
			throws Exception {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			try {
				// search for email by name aliases in case name is specified
				login = usrMgr.findUserByNameOrNameAlias(login).getEmail();
			} catch (NoSuchUserException ex) {
			}
		}

		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		log.debug("Starting to Sign in using login " + login + " and password "
				+ password);
		this.IEnterEmail(login);
		this.IEnterPassword(password);
		this.IPressSignInButton();

		// Wait until signed in properly
		this.IAmSignedInProperly();
	}

	/**
	 * Presses Sign In button on the corresponding page
	 *
	 * @step. ^I press Sign In button$
	 *
	 * @throws Exception
	 *             if Selenium fails to wait until sign in action completes
	 */
	@When("^I press Sign In button$")
	public void IPressSignInButton() throws Exception {
		webappPagesCollection.getPage(LoginPage.class).clickSignInButton();
	}

	/**
	 * Presses Sign In button on the corresponding page
	 *
	 * @step. ^I press Sign In button$
	 *
	 * @throws Exception
	 *             if Selenium fails to wait until sign in action completes
	 */
	@When("^Sign In button is disabled$")
	public void SignInButtonIsDisabled() throws Exception {
		Assert.assertTrue(webappPagesCollection.getPage(LoginPage.class)
				.isSignInButtonDisabled());
	}

	/**
	 * Verifies whether an account is signed in properly
	 *
	 * @step. ^I am signed in properly$
	 *
	 * @throws Exception
	 */
	@Then("^I am signed in properly$")
	public void IAmSignedInProperly() throws Exception {
		Assert.assertTrue(
				"Sign In button/login progress spinner are still visible",
				webappPagesCollection.getPage(LoginPage.class).waitForLogin());
	}

	/**
	 * Checks if a error message is shown on the sign in page
	 *
	 * @step. ^the sign in error message reads (.*)
	 * @param message
	 *            expected error message
	 * @throws Exception
	 */
	@Then("^the sign in error message reads (.*)")
	public void TheSignInErrorMessageReads(String message) throws Exception {
		assertThat("sign in error message",
				webappPagesCollection.getPage(LoginPage.class)
						.getErrorMessage(), equalTo(message));
	}

	/**
	 * Checks if a orange line is shown around the email field on the sign in
	 * form
	 *
	 * @step. ^the email field on the sign in form is marked as error$
	 * @throws Exception
	 */
	@Then("^the email field on the sign in form is marked as error$")
	public void ARedDotIsShownOnTheEmailField() throws Exception {
		assertThat("Email field not marked",
				webappPagesCollection.getPage(LoginPage.class)
						.isEmailFieldMarkedAsError());
	}

	/**
	 * Checks if a orange line is shown around the password field on the sign in
	 * form
	 *
	 * @step. ^the password field on the sign in form is marked as error$
	 * @throws Exception
	 */
	@Then("^the password field on the sign in form is marked as error$")
	public void ARedDotIsShownOnThePasswordField() throws Exception {
		assertThat("Password field not marked",
				webappPagesCollection.getPage(LoginPage.class)
						.isPasswordFieldMarkedAsError());
	}

	/**
	 * Types email string into the corresponding input field on sign in page
	 * 
	 * @step. ^I enter email \"([^\"]*)\"$
	 * 
	 * @param email
	 *            user email string
	 */
	@When("^I enter email \"([^\"]*)\"$")
	public void IEnterEmail(String email) throws Exception {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		webappPagesCollection.getPage(LoginPage.class).inputEmail(email);
	}

	/**
	 * Types password string into the corresponding input field on sign in page
	 * 
	 * @step. ^I enter password \"([^\"]*)\"$
	 * 
	 * @param password
	 *            password string
	 */
	@When("^I enter password \"([^\"]*)\"$")
	public void IEnterPassword(String password) throws Exception {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		webappPagesCollection.getPage(LoginPage.class).inputPassword(password);
	}

	@When("I (un)?check option to remember me")
	public void ICheckOptionToRememberMe(String uncheck) throws Exception {
		if (uncheck == null) {
			webappPagesCollection.getPage(LoginPage.class).checkRememberMe();
		} else {
			webappPagesCollection.getPage(LoginPage.class).uncheckRememberMe();
		}
	}

	/**
	 * Verifies whether Sign In page is the current page
	 * 
	 * @step. ^I see Sign In page$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if current page is not Sign In page
	 */
	@Given("^I see Sign In page$")
	public void ISeeSignInPage() throws Exception {
		Assert.assertTrue(webappPagesCollection.getPage(LoginPage.class)
				.isSignInFormVisible());
	}

	/**
	 * Click Change Password button on login page
	 * 
	 * @step. ^I click Change Password button$
	 * 
	 * @throws Exception
	 */
	@When("^I click Change Password button$")
	public void IClickChangePassword() throws Exception {
		webappPagesCollection.getPage(LoginPage.class)
				.clickChangePasswordButton();
	}

	/**
	 * Verifies whether the expected login error is visible on the page
	 * 
	 * @step. ^I see login error \"(.*)\"$
	 * 
	 * @param expectedError
	 *            the text of error
	 * @throws Exception
	 */
	@Then("^I see login error \"(.*)\"$")
	public void ISeeLoginError(String expectedError) throws Exception {
		final String loginErrorText = webappPagesCollection.getPage(
				LoginPage.class).getErrorMessage();
		Assert.assertTrue(
				String.format(
						"The actual login error '%s' is not equal to the expected one: '%s'",
						loginErrorText, expectedError), loginErrorText
						.equals(expectedError));
	}

	@When("^I switch to phone number sign in page$")
	public void i_switch_to_phone_number_sign_in_page() throws Throwable {
		webappPagesCollection.getPage(LoginPage.class)
				.switchToPhoneNumberLoginPage();
	}
}
