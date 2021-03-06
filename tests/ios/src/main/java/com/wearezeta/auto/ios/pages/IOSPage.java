package com.wearezeta.auto.ios.pages;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.*;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.IOSDistributable;
import com.wearezeta.auto.common.driver.device_helpers.IOSSimulatorHelpers;
import com.wearezeta.auto.common.misc.Timedelta;
import io.appium.java_client.MobileBy;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.openqa.selenium.*;

import com.wearezeta.auto.ios.pages.keyboard.IOSKeyboard;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.UnreachableBrowserException;

import javax.annotation.Nullable;

import static com.wearezeta.auto.common.CommonUtils.getIosApplicationPathFromConfig;


public abstract class IOSPage extends BasePage {
    private static final Logger log = ZetaLogger.getLog(IOSPage.class.getSimpleName());

    public static final int DRIVER_CREATION_RETRIES_COUNT = 2;

    private static final int DEFAULT_RETRY_COUNT = 2;

    protected static final String nameStrMainWindow = "ZClientMainWindow";

    private static final By nameBadgeItemSelectAll = MobileBy.AccessibilityId("Select All");
    private static final By nameBadgeItemCopy = MobileBy.AccessibilityId("Copy");
    private static final By nameBadgeItemDelete = MobileBy.AccessibilityId("Delete");
    protected static final By nameBadgeItemPaste = MobileBy.AccessibilityId("Paste");
    private static final By nameBadgeItemSave = MobileBy.AccessibilityId("Save");
    private static final By nameBadgeItemEdit = MobileBy.AccessibilityId("Edit");
    private static final By nameBadgeItemLike = MobileBy.AccessibilityId("Like");
    private static final By nameBadgeItemUnlike = MobileBy.AccessibilityId("Unlike");
    private static final By nameBadgeItemForward = MobileBy.AccessibilityId("Forward");
    private static final By nameBadgeItemReveal = MobileBy.AccessibilityId("Reveal");
    private static final By nameBadgeItemShare = MobileBy.AccessibilityId("Share");

    private static final Function<String, String> xpathStrAlertByText = text ->
            String.format("//XCUIElementTypeAlert[ .//*[contains(@name, '%s')] or contains(@name, '%s')]", text, text);

    protected static final By xpathBrowserURLButton = By.xpath("//XCUIElementTypeButton[@name='URL']");

    protected static final By nameBackToWireBrowserButton = MobileBy.AccessibilityId("Return to Wire");

    protected static final By xpathConfirmButton = By.xpath("//XCUIElementTypeButton[@name='OK']");

    protected static final By xpathCancelButton = By.xpath("//XCUIElementTypeButton[@name='Cancel']");

    private static final Function<String, String> xpathStrAlertButtonByCaption = caption ->
            String.format("//XCUIElementTypeAlert//XCUIElementTypeButton[@label='%s']", caption);

    private static final Function<String, String> xpathStrAddressBarByUrlPart = urlPart ->
            String.format("//XCUIElementTypeTextField[@name='Address' and contains(@value, '%s')]", urlPart);

    private static final By nameDefaultMapApplication = MobileBy.AccessibilityId("Tracking");

    private IOSKeyboard onScreenKeyboard;

    private IOSKeyboard getOnScreenKeyboard() throws Exception {
        if (this.onScreenKeyboard == null) {
            this.onScreenKeyboard = new IOSKeyboard(getLazyDriver());
        }
        return this.onScreenKeyboard;
    }

    protected long getDriverInitializationTimeout() {
        return (ZetaIOSDriver.MAX_SESSION_INIT_DURATION.asMilliSeconds() +
                AppiumServer.RESTART_TIMEOUT.asMilliSeconds()) * DRIVER_CREATION_RETRIES_COUNT;
    }

