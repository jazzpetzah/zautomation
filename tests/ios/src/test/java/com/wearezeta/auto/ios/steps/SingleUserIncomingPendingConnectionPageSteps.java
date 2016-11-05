package com.wearezeta.auto.ios.steps;

import cucumber.api.java.en.Then;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.details_overlay.single.SinglePendingUserIncomingConnectionPage;

import cucumber.api.java.en.When;

public class SingleUserIncomingPendingConnectionPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private SinglePendingUserIncomingConnectionPage getPage() throws Exception {
        return pagesCollection.getPage(SinglePendingUserIncomingConnectionPage.class);
    }

    @Then("^I (do not )?see (.*) (email|name) on Single user Pending incoming connection page$")
    public void ISeeLabel(String shouldNotSee, String value, String fieldType) throws Exception {
        boolean result;
        switch (fieldType) {
            case "email":
                value = usrMgr.replaceAliasesOccurences(value, ClientUsersManager.FindBy.EMAIL_ALIAS);
                if (shouldNotSee == null) {
                    result = getPage().isNameVisible(value);
                } else {
                    result = getPage().isNameInvisible(value);
                }
                break;
            case "name":
                value = usrMgr.replaceAliasesOccurences(value, ClientUsersManager.FindBy.NAME_ALIAS);
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

    @Then("^I (do not )?see (Cancel Request|Connect) button on Single user Pending incoming connection page$")
    public void ISeeButton(String shouldNotSee, String btnName) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("'%s' button is expected to be visible", btnName),
                    getPage().isButtonVisible(btnName));
        } else {
            Assert.assertTrue(String.format("'%s' button is expected to be invisible", btnName),
                    getPage().isButtonInvisible(btnName));
        }
    }

    @When("^I tap (Ignore|Connect) button on Single user Pending incoming connection page$")
    public void ITapButton(String btnName) throws Exception {
        getPage().tapButton(btnName);
    }
}
