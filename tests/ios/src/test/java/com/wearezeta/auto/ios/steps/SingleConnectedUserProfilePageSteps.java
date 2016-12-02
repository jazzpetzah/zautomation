package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.misc.ElementState;
import cucumber.api.java.en.Then;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.details_overlay.single.SingleConnectedUserProfilePage;

import cucumber.api.java.en.When;

public class SingleConnectedUserProfilePageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private SingleConnectedUserProfilePage getPage() throws Exception {
        return pagesCollection.getPage(SingleConnectedUserProfilePage.class);
    }

    /**
     * Tap the corresponding button on participant profile page
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Create Group|X|Open Menu) button on Single user profile page$
     */
    @When("^I tap (Create Group|X|Open Menu) button on Single user profile page$")
    public void ITapButton(String btnName) throws Exception {
        getPage().tapButton(btnName);
    }

    /**
     * Verify user name on Single user profile page
     *
     * @param value     user name or email
     * @param fieldType either name or email
     * @throws Exception
     * @step. ^I see (.*) (name|email) on Single user profile page$
     */
    @When("^I (do not )?see (.*) (name|email) on Single user profile page$")
    public void IVerifyUserOtherUserProfilePage(String shouldNotSee, String value, String fieldType) throws Exception {
        boolean result;
        switch (fieldType) {
            case "name":
                value = usrMgr.replaceAliasesOccurences(value, ClientUsersManager.FindBy.NAME_ALIAS);
                if (shouldNotSee == null) {
                    result = getPage().isNameVisible(value);
                } else {
                    result = getPage().isNameInvisible(value);
                }
                break;
            case "email":
                value = usrMgr.replaceAliasesOccurences(value, ClientUsersManager.FindBy.EMAIL_ALIAS);
                if (shouldNotSee == null) {
                    result = getPage().isEmailVisible(value);
                } else {
                    result = getPage().isEmailInvisible(value);
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown field type '%s'", fieldType));
        }
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("'%s' field is expected to be visible", value), result);
        } else {
            Assert.assertTrue(String.format("'%s' field is expected to be invisible", value), result);
        }
    }

    private String userAddressBookName = null;

    /**
     * Remembers the name of the user how he is saved in the Address Book
     *
     * @param addressbookName name of user in Address Book
     * @throws Exception
     * @step. ^I remember the name of user (.*) in Address Book$
     */
    @When("^I remember the name of user (.*) in Address Book$")
    public void IRememberTheUsersAddressBookName(String addressbookName) throws Exception {
        userAddressBookName = usrMgr.replaceAliasesOccurences(addressbookName, ClientUsersManager.FindBy.NAME_ALIAS);
    }

    /**
     * Verifies that the Address Book name of the user is displayed
     *
     * @throws Exception
     * @step. ^I verify the previously remembered user name from Address Book is displayed on Single user profile page$
     */
    @Then("^I verify the previously remembered user name from Address Book is displayed on Single user profile page$")
    public void IVerifyUsersAddressBookNameOnOtherUserProfilePageIsDisplayed() throws Exception {
        if (userAddressBookName == null) {
            throw new IllegalStateException("Save the Address Book name of the user first!");
        }
        Assert.assertTrue(String.format("User Address Book name '%s' is not visible", userAddressBookName),
                getPage().isAddressBookNameVisible(userAddressBookName));
    }

    /**
     * Click on Devices button
     *
     * @param tabName either Devices or Details
     * @throws Exception
     * @step. ^I switch to (Devices|Details) tab$
     */
    @When("^I switch to (Devices|Details) tab on Single user profile page$")
    public void IChangeTab(String tabName) throws Exception {
        getPage().switchToTab(tabName);
    }

    /**
     * Verify whether the shield icon is visible on conversation details page
     *
     * @param shouldNotSee equals to null if the shield should be visible
     * @throws Exception
     * @step. ^I (do not )?see shield icon on Single user profile page$
     */
    @Then("^I (do not )?see shield icon on Single user profile page$")
    public void ISeeShieldIcon(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The shield icon is not visible on convo details page",
                    getPage().isShieldIconVisible());
        } else {
            Assert.assertTrue("The shield icon is still visible on convo details page",
                    getPage().isShieldIconNotVisible());
        }
    }

    private static final int PROFILE_PICTURE_CHANGE_TIMEOUT_SECONDS = 7;
    private static final double PROFILE_PICTURE_MAX_SCORE = 0.7;

    private final ElementState profilePictureState = new ElementState(
            () -> getPage().getProfilePictureScreenshot()
    );

    /**
     * Remember the current sate of user profile picture
     *
     * @throws Exception
     * @step. ^I remember user picture on Single user profile page$
     */
    @When("^I remember user picture on Single user profile page$")
    public void IRememberPicture() throws Exception {
        profilePictureState.remember();
    }

    /**
     * Verify whether user profile picture has been changed or not
     *
     * @param shouldNotBeChanged equals to null if the picture should stay the same
     * @throws Exception
     * @step. ^I see user picture is (not )?changed on Single user profile page$"
     */
    @Then("^I see user picture is (not )?changed on Single user profile page$")
    public void IVerifyPicture(String shouldNotBeChanged) throws Exception {
        if (shouldNotBeChanged == null) {
            Assert.assertTrue("User profile picture is still the same",
                    profilePictureState.isChanged(PROFILE_PICTURE_CHANGE_TIMEOUT_SECONDS, PROFILE_PICTURE_MAX_SCORE));
        } else {
            Assert.assertTrue("User profile picture is expected to be the same",
                    profilePictureState.isNotChanged(PROFILE_PICTURE_CHANGE_TIMEOUT_SECONDS, PROFILE_PICTURE_MAX_SCORE));
        }
    }
}