    public IOSPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected ZetaIOSDriver getDriver() throws Exception {
        try {
            return (ZetaIOSDriver) super.getDriver();
        } catch (ExecutionException e) {
            if ((e.getCause() instanceof UnreachableBrowserException) ||
                    (e.getCause() instanceof TimeoutException) ||
                    ((e.getCause() instanceof WebDriverException) &&
                            (e.getCause().getCause() instanceof TimeoutException))) {
                throw new TimeoutException((AppiumServer.getInstance().getLog().orElse("Appium log is empty")) +
                        "\n" + ExceptionUtils.getStackTrace(e));
            } else {
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Future<ZetaIOSDriver> getLazyDriver() {
        return (Future<ZetaIOSDriver>) super.getLazyDriver();
    }

    private By getBadgeLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "select all":
                return nameBadgeItemSelectAll;
            case "edit":
                return nameBadgeItemEdit;
            case "copy":
                return nameBadgeItemCopy;
            case "delete":
                return nameBadgeItemDelete;
            case "paste":
                return nameBadgeItemPaste;
            case "save":
                return nameBadgeItemSave;
            case "like":
                return nameBadgeItemLike;
            case "unlike":
                return nameBadgeItemUnlike;
            case "forward":
                return nameBadgeItemForward;
            case "reveal":
                return nameBadgeItemReveal;
            case "share":
                return nameBadgeItemShare;
            default:
                throw new IllegalArgumentException(String.format("Unknown badge name: '%s'", name));
        }
    }

    public void tapBadgeItem(String name) throws Exception {
        final By locator = getBadgeLocatorByName(name);
        getElement(locator).click();
        // Wait for animation
        Thread.sleep(2000);
    }

    public boolean isBadgeItemVisible(String name) throws Exception {
        final By locator = getBadgeLocatorByName(name);
        return isLocatorDisplayed(locator);
    }

    public boolean isBadgeItemInvisible(String name) throws Exception {
        final By locator = getBadgeLocatorByName(name);
        return isLocatorInvisible(locator);
    }

    public boolean isKeyboardVisible() throws Exception {
        return this.getOnScreenKeyboard().isVisible();
    }

    public boolean isKeyboardInvisible() throws Exception {
        return this.isKeyboardInvisible(Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    public boolean isKeyboardInvisible(Timedelta timeout) throws Exception {
        return this.getOnScreenKeyboard().isInvisible(timeout);
    }

    public void tapKeyboardDeleteButton() throws Exception {
        this.getOnScreenKeyboard().pressDeleteButton();
    }

    public void tapHideKeyboardButton() throws Exception {
        this.getOnScreenKeyboard().pressHideButton();
    }

    public void tapSpaceKeyboardButton() throws Exception {
        this.getOnScreenKeyboard().pressSpaceButton();
    }

    public void tapKeyboardCommitButton() throws Exception {
        this.getOnScreenKeyboard().pressCommitButton();
        // Wait for animation
        Thread.sleep(1000);
    }

    public void acceptAlertIfVisible() throws Exception {
        try {
            handleAlert(AlertAction.ACCEPT, Timedelta.fromSeconds(3));
        } catch (IllegalStateException e) {
            log.error("Did not accept the alert", e);
        }
    }

    public void dismissAlertIfVisible() throws Exception {
        try {
            handleAlert(AlertAction.DISMISS, Timedelta.fromSeconds(3));
        } catch (IllegalStateException e) {
            // Ignore silently
        }
    }

    public void acceptAlert() throws Exception {
        handleAlert(AlertAction.ACCEPT, Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    public void dismissAlert() throws Exception {
        handleAlert(AlertAction.DISMISS, Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    private final static int MAX_ALERT_HANDLING_RETRIES = 5;

    public void forceAcceptAlert() throws Exception {
        // Do not keep non-closed alerts on iOS
        if (getDriver().isRealDevice()) {
            getDriver().forceAcceptAlert();
        } else {
            try {
                handleAlert(AlertAction.ACCEPT, Timedelta.fromSeconds(1));
            } catch (Exception e) {
                // just ignore it
            }
        }
    }

    private enum AlertAction {
        ACCEPT, DISMISS
    }

    public void handleAlert(AlertAction action, Timedelta timeout) throws Exception {
        final Optional<String> initialAlertText = readAlertText(timeout);
        if (initialAlertText.isPresent()) {
            int retry = 0;
            do {
                try {
                    // Workaround for https://github.com/facebook/WebDriverAgent/issues/300
                    boolean wasLandscape = false;
                    if (getDriver().getOrientation() == ScreenOrientation.LANDSCAPE) {
                        getDriver().rotate(ScreenOrientation.PORTRAIT);
                        wasLandscape = true;
                    }
                    switch (action) {
                        case ACCEPT:
                            getDriver().switchTo().alert().accept();
                            break;
                        case DISMISS:
                            getDriver().switchTo().alert().dismiss();
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    String.format("Illegal alert action '%s'", action.name())
                            );
                    }
                    if (wasLandscape) {
                        getDriver().rotate(ScreenOrientation.LANDSCAPE);
                    }
                } catch (WebDriverException e) {
                    // ignore silently
                }
                final Optional<String> currentText = readAlertText(Timedelta.fromSeconds(1));
                if (!currentText.isPresent() || !currentText.get().equals(initialAlertText.get())) {
                    return;
                }
                retry++;
            } while (retry < MAX_ALERT_HANDLING_RETRIES);
            if (retry < MAX_ALERT_HANDLING_RETRIES) {
                return;
            }
        }
        throw new IllegalStateException(
                String.format("No alert has been shown after %s or it cannot be %s after %s retries",
                        timeout.toString(),
                        (action == AlertAction.ACCEPT) ? "accepted" : "dismissed",
                        MAX_ALERT_HANDLING_RETRIES)
        );
    }

    public boolean isAlertContainsText(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrAlertByText.apply(expectedText));
        return isLocatorDisplayed(locator);
    }

    public boolean isAlertDoesNotContainsText(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrAlertByText.apply(expectedText));
        return isLocatorInvisible(locator);
    }

    public void putWireToBackgroundFor(int timeSeconds) throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            getDriver().pressHomeButton();
            Thread.sleep(timeSeconds * 1000);
            IOSSimulatorHelpers.launchApp(
                    IOSDistributable.getInstance(getIosApplicationPathFromConfig(getClass())).getBundleId()
            );
            Thread.sleep(1000);
        } else {
            this.getDriver().runAppInBackground(timeSeconds);
        }
    }

    public void pressHomeButton() throws Exception {
        getDriver().pressHomeButton();
        Thread.sleep(1000);
    }

    public void restoreWire() throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            assert getDriver() != null : "WebDriver is not ready";
            IOSSimulatorHelpers.launchApp(
                    IOSDistributable.getInstance(getIosApplicationPathFromConfig(getClass())).getBundleId()
            );
        } else {
            // Try to open Wire from dashboard icon
            getElement(MobileBy.AccessibilityId("Wire")).click();
        }
        Thread.sleep(1000);
    }

