package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rc.RCTestcase;
import com.wearezeta.auto.common.rc.TestcaseResultToTestrailTransformer;
import com.wearezeta.auto.common.testrail.TestrailSyncUtilities;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;
import gherkin.formatter.model.Tag;
import io.appium.java_client.ios.IOSDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ZetaFormatter implements Formatter, Reporter {

    private static String feature = "";
    private static String scenario = "";
    private static Map<Step, String> steps = new LinkedHashMap<>();
    private static Optional<Iterator<Step>> stepsIterator = Optional.empty();

    private static final Logger log = ZetaLogger.getLog(ZetaFormatter.class.getSimpleName());

    private long stepStartedTimestamp;

    @Override
    public void background(Background arg0) {
    }

    @Override
    public void close() {

    }

    @Override
    public void done() {

    }

    @Override
    public void eof() {
    }

    @Override
    public void examples(Examples arg0) {
    }

    @Override
    public void feature(Feature arg0) {
        feature = arg0.getName();
        log.debug("\n\n----------------------------------------------\nFeature: " + feature +
                "\n----------------------------------------------");
    }

    private static String formatTags(List<Tag> tags) {
        final StringBuilder result = new StringBuilder();
        result.append("[ ");
        for (Tag tag : tags) {
            result.append(tag.getName()).append(" ");
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public void scenario(Scenario arg0) {
        scenario = arg0.getName();
        log.debug(String.format("\n\n----------------------------------------------\nScenario: %s " +
                "%s\n----------------------------------------------", scenario, formatTags
                (arg0.getTags())));
        if (steps.size() > 0) {
            steps.clear();
        }
    }

    @Override
    public void scenarioOutline(ScenarioOutline arg0) {
    }

    // This will prefill all the step names before any 'result' method is called
    @Override
    public void step(Step arg0) {
        steps.put(arg0, null);
    }

    @Override
    public void syntaxError(String arg0, String arg1, List<String> arg2, String arg3, Integer arg4) {

    }

    @Override
    public void uri(String arg0) {
    }

    @Override
    public void after(Match arg0, Result arg1) {
    }

    @Override
    public void before(Match arg0, Result arg1) {
    }

    @Override
    public void embedding(String arg0, byte[] arg1) {
    }

    @Override
    public void match(Match arg0) {
        stepStartedTimestamp = System.currentTimeMillis();
    }

    private static final int MAX_SCREENSHOT_WIDTH = 1440;
    private static final int MAX_SCREENSHOT_HEIGHT = 800;

    private void takeStepScreenshot(final Result stepResult, final String stepName) throws Exception {
        final ZetaDriver driver = getDriver().orElse(null);
        if (driver != null) {
            if (stepResult.getStatus().equals(Result.SKIPPED.getStatus())) {
                // Don't make screenshots for skipped steps to speed up
                // suite execution
                return;
            }
            int index = 1;
            File tmpScreenshot;
            do {
                final String stepNameForScr = stepName.replaceAll("\\W+", "_");
                tmpScreenshot = new File(String.format("%s/%s/%s/%s_%s.png",
                        CommonUtils.getPictureResultsPathFromConfig(this.getClass()), feature.replaceAll("\\W+", "_"),
                        scenario.replaceAll("\\W+", "_"),
                        stepNameForScr.matches(".*_") ? stepNameForScr.substring(0, stepNameForScr.length() - 1) :
                                stepNameForScr, index));
                index++;
            } while (tmpScreenshot.exists());
            final File resultScreenshot = tmpScreenshot;
            if (driver instanceof IOSDriver) {
                final Optional<BufferedImage> screenshot = DriverUtils.takeFullScreenShot(driver);
                if (screenshot.isPresent()) {
                    screenshotSavers.execute(() -> ImageUtil.storeImage(ImageUtil.scaleTo(screenshot.get(),
                            MAX_SCREENSHOT_WIDTH, MAX_SCREENSHOT_HEIGHT), resultScreenshot));
                } else {
                    if (CommonUtils.getIsSimulatorFromConfig(ZetaFormatter.class)) {
                        try {
                            CommonUtils.takeIOSSimulatorScreenshot(resultScreenshot);
                        } catch (Exception e) {
                            log.error("Failed to take iOS simulator screenshot:");
                            e.printStackTrace();
                        }
                    }
                }
            } else if (driver instanceof ZetaAndroidDriver) {
                try {
                    CommonUtils.takeAndroidScreenshot((ZetaAndroidDriver) driver, resultScreenshot, true);
                } catch (Exception e) {
                    log.error("Failed to take Android screenshot:");
                    e.printStackTrace();
                }
            } else {
                final Optional<BufferedImage> screenshot = DriverUtils.takeFullScreenShot(driver);
                if (!screenshot.isPresent()) {
                    return;
                }
                screenshotSavers.execute(() -> ImageUtil.storeImage(ImageUtil.scaleTo(screenshot.get(),
                        MAX_SCREENSHOT_WIDTH, MAX_SCREENSHOT_HEIGHT), resultScreenshot));
            }
        } else {
            log.debug(String.format("Selenium driver is not ready yet. Skipping screenshot creation for step '%s'",
                    stepName));
        }
    }

    private static boolean isScreenshotingEnabled = true;

    static {
        try {
            isScreenshotingEnabled = CommonUtils.getMakeScreenshotsFromConfig(ZetaFormatter.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isScreenshotingOnPassedStepsEnabled = true;

    static {
        try {
            isScreenshotingOnPassedStepsEnabled = CommonUtils.getMakeScreenshotOnPassedStepsFromConfig(ZetaFormatter.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void result(Result result) {
        if (!stepsIterator.isPresent()) {
            stepsIterator = Optional.of(steps.keySet().iterator());
        }
        final Step currentStep = stepsIterator.get().next();
        final String stepName = currentStep.getName();
        final String stepStatus = result.getStatus();
        steps.put(currentStep, stepStatus);
        final long stepFinishedTimestamp = System.currentTimeMillis();
        if (isScreenshotingEnabled) {
            if (!isScreenshotingOnPassedStepsEnabled && (result.getStatus().equals(Result.PASSED))) {
                log.debug("Skip screenshot for passed step....");
            } else {
                try {
                    takeStepScreenshot(result, stepName);
                } catch (Exception e) {
                    // Ignore screenshoting exceptions
                    e.printStackTrace();
                }
            }
            log.debug(String.format("\n----------------------------------------------\nSTEP: %s (status: %s, step duration: " +
                            "%s ms + screenshot duration: %s ms)\n----------------------------------------------",
                    stepName, stepStatus, stepFinishedTimestamp - stepStartedTimestamp, System.currentTimeMillis() - stepFinishedTimestamp));
        } else {
            log.debug(String.format("\n----------------------------------------------\nSTEP: %s (status: %s, step duration: " +
                            "%s " +
                            "ms)\n----------------------------------------------", stepName, stepStatus,
                    stepFinishedTimestamp - stepStartedTimestamp));
        }
    }

    @Override
    public void write(String arg0) {
    }

    private static Optional<ZetaDriver> getDriver() throws Exception {
        if (lazyDriver != null && lazyDriver.isDone() && !lazyDriver.isCancelled()) {
            return Optional.of((ZetaDriver) lazyDriver.get());
        } else {
            return Optional.empty();
        }
    }

    private static Future<? extends RemoteWebDriver> lazyDriver = null;

    public synchronized static void setLazyDriver(Future<? extends RemoteWebDriver> lazyDriver) {
        ZetaFormatter.lazyDriver = lazyDriver;
    }

    private static final ExecutorService screenshotSavers = Executors.newFixedThreadPool(3);

    @Override
    public void startOfScenarioLifeCycle(Scenario scenario) {
    }

    private static Set<String> normalizeTags(List<Tag> tags) {
        Set<String> result = new LinkedHashSet<>();
        for (Tag tag : tags) {
            if (tag.getName().startsWith(RCTestcase.MAGIC_TAG_PREFIX)) {
                result.add(tag.getName());
            } else {
                result.add(RCTestcase.MAGIC_TAG_PREFIX + tag.getName());
            }
        }
        return result;
    }

    @Override
    public void endOfScenarioLifeCycle(Scenario scenario) {
        try {
            final Set<String> normalizedTags = normalizeTags(scenario.getTags());

            TestrailSyncUtilities.syncExecutedScenarioWithTestrail(scenario,
                    new TestcaseResultToTestrailTransformer(steps).transform(), normalizedTags);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            steps.clear();
            stepsIterator = Optional.empty();
        }
    }

    public static String getFeature() {
        return feature;
    }

    public static String getScenario() {
        return scenario;
    }
}
