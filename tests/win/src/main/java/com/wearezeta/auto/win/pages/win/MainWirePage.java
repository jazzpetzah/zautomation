package com.wearezeta.auto.win.pages.win;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWinDriver;
import com.wearezeta.auto.win.locators.WinLocators;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/*

This page object is used to execute various tasks related to the size of the Wrapper (Resize, minimize etc.). Coordinates depend
on various different factors, like window decoration and screen size. This is a basic overview of the screen:

 ----------------------------------------
| Screen                                 |
|       _______________________________  |
|      | Window                        | |
|      | -------------------------     | |
|      ||  WebView                |    | |
|      | -------------------------     | |
|      --------------------------------  |
 ----------------------------------------

 The screen is the whole desktop size. The window is the wrapper with all window decoration, like title bar, menu bar, etc. The
 WebView is the Chrome instance that is embedded in the wrapper window.

 */
public class MainWirePage extends WinPage {

    private static final Logger LOG = ZetaLogger.getLog(MainWirePage.class.getSimpleName());

    public static final int APP_MAX_WIDTH = 1103;
    private static final Dimension LOGIN_PAGE_DIMENSION = new Dimension(403, 618);
    private static final Dimension APP_MIN_DIMENSION = new Dimension(780, 600);
    private static final int APP_EXPAND_TIMEOUT_MILLIS = 10000;
    private static final int TITLEBAR_HEIGHT = 35;
    private static final int WINDOW_DECORATION_WIDTH = 10;
    private static final int MENUBAR_HEIGHT = 20;

    private final Robot robot = new Robot();

    @FindBy(how = How.XPATH, using = WinLocators.MainWirePage.xpathWindow)
    protected WebElement window;

    @FindBy(how = How.XPATH, using = WinLocators.MainWirePage.xpathMinimizeButton)
    protected WebElement minimizeButton;

    @FindBy(how = How.XPATH, using = WinLocators.MainWirePage.xpathZoomButton)
    protected WebElement zoomButton;

    @FindBy(how = How.XPATH, using = WinLocators.MainWirePage.xpathCloseButton)
    protected WebElement closeButton;

