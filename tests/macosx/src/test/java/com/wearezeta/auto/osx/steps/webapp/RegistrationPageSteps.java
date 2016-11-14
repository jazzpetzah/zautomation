package com.wearezeta.auto.osx.steps.webapp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.web.pages.RegistrationPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;

public class RegistrationPageSteps {
    private static final Logger LOG = ZetaLogger.getLog(RegistrationPageSteps.class.getName());

    private ClientUser userToRegister = null;
    private Future<String> activationMessage;
    public static final int maxCheckCnt = 2;
    
    private final TestContext webContext;
    private final TestContext wrapperContext;

    public RegistrationPageSteps() {
        this.webContext = new TestContext();
        this.wrapperContext = new TestContext();
    }
    
    public RegistrationPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    /**
     * Enter user name into registration form
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I enter user name (.*) on Registration page$
     */
    @When("^I enter user name (.*) on Registration page$")
    public void IEnterName(String name) throws Exception {
        webContext.getPagesCollection().getPage(RegistrationPage.class)
                .waitForRegistrationPageToFullyLoad();
        try {
            this.userToRegister = webContext.getUserManager().findUserByNameOrNameAlias(name);
        } catch (NoSuchUserException e) {
            if (this.userToRegister == null) {
                this.userToRegister = new ClientUser();
            }
            this.userToRegister.setName(name);
            this.userToRegister.clearNameAliases();
            this.userToRegister.addNameAlias(name);
        }
        webContext.getPagesCollection().getPage(RegistrationPage.class).enterName(
                this.userToRegister.getName());
    }

    /**
     * Enter user email into registration form
     *
     * @param email user email/alias
     * @throws Exception
     * @step. ^I enter user email (.*) on Registration page$
     */
    @When("^I enter user email (.*) on Registration page$")
    public void IEnterEmail(String email) throws Exception {
        boolean flag = false;
        try {
            String realEmail = webContext.getUserManager().findUserByEmailOrEmailAlias(email)
                    .getEmail();
            this.userToRegister.setEmail(realEmail);
        } catch (NoSuchUserException e) {
            if (this.userToRegister == null) {
                this.userToRegister = new ClientUser();
            }
            flag = true;
        }

        if (flag) {
            webContext.getPagesCollection().getPage(RegistrationPage.class).enterEmail(
                    email);
        } else {
            webContext.getPagesCollection().getPage(RegistrationPage.class).enterEmail(
                    this.userToRegister.getEmail());
        }
    }

    /**
     * Verifies autofilled email of user
     *
     * @param usernameAlias user name/alias
     * @throws Exception
     * @step. ^(.*) verifies email is correct on Registration page$
     */
    @When("^(.*) verifies email is correct on Registration page$")
    public void IVerifyEmail(String usernameAlias) throws Exception {
        String realEmail = webContext.getUserManager().findUserByNameOrNameAlias(usernameAlias)
                .getEmail();
        Assert.assertEquals("Entered email is wrong", realEmail,
                webContext.getPagesCollection().getPage(RegistrationPage.class)
                .getEnteredEmail());

    }

    /**
     * Verifies autofilled username of user
     *
     * @param usernameAlias user name/alias
     * @throws Exception
     * @step. ^(.*) verifies username is correct on Registration page$
     */
    @When("^(.*) verifies username is correct on Registration page$")
    public void IVerifyUsername(String usernameAlias) throws Exception {
        String realUsername = webContext.getUserManager().findUserByNameOrNameAlias(usernameAlias)
                .getName();
        Assert.assertEquals("Entered username is wrong", realUsername,
                webContext.getPagesCollection().getPage(RegistrationPage.class)
                .getEnteredName());

    }

    /**
     * Enter user password into registration form
     *
     * @param password user password/alias
     * @throws Exception
     * @step. ^I enter user password \"(.*)\" on Registration page$
     */
    @When("^I enter user password \"(.*)\" on Registration page$")
    public void IEnterPassword(String password) throws Exception {

        try {
            ClientUser user = webContext.getUserManager().findUserByPasswordAlias(password);
            if (this.userToRegister == null) {
                this.userToRegister = user;
            }
            this.userToRegister.setPassword(user.getPassword());
        } catch (NoSuchUserException e) {
            this.userToRegister.setPassword(password);
            this.userToRegister.addPasswordAlias(password);
        }
        webContext.getPagesCollection().getPage(RegistrationPage.class).enterPassword(
                this.userToRegister.getPassword());
    }

    /**
     * Check terms of use checkbox
     *
     * @throws Exception
     * @step. ^I accept the Terms of Use$
     */
    @When("^I accept the Terms of Use$")
    public void IAcceptTermsOfUse() throws Exception {
        webContext.getPagesCollection().getPage(RegistrationPage.class)
                .acceptTermsOfUse();
    }

    /**
     * Submit registration form
     *
     * @throws Exception
     * @step. ^I submit registration form$
     */
    @When("^I submit registration form$")
    public void ISubmitRegistration() throws Exception {
        webContext.getPagesCollection().getPage(RegistrationPage.class)
                .submitRegistration();
    }

    /**
     * Start monitoring thread for activation email. Please put this step BEFORE you submit the registration form
     *
     * @throws Exception
     * @step. ^I start activation email monitoring$
     */
    @When("^I start activation email monitoring$")
    public void IStartActivationEmailMonitoring() throws Exception {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
        this.activationMessage = IMAPSMailbox.getInstance(userToRegister.getEmail(), userToRegister.getPassword())
                .getMessage(expectedHeaders, BackendAPIWrappers.ACTIVATION_TIMEOUT);
    }

