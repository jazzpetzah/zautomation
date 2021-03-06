package com.wearezeta.auto.android.steps;

import java.util.List;
import java.util.Random;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.CallIncomingPage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.calling2.v1.exception.CallingServiceCallException;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;

public class AndroidCommonCallingSteps {
    private static final Logger log = ZetaLogger.getLog(CommonUtils.class.getSimpleName());

    private CallIncomingPage getIncomingCallPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext()
                .getPagesCollection().getPage(CallIncomingPage.class);
    }

    private static final int DEFAULT_RETRIES = 3;
    private static final int DEFAULT_RETRY_DELAY = 30;

    private final Random random = new Random();

    public void callToConversation(List<String> callerNames, String conversationName) throws Exception {
        int retryNumber = 1;
        int intervalSeconds = 1;
        do {
            long sleepInterval = 1000;
            try {
                AndroidTestContextHolder.getInstance().getTestContext().getCallingManager()
                        .callToConversation(callerNames, conversationName);
                return;
            } catch (CallingServiceCallException e) {
                e.printStackTrace();
                sleepInterval = (intervalSeconds +
                        random.nextInt(DEFAULT_RETRY_DELAY)) * 2000;
                intervalSeconds *= 2;
                if (getIncomingCallPage().waitUntilIncomingCallOverlayAppears()) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.debug(String.format("Failed to call to conversation. Retrying (%d of %d)...", retryNumber, DEFAULT_RETRIES));
            try {
                Thread.sleep(sleepInterval);
            } catch (InterruptedException ex) {
                return;
            }
            retryNumber++;
        } while (retryNumber <= DEFAULT_RETRIES);
        throw new
                RuntimeException(
                String.format("Failed to call to conversation after '%d' retries", DEFAULT_RETRIES));
    }
}
