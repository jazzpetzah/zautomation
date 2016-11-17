package com.wearezeta.auto.win.common;

import java.util.List;
import java.util.Map;
import org.junit.runner.RunWith;
import com.wire.picklejar.PickleJar;
import com.wire.picklejar.execution.PickleExecutor;
import com.wire.picklejar.execution.PickleJarTest;
import com.wire.picklejar.execution.exception.StepNotExecutableException;
import com.wire.picklejar.gherkin.model.Result;
import static com.wire.picklejar.gherkin.model.Result.FAILED;
import static com.wire.picklejar.gherkin.model.Result.PASSED;
import static com.wire.picklejar.gherkin.model.Result.SKIPPED;
import com.wire.picklejar.gherkin.model.Step;
import cucumber.api.PendingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogEntry;
import com.wearezeta.auto.web.common.WebAppExecutionContext;

@RunWith(Parameterized.class)
public class PickleJarInheritedTest extends PickleJarTest {

    private final int MAX_LOG_TAIL_SIZE = 20;
    private Lifecycle lifecycle;

    @Parameters(name = "{0}: {1} {2}")
    public static Collection<Object[]> getTestcases() throws IOException {
        return PickleJar.getTestcases();
    }

    public PickleJarInheritedTest(String feature, String testcase, Integer exampleNum, List<String> steps,
            Map<String, String> examples, List<String> tags) throws Exception {
        super(feature, testcase, exampleNum, steps, examples, tags);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        PickleJarTest.setUpClass();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        try {
            lifecycle = new Lifecycle();
            lifecycle.setUp(getTestcase());
        } catch (Exception e) {
            getReportScenario().getSteps().stream().findFirst().ifPresent((s) -> setResult(s, new Result(1L, FAILED,
                    PickleExecutor.getThrowableStacktraceString(e))));
            throw e;
        }
    }

    @Test
    @Override
    public void test() throws Throwable {
        super.test();
        Throwable ex = null;
        List<Step> reportSteps = getReportScenario().getSteps();
        for (int i = 0; i < getSteps().size(); i++) {
            final String rawStep = getSteps().get(i);
            final Step reportStep = reportSteps.get(i);
            long execTime = 1L;
            try {
                execTime = getPickle().getExecutor().invokeMethodForStep(rawStep, getExampleRow(), lifecycle.getContext());
                setResult(reportStep, new Result(execTime, PASSED, null));
            } catch (Throwable e) {
                ex = e;
                if (ex instanceof StepNotExecutableException) {
                    execTime = ((StepNotExecutableException) e).getExecutionTime();
                    ex = PickleExecutor.getLastCause(e);
                }
                if (ex instanceof PendingException) {
                    setResult(reportStep, new Result(execTime, SKIPPED, PickleExecutor.getThrowableStacktraceString(ex)));
                    break;
                }
                setResult(reportStep, new Result(execTime, FAILED,
                        PickleExecutor.getThrowableStacktraceString(ex) + "\n" + tailBrowserLog(MAX_LOG_TAIL_SIZE)));
            }
            try {
                byte[] screenshot = lifecycle.getContext().getWinDriver().getScreenshotAs(OutputType.BYTES);
                saveScreenshot(reportStep, screenshot);
            } catch (Exception e) {
                LOG.warn("Can not make sceenshot", e);
            }
            if (ex != null) {
                throw ex;
            }
        }
        try {
            checkLogForErrors();
        } catch (Exception e) {
            if (!getSteps().isEmpty()) {
                Step lastStep = reportSteps.get(getSteps().size() - 1);
                setResult(lastStep, new Result(lastStep.getResult().getDuration(), FAILED,
                        PickleExecutor.getThrowableStacktraceString(e) + "\n" + tailBrowserLog(MAX_LOG_TAIL_SIZE)));
            }
            throw e;
        }
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        lifecycle.tearDown(getReportScenario());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        PickleJarTest.tearDownClass();
    }

    private void setResult(Step reportStep, Result result) {
        LOG.info("\n::          {}", result.getStatus().toUpperCase());
        reportStep.setResult(result);
    }

