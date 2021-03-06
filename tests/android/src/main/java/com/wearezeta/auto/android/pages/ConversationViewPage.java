package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.details_overlay.BaseUserDetailsOverlay;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.common.misc.Timedelta;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;

import javax.ws.rs.NotSupportedException;

public class ConversationViewPage extends BaseUserDetailsOverlay {

    public static final By idConversationRoot = By.id("messages_list_view");

    //region Conversation Row Locators
    // Text
    private static final String idStrRowConversationMessage = "ltv__row_conversation__message";
    private static final Function<String, String> xpathStrConversationMessageByText = text -> String
            .format("//*[@id='%s' and @value='%s']", idStrRowConversationMessage, text);
    private static final By xpathLastConversationMessage =
            By.xpath(String.format("(//*[@id='%s'])[last()]", idStrRowConversationMessage));
    private static final By xpathFirstConversationMessage =
            By.xpath(String.format("(//*[@id='%s'])[1]", idStrRowConversationMessage));

    // Image
    private static final String idStrConversationImages = "message_image";
    public static final By idConversationImageContainer = By.id(idStrConversationImages);
    private static final By idClickedImageSendingIndicator = By.id("v__row_conversation__pending");

    // Missed call message
    private static final String idStrMissedCallMesage = "tvMessage";
    private static final Function<String, String> xpathStrMissedCallMesageByText = text -> String
            .format("//*[@id='%s' and @value='%s']", idStrMissedCallMesage, text.toUpperCase());

    //region System message
    private static final String strIdSystemMessage = "ttv__system_message__text";
    private static final By xpathOtrVerifiedMessage = By
            .xpath(String.format("//*[@id='%s' and @value='ALL FINGERPRINTS ARE VERIFIED']", strIdSystemMessage));

    private static final By xpathSystemMessageNewDevice = By
            .xpath(String.format("//*[@id='%s' and contains(@value,'STARTED USING A NEW DEVICE')]", strIdSystemMessage));

    private static final Function<String, String> xpathStrSystemMessageNewDeviceByValue = value -> String.format(
            "//*[@id='%s' and @value='%s STARTED USING A NEW DEVICE']", strIdSystemMessage, value.toUpperCase());

    private static final Function<String, String> xpathStrSystemMessageByExp = exp -> String
            .format("//*[@id='%s' and %s]", strIdSystemMessage, exp);
    //endregion

    // Ping
    private static final String strIdPingMessage = "ttv__row_conversation__ping_message";
    public static final Function<String, String> xpathStrPingMessageByText = text -> String
            .format("//*[@id='%s' and @value='%s']", strIdPingMessage, text.toUpperCase());
    public static final Function<Integer, String> xpathPingMessageByIndex = index -> String
            .format("(//*[@id='%s'])[%d]", strIdPingMessage, index);
    private static final By xpathLastPingMessage =
            By.xpath(String.format("(//*[@id='%s'])[last()]", strIdPingMessage));

    //endregion

    //region Conversation Cursor locators
    private static final String strIdCusorSendButtonContainer = "fl__cursor__send_button_container";
    public static final By idCursorSendButtonContainer = By.id(strIdCusorSendButtonContainer);
    public static final By xpathCursorSendButton = By.xpath(
            String.format("//*[@id='%s']/*[2]", strIdCusorSendButtonContainer));
    public static final By xpathCursorEphemeralButton = By.xpath(
            String.format("//*[@id='%s']/*[3]", strIdCusorSendButtonContainer));
    private static final By idCursorInputEmojiButton = By.id("fl__cursor__emoji_container");
    private static final By idCursorCamera = By.id("cursor_menu_item_camera");
    private static final By idCursorPing = By.id("cursor_menu_item_ping");
    private static final By idCursorMore = By.id("cursor_menu_item_more");
    private static final By idCursorLess = By.id("cursor_menu_item_less");
    private static final By idCursorVideoMessage = By.id("cursor_menu_item_video");
    private static final By idCursorShareLocation = By.id("cursor_menu_item_location");
    private static final By idCursorGif = By.id("cursor_menu_item_gif");
    private static final By idCursorView = By.id("cal__cursor");
    public static final String idStrCursorEditText = "cet__cursor";
    private static final String CURSOR_EDIT_TOOLTIP = "TYPE A MESSAGE";
    private static final By xpathCursorEditHint = By.xpath(
            String.format("//*[@id='ttv__cursor_hint' and contains(@value, '%s')]", CURSOR_EDIT_TOOLTIP));
    public static final By idCursorEditText = By.id(idStrCursorEditText);
    public static Function<String, String> xpathCursorEditTextByValue = value ->
            String.format("//*[@id='%s' and contains(@value, '%s')]", idStrCursorEditText, value);
    public static Function<String, String> xpathCursorTypingIndicatorByContainsQuery = query ->
            String.format("//*[@id='ttv__typing_indicator_names'%s]", query);
    //endregion

    //region Message footer
    private static final FunctionalInterfaces.FunctionFor2Parameters<String, String, String> xpathStrTemplateIdValue
            = (id, value) -> String.format("//*[@id='%s' and contains(@value,'%s')]", id, value);

    private static final String strIdMessageMetaLikeButton = "like__button";
    private static final String strIdMessageMetaLikeDescription = "like__description";
    private static final String strIdMessageMetaStatus = "timestamp_and_status";
    private static final String strIdMessageMetaFirstLike = "like_chathead_container";
    private static final String strIdMessageMetaSecondLike = "like_chathead_container";

    private static final By idResendButton = By.xpath(
            String.format("//*[@id='%s' and @value='Sending failed. Resend']", strIdMessageMetaStatus));
    //endregion

