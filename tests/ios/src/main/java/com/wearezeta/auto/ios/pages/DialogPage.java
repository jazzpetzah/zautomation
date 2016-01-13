package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import javax.script.ScriptException;

import io.appium.java_client.ios.IOSElement;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.ScreenOrientation;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.IOSConstants;

public class DialogPage extends IOSPage {
    private static final Logger log = ZetaLogger.getLog(DialogPage.class
            .getSimpleName());

    public static final String PING_LABEL = "PINGED";
    public static final String HOT_PING_LABEL = "PINGED AGAIN";
    private static final long PING_ANIMATION_TIME = 3000;

    final String[] scriptArr = new String[]{
            "property thisapp: \"Simulator\"",
            "tell application \"System Events\"", " tell process thisapp",
            " click menu item \"Paste\" of menu \"Edit\" of menu bar 1",
            " end tell", "end tell"};

    private static final String xpathConversationWindow = "//UIATableView";
    @FindBy(xpath = xpathConversationWindow)
    private WebElement conversationWindow;

    private static final String nameConversationBackButton = "ConversationBackButton";
    @FindBy(name = nameConversationBackButton)
    private WebElement conversationBackButton;

    private static final String nameConversationCursorInput = "ConversationTextInputField";
    @FindBy(name = nameConversationCursorInput)
    private WebElement conversationInput;

    private static final String nameTextInput = "ComposeControllerTextView";
    @FindBy(name = nameTextInput)
    private WebElement textInput;

    private static final String xpathPinged =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']" +
                    "/UIATableView[1]/UIATableCell[last()]/UIAStaticText[contains(@name, 'PINGED')]";
    @FindBy(xpath = xpathPinged)
    private WebElement pinged;

    private static final String xpathPingedAgain = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']" +
            "/UIATableView[1]/UIATableCell[last()]/UIAStaticText[contains(@name, 'PINGED AGAIN')]";
    @FindBy(xpath = xpathPingedAgain)
    private WebElement pingedAgain;

    private static final String namePlusButton = "plusButton";
    @FindBy(name = namePlusButton)
    protected WebElement plusButton;

    private static final String nameOpenConversationDetails = "ComposeControllerConversationDetailButton";
    @FindBy(name = nameOpenConversationDetails)
    protected WebElement openConversationDetails;

    private static final String classNameDialogMessages = "UIATableCell";
    @FindBy(className = classNameDialogMessages)
    private List<WebElement> messagesList;

    private static final String xpathConnectMessageLabel = "//UIAStaticText[starts-with(@name, 'CONNECTING TO')]";
    @FindBy(xpath = xpathConnectMessageLabel)
    private WebElement connectMessageLabel;

    private static final String xpathConnectionMessage = "//UIAStaticText[contains(@name, 'Let’s connect on Wire.')]";
    @FindBy(xpath = xpathConnectionMessage)
    private WebElement connectionMessage;

    private static final String nameYouRenamedConversation = "YOU RENAMED THE CONVERSATION";
    @FindBy(name = nameYouRenamedConversation)
    private WebElement youRenamedConversation;

    private static final String namePendingButton = "PENDING";
    @FindBy(name = namePendingButton)
    private WebElement pendingButton;

