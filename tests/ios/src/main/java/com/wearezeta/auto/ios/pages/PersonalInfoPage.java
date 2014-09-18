package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class PersonalInfoPage extends IOSPage{
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathEmailField)
	private WebElement emailField;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathUserProfileName)
	private WebElement profileNameField;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameUIAButton)
	private List<WebElement> optionsButtons;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameProfileSettingsButton)
	private WebElement settingsButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSignOutButton)
	private WebElement signoutButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathPersonalInfoPage)
	private WebElement personalPage;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCameraButton)
	private WebElement cameraButton;
	
	
	private String url;
	private String path;
	
	public PersonalInfoPage(String URL, String path) throws IOException {
		super(URL, path);
		url = URL;
		this.path = path;
	}
	
	public String getUserNameValue(){
		String name = profileNameField.getText();
		return name;
	}
	
	public String getUserEmailVaue(){
		String email = emailField.getText();
		return email;
	}
	
	public boolean isSettingsButtonVisible() {
		return DriverUtils.isElementDisplayed(settingsButton);
	}
	
	public PersonalInfoPage clickOnSettingsButton(){
		settingsButton.click();
		return this;
	}
	
	public LoginPage clickSignoutButton() throws MalformedURLException{
		LoginPage page;
		signoutButton.click();
		page = new LoginPage(url, path);
		return page;
	}
	
	public void waitForSettingsButtonAppears(){
		DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameProfileSettingsButton));
	}
	
	public void waitForEmailFieldVisible(){
		DriverUtils.waitUntilElementAppears(driver, By.xpath(IOSLocators.xpathEmailField));
	}
	
	public void tapOptionsButtonByText(String buttonText){
		
		 for(WebElement button : optionsButtons)
		 {
			 if (button.getText().equals(buttonText))
			 {
				 button.click();
				 break;
			 }
		 }
	}
	
	public void tapOnPersonalPage(){
		personalPage.click();
	}
	
	public CameraRollPage pressCameraButton() throws IOException{
		
		CameraRollPage page;
		page = new CameraRollPage(url, path);
		cameraButton.click();
		
		return page;
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		
		IOSPage page = null;
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
			page = new ContactListPage(url, path);
			break;
		}
	}	
		return page;
	}

}
