package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.CallIncomingPage;

import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class CallIncomingPageSteps {
    private CallIncomingPage getPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext()
                .getPagesCollection().getPage(CallIncomingPage.class);
    }

    /**
     * Verifies presence of incoming call
     *
     * @param not         equals to null means should see incoming call
     * @param isVideoCall equals to null means it is the video incoming call view
     * @throws Exception
     * @step. ^I (do not )?see incoming (video )?call$
     */
    @Then("^I (do not )?see incoming (video )?call$")
    public void ISeeIncomingCall(String not, String isVideoCall) throws Exception {
        String subtitle = isVideoCall == null ? "calling" : "video calling";
        if (not == null) {
            assertTrue("Incoming call not visible", getPage().waitUntilVisible(subtitle));
        } else {
            assertTrue("Incoming call should not be visible", getPage().waitUntilNotVisible(subtitle));
        }
    }

    /**
     * Wait for incoming call up to Timeout seconds
     *
     * @param timeoutSeconds Timeout in seconds
     * @param not            equals to null means should see incoming call
     * @param isVideoCall    equals to null means it is the video incoming call view
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds? and (do not )?see incoming (video )?call$
     */
    @Then("^I wait up to (\\d+) seconds? and (do not )?see incoming (video )?call$")
    public void ISeeIncomingCallBeforeTimeout(int timeoutSeconds, String not, String isVideoCall) throws Exception {
        String subtitle = isVideoCall == null ? "calling" : "video calling";
        if (not == null) {
            assertTrue("Incoming call not visible", getPage().waitUntilVisible(subtitle, timeoutSeconds));
        } else {
            assertTrue("Incoming call should not be visible", getPage().waitUntilNotVisible(subtitle, timeoutSeconds));
        }
    }

    /**
     * Ignores an incoming call
     *
     * @param action either 'accept' or 'ignore'
     * @throws Exception
     * @step. ^I swipe to (ignore|accept) the call$
     */
    @When("^I swipe to (ignore|accept) the call$")
    public void ISwipeTo(String action) throws Exception {
        switch (action) {
            case "accept":
                getPage().acceptCall();
                break;
            case "ignore":
                getPage().ignoreCall();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown action name '%s'", action));
        }
    }

    /**
     * Verify that incoming calling UI is visible and that the correct caller name is shown
     *
     * @param expectedCallerName User name who calls
     * @throws Exception
     * @step. ^I see incoming call from (.*)$
     */
    @When("^I see incoming call from (.*)$")
    public void ISeeIncomingCallingMessage(String expectedCallerName)
            throws Exception {
        expectedCallerName = AndroidTestContextHolder.getInstance().getTestContext()
                .getUsersManager().findUserByNameOrNameAlias(expectedCallerName).getName();
        Assert.assertTrue(String.format(
                "The current caller name differs from the expected value '%s'", expectedCallerName),
                getPage().waitUntilNameAppearsOnCallingBarCaption(expectedCallerName));
    }

    /**
     * Wait for incoming call from user up to TimeOut seconds
     *
     * @param timeoutSeconds     Timeout in seconds
     * @param expectedCallerName User name who calls
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds? for incoming call from (.*)$
     */
    @When("^I wait up to (\\d+) seconds? for incoming call from (.*)$")
    public void ISeeIncomingCallingMessageBeforeTimeout(int timeoutSeconds, String expectedCallerName)
            throws Exception {
        expectedCallerName = AndroidTestContextHolder.getInstance().getTestContext()
                .getUsersManager().findUserByNameOrNameAlias(expectedCallerName).getName();
        Assert.assertTrue(String.format(
                "The current caller name differs from the expected value '%s'", expectedCallerName),
                getPage().waitUntilNameAppearsOnCallingBarCaption(expectedCallerName, timeoutSeconds));
    }
}
