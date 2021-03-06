package com.wearezeta.auto.web.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.PasswordResetMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.external.PasswordChangePage;
import com.wearezeta.auto.web.pages.external.PasswordChangeRequestPage;
import com.wearezeta.auto.web.pages.external.PasswordChangeRequestSuccessfullPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PasswordChangeRequestSteps {

    @SuppressWarnings("unused")
    private static final Logger log = ZetaLogger.getLog(PasswordChangeRequestSteps.class.getSimpleName());

    private Future<String> passwordChangeMessage = null;
    private final WebAppTestContext context;

    public PasswordChangeRequestSteps(WebAppTestContext context) {
        this.context = context;
    }

    @When("^I go to Password Change Reset page for (.*)")
    public void IGoToPasswordChangeResetPageFor(String agent) throws Exception {
        final String website = CommonUtils.getWebsitePathFromConfig(PasswordChangeRequestSteps.class);
        context.getPagesCollection().getPage(PasswordChangePage.class).setUrl(website + "/forgot/?agent=" + agent);
        context.getPagesCollection().getPage(PasswordChangePage.class).navigateTo();
    }

    @Then("^I see Password Change Request page$")
    public void ISeePasswordChangeRequestPage() throws Exception {
        Assert.assertTrue("Email field not visible", context.getPagesCollection()
                .getPage(PasswordChangeRequestPage.class).isEmailFieldVisible());
    }

    @When("^I enter email (\\S+) on Password Change Request page$")
    public void IEnterEmail(String emailOrAlias) throws Exception {
        emailOrAlias = context.getUsersManager().replaceAliasesOccurences(emailOrAlias,
                FindBy.EMAIL_ALIAS);
        context.getPagesCollection().getPage(PasswordChangeRequestPage.class)
                .setEmail(emailOrAlias);
    }

    @When("^I enter unregistered email(.*)$")
    public void IEnterUnregisteredEmail(String unregisteredMail) throws Exception {
        context.getPagesCollection().getPage(PasswordChangeRequestPage.class)
                .setEmail(unregisteredMail);
    }

    private static final int PASSWORD_MSG_TIMEOUT_SECONDS = 60;
    private static final long TIME_SENT_DELTA_MILLISECONDS = 2000;

    @And("(.*) starts? listening for password change confirmation$")
    public void IStartListeningForPasswordChangeEmail(String emailOrName)
            throws Exception {
        ClientUser user;
        try {
            user = context.getUsersManager().findUserByEmailOrEmailAlias(emailOrName);
        } catch (NoSuchUserException e) {
            user = context.getUsersManager().findUserByNameOrNameAlias(emailOrName);
        }
        IMAPSMailbox mbox = IMAPSMailbox.getInstance(user.getEmail(), user.getPassword());
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, user.getEmail());
        this.passwordChangeMessage = mbox.getMessage(expectedHeaders,
                PASSWORD_MSG_TIMEOUT_SECONDS, System.currentTimeMillis()
                - TIME_SENT_DELTA_MILLISECONDS);
    }

    @And("^I click Change Password button on Password Change Request page$")
    public void IClickChangePasswordButton() throws Exception {
        context.getPagesCollection().getPage(PasswordChangeRequestPage.class)
                .clickChangePasswordButton();
    }

    @Then("^I see Password Change Request Succeeded page$")
    public void ISeeRequestSucceededPage() throws Exception {
        assertThat(
                context.getPagesCollection().getPage(
                        PasswordChangeRequestSuccessfullPage.class)
                        .isConfirmationTextVisible(), is(true));
    }

    @Then("^I see unused mail message$")
    public void ISeeUnusedMailMessage() throws Exception {
        assertThat(
                context.getPagesCollection().getPage(
                        PasswordChangeRequestSuccessfullPage.class)
                        .isUnusedTextVisible(), is(true));
    }

    @When("^I open Password Change link from the received email$")
    public void IOpenPasswordChangeLinkFromEmail() throws Exception {
        if (this.passwordChangeMessage == null) {
            throw new RuntimeException(
                    "Please call the corresponding step to initialize email listener first");
        }
        final PasswordResetMessage wrappedMsg = new PasswordResetMessage(
                this.passwordChangeMessage.get());
        context.getPagesCollection().getPage(PasswordChangePage.class).setUrl(
                wrappedMsg.extractPasswordResetLink());
        context.getPagesCollection().getPage(PasswordChangePage.class).navigateTo();
    }

    @When("^I open Password Change link from the received email for (.*)$")
    public void IOpenPasswordChangeLinkFromEmailFor(String agent) throws Exception {
        if (this.passwordChangeMessage == null) {
            throw new RuntimeException(
                    "Please call the corresponding step to initialize email listener first");
        }
        final PasswordResetMessage wrappedMsg = new PasswordResetMessage(
                this.passwordChangeMessage.get());
        String link = wrappedMsg.extractPasswordResetLink() + "&agent=" + agent;
        context.getPagesCollection().getPage(PasswordChangePage.class).setUrl(link);
        context.getPagesCollection().getPage(PasswordChangePage.class).navigateTo();
    }

    @When("^I open Password Change link from the received email with wrong checksum for (.*)$")
    public void IOpenPasswordChangeLinkFromEmailForWithWrongChecksum(String agent) throws Exception {
        if (this.passwordChangeMessage == null) {
            throw new RuntimeException(
                    "Please call the corresponding step to initialize email listener first");
        }
        final PasswordResetMessage wrappedMsg = new PasswordResetMessage(
                this.passwordChangeMessage.get());
        String link = wrappedMsg.extractPasswordResetLink() + "_WRONG_CHECKSUM_" + "&agent=" + agent;
        context.getPagesCollection().getPage(PasswordChangePage.class).setUrl(link);
        context.getPagesCollection().getPage(PasswordChangePage.class).navigateTo();
    }

    @When("^I see password change mail in (.*) with (.*)$")
    public void ISeePasswordChangeMailInLanguage(String language, String message) throws Exception {
        final String content = BackendAPIWrappers
                .getMessageContent(this.passwordChangeMessage);
        switch (language) {
            case "de":
                assertThat("E-Mail is not German.", content, containsString(message));
                break;
            case "en":
                assertThat("E-Mail is not English.", content, containsString(message));
                break;
            default:
                break;
        }
    }
}