    public MainWirePage(Future<ZetaWinDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void focusApp() {
        window.click();
    }

    public boolean isMainWindowVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(WinLocators.MainWirePage.xpathWindow));
    }

    public void minimizeWindow() throws Exception {
        minimizeButton.click();
    }

    public void closeWindow() {
        closeButton.click();
    }

    public void pressShortCutForQuit() throws Exception {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F4);
        robot.keyRelease(KeyEvent.VK_F4);
        robot.keyRelease(KeyEvent.VK_ALT);
    }

    public void pressShortCutForPreferences() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_COMMA);
        robot.keyRelease(KeyEvent.VK_COMMA);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public boolean isMini() throws Exception {
        return isApproximatelyWidth(APP_MIN_DIMENSION.getWidth()) && isApproximatelyHeight(APP_MIN_DIMENSION.getHeight());
    }
    
    public boolean isLoginPageSize() throws Exception {
        return isApproximatelyWidth(LOGIN_PAGE_DIMENSION.getWidth()) && isApproximatelyHeight(LOGIN_PAGE_DIMENSION.getHeight());
    }
    
    public boolean waitUntilAppExpands() throws Exception{
        WebDriverWait wait = new WebDriverWait(getDriver(), APP_EXPAND_TIMEOUT_MILLIS, 1000);
        return wait.until((ExpectedCondition<Boolean>) (WebDriver unusedDriver) -> {
            try {
                return !isLoginPageSize();
            } catch (Exception ex) {
                LOG.warn("Check for window expanded failed", ex);
                return false;
            }
        });
    }

    public int getX() throws Exception {
        return getDriver().manage().window().getPosition().getX();
    }

    public int getY() throws Exception {
        return getDriver().manage().window().getPosition().getY();
    }

    public int getWidth() throws Exception {
        return getDriver().manage().window().getSize().getWidth();
    }

    public int getHeight() throws Exception {
        return getDriver().manage().window().getSize().getHeight();
    }

    public boolean isApproximatelyWidth(int width) throws Exception {
        int plusMinus = 5;
        return getWidth() > (width - plusMinus) && getWidth() < (width + plusMinus);
    }

    public boolean isApproximatelyHeight(int height) throws Exception {
        int plusMinus = 5;
        return getHeight() > (height - plusMinus) && getHeight() < (height + plusMinus);
    }

    public void resizeByHand(int width, int height) throws Exception {
        final Dimension windowDimensions = getDriver().manage().window().getSize();
        final Point windowPosition = getDriver().manage().window().getPosition();
        final Point lowerRightWindowHandle = getLowerRightWindowHandle(windowDimensions, windowPosition);

        long newWidthOverflow = windowPosition.getX() + (long) width;
        long newHeightOverflow = windowPosition.getY() + (long) height;

        if (newWidthOverflow >= Integer.MAX_VALUE) {
            newWidthOverflow = Integer.MAX_VALUE;
        }
        if (newHeightOverflow >= Integer.MAX_VALUE) {
            newHeightOverflow = Integer.MAX_VALUE;
        }

        robot.mouseMove(lowerRightWindowHandle.getX(),
                lowerRightWindowHandle.getY());
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseMove((int) newWidthOverflow, (int) newHeightOverflow);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void positionByHand(int x, int y) throws Exception {
        // Offset prevents the click on the logo in the titlebar
        final int TITLEBAR_X_OFFSET = 100;
        final int TITLEBAR_Y_OFFSET = 10;
        final Point windowPosition = getDriver().manage().window().getPosition();
        final Point titleBar = new Point(windowPosition.getX() + TITLEBAR_X_OFFSET, windowPosition.getY() + TITLEBAR_Y_OFFSET);
        final Point customPosition = new Point(x + TITLEBAR_X_OFFSET, y + TITLEBAR_Y_OFFSET);

        robot.mouseMove(titleBar.getX(), titleBar.getY());
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseMove(customPosition.getX(), customPosition.getY());
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void resizeToMaxByHand() throws Exception {
        resizeByHand(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public void resizeToMinByHand() throws Exception {
        resizeByHand(0, 0);
    }

    public void ensurePosition() throws Exception {
        positionByHand(0, 0);
    }

    private Point getLowerRightWindowHandle(Dimension windowDimensions,
                                            Point windowPosition) {
        // we have to subtract 1 to get the handle
        return new Point(windowPosition.getX() + windowDimensions.getWidth() - 1,
                windowPosition.getY() + windowDimensions.getHeight() - 1);
    }

    public void clickMaximizeButton() {
        zoomButton.click();
    }

    public void clickOnWebView(Point point) throws Exception {
        // Click center of element
        int x = getX() + WINDOW_DECORATION_WIDTH + point.getX();
        int y = getY() + TITLEBAR_HEIGHT + MENUBAR_HEIGHT + point.getY();
        click(x, y);
    }

    public void rightClickOnWebView(Point point) throws Exception {
        // Click center of element
        int x = getX() + WINDOW_DECORATION_WIDTH + point.getX();
        int y = getY() + TITLEBAR_HEIGHT + MENUBAR_HEIGHT + point.getY();
        rightClick(x, y);
    }

    public void click(int x, int y) throws InterruptedException {
        LOG.info("Click at " + x + ":" + y);
        screenCapturePosition(x, y);
        robot.mouseMove(x, y);
        Thread.sleep(100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void rightClick(int x, int y) throws InterruptedException {
        LOG.info("Right click at " + x + ":" + y);
        screenCapturePosition(x, y);
        robot.mouseMove(x, y);
        Thread.sleep(100);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    private void screenCapturePosition(int x, int y) {
        int borderSize = 20;
        BufferedImage image = robot.createScreenCapture(new Rectangle(x - borderSize, y - borderSize, borderSize * 2 + 1,
                borderSize * 2 + 1));
        Color color = new Color(255, 0, 0);
        image.setRGB(borderSize + 1, borderSize + 1, color.getRGB());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            LOG.error("Could not write screen capture: " + e.getMessage());
        }
        LOG.info("data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray()));
    }
}