    //region Message Bottom Menu
    private static final By idMessageBottomMenu = By.id("action_bar_root");
    private static final By idMessageBottomMenuForwardButton = By.id("message_bottom_menu_item_forward");
    private static final By idMessageBottomMenuDeleteGeneralButton = By.id("message_bottom_menu_item_delete");
    private static final By idMessageBottomMenuDeleteLocalButton = By.id("message_bottom_menu_item_delete_local");
    private static final By idMessageBottomMenuDeleteGlobalButton = By.id("message_bottom_menu_item_delete_global");
    private static final By idMessageBottomMenuCopyButton = By.id("message_bottom_menu_item_copy");
    private static final By idMessageBottomMenuEditButton = By.id("message_bottom_menu_item_edit");
    private static final By idMessageBottomMenuLikeButton = By.id("message_bottom_menu_item_like");
    private static final By idMessageBottomMenuUnlikeButton = By.id("message_bottom_menu_item_unlike");
    private static final By idMessageBottomMenuOpenButton = By.id("message_bottom_menu_item_open_file");
    //endregion

    private static final By idFullScreenImage = By.id("tiv__single_image_message__image");

    public static final By idVerifiedConversationShield = By.id("cursor_button_giphy");

    private static final By idPlayPauseMedia = By.id("gtv__media_play");

    private static final By idYoutubePlayButton = By.id("gtv__youtube_message__play");

    private static final String strIdMediaToolbar = "tb__conversation_header__mediabar";

    private static final By idMediaBarPlayBtn = By.xpath(String.format("//*[@id='%s']/*[2]", strIdMediaToolbar));

    private static final By idMediaToolbar = By.id(strIdMediaToolbar);

    private static final By idCursorSketch = By.id("cursor_menu_item_draw");

    //region Top toolbar button
    private static final By idAudioCall = By.id("action_audio_call");

    private static final By idVideoCall = By.id("action_video_call");

    private static final By idCollectionsButton = By.id("action_collection");
    //endregion

    private static final By idCursorFile = By.id("cursor_menu_item_file");

    private static final By idCursorAudioMessage = By.id("cursor_menu_item_audio_message");

    private static final By idAudioMessageRecordingSileControl = By.id("fl__audio_message__recording__slide_control");

    private static final By idAudioRecordingSendButton = By.id("fl__audio_message__recording__send_button_container");

    private static final By idAudioRecordingPlayButton = By.id("fl__audio_message__recording__bottom_button_container");

    private static final By idAudioRecordingCancelButton = By.id("fl__audio_message__recording__cancel_button_container");

    private static final By xpathAudioMessageDurationText =
            By.xpath("//*[@id='ttv__audio_message__recording__duration' and not(text())]");

    //region File transfer
    private static final By idFileActionBtn = By.id("action_button");

    private static final By idFileTransferContainer = By.id("message_file_asset");

    private static final By idFileTransferPlaceholder = By.id("pdv__row_conversation__file_placeholder_dots");

    private static final Function<String, String> xpathFileNamePlaceHolderByValue = value -> String
            .format("//*[@id='file_name' and @value='%s']", value);

    private static final Function<String, String> xpathFileInfoPlaceHolderByValue = value -> String
            .format("//*[@id='file_info' and @value='%s']", value);

    private static final By idFileDialogActionOpenBtn = By.id("ttv__file_action_dialog__open");

    private static final By idFileDialogActionSaveBtn = By.id("ttv__file_action_dialog__save");
    //endregion

    private static final String xpathStrConversationToolbar = "//*[@id='t_conversation_toolbar']";

    private static final By xpathToolbar = By.xpath(xpathStrConversationToolbar);

    private static final By xpathToolBarNavigation =
            By.xpath(String.format("%s/*[@value='' and count(*)=1]", xpathStrConversationToolbar));

    public static final By idCursorCloseButton = By.id("cursor_button_close");

    private static final String idStrNewConversationNameMessage = "ttv__new_conversation_name";

    private static Function<String, String> xpathStrNewConversationNameByValue = value -> String
            .format("//*[@id='%s' and @value='%s']", idStrNewConversationNameMessage, value);

    private static final String strIdUserName = "tv__conversation_toolbar__title";
    private static final Function<String, String> xpathMessageNotificationByValue = value -> String
            .format("//*[starts-with(@id,'ttv_message_notification_chathead__label') and @value='%s']", value);

    private static final Function<String, String> xpathLinkPreviewUrlByValue = value -> String
            .format("//*[@id='ttv__row_conversation__link_preview__url' and @value='%s']", value);

    private static final String idStrSeparatorName = "tvName";

    private static final Function<String, String> xpathMessageStateGlyphByName = name -> String
            .format("//*[@id='%s' and @value='%s']/following-sibling::*[@id='gtvStateGlyph']",
                    idStrSeparatorName, name.toLowerCase());

    private static final Function<String, String> xpathMessageSeparator = name -> String
            .format("//*[@id='%s' and @value='%s']", idStrSeparatorName, name.toLowerCase());

    private static final By idYoutubeContainer = By.id("message_youtube");

    private static final By idSoundcloudContainer = By.id("mpv__row_conversation__message_media_player");

    private static final By idVideoMessageContainer = By.id("message_video_asset");

    private static final By idVideoContainerButton = By.id("action_button");

    private static final By idAudioMessageContainer = By.id("message_audio_asset");

    private static final By idAudioContainerButton = By.id("action_button");

    private static final By idShareLocationContainer = By.id("message_location");

    private static final By idLinkPreviewContainer = By.id("cv__row_conversation__link_preview__container");

    private static final By idAudioContainerSeekbar = By.id("progress");

    private static final By idAudioMessagePreviewSeekbar = By.id("sb__voice_message__recording__seekbar");

    private static final By idImageContainerSketchButton = By.id("button_sketch");

    private static final By idImageContainerFullScreenButton = By.id("button_fullscreen");

    private static final By idImageContainerSketchEmojiButton = By.id("button_emoji");

