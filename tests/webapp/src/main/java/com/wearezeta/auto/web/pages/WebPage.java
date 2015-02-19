package com.wearezeta.auto.web.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebCommonUtils;

public class WebPage extends BasePage {
	protected static ZetaWebAppDriver driver;
	protected static WebDriverWait wait;

	public WebPage(String URL, String path) throws Exception {
		this(URL, path, false);
	}

	public WebPage(String URL, String path, boolean doNavigate)
			throws Exception {

		final String browser = WebCommonUtils
				.getWebAppBrowserNameFromConfig(WebPage.class);

		DesiredCapabilities capabilities;

		switch (browser) {
		case "chrome":
			capabilities = DesiredCapabilities.chrome();
			break;
		case "firefox":
			capabilities = DesiredCapabilities.firefox();
			break;
		case "safari":
			capabilities = DesiredCapabilities.safari();
			break;
		case "ie":
			capabilities = DesiredCapabilities.internetExplorer();
			break;
		default:
			throw new NotImplementedException(
					"Incorrect browser name is set - "
							+ browser
							+ ". Please choose one of the following: chrome | firefox | safari | ie");
		}
		final String webPlatformName = WebCommonUtils
				.getPlatformNameFromConfig(WebPage.class);
		if (webPlatformName.length() > 0) {
			// Use undocumented grid property to match platforms
			// https://groups.google.com/forum/#!topic/selenium-users/PRsEBcbpNlM
			capabilities.setCapability("applicationName", webPlatformName);
		}
		capabilities.setCapability("platformName", Platform.Web.getName());

		super.InitConnection(URL, capabilities);

		driver = (ZetaWebAppDriver) drivers.get(Platform.Web);
		wait = waits.get(Platform.Web);

		driver.setFileDetector(new LocalFileDetector());
		try {
			driver.manage().window().maximize();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (doNavigate) {
			// After beta code is applied we should wait till sign in page
			// pointed to production backend will be loaded before loading
			// staging page
			Thread.sleep(5000);
			driver.navigate().to(path);
		}
	}

	@Override
	public void close() throws Exception {
		super.close();
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot(driver);
	}

	public static void clearPagesCollection() throws IllegalArgumentException,
			IllegalAccessException {
		clearPagesCollection(PagesCollection.class, WebPage.class);
	}

	// not used in WebApp
	@Override
	public BasePage swipeLeft(int time) throws IOException {
		return null;
	}

	@Override
	public BasePage swipeRight(int time) throws IOException {
		return null;
	}

	@Override
	public BasePage swipeUp(int time) throws IOException {
		return null;
	}

	@Override
	public BasePage swipeDown(int time) throws IOException {
		return null;
	}

	public RemoteWebDriver getDriver() {
		return driver;
	}
}