    /**
	 * Verify whether email address, which is visible on email confirmation page
	 * is the same as the expected one
	 * 
	 * @step. ^I see email (.*) on [Vv]erification page$
	 * 
	 * @param email
	 *            expected email/alias
	 * @throws Exception
	 */
	@Then("^I see email (.*) on [Vv]erification page$")
	public void ISeeVerificationEmail(String email) throws Exception {
		email = webContext.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
		assertThat(webContext.getPagesCollection().getPage(RegistrationPage.class)
				.getVerificationEmailAddress(), containsString(email));
	}

    /**
     * Verify whether email address, which is visible on email pending page is the same as the expected one
     *
     * @param email expected email/alias
     * @throws Exception
     * @step. ^I see email (.*) on pending page$
     */
    @Then("^I see email (.*) on pending page$")
    public void ISeePendingEmail(String email) throws Exception {
        email = webContext.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
        assertThat(webContext.getPagesCollection().getPage(RegistrationPage.class)
                .getPendingEmailAddress(), containsString(email));
    }

    /**
     * Verify whether I see an error message on the verification page
     *
     * @param message expected error message
     * @throws NoSuchUserException
     * @step. ^I see error \"(.*)\" on [Vv]erification page$
     */
    @Then("^I see error \"(.*)\" on [Vv]erification page$")
    public void ISeeErrorMessageOnVerificationPage(String message)
            throws Throwable {
        assertThat(webContext.getPagesCollection().getPage(RegistrationPage.class)
                .getErrorMessages(), hasItem(message));
    }

    /**
     * Checks if a orange line is shown around the email field on the registration form
     *
     * @throws Exception
     * @step. ^I verify that the email field on the registration form is( not)? marked as error$
     */
    @Then("^I verify that the email field on the registration form is( not)? marked as error$")
    public void ARedDotIsShownOnTheEmailField(String not) throws Exception {
        if (not == null) {
            assertThat("email field marked as error", webContext.getPagesCollection()
                    .getPage(RegistrationPage.class)
                    .isEmailFieldMarkedAsError());
        } else {
            assertThat("email field marked as valid", webContext.getPagesCollection()
                    .getPage(RegistrationPage.class)
                    .isEmailFieldMarkedAsValid());
        }
    }

    /**
     * Checks if an icon is shown
     *
     * @throws Exception
     * @step. ^I verify that an envelope icon is shown$
     */
    @Then("^I verify that an envelope icon is shown$")
    public void IVerifyThatAnEnvelopeIconIsShown() throws Exception {
        assertThat("Envelope icon not shown",
                webContext.getPagesCollection().getPage(RegistrationPage.class)
                .isEnvelopeShown());
    }

    /**
     * Activate newly registered user on the backend. Don't forget to call the 'I start activation email monitoring' step before
     * this one
     *
     * @throws Exception
     * @step. ^I verify registration email$
     */
    @Then("^I verify registration email$")
    public void IVerifyRegistrationEmail() throws Exception {
        BackendAPIWrappers.activateRegisteredUserByEmail(this.activationMessage);
    }

    /**
     * Activates user using browser URL from activation email and sign him in to the app if the activation was successful. Don't
     * forget to call the 'I start activation email monitoring' step before this one
     *
     * @throws Exception
     * @step. ^I activate user by URL$
     */
    @Then("^I activate user by URL$")
    public void WhenIActivateUserByUrl() throws Exception {
        final String link = BackendAPIWrappers
                .getUserActivationLink(this.activationMessage);
        LOG.info("Get activation link from " + link);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(link);
        HttpEntity entity = httpclient.execute(httpGet).getEntity();
        if (entity != null) {
            String content = EntityUtils.toString(entity);
            Pattern p = Pattern.compile("data-url=\"(.*?)\"");
            Matcher m = p.matcher(content);
            while (m.find()) {
                String activationLink = m.group(1);
                LOG.info("Activation link: " + activationLink);
                httpGet = new HttpGet(activationLink);
                httpclient.execute(httpGet);
            }
        }

        // indexes in aliases start from 1
        final int userIndex = webContext.getUserManager().appendCustomUser(userToRegister) + 1;
        userToRegister.addEmailAlias(ClientUsersManager.EMAIL_ALIAS_TEMPLATE
                .apply(userIndex));
        userToRegister.addNameAlias(ClientUsersManager.NAME_ALIAS_TEMPLATE
                .apply(userIndex));
        userToRegister
                .addPasswordAlias(ClientUsersManager.PASSWORD_ALIAS_TEMPLATE
                        .apply(userIndex));

        webContext.getPagesCollection().getPage(LoginPage.class).waitForLogin();
    }

    /**
     * Switch to Sign In page
     *
     * @throws Exception
     * @step. ^I switch to [Ss]ign [Ii]n page$
     */
    @Given("^I switch to [Ss]ign [Ii]n page$")
    public void ISwitchToLoginPage() throws Exception {
        webContext.getPagesCollection().getPage(RegistrationPage.class)
                .switchToLoginPage();
    }

    /**
     * Clicks on Verify later button on Verification page
     *
     * @throws Exception
     * @step. ^I click on Verify later button on Verification page$
     */
    @Then("^I click on Verify later button on Verification page$")
    public void IClickVerifyLaterButton() throws Exception {
        webContext.getPagesCollection().getPage(RegistrationPage.class)
                .clickVerifyLaterButton();
    }
}