    private static final By idImageContainerSketchTextButton = By.id("button_text");

    private static final int MAX_CLICK_RETRIES = 5;

    private static final double LOCATION_DIFFERENCE_BETWEEN_TOP_TOOLBAR_AND_MEDIA_BAR = 0.01;

    private static final String FILE_UPLOADING_MESSAGE = "UPLOADING…";

    private static final String FILE_DOWNLOADING_MESSAGE = "DOWNLOADING…";

    private static final String FILE_UPLOAD_FAILED = "UPLOAD FAILED";

    private static final String FILE_MESSAGE_SEPARATOR = " · ";

    private static final Timedelta SCROLL_TO_BOTTOM_INTERVAL = Timedelta.fromMilliSeconds(1000);

    private static final int DEFAULT_SWIPE_DURATION_MILLISECONDS = 40;

    private static final int CONTAINER_VISIBILITY_TIMEOUT_SECONDS = 20;

    private static final int VISIBILITY_TIMEOUT_SECONDS = 30;

    private static final int JENKINS_STABILITY_TIMEOUT_SECONDS = 15;

    public enum MessageIndexLocator {
        FIRST(xpathFirstConversationMessage),
        LAST(xpathLastConversationMessage);

        private final By locator;

        MessageIndexLocator(By locator) {
            this.locator = locator;
        }

        public By getLocator() {
            return this.locator;
        }
    }

    public ConversationViewPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected String getUserNameId() {
        return strIdUserName;
    }

    @Override
    protected By getAvatarLocator() {
        throw new NotSupportedException("Getting the avatar is not supported");
    }