    protected void doubleClickAt(WebElement el, int percentX, int percentY) throws Exception {
        if (!CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            throw new IllegalStateException("This method works for iOS Simulator only");
        }
        final Dimension elSize = el.getSize();
        final Point elLocation = el.getLocation();
        final Dimension windowSize = getDriver().manage().window().getSize();
        IOSSimulatorHelpers.doubleClickAt(
                String.format("%.2f", (elLocation.x + elSize.width * percentX / 100.0) / windowSize.width),
                String.format("%.2f", (elLocation.y + elSize.height * percentY / 100.0) / windowSize.height));
    }

    protected void doubleClickAt(WebElement el) throws Exception {
        doubleClickAt(el, 50, 50);
    }

    /**
     * Perform click on Simulator using Python script
     *
     * @param el       optional element to click. Absolute screen size will be calculated if Optional.empty()
     *                 is provided
     * @param percentX should be between 0 and 100
     * @param percentY should be between 0 and 100
     * @param duration click duration
     * @throws Exception
     */
    protected void clickAt(Optional<WebElement> el, int percentX, int percentY, Timedelta duration) throws Exception {
        if (!CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            throw new IllegalStateException("This method works for iOS Simulator only");
        }
        double px;
        double py;
        if (el.isPresent()) {
            final Dimension windowSize = getDriver().manage().window().getSize();
            final Dimension elSize = el.get().getSize();
            final Point elLocation = el.get().getLocation();
            px = (elLocation.x + elSize.width * percentX / 100.0) / windowSize.width;
            py = (elLocation.y + elSize.height * percentY / 100.0) / windowSize.height;
        } else {
            px = percentX / 100.0;
            py = percentY / 100.0;
        }
        IOSSimulatorHelpers.clickAt(String.format("%.2f", px),
                String.format("%.2f", py),
                String.format("%.3f", duration.asFloatSeconds()));
    }

