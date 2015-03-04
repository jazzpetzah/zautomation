package com.wearezeta.auto.ios.pages;

import io.appium.java_client.AppiumDriver;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class LoginPage extends IOSPage {
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());
	

	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
	private WebElement viewPager;

	@FindBy(how = How.NAME, using = IOSLocators.nameSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameLoginButton)
	private WebElement confirmSignInButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameRegisterButton)
	private WebElement registerButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameLoginField)
	private WebElement loginField;

	@FindBy(how = How.NAME, using = IOSLocators.namePasswordField)
	private WebElement passwordField;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classUIATextView)
	private List<WebElement> userName;

	@FindBy(how = How.NAME, using = IOSLocators.nameTermsPrivacyLinks)
	private WebElement termsButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameTermsPrivacyLinks)
	private WebElement privacyButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameTermsPrivacyCloseButton)
	private WebElement termsPrivacyCloseButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameErrorMailNotification)
	private WebElement errorMailNotification;

	@FindBy(how = How.NAME, using = IOSLocators.nameWrongCredentialsNotification)
	private WebElement wrongCredentialsNotification;

	@FindBy(how = How.NAME, using = IOSLocators.nameIgnoreUpdateButton)
	private WebElement ignoreUpdateButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameTermsOfServiceButton)
	private WebElement termsOfServiceButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameProfileName)
	private WebElement selfProfileName;

	@FindBy(how = How.NAME, using = IOSLocators.nameShareButton)
	private WebElement shareButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameForgotPasswordButton)
	private WebElement changePasswordButtonSignIn;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathChangePasswordEmailField)
	private WebElement changePWEmailField;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathChangePasswordPageChangePasswordButton)
	private WebElement changePasswordPageChangePasswordButton;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameUIATextField)
	private List<WebElement> textFields;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameUIAButton)
	private List<WebElement> uiButtons;

	private String login;

	private String password;

	public String message;

	public LoginPage(ZetaIOSDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public Boolean isVisible() {
		return viewPager != null;
	}

	public IOSPage signIn() throws IOException {

		signInButton.click();
		return this;
	}

	public PeoplePickerPage clickLaterButton() throws Exception {
		if (DriverUtils.isElementDisplayed(shareButton)) {
			shareButton.click();
			return new PeoplePickerPage(this.getDriver(), this.getWait());
		} else {
			// workaround for Sync Engine scenario
			// on real iOS device when contacts are shared there is no
			// Share Contacts dialog but people picker page appears, which we
			// not process in case no Share Contacts dialog
			if (!CommonUtils.getIsSimulatorFromConfig(LoginPage.class)) {
				return new PeoplePickerPage(this.getDriver(), this.getWait());
			}
		}

		return null;
	}

	public boolean isSelfProfileVisible() {

		return DriverUtils
				.isElementDisplayed(PagesCollection.loginPage.selfProfileName);
	}

	public IOSPage login() throws Exception {

		confirmSignInButton.click();

		if (DriverUtils.waitUntilElementDissapear(driver,
				By.name(IOSLocators.nameLoginButton), 40)) {
			return new ContactListPage(this.getDriver(), this.getWait());
		} else {
			return null;
		}
	}

	public void clickLoginButton() {
		confirmSignInButton.click();
	}

	public void clickJoinButton() {
		registerButton.click();
	}

	public RegistrationPage join() throws Exception {
		termsOfServiceButton.click();
		registerButton.click();

		return new RegistrationPage(this.getDriver(), this.getWait());
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) throws Exception {
		String script = String.format(IOSLocators.scriptSignInEmailPath
				+ ".setValue(\"%s\")", login);
		driver.executeScript(script);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws Exception {
		String script = String.format(IOSLocators.scriptSignInPasswordPath
				+ ".setValue(\"%s\")", password);
		driver.executeScript(script);
	}

	public boolean waitForLogin() throws Exception {
		return DriverUtils.waitUntilElementDissapear(driver,
				By.name(IOSLocators.nameLoginField));
	}

	public Boolean isLoginFinished(String contact) throws Exception {
		try {
			this.getWait().until(ExpectedConditions.presenceOfElementLocated(By
					.name(contact)));
			this.getWait().until(ExpectedConditions.visibilityOfElementLocated(By
					.name(contact)));
		} catch (WebDriverException ex) {
			log.debug(ex.getMessage());
		}
		return DriverUtils.waitUntilElementAppears(driver, By.name(contact));
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// no need to swipe
		return null;
	}

	public Boolean isLoginButtonVisible() {

		return (ExpectedConditions.visibilityOf(signInButton) != null);
	}

	public void tapHoldEmailInput() {
		message = driver.findElement(By.name(IOSLocators.nameLoginField))
				.getText();
		this.getDriver().tap(1, this.getDriver().findElement(By.name(IOSLocators.nameLoginField)),
				1000);
	}

	public void openTermsLink() {
		Point p = termsButton.getLocation();
		Dimension k = termsButton.getSize();
		this.getDriver().tap(1, (p.x) + (k.width - 70), (p.y) + (k.height - 16), 1);
	}

	public void openPrivacyLink() {
		Point p = privacyButton.getLocation();
		Dimension k = privacyButton.getSize();
		this.getDriver().tap(1, (p.x) + (k.width / 3), (p.y) + (k.height - 8), 1);
	}

	public void closeTermsPrivacyController() {
		this.getWait().until(ExpectedConditions
				.elementToBeClickable(termsPrivacyCloseButton));
		termsPrivacyCloseButton.click();
	}

	public Boolean isTermsPrivacyColseButtonVisible() {

		return (ExpectedConditions.visibilityOf(termsPrivacyCloseButton) != null);
	}

	public void tapPasswordField() {
		passwordField.click();
	}

	public Boolean errorMailNotificationIsShown() {
		return DriverUtils.isElementDisplayed(errorMailNotification);
	}

	public Boolean errorMailNotificationIsNotShown() {
		return (ExpectedConditions.visibilityOf(errorMailNotification) == null);
	}

	public Boolean wrongCredentialsNotificationIsShown() {
		return DriverUtils.isElementDisplayed(wrongCredentialsNotification);
	}

	public void ignoreUpdate() throws Exception {
		DriverUtils.waitUntilElementAppears(driver,
				By.name(IOSLocators.nameIgnoreUpdateButton));
		ignoreUpdateButton.click();
	}
	
	public PersonalInfoPage tapChangePasswordButton() throws Exception{
		changePasswordButtonSignIn.click();
		return new PersonalInfoPage(this.getDriver(), this.getWait());
	}
	
	public void tapEmailFieldToChangePassword(String email) throws InterruptedException{
		for (WebElement textField : textFields){
			String valueOfField = textField.getAttribute("value");
			if(valueOfField.equals("Email")){
				DriverUtils.mobileTapByCoordinates(getDriver(), textField);
				this.inputStringFromKeyboard(email);
			}
		}
	}
	
	public void changeAppContext(){
		DriverUtils.changeAppContext(getDriver());
	}
	
	public void tapChangePasswordButtonInWebView(){
		for (WebElement uiButton : uiButtons){
			String nameOfButton = uiButton.getAttribute("name");
			if(nameOfButton.equals("CHANGE PASSWORD")){
				DriverUtils.mobileTapByCoordinates(getDriver(), uiButton);
			}
		}
	}
	
	public void changeURLInBrowser(String URL) throws InterruptedException{
		for (WebElement uiButton : uiButtons){
			String nameOfButton = uiButton.getAttribute("name");
			if(nameOfButton.equals("URL")){
				DriverUtils.mobileTapByCoordinates(getDriver(), uiButton);
				this.inputStringFromKeyboard(URL);
			}
			for (WebElement uiButton2 : uiButtons){
				String nameOfButton2 = uiButton2.getAttribute("name");
				if(nameOfButton2.equals("Go")){
					DriverUtils.mobileTapByCoordinates(getDriver(), uiButton2);
			}
		}
	  }
	}
}
