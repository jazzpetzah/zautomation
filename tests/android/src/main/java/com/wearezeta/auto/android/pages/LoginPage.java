package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class LoginPage extends AndroidPage {

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idSignInButton")
	private WebElement signInButton;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idWelcomeSlogan")
	private WebElement welcomeSlogan;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idSignUpButton")
	private WebElement signUpButton;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idLoginButton")
	private WebElement confirmSignInButton;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idLoginPasswordInput")
	private List<WebElement> loginPasswordField;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idWelcomeButtonsContainer")
	private List<WebElement> welcomeButtonsContainer;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idContactListNames")
	private WebElement contactListNames;

	private String login;
	private String password;
	private String url;
	private String path;

	public LoginPage(String URL, String path) throws Exception {

		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public LoginPage(String URL, String path, boolean isUnicode) throws Exception {

		super(URL, path, isUnicode);
		this.url = URL;
		this.path = path;
	}
	
	
	public RemoteWebDriver getDriver() {
		return driver;
	}

	public Boolean isVisible() {

		return welcomeSlogan != null;
	}

	public LoginPage SignIn() throws IOException {

		signInButton.click();
		return this;
	}

	public ContactListPage LogIn() throws Exception {
		confirmSignInButton.click();
		return new ContactListPage(url, path);
	}
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {

		for(WebElement el: loginPasswordField){
			if(el.getText().equals("Email")){
				el.click();
				el.sendKeys(login);
				break;
			}
		}
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws InterruptedException {

		for(WebElement el: loginPasswordField){
			if(el.getText() == null || el.getText().isEmpty() ){
				el.click();
				el.sendKeys(password);
				break;
			}
		}
	}

	public boolean waitForLogin() {

		return DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.LoginPage.idLoginProgressBar));
	}

	public Boolean isLoginFinished(String contact) throws InterruptedException {
		refreshUITree();
		return DriverUtils.waitForElementWithTextByXPath(AndroidLocators.ContactListPage.xpathContacts,contact,driver);
	}

	public Boolean isWelcomeButtonsExist(){
		return welcomeButtonsContainer.size() > 0;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) {
		// no need to swipe
		return null;
	}

	public RegistrationPage join() throws Exception {
		signUpButton.click();

		return new RegistrationPage(url, path);
	}
}