    private static final String xpathLastChatMessage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]/*[last()]";
    @FindBy(xpath = xpathLastChatMessage)
    private WebElement lastMessage;

    private static final String xpathStartedConversationMessage = "//UIAStaticText[starts-with(@name, 'YOU STARTED A CONVERSATION WITH')]";
    @FindBy(xpath = xpathStartedConversationMessage)
    private WebElement startedConversationMessage;

    private static final String xpathAddedToConversationMessage = "//UIAStaticText[starts-with(@name, 'YOU ADDED')]";
    @FindBy(xpath = xpathAddedToConversationMessage)
    private WebElement addedToConversationMessage;

    public static final String nameAddPictureButton = "ComposeControllerPictureButton";
    @FindBy(name = nameAddPictureButton)
    private WebElement addPictureButton;

    public static final String nameCallButton = "ComposeControllerVoiceButton";
    @FindBy(name = nameCallButton)
    private WebElement callButton;

    public static final String xpathMessageEntries = "//UIAWindow[@name='ZClientMainWindow']/UIATableView/UIATableCell";
    @FindBy(xpath = xpathMessageEntries)
    private List<WebElement> messageEntries;

    public static final String xpathOtherConversationCellFormat =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]";
    @FindBy(xpath = xpathOtherConversationCellFormat)
    private WebElement imageCell;

    public static final String xpathNameMediaContainer =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]";
    @FindBy(xpath = xpathNameMediaContainer)
    private WebElement mediaContainer;

    public static final String xpathMediaConversationCell =
            "//UIAWindow[@name='ZClientMainWindow']" +
                    "/UIATableView[last()]/UIATableCell[last()]/UIAButton[@name='soundcloud']/following-sibling::UIAButton";
    @FindBy(xpath = xpathMediaConversationCell)
    private WebElement mediaLinkCell;

    public static final String xpathYoutubeVimeoConversationCell =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']"
                    + "/UIATableView[1]/UIATableCell[last()]/UIAButton[1]";
    @FindBy(xpath = xpathYoutubeVimeoConversationCell)
    private WebElement youtubeCell;

    public static final String namePlayButton = "mediaBarPlayButton";
    @FindBy(name = namePlayButton)
    private WebElement mediabarPlayButton;

    public static final String namePauseButton = "mediaBarPauseButton";
    @FindBy(name = namePauseButton)
    private WebElement mediabarPauseButton;

    public static final String xpathConversationPage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]";
    @FindBy(xpath = xpathConversationPage)
    private WebElement conversationPage;

    public static final String nameCloseButton = "mediabarCloseButton";
    @FindBy(name = nameCloseButton)
    private WebElement mediabarStopCloseButton;
    @FindBy(name = nameCloseButton)
    private WebElement closeButton;

    public static final String nameTitle = "playingMediaTitle";
    @FindBy(name = nameTitle)
    private WebElement mediabarBarTitle;

    public static final String namePingButton = "ComposeControllerPingButton";
    @FindBy(name = namePingButton)
    private WebElement pingButton;

    public static final String xpathYouAddedMessageCellFormat =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[1]";
    @FindBy(xpath = xpathYouAddedMessageCellFormat)
    private List<WebElement> youAddedCell;

    public static final String nameAddContactToChatButton = "metaControllerLeftButton";
    @FindBy(name = nameAddContactToChatButton)
    protected WebElement addInfoPage;

    public static final String xpathDialogTitleBar = "//UIAStaticText[@name='%s']";

    public static final String nameSoundCloudPause = "Pause";
    @FindBy(name = nameSoundCloudPause)
    private WebElement soundCloudPause;

    public static final String nameChatheadAvatarImage = "ChatheadAvatarImage";
    @FindBy(name = nameChatheadAvatarImage)
    private WebElement chatheadAvatarImage;

    public static final String nameGifButton = "rightMenuButton";
    @FindBy(name = nameGifButton)
    private WebElement openGifPreviewButton;

    public static final String nameCursorSketchButton = "ComposeControllerSketchButton";
    @FindBy(name = nameCursorSketchButton)
    private WebElement openSketchButton;

    public static final String xpathGiphyImage =
            "//UIATextView[@name='via giphy.com']/following::UIATableCell[@name='ImageCell']";
    @FindBy(xpath = xpathGiphyImage)
    private WebElement giphyImage;

    public static final String nameSoundCloudButton = "soundcloud";
    @FindBy(name = nameSoundCloudButton)
    private WebElement soundCloudButton;

    public static final String xpathUserAvatarNextToInput =
            "//UIAImage[following-sibling::UIATextView[@name='ConversationTextInputField'] and @visible='true']";
    @FindBy(xpath = xpathUserAvatarNextToInput)
    private WebElement userAvatarNextToInput;

    private static final String xpathAllMessages =
            xpathMainWindow + "/UIATableView[1]/UIATableCell/UIATextView";

    private static final Function<String, String> xpathMessagesByText = text ->
            String.format("%s[@value='%s']", xpathAllMessages, text);

    public static final String xpathFormatMissedCallButtonForContact =
            "//UIATableCell[UIAStaticText[@name='%s CALLED']]/UIAButton[@name='ConversationMissedCallButton']";

    public static final String nameOtherUserAddContactToChatButton = "OtherUserMetaControllerLeftButton";

    public static final String xpathLastMessageFormat =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[%s]/UIATextView[1]";

    private String connectingLabel = "CONNECTING TO %s.";

    public static final String xpathLoremIpsumText = "//UIATextView[contains(@name, 'Lorem ipsum')]";

    public static final String nameCameraLibraryButton = "cameraLibraryButton";

    public static final String nameSoundCloudContainer = "Play on SoundCloud";

    public static final String xpathChatheadName = "//UIAElement/following-sibling::UIAStaticText[@name='%s']";

    public static final String xpathChatheadMessage = "//UIAElement/following-sibling::UIAStaticText[@name='%s']";

    public static final String xpathImage =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[2]";

    public static final String xpathSimpleMessageLink =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]/UIATextView[1]";

    public static final String xpathMyNameInDialog = "//UIAStaticText[@name='%s'][last()]";

    public static final String xpathConnectedToUserLabel =
            "//UIAStaticText[contains(@name, 'CONNECTED TO %s')]";

    public DialogPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public String getStartedtChatMessage() {
        return startedConversationMessage.getText();
    }

    public String getAddedtoChatMessage() {
        return startedConversationMessage.getText();
    }

    public boolean isMessageVisible(String msg) throws Exception {

        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.name(msg));
    }

    public boolean isPingButtonVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(this.getDriver(),
                pingButton);
    }

    public void pressPingButton() {
        pingButton.click();
    }

    public ContactListPage returnToContactList() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(),
                conversationBackButton);
        conversationBackButton.click();
        return new ContactListPage(getLazyDriver());
    }

    public StartedCallPage pressCallButton() throws Exception {
        callButton.click();
        return new StartedCallPage(getLazyDriver());
    }

    public int getNumberOfMessageEntries() {
        return messageEntries.size();
    }

    public boolean waitForCursorInputVisible() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.name(nameCloseButton), 2)) {
            closeButton.click();
        }
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.name(nameConversationCursorInput), 10);
    }

    public boolean isCursorInputVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                conversationInput);
    }

    public StartedCallPage clickOnCallButtonForContact(String contact)
            throws Exception {
        this.getDriver()
                .findElement(
                        By.xpath(String
                                .format(xpathFormatMissedCallButtonForContact,
                                        contact.toUpperCase()))).click();
        return new StartedCallPage(getLazyDriver());
    }

    public void tapOnCursorInput() throws Exception {
        try {
            conversationInput.click();
        } catch (NoSuchElementException e) {
            log.debug(this.getDriver().getPageSource());
            throw e;
        }
    }

    public void clearTextInput() {
        conversationInput.clear();
    }

    public String getStringFromInput() throws Exception {
        return conversationInput.getText();
    }

    public String getRenamedMessage() {
        return youRenamedConversation.getText();
    }

    public String getLastMessageFromDialog() {
        return getLastMessage(messagesList);
    }

    public int getMessagesCount() throws Exception {
        return getMessagesCount(null);
    }

    public int getMessagesCount(String expectedMessage) throws Exception {
        By locator;
        if (expectedMessage == null) {
            locator = By.xpath(xpathAllMessages);
        } else {
            locator = By.xpath(xpathMessagesByText.apply(expectedMessage));
        }
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator)) {
            return getDriver().findElements(locator).size();
        }
        return 0;
    }

    public String getExpectedConnectingLabel(String name) {
        return String.format(connectingLabel, name.toUpperCase());
    }

    public void swipeInputCursor() throws Exception {
        DriverUtils.swipeRight(this.getDriver(), conversationInput, 1000);
    }

    public void swipeLeftOptionsButtons() throws Exception {
        int inputMiddle = conversationInput.getLocation().y
                + conversationInput.getSize().height / 2;
        int windowSize = mainWindow.getSize().height;
        int swipeLocation = inputMiddle * 100 / windowSize;
        DriverUtils.swipeLeftCoordinates(getDriver(), 1000, swipeLocation);
    }

    public CameraRollPage pressAddPictureButton() throws Exception {
        CameraRollPage page;
        addPictureButton.click();
        DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.name(nameCameraLibraryButton));
        page = new CameraRollPage(this.getLazyDriver());
        return page;
    }

    public int getNumberOfImages() throws Exception {
        List<WebElement> conversationImages = this.getDriver().findElements(
                By.xpath(xpathOtherConversationCellFormat));
        return conversationImages.size();
    }

    public void startMediaContent() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(
                this.getDriver(), By.xpath(xpathMediaConversationCell), 3)) {
            mediaLinkCell.click();
        } else {
            this.getDriver().tap(1, soundCloudButton.getLocation().x + 200,
                    soundCloudButton.getLocation().y + 200, 1);
        }
    }

    public void scrollDownTilMediaBarAppears() throws Exception {
        int count = 0;
        while ((count < 3) && !isMediaBarDisplayed()) {
            swipeDialogPageDown(2000);
            count++;
        }
    }

    private boolean isMediaBarPauesButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.name(namePauseButton), 3);
    }

    private void clickMediaBarPauseButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), mediabarPauseButton);
        mediabarPauseButton.click();
    }

    public void pauseMediaContent() throws Exception {
        clickMediaBarPauseButton();
    }

    private boolean isMediaBarPlayButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.name(namePlayButton), 3);
    }

    private void clickMediaBarPlayButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), mediabarPlayButton);
        mediabarPlayButton.click();
    }

    public void playMediaContent() throws Exception {
        clickMediaBarPlayButton();
    }

    private void clickMediaBarCloseButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(),
                mediabarStopCloseButton);
        mediabarStopCloseButton.click();
    }

    public void stopMediaContent() throws Exception {
        clickMediaBarCloseButton();
    }

    public String getMediaState() throws Exception {
        if (isMediaBarPlayButtonVisible()) {
            return IOSConstants.MEDIA_STATE_PAUSED;
        } else if (isMediaBarPauesButtonVisible()) {
            return IOSConstants.MEDIA_STATE_PLAYING;
        }
        return IOSConstants.MEDIA_STATE_STOPPED;
    }

    public void tapOnMediaBar() {
        mediabarBarTitle.click();
    }

    private final int TEXT_INPUT_HEIGH = 150;
    private final int TOP_BORDER_WIDTH = 40;

    public IOSPage openConversationDetailsClick() throws Exception {
        if (DriverUtils.isElementPresentAndDisplayed(getDriver(),
                openConversationDetails)) {
            openConversationDetails.click();
        } else {
            for (int i = 0; i < 3; i++) {
                if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                        By.name(namePlusButton))) {
                    plusButton.click();
                    openConversationDetails.click();
                }
                if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                        By.name(nameAddContactToChatButton), 2)
                        || DriverUtils
                        .waitUntilLocatorIsDisplayed(
                                this.getDriver(),
                                By.name(nameOtherUserAddContactToChatButton),
                                2)) {
                    break;
                } else {
                    swipeUp(1000);
                }
            }

        }

        return new OtherUserPersonalInfoPage(this.getLazyDriver());
    }

    public OtherUserOnPendingProfilePage clickConversationDeatailForPendingUser()
            throws Exception {
        plusButton.click();
        openConversationDetails.click();
        return new OtherUserOnPendingProfilePage(this.getLazyDriver());
    }

    @Override
    public void swipeUp(int time) throws Exception {
        WebElement element = this.getDriver().findElement(
                By.name(nameMainWindow));

        Point coords = element.getLocation();
        Dimension elementSize = element.getSize();
        this.getDriver().swipe(coords.x + elementSize.width / 2,
                coords.y + elementSize.height - TEXT_INPUT_HEIGH,
                coords.x + elementSize.width / 2, coords.y + TOP_BORDER_WIDTH,
                time);
    }

    public void swipeDialogPageDown(int time) throws Exception {
        DriverUtils.swipeDown(this.getDriver(), conversationPage, time);
    }

    public OtherUserOnPendingProfilePage swipePendingDialogPageUp(int time)
            throws Throwable {
        WebElement element = this.getDriver().findElement(
                By.name(nameMainWindow));

        Point coords = element.getLocation();
        Dimension elementSize = element.getSize();
        this.getDriver().swipe(coords.x + elementSize.width / 2,
                coords.y + elementSize.height - TEXT_INPUT_HEIGH,
                coords.x + elementSize.width / 2, coords.y + TOP_BORDER_WIDTH,
                time);
        return new OtherUserOnPendingProfilePage(this.getLazyDriver());
    }

    public boolean isYoutubeContainerVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpathYoutubeVimeoConversationCell), 10);
    }

    public boolean isMediaContainerVisible() throws Exception {
        boolean isVisible = DriverUtils.waitUntilLocatorAppears(
                this.getDriver(),
                By.xpath(xpathMediaConversationCell), 10);
        if (!isVisible) {
            rotateDeviceToRefreshElementsTree();
        }
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpathMediaConversationCell), 10);
    }

    public VideoPlayerPage clickOnVideoContainerFirstTime() throws Exception {
        VideoPlayerPage page = new VideoPlayerPage(this.getLazyDriver());
        youtubeCell.click();

        return page;
    }

    public String getConnectMessageLabel() {
        return connectMessageLabel.getText();
    }

    private String getLastMessage(List<WebElement> chatList) {
        String lastMessage;
        if (chatList.size() > 0) {
            try {
                String lastMessageXPath = String.format(
                        xpathLastMessageFormat, chatList.size());
                WebElement el = this.getDriver().findElementByXPath(
                        lastMessageXPath);
                lastMessage = el.getText();
            } catch (Exception e) {
                lastMessage = "Last message is image";
            }
        } else {
            lastMessage = "Empty chat";
        }
        return lastMessage;
    }

    public long getSendTime() {
        long currentTime;
        Date date = new Date();
        currentTime = date.getTime();
        return currentTime;
    }

    public boolean isMediaBarDisplayed() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                mediabarBarTitle);
    }

    public boolean waitMediabarClose() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.name(nameTitle));
    }

    public void tapImageToOpen() throws Exception {
        imageCell.click();
    }

    public void tapHoldTextInput() throws Exception {
        try {
            cmdVscript(scriptArr);
        } catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.getDriver().tap(1, this.getDriver().findElementByName(nameConversationCursorInput), 1000);
    }

    public DialogPage scrollToBeginningOfConversation() throws Exception {
        DialogPage page = null;
        int count = 0;
        if (youAddedCell.size() > 0) {
            boolean beginningConversation = youAddedCell.get(0).isDisplayed();
            while (!(beginningConversation) & (count < 5)) {
                DriverUtils.swipeDown(this.getDriver(), conversationPage,
                        500);
                page = this;
                beginningConversation = youAddedCell.get(0).isDisplayed();
                count++;
            }
        }
        Assert.assertTrue(youAddedCell.get(0).isDisplayed());
        return page;
    }

    private static final int IMAGE_IN_CONVERSATION_HEIGHT = 510;
    private static final int IMAGE_IN_IPAD_CONVERSATION_HEIGHT = 1020;

    public BufferedImage takeImageScreenshot() throws Exception {
        BufferedImage image;

        image = getElementScreenshot(imageCell).orElseThrow(
                IllegalStateException::new);

        String deviceType = CommonUtils.getDeviceName(this.getClass());

        if (deviceType.equals("iPhone 6")) {

            image = image.getSubimage(0, image.getHeight()
                            - IMAGE_IN_CONVERSATION_HEIGHT, image.getWidth(),
                    IMAGE_IN_CONVERSATION_HEIGHT);

        } else {

            image = image.getSubimage(0, image.getHeight()
                            - IMAGE_IN_IPAD_CONVERSATION_HEIGHT, image.getWidth(),
                    IMAGE_IN_IPAD_CONVERSATION_HEIGHT);
        }

        return image;
    }

    public double isLastImageSameAsTemplate(String filename) throws Throwable {

        BufferedImage templateImage = takeImageScreenshot();
        BufferedImage referenceImage = ImageUtil.readImageFromFile(IOSPage
                .getImagesPath() + filename);

        double score = ImageUtil.getOverlapScore(referenceImage, templateImage,
                ImageUtil.RESIZE_TEMPLATE_TO_RESOLUTION);

        log.debug("SCORE: " + score);

        return score;

    }

    public void typeAndSendConversationMessage(String message) throws Exception {
        typeConversationMessage(message);
        clickKeyboardReturnButton();
    }

    public void typeConversationMessage(String message) throws Exception {
        assert waitForCursorInputVisible() : "Conversation input is not visible after the timeout";
        conversationInput.click();
        try {
            ((IOSElement) getDriver().findElementByName(nameConversationCursorInput)).
                    setValue(message);
        } catch (WebDriverException e) {
            // Ignore silently
        }
    }

    public void waitLoremIpsumText() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.xpath(xpathLoremIpsumText), 10);
    }

    public void waitSoundCloudLoad() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.name(nameSoundCloudContainer));
    }

    public double checkPingIcon(String label) throws Exception {
        String path = null;
        BufferedImage pingImage = null;
        ScreenOrientation orient = getOrientation();
        if (label.equals(PING_LABEL)) {
            pingImage = getPingIconScreenShot();
            path = CommonUtils.getPingIconPathIOS(GroupChatPage.class);
            if (orient == ScreenOrientation.LANDSCAPE) {
                path = path.replace(".png", "_landscape.png");
            }
        } else if (label.equals(HOT_PING_LABEL)) {
            pingImage = getPingAgainIconScreenShot();
            path = CommonUtils.getHotPingIconPathIOS(GroupChatPage.class);
            if (orient == ScreenOrientation.LANDSCAPE) {
                path = path.replace(".png", "_landscape.png");
            }
        }
        BufferedImage templateImage = ImageUtil.readImageFromFile(path);
        return ImageUtil.getOverlapScore(pingImage, templateImage);
    }

    private static final int PING_ICON_WIDTH = 72;
    private static final int PING_ICON_HEIGHT = 60;
    private static final int PING_ICON_Y_OFFSET = 7;

    private BufferedImage getPingIconScreenShot() throws Exception {
        Point elementLocation = pinged.getLocation();
        Dimension elementSize = pinged.getSize();
        int x = elementLocation.x * 2 + elementSize.width * 2;
        int y = (elementLocation.y - PING_ICON_Y_OFFSET) * 2;
        int w = PING_ICON_WIDTH;
        int h = PING_ICON_HEIGHT;
        return getScreenshotByCoordinates(x, y, w, h).orElseThrow(
                IllegalStateException::new);
    }

    private BufferedImage getPingAgainIconScreenShot() throws Exception {
        Point elementLocation = pingedAgain.getLocation();
        Dimension elementSize = pingedAgain.getSize();
        int x = elementLocation.x * 2 + elementSize.width * 2;
        int y = (elementLocation.y - PING_ICON_Y_OFFSET) * 2;
        int w = PING_ICON_WIDTH;
        int h = PING_ICON_HEIGHT;
        return getScreenshotByCoordinates(x, y, w, h).orElseThrow(
                IllegalStateException::new);
    }

    public void waitPingAnimation() throws InterruptedException {
        Thread.sleep(PING_ANIMATION_TIME);
    }

    public void scrollToEndOfConversation() throws Exception {
        WebElement el = this.getDriver().findElement(By.xpath(xpathLastChatMessage));
        try {
            this.getDriver().scrollToExact(el.getText());
        } catch (WebDriverException e) {
            // Simply ignore
        }
    }

    public boolean isTitleBarDisplayed(String name) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(String.format(xpathDialogTitleBar, name)));
    }

    public boolean isTypeOrSlideExists(String msg) throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(msg), 5);
    }

    public boolean chatheadIsVisible(String contact) throws Exception {

        List<WebElement> el = this.getDriver()
                .findElements(
                        By.xpath(String.format(xpathChatheadName, contact)));
        for (WebElement element : el) {
            if (DriverUtils.isElementPresentAndDisplayed(getDriver(), element)) {
                return true;
            }
        }
        return false;
    }

    public boolean chatheadAvatarImageIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(nameChatheadAvatarImage));
    }

    public void clickOnPlayVideoButton() throws Exception {
        youtubeCell.click();
    }

    public void openGifPreviewPage() {
        openGifPreviewButton.click();
    }

    public void openSketch() {
        openSketchButton.click();
    }

    public boolean isMyNameInDialogDisplayed(String name) throws Exception {
        WebElement el = getDriver().findElementByXPath(
                String.format(xpathMyNameInDialog, name.toUpperCase()));
        return DriverUtils.isElementPresentAndDisplayed(getDriver(), el);
    }

    public boolean isConnectedToUserStartedConversationLabelVisible(
            String username) throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(String
                .format(xpathConnectedToUserLabel, username.toUpperCase())), 5);
    }

    /**
     * Navigates back by swipe and initialize ContactListPage
     *
     * @throws Exception
     */
    public ContactListPage navigateBack(int timeMilliseconds) throws Exception {
        swipeRight(timeMilliseconds,
                DriverUtils.SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL, 30);
        return new ContactListPage(this.getLazyDriver());
    }

    public void clickPlusButton() {
        plusButton.click();
    }

    public boolean isPlusButtonVisible() throws Exception {
        return DriverUtils
                .isElementPresentAndDisplayed(getDriver(), plusButton);
    }

    public boolean waitPlusButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.name(namePlusButton));
    }

    public boolean isOpenConversationDetailsButtonVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                openConversationDetails);
    }

    public boolean isCallButtonVisible() throws Exception {
        return DriverUtils
                .isElementPresentAndDisplayed(getDriver(), callButton);
    }

    public boolean isCameraButtonVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                addPictureButton);
    }

    public boolean isOpenScetchButtonVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                openSketchButton);
    }

    public boolean isCloseButtonVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                closeButton);
    }

    public void clickCloseButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), closeButton);
        closeButton.click();
    }

    public boolean isGiphyImageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathGiphyImage));
    }

    public void tapOnLink() throws Exception {
        WebElement tapLink = this.getDriver().findElementByXPath(xpathSimpleMessageLink);
        DriverUtils.tapByCoordinates(getDriver(), tapLink);
    }

    public void tapOnLinkWithinAMessage() throws Exception {
        WebElement tapLink = this.getDriver().findElementByXPath(xpathSimpleMessageLink);
        DriverUtils.tapByCoordinates(getDriver(), tapLink,
                -(tapLink.getSize().width / 4), 0);
    }

    public boolean isTherePossibilityControllerButtonsToBeDisplayed() {
        int pingX = pingButton.getLocation().x;
        int conversationX = conversationWindow.getLocation().x;
        return pingX > conversationX;
    }

    public void tapHoldImage() {
        try {
            this.getDriver().tap(1, this.getDriver().findElementByXPath(xpathImage), 1000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isUserAvatarNextToInputVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                userAvatarNextToInput);
    }

}
