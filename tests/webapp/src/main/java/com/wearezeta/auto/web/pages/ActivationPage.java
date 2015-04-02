package com.wearezeta.auto.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ActivationPage extends WebPage {
	private String url;

	public ActivationPage(ZetaWebAppDriver driver, WebDriverWait wait,
			String url) throws Exception {
		super(driver, wait);
		this.url = url;
	}

	public void openInNewTab() {
		WebCommonUtils.openUrlInNewTab(
				PagesCollection.registrationPage.getDriver(), this.url);
	}

	public void verifyActivation(int timeoutSeconds) throws Exception {
		assert DriverUtils.isElementDisplayed(driver,
				By.xpath(WebAppLocators.ActivationPage.xpathSuccessfullResult),
				timeoutSeconds) : "It seems there was some failure while verifying registered account";
	}

	@Override
	public void close() throws Exception {
		WebCommonUtils.switchToPreviousTab(driver);
		super.close();
	}
}
