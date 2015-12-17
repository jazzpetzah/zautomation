package com.wearezeta.auto.common.testrail;


import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Optional;


/**
 * http://docs.gurock.com/testrail-api2/start
 *
 */
class TestrailREST {
    private static final Logger log = ZetaLogger.getLog(TestrailREST.class
            .getSimpleName());

    private static final int MAX_REQUEST_RETRY_COUNT = 2;

    private static final CommonRESTHandlers restHandlers = new CommonRESTHandlers(
            TestrailREST::verifyRequestResult, MAX_REQUEST_RETRY_COUNT);

    static {
        log.setLevel(Level.DEBUG);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

    private static String getBaseURI() throws Exception {
        final String host = CommonUtils.getTestrailServerFromConfig(TestrailREST.class);
        return String.format("https://%s/index.php?/api/v2", host);
    }

    private static String getTestailUser() throws Exception {
        return CommonUtils.getTestrailUsernameFromConfig(TestrailREST.class);
    }

    private static String getTestrailToken() throws Exception {
        return CommonUtils.getTestrailTokenFromConfig(TestrailREST.class);
    }

    private static void verifyRequestResult(int currentResponseCode,
                                            int[] acceptableResponseCodes) throws BackendRequestException {
        if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
            throw new BackendRequestException(
                    String.format(
                            "Backend request failed. Request return code is: %d. Expected codes are: %s",
                            currentResponseCode,
                            Arrays.toString(acceptableResponseCodes)),
                    currentResponseCode);
        }
    }

    private static Invocation.Builder buildRequest(String restAction) throws Exception {
        final String dstUrl = String.format("%s/%s", getBaseURI(), restAction);
        log.debug(String.format("Making request to %s...", dstUrl));
        final Client client = ClientBuilder.newClient();
        return client
                .target(dstUrl)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,
                        String.format("%s:%s", getTestailUser(),
                                getTestrailToken()));
    }

    public static JSONArray getProjects() throws Exception {
        Invocation.Builder webResource = buildRequest("get_projects");
        final String output = restHandlers.httpGet(webResource, new int[] { HttpStatus.SC_OK });
        return new JSONArray(output);
    }

    public static JSONArray getTestPlans(long projectId) throws Exception {
        Invocation.Builder webResource = buildRequest(String.format("get_plans/%s", projectId));
        final String output = restHandlers.httpGet(webResource, new int[] { HttpStatus.SC_OK });
        return new JSONArray(output);
    }

    public static JSONObject getTestPlan(long testPlanId) throws Exception {
        Invocation.Builder webResource = buildRequest(String.format("get_plan/%s", testPlanId));
        final String output = restHandlers.httpGet(webResource, new int[] { HttpStatus.SC_OK });
        return new JSONObject(output);
    }

    public static JSONObject addTestCaseResult(long testRunId, long caseId,
                                               int statusId, Optional<String> comment) throws Exception {
        Invocation.Builder webResource = buildRequest(String.format("add_result_for_case/%s/%s",
                testRunId, caseId));
        final JSONObject requestBody = new JSONObject();
        requestBody.put("status_id", statusId);
        if (comment.isPresent()) {
            requestBody.put("comment", comment.get());
        }
        final String output = restHandlers.httpPost(webResource, requestBody.toString(),
                new int[] { HttpStatus.SC_OK });
        return new JSONObject(output);
    }

    public static JSONArray getTestCaseResults(long testRunId, long caseId) throws Exception {
        Invocation.Builder webResource = buildRequest(String.format("get_results_for_case/%s/%s",
                testRunId, caseId));
        final String output = restHandlers.httpGet(webResource, new int[] { HttpStatus.SC_OK });
        return new JSONArray(output);
    }
}