    private Point calculateTapCoordinates(Optional<FBElement> el, int percentX, int percentY) throws Exception {
        int tapX, tapY;
        if (el.isPresent()) {
            final Rectangle elRect = el.get().getRect();
            tapX = elRect.x + elRect.width * percentX / 100;
            tapY = elRect.y + elRect.height * percentY / 100;
            return new Point(tapX, tapY);
        }
        final Dimension screenSize = getDriver().manage().window().getSize();
        tapX = screenSize.getWidth() * percentX / 100;
        tapY = screenSize.getHeight() * percentY / 100;
        return new Point(tapX, tapY);
    }

    protected void doubleTapAt(FBElement el, int percentX, int percentY) throws Exception {
        this.doubleTapAt(Optional.of(el), percentX, percentY);
    }

    protected void doubleTapAt(Optional<FBElement> el, int percentX, int percentY) throws Exception {
        final Point tapPoint = calculateTapCoordinates(el, percentX, percentY);
        getDriver().doubleTapScreenAt(tapPoint.getX(), tapPoint.getY());
    }

    protected void longTapAt(FBElement el, int percentX, int percentY) throws Exception {
        this.longTapAt(Optional.of(el), percentX, percentY, Timedelta.fromMilliSeconds(DriverUtils.LONG_TAP_DURATION));
    }

    protected void longTapAt(Optional<FBElement> el, int percentX, int percentY, Timedelta duration)
            throws Exception {
        final Point tapPoint = calculateTapCoordinates(el, percentX, percentY);
        getDriver().longTapScreenAt(tapPoint.getX(), tapPoint.getY(), duration);
    }

    protected void longClickAt(WebElement el, int percentX, int percentY) throws Exception {
        this.clickAt(Optional.of(el), percentX, percentY, Timedelta.fromMilliSeconds(DriverUtils.LONG_TAP_DURATION));
    }

    protected void longClickAt(int percentX, int percentY) throws Exception {
        this.clickAt(Optional.empty(), percentX, percentY, Timedelta.fromMilliSeconds(DriverUtils.LONG_TAP_DURATION));
    }

    protected void clickAt(int percentX, int percentY) throws Exception {
        this.clickAt(Optional.empty(), percentX, percentY, Timedelta.fromMilliSeconds(DriverUtils.SINGLE_TAP_DURATION));
    }

