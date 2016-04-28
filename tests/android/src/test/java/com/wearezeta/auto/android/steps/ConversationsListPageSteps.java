package com.wearezeta.auto.android.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.misc.ElementState;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationsListPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private ConversationsListPage getConversationsListPage() throws Exception {
        return pagesCollection.getPage(ConversationsListPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Verify whether conversations list is visible or not
     *
     * @throws Exception
     * @step. ^I see Contact list with (no )?contacts$
     */
    @Given("^I see Contact list with (no )?contacts$")
    public void GivenISeeContactList(String shouldNotBeVisible) throws Exception {
        getConversationsListPage().verifyContactListIsFullyLoaded();
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(
                    "No conversations are visible in the conversations list, but some are expected",
                    getConversationsListPage().isAnyConversationVisible());
        } else {
            Assert.assertTrue(
                    "Some conversations are visible in the conversations list, but zero is expected",
                    getConversationsListPage().isNoConversationsVisible());

        }
    }

    /**
     * Verify whether conversations list is visible
     *
     * @throws Exception
     * @step. ^I see Contact list$
     */
    @Given("^I see Contact list$")
    public void GivenISeeContactList() throws Exception {
        getConversationsListPage().verifyContactListIsFullyLoaded();
    }

    /**
     * Taps on a given contact name
     *
     * @param contactName the contact to tap on
     * @throws Exception
     * @step. ^I tap on contact name (.*)$
     */
    @When("^I tap on contact name (.*)$")
    public void WhenITapOnContactName(String contactName) throws Exception {
        try {
            contactName = usrMgr.findUserByNameOrNameAlias(contactName).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getConversationsListPage().tapOnName(contactName);
    }

    /**
     * Taps on the profile icon at the bottom of convo list
     *
     * @throws Exception
     * @step. ^I tap conversations list settings button$
     */
    @When("^I tap conversations list settings button$")
    public void ITapConvoSettings() throws Exception {
        getConversationsListPage().tapListSettingsButton();
    }

    /**
     * Swipes right on a contact to archive the conversation with that contact
     * (Perhaps this step should only half swipe to reveal the archive button,
     * since the "I swipe archive conversation" step exists)
     *
     * @param contact the contact or conversation name on which to swipe
     * @throws Exception
     * @step. ^I swipe right on a (.*)$
     */
    @When("^I swipe right on a (.*)$")
    public void ISwipeRightOnContact(String contact) throws Exception {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently - seems bad...
        }
        getConversationsListPage().swipeRightOnConversation(1000, contact);
    }

    /**
     * Makes a short swipe on teh contact to get the 3 dots for the option menu
     *
     * @param contact to swipe on
     * @throws Exception
     * @step. ^I short swipe right on a (.*)
     */
    @When("^I short swipe right on a (.*)$")
    public void IShortSwipeRightOnAUser(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getConversationsListPage().swipeShortRightOnConversation(1000, contact);

    }

    /**
     * Tap the corresponding button to open Search UI
     *
     * @throws Exception
     * @step. I open [Ss]earch UI$
     */
    @When("^I open [Ss]earch UI$")
    public void IOpenSearchUI() throws Exception {
        getConversationsListPage().tapListActionsAvatar();
    }

    /**
     * Swipes up on the contact list to reveal archived conversations
     *
     * @throws Exception
     * @step. ^I swipe up contact list$
     */
    @When("^I swipe up contact list$")
    public void ISwipeUpContactList() throws Exception {
        getConversationsListPage().doLongSwipeUp();
    }

    /**
     * Asserts that two given contact names exist in the conversation list
     * (should this maybe be set to a list?)
     *
     * @param shouldNotSee equals to null if the conversation should be present
     * @param contacts     The list of comma-separated contact names/aliases
     * @throws Exception
     * @step. ^I see group conversation with (.*) in conversations list$
     */
    @Then("^I (do not )?see group conversation with (.*) in conversations list$")
    public void ISeeGroupChatInContactList(String shouldNotSee, String contacts) throws Exception {
        final List<String> users = new ArrayList<>();
        for (String alias : CommonSteps.splitAliases(contacts)) {
            users.add(usrMgr.findUserByNameOrNameAlias(alias).getName());
        }
        if (shouldNotSee == null) {
            Assert.assertTrue(
                    String.format("The is no group conversation with users '%s' in the list",
                            StringUtils.join(",", users)),
                    getConversationsListPage().isConversationItemExist(users));
        } else {
            Assert.assertTrue(
                    String.format("The group conversation with users '%s' in present the list, but it should not",
                            StringUtils.join(",", users)),
                    getConversationsListPage().isConversationItemNotExist(users));
        }
    }

    /**
     * Check to see that the contact hint banner is visible in the bottom of contact list
     *
     * @param shouldNotSee equals to null if "do not" part does not exist
     * @throws Exception
     */
    @Then("^I( do not)? see contact hint banner$")
    public void ISeeContactsHintBanner(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The contact hint banner is not visible in the list",
                    getConversationsListPage().isContactsBannerVisible());
        } else {
            Assert.assertTrue("The contact hint banner is visible in the list, but should be hidden",
                    getConversationsListPage().isContactsBannerNotVisible());
        }
    }

    /**
     * Check to see that a given username appears in the contact list
     *
     * @param userName     the username to check for in the contact list
     * @param shouldNotSee equals to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I( do not)? see contact list with name (.*)$
     */
    @Then("^I( do not)? see contact list with name (.*)$")
    public void ISeeUserNameInContactList(String shouldNotSee, String userName)
            throws Exception {
        try {
            userName = usrMgr.findUserByNameOrNameAlias(userName).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getConversationsListPage().verifyContactListIsFullyLoaded();
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The conversation '%s' is not visible in the list",
                    userName), getConversationsListPage().isConversationVisible(userName));
        } else {
            Assert.assertTrue(String.format("The conversation '%s' is  visible in the list, but should be hidden",
                    userName), getConversationsListPage().waitUntilConversationDisappears(userName));
        }
    }

    /**
     * Check to see that a given username appears in the contact list
     *
     * @param timeoutSeconds conversation visibility timeout
     * @param convoName      conversation name/alias
     * @param expectedAction either 'appears in' or 'disappears from'
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds? until conversation (.*) (appears in|disappears from) the list$
     */
    @Then("^I wait up to (\\d+) seconds? until conversation (.*) (appears in|disappears from) the list$")
    public void IWaitForConvo(int timeoutSeconds, String convoName, String expectedAction) throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        if (expectedAction.equals("appears in")) {
            Assert.assertTrue(String.format("The conversation '%s' is not visible in the list",
                    convoName), getConversationsListPage().isConversationVisible(convoName, timeoutSeconds));
        } else {
            Assert.assertTrue(String.format("The conversation '%s' is  visible in the list, but should be hidden",
                    convoName), getConversationsListPage().waitUntilConversationDisappears(convoName, timeoutSeconds));
        }
    }

    /**
     * Checks to see that the muted symbol appears or not for the given contact.
     *
     * @param contact          user name/alias
     * @param shouldNotBeMuted is set to null if 'not' part does not exist
     * @throws Exception
     * @step. "^Contact (.*) is (not )?muted$
     */
    @Then("^Contact (.*) is (not )?muted$")
    public void ContactIsMutedOrNot(String contact, String shouldNotBeMuted)
            throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        if (shouldNotBeMuted == null) {
            Assert.assertTrue(
                    String.format("The conversation '%s' is supposed to be muted, but it is not", contact),
                    getConversationsListPage().isContactMuted(contact));
        } else {
            Assert.assertTrue(String.format(
                    "The conversation '%s' is supposed to be not muted, but it is", contact),
                    getConversationsListPage().waitUntilContactNotMuted(contact));
        }
    }

    private ElementState previousPlayPauseBtnState = null;

    /**
     * Save the current state of PlayPause button into the internal data
     * structure
     *
     * @param convoName conversation name for which the screenshot should be taken
     * @throws Exception
     * @step. ^I remember the state of PlayPause button next to the (.*)
     * conversation$
     */
    @When("^I remember the state of PlayPause button next to the (.*) conversation$")
    public void IRememberTheStateOfPlayPauseButton(String convoName)
            throws Exception {
        final String name = usrMgr.replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        previousPlayPauseBtnState = new ElementState(
                () -> getConversationsListPage().getScreenshotOfPlayPauseButtonNextTo(name).orElseThrow(
                        IllegalStateException::new)).remember();
    }

    private final static double MAX_SIMILARITY_THRESHOLD = 0.6;
    private final static int STATE_CHANGE_TIMEOUT_SECONDS = 5;

    /**
     * Verify whether the current screenshot of PlayPause button is different
     * from the previous one
     *
     * @param convoName conversation name/alias
     * @throws Exception
     * @step. ^I see the state of PlayPause button next to the (.*) conversation
     * is changed$
     */
    @Then("^I see the state of PlayPause button next to the (.*) conversation is changed$")
    public void ISeeThePlayPauseButtonStateIsChanged(String convoName) throws Exception {
        if (previousPlayPauseBtnState == null) {
            throw new IllegalStateException("Please take a screenshot of previous button state first");
        }
        convoName = usrMgr.findUserByNameOrNameAlias(convoName).getName();
        Assert.assertTrue(String.format(
                "The current and previous states of PlayPause button for '%s' conversation " +
                        "seems to be very similar after %d seconds",
                convoName, STATE_CHANGE_TIMEOUT_SECONDS),
                previousPlayPauseBtnState.isChanged(STATE_CHANGE_TIMEOUT_SECONDS, MAX_SIMILARITY_THRESHOLD));
    }

    /**
     * Verify that Play/Pause media content button is visible in Conversation
     * List
     *
     * @param convoName conversation name for which button presence is checked
     * @step. ^I see PlayPause media content button for conversation (.*)
     */
    @Then("^I see PlayPause media content button for conversation (.*)")
    public void ThenISeePlayPauseButtonForConvo(String convoName)
            throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName,
                FindBy.NAME_ALIAS);
        Assert.assertTrue(getConversationsListPage().isPlayPauseMediaButtonVisible(
                convoName));
    }

    /**
     * Tap PlayPause button next to the particular conversation name
     *
     * @param convoName conversation name/alias
     * @throws Exception
     * @step. ^I tap PlayPause button next to the (.*) conversation$
     */
    @When("^I tap PlayPause button next to the (.*) conversation$")
    public void ITapPlayPauseButton(String convoName) throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName,
                FindBy.NAME_ALIAS);
        getConversationsListPage().tapPlayPauseMediaButton(convoName);
    }

    /**
     * Tap the corresponding item in conversation settings menu
     *
     * @param itemName menu item name
     * @throws Exception
     * @step. ^I select (.*) From conversation settings menu$
     */
    @And("^I select (.*) from conversation settings menu$")
    public void ISelectConvoSettingsMenuItem(String itemName) throws Exception {
        getConversationsListPage().selectConvoSettingsMenuItem(itemName);
    }

    private Map<String, ElementState> previousUnreadIndicatorState = new HashMap<>();

    /**
     * Save the state of conversation idicator into the internal field for the
     * future comparison
     *
     * @param name conversation name/alias
     * @throws Exception
     * @step. ^I remember unread messages indicator state for conversation (.*)
     */
    @When("^I remember unread messages indicator state for conversation (.*)")
    public void IRememberUnreadIndicatorState(String name) throws Exception {
        final String convoName = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        this.previousUnreadIndicatorState.put(convoName,
                new ElementState(() -> getConversationsListPage().getMessageIndicatorScreenshot(convoName)).remember()
        );
    }

    private static final double MAX_UNREAD_DOT_SIMILARITY_THRESHOLD = 0.97;

    /**
     * Verify whether unread dot state is changed for the particular
     * conversation in comparison to the previous state
     *
     * @param name conversation name/alias
     * @throws Exception
     * @step. ^I see unread messages indicator state is changed for conversation
     * (.*)"
     */
    @Then("^I see unread messages indicator state is changed for conversation (.*)")
    public void ISeeUnreadIndicatorStateIsChanged(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (!this.previousUnreadIndicatorState.containsKey(name)) {
            throw new IllegalStateException(
                    String.format(
                            "Please invoke the corresponding step to make a screenshot of previous state of '%s' conversation",
                            name));
        }
        Assert.assertTrue(String.format(
                "The current and previous states of Unread Dot for conversation '%s' seems to be very similar",
                name),
                this.previousUnreadIndicatorState.get(name).isChanged(10, MAX_UNREAD_DOT_SIMILARITY_THRESHOLD));
    }

    /**
     * Presses Delete on the delete confirmation alert
     *
     * @throws Exception
     * @step. ^I press DELETE on the confirm alert$
     */
    @When("^I press DELETE on the confirm alert$")
    public void IPressDELETEOnTheConfirmAlert() throws Exception {
        getConversationsListPage().confirmDeleteConversationAlert();
    }

    /**
     * Presses the leave as well during delete checkbox
     *
     * @throws Exception
     * @step. ^I click the Leave check box$
     */
    @When("^I click the Leave check box$")
    public void IClickLeave() throws Exception {
        getConversationsListPage().checkLeaveWhileDeleteCheckbox();
    }

    /**
     * Verifies that specific items are visible in the conversation settings menu
     *
     * @param name name of item to check in the menu (ARCHIVE, BLOCK, CANCEL,...)
     * @throws Exception
     * @step. ^I see (.*) button in conversation settings menu$
     */
    @Then("^I see (.*) button in conversation settings menu$")
    public void ISeeButtonInConversationSettingsMenuAtPosition(String name) throws Exception {
        Assert.assertTrue("The converastion settings menu item is not visible",
                getConversationsListPage().isConvSettingsMenuItemVisible(name));
    }

    /**
     * Verifies if Leave check box is visible or not
     *
     * @param shouldNotSee equals to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I( do not)? see the Leave check box$
     */
    @Then("^I( do not)? see the Leave check box$")
    public void ISeeTheLeaveCheckBox(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(getConversationsListPage().isLeaveCheckBoxVisible());
        } else {
            Assert.assertFalse(getConversationsListPage().isLeaveCheckBoxVisible());
        }
    }

    /**
     * Verifies that the three dot button to open the option menu is visible
     *
     * @throws Exception
     * @step. ^I see three dots option menu button$
     */
    @When("^I see three dots option menu button$")
    public void ISeeThreeDotsOptionMenuButton() throws Exception {
        Assert.assertTrue("Three dot button is not visible",
                getConversationsListPage().isThreeDotButtonVisible());
    }

    /**
     * Taps the three dot button to open the option menu
     *
     * @throws Exception
     * @step. ^I press the three dots option menu button$
     */
    @When("^I press the three dots option menu button$")
    public void IPressTheThreeDotsOptionMenuButton() throws Exception {
        getConversationsListPage().tapThreeDotOptionMenuButton();
    }

}