    // region Screeshot buffer
    public BufferedImage getShieldStateScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idVerifiedConversationShield)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of verification shield")
        );
    }

    public BufferedImage getMediaButtonState() throws Exception {
        return this.getElementScreenshot(getElement(idPlayPauseMedia)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of Play/Pause button")
        );
    }

    public BufferedImage getTopToolbarState() throws Exception {
        return this.getElementScreenshot(getElement(xpathToolbar)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of upper toolbar")
        );
    }

    public BufferedImage getFilePlaceholderActionButtonState() throws Exception {
        return this.getElementScreenshot(getElement(idFileActionBtn)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of file place holder action button")
        );
    }

    public BufferedImage getAudioMessageSeekbarState() throws Exception {
        return this.getElementScreenshot(getElement(idAudioContainerSeekbar)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of seekbar within audio message container")
        );
    }

    public BufferedImage getAudioMessagePreviewSeekbarState() throws Exception {
        return this.getElementScreenshot(getElement(idAudioMessagePreviewSeekbar)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of seekbar within audio message preview ")
        );
    }

    public BufferedImage getAudioMessagePreviewMicrophoneButtonState() throws Exception {
        return this.getElementScreenshot(getElement(idAudioRecordingPlayButton)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of Audio message recording slide microphone button")
        );
    }

    public BufferedImage getMessageContainerState(String containerType) throws Exception {
        By locator = getContainerLocatorByName(containerType);
        return this.getElementScreenshot(getElement(locator)).orElseThrow(
                () -> new IllegalStateException(
                        String.format("Cannot get a screenshot of message container '%s'", containerType))
        );
    }
    //endregion

    //region Cursor
    public void tapOnCursorToolButton(String tapType, String btnName, String longTapDurationSeconds, String shouldReleaseFinger) throws Exception {
        switch (tapType) {
            case "tap":
                simpleTapCursorToolButton(btnName);
                break;
            case "long tap":
                int longTapDuration = (longTapDurationSeconds == null) ? DriverUtils.LONG_TAP_DURATION :
                        Integer.parseInt(longTapDurationSeconds.replaceAll("[\\D]", "")) * 1000;

                if (btnName.toLowerCase().equals("audio message")) {
                    if (shouldReleaseFinger == null) {
                        longTapAudioMessageCursorBtn(longTapDuration);
                    } else {
                        longTapAndKeepAudioMessageCursorBtn();
                    }
                } else {
                    throw new IllegalStateException(String.format("Unknow button name '%s' for long tap", btnName));

                }
                break;
            case "double tap":
                if (btnName.toLowerCase().equals("ephemeral")) {
                    doubleTapOnEphemeralButton();
                } else {
                    throw new IllegalStateException(String.format("Unknow button name '%s' for double tap", btnName));
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify tap type '%s'", tapType));
        }
    }

    public boolean isCursorViewVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorView);
    }

    public boolean isTextInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorEditText);
    }

    public boolean isTextInputInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idCursorEditText);
    }

    public boolean isTooltipOfTextInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathCursorEditHint);
    }

    public boolean isTooltipOfTextInputInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathCursorEditHint);
    }

    public void tapOnTextInput() throws Exception {
        getElement(idCursorEditText).click();
    }

    public void closeInputOptions() throws Exception {
        getElement(idCursorCloseButton, "Close cursor button is not visible").click();
    }

    public void typeAndSendMessage(String message, String sendFrom, boolean hideKeyboard) throws Exception {
        final WebElement cursorInput = getElement(idCursorEditText);
        final int maxTries = 5;
        int ntry = 0;
        do {
            cursorInput.clear();
            cursorInput.sendKeys(message);
            ntry++;
        } while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathCursorEditTextByValue.apply(message)), 2) && ntry < maxTries);
        if (ntry >= maxTries) {
            throw new IllegalStateException(String.format(
                    "The string '%s' was autocorrected. Please disable autocorrection on the device and restart the " +
                            "test.",
                    message));
        }

        switch (sendFrom.toLowerCase()) {
            case "keyboard":
                pressKeyboardSendButton();
                break;
            case "cursor":
                getElement(idCursorSendButtonContainer).click();
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the send button type '%s'", sendFrom));
        }
        if (hideKeyboard) {
            this.hideKeyboard();
        }
    }

    public void typeMessage(String message) throws Exception {
        getElement(idCursorEditText).sendKeys(message);
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathCursorEditTextByValue.apply(message)),
                2)) {
            log.warn(String.format("The message '%s' was autocorrected. This might cause unpredicted test results",
                    message));
        }
    }

    public void clearMessageInCursorInput() throws Exception {
        getElement(idCursorEditText).clear();
    }

    public boolean waitUntilCursorInputTextVisible(String text) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathCursorEditTextByValue.apply(text)));
    }

    public boolean waitUntilTypingVisible(String names) throws Exception {
        String[] nameList = names.split(",");
        StringBuilder buffer = new StringBuilder();
        for (String name : nameList) {
            buffer.append(String.format(" and contains(@value,'%s')", name.toLowerCase().trim()));
        }
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathCursorTypingIndicatorByContainsQuery.apply(buffer.toString())));
    }

    private By getCursorToolButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "switch to text":
            case "switch to emoji":
                return idCursorInputEmojiButton;
            case "ephemeral":
            case "send":
                return idCursorSendButtonContainer;
            case "ping":
                return idCursorPing;
            case "add picture":
                return idCursorCamera;
            case "sketch":
                return idCursorSketch;
            case "file":
                return idCursorFile;
            case "audio message":
                return idCursorAudioMessage;
            case "video message":
                return idCursorVideoMessage;
            case "share location":
                return idCursorShareLocation;
            case "gif":
                return idCursorGif;
            default:
                throw new IllegalArgumentException(String.format("Unknown tool button name '%s'", name));
        }
    }

    private WebElement showCursorToolButtonIfNotVisible(By locator) throws Exception {
        final Optional<WebElement> btn = getElementIfDisplayed(locator, Timedelta.fromSeconds(3));
        if (btn.isPresent()) {
            return btn.get();
        } else {
            getElementIfDisplayed(idCursorMore, Timedelta.fromSeconds(3)).orElseGet(() ->
                    {
                        try {
                            return getElement(idCursorLess);
                        } catch (Exception e) {
                            throw new IllegalStateException("Cannot find element with Cursor less button");
                        }
                    }
            ).click();
            // Wait for animation
            Thread.sleep(1000);
            return getElement(locator);
        }
    }

    private WebElement showCursorToolButtonIfNotVisible(String name) throws Exception {
        final By locator = getCursorToolButtonLocatorByName(name);
        return showCursorToolButtonIfNotVisible(locator);
    }

    public void simpleTapCursorToolButton(String name) throws Exception {
        showCursorToolButtonIfNotVisible(name).click();
    }

    private By getCursorInputButtonLocator(String buttonType) {
        switch (buttonType.toLowerCase()) {
            case "send":
                return xpathCursorSendButton;
            case "ephemeral":
                return xpathCursorEphemeralButton;
            default:
                throw new IllegalStateException(String.format("Cannot identify cursor input button type '%s'",
                        buttonType));
        }
    }

    public void doubleTapOnEphemeralButton() throws Exception {
        getDriver().doubleTap(getElement(xpathCursorEphemeralButton));
    }

    public boolean waitUntilCursorInputButtonVisible(String buttonType) throws Exception {
        By locator = getCursorInputButtonLocator(buttonType);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilCursorInputButtonInvisible(String buttonType) throws Exception {
        By locator = getCursorInputButtonLocator(buttonType);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void longTapAudioMessageCursorBtn(int durationMillis) throws Exception {
        getDriver().longTap(showCursorToolButtonIfNotVisible(idCursorAudioMessage), durationMillis);
    }

    public void longTapAudioMessageCursorBtnAndSwipeUp(int durationMillis) throws Exception {
        longTapAndSwipe(showCursorToolButtonIfNotVisible(idCursorAudioMessage),
                () -> getElement(idAudioRecordingSendButton), DEFAULT_SWIPE_DURATION_MILLISECONDS, durationMillis);
    }

    public void longTapAudioMessageCursorBtnAndRememberIcon(int durationMillis, ElementState elementState)
            throws Exception {
        longTapAndSwipe(getElement(idCursorAudioMessage), () -> getElement(idCursorAudioMessage),
                DEFAULT_SWIPE_DURATION_MILLISECONDS, durationMillis, Optional.of(elementState::remember));
    }

    public boolean waitUntilAudioMessageRecordingSlideVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idAudioMessageRecordingSileControl);
    }

    public boolean waitUntilAudioMessageRecordingSlideInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idAudioMessageRecordingSileControl);
    }

    public boolean waitUntilAudioRecordingButtonVisible(String name) throws Exception {
        final By locator = getAudioRecordingButtonLocator(name);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilAudioRecordingButtonInvisible(String name) throws Exception {
        final By locator = getAudioRecordingButtonLocator(name);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isAudioMessageRecordingDurationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathAudioMessageDurationText);
    }

    private By getAudioRecordingButtonLocator(String btnName) {
        switch (btnName.toLowerCase()) {
            case "send":
                return idAudioRecordingSendButton;
            case "cancel":
                return idAudioRecordingCancelButton;
            case "play":
                return idAudioRecordingPlayButton;
            default:
                throw new IllegalStateException(String.format("Cannot identify audio recording button '%s'", btnName));
        }
    }

    public void tapAudioRecordingButton(String name) throws Exception {
        final By locator = getAudioRecordingButtonLocator(name);
        if (locator.equals(idAudioRecordingSendButton)) {
            getDriver().longTap(getElement(idAudioRecordingSendButton), DriverUtils.SINGLE_TAP_DURATION);
        } else {
            getElement(locator).click();
        }
    }

    //endregion

    //region Text Message
    public boolean waitUntilMessageWithTextVisible(String text) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(text));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public boolean waitUntilMessageWithTextInvisible(String msg) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(msg));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilAnyMessageInvisible() throws Exception {
        final By locator = By.id(idStrRowConversationMessage);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitForXMessages(String msg, int times) throws Exception {
        By locator = By.xpath(xpathStrConversationMessageByText.apply(msg));
        if (times > 0) {
            DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
        }
        return getElements(locator).stream().collect(Collectors.toList()).size() == times;
    }
    //endregion

    //region Ping Message
    public boolean waitUntilPingMessageWithTextVisible(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrPingMessageByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilPingMessageWithTextInvisible(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrPingMessageByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isCountOfPingsEqualTo(int expectedNumberOfPings) throws Exception {
        assert expectedNumberOfPings > 0 : "The expected number of pings should be greater than zero";
        By locator = By.xpath(xpathPingMessageByIndex.apply(expectedNumberOfPings));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
    //endregion

    /**
     * It based on the cursor action , scroll to the bottom of view when you tap on input text field and focus on it
     *
     * @throws Exception
     */
    public void scrollToTheBottom() throws Exception {
        tapOnTextInput();
        this.hideKeyboard();
        swipeByCoordinates(SCROLL_TO_BOTTOM_INTERVAL, 50, 75, 50, 40);
    }

    private By getTopBarButtonLocator(String btnName) {
        switch (btnName.toLowerCase()) {
            case "audio call":
                return idAudioCall;
            case "video call":
                return idVideoCall;
            case "collections":
                return idCollectionsButton;
            case "back":
                return xpathToolBarNavigation;
            default:
                throw new IllegalArgumentException(String.format("Unknown top bar button name '%s'", btnName));
        }
    }

    public void tapTopBarButton(String name) throws Exception {
        final By locator = getTopBarButtonLocator(name);
        getElement(locator).click();
    }

    public void tapTopToolbarTitle() throws Exception {
        getElement(xpathToolbar, "Top toolbar title is not visible").click();
    }

    public boolean waitForConversationNameChangedMessage(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrNewConversationNameByValue.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForOtrVerifiedMessage() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOtrVerifiedMessage);
    }

    public boolean waitForOtrNonVerifiedMessage() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSystemMessageNewDevice);
    }

    public boolean waitForOtrNonVerifiedMessageCausedByUser(String userName) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathStrSystemMessageNewDeviceByValue.apply(userName)));
    }

    public boolean waitUntilSystemMessageVisible(String text) throws Exception {
        final By locator = By.xpath(xpathStrSystemMessageByExp.apply(String.format("contains(@value, '%s')", text.toUpperCase())));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilImageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), idConversationImageContainer) &&
                DriverUtils.waitUntilLocatorDissapears(getDriver(), idClickedImageSendingIndicator, 20);
    }

    /**
     * Navigates back by swipe and initialize ConversationsListPage
     *
     * @throws Exception
     */
    public void navigateBack(Timedelta duration) throws Exception {
        swipeRightCoordinates(duration);
    }

    public boolean isConversationPeopleChangedMessageContainsNames(List<String> names) throws Exception {
        final String xpathExpr = String.join(" and ", names.stream().map(
                name -> String.format("contains(@value, '%s')", name.toUpperCase())
        ).collect(Collectors.toList()));
        final By locator = By.xpath(xpathStrSystemMessageByExp.apply(xpathExpr));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isTopToolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathToolbar);
    }

    public boolean isAudioCallIconInToptoolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idAudioCall);
    }

    public boolean isAudioCallIconInToptoolbarInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idAudioCall);
    }

    public boolean isVideoCallIconInToptoolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idVideoCall);
    }

    public boolean isVideoCallIconInToptoolbarInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idVideoCall);
    }

    public boolean isConversationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idConversationRoot);
    }

    private static final double MAX_BUTTON_STATE_OVERLAP = 0.5;

    public void tapPlayPauseBtn() throws Exception {
        // TODO: Check whether swipe on SoundCloud, it will trigger long tap action?
        final WebElement playPauseBtn = getElement(idPlayPauseMedia, "Play/Pause button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), playPauseBtn)) {
            throw new IllegalStateException("Play/Pause button is not clickable");
        }
        final BufferedImage initialState = getElementScreenshot(playPauseBtn)
                .orElseThrow(() -> new IllegalStateException("Failed to get a screenshot of Play/Pause button"));
        playPauseBtn.click();
        Thread.sleep(2000);
        int clickTry = 1;
        do {
            final BufferedImage currentState = getElementScreenshot(playPauseBtn)
                    .orElseThrow(() -> new AssertionError("Failed to get a screenshot of Play/Pause button"));
            final double overlapScore = ImageUtil.getOverlapScore(currentState, initialState, ImageUtil
                    .RESIZE_TO_MAX_SCORE);
            if (overlapScore < MAX_BUTTON_STATE_OVERLAP) {
                return;
            } else {
                playPauseBtn.click();
                Thread.sleep(2000);
            }
            clickTry++;
        } while (clickTry <= MAX_CLICK_RETRIES);
        assert (clickTry > MAX_CLICK_RETRIES) : "Media playback state has not been changed after " + MAX_CLICK_RETRIES
                + " retries";
    }

    public boolean waitUntilYoutubePlayButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idYoutubePlayButton);
    }

    public void tapPlayPauseMediaBarBtn() throws Exception {
        getElement(idMediaBarPlayBtn, "Media bar PlayPause button is not visible").click();
    }

    private boolean waitUntilMediaBarVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMediaToolbar, timeoutSeconds);
    }

    public boolean waitUntilMissedCallMessageIsVisible(String expectedMessage) throws Exception {
        final By locator = By.xpath(xpathStrMissedCallMesageByText.apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLastMessageEqualTo(String expectedMessage, int timeoutSeconds) throws Exception {
        return isIndexMessageEqualsTo(expectedMessage, timeoutSeconds, MessageIndexLocator.LAST);
    }

    public boolean isFirstMessageEqualTo(String expectedMessage, int timeoutSeconds) throws Exception {
        return isIndexMessageEqualsTo(expectedMessage, timeoutSeconds, MessageIndexLocator.FIRST);
    }

    /**
     * Compare the indexed message with expected message, such first or last, or N-th message, but you need to
     * pass the correspondant locator.
     *
     * @param expectedMessage     expected message
     * @param timeoutSeconds      time out in seconds
     * @param messageIndexLocator the locator used for locating the N-th message
     * @return true measn expected message in expected index
     * @throws Exception
     */
    private boolean isIndexMessageEqualsTo(String expectedMessage, int timeoutSeconds,
                                           MessageIndexLocator messageIndexLocator) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(expectedMessage));
        return CommonUtils.waitUntilTrue(
                Timedelta.fromSeconds(timeoutSeconds),
                Timedelta.fromMilliSeconds(500),
                () -> {
                    final Optional<WebElement> msgElement = getElementIfDisplayed(locator, Timedelta.fromSeconds(1));
                    if (msgElement.isPresent()) {
                        final String indexMessage = getElement(messageIndexLocator.getLocator(),
                                "Cannot find the indexed message in the conversation",
                                Timedelta.fromSeconds(1)).getText();
                        return expectedMessage.equals(indexMessage);
                    } else {
                        return false;
                    }
                }
        );
    }

    public Optional<BufferedImage> getRecentPictureScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idConversationImageContainer));
    }

    public Optional<BufferedImage> getPreviewPictureScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idFullScreenImage));
    }

    public boolean waitUntilImageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idConversationImageContainer);
    }

    public boolean scrollUpUntilMediaBarVisible(final int maxScrollRetries) throws Exception {
        int swipeNum = 1;
        while (swipeNum <= maxScrollRetries) {
            swipeByCoordinates(Timedelta.fromMilliSeconds(1000),
                    50, 30, 50, 90);
            if (waitUntilMediaBarVisible(2)) {
                return true;
            }
            swipeNum++;
        }
        return false;
    }

    private static final long IMAGES_VISIBILITY_TIMEOUT = 10000; // seconds;

    public boolean waitForXImages(int expectedCount) throws Exception {
        assert expectedCount >= 0;
        final Optional<WebElement> imgElement = getElementIfDisplayed(idConversationImageContainer);
        if (expectedCount <= 1) {
            return (expectedCount == 0 && !imgElement.isPresent()) || (expectedCount == 1 && imgElement.isPresent());
        }
        final long msStarted = System.currentTimeMillis();
        do {
            int actualCnt = getElements(idConversationImageContainer).size();
            if (actualCnt >= expectedCount) {
                return true;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - msStarted <= IMAGES_VISIBILITY_TIMEOUT);
        return false;
    }

    public boolean waitForMessageNotification(String message) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathMessageNotificationByValue.apply(message)));
    }

    public void tapMessageNotification(String message) throws Exception {
        getElement(By.xpath(xpathMessageNotificationByValue.apply(message))).click();
    }

    public boolean isMediaBarBelowUptoolbar() throws Exception {
        return isElementABelowElementB(getElement(idMediaToolbar), getElement(xpathToolbar),
                LOCATION_DIFFERENCE_BETWEEN_TOP_TOOLBAR_AND_MEDIA_BAR);
    }

    public void waitUntilFileUploadIsCompleted(int timeoutSeconds, String size, String extension) throws Exception {
        Thread.sleep(2000);
        String fileInfo = StringUtils.isEmpty(extension) ? size :
                size + FILE_MESSAGE_SEPARATOR + extension.toUpperCase();
        fileInfo = String.format("%s%s%s", fileInfo, FILE_MESSAGE_SEPARATOR, FILE_UPLOADING_MESSAGE);
        DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(xpathFileInfoPlaceHolderByValue.apply(fileInfo)), timeoutSeconds);
    }

    public boolean isFilePlaceHolderVisible(String fileFullName, String size, String extension,
                                            boolean isUpload, boolean isSuccess, int timeout) throws Exception {
        size = size.toUpperCase();
        String fileInfo = StringUtils.isEmpty(extension) ? size :
                String.format("%s%s%s", size, FILE_MESSAGE_SEPARATOR, extension.toUpperCase());

        if (!isSuccess) {
            fileInfo = String.format("%s%s%s", fileInfo, FILE_MESSAGE_SEPARATOR,
                    isUpload ? FILE_UPLOAD_FAILED : FILE_DOWNLOADING_MESSAGE);
        }

        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathFileNamePlaceHolderByValue.apply(fileFullName)), timeout) &&
                DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                        By.xpath(xpathFileInfoPlaceHolderByValue.apply(fileInfo)), timeout);
    }

    public boolean isFilePlaceHolderInvisible(String fileFullName, String size, String extension,
                                              boolean isUpload, boolean isSuccess, int timeout) throws Exception {
        size = size.toUpperCase();
        String fileInfo = StringUtils.isEmpty(extension) ? size :
                String.format("%s%s%s", size, FILE_MESSAGE_SEPARATOR, extension.toUpperCase());

        if (!isSuccess) {
            fileInfo = String.format("%s%s%s", fileInfo, FILE_MESSAGE_SEPARATOR,
                    isUpload ? FILE_UPLOAD_FAILED : FILE_DOWNLOADING_MESSAGE);
        }

        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(xpathFileNamePlaceHolderByValue.apply(fileFullName)), timeout) &&
                DriverUtils.waitUntilLocatorDissapears(getDriver(),
                        By.xpath(xpathFileInfoPlaceHolderByValue.apply(fileInfo)), timeout);
    }

    public void tapFileActionButton() throws Exception {
        getElement(idFileActionBtn).click();
    }

    public void tapFileDialogActionButton(String action) throws Exception {
        switch (action) {
            case "save":
                getElement(idFileDialogActionSaveBtn).click();
                break;
            case "open":
                getElement(idFileDialogActionOpenBtn).click();
                break;
            default:
                throw new Exception(String.format("Cannot identify the action '%s' in File dialog", action));

        }
    }

    private By getContainerLocatorByName(String containerType) {
        switch (containerType.toLowerCase()) {
            case "picture":
            case "image":
                return idConversationImageContainer;
            case "youtube":
                return idYoutubeContainer;
            case "soundcloud":
                return idSoundcloudContainer;
            case "file upload":
                return idFileTransferContainer;
            case "file upload placeholder":
                return idFileTransferPlaceholder;
            case "video message":
                return idVideoMessageContainer;
            case "audio message":
                return idAudioMessageContainer;
            case "share location":
                return idShareLocationContainer;
            case "link preview":
                return idLinkPreviewContainer;
            default:
                throw new IllegalArgumentException(String.format("Unknown container type: '%s'", containerType));
        }
    }

    public boolean isContainerVisible(String name) throws Exception {
        final By locator = getContainerLocatorByName(name);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, CONTAINER_VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean isContainerInvisible(String name) throws Exception {
        final By locator = getContainerLocatorByName(name);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, CONTAINER_VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilLinkPreviewUrlVisible(String url) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathLinkPreviewUrlByValue.apply(url)));
    }

    public void tapContainer(String tapType, String index, String name) throws Exception {
        final By locator = getContainerLocatorByName(name);
        final List<WebElement> els = selectVisibleElements(locator);
        // By default, we get last matched element
        WebElement el = (index != null && index.toLowerCase().equals("first")) ? els.get(0) : els.get(els.size() - 1);
        final Point location = el.getLocation();
        final Dimension size = el.getSize();
        if (Arrays.asList(idAudioMessageContainer, idVideoMessageContainer, idYoutubeContainer).contains(locator)) {
            // To avoid to tap on play button in Video message and Audio message container.
            getDriver().tap(tapType, location.x + size.width / 5, location.y + size.height / 5);
        } else {
            // Tap on the center of container
            getDriver().tap(tapType, location.x + size.width / 2, location.y + size.height / 2);
        }
    }

    private By getTextLocatorByTextMessageType(String messageType, Optional<String> message) {
        switch (messageType.toLowerCase()) {
            case "ping":
                return message.isPresent() ? By.xpath(xpathStrPingMessageByText.apply(message.get()))
                        : xpathLastPingMessage;
            case "text":
                return message.isPresent() ? By.xpath(xpathStrConversationMessageByText.apply(message.get()))
                        : xpathLastConversationMessage;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the type '%s'", messageType));
        }
    }

    public void tapMessage(String messageType, Optional<String> message, String tapType) throws Exception {
        By locator = getTextLocatorByTextMessageType(messageType, message);
        switch (tapType.toLowerCase()) {
            case "tap":
                getElement(locator).click();
                break;
            case "long tap":
                getDriver().longTap(getElement(locator), DriverUtils.LONG_TAP_DURATION);
                break;
            case "double tap":
                Point location = getElement(locator).getLocation();
                getDriver().doubleTap(location.getX(), location.getY());
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the tap type '%s'", tapType));
        }
    }

    public void tapVideoMessageButton() throws Exception {
        getElement(idVideoContainerButton).click();
    }

    public void tapAudioMessageButton() throws Exception {
        getElement(idAudioContainerButton).click();
    }

    private By getImageContainerButtonLocator(String buttonName) {
        switch (buttonName.toLowerCase()) {
            case "sketch":
                return idImageContainerSketchButton;
            case "fullscreen":
                return idImageContainerFullScreenButton;
            case "sketch emoji":
                return idImageContainerSketchEmojiButton;
            case "sketch text":
                return idImageContainerSketchTextButton;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the button type '%s'", buttonName));
        }
    }

    public void tapImageContainerButton(String buttonName) throws Exception {
        By locator = getImageContainerButtonLocator(buttonName);
        getElement(locator).click();
    }

    public boolean waitUntilImageContainerButtonVisible(String buttonName) throws Exception {
        By locator = getImageContainerButtonLocator(buttonName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilImageContainerButtonInvisible(String buttonName) throws Exception {
        By locator = getImageContainerButtonLocator(buttonName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isVideoMessageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idVideoContainerButton);
    }

    public boolean isAudioMessageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idAudioContainerButton);
    }

    public Optional<BufferedImage> getVideoContainerButtonState() throws Exception {
        return getElementScreenshot(getElement(idVideoContainerButton));
    }

    public Optional<BufferedImage> getAudioContainerButtonState() throws Exception {
        return getElementScreenshot(getElement(idAudioContainerButton));
    }

    public void longTapAndKeepAudioMessageCursorBtn() throws Exception {
        final WebElement audioMsgButton = getElement(idCursorAudioMessage);
        final int x = audioMsgButton.getLocation().getX() + audioMsgButton.getSize().getWidth() / 2;
        final int y = audioMsgButton.getLocation().getY() + audioMsgButton.getSize().getHeight() / 2;
        new TouchActions(getDriver()).down(x, y).perform();
    }

    public boolean isCursorToolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorMore)
                || DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorLess);
    }

    public boolean isCursorToolbarInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idCursorMore)
                && DriverUtils.waitUntilLocatorDissapears(getDriver(), idCursorLess);
    }

    public int getMessageHeight(String msg) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(msg));
        return Integer.parseInt(getElement(locator).getAttribute("height"));
    }

    //region Message Bottom Menu
    private By getMessageBottomMenuButtonLocatorByName(String btnName) {
        switch (btnName.toLowerCase()) {
            case "delete":
                return idMessageBottomMenuDeleteGeneralButton;
            case "delete only for me":
                return idMessageBottomMenuDeleteLocalButton;
            case "delete for everyone":
                return idMessageBottomMenuDeleteGlobalButton;
            case "copy":
                return idMessageBottomMenuCopyButton;
            case "forward":
                return idMessageBottomMenuForwardButton;
            case "edit":
                return idMessageBottomMenuEditButton;
            case "like":
                return idMessageBottomMenuLikeButton;
            case "unlike":
                return idMessageBottomMenuUnlikeButton;
            case "open":
                return idMessageBottomMenuOpenButton;
            default:
                throw new IllegalArgumentException(String.format("There is no '%s' button on Message Bottom Menu",
                        btnName));
        }
    }

    public void tapMessageBottomMenuButton(String name) throws Exception {
        final By locator = getMessageBottomMenuButtonLocatorByName(name);
        assert scrollUntilMessageMenuElementVisible(locator, 5) : String
                .format("Message Menu item '%s' is not present", name);
        getElement(locator, String.format("Message bottom menu %s button is invisible", name)).click();
    }

    private boolean scrollUntilMessageMenuElementVisible(By locator, int maxScrolls) throws Exception {
        final int offset = 20;
        int nScrolls = 0;
        int screenHeight = AndroidCommonUtils.getScreenSize(getDriver()).getHeight();
        int containerHeight = getElement(idMessageBottomMenu).getSize().getHeight();
        int endHeightPercentage = (screenHeight - containerHeight) / screenHeight;

        while (nScrolls < maxScrolls) {
            Optional<WebElement> el = getElementIfDisplayed(locator, Timedelta.fromSeconds(1));
            if (el.isPresent() &&
                    el.get().getLocation().getY() + el.get().getSize().getHeight() - offset <= screenHeight) {
                return true;
            }
            swipeByCoordinates(Timedelta.fromMilliSeconds(500),
                    50, 95, 50, endHeightPercentage);
            nScrolls++;
        }
        return false;
    }

    public boolean waitUntilMessageBottomMenuButtonVisible(String btnNAme) throws Exception {
        final By locator = getMessageBottomMenuButtonLocatorByName(btnNAme);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilMessageBottomMenuButtonInvisible(String btnNAme) throws Exception {
        final By locator = getMessageBottomMenuButtonLocatorByName(btnNAme);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }
    //endregion

    //region Contact name icon
    //TODO: ROBIN - refactoring waitUntilTrash + waitUntilPen, also for tablet
    public boolean waitUntilTrashIconVisible(String name) throws Exception {
        final By locator = By.xpath(xpathMessageStateGlyphByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilTrashIconInvisible(String name) throws Exception {
        final By locator = By.xpath(xpathMessageStateGlyphByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilPenIconVisible(String name) throws Exception {
        final By locator = By.xpath(xpathMessageStateGlyphByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilPenIconInvisible(String name) throws Exception {
        final By locator = By.xpath(xpathMessageStateGlyphByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilMessageSeparatorVisible(String name, int timeout) throws Exception {
        final By locator = By.xpath(xpathMessageSeparator.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, timeout);
    }

    public boolean waitUntilMessageSeparatorInvisible(String name, int timeout) throws Exception {
        final By locator = By.xpath(xpathMessageSeparator.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, timeout);
    }

    public void tapAllResendButton() throws Exception {
        List<WebElement> resendButtonList = selectVisibleElements(idResendButton);
        for (WebElement resendButton : resendButtonList) {
            DriverUtils.tapOnPercentOfElement(this.getDriver(), resendButton, 85, 50);
        }
    }
    //endregion

    //region Message Meta
    public boolean waitUntilMessageMetaItemVisible(String itemType) throws Exception {
        String locatorId = getMessageMetaLocatorIdString(itemType);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(locatorId), JENKINS_STABILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilMessageMetaItemInvisible(String itemType) throws Exception {
        String locatorId = getMessageMetaLocatorIdString(itemType);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.id(locatorId), JENKINS_STABILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilMessageMetaItemVisible(String itemType, String expectedItemText)
            throws Exception {
        String locatorId = getMessageMetaLocatorIdString(itemType);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathStrTemplateIdValue.apply(locatorId, expectedItemText)), VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilMessageMetaItemInvisible(String itemType, String expectedItemText)
            throws Exception {
        String locatorId = getMessageMetaLocatorIdString(itemType);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(xpathStrTemplateIdValue.apply(locatorId, expectedItemText)));
    }

    public void tapMessageMetaItem(String itemType) throws Exception {
        String locatorId = getMessageMetaLocatorIdString(itemType);
        getElement(By.id(locatorId)).click();
    }

    public BufferedImage getMessageLikeButtonState() throws Exception {
        By locator = By.id(strIdMessageMetaLikeButton);
        return this.getElementScreenshot(getElement(locator)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot for like button")
        );
    }

    public int getMessageStatusCount() throws Exception {
        By locator = By.id(strIdMessageMetaStatus);
        return selectVisibleElements(locator).size();
    }

    private String getMessageMetaLocatorIdString(String itemType) throws Exception {
        switch (itemType.toLowerCase()) {
            case "like button":
                return strIdMessageMetaLikeButton;
            case "like description":
                return strIdMessageMetaLikeDescription;
            case "message status":
                return strIdMessageMetaStatus;
            case "first like avatar":
                return strIdMessageMetaFirstLike;
            case "second like avatar":
                return strIdMessageMetaSecondLike;
            default:
                throw new IllegalStateException(
                        String.format("Cannot identify the item type '%s' in Message Meta", itemType));
        }
    }
    //endregion
}
