package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.UniqueUsernamePage;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.List;

public class UniqueUsernamePageSteps {
    private UniqueUsernamePage getUniqueUsernamePage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(UniqueUsernamePage.class);
    }

    private static String newUniqueName = null;

    static String getNewUniqueName() {
        if (newUniqueName == null) {
            throw new IllegalStateException("'newUniqueName' variable should be initialised first");
        }
        return newUniqueName;
    }

    /**
     * Verify visibility of Unique Username page
     *
     * @throws Exception
     * @step. ^I see Unique Username page$
     */
    @Then("^I see Unique Username page$")
    public void ISeeUsernamePage() throws Exception {
        Assert.assertTrue("Unique Username page is not visible", getUniqueUsernamePage().isPageHeaderVisible());
    }

    /**
     * Tap Save button on Unique Username page
     *
     * @throws Exception
     * @step. ^I tap (Save) button on Unique Username page$
     */
    @When("^I tap (Save) button on Unique Username page$")
    public void ITapButtonOnUniqueUsernamePage(String buttonName) throws Exception {
        getUniqueUsernamePage().tapButtonByName(buttonName);
    }

    /**
     * Fill in name input an string
     *
     * @param name string to be input
     * @throws Exception
     * @step. ^I enter "(.*)" name on Unique Username page$
     */
    @When("^I enter \"(.*)\" name on Unique Username page$")
    public void IFillInNameInInputOnUniqueUsernamePage(String name) throws Exception {
        name = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(name, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
        getUniqueUsernamePage().inputStringInNameInput(name);
    }

    /**
     * Verify Save button isEnable state
     *
     * @param expectedState Disabe/Enable
     * @throws Exception
     * @step. ^I see Save button state is (Disabled|Enabled) on Unique Username page$
     */
    @When("^I see Save button state is (Disabled|Enabled) on Unique Username page$")
    public void ISeeSaveButtonIsDisabled(String expectedState) throws Exception {
        boolean buttonState = getUniqueUsernamePage().isSaveButtonEnabled();
        if (expectedState.equals("Disabled")) {
            Assert.assertFalse(String.format("Wrong Save button state. Should be %s.", expectedState), buttonState);
        } else {
            Assert.assertTrue(String.format("Wrong Save button state. Should be %s.", expectedState), buttonState);
        }
    }

    /**
     * Attempt to enter X random latin alphanumeric chars as name
     *
     * @param count number of chars to enter
     * @throws Exception
     * @step. ^I attempt to enter (\d+) random latin alphanumeric chars as name on Unique Username page$
     */
    @When("^I attempt to enter (\\d+) random latin alphanumeric chars as name on Unique Username page$")
    public void IAttemtToEnterXRandomCharsAsName(int count) throws Exception {
        newUniqueName = getUniqueUsernamePage().inputXCharsRandomString(count);
    }

    /**
     * Verify Username length in name input
     *
     * @param count chars count to compare with
     * @throws Exception
     * @step. ^I see that name length is (less than|equal to) (\d+) chars? on Unique Username page$
     */
    @Then("^I see that name length is (less than|equal to) (\\d+) chars? on Unique Username page$")
    public void ISeeNameLenghIsLessThanXChars(String compareState, int count) throws Exception {
        if (compareState.equals("less than")) {
            Assert.assertTrue(String.format("Username length in name input is not less than %s chars.", count),
                    getUniqueUsernamePage().getNameInputTextLength() < count);
        } else {
            Assert.assertTrue(String.format("Username length in name input is not equal to %s chars.", count),
                    getUniqueUsernamePage().getNameInputTextLength() == count);
        }
    }

    /**
     * Verify that name input stays empty if user try input unacceptable symbols from the table
     *
     * @param table table of symbols
     * @step. ^I type unique usernames from the data table and verify they cannot be committed on Unique Username page$
     */
    @When("^I type unique usernames from the data table and verify they cannot be committed on Unique Username page$")
    public void IFillInInputDataAndVerify(DataTable table) throws Exception {
        final List<List<String>> data = table.raw();
        for (int i = 1; i < data.size(); i++) {
            String newName = data.get(i).get(1);
            getUniqueUsernamePage().inputStringInNameInput(newName);
            Assert.assertTrue(
                    String.format("Name input after enter of '%s' charset is not empty on Unique Username page", newName),
                    getUniqueUsernamePage().isNameInputEmpty());
        }
    }

    /**
     * Verify unique username input is prefilled with pointed name
     *
     * @param nameAlias user name/alias
     * @throws Exception
     * @step. ^I see unique username input is prefilled with (.*) on Unique Username page$
     */
    @Then("^I see unique username input is prefilled with (.*) on Unique Username page$")
    public void ISeeUniqueUsernameInputIsPrefilledWith(String nameAlias) throws Exception {
        final String name = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(nameAlias, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Unique username is not prefilled with %s", name),
                getUniqueUsernamePage().getNameInputValue().equals(name));
    }

    /**
     * Verify error label presence
     *
     * @param expectedLabel the expected error label
     * @step. ^I see "(.*)" error label on Unique Username page$
     */
    @Then("^I see \"(.*)\" error label on Unique Username page$")
    public void ISeeErrorLabel(String expectedLabel) throws Exception {
        Assert.assertTrue(String.format("Error label '%s' is not shown", expectedLabel),
                getUniqueUsernamePage().isErrorLabelVisible(expectedLabel));
    }
}
