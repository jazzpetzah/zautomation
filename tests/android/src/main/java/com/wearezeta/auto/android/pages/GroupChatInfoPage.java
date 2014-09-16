package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class GroupChatInfoPage extends AndroidPage {

	public static final String LEAVE_CONVERSATION_BUTTON = "Leave conversation";
	public static final String LEAVE_BUTTON = "LEAVE";
	private static final String AVATAR_WITH_IMAGE = "avatarPictureTestAndroid.png";
	private static final String AVATAR_NO_IMAGE = "avatarTestAndroid.png";
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.95;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.GroupChatInfoPage.CLASS_NAME, locatorKey = "idLeaveConversationButton")
	private WebElement leaveConversationButton;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idConfirmBtn")
	private WebElement confirmButton;
	
	@FindBy(how = How.XPATH, using = AndroidLocators.GroupChatInfoPage.xpathGroupChatInfoLinearLayout)
	private List<WebElement> linearLayout;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.GroupChatInfoPage.classNameGridView)
	private WebElement groupChatUsersGrid;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.GroupChatInfoPage.CLASS_NAME, locatorKey = "idGroupChatInfoName")
	private WebElement groupChatName;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.GroupChatInfoPage.CLASS_NAME, locatorKey = "idParticipantsSubHeader")
	private WebElement participantsSubHeader;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
	private List<WebElement> connectToHeader;
	
	
	private String url;
	private String path;
	
	public GroupChatInfoPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws Exception {
		AndroidPage page = null;
		switch (direction){
		case DOWN:
		{
			break;
		}
		case UP:
		{
			break;
		}
		case LEFT:
		{
			break;
		}
		case RIGHT:
		{
			page = new GroupChatPage(url,path);
			break;
		}
		}	
		return page;
	}

	public void pressLeaveConversationButton() {
		refreshUITree();//TODO workaround
		wait.until(ExpectedConditions.elementToBeClickable(leaveConversationButton));
		leaveConversationButton.click();

	}

	public void pressConfirmButton() {
		refreshUITree();//TODO workaround
		confirmButton.click();
	}

	public void renameGroupChat(String chatName){
		groupChatName.sendKeys(chatName + "\n");
	}
	
	public AndroidPage selectContactByName(String contactName) throws Exception, InterruptedException {
		boolean flag = false;
		refreshUITree();

		for(WebElement user : linearLayout)
		{
			List<WebElement> elements = user.findElements(By.className(AndroidLocators.CommonLocators.classNameTextView));
			for(WebElement element : elements){
				if(element.getText() != null && element.getText().equals((contactName.toUpperCase()))){
					user.click();
					flag = true;
					break;
				}
			}
			if(flag){
				break;
			}
		}
		if(connectToHeader.size()>0){
			return new ConnectToPage(url, path);
		}
		else{
			return new OtherUserPersonalInfoPage(url, path);
		}
	}
	
	public GroupChatPage tabBackButton() throws Exception {
		driver.navigate().back();
		return new GroupChatPage(url, path);
	}

	public boolean isParticipantAvatars(String contact1, String contact2) throws IOException {
		boolean flag1 = false;
		boolean flag2 = false;
		boolean commonFlag = false;
		BufferedImage avatarIcon = null;
		refreshUITree();
		String path = CommonUtils.getImagesPath(CommonUtils.class);
		for(int i = 1; i < linearLayout.size()+1;i++){
			avatarIcon = getElementScreenshot(driver.findElement(By.xpath(String.format(AndroidLocators.GroupChatInfoPage.xpathGroupChatInfoContacts, i,1))));
			String avatarName = driver.findElement(By.xpath(String.format(AndroidLocators.GroupChatInfoPage.xpathGroupChatInfoContacts, i,2))).getText();
			if(avatarName.equalsIgnoreCase(contact1)){
				BufferedImage realImage = ImageUtil.readImageFromFile(path+AVATAR_WITH_IMAGE);
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				}
				flag1 = true;
			}
			if(avatarName.equalsIgnoreCase(contact2)){
				//must be a yellow user with initials AT
				BufferedImage realImage = ImageUtil.readImageFromFile(path+AVATAR_NO_IMAGE);
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				}
				flag2 = true;
			}
		}
		if(flag1 && flag2)
		{
			commonFlag = true;
		}
		
		return commonFlag;
	}

	public String getSubHeader() {
		return participantsSubHeader.getText();
	}

	public String getConversationName() {
		refreshUITree();
		return groupChatName.getText();
	}

	public AndroidPage tapOnContact(String contact) throws Exception {
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(groupChatName));
		WebElement cn = driver.findElement(By.xpath(String.format(AndroidLocators.ContactListPage.xpathContacts, contact.toUpperCase())));
		cn.click();
		if(connectToHeader.size()>0){
			return new ConnectToPage(url, path);
		}
		else{
			return new OtherUserPersonalInfoPage(url, path);
		}
	}

	public boolean isContactExists(String contact) {
		boolean flag = false;
		refreshUITree();
		
		for(WebElement user : linearLayout)
		{
			List<WebElement> elements = user.findElements(By.className(AndroidLocators.CommonLocators.classNameTextView));
			for(WebElement element : elements){
					if(element.getText() != null && element.getText().equals((contact.toUpperCase()))){
						flag = true;
						break;
					}
			}
			if(flag){
				break;
			}
		}
		return flag;
	}	
}
