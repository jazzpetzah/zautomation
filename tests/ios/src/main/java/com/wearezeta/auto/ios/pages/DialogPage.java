package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class DialogPage extends IOSPage{
	private static final Logger log = ZetaLogger.getLog(DialogPage.class.getSimpleName());
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
	private WebElement dialogWindow;
	
//	@FindBy(how = How.XPATH, using = IOSLocators.xpathCursorInput)
//	private WebElement cursorInput;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConversationCursorInput)
	private WebElement conversationInput;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameTextInput)
	private WebElement textInput;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameDialogMessages)
	private List<WebElement> messagesList;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameConnectMessageLabel)
	private WebElement connectMessageLabel;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePendingButton)
	private WebElement pendingButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathHelloCellFormat)
	private WebElement helloCell;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathHeyCellFormat)
	private WebElement heyCell;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAddPictureButton)
	private WebElement addPictureButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherConversationCellFormat)
	private WebElement imageCell;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathNameMediaContainer)
	private WebElement mediaContainer;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathMediaConversationCell)
	private WebElement mediaLinkCell;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarPlayPauseButton)
	private WebElement mediabarPlayPauseButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathConversationPage)
	private WebElement conversationPage;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarCloseButton)
	private WebElement mediabarStopCloseButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarTitle)
	private WebElement mediabarBarTitle;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathYouAddedMessageCellFormat)
	private List<WebElement> youAddedCell;
	
	
	private String url;
	private String path;
	
	public DialogPage(String URL, String path) throws IOException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}

	public void waitForCursorInputVisible(){
		wait.until(ExpectedConditions.visibilityOf(conversationInput));
	}
	
	public void waitForYouAddedCellVisible(){
		wait.until(ExpectedConditions.visibilityOf(youAddedCell.get(0)));
	}
	
	public void tapOnCursorInput(){
		conversationInput.click();
	}
	
	public void multiTapOnCursorInput() throws InterruptedException{
		
		DriverUtils.iOSMultiTap(driver, conversationInput, 3);
	}
	
	public void sendStringToInput(String message) throws InterruptedException
	{
		conversationInput.sendKeys(message);
	}
		
	public void ScrollToLastMessage(){
		//DriverUtils.scrollToElement(driver, messagesList.get(messagesList.size()-1));
	}
	
	public void scrollToTheEndOfConversation() {
//		WebElement inputField = driver.findElement(By.name(IOSLocators.nameConversationCursorInput));
//		driver.tap(1, inputField, 500);
		String script = IOSLocators.scriptCursorInputPath + ".tap();";
		driver.executeScript(script);
	}

	public String getLastMessageFromDialog()
	{
		return GetLastMessage(messagesList);
	}
		
	public boolean isConnectMessageValid(String username){
		return getConnectMessageLabel().equals("Connect to " + username);
	}
	
	public boolean isPendingButtonVisible(){
		return pendingButton.isDisplayed();
	}
	
	private String GetHelloCell(List<WebElement> chatList) {
		String helloCellText = helloCell.getAttribute("name");  
		return helloCellText;
	}

	public String getHelloCellFromDialog()
	{
		return GetHelloCell(messagesList);
	}
	
	private String GetHeyCell(List<WebElement> chatList) {
		String heyCellText = heyCell.getAttribute("name");  //I dont get HEY FROM PIOTR here
		return heyCellText;
	}

	public String getHeyCellFromDialog()
	{
		return GetHeyCell(messagesList);
	}
	
	public void swipeInputCursor() throws IOException, InterruptedException{
		DriverUtils.swipeRight(driver, conversationInput, 500);
	}
	
	public CameraRollPage pressAddPictureButton() throws IOException{
		
		CameraRollPage page;
		addPictureButton.click();
		DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameCameraLibraryButton));
		page = new CameraRollPage(url, path);
		
		return page;
	}

	
	private String GetImageCell(List<WebElement> chatList) {
		String lastMessage = imageCell.getAttribute("name");
		return lastMessage;
	}

	public String getImageCellFromDialog()
	{
		return GetImageCell(messagesList);
	}
	
	public void startMediaContent(){
		mediaLinkCell.click();
	}
	
	public DialogPage scrollDownTilMediaBarAppears() throws Exception{
		DialogPage page = null;
		int count = 0;
		boolean buttonIsShown = mediabarPlayPauseButton.isDisplayed();
		while(!(buttonIsShown) & (count<5)){
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true){
				DriverUtils.swipeDown(driver, conversationPage, 500);
				page = this;
			}
			else {
				swipeDownSimulator();
				page = this;
			}
			buttonIsShown = mediabarPlayPauseButton.isDisplayed();
			count++;
		}
		
		Assert.assertTrue(mediabarPlayPauseButton.isDisplayed());
		return page;
	}

	public void pauseMediaContent(){
		mediabarPlayPauseButton.click();
	}
	
	public void playMediaContent(){
		mediabarPlayPauseButton.click();
	}
	
	public void stopMediaContent(){
		mediabarStopCloseButton.click();
	}
	
	public String getMediaState(){
		
		String mediaState = mediabarBarTitle.getAttribute("value");
		return mediaState;
	}
	
	public void tapOnMediaBar(){
		mediabarBarTitle.click();
	}
	
	@Override
	public IOSPage swipeUp(int time) throws IOException
	{
		WebElement element =  driver.findElement(By.name(IOSLocators.nameMainWindow));
		
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		driver.swipe(coords.x + elementSize.width / 2, coords.y + elementSize.height - 170, coords.x + elementSize.width / 2, coords.y + 40, time);
		return returnBySwipe(SwipeDirection.UP);
	}
	
	public DialogPage swipeDialogPageDown(int time) throws Throwable {		
		DialogPage page = null;
		if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true){
			DriverUtils.swipeDown(driver, conversationPage, time);
			page = this;
		}
		else {
			swipeDownSimulator();
			page = this;
		}
		return page;
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		IOSPage page = null;
		switch (direction){
			case DOWN:
			{
				page= new DialogPage(url, path);
				break;
			}
			case UP:
			{
				page = new OtherUserPersonalInfoPage(url, path);
				break;
			}
			case LEFT:
			{
				page = new OtherUserPersonalInfoPage(url, path);
				break;
			}
			case RIGHT:
			{
				page = new ContactListPage(url, path);
				break;
			}
		}	
		return page;
	}
	
	public boolean isMediaContainerVisible(){
		return mediaLinkCell != null;
	}
	
	public VideoPlayerPage clickOnVideoContainerFirstTime() throws IOException, InterruptedException {
		VideoPlayerPage page = new VideoPlayerPage(url, path);
		mediaContainer.click();
		
		DriverUtils.setImplicitWaitValue(driver, 5);
		if (!page.isVideoPlayerPageOpened()) {
			if (!mediaLinkCell.isDisplayed()) {
				DriverUtils.mobileTapByCoordinates(driver, mediaContainer);
			}
			mediaLinkCell.click();
		}
		DriverUtils.setDefaultImplicitWait(driver);
		return page;
	}
	
	public void tapDialogWindow(){
		driver.tap(1, 1, 1, 500);
	}
	
	private String getConnectMessageLabel(){
		return connectMessageLabel.getText();
	}
	
	private String GetLastMessage(List<WebElement> chatList) {
		String lastMessageXPath = String.format(IOSLocators.xpathLastMessageFormat, chatList.size());
		WebElement el = driver.findElementByXPath(lastMessageXPath);
		String lastMessage = el.getText();
		return lastMessage;
	}

	public String getSendTime() {
		String formattedDate;
		DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy '∙' h:mm a");
		Date date = new Date();
		formattedDate=dateFormat.format(date);
		return formattedDate;
	}

	public boolean isMediaBarDisplayed() {
		boolean flag = mediabarPlayPauseButton.isDisplayed();
		return flag;
	}

	public DialogPage scrollUpToMediaContainer() throws Throwable {
		DialogPage page = null;
		int count = 0;
		boolean mediaContainerShown = mediaContainer.isDisplayed();
		while(!(mediaContainerShown) & (count<5)){
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true){
				DriverUtils.swipeUp(driver, conversationPage, 500);
				page = this;
			}
			else {
				swipeUpSimulator();
				page = this;
			}
			mediaContainerShown = mediabarPlayPauseButton.isDisplayed();
			count++;
		}		
		Assert.assertTrue(mediabarPlayPauseButton.isDisplayed());
		return page;
	}

	public ImageFullScreenPage tapImageToOpen() throws Throwable {
		ImageFullScreenPage page = null;
		imageCell.click();
		page = new ImageFullScreenPage(url, path);
		return page;
	}
	
	public void tapHoldTextInput(){
		driver.tap(1, driver.findElement(By.name(IOSLocators.nameConversationCursorInput)), 1000);
	}
	
	public DialogPage scrollToBeginningOfConversation() throws Throwable, Exception{
		DialogPage page = null;
		int count = 0;
		if(youAddedCell.size() > 0){
		boolean beginningConversation = youAddedCell.get(0).isDisplayed();
		while(!(beginningConversation) & (count<5)){
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true){
				DriverUtils.swipeDown(driver, conversationPage, 500);
				page = this;
			}
			else {
				swipeDownSimulator();
				page = this;
			}
			beginningConversation = youAddedCell.get(0).isDisplayed();
			count++;
		}
	   }
		Assert.assertTrue(youAddedCell.get(0).isDisplayed());
		return page;
	}
	
	private static final int IMAGE_CONTROL_IN_CONVERSATION_HEIGHT = 472;
	private static final int IMAGE_IN_CONVERSATION_HEIGHT = 427;
	public BufferedImage takeImageScreenshot() throws Throwable{
		BufferedImage image;
		image= getElementScreenshot(imageCell);
		if (image.getHeight()>IMAGE_IN_CONVERSATION_HEIGHT){
			image= image.getSubimage(0, image.getHeight()-IMAGE_CONTROL_IN_CONVERSATION_HEIGHT, image.getWidth(), IMAGE_IN_CONVERSATION_HEIGHT);
		}
		return image;
	}
	
	public DialogPage scrollToImage() throws Throwable{
		WebElement el = driver.findElement(By.xpath(IOSLocators.xpathOtherConversationCellFormat));
		DriverUtils.scrollToElement(driver, el);
		DialogPage page = new DialogPage(url, path);
		return page;
	}
	
	
	private static final String TEXT_MESSAGE_PATTERN = "<UIATextView[^>]*value=\"([^\"]*)\"[^>]*>\\s*</UIATextView>";
	private static final int TIMES_TO_SCROLL = 100;
	
	public boolean swipeAndCheckMessageFound(String direction, String pattern) throws IOException, Exception {
		

		boolean result = false;
		
		Point coords = new Point(0, 0);
		Dimension elementSize = driver.manage().window().getSize();

		switch(direction) {
		case "up":
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
				driver.swipe(
						coords.x + elementSize.width / 2, coords.y + elementSize.height/2,
						coords.x + elementSize.width / 2, coords.y + 120,
						500);
			} else {
				DriverUtils.iOSSimulatorSwipeDialogPageUp(
						CommonUtils.getSwipeScriptPath(IOSPage.class));
			}
		

			break;
		case "down":
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
				driver.swipe(
						coords.x + elementSize.width / 2, coords.y + 50,
						coords.x + elementSize.width / 2, coords.y + elementSize.height - 100,
						500);
			} else {
				DriverUtils.iOSSimulatorSwipeDialogPageDown(
						CommonUtils.getSwipeScriptPath(IOSPage.class));
			}
		
			break;
		default:
			log.fatal("Unknown direction");
		}
		String source = driver.getPageSource();
		Pattern messagesPattern = Pattern.compile(TEXT_MESSAGE_PATTERN);
		Matcher messagesMatcher = messagesPattern.matcher(source);
		while (messagesMatcher.find()) {
			String message = messagesMatcher.group(1);
			Pattern messagePattern = Pattern.compile(pattern);
			Matcher messageMatcher = messagePattern.matcher(message);
			if (messageMatcher.find()) {
				result = true;
			}
		}
		return result;
	}
	
	public void swipeTillTextMessageWithPattern(String direction, String pattern) throws IOException, Exception {
		boolean isAddedMessage = false;
		int count = 0;
		do {
			isAddedMessage = swipeAndCheckMessageFound(direction, pattern);
			count++;
		} while (!isAddedMessage && count < TIMES_TO_SCROLL);
	}
	
	private static final String UUID_TEXT_MESSAGE_PATTERN = "<UIATextView[^>]*value=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*>\\s*</UIATextView>";
	private static final String DIALOG_START_MESSAGE_PATTERN = "^(.*)\\sADDED\\s(.*)$";
	public ArrayList<MessageEntry> listAllMessages(boolean checkTime) throws Exception, Throwable {
		try {
			log.debug("Trying to close keyboard");
			driver.hideKeyboard();
		} catch (WebDriverException e) { }
		
		String lastMessage = messagesList.get(messagesList.size()-1).getText();
	
		swipeTillTextMessageWithPattern("down", DIALOG_START_MESSAGE_PATTERN);

		LinkedHashMap<String, MessageEntry> messages = new LinkedHashMap<String, MessageEntry>();
		
		boolean lastMessageAppears = false;
		boolean temp = false;
		int i = 0;
		do {
			i++;
			lastMessageAppears = temp;
			Date receivedDate = new Date();
			String source = driver.getPageSource();
			Pattern pattern = Pattern.compile(UUID_TEXT_MESSAGE_PATTERN);
			Matcher matcher = pattern.matcher(source);
			while (matcher.find()) {
				if (messages.get(matcher.group(1)) == null) {
					messages.put(matcher.group(1), new MessageEntry("text", matcher.group(1), receivedDate, checkTime));
				}
			}
			driver.getPageSource();
			if (!lastMessageAppears) {
				temp = swipeAndCheckMessageFound("up", lastMessage);
			}
		} while (!lastMessageAppears && i < TIMES_TO_SCROLL);
		

		ArrayList<MessageEntry> listResult = new ArrayList<MessageEntry>();
		
		for (Map.Entry<String, MessageEntry> mess: messages.entrySet()) {
			listResult.add(mess.getValue());
		}
		return listResult;
	}
	
	public MessageEntry receiveMessage(String message, boolean checkTime) {
		WebElement messageElement = driver.findElement(By.name(message));
		if (messageElement != null) {
			return new MessageEntry("text", message, new Date(), checkTime);
		}
		return null;
	}
	
	public void sendMessageUsingScript(String message) {
		String script = String.format(
				IOSLocators.scriptCursorInputPath + ".setValue(\"%s\");" +
						IOSLocators.scriptKeyboardReturnKeyPath + ".tap();", message);
		driver.executeScript(script);
	}
	
	public void sendMessagesUsingScript(String[] messages) {
		//swipe down workaround
		try {
			Point coords = new Point(0, 0);
			Dimension elementSize = driver.manage().window().getSize();

			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
				driver.swipe(
					coords.x + elementSize.width / 2, coords.y + 50,
					coords.x + elementSize.width / 2, coords.y + elementSize.height - 100,
					500);
			} else {
				DriverUtils.iOSSimulatorSwipeDialogPageDown(
						CommonUtils.getSwipeScriptPath(IOSPage.class));
			}
		} catch (Exception e) { }
	
		scrollToTheEndOfConversation();
		String script = "";
		for (int i = 0; i < messages.length; i++) {
			script +=
					String.format(
							IOSLocators.scriptCursorInputPath + ".setValue(\"%s\");" +
									IOSLocators.scriptKeyboardReturnKeyPath + ".tap();", messages[i]);
		}
		driver.executeScript(script);
	}
	
	public void takeCameraPhoto() throws IOException, InterruptedException{
		swipeInputCursor();
		CameraRollPage page = pressAddPictureButton();
		page.pressSelectFromLibraryButton();
		page.pressConfirmButton();
	}
	
	public DialogPage sendImageFromAlbum() throws Throwable{
		swipeInputCursor();
		Thread.sleep(1000);
		CameraRollPage page = pressAddPictureButton();
		page.pressSelectFromLibraryButton();
		page.clickFirstLibraryFolder();
		page.clickFirstImage();
		page.pressConfirmButton();
		return new DialogPage(url, path);
	}
}
