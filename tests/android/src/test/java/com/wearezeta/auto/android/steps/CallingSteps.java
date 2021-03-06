package com.wearezeta.auto.android.steps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.Timedelta;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CallingSteps {
    private final AndroidCommonCallingSteps androidCallingSteps = new AndroidCommonCallingSteps();

    /**
     * Make audio or video call(s) to one specific conversation.
     *
     * @param callerNames      caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) start(?:s|ing) a video call to (.*)$
     */
    @When("^(.*) start(?:s|ing) a video call to (.*)$")
    public void UserXCallsWithVideoToConversationY(String callerNames, String conversationName) throws Exception {
        AndroidTestContextHolder.getInstance().getTestContext().getCallingManager()
                .startVideoCallToConversation(AndroidTestContextHolder.getInstance().getTestContext()
                        .getUsersManager().splitAliases(callerNames), conversationName);
    }

    /**
     * Make voice calls to one specific conversation.
     *
     * @param callerNames      caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) calls (.*)$
     */
    @When("^(.*) calls (.*)$")
    public void UserXCallsToConversationY(String callerNames, String conversationName) throws Exception {
        androidCallingSteps.callToConversation(AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .splitAliases(callerNames), conversationName);
    }

    /**
     * Stop call on the other side
     *
     * @param instanceUsers    comma separated list of user names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) stops? calls( to (.*))$
     */
    @When("^(.*) stops? calling( (.*))?$")
    public void UserXStopsCallsToUserY(String instanceUsers, String outgoingCall, String conversationName)
            throws Exception {
        if (outgoingCall == null) {
            AndroidTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .stopIncomingCall(AndroidTestContextHolder.getInstance().getTestContext()
                            .getUsersManager().splitAliases(instanceUsers));
        } else {
            AndroidTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .stopOutgoingCall(AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                            .splitAliases(instanceUsers), conversationName);
        }
    }

    /**
     * Verify whether call status is changed to one of the expected values after N seconds timeout
     *
     * @param conversationName destination conversation
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$
     */
    @Then("(.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callers,
                                              String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        AndroidTestContextHolder.getInstance().getTestContext().getCallingManager().verifyCallingStatus(
                AndroidTestContextHolder.getInstance().getTestContext().getUsersManager().splitAliases(callers),
                conversationName, expectedStatuses, timeoutSeconds);
    }

    /**
     * Verify whether waiting instance status is changed to one of the expected values after N seconds timeout
     *
     * @param callees          comma separated callee names/aliases
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$
     */
    @Then("(.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callees,
                                              String expectedStatuses, int timeoutSeconds) throws Exception {
        AndroidTestContextHolder.getInstance().getTestContext().getCallingManager()
                .verifyAcceptingCallStatus(
                        AndroidTestContextHolder.getInstance().getTestContext().getUsersManager().splitAliases(callees),
                        expectedStatuses, timeoutSeconds);
    }

    /**
     * Execute instance as 'userAsNameAlias' user on calling server using 'callingServiceBackend' tool
     *
     * @param callees               callee name/alias
     * @param callingServiceBackend available values: 'autocall', 'chrome', 'firefox', 'zcall'
     * @throws Exception
     * @step. (.*) starts? instances? using (.*)$
     */
    @When("(.*) starts? instances? using (.*)$")
    public void UserXStartsInstance(String callees,
                                    String callingServiceBackend) throws Exception {
        AndroidTestContextHolder.getInstance().getTestContext().getCallingManager().
                startInstances(AndroidTestContextHolder.getInstance().getTestContext()
                                .getUsersManager().splitAliases(callees),
                        callingServiceBackend, "Android", ZetaFormatter.getScenario());
    }

    /**
     * Automatically accept the next incoming audio call or for the particular user as soon as it appears in UI. Waiting
     * instance should be already created for this particular user
     *
     * @param callees callee names/aliases
     * @throws Exception
     * @step. (.*) accepts? next incoming call automatically$
     */
    @When("(.*) accepts? next incoming( video)? call automatically$")
    public void UserXAcceptsNextIncomingCallAutomatically(String callees, String video)
            throws Exception {
        if (video == null) {
            AndroidTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .acceptNextCall(AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                            .splitAliases(callees));
        } else {
            AndroidTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .acceptNextVideoCall(AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                            .splitAliases(callees));
        }

    }

    /**
     * Verify that the instance has X active flows
     *
     * @param callees       comma separated list of callee names/aliases
     * @param numberOfFlows expected number of flows
     * @throws Exception
     * @step. (.*) verif(?:ies|y) to have (\\d+) flows?$
     */
    @Then("(.*) verif(?:ies|y) to have (\\d+) flows?$")
    public void UserXVerifesHavingXFlows(String callees, int numberOfFlows)
            throws Exception {
        for (String callee : AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .splitAliases(callees)) {
            assertThat(AndroidTestContextHolder.getInstance().getTestContext().getCallingManager().getFlows(callee),
                    hasSize(numberOfFlows));
        }
    }

    /**
     * Verify that each flow of the instance had incoming and outgoing bytes running over the line
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that all flows have greater than 0 bytes$
     */
    @Then("(.*) verif(?:ies|y) that all flows have greater than 0 bytes$")
    public void UserXVerifesHavingXFlows(String callees) throws Exception {
        for (String callee : AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .splitAliases(callees)) {
            for (Flow flow : AndroidTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .getFlows(callee)) {
                assertThat("incoming bytes", flow.getTelemetry().getStats().getAudio().getBytesReceived(),
                        greaterThan(0L));
                assertThat("outgoing bytes", flow.getTelemetry().getStats().getAudio().getBytesSent(),
                        greaterThan(0L));
            }
        }
    }

    /**
     * Verify that each call of the instances was successful
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that call to conversation (.*) was successful$
     */
    @Then("(.*) verif(?:ies|y) that call to conversation (.*) was successful$")
    public void UserXVerifesCallWasSuccessful(String callees, String conversation) throws Exception {
        for (Call call : AndroidTestContextHolder.getInstance().getTestContext().getCallingManager()
                .getOutgoingCall(AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                        .splitAliases(callees), conversation)) {
            assertNotNull("There are no metrics available for this call \n" + call, call.getMetrics());
            assertTrue("Call failed: \n" + call + "\n" + call.getMetrics(), call.getMetrics().isSuccess());
        }
    }

    private static final Logger LOG = ZetaLogger.getLog(CallingSteps.class
            .getName());

    /**
     * Executes consecutive calls without logging out etc.
     *
     * @param callDuration
     * @param times               number of consecutive calls
     * @param callees             participants which will wait for a call
     * @throws java.lang.Throwable
     * @step. ^I call (\\d+) times for (\\d+) minutes with (.*)$
     */
    @Then("^I call (\\d+) times for (\\d+) (minutes?|seconds?) with (.*)$")
    public void ICallXTimes(int times, int callDuration, String time, String callees)
            throws Throwable {
        final int timeBetweenCall = 10;
        if (time.toLowerCase().startsWith("min")) callDuration = Timedelta.fromMinutes(callDuration).asSeconds();
        final List<String> calleeList = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .splitAliases(callees);
        final ConversationViewPageSteps convSteps = new ConversationViewPageSteps();
        final CallOngoingAudioPageSteps callOngoingPageSteps = new CallOngoingAudioPageSteps();
        final CallOutgoingPageSteps callOutgoingPageSteps = new CallOutgoingPageSteps();
        final CommonAndroidSteps commonAndroidSteps = new CommonAndroidSteps();
        final Map<Integer, Throwable> failures = new HashMap<>();
        for (int i = 0; i < times; i++) {
            LOG.info(String.format("\n\nSTARTING CALL %s", i));
            try {
                convSteps.ITapTopToolbarButton("Audio Call");
                LOG.info("Checking outgoing call page");
                callOutgoingPageSteps.ISeeOutgoingCall(null, null);

                for (String callee : calleeList) {
                    LOG.info(String.format("%s accepts incoming call", callee));
                    UserXAcceptsNextIncomingCallAutomatically(callee, null);
                    UserXVerifesCallStatusToUserY(callee, "active", 20);
                }
                LOG.info("All instances are active");

                callOngoingPageSteps.ISeeOngoingCall(null);
                LOG.info("Calling overlay is visible");

                commonAndroidSteps.WaitForTime(callDuration);

                callOngoingPageSteps.IHangUp();

                for (String callee : calleeList) {
                    UserXVerifesCallStatusToUserY(callee, "destroyed", 20);
                }
                LOG.info("All instances are destroyed");

                callOngoingPageSteps.ISeeOngoingCall("do not");
                LOG.info("Calling overlay is NOT visible");
                LOG.info(String.format("CALL %s SUCCESSFUL", i));
                commonAndroidSteps.WaitForTime(timeBetweenCall);

            } catch (Throwable t) {
                LOG.info(String.format("CALL %s FAILED", i));
                LOG.error(String.format("Can not stop waiting call %s %s", i, t));
                try {
                    callOngoingPageSteps.IHangUp();
                    convSteps.ISeeVideoCallButtonInUpperToolbar(null, "audio");
                } catch (Throwable throwable) {
                    LOG.error(String.format("Can not stop call %s %s", i, throwable));
                }
                failures.put(i, t);
            }

        }

        String resultMessage = String.format("%s failures happened during %s calls", failures.size(), times);
        LOG.info(resultMessage);
        failures.forEach((Integer i, Throwable t) -> {
            LOG.error(i + ": " + t.getMessage());
        });

        for (Map.Entry<Integer, Throwable> entrySet : failures.entrySet()) {
            // will just throw the first exception to indicate failed calls in
            // test results
            throw new AssertionError(
                    String.format("\n---------------------\n\n%s\n\n---------------------", resultMessage),
                    entrySet.getValue());
        }
    }
}
