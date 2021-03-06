package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.NewDeviceOverlay;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class NewDeviceOverlaySteps {
    private NewDeviceOverlay getNewDeviceOverlay() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(NewDeviceOverlay.class);
    }

    /**
     * Wait for a while and accept First Time Usage overlay if it is viisble
     *
     * @throws Exception
     * @param expectedLabel the expected label. The label may contain user name aliases
     * @step. ^I see the label "(.*)" on New Device overlay$
     */
    @Then("^I see the label \"(.*)\" on New Device overlay$")
    public void ISeeLabel(String expectedLabel) throws Exception {
        expectedLabel = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(expectedLabel, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("New Device overlay does not contain the label '%s'", expectedLabel),
                getNewDeviceOverlay().isContainingLabel(expectedLabel));
    }

    /**
     * Tap button on New Device overlay
     *
     * @param buttonName name of button
     * @throws Exception
     * @step. ^I tap (X|Show Device|Send Anyway) button on New Device overlay$
     */
    @When("^I tap (X|Show Device|Send Anyway) button on New Device overlay$")
    public void ITapButtonOnNewDeviceOverlay(String buttonName) throws Exception {
        getNewDeviceOverlay().tapButton(buttonName);
    }
}