package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.FirstTimeOverlay;
import org.junit.Assert;

import java.io.IOException;
import java.util.Random;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.RegistrationPage;

import cucumber.api.java.en.*;

/**
 * Contains steps to work with Login/Welcome page
 */
public class LoginPageSteps {
    private LoginPage getLoginPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(LoginPage.class);
    }

    private RegistrationPage getRegistrationPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(RegistrationPage.class);
    }

    private FirstTimeOverlay getFirstTimeOverlayPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(FirstTimeOverlay.class);
    }

    /**
     * Verifies whether sign in screen is the current screen
     *
     * @throws Exception
     * @step. I see sign in screen
     */
    @Given("I see sign in screen")
    public void ISeeSignInScreen() throws Exception {
        Assert.assertTrue("Login page is not visible", getLoginPage().isVisible());
    }

    /**
     * Enter user email and password into corresponding fields on sign in screen
     * then taps "Sign In" button
     *
     * @throws AssertionError if login operation was unsuccessful
     * @step. ^I sign in using my email$
     */
    @Given("^I sign in using my email$")
    public void GivenISignInUsingEmail() throws Exception {
        final ClientUser self = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .getSelfUserOrThrowError();
        emailLoginSequence(self.getEmail(), self.getPassword());
    }

    private void emailLoginSequence(String login, String password) throws Exception {
        getLoginPage().switchToLogin();
        if (IOSTestContextHolder.getInstance().getTestContext().getFastLoginContainer().isEnabled()) {
            getLoginPage().waitForLoginToFinish();
            return;
        }
        getLoginPage().setLogin(login);
        getLoginPage().setPassword(password);
        getLoginPage().tapLoginButton();
        getLoginPage().waitForLoginToFinish();
        getLoginPage().acceptAlertIfVisible();
        getFirstTimeOverlayPage().accept();
        getLoginPage().dismissSettingsWarningIfVisible();
    }

    private void phoneLoginSequence(final PhoneNumber number) throws Exception {
        getLoginPage().switchToLogin();
        getLoginPage().switchToPhoneLogin();
        getRegistrationPage().inputPhoneNumber(number);
        getLoginPage().inputLoginCode(number);
        getLoginPage().waitForLoginToFinish();
        getLoginPage().acceptAlertIfVisible();
        getFirstTimeOverlayPage().accept();
        getLoginPage().dismissSettingsWarningIfVisible();
    }

    /**
     * Enter verification code for specified user
     *
     * @param name     name of user
     * @param codeType one of possible types of verification codes
     * @throws Exception
     * @step. ^I enter (login|registration|random) verification code for (.*)
     */
    @When("^I enter (login|registration|random) verification code for (.*)")
    public void IEnterVerificationCodeForUser(String codeType, String name) throws Exception {
        ClientUser user = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(name);
        switch (codeType.toLowerCase()) {
            case "login":
                getLoginPage().inputLoginCode(user.getPhoneNumber());
                break;
            case "registration":
                getRegistrationPage().inputActivationCode(user.getPhoneNumber());
                break;
            case "random":
                getRegistrationPage().inputRandomConfirmationCode();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown code type '%s'", codeType));
        }
    }

    /**
     * Click on RESEND button to send new verification code
     *
     * @throws Exception
     * @step. ^I tap RESEND code button$"
     */
    @When("^I tap RESEND code button$")
    public void ITapResendCodeButton() throws Exception {
        getRegistrationPage().clickResendCodeButton();
    }

    /**
     * Tap PHONE tab caption on log in screen
     *
     * @throws Exception
     * @step. ^I switch to Phone Log In tab$
     */
    @When("^I switch to Phone Log In tab$")
    public void ITapPhoneButton() throws Exception {
        getLoginPage().switchToPhoneLogin();
    }

    private static final int BY_PHONE_NUMBER_LOGIN_PROBABILITY = 5;
    private static final Random rand = new Random();

    /**
     * Sign in with email/password (20%) or phone number (80%).
     * Email login will be always enabled if @fastLogin tag is provided
     *
     * @throws AssertionError if login operation was unsuccessful
     * @step. ^I sign in using my email or phone number$
     */
    @Given("^I sign in using my email or phone number$")
    public void GivenISignInUsingEmailOrPhone() throws Exception {
        final ClientUser self = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .getSelfUserOrThrowError();
        if (!IOSTestContextHolder.getInstance().getTestContext().getFastLoginContainer().isEnabled()
                && rand.nextInt(100) < BY_PHONE_NUMBER_LOGIN_PROBABILITY) {
            phoneLoginSequence(self.getPhoneNumber());
        } else {
            emailLoginSequence(self.getEmail(), self.getPassword());
        }
    }

    /**
     * Switch to Log In tab
     *
     * @throws Exception
     * @step. ^I switch to Log In tab$
     */
    @When("^I switch to Log In tab$")
    public void ISwitchToLogInTab() throws Exception {
        getLoginPage().switchToLogin();
    }

    /**
     * Taps Login button on the corresponding screen
     *
     * @throws IOException
     * @step. ^I tap Login button$
     */
    @When("^I tap Login button$")
    public void ITapSignInButtonAgain() throws Exception {
        getLoginPage().tapLoginButton();
        getLoginPage().waitForLoginToFinish();
    }

    /**
     * Taps Login button on the corresponding screen
     *
     * @throws Exception
     * @step. ^I attempt to tap Login button$
     */
    @When("^I attempt to tap Login button$")
    public void IAttemptToTapLoginButton() throws Exception {
        getLoginPage().tapLoginButton();
    }

    /**
     * Types login string into the corresponding input field on sign in page
     *
     * @param login login string (usually it is user email)
     * @throws IOException
     * @step. ^I have entered login (.*)
     */
    @When("^I have entered login (.*)")
    public void WhenIHaveEnteredLogin(String login) throws Exception {
        login = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(login, ClientUsersManager.FindBy.EMAIL_ALIAS);
        getLoginPage().setLogin(login);
    }

    /**
     * Types password string into the corresponding input field on sign in page
     *
     * @param password password string
     * @throws IOException
     * @step. ^I have entered password (.*)
     */
    @When("^I have entered password (.*)")
    public void WhenIHaveEnteredPassword(String password) throws Exception {
        password = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(password, ClientUsersManager.FindBy.PASSWORD_ALIAS);
        getLoginPage().setPassword(password);
    }

    /**
     * Tap and holds the "finger" for a while on email input field
     *
     * @throws Exception
     * @step. I tap and hold on Email input
     */
    @When("I tap and hold on Email input")
    public void ITapHoldEmailInput() throws Exception {
        getLoginPage().tapHoldEmailInput();
    }

    /**
     * Enters given text into email input field and taps password field
     *
     * @param wrongMail text to enter into email input field
     * @step. ^I enter wrong email (.*)
     */
    @When("^I enter wrong email (.*)")
    public void IEnterWrongEmail(String wrongMail) throws Exception {
        getLoginPage().setLogin(wrongMail);
        getLoginPage().tapPasswordField();
    }

    /**
     * Enters a text into password input field
     *
     * @step. ^I enter wrong password (.*)
     */
    @When("^I enter wrong password (.*)")
    public void IEnterWrongPassword(String wrongPassword) throws Exception {
        getLoginPage().setPassword(wrongPassword);
    }

    /**
     * Verifies whether the notification about wrong credentials exists on the
     * current screen
     *
     * @throws Exception
     * @step. ^I see wrong credentials notification$
     */
    @Then("^I see wrong credentials notification$")
    public void ISeeWrongCredentialsNotification() throws Exception {
        Assert.assertTrue("I don't see wrong credentials notification",
                getLoginPage().wrongCredentialsNotificationIsShown());
    }

    /**
     * Verifies whether the notification invalid email is shown
     *
     * @throws Exception
     * @step. ^I see invalid email alert$
     */
    @Then("^I see invalid email alert$")
    public void ISeeInvalidEmailAlert() throws Exception {
        Assert.assertTrue("I don't see invalid email alert",
                getLoginPage().isInvalidEmailAlertShown());
    }

    /**
     * Verifies whether the notification already registered email shown
     *
     * @throws Exception
     * @step. ^I see already registered email alert$
     */
    @Then("^I see already registered email alert$")
    public void ISeeAlreadyRegisteredEmailAlert() throws Exception {
        Assert.assertTrue("I don't see already registered email alert",
                getLoginPage().isAlreadyRegisteredEmailAlertShown());
    }

    @When("^I tap Not Now to not add phone number$")
    public void ITapNotNowToNotAddPhoneNumber() throws Exception {
        getLoginPage().tapPhoneNotNow();
        getLoginPage().waitForLoginToFinish();
    }

}