    private String tailBrowserLog(int maxLogTailSize) throws InterruptedException, ExecutionException, TimeoutException {
        if (!WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            return "No tailed log available";
        }
        try {
            List<LogEntry> browserLog = lifecycle.getContext().getBrowserLog();
            if (browserLog.size() >= maxLogTailSize) {
                browserLog = browserLog.subList(browserLog.size() - maxLogTailSize, browserLog.size());
            }
            return browserLog.stream()
                    .map((l) -> l.getMessage().replaceAll("^.*z\\.", "z\\."))
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            LOG.warn("No tailed log available");
            return "No tailed log available";
        }
    }

    private void checkLogForErrors() throws Exception {
        List<LogEntry> browserLog = new ArrayList<>();
        if (!WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            LOG.warn("No error log check available");
            return;
        }
        try {
            browserLog = lifecycle.getContext().getBrowserLog();
            browserLog = browserLog.stream()
                    .filter((entry)
                            -> entry.getLevel().intValue() >= Level.SEVERE.intValue())
                    // filter auto login attempts
                    .filter((entry)
                            -> !entry.getMessage().contains("/access") && !entry.getMessage().contains("403"))
                    .filter((entry)
                            -> !entry.getMessage().contains("/self") && !entry.getMessage().contains("401"))
                    .filter((entry)
                            -> !entry.getMessage().contains("attempt"))
                    // filter failed logins
                    .filter((entry)
                            -> !entry.getMessage().contains("/login") && !entry.getMessage().contains("403"))
                    // filter already used email on registration
                    .filter((entry)
                            -> !entry.getMessage().contains("/register?challenge_cookie=true") && !entry.getMessage().contains(
                            "409"))
                    // filter ignored image previews
                    .filter((entry)
                            -> !entry.getMessage().contains("Ignored image preview"))
                    // filter ignored hot knocks
                    .filter((entry)
                            -> !entry.getMessage().contains("Ignored hot knock"))
                    // filter youtube cast_sender
                    .filter((entry)
                            -> !entry.getMessage().contains("cast_sender.js"))
                    // filter spotify
                    .filter((entry)
                            -> !entry.getMessage().contains("/service/version.json") && !entry.getMessage().contains(
                            "Failed to load resource"))
                    // filter removed temporary devices
                    .filter((entry)
                            -> !entry.getMessage().contains("Caused by: Local client does not exist on backend"))
                    // filter too many clients
                    .filter((entry)
                            -> !entry.getMessage().contains("/clients") && !entry.getMessage().contains("403"))
                    .filter((entry)
                            -> !entry.getMessage().contains("User has reached the maximum of allowed clients"))
                    // filter broken sessions
                    .filter((entry)
                            -> !entry.getMessage().contains(
                                    "broken or out of sync. Reset the session and decryption is likely to work again."))
                    .filter((entry)
                            -> !entry.getMessage().contains("and we have client ID"))
                    .filter((entry)
                            -> !entry.getMessage().contains("Unhandled event type"))
                    // filter full call
                    .filter((entry)
                            -> !entry.getMessage().contains("/call/state") && !entry.getMessage().contains("409"))
                    .filter((entry)
                            -> !entry.getMessage().contains("failed: the voice channel is full"))
                    .filter((entry)
                            -> !entry.getMessage().contains("Too many participants in call"))
                    // filter failed asset download
                    .filter((entry)
                            -> !entry.getMessage().contains("Failed to download asset"))
                    // filter failed group renaming
                    .filter((entry)
                            -> !entry.getMessage().contains("Failed to rename conversation"))
                    // filter encryption precondition
                    .filter((entry)
                            -> !entry.getMessage().contains("otr") && !entry.getMessage().contains("412 (Precondition Failed)"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOG.warn("No error log check available", e);
        }
        if (!browserLog.isEmpty()) {
            throw new Exception("BrowserLog does have errors: \n" + browserLog.stream()
                    .map((entry) -> String.format("%s: %s", entry.getLevel(), entry.getMessage()))
                    .collect(Collectors.joining("\n")));
        }
    }
}