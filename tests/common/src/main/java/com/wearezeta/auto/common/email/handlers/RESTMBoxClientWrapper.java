package com.wearezeta.auto.common.email.handlers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;


class RESTMBoxClientWrapper implements ISupportsMessagesPolling {
    private static final Logger log = ZetaLogger.getLog(RESTMBoxClientWrapper.class.getSimpleName());

    private String mboxUserName;

    public String getMboxUserName() {
        return mboxUserName;
    }

    public RESTMBoxClientWrapper(String mboxUserName) {
        this.mboxUserName = mboxUserName;
    }

    protected List<String> getRecentMessages(String email, int minCount, int maxCount, int timeoutMilliseconds)
            throws Exception {
        List<String> result = new ArrayList<>();
        final JSONArray recentEmails =
                RESTMBoxAPI.getRecentEmailsForUser(email, minCount, maxCount, timeoutMilliseconds);
        for (int i = 0; i < recentEmails.length(); i++) {
            final JSONObject recentEmailInfo = recentEmails.getJSONObject(i);
            result.add(recentEmailInfo.getString("raw_text"));
        }
        return result;
    }

    private final ExecutorService pool = Executors.newFixedThreadPool(CommonUtils.MAX_PARALLEL_USER_CREATION_TASKS);

    @Override
    public Future<String> getMessage(Map<String, String> expectedHeaders, int timeoutSeconds,
                                     long rejectMessagesBeforeTimestamp) throws Exception {
        RESTMBoxChangesListener listener = new RESTMBoxChangesListener(this, expectedHeaders, timeoutSeconds,
                rejectMessagesBeforeTimestamp);
        log.debug(String.format("Started email listener for message containing headers %s...",
                expectedHeaders.toString()));
        return pool.submit(listener);
    }

    @Override
    public void waitUntilMessagesCountReaches(String deliveredTo, int expectedMsgsCount, int timeoutSeconds)
            throws Exception {
        getRecentMessages(deliveredTo, expectedMsgsCount, expectedMsgsCount, timeoutSeconds * 1000);
    }

    @Override
    public boolean isAlive() {
        try {
            return CommonRESTHandlers.isAlive(
                    new URL(String.format("%s/recent_emails/%s/0/0", RESTMBoxAPI.getApiRoot(), this.mboxUserName))
            );
        } catch (MalformedURLException e) {
            Throwables.propagate(e);
        }
        return false;
    }
}
