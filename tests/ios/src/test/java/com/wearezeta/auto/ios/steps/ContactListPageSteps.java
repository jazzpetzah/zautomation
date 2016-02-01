package com.wearezeta.auto.ios.steps;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import cucumber.api.java.en.*;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.*;

public class ContactListPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ContactListPage getContactListPage() throws Exception {
        return pagesCollection.getPage(ContactListPage.class);
    }

    private LoginPage getLoginPage() throws Exception {
        return pagesCollection.getPage(LoginPage.class);
    }

    private PersonalInfoPage getPersonalInfoPage() throws Exception {
        return pagesCollection.getPage(PersonalInfoPage.class);
    }

    @Given("^I see conversations list$")
    public void GivenISeeConversationsList() throws Exception {
        Assert.assertTrue("Conversations list is not visible after the timeout", getLoginPage().isLoginFinished());
    }

    private Map<String, BufferedImage> savedConvoItemScreenshots = new HashMap<>();

    /**
     * Store the screenshot of a particular conversation list entry
     *
     * @param nameAlias conversation name/alias
     * @throws Exception
     * @step. ^I remember the state of (.*) conversation item$
     */
    @When("^I remember the state of (.*) conversation item$")
    public void IRememberConvoItemState(String nameAlias) throws Exception {
        final String name = usrMgr.replaceAliasesOccurences(nameAlias, FindBy.NAME_ALIAS);
        this.savedConvoItemScreenshots.put(name, getContactListPage().getConversationEntryScreenshot(name));
    }

    /**
     * Verify whether the previous conversation state is the same or different to the current state
     *
     * @param nameAlias          conversation name/alias
     * @param shouldNotBeChanged equals to null if the state should be changed
     * @throws Exception
     * @step. ^I see the state of (.*) conversation item is (not )?changed$"
     */
    @Then("^I see the state of (.*) conversation item is (not )?changed$")
    public void IVerifyConvoState(String nameAlias, String shouldNotBeChanged) throws Exception {
        final String name = usrMgr.replaceAliasesOccurences(nameAlias, FindBy.NAME_ALIAS);
        if (!this.savedConvoItemScreenshots.containsKey(name)) {
            throw new IllegalStateException(String.format(
                    "Please take a screenshot of '%s' conversation entry first", name));
        }
        final BufferedImage actualConvoItemScreenshot = getContactListPage().getConversationEntryScreenshot(name);
        final double score = ImageUtil.getOverlapScore(this.savedConvoItemScreenshots.get(name),
                actualConvoItemScreenshot, ImageUtil.RESIZE_NORESIZE);
        final double minScore = 0.97;
        if (shouldNotBeChanged == null) {
            Assert.assertTrue(
                    String.format("The state of '%s' conversation item seems to be the same (%.2f >= %.2f)",
                            name, score, minScore), score < minScore);
        } else {
            Assert.assertTrue(
                    String.format("The state of '%s' conversation item seems to be changed (%.2f < %.2f)",
                            name, score, minScore), score >= minScore);
        }
    }

    /**
     * Verify label in Self button
     *
     * @param name username
     * @throws Exception
     * @step. ^I see my name (.*) first letter as label of Self Button$
     */
    @When("^I see my name (.*) first letter as label of Self Button$")
    public void ISeeFirstLetterAsLabelSelfButton(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertTrue(getContactListPage()
                .isSelfButtonContainingFirstNameLetter(name));
    }

    /**
     * Click MAYBE LATER on settings warning screen
     *
     * @throws Exception
     * @step. ^I dismiss settings warning$
     */
    @When("^I dismiss settings warning$")
    public void IDismissSettingsWarning() throws Exception {
        getLoginPage().dismissSettingsWarning();
    }

    @When("^I tap my avatar$")
    public void WhenITapOnMyName() throws Exception {
        getContactListPage().tapMyAvatar();
    }

    @When("^I tap on contact name (.*)")
    public void WhenITapOnContactName(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        getContactListPage().tapOnName(name);
    }

    @When("^I tap on group chat with name (.*)")
    public void WhenITapOnGroupChatName(String chatName) throws Exception {
        WhenITapOnContactName(chatName);
    }

    @When("^I swipe down contact list$")
    public void ISwipeDownContactList() throws Throwable {
        if (!CommonUtils.getIsSimulatorFromConfig(IOSPage.class)) {
            getContactListPage().swipeDown(500);
        } else {
            getContactListPage().swipeDownSimulator();
        }
    }

    /**
     * Open search by taping on search field
     *
     * @throws Exception
     * @step. ^I open search by taping on it$
     */
    @When("^I open search by taping on it$")
    public void IOpenSearchByTap() throws Exception {
        getContactListPage().openSearch();
    }

    @Then("^I see first item in contact list named (.*)$")
    public void ISeeUserNameFirstInContactList(String value) throws Throwable {
        try {
            value = usrMgr.findUserByNameOrNameAlias(value).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        Assert.assertEquals("User name doesn't appeared in contact list.",
                value, getContactListPage().getFirstConversationName());
    }

    /**
     * verifies the visibility of a specific user in the contact list
     *
     * @param value username value string
     * @throws AssertionError if the user does not exist
     * @step. ^I see user (.*) in contact list$
     */
    @Then("^I see user (.*) in contact list$")
    public void ISeeUserInContactList(String value) throws Throwable {
        value = usrMgr.replaceAliasesOccurences(value, FindBy.NAME_ALIAS);
        Assert.assertTrue(getContactListPage().isChatInContactList(value));
    }

    @When("^I create group chat with (.*) and (.*)$")
    public void ICreateGroupChat(String contact1, String contact2) throws Exception {
        WhenITapOnContactName(contact1);
        DialogPageSteps dialogSteps = new DialogPageSteps();
        dialogSteps.IOpenConversationDetails();

        OtherUserPersonalInfoPageSteps infoPageSteps = new OtherUserPersonalInfoPageSteps();
        infoPageSteps.WhenIPressAddButton();

        PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
        pickerSteps.WhenITapOnSearchInputOnPeoplePickerPage();
        pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact2);
        pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact2);
        pickerSteps.ITapOnConversationFromSearch(contact2);
        pickerSteps.WhenIClickOnAddToConversationButton();

        GroupChatPageSteps groupChatSteps = new GroupChatPageSteps();
        groupChatSteps.ThenISeeGroupChatPage(String.format("%s%s%s",
                contact1, CommonSteps.ALIASES_SEPARATOR, contact2));
    }

    @When("^I swipe right on a (.*)$")
    public void ISwipeRightOnContact(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        getContactListPage().swipeRightConversationToRevealActionButtons(
                contact);
    }

    @Then("^I open archived conversations$")
    public void IOpenArchivedConversations() throws Exception {
        getContactListPage().swipeUp(1000);
    }

    @When("I see play/pause button next to username (.*) in contact list")
    public void ISeePlayPauseButtonNextToUserName(String contact)
            throws Exception {
        String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue("Play pause button is not shown",
                getContactListPage().isPlayPauseButtonVisible(name));
    }

    @When("I tap on play/pause button in contact list")
    public void ITapOnPlayPauseButtonInContactList() throws Exception {
        getContactListPage().tapPlayPauseButton();
    }

    @When("I tap play/pause button in contact list next to username (.*)")
    public void ITapPlayPauseButtonInContactListNextTo(String contact)
            throws Exception {
        String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getContactListPage().tapPlayPauseButtonNextTo(name);
    }

    @When("I see in contact list group chat named (.*)")
    public void ISeeInContactListGroupChatWithName(String name)
            throws Exception {
        Assert.assertTrue(getContactListPage().isChatInContactList(name));
    }

    @When("I click on Pending request link in contact list")
    public void ICcickPendingRequestLinkContactList() throws Throwable {
        getContactListPage().clickPendingRequest();
    }

    @When("I (dont )?see Pending request link in contact list")
    public void ISeePendingRequestLinkInContacts(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Pending request link is not in Contact list",
                    getContactListPage().isPendingRequestInContactList());
        } else {
            Assert.assertTrue("Pending request link is shown in contact list",
                    getContactListPage().pendingRequestInContactListIsNotShown());
        }
    }

    @When("I (don't )?see in contact list group chat with (.*)")
    public void ISeeInContactsGroupChatWith(String shouldNotSee, String participantNameAliases) throws Exception {
        participantNameAliases = usrMgr.replaceAliasesOccurences(participantNameAliases,
                ClientUsersManager.FindBy.NAME_ALIAS);
        final List<String> participantNames = CommonSteps.splitAliases(participantNameAliases);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("There is no conversation with '%s' in the list", participantNames),
                    getContactListPage().isConversationWithUsersExist(participantNames, 5));
        } else {
            Assert.assertFalse(String.format("There is conversation with '%s' in the list, which should be hidden",
                    participantNames), getContactListPage().isConversationWithUsersExist(participantNames, 2));
        }
    }

    /**
     * Verify that conversation with pointed name is not displayed in contact
     * list
     *
     * @param name conversation name to verify
     * @throws Exception
     * @step. I dont see conversation (.*) in contact list
     */
    @When("I dont see conversation (.*) in contact list")
    public void IDoNotSeeConversationInContactList(String name)
            throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Conversation '%s' is still displayed", name),
                getContactListPage().contactIsNotDisplayed(name));
    }

    /**
     * Click on archive button for a conversation
     *
     * @throws Exception if conversation is not found
     * @step. ^I click archive button for conversation$
     */
    @When("^I click archive button for conversation$")
    public void IClickArchiveConversationButton() throws Exception {
        getContactListPage().clickArchiveConversationButton();
    }

    /**
     * Verify that mute call button is shown in conversation list
     *
     * @throws Exception
     * @step. ^I see mute call button in conversation list$
     */
    @Then("^I see mute call button in conversation list$")
    public void ISeeMuteCallButtonInConversationList() throws Exception {
        Assert.assertTrue("Mute call button is not shown in conversation list",
                getContactListPage().isMuteCallButtonVisible());
    }

    /**
     * Click on mute call button in conversation list
     *
     * @throws Exception
     * @step. ^I click mute call button in conversation list
     */
    @Then("^I click mute call button in conversation list$")
    public void IClickMuteCallButtonInConversationList() throws Exception {
        getContactListPage().clickMuteCallButton();
    }

    /**
     * Checks that conversation name appears in displayed action menu
     *
     * @param conversation conversation name
     * @throws Exception
     * @step. ^I see conversation (.*) name in action menu in [Cc]ontact
     * [Ll]ist$
     */
    @And("^I see conversation (.*) name in action menu in [Cc]ontact [Ll]ist$")
    public void ISeeConversationNameInActionMenu(String conversation)
            throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation,
                FindBy.NAME_ALIAS);
        Assert.assertTrue("There is no conversation name " + conversation
                + " in opened action menu.", getContactListPage()
                .isActionMenuVisibleForConversation(conversation));
    }

    /**
     * Checks that specified button is exist in displayed action menu
     *
     * @param buttonTitle Silence | Delete | Leave | Archive | Block | Cancel
     * @throws Exception
     * @step. ^I see (Silence|Delete|Leave|Archive|Block|Cancel) button in
     * action menu in [Cc]ontact [Ll]ist$
     */
    @And("^I see (Silence|Delete|Leave|Archive|Block|Cancel) button in action menu in [Cc]ontact [Ll]ist$")
    public void ISeeXButtonInActionMenu(String buttonTitle) throws Exception {
        Assert.assertTrue("There is no button " + buttonTitle.toUpperCase()
                + " in opened action menu.", getContactListPage()
                .isButtonVisibleInActionMenu(buttonTitle));
    }

    /**
     * Clicks the Archive button in action menu of contact list
     *
     * @throws Throwable
     * @step. ^I press Archive button in action menu in Contact List$
     */
    @When("^I press Archive button in action menu in Contact List$")
    public void IPressArchiveButtonInActionMenuInContactList() throws Throwable {
        getContactListPage().clickArchiveButtonInActionMenu();
    }

    /**
     * Clicks the Leave button in action menu of contact list
     *
     * @throws Throwable
     * @step. ^I press Leave button in action menu in Contact List$
     */
    @When("^I press Leave button in action menu in Contact List$")
    public void IPressLeaveButtonInActionMenuInContactList() throws Throwable {
        getContactListPage().clickLeaveButtonInActionMenu();
    }

    /**
     * Clicks the Cancel button in action menu of contact list
     *
     * @throws Throwable
     * @step. ^I press Cancel button in action menu in Contact list$
     */
    @Then("^I press Cancel button in action menu in Contact List$")
    public void IPressCancelButtonInActionMenuInContactList() throws Throwable {
        getContactListPage().clickCancelButtonInActionMenu();
    }

    /**
     * Verifies that next conversation is selected in list
     *
     * @param conversation that is selected now
     * @throws Throwable
     * @step. ^I see conversation (.*) is selected in list$
     */
    @Then("^I see conversation (.*) is selected in list$")
    public void ISeeConversationIsSelectedInList(String conversation)
            throws Throwable {
        conversation = usrMgr.replaceAliasesOccurences(conversation,
                FindBy.NAME_ALIAS);
        Assert.assertEquals("Converstaion is not selected", "1",
                getContactListPage().getSelectedConversationCellValue(conversation).
                        orElseThrow(() -> new IllegalStateException("No conversations are selected in the list")));
    }

    /**
     * Verify if Invite more people button is shown in contact list
     *
     * @throws Exception
     * @step. ^I see Invite more people button$
     */
    @When("^I see Invite more people button$")
    public void ISeeInviteMorePeopleButton() throws Exception {
        Assert.assertTrue("Invite more people button is not shown",
                getContactListPage().isInviteMorePeopleButtonVisible());
    }

    /**
     * Verify if Invite more people button is NOT shown in contact list
     *
     * @throws Exception
     * @step. ^I DONT see Invite more people button$
     */
    @When("^I DONT see Invite more people button$")
    public void IDontSeeInviteMorePeopleButton() throws Exception {
        Assert.assertTrue("Invite more people button is shown",
                getContactListPage().isInviteMorePeopleButtonNotVisible());
    }

}
