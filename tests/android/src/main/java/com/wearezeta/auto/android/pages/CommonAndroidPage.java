package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import android.view.KeyEvent;

import com.wearezeta.auto.android.common.AndroidExecutionContext;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class CommonAndroidPage extends AndroidPage {

	public CommonAndroidPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@FindBy(id = AndroidLocators.Gmail.idSubject)
	private WebElement gmailSubject;

	@FindBy(id = AndroidLocators.Gmail.idBoby)
	private WebElement gmailContent;

	@FindBy(id = AndroidLocators.Browsers.idFirefoxUrlBar)
	private WebElement urlBar;

	@FindBy(id = AndroidLocators.Browsers.idFirefoxUrlBarEditText)
	private WebElement urlBarEditText;

	@FindBy(id = AndroidLocators.Browsers.idUrlBar)
	private WebElement chromeUrlBar;

	@FindBy(xpath = AndroidLocators.Browsers.ForgotPasswordPage.xpathEmailEditField)
	private WebElement emailEditField;

	@FindBy(xpath = AndroidLocators.Browsers.ForgotPasswordPage.xpathEnterNewPasswordEditField)
	private WebElement enterNewPasswordEditField;

	@FindBy(xpath = AndroidLocators.Browsers.ForgotPasswordPage.xpathChangePasswordButton)
	private WebElement changePassswordButton;

	@FindBy(xpath = AndroidLocators.Browsers.xpathChrome)
	private WebElement chromeBrowser;

	private static final String SERVER_URL = "https://staging-website.zinfra.io/forgot/";

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public PeoplePickerPage activateByLink(String link) throws Exception {
		openFirefoxBrowser();
		Thread.sleep(5000);
		setFirefoxBrowserURL(link);
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public void ConnectByInvitationLink(String link) throws Exception {
		openFirefoxBrowser();
		setFirefoxBrowserURL("wire://connect?code=" + link);
	}

	public void requestResetPassword(String email) throws Exception {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(emailEditField));
		setChromeBrowserURL(SERVER_URL);
		this.getWait().until(ExpectedConditions.visibilityOf(emailEditField));
		emailEditField.click();
		emailEditField.sendKeys(email);
		this.pressEnter();
	}

	public PeoplePickerPage resetByLink(String link, String newPass)
			throws Exception {
		setChromeBrowserURL(link);
		enterNewPasswordEditField.click();
		enterNewPasswordEditField.sendKeys(newPass);
		this.pressEnter();
		return null;
	}

	private void openFirefoxBrowser() throws Exception {
		this.getDriver().startActivity("org.mozilla.firefox",
				"org.mozilla.firefox.App", "org.mozilla.firefox",
				"org.mozilla.firefox.App");
	}

	private void setChromeBrowserURL(String link) throws Exception {
		if (AndroidExecutionContext.getOSVersion() < 43) {
			int ln = chromeUrlBar.getText().length();
			chromeUrlBar.click();
			for (int i = 0; i < ln; i++) {
				this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_DEL);
			}
		} else {
			chromeUrlBar.clear();
		}
		chromeUrlBar.sendKeys(link);
		this.pressEnter();
	}

	private void setFirefoxBrowserURL(String link) throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AndroidLocators.Browsers.idFirefoxUrlBar));
		urlBar.click();
		for (int i = 0; i < 10; i++) {
			this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_DEL);
		}
		urlBarEditText.sendKeys(link);
		this.pressEnter();
	}

	public String getGmailSubject() throws Exception {
		getWait().until(ExpectedConditions.visibilityOf(gmailSubject));
		return gmailSubject.getText();
	}

	public boolean mailContains(String email) {
		return gmailContent.getText().contains(email);
	}

}
