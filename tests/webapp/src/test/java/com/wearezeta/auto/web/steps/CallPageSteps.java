package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.CallPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class CallPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
            .getInstance();
    
    /**
     * Click the accept call button in conversation list
     *
     * @throws Throwable
     * @step. ^I click the accept call button in conversation list$
     * @step. ^I accept the call from conversation (.*)$
     */
    @And("^I accept the call from conversation (.*)$")
    public void IClickAcceptCallButtonInConversationView(String conversationName) throws Throwable {
        webappPagesCollection.getPage(CallPage.class)
                .clickAcceptVideoCallButton();
    }

    /**
     * Click the decline call button in conversation list
     *
     * @throws Throwable
     * @step. ^I click the decline call button in conversation list$
     * @step. ^I ignore the call from conversation (.*)$
     */
    @And("^I ignore the call from conversation (.*)$")
    public void IClickDeclineCallButtonInConversationView(String conversationName) throws Throwable {
        webappPagesCollection.getPage(CallPage.class)
                .clickDeclineCallButton();
    }


    /**
     * Verifies if mute call button is visible for conversation
     *
     * @param doNot   is set to null if "do not" part does not exist
     * @param conversation conversation name string
     * @throws Exception
     * @step. ^I see mute call button for conversation (.*)
     */
    @When("^I( do not)? see mute call button for conversation (.*)")
    public void ISeeMuteCallButton(String doNot, String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            contactListPage.isMuteCallButtonVisibleForConversation(conversation);
        } else {
            contactListPage.isMuteCallButtonNotVisibleForConversation(conversation);
        }
    }

    /**
     * Verifies if video button is visible for conversation
     *
     * @param doNot   is set to null if "do not" part does not exist
     * @param conversation conversation name string
     * @throws Exception
     * @step. ^I see video button for conversation(.*)
     */
    @When("^I( do not)? see video button for conversation (.*)")
    public void ISeeVideoButton(String doNot, String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            contactListPage.isVideoButtonVisibleForConversation(conversation);
        } else {
            contactListPage.isVideoButtonNotVisibleForConversation(conversation);
        }
    }

    /**
     * Verifies if end call button is visible for conversation
     *
     * @param doNot   is set to null if "do not" part does not exist
     * @param conversation conversation name string
     * @throws Exception
     * @step. ^I see end call button for conversation(.*)
     */
    @When("^I( do not)? see end call button for conversation (.*)")
    public void ISeeEndCallButton(String doNot, String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            contactListPage.isEndCallButtonVisibleForConversation(conversation);
        } else {
            contactListPage.isEndCallButtonNotVisibleForConversation(conversation);
        }
    }

    /**
     * Clicks end call button for conversation
     *
     * @throws Exception
     * @step. ^I click end call button from conversation list$
     * @step. ^I hang up call with conversation (.*)$
     */
    @When("^I hang up call with conversation (.*)$")
    public void IClickEndCallButton(String conversationName) throws Exception {

        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        contactListPage.clickEndCallButton();
    }

    /**
     * Checks if your self video is visible
     *
     * @param doNot is set to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I( do not)? see my self video view$
     */
    @When("^I( do not)? see my self video view$")
    public void IDoNotSeeMySelfVideoView(String doNot)
            throws Exception {
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            contactListPage.isSelfVideoVisible();
        } else {
            contactListPage.isSelfVideoNotVisible();
        }
    }

    /**
     * Checks if the name of user you’re calling is visible
     *
     * @param nameAlias name of user whom you’re calling
     * @throws Exception
     * @step. ^I see name of user (.*) in calling banner in conversation list$
     * @step. ^I see the name of user (.*) in calling banner for conversation (.*)$
     */
    @Then("^I see the name of user (.*) in calling banner for conversation (.*)$")
    public void ISeeNameOfUserInCalling(String nameAlias, String conversationName) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(nameAlias);
        Assert.assertTrue(webappPagesCollection.getPage(
                CallPage.class).isUserNameVisibleInCallingBanner(user.getName()));
    }

    /**
     * Verifies if accept video call button is visible for conversation
     *
     * @param doNot   is set to null if "do not" part does not exist
     * @param conversation conversation name string
     * @throws Exception
     * @step. ^I( do not)? see accept video call button for conversation (.*)
     */
    @When("^I( do not)? see accept video call button for conversation (.*)")
    public void ISeeAcceptVideoCallButton(String doNot, String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            contactListPage.isAcceptVideoCallButtonVisibleForConversation(conversation);
        } else {
            contactListPage.isAcceptVideoCallButtonNotVisibleForConversation(conversation);
        }
    }

    /**
     * Verifies if decline call button is visible for conversation
     *
     * @param doNot   is set to null if "do not" part does not exist
     * @param contact conversation name string
     * @throws Exception
     * @step. ^I( do not)? see decline call button for conversation (.*)
     */
    @When("^I( do not)? see decline call button for conversation (.*)")
    public void ISeeDeclineCallButton(String doNot, String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            contactListPage.isDeclineCallButtonVisibleForConversation(contact);
        } else {
            contactListPage.isDeclineCallButtonNotVisibleForConversation(contact);
        }
    }

    /**
     * Clicks mute call button in conversation list
     *
     * @throws Exception
     * @step. ^I click mute call button in conversation list$
     * @step. ^I click mute call button for conversation (.*)$
     */
    @When("^I click mute call button for conversation (.*)$")
    public void IClickMuteCallButton(String conversationName) throws Exception {

        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        contactListPage.clickMuteCallButton();
    }

    /**
     * Checks if mute button in conversation list pressed
     *
     * @param doNot is set to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I see mute button in conversation list is( not)? pressed$
     * @step. ^I see mute button for conversation (.*) is( not)? pressed$
     */
    @When("^I see mute button for conversation (.*) is( not)? pressed$")
    public void ISeeMuteButtonNotPressed(String conversationName, String doNot)
            throws Exception {
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            contactListPage.isMuteCallButtonPressed();
        } else {
            contactListPage.isMuteCallButtonNotPressed();
        }
    }
}
