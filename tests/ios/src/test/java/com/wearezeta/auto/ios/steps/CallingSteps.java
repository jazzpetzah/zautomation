package com.wearezeta.auto.ios.steps;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.calling2.v1.model.Flow;

import com.wearezeta.auto.common.ZetaFormatter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.wearezeta.auto.common.calling2.v1.model.Metrics;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.CallKitOverlayPage;
import com.wearezeta.auto.ios.pages.CallingOverlayPage;
import com.wearezeta.auto.ios.pages.ConversationViewPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallingSteps {
    /**
     * Make call to a specific user. You may instantiate more than one incoming
     * call from single caller by calling this step multiple times
     *
     * @param caller           caller name/alias
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. (\w+) calls (\w+)$
     */
    @When("(\\w+) calls (\\w+)$")
    public void UserXCallsToUserYUsingCallBackend(String caller, String conversationName) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCallingManager().callToConversation(
                IOSTestContextHolder.getInstance().getTestContext().getUsersManager().splitAliases(caller),
                conversationName);
    }

    /**
     * Stop outgoing or incoming call (audio and video) to the other side
     *
     * @param instanceUsers    comma separated list of user names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) stops? (incoming call from|outgoing call to) (.*)
     */
    @When("^(.*) stops? (incoming call from|outgoing call to) (.*)")
    public void UserXStopsIncomingOutgoingCallsToUserY(String instanceUsers, String typeOfCall, String conversationName)
            throws Exception {
        if (typeOfCall.equals("incoming call from")) {
            IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .stopIncomingCall(IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                            .splitAliases(instanceUsers));
        } else {
            IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .stopOutgoingCall(IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                            .splitAliases(instanceUsers), conversationName);
        }
    }


    /**
     * Verify whether call status is changed to one of the expected values after
     * N seconds timeout
     *
     * @param callers          caller names/aliases
     * @param conversationName destination conversation
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for
     *                         more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that call status to (.*) is changed to (.*) in
     * (\\d+) seconds?$
     */
    @Then("(.*) verif(?:ies|y) that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callers,
                                              String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                .verifyCallingStatus(IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                                .splitAliases(callers), conversationName, expectedStatuses, timeoutSeconds);
    }

    /**
     * Verify whether waiting instance status is changed to one of the expected
     * values after N seconds timeout
     *
     * @param callees          comma separated list of callee names/aliases
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for
     *                         more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that waiting instance status is changed to
     * (.*) in * (\\d+) seconds?$
     */
    @Then("(.*) verif(?:ies|y) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callees,
                                              String expectedStatuses, int timeoutSeconds) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                .verifyAcceptingCallStatus(IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                                .splitAliases(callees), expectedStatuses, timeoutSeconds);
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
        for (String callee : IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .splitAliases(callees)) {
            int actualFlowNumber = IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .getFlows(callee).size();
            Assert.assertTrue(String.format("Expected flows number is : %s but actual flows number was : %s",
                    numberOfFlows, actualFlowNumber), actualFlowNumber == numberOfFlows);
        }
    }

    /**
     * Verify that each flow of the instance had incoming and outgoing bytes
     * running over the line
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that all flows have greater than 0 bytes$
     */
    @Then("(.*) verif(?:ies|y) that all flows have greater than 0 bytes$")
    public void UserXVerifesHavingXFlows(String callees) throws Exception {
        for (String callee : IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .splitAliases(callees)) {
            for (Flow flow : IOSTestContextHolder.getInstance().getTestContext().getCallingManager().getFlows(callee)) {
                Assert.assertTrue("There is no incoming bytes",
                        flow.getTelemetry().getStats().getAudio().getBytesReceived() > 0L);
                Assert.assertTrue("There is no outgoing bytes",
                        flow.getTelemetry().getStats().getAudio().getBytesSent() > 0L);
            }
        }
    }

    /**
     * Execute instance as 'userAsNameAlias' user on calling server
     * using 'callingServiceBackend' tool
     *
     * @param callees               comma separated callee name/alias
     * @param callingServiceBackend available values: 'blender', 'chrome', * 'firefox'
     * @throws Exception
     * @step. (.*) starts? instance using (.*)$
     */
    @When("(.*) starts? instance using (.*)$")
    public void UserXStartsInstance(String callees, String callingServiceBackend) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                .startInstances(IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                                .splitAliases(callees),
                callingServiceBackend, "iOS", ZetaFormatter.getScenario());
    }

    /**
     * Automatically accept the next incoming call for the particular user as
     * soon as it appears in UI. Waiting instance should be already created for
     * this particular user
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) accepts? next incoming call automatically$
     */
    @When("(.*) accepts? next incoming call automatically$")
    public void UserXAcceptsNextIncomingCallAutomatically(String callees) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                .acceptNextCall(IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                        .splitAliases(callees));
    }

    /**
     * Automatically accept the next incoming video call for the particular user
     * as soon as it appears in UI. Waiting instance should be already created
     * for this particular user
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) accepts? next incoming video call automatically$
     */
    @When("(.*) accepts? next incoming video call automatically$")
    public void UserXAcceptsNextIncomingVideoCallAutomatically(String callees) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                .acceptNextVideoCall(IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                        .splitAliases(callees));
    }

    /**
     * Make a video call to a specific user.
     *
     * @param callers          caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. (.*) starts? a video call to (.*)$
     */
    @When("(.*) starts? a video call to (.*)$")
    public void UserXStartVideoCallsToUserYUsingCallBackend(String callers, String conversationName) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                .startVideoCallToConversation(IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                                .splitAliases(callers), conversationName);
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
        for (Call call : IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                .getOutgoingCall(IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
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
     * @param callDurationMinutes
     * @param times               number of consecutive calls
     * @param callees             participants which will wait for a call
     * @throws java.lang.Throwable
     * @step. ^I call (\\d+) times for (\\d+) minutes with (.*)$
     */
    @Then("^I call (\\d+) times for (\\d+) minutes with (.*)$")
    public void ICallXTimes(int times, int callDurationMinutes, String callees)
            throws Exception {
        final Map<Integer, Exception> failures = new HashMap<>();
        final List<Integer> callsWithoutMetricsData = new ArrayList<>();
        final List<Integer> callsWithoutByteFlowData = new ArrayList<>();
        arrayCallSetupTime.clear();
        arrayCallEstabTime.clear();
        for (int i = 0; i < times; i++) {
            makeSingleCall(callees, i, callDurationMinutes, failures, callsWithoutMetricsData, callsWithoutByteFlowData);
        }

        int avgCallSetupTime = calculateCallStatistics(arrayCallSetupTime, times, failures, callsWithoutMetricsData);
        int avgCallEstabTime = calculateCallStatistics(arrayCallEstabTime, times, failures, callsWithoutMetricsData);
        createCallingReport(times, failures, callsWithoutMetricsData, callsWithoutByteFlowData, avgCallSetupTime, avgCallEstabTime, CallLoopType.Make);

        failures.forEach((Integer i, Throwable t) -> {
            LOG.error(String.format("Failure %s: %s", i, t.getMessage()));
        });

        if (!failures.isEmpty()) {
            Assert.fail(formatFailuresList(failures));
        }
    }

    private static final String CALL_STATUS_DESTROYED = "destroyed";
    private static final String CALL_STATUS_ACTIVE = "active";

    private void makeSingleCall(String callees, int callIndex, int callDurationMinutes,
                                final Map<Integer, Exception> failures, List<Integer> callsWithoutMetricsData,
                                List<Integer> callsWithoutByteFlowData) throws Exception {
        final int timeBetweenCall = 10;
        final List<String> calleeList =
                IOSTestContextHolder.getInstance().getTestContext().getUsersManager().splitAliases(callees);

        LOG.info("\n\nSTARTING CALL " + callIndex);
        try {
            IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getPage(ConversationViewPage.class).tapButton("Audio Call");
            LOG.info("Pressing Audio button to start call");

            if (callIndex == 0) {
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getCommonPage().acceptAlert();
            }

            IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .acceptNextCall(calleeList);
            IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .verifyAcceptingCallStatus(calleeList, CALL_STATUS_ACTIVE, 20);
            LOG.info("All instances are active");

            boolean isVisible = IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getPage(CallingOverlayPage.class).isCallStatusLabelVisible();
            if (!isVisible) {
                throw new CallIterationError(callIndex, "Calling overlay should be visible");
            }
            LOG.info("Calling overlay is visible");

            IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                    .WaitForTime(callDurationMinutes * 60);
            IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getPage(CallingOverlayPage.class).tapButtonByName(CALLINGOVERLAY_LEAVE_BUTTON);
            boolean isInvisible = IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getPage(CallingOverlayPage.class).isCallStatusLabelInvisible();
            if (!isInvisible) {
                throw new CallIterationError(callIndex, "Calling overlay should be invisible");
            }
            LOG.info("Calling overlay is NOT visible");
            IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .verifyAcceptingCallStatus(calleeList, CALL_STATUS_DESTROYED, 20);
            LOG.info("All instances are destroyed");

            for (Call call : IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .getIncomingCall(calleeList)) {
                final Metrics metrics = call.getMetrics();
                if (metrics == null) {
                    LOG.info("Could not get metrics for this call.");
                    callsWithoutMetricsData.add(callIndex);
                } else {
                    final long avgRateU = metrics.getAvgRateU();
                    final long avgRateD = metrics.getAvgRateD();
                    if (avgRateU < 0 || avgRateD < 0) {
                        callsWithoutByteFlowData.add(callIndex);
                    }
                    arrayCallSetupTime.add(((int) metrics.getSetupTime()));
                    arrayCallEstabTime.add(((int) metrics.getEstabTime()));
                }
            }
            LOG.info(String.format("CALL %s SUCCESSFUL", callIndex));
            IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                    .WaitForTime(timeBetweenCall);

        } catch (InterruptedException ie) {
            Throwables.propagate(ie);
        } catch (Exception t) {
            LOG.info(String.format("CALL %s FAILED", callIndex));
            LOG.error(String.format("Can not stop waiting call %s because %s", callIndex, t.getMessage()));
            try {
                if (IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getPage(CallingOverlayPage.class).isButtonVisible(CALLINGOVERLAY_LEAVE_BUTTON)) {
                    IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                            .getPage(CallingOverlayPage.class).tapButtonByName(CALLINGOVERLAY_LEAVE_BUTTON);
                }
            } catch (Exception ex) {
                LOG.error(String.format("Can not stop call kit %s because %s", callIndex, ex));
                try {
                    boolean callButtonVisible = IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                            .getPage(ConversationViewPage.class).isButtonVisible("Audio Call");
                    if (!callButtonVisible) {
                        throw new CallIterationError(callIndex, "Calling button is not visible");
                    }
                } catch (Exception exe) {
                    LOG.error(String.format("Can not start a new call because %s", ex));
                }
            }
            failures.put(callIndex, t);
        }
    }

    //save the setup time and estab time for every call to calculate the average time
    private List<Integer> arrayCallSetupTime = new ArrayList<>();
    private List<Integer> arrayCallEstabTime = new ArrayList<>();

    /**
     * Receive consecutive calls without logging out etc.
     *
     * @param callDurationMinutes time of the call
     * @param times               number of consecutive calls
     * @param callees             participants which will wait for a call
     * @param conversationName    user to be called
     * @param appState            if App is in BG or FG
     * @throws Exception
     * @step. ^(\w+) calls to (\w+) in (Background|Foreground) (\d+) times? for (\d+) minutes?$
     */
    @Then("^(\\w+) calls to (\\w+) in (Background|Foreground) (\\d+) times? for (\\d+) minutes?$")
    public void IReceiveCallsXTimes(String callees, String conversationName, String appState, int times, int callDurationMinutes)
            throws Exception {
        LOG.info("Call setup_time and estab_time array needs to be emptied");
        arrayCallSetupTime.clear();
        arrayCallEstabTime.clear();

        final Map<Integer, Exception> failures = new HashMap<>();
        final List<Integer> callsWithoutMetricsData = new ArrayList<>();
        final List<Integer> callsWithoutByteFlowData = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            receiveSingleCall(callees, i, appState, conversationName, callDurationMinutes, failures, callsWithoutMetricsData, callsWithoutByteFlowData);
        }

        int avgCallSetupTime = calculateCallStatistics(arrayCallSetupTime, times, failures, callsWithoutMetricsData);
        int avgCallEstabTime = calculateCallStatistics(arrayCallEstabTime, times, failures, callsWithoutMetricsData);
        createCallingReport(times, failures, callsWithoutMetricsData, callsWithoutByteFlowData, avgCallSetupTime, avgCallEstabTime, CallLoopType.Receive);

        failures.forEach((Integer i, Throwable t) -> {
            LOG.error(String.format("Failure %s: %s", i, t.getMessage()));
        });
        if (!failures.isEmpty()) {
            Assert.fail(formatFailuresList(failures));
        }
    }

    private int calculateCallStatistics(List<Integer> callStatisticTime, int numberOfCalls,
                                        final Map<Integer, Exception> failures,
                                        List<Integer> callsWithoutMetricsData) {
        int sumCallStatisticTime = callStatisticTime.stream().reduce(0, Integer::sum);
        int avgCallStatistic = 0;
        int successfulCallsCount = numberOfCalls - failures.size() - callsWithoutMetricsData.size();
        if (successfulCallsCount > 0) {
            avgCallStatistic = sumCallStatisticTime / successfulCallsCount;
        }
        return avgCallStatistic;
    }

    public enum CallLoopType {
        Receive("RECEIVE"), Make("MAKE");

        private final String name;

        CallLoopType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // this file is needed to report the call stats via jenkins into a read only chat
    private static final String CALL_STATS_FILENAME = "multi_call_result.txt";
    private static final String CALL_STATS_JENKINS_ENV_VARIABLE = "MULTI_CALL_RESULT";

    private void createCallingReport(int timesOfCalls, final Map<Integer, Exception> failures,
                                     final List<Integer> callsWithoutMetricsData, final List<Integer> callsWithoutByteFlowData,
                                     int avgCallSetupTime, int avgCallEstabTime, CallLoopType typeOfCall)
            throws Exception {
        String message = String.format("%s CALL LOOP! \\\n%s/%s calls succeeded. %s call(s) no byte flow, %s call(s) failed.\\\n" +
                        "Average calculated from %s call(s): Average Call setup_time: %s ms , average Call estab_time: %s ms.",
                typeOfCall, timesOfCalls - failures.size(), timesOfCalls, callsWithoutByteFlowData.size(), failures.size(),
                timesOfCalls - failures.size() - callsWithoutMetricsData.size(), avgCallSetupTime, avgCallEstabTime);
        LOG.info(message);

        final String reportContent = String.format("%s=%s", CALL_STATS_JENKINS_ENV_VARIABLE, message);
        Files.write(Paths.get(CommonUtils.getBuildPathFromConfig(CallingSteps.class), CALL_STATS_FILENAME),
                reportContent.getBytes());
    }

    private final class CallIterationError extends Exception {
        public CallIterationError(int numberOfCall, String message) {
            super(String.format("Call %s failed with %s", numberOfCall, message));
        }
    }

    private static final String SEPARATOR = "\n--------------------------------------\n";

    private String formatFailuresList(Map<Integer, Exception> failures) {
        final StringBuilder builder = new StringBuilder();
        failures.forEach((callIndex, failureException) -> {
            String header = String.format("Failure summary for call # %s\n", callIndex);
            builder.append(header);
            builder.append(failureException.getMessage()).append("\n");
            if (!(failureException instanceof CallIterationError)) {
                builder.append(ExceptionUtils.getStackTrace(failureException));
            }
            builder.append(SEPARATOR);
        });
        return builder.toString();
    }

    private static final String CALLINGOVERLAY_LEAVE_BUTTON = "Leave";
    private static final String CALLKIT_DECLINE_BUTTON = "Decline";
    private static final String CALLKIT_ACCEPT_BUTTON = "Accept";

    private void receiveSingleCall(String callees, int callIndex, String appState, String conversationName,
                                   int callDurationMinutes, final Map<Integer, Exception> failures,
                                   List<Integer> callsWithoutMetricsData,
                                   List<Integer> callsWithoutByteFlowData) throws Exception {
        final int timeBetweenCall = 10;
        final List<String> calleeList =
                IOSTestContextHolder.getInstance().getTestContext().getUsersManager().splitAliases(callees);

        LOG.info("\n\nSTARTING CALL " + callIndex);
        try {
            if (appState.equals("Background")) {
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getCommonPage().pressHomeButton();
                LOG.info("Put app into background");
            }
            IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .callToConversation(calleeList, conversationName);

            if (callIndex == 0) {
                IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                        .WaitForTime(5);
            }
            boolean isVisible = IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getPage(CallKitOverlayPage.class).isVisible("Audio");
            if (!isVisible) {
                throw new CallIterationError(callIndex, "Audio Call Kit overlay should be visible");
            }

            LOG.info("Audio Call Kit overlay is visible");
            IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getPage(CallKitOverlayPage.class).tapButton(CALLKIT_ACCEPT_BUTTON);
            if (callIndex == 0) {
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getCommonPage().acceptAlert();
            }

            IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .verifyCallingStatus(calleeList, conversationName, CALL_STATUS_ACTIVE, 20);
            LOG.info("All instances are active");

            LOG.info("Calling for 1 minute");
            IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                    .WaitForTime(callDurationMinutes * 60);

            IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getPage(CallingOverlayPage.class).tapButtonByName(CALLINGOVERLAY_LEAVE_BUTTON);
            boolean isNotVisible = IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getPage(CallingOverlayPage.class).isCallStatusLabelInvisible();
            if (!isNotVisible) {
                throw new CallIterationError(callIndex, "Audio Call overlay should be invisible");
            }
            LOG.info("Calling overlay is NOT visible");

            for (Call call : IOSTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .getOutgoingCall(calleeList, conversationName)) {
                final Metrics metrics = call.getMetrics();
                if (metrics == null) {
                    LOG.info("Could not get metrics for this call.");
                    callsWithoutMetricsData.add(callIndex);
                    continue;
                }
                final long avgRateU = metrics.getAvgRateU();
                final long avgRateD = metrics.getAvgRateD();
                if (avgRateU < 0 || avgRateD < 0) {
                    callsWithoutByteFlowData.add(callIndex);
                }
                arrayCallSetupTime.add(((int) metrics.getSetupTime()));
                arrayCallEstabTime.add(((int) metrics.getEstabTime()));
            }

            LOG.info(String.format("CALL %s SUCCESSFUL", callIndex));
            IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                    .WaitForTime(timeBetweenCall);
        } catch (InterruptedException ie) {
            Throwables.propagate(ie);
        } catch (Exception t) {
            LOG.info(String.format("CALL %s FAILED", callIndex));
            try {
                if (IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getPage(CallingOverlayPage.class).isButtonVisible(CALLINGOVERLAY_LEAVE_BUTTON)) {
                    IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                            .getPage(CallingOverlayPage.class).tapButtonByName(CALLINGOVERLAY_LEAVE_BUTTON);
                }
            } catch (Exception ex) {
                LOG.error(String.format("Can not stop call %s because %s", callIndex, ex));
                try {
                    if (IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                            .getPage(CallKitOverlayPage.class).isButtonVisible(CALLKIT_DECLINE_BUTTON)) {
                        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                                .getPage(CallKitOverlayPage.class).tapButton(CALLKIT_DECLINE_BUTTON);
                    }
                } catch (Exception exe) {
                    LOG.error(String.format("Can not stop call kit %s because %s", callIndex, exe));
                    try {
                        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                                .getCommonPage().pressHomeButton();
                        LOG.info("Put app into background");
                    } catch (Exception morexe) {
                        LOG.error(String.format("Call %s failed because %s", callIndex, morexe));
                    }
                }
            }
            failures.put(callIndex, t);
        }
    }
}