package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.List;

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

	private String login;

	private String password;

	public String message;

	public LoginPage(ZetaIOSDriver driver, WebDriverWait wait) throws Exception {
		super(driver, wait);
	}

	public Boolean isVisible() {
		return viewPager != null;
	}

	public IOSPage signIn() throws IOException {

		signInButton.click();
		return this;
	}

	public void waitForLaterButton(int time) throws Exception {
		DriverUtils.waitUntilElementAppears(getDriver(),
				By.name(IOSLocators.nameShareButton), time);
	}

	public PeoplePickerPage clickLaterButton() throws Exception {
		if (DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameShareButton))) {
			shareButton.click();
			return new PeoplePickerPage(this.getDriver(), this.getWait());
		} else {
			return null;
		}
	}

	public boolean isSelfProfileVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameProfileName));
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
		try {
			driver.executeScript(script);
		} catch (WebDriverException ex) {
			log.debug("fucking appium! " + ex.getMessage());
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws Exception {
		String script = String.format(IOSLocators.scriptSignInPasswordPath
				+ ".setValue(\"%s\")", password);
		try {
			driver.executeScript(script);
		} catch (WebDriverException ex) {
			log.debug("fucking web appium! " + ex.getMessage());
		}
	}

	public boolean waitForLogin() throws Exception {
		return DriverUtils.waitUntilElementDissapear(driver,
				By.name(IOSLocators.nameLoginField));
	}

	public Boolean isLoginFinished(String contact) throws Exception {
		try {
			this.getWait().until(
					ExpectedConditions.presenceOfElementLocated(By
							.name(contact)));
			this.getWait().until(
					ExpectedConditions.visibilityOfElementLocated(By
							.name(contact)));
		} catch (WebDriverException ex) {
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
		this.getDriver().tap(
				1,
				this.getDriver().findElement(
						By.name(IOSLocators.nameLoginField)), 1000);
	}

	public void openTermsLink() {
		Point p = termsButton.getLocation();
		Dimension k = termsButton.getSize();
		this.getDriver().tap(1, (p.x) + (k.width - 70),
				(p.y) + (k.height - 16), 1);
	}

	public void openPrivacyLink() {
		Point p = privacyButton.getLocation();
		Dimension k = privacyButton.getSize();
		this.getDriver().tap(1, (p.x) + (k.width / 3), (p.y) + (k.height - 8),
				1);
	}

	public void closeTermsPrivacyController() {
		this.getWait().until(
				ExpectedConditions
						.elementToBeClickable(termsPrivacyCloseButton));
		termsPrivacyCloseButton.click();
	}

	public Boolean isTermsPrivacyColseButtonVisible() {

		return (ExpectedConditions.visibilityOf(termsPrivacyCloseButton) != null);
	}

	public void tapPasswordField() {
		passwordField.click();
	}

	public Boolean errorMailNotificationIsShown() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameErrorMailNotification));
	}

	public Boolean errorMailNotificationIsNotShown() {
		return (ExpectedConditions.visibilityOf(errorMailNotification) == null);
	}

	public Boolean wrongCredentialsNotificationIsShown() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameWrongCredentialsNotification));
	}

	public void ignoreUpdate() throws Exception {
		DriverUtils.waitUntilElementAppears(driver,
				By.name(IOSLocators.nameIgnoreUpdateButton));
		ignoreUpdateButton.click();
	}
}
