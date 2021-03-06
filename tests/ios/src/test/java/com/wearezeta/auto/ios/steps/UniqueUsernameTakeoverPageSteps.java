package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.UniqueUsernameTakeoverPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;


public class UniqueUsernameTakeoverPageSteps {
    private UniqueUsernameTakeoverPage getPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(UniqueUsernameTakeoverPage.class);
    }

    /**
     * Tap the corresponding button щn Unique Username Takeover page$
     *
     * @param buttonName one of possible button names
     * @throws Exception
     * @step. ^I tap (Choose Yours|Keep This One) button on Unique Username Takeover page$
     */
    @When("^I tap (Choose Yours|Keep This One) button on Unique Username Takeover page$")
    public void ITapButton(String buttonName) throws Exception {
        getPage().tapButton(buttonName);
    }

    /**
     * Verify whether username is visible on the ttakeover page
     *
     * @param shouldNotSee     equals to null if the name should be visible
     * @param isUnique         if present then unique username will be verified otherwise the 'simple' one
     * @param startsWith       equals to null if there should be complete name accordance
     * @param expectedUsername name, unique username or an alias
     * @throws Exception
     * @step. ^I (do not )?see (unique )?username (starts with )?(.*) on Unique Username Takeover page
     */
    @Then("^I (do not )?see (unique )?username (starts with )?(.*) on Unique Username Takeover page$")
    public void ISeeUniqueUsername(String shouldNotSee, String isUnique, String startsWith, String expectedUsername)
            throws Exception {
        expectedUsername = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(expectedUsername, ClientUsersManager.FindBy.NAME_ALIAS);
        expectedUsername = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(expectedUsername, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
        if (isUnique == null) {
            if (shouldNotSee == null) {
                Assert.assertTrue(String.format("Name '%s' is not visible", expectedUsername),
                        getPage().isUsernameVisible(startsWith != null, expectedUsername));
            } else {
                Assert.assertTrue(String.format("Name '%s' should not be visible", expectedUsername),
                        getPage().isUsernameInvisible(startsWith != null, expectedUsername));
            }
        } else {
            if (shouldNotSee == null) {
                Assert.assertTrue(String.format("Unique username '%s' is not visible", expectedUsername),
                        getPage().isUniqueUsernameVisible(startsWith != null, expectedUsername));
            } else {
                Assert.assertTrue(String.format("Unique username '%s' should not be visible", expectedUsername),
                        getPage().isUniqueUsernameInvisible(startsWith != null, expectedUsername));
            }
        }
    }

    /**
     * Verify that Unique Username is a string with only latin letters
     *
     * @throws Exception
     * @step. ^I see unique username on Unique Username Takeover page contains latin characters only$
     */
    @Then("^I see unique username on Unique Username Takeover page contains latin characters only$")
    public void ISeeUniqueNameIsLatingCharsString() throws Exception {
        Assert.assertTrue("Unique Username contains non-alpha values", getPage().isUniqueUserNameAlphaString());
    }

    private String previousUniqueUsername = null;

    /**
     * Remember the unique username currently shown on takeover page
     *
     * @throws Exception
     * @step. ^I remember the unique username on Unique Username Takeover page$"
     */
    @When("^I remember the unique username on Unique Username Takeover page$")
    public void IRememberUniqueUsername() throws Exception {
        previousUniqueUsername = getPage().getUniqueNameValue();
    }

    /**
     * Verify whether the previously remebered username is properly applied to the backed
     *
     * @throws Exception
     * @step. ^I verify my unique username on the backend is equal to the one which was visible on Unique Username Takeover page$"
     */
    @Then("^I verify my unique username on the backend is equal to the one which was visible on Unique Username Takeover page$")
    public void IVerifyBackendUniqueUsername() throws Exception {
        if (previousUniqueUsername == null) {
            throw new IllegalStateException("The previous unique username should be remembered first");
        }
        final String currentUniqueUsername = "@" + BackendAPIWrappers.getUniqueUsername(
                IOSTestContextHolder.getInstance().getTestContext().getUsersManager().getSelfUserOrThrowError()
        ).orElse("<NOT_SET>");
        Assert.assertEquals(
                String.format("The current unique username '%s' is not equal to the previously remembered one '%s'",
                        currentUniqueUsername, previousUniqueUsername), previousUniqueUsername, currentUniqueUsername);
    }
}
