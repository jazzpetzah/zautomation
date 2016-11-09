package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import com.wearezeta.auto.ios.pages.details_overlay.single.SinglePendingUserOutgoingConnectionPage;

import cucumber.api.java.en.When;

public class SingleUserOutgoingPendingConnectionPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private SinglePendingUserOutgoingConnectionPage getPage() throws Exception {
        return pagesCollection.getPage(SinglePendingUserOutgoingConnectionPage.class);
    }

    /**
     * Verify user name/email presence on Single user Pending outgoing connection page
     *
     * @param shouldNotSee equals to null if the label should be visible
     * @param value        the actual value or alias
     * @param fieldType    either 'email' or 'name'
     * @throws Exception
     * @step. ^I (do not )?see (.*) (email|name) on Single user Pending outgoing connection page$"
     */
    @Then("^I (do not )?see (.*) (email|name) on Single user Pending outgoing connection page$")
    public void ISeeLabel(String shouldNotSee, String value, String fieldType) throws Exception {
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

    /**
     * Verify button visibility on Single user Pending outgoing connection page
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param btnName      button name
     * @throws Exception
     * @step. ^I (do not )?see (Cancel Request|Connect) button on Single user Pending outgoing connection page$
     */
    @Then("^I (do not )?see (Cancel Request|Connect) button on Single user Pending outgoing connection page$")
    public void ISeeButton(String shouldNotSee, String btnName) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("'%s' button is expected to be visible", btnName),
                    getPage().isButtonVisible(btnName));
        } else {
            Assert.assertTrue(String.format("'%s' button is expected to be invisible", btnName),
                    getPage().isButtonInvisible(btnName));
        }
    }

    /**
     * Tap the corresponding button on Single user Pending incoming connection page
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Cancel Request|Connect) button on Single user Pending incoming connection page$
     */
    @When("^I tap (Cancel Request|Connect) button on Single user Pending outgoing connection page$")
    public void ITapButton(String btnName) throws Exception {
        getPage().tapButton(btnName);
    }
}
