package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.common.misc.ElementState;
import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationsListPage;
import com.wearezeta.auto.android_tablet.pages.TabletPeoplePickerPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private TabletPeoplePickerPage getPeoplePickerPage() throws Exception {
        return pagesCollection.getPage(TabletPeoplePickerPage.class);
    }

    private TabletConversationsListPage getConversationsListPage() throws Exception {
        return pagesCollection.getPage(TabletConversationsListPage.class);
    }

    /**
     * Verify that People Picker is visible or not.
     * Sometimes it's quite hard to make sure this page is not visible and that is why we check whether Top People
     * overlay is shown or not
     *
     * @param shouldNotBeVisible equals to null is "do not" part does not exist
     * @throws Exception
     * @step. ^I (do not )?see People Picker page$
     */
    @When("^I (do not )?see People Picker page$")
    public void WhenITapOnTabletCreateConversation(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("People Picker page is not visible", getPeoplePickerPage().waitUntilVisible());
        } else {
            Assert.assertTrue(
                    "People Picker page is visible, but should be hidden", getPeoplePickerPage().waitUntilInvisible());
        }
    }

    /**
     * Enter user name or email into the corresponding People Picker field
     *
     * @param searchCriteria user name/email/phone number or the corresponding aliases
     * @throws Exception
     * @step. ^I enter \"(.*)\" into Search input on People [Pp]icker page$
     */
    @When("^I enter \"(.*)\" into Search input on People [Pp]icker page")
    public void IEnterStringIntoSearchField(String searchCriteria) throws Exception {
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria, FindBy.EMAIL_ALIAS);
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria, FindBy.NAME_ALIAS);
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria, FindBy.PHONENUMBER_ALIAS);
        getPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
    }

    /**
     * Tap one of found items in People Picker results
     *
     * @param item user name/email/phone number or the corresponding aliases
     * @throws Exception
     * @step. ^I tap the found item (.*) on [Pp]eople [Pp]icker page$
     */
    @When("^I tap the found item (.*) on [Pp]eople [Pp]icker page$")
    public void ITapUserName(String item) throws Exception {
        item = usrMgr.replaceAliasesOccurences(item, FindBy.EMAIL_ALIAS);
        item = usrMgr.replaceAliasesOccurences(item, FindBy.NAME_ALIAS);
        item = usrMgr.replaceAliasesOccurences(item, FindBy.PHONENUMBER_ALIAS);
        getPeoplePickerPage().tapFoundItem(item);
    }

    /**
     * Check whether the particular user avatar is visible
     *
     * @param shouldNotSee equals to null if "do not " part does not exist in the step
     *                     description
     * @param name         user name/alias
     * @param isGroup      is not null if group avatar should be verified instead of single user avatar
     * @throws Exception
     * @step. ^I see "(.*)" (group )?avatar on [Pp]eople [Pp]icker page$
     */
    @When("^I (do not )?see \"(.*)\" (group )?avatar on [Pp]eople [Pp]icker page$")
    public void ISeeContactAvatar(String shouldNotSee, String name, String isGroup) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            if (isGroup == null) {
                Assert.assertTrue(String.format(
                        "The avatar for contact '%s' is not visible", name),
                        getPeoplePickerPage().waitUntilAvatarIsVisible(name));
            } else {
                Assert.assertTrue(String.format(
                        "The group avatar '%s' is not visible", name),
                        getPeoplePickerPage().waitUntilGroupAvatarIsVisible(name));
            }
        } else {
            if (isGroup == null) {
                Assert.assertTrue(String.format(
                        "The avatar for contact '%s' is visible", name),
                        getPeoplePickerPage().waitUntilAvatarIsInvisible(name));
            } else {
                Assert.assertTrue(String.format(
                        "The group avatar '%s' is visible", name),
                        getPeoplePickerPage().waitUntilGroupAvatarIsInvisible(name));
            }

        }
    }

    private ElementState rememberedAvatar = null;
    final static double MAX_SIMILARITY_VALUE = 0.90;

    /**
     * Save the screenshot of current user avatar on People Picker page
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I remember (.*) avatar on [Pp]eople [Pp]icker page$
     */
    @When("^I remember (.*) avatar on [Pp]eople [Pp]icker page$")
    public void ITakeScreenshotOfContactAvatar(String name) throws Exception {
        final String convoName = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        this.rememberedAvatar = new ElementState(
                () -> getPeoplePickerPage().takeAvatarScreenshot(convoName).orElseThrow(IllegalStateException::new)
        ).remember();
    }

    /**
     * Compare the screenshot of current user avatar on People Picker page with
     * the previous one
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I verify (.*) avatar on [Pp]eople [Pp]icker page is not the same
     * as the previous one$
     */
    @Then("^I verify (.*) avatar on [Pp]eople [Pp]icker page is not the same as the previous one$")
    public void IVerifyAvatarIsNotTheSame(String name) throws Exception {
        final String convoName = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (this.rememberedAvatar == null) {
            throw new IllegalStateException("Please take a previous screenshot of user avatar first");
        }
        Assert.assertTrue(
                String.format("The current contact avatar of '%s' is very similar to the previous one", convoName),
                this.rememberedAvatar.isChanged(10, MAX_SIMILARITY_VALUE));
    }

    /**
     * Click the X button to close People Picker
     *
     * @throws Exception
     * @step. ^I close (?:the |\\s*)People Picker$
     */
    @And("^I close (?:the |\\s*)People Picker$")
    public void IClosePeoplePicker() throws Exception {
        getPeoplePickerPage().tapCloseButton();
    }

    private static final int TOP_PEOPLE_MAX_RETRIES = 5;

    /**
     * Try to reopen People Picker several times until Top People list is
     * visible
     *
     * @throws Exception
     * @step. ^I keep on reopening People Picker until I see Top People$
     */
    @And("^I keep on reopening People Picker until I see Top People$")
    public void IReopenPeoplePickerUntilTopPeopleAppears() throws Exception {
        int ntry = 1;
        while (!getPeoplePickerPage().waitUntilTopPeopleIsVisible()
                && ntry <= TOP_PEOPLE_MAX_RETRIES) {
            getPeoplePickerPage().tapCloseButton();
            Thread.sleep(3000);
            getConversationsListPage().tapSearchInput();
            ntry++;
        }
        if (ntry >= TOP_PEOPLE_MAX_RETRIES) {
            throw new AssertionError(String.format(
                    "Top People list was not shown after %s retries", ntry));
        }
    }

    /**
     * Tap the particular Top People avatar
     *
     * @param name name/alias
     * @throws Exception
     * @step. ^I tap (.*) avatar in Top People$
     */
    @When("^I tap (.*) avatar in Top People$")
    public void ITapAvatarInTopPeople(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getPeoplePickerPage().tapTopPeopleAvatar(name);
    }

    private enum SwipeType {
        LONG, SHORT
    }

    /**
     * Perform long/short swipe down on People Picker page
     *
     * @param swipeTypeStr see SwipeType enum for the list of available values
     * @throws Exception
     * @step. ^I do (long|short) swipe down on [Pp]eople [Pp]icker page$
     */
    @When("^I do (long|short) swipe down on [Pp]eople [Pp]icker page$")
    public void IDoSwipeDown(String swipeTypeStr) throws Exception {
        final SwipeType swipeType = SwipeType.valueOf(swipeTypeStr
                .toUpperCase());
        switch (swipeType) {
            case SHORT:
                getPeoplePickerPage().doShortSwipeDown();
                break;
            case LONG:
                getPeoplePickerPage().doLongSwipeDown();
                break;
            default:
                throw new IllegalStateException(String.format(
                        "Swipe type '%s' is not supported", swipeTypeStr));
        }
    }

    /**
     * Tap the corresponding action button
     *
     * @param buttonName   one of possible action button names
     * @throws Exception
     * @step. ^I tap (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on [Pp]eople [Pp]ickerpage$
     */
    @When("^I tap (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on [Pp]eople [Pp]icker page$")
    public void ITapActionButtons(String buttonName) throws Exception {
        getPeoplePickerPage().tapActionButton(buttonName);
    }

    /**
     * Verify action button presence
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param buttonName   one of possible action button names
     * @throws Exception
     * @step. ^I (do not )?see (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on [Pp]eople [Pp]ickerpage$
     */
    @Then("^I (do not )?see (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on [Pp]eople [Pp]icker page$")
    public void ISeeActionButton(String shouldNotSee, String buttonName) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("'%s' action button is not visible", buttonName),
                    getPeoplePickerPage().waitUntilActionButtonIsVisible(buttonName));
        } else {
            Assert.assertTrue(String.format("'%s' action button is not visible", buttonName),
                    getPeoplePickerPage().waitUntilActionButtonIsInvisible(buttonName));
        }
    }

    /**
     * Tap Backspace in the search field
     *
     * @throws Exception
     * @step. ^I type backspace in Search input on [Pp]eople [Pp]icker page$
     */
    @When("^I type backspace in Search input on [Pp]eople [Pp]icker page$")
    public void ITypeBackspace() throws Exception {
        getPeoplePickerPage().typeBackspaceInSearchInput();
    }
}
