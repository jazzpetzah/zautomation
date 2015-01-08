package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class OtherUserOnPendingProfilePage extends IOSPage {
	
	@FindBy(how = How.XPATH, using = IOSLocators.nameOtherUserProfilePageCloseButton)
	private WebElement closePageButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameOtherProfilePagePendingLabel)
	private WebElement pendingLabel;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameOtherProfilePageStartConversationButton)
	private WebElement startConversationButton;
	
	public OtherUserOnPendingProfilePage(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}
	
	public boolean isClosePageButtonVisible(){
		return DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameOtherProfilePageStartConversationButton));
	}
	
	public boolean isPendingLabelVisible(){
		return DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameOtherProfilePageStartConversationButton));
	}
	
	public void clickStartConversationButton(){
		startConversationButton.click();
	}
	
	public boolean isUserNameDisplayed(String name){
		WebElement otherUserName = driver.findElementByName(name);
		return DriverUtils.isElementDisplayed(otherUserName);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