    public void rotateScreen(ScreenOrientation orientation) throws Exception {
        switch (orientation) {
            case LANDSCAPE:
                rotateLandscape();
                break;
            case PORTRAIT:
                rotatePortrait();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown orientation '%s'",
                        orientation));
        }
    }

    private void rotateLandscape() throws Exception {
        this.getDriver().rotate(ScreenOrientation.LANDSCAPE);
    }

    private void rotatePortrait() throws Exception {
        this.getDriver().rotate(ScreenOrientation.PORTRAIT);
    }

    public void lockScreen(Timedelta duration) throws Exception {
        assert getDriver() != null : "WebDriver is not ready";
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            IOSSimulatorHelpers.lock();
            Thread.sleep(2000);
            // this is to show the unlock label if not visible yet
            IOSSimulatorHelpers.goHome();
            if (getDriver().getOSVersion().compareTo(new DefaultArtifactVersion("10.0")) >= 0) {
                IOSSimulatorHelpers.goHome();
            } else {
                IOSSimulatorHelpers.swipeRight();
            }
            Thread.sleep(2000);
        } else {
            this.getDriver().lockDevice(duration.asSeconds());
        }
    }

    public void swipeAtElement(WebElement el, int percentStartX, int percentStartY,
                               int percentEndX, int percentEndY, double durationSeconds) throws Exception {
        if (!CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            throw new IllegalStateException("This method is supported only on Simulator");
        }
        final Point location = el.getLocation();
        final Dimension size = el.getSize();
        final Dimension screenSize = getDriver().manage().window().getSize();
        IOSSimulatorHelpers.swipe(
                (location.getX() + percentStartX * size.getWidth() / 100.0) / screenSize.getWidth(),
                (location.getY() + percentStartY * size.getHeight() / 100.0) / screenSize.getHeight(),
                (location.getX() + percentEndX * size.getWidth() / 100.0) / screenSize.getWidth(),
                (location.getY() + percentEndY * size.getHeight() / 100.0) / screenSize.getHeight(),
                (long) (durationSeconds * 1000.0));
    }

    public void tapElementWithRetryIfStillDisplayed(By locator, int retryCount) throws Exception {
        WebElement el = getElement(locator);
        int counter = 0;
        do {
            el.click();
            counter++;
            if (isLocatorInvisible(locator, Timedelta.fromSeconds(4))) {
                return;
            }
        } while (counter < retryCount);
        throw new IllegalStateException(String.format("Locator %s is still displayed", locator));
    }

    public void tapElementWithRetryIfStillDisplayed(By locator) throws Exception {
        tapElementWithRetryIfStillDisplayed(locator, DEFAULT_RETRY_COUNT);
    }

    public void tapElementWithRetryIfNextElementNotAppears(By locator, By nextLocator, int retryCount)
            throws Exception {
        WebElement el = getElement(locator);
        int counter = 0;
        do {
            el.click();
            counter++;
            if (isLocatorExist(nextLocator)) {
                return;
            }
        } while (counter < retryCount);
        throw new IllegalStateException(String.format("Locator %s did't appear", nextLocator));
    }

    public void tapElementWithRetryIfNextElementAppears(By locator, By nextLocator) throws Exception {
        tapElementWithRetryIfNextElementNotAppears(locator, nextLocator, DEFAULT_RETRY_COUNT);
    }

    //region Elements location

    protected WebElement getElement(WebElement parent, By locator) throws Exception {
        return this.getElement(parent, locator,
                String.format("The element '%s' is not visible", locator),
                Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds())
        );
    }

    @Override
    protected WebElement getElement(By locator) throws Exception {
        return this.getElement(locator,
                String.format("The element '%s' is not visible", locator),
                Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds())
        );
    }

    protected WebElement getElement(WebElement parent, By locator, String message) throws Exception {
        return this.getElement(parent, locator, message,
                Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    @Override
    protected WebElement getElement(By locator, String message) throws Exception {
        return this.getElement(locator, message, Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    private static final double MAX_EXISTENCE_DELAY_MS = 3000.0;
    private static final long MIN_EXISTENCE_ITERATIONS_COUNT = 2;

    protected WebElement getElement(@Nullable WebElement parent, By locator, String message,
                                    Timedelta timeout) throws Exception {
        WebDriverException savedException;
        final Timedelta started = Timedelta.now();
        int iterationNumber = 1;
        do {
            try {
                final WebElement el;
                if (parent == null) {
                    el = getDriver().findElement(locator);
                } else {
                    el = parent.findElement(locator);
                }
                if (el.isDisplayed()) {
                    return el;
                }
                throw new WebDriverException(String.format("The element '%s' is still not visible after %s",
                        locator, Timedelta.now().diff(started).toString()));
            } catch (WebDriverException e) {
                log.debug(e.getMessage());
                savedException = e;
            }
            Thread.sleep((long) (MAX_EXISTENCE_DELAY_MS / iterationNumber));
            iterationNumber++;
        } while (Timedelta.now().isDiffLessOrEqual(started, timeout) ||
                iterationNumber <= MIN_EXISTENCE_ITERATIONS_COUNT);
        throw new IllegalStateException(message, savedException);
    }

    @Override
    protected WebElement getElement(By locator, String message, Timedelta timeout) throws Exception {
        return getElement(null, locator, message, timeout);
    }

    protected boolean isLocatorExist(By locator) throws Exception {
        return this.isLocatorExist(locator, Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    protected boolean isLocatorExist(By locator, Timedelta timeout) throws Exception {
        final Timedelta started = Timedelta.now();
        int iterationNumber = 1;
        do {
            try {
                final WebElement el = getDriver().findElement(locator);
                if (el != null) {
                    return true;
                }
                throw new WebDriverException(String.format("The element '%s' is still not present after %s",
                        locator, Timedelta.now().diff(started).toString()));
            } catch (WebDriverException e) {
                log.debug(e.getMessage());
            }
            Thread.sleep((long) (MAX_EXISTENCE_DELAY_MS / iterationNumber));
            iterationNumber++;
        } while (Timedelta.now().isDiffLessOrEqual(started, timeout) ||
                iterationNumber <= MIN_EXISTENCE_ITERATIONS_COUNT);
        return false;
    }

    protected boolean isLocatorDisplayed(WebElement parent, By locator) throws Exception {
        return this.isLocatorDisplayed(parent, locator,
                Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    protected boolean isLocatorDisplayed(By locator) throws Exception {
        return this.isLocatorDisplayed(locator, Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    protected boolean isLocatorDisplayed(@Nullable WebElement parent, By locator, Timedelta timeout) throws Exception {
        final Timedelta started = Timedelta.now();
        int iterationNumber = 1;
        do {
            try {
                final WebElement el;
                if (parent == null) {
                    el = getDriver().findElement(locator);
                } else {
                    el = parent.findElement(locator);
                }
                if (el.isDisplayed()) {
                    return true;
                }
                throw new WebDriverException(String.format("The element '%s' is still not visible after %s",
                        locator, Timedelta.now().diff(started).toString()));
            } catch (WebDriverException e) {
                log.debug(e.getMessage());
            }
            Thread.sleep((long) (MAX_EXISTENCE_DELAY_MS / iterationNumber));
            iterationNumber++;
        } while (Timedelta.now().isDiffLessOrEqual(started, timeout) ||
                iterationNumber <= MIN_EXISTENCE_ITERATIONS_COUNT);
        return false;
    }

    protected boolean isLocatorDisplayed(By locator, Timedelta timeout) throws Exception {
        return isLocatorDisplayed(null, locator, timeout);
    }

    protected boolean isLocatorInvisible(By locator) throws Exception {
        return this.isLocatorInvisible(locator, Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    protected boolean isLocatorInvisible(WebElement parent, By locator) throws Exception {
        return this.isLocatorInvisible(parent, locator,
                Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    protected boolean isLocatorInvisible(@Nullable WebElement parent, By locator, Timedelta timeout) throws Exception {
        final Timedelta started = Timedelta.now();
        int iterationNumber = 1;
        do {
            try {
                final WebElement el;
                if (parent == null) {
                    el = getDriver().findElement(locator);
                } else {
                    el = parent.findElement(locator);
                }
                if (!el.isDisplayed()) {
                    return true;
                }
                log.debug(String.format("The element '%s' is still visible after %s",
                        locator, Timedelta.now().diff(started).toString()));
            } catch (WebDriverException e) {
                return true;
            }
            Thread.sleep((long) (MAX_EXISTENCE_DELAY_MS / iterationNumber));
            iterationNumber++;
        } while (Timedelta.now().isDiffLessOrEqual(started, timeout) ||
                iterationNumber <= MIN_EXISTENCE_ITERATIONS_COUNT);
        return false;
    }

    protected boolean isLocatorInvisible(By locator, Timedelta timeout) throws Exception {
        return isLocatorInvisible(null, locator, timeout);
    }

    protected boolean isElementInvisible(WebElement element) throws Exception {
        return this.isElementInvisible(element, Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    protected boolean isElementInvisible(WebElement el, Timedelta timeout) throws Exception {
        final Timedelta started = Timedelta.now();
        int iterationNumber = 1;
        do {
            try {
                if (!el.isDisplayed()) {
                    return true;
                }
                log.debug(String.format("The element '%s' is still visible after %s",
                        el, Timedelta.now().diff(started).toString()));
            } catch (WebDriverException e) {
                return true;
            }
            Thread.sleep((long) (MAX_EXISTENCE_DELAY_MS / iterationNumber));
            iterationNumber++;
        } while (Timedelta.now().isDiffLessOrEqual(started, timeout) ||
                iterationNumber <= MIN_EXISTENCE_ITERATIONS_COUNT);
        return false;
    }

    protected boolean isElementVisible(WebElement element) throws Exception {
        return this.isElementVisible(element, Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    protected boolean isElementVisible(WebElement el, Timedelta timeout) throws Exception {
        final Timedelta started = Timedelta.now();
        int iterationNumber = 1;
        do {
            try {
                if (el.isDisplayed()) {
                    return true;
                }
                log.debug(String.format("The element '%s' is still invisible after %s",
                        el, Timedelta.now().diff(started).toString()));
            } catch (WebDriverException e) {
                return false;
            }
            Thread.sleep((long) (MAX_EXISTENCE_DELAY_MS / iterationNumber));
            iterationNumber++;
        } while (Timedelta.now().isDiffLessOrEqual(started, timeout) ||
                iterationNumber <= MIN_EXISTENCE_ITERATIONS_COUNT);
        return false;
    }

    @Override
    protected Optional<WebElement> getElementIfDisplayed(By locator, Timedelta timeout) throws Exception {
        final Timedelta started = Timedelta.now();
        int iterationNumber = 1;
        do {
            try {
                final WebElement el = getDriver().findElement(locator);
                if (el.isDisplayed()) {
                    return Optional.of(el);
                }
                throw new WebDriverException(String.format("The element '%s' is still not visible after %s",
                        locator, Timedelta.now().diff(started).toString()));
            } catch (WebDriverException e) {
                log.debug(e.getMessage());
            }
            Thread.sleep((long) (MAX_EXISTENCE_DELAY_MS / iterationNumber));
            iterationNumber++;
        } while (Timedelta.now().isDiffLessOrEqual(started, timeout));
        return Optional.empty();
    }

    @Override
    protected Optional<WebElement> getElementIfExists(By locator) throws Exception {
        return this.getElementIfExists(locator, Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    @Override
    protected Optional<WebElement> getElementIfExists(By locator, Timedelta timeout) throws Exception {
        final Timedelta started = Timedelta.now();
        int iterationNumber = 1;
        do {
            try {
                final WebElement el = getDriver().findElement(locator);
                if (el != null) {
                    return Optional.of(el);
                }
                throw new WebDriverException(String.format("The element '%s' is still not present after %s",
                        locator, Timedelta.now().diff(started).toString()));
            } catch (WebDriverException e) {
                log.debug(e.getMessage());
            }
            Thread.sleep((long) (MAX_EXISTENCE_DELAY_MS / iterationNumber));
            iterationNumber++;
        } while (Timedelta.now().isDiffLessOrEqual(started, timeout));
        return Optional.empty();
    }

    @Override
    protected List<WebElement> selectVisibleElements(By locator) throws Exception {
        return this.selectVisibleElements(locator, Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    @Override
    protected List<WebElement> selectVisibleElements(By locator, Timedelta timeout) throws Exception {
        final List<WebElement> result = new ArrayList<>();
        final Timedelta started = Timedelta.now();
        int iterationNumber = 1;
        do {
            result.addAll(
                    getDriver().findElements(locator).stream().
                            filter(WebElement::isDisplayed).collect(Collectors.toList())
            );
            if (result.size() > 0) {
                return result;
            }
            Thread.sleep((long) (MAX_EXISTENCE_DELAY_MS / iterationNumber));
            iterationNumber++;
        } while (Timedelta.now().isDiffLessOrEqual(started, timeout));
        return result;
    }

    //endregion

    public boolean isWebPageVisible(String expectedUrl) throws Exception {
        getElement(xpathBrowserURLButton, "The address bar of web browser is not visible").click();
        return isLocatorExist(By.xpath(xpathStrAddressBarByUrlPart.apply(expectedUrl)));
    }

    public void tapBackToWire() throws Exception {
        final WebElement backToWireButton = getElement(nameBackToWireBrowserButton);
        backToWireButton.click();
        // Wait for animation
        Thread.sleep(3000);
    }

    public void tapConfirmButton() throws Exception {
        getElement(xpathConfirmButton).click();
    }

    public void tapCancelButton() throws Exception {
        getElement(xpathCancelButton).click();
    }

    public void installApp(File appFile) throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelpers.installApp(appFile);
        } else {
            throw new NotImplementedException("Application install is only available for Simulator");
        }
    }

    public void uninstallApp(String bundleId) throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelpers.uninstallApp(bundleId);
        } else {
            throw new NotImplementedException("Application uninstall is only available for Simulator");
        }
    }

    public void tapByPercentOfElementSize(FBElement el, int percentX, int percentY) throws Exception {
        final Dimension size = el.getSize();
        el.tap(percentX * size.getWidth() / 100, percentY * size.getHeight() / 100);
    }

    public void tapAtTheCenterOfElement(FBElement el) throws Exception {
        tapByPercentOfElementSize(el, 50, 50);
    }

    public void tapScreenAt(int x, int y) throws Exception {
        getDriver().tapScreenAt(x, y);
    }

    public void tapScreenByPercents(int percentX, int percentY) throws Exception {
        final Dimension size = getDriver().manage().window().getSize();
        getDriver().tapScreenAt(percentX * size.getWidth() / 100, percentY * size.getHeight() / 100);
    }

    public Optional<String> readAlertText() throws Exception {
        return readAlertText(Timedelta.fromSeconds(DriverUtils.getDefaultLookupTimeoutSeconds()));
    }

    public Optional<String> readAlertText(Timedelta timeout) throws Exception {
        final Timedelta start = Timedelta.now();
        do {
            try {
                final String text = getDriver().switchTo().alert().getText();
                if (text != null && text.length() > 0 && !text.equals("null")) {
                    return Optional.of(text);
                }
            } catch (WebDriverException e) {
                Thread.sleep(500);
            }
        } while (Timedelta.now().isDiffLessOrEqual(start, timeout));
        return Optional.empty();
    }

    public void tapAlertButton(String caption) throws Exception {
        final By locator = By.xpath(xpathStrAlertButtonByCaption.apply(caption));
        getElement(locator).click();
    }

    @Override
    public Optional<BufferedImage> takeScreenshot() throws Exception {
        Optional<BufferedImage> screenshotImage = super.takeScreenshot();
        if (screenshotImage.isPresent()) {
            final Dimension screenSize = getDriver().manage().window().getSize();
            if (screenshotImage.get().getWidth() != screenSize.getWidth()) {
                // proportions are expected to be the same
                final double scale = 1.0 * screenSize.getWidth() / screenshotImage.get().getWidth();
                screenshotImage = Optional.of(
                        ImageUtil.resizeImage(screenshotImage.get(), (float) scale)
                );
            }
        }
        return screenshotImage;
    }

    public boolean isDefaultMapApplicationVisible() throws Exception {
        return isLocatorExist(nameDefaultMapApplication);
    }

    public void tapEmojiKeyboardKey(String keyName) throws Exception {
        final List<WebElement> elements = getDriver().findElements(MobileBy.AccessibilityId(keyName));
        if (elements.size() > 0) {
            elements.get(elements.size() - 1).click();
            // Wait for animation
            Thread.sleep(1000);
        } else {
            throw new IllegalArgumentException(String.format("There is no '%s' key on Emoji keyboard", keyName));
        }
    }

    @Override
    public Optional<BufferedImage> getElementScreenshot(WebElement element) throws Exception {
        Rectangle dstRect;
        if (element instanceof FBElement) {
            dstRect = ((FBElement) element).getRect();
        } else {
            final Point elementLocation = element.getLocation();
            final Dimension elementSize = element.getSize();
            dstRect = new Rectangle(elementLocation.x, elementLocation.y, elementSize.width, elementSize.height);
        }
        return this.getElementScreenshot(dstRect);
    }
}
