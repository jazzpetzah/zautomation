package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class RegistrationPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(RegistrationPage.class
			.getSimpleName());

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssNameFiled)
	private WebElement nameField;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssEmailFiled)
	private WebElement emailField;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssPasswordFiled)
	private WebElement passwordField;

	@FindBy(how = How.ID, using = WebAppLocators.RegistrationPage.idCreateAccountButton)
	private WebElement createAccount;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssVerificationEmail)
	private WebElement verificationEmail;

	private static final int MAX_TRIES = 3;

	public RegistrationPage(ZetaWebAppDriver driver, WebDriverWait wait,
			String url) throws Exception {
		super(driver, wait, url);
	}

	@Override
	public void navigateTo() {
		super.navigateTo();
		WebCommonUtils.forceLogoutFromWebapp(getDriver(), true);

		// FIXME: I'm not sure whether white page instead of sign in is
		// Amazon issue or webapp issue,
		// but since this happens randomly in different browsers, then I can
		// assume this issue has something to do to the hosting and/or
		// Selenium driver
		int ntry = 0;
		while (ntry < MAX_TRIES) {
			try {
				if (!(DriverUtils
						.isElementDisplayed(
								this.getDriver(),
								By.xpath(WebAppLocators.LandingPage.xpathSwitchToSignInButton)))) {
					log.error(String
							.format("Landing page has failed to load. Trying to refresh (%s of %s)...",
									ntry + 1, MAX_TRIES));
					driver.navigate().to(driver.getCurrentUrl());
				} else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ntry++;
		}
	}

	public LoginPage switchToLoginPage() throws Exception {
		WebCommonUtils.forceLogoutFromWebapp(getDriver(), true);
		final By signInBtnlocator = By
				.xpath(WebAppLocators.LoginPage.xpathSignInButton);
		final By switchtoSignInBtnlocator = By
				.xpath(WebAppLocators.LandingPage.xpathSwitchToSignInButton);
		int ntry = 0;
		// FIXME: temporary workaround for white page instead of landing issue
		while (ntry < MAX_TRIES) {
			try {
				if (DriverUtils.isElementDisplayed(this.getDriver(),
						switchtoSignInBtnlocator)) {
					driver.findElement(switchtoSignInBtnlocator).click();
				}
				if (DriverUtils.isElementDisplayed(this.getDriver(),
						signInBtnlocator)) {
					break;
				} else {
					log.debug(String
							.format("Trying to refresh currupted login page (retry %s of %s)...",
									ntry + 1, MAX_TRIES));
					driver.navigate().to(driver.getCurrentUrl());
				}
			} catch (Exception e) {
				driver.navigate().to(driver.getCurrentUrl());
			}
			ntry++;
		}
		assert DriverUtils.isElementDisplayed(this.getDriver(),
				signInBtnlocator) : "Sign in page is not visible";

		return new LoginPage(this.getDriver(), this.getWait());
	}

	private void removeReadonlyAttr(String cssLocator) {
		driver.executeScript(String.format(
				"$(document).find(\"%s\").removeAttr('readonly');", cssLocator));
	}

	public void enterName(String name) {
		removeReadonlyAttr(WebAppLocators.RegistrationPage.cssNameFiled);
		nameField.clear();
		nameField.sendKeys(name);
	}

	public void enterEmail(String email) {
		removeReadonlyAttr(WebAppLocators.RegistrationPage.cssEmailFiled);
		emailField.clear();
		emailField.sendKeys(email);
	}

	public void enterPassword(String password) {
		removeReadonlyAttr(WebAppLocators.RegistrationPage.cssPasswordFiled);
		passwordField.clear();
		passwordField.sendKeys(password);
	}

	public void submitRegistration() throws Exception {
		assert DriverUtils.waitUntilElementClickable(driver, createAccount) : "'Create Account' button is not clickable after timeout";
		createAccount.click();
	}

	public boolean isVerificationEmailCorrect(String email) {
		return verificationEmail.getText().equalsIgnoreCase(email);
	}
}
