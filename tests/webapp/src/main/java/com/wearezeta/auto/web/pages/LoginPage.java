package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebAppConstants.Browser;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.external.PasswordChangeRequestPage;

public class LoginPage extends WebPage {
	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathCreateAccountButton)
	private WebElement createAccountButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathChangePasswordButton)
	private WebElement changePasswordButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathEmailInput)
	private WebElement emailInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathPasswordInput)
	private WebElement passwordInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathLoginErrorText)
	private WebElement loginErrorText;

	@FindBy(css = ".auth-page .has-error .form-control #wire-email")
	private WebElement redDotOnEmailField;

	@FindBy(css = ".auth-page .has-error .form-control #wire-password")
	private WebElement redDotOnPasswordField;

	public LoginPage(Future<ZetaWebAppDriver> lazyDriver, String url)
			throws Exception {
		super(lazyDriver, url);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(WebAppLocators.LoginPage.xpathLoginPage));
	}

	public void inputEmail(String email) {
		emailInput.clear();
		emailInput.sendKeys(email);
	}

	public void inputPassword(String password) {
		passwordInput.clear();
		passwordInput.sendKeys(password);
	}

	private boolean waitForLoginButtonDisappearance() throws Exception {
		// workarounds for IE driver bugs:
		// 1. when findElements() returns one RemoteWebElement instead of list
		// of elements and throws WebDriverException
		// 2. NPE when findElements() call
		boolean noSignIn = false;
		try {
			noSignIn = DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
					By.xpath(WebAppLocators.LoginPage.xpathSignInButton), 60);
		} catch (WebDriverException e) {
			if (WebAppExecutionContext.getCurrentBrowser() == Browser.InternetExplorer) {
				noSignIn = true;
			} else {
				throw e;
			}
		}
		return noSignIn;
	}

	public boolean waitForLogin() throws Exception {
		boolean noSignIn = waitForLoginButtonDisappearance();
		boolean noSignInSpinner = DriverUtils.waitUntilLocatorDissapears(
				this.getDriver(),
				By.className(WebAppLocators.LoginPage.classNameSpinner), 40);
		return noSignIn && noSignInSpinner;
	}

	public ContactListPage clickSignInButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				signInButton);
		signInButton.click();

		return new ContactListPage(this.getLazyDriver());
	}

	public PasswordChangeRequestPage clickChangePasswordButton()
			throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				changePasswordButton);

		// TODO: This is commented because the button always redirects to
		// production site and we usually need staging one
		// changePasswordButton.click();
		final PasswordChangeRequestPage changePasswordPage = new PasswordChangeRequestPage(
				this.getLazyDriver());
		changePasswordPage.navigateTo();
		return changePasswordPage;
	}

	public RegistrationPage switchToRegistrationPage() throws Exception {
		this.getDriver().navigate().to(this.getUrl());
		DriverUtils.retryOnWhitePage(this.getDriver(), this.getUrl());

		final By locator = By
				.xpath(WebAppLocators.LoginPage.xpathSwitchToRegisterButtons);
		if (DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator, 2)) {
			for (WebElement btn : getDriver().findElements(locator)) {
				if (btn.isDisplayed()) {
					btn.click();
					break;
				}
			}
		}
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(WebAppLocators.RegistrationPage.xpathRootForm)) : "Registration page is not visible";

		return new RegistrationPage(this.getLazyDriver(), this.getUrl());
	}

	public String getErrorMessage() {
		return loginErrorText.getText();
	}

	public boolean isRedDotOnEmailField() {
		return DriverUtils.isElementPresentAndDisplayed(redDotOnEmailField);
	}

	public boolean isRedDotOnPasswordField() {
		return DriverUtils.isElementPresentAndDisplayed(redDotOnPasswordField);
	}

}
