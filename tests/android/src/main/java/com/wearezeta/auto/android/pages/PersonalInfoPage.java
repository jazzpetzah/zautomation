package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;

public class PersonalInfoPage extends AndroidPage
{

	private String url;
	private String path;

	@FindBy(how = How.ID, using = AndroidLocators.idBackgroundOverlay)
	private WebElement backgroundOverlay;

	@FindBy(how = How.ID, using = AndroidLocators.idSettingsBox)
	private WebElement settingBox;

	@FindBy(how = How.ID, using = AndroidLocators.idEmailField)
	private WebElement emailField;

	@FindBy(how = How.ID, using = AndroidLocators.idNameField)
	private WebElement nameField;

	@FindBy(how = How.ID, using = AndroidLocators.idSettingsBtn)
	private WebElement settingsButton;

	@FindBy(how = How.ID, using = AndroidLocators.idNameEdit)
	private WebElement nameEdit;

	@FindBy(how = How.ID, using = AndroidLocators.idChangePhotoBtn)
	private WebElement changePhotoBtn;

	@FindBy(how = How.ID, using = AndroidLocators.idGalleryBtn)
	private WebElement galleryBtn;

	@FindBy(how = How.ID, using = AndroidLocators.idConfirmButton)
	private WebElement confirmBtn;

	@FindBy(how = How.ID, using = AndroidLocators.idProfileOptionsButton)
	private WebElement optionsButton;

	@FindBy(how = How.ID, using = AndroidLocators.idAboutButton)
	private WebElement aboutButton;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLoginPage)
	private WebElement page;

	@FindBy(how = How.XPATH, using = AndroidLocators.xpathImagesFrameLayout)
	private List<WebElement> frameLayouts;

	@FindBy(how = How.ID, using = AndroidLocators.idSignOutBtn)
	private WebElement signOutBtn;

	@FindBy(how = How.ID, using = AndroidLocators.idOpenFrom)
	private List<WebElement> openFrom;

	@FindBy(how = How.ID, using = AndroidLocators.idProfileOptionsButton)
	private List<WebElement> settingsButtonList;

	@FindBy(how = How.XPATH, using = AndroidLocators.xpathImage)
	private List<WebElement> image;

	public PersonalInfoPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;

	}

	public boolean isPersonalInfoVisible() {

		return emailField.isDisplayed();
	}

	public void waitForEmailFieldVisible(){

		wait.until(ExpectedConditions.visibilityOf(emailField));
	}

	public void clickOnPage() throws InterruptedException{
		DriverUtils.androidMultiTap(driver, page,1,0.2);
	}

	public void selectPhoto(){
		refreshUITree();
		try{
			frameLayouts.get(0).click();
			return;
		}
		catch(Exception ex)
		{

		}
		try{
			image.get(0).click();
			return;
		}
		catch(Exception ex){
		}
	}

	public void tapChangePhotoButton(){
		changePhotoBtn.click();
	}

	public void tapGalleryButton(){
		galleryBtn.click();
	}

	public void tapConfirmButton() throws IOException{
		confirmBtn.click();
	}

	public void tapSignOutBtn(){

		refreshUITree();
		signOutBtn.click();
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {

		AndroidPage page = null;
		switch (direction){
		case DOWN:
		{
			break;
		}
		case UP:
		{
			page = this;
			break;
		}
		case LEFT:
		{
			break;
		}
		case RIGHT:
		{
			page = new ContactListPage(url,path);
			break;
		}
		}	
		return page;
	}

	public void tapOptionsButton() throws InterruptedException {
		optionsButton.click();
	}

	public SettingsPage tapSettingsButton() throws Exception {

		refreshUITree();
		settingsButton.click();
		return new SettingsPage (url, path);
	}

	public void waitForConfirmBtn(){
		wait.until(ExpectedConditions.visibilityOf(confirmBtn));
	}

	public void tapOnMyName() {
		wait.until(ExpectedConditions.visibilityOf(nameField));
		nameField.click();
	}

	public void changeName(String name, String newName) {
		for(int i=0; i<name.length();i++)
		{
			driver.sendKeyEvent(67);
		}
		nameEdit.sendKeys(newName);
		DriverUtils.mobileTapByCoordinates(driver, backgroundOverlay);
	}

	public String getUserName() {
		refreshUITree();
		return nameField.getText();
	}

	public AboutPage tapAboutButton() throws Exception {
		refreshUITree();
		aboutButton.click();
		return new AboutPage(url, path);
	}

	public boolean isSettingsVisible() {

		return settingBox.isDisplayed();
	}

	public boolean isSettingsButtonNotVisible() {
		boolean flag = false;
		refreshUITree();
		if(settingsButtonList == null || settingsButtonList.isEmpty())
		{
			flag = true;
		}
		return flag;
	}
	
	public boolean waitForSettingsDissapear() {

		return DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.idProfileOptionsButton));
	}

}
