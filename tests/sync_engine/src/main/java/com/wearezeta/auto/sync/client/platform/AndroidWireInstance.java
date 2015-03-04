package com.wearezeta.auto.sync.client.platform;

import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.CommonAndroidSteps;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.CommonSteps;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;
import com.wearezeta.auto.sync.client.WireInstance;
import com.wearezeta.auto.sync.client.listener.AndroidListener;
import com.wearezeta.auto.sync.client.sender.AndroidSender;

public class AndroidWireInstance extends WireInstance {

	private static final Logger log = ZetaLogger
			.getLog(AndroidWireInstance.class.getSimpleName());

	public AndroidWireInstance() throws Exception {
		super(Platform.Android);
	}

	@Override
	public void createSender() {
		sender = new AndroidSender(this, messagesToSend);

	}

	@Override
	public void createListener() {
		listener = new AndroidListener(this);
	}

	@Override
	public void readPlatformSettings() throws Exception {
		try {
			enabled = SyncEngineUtil
					.getAndroidClientEnabledFromConfig(ExecutionContext.class);
		} catch (Exception e) {
			enabled = true;
			log.warn("Failed to read property android.client.enabled "
					+ "from config file. Set to 'true' by default");
		}

		try {
			sendToBackend = SyncEngineUtil
					.getAndroidBackendSenderFromConfig(this.getClass());
		} catch (Exception e) {
			sendToBackend = false;
			log.warn("Failed to read property android.backend.sender "
					+ "from config file. Set to 'false' by default");
		}

		wirePath = CommonUtils.getAndroidApplicationPathFromConfig(this
				.getClass());

		appiumUrl = CommonUtils.getAndroidAppiumUrlFromConfig(this.getClass());
	}

	@Override
	public void closeAndClearImpl() throws Exception {
		PagesCollection.loginPage.close();
		AndroidPage.clearPagesCollection();
	}

	private void disableHints() {
		try {
			AndroidCommonUtils.disableHints();
		} catch (Exception e) {
			log.warn("Failed to disable hints on Android client.\n"
					+ e.getMessage());
		}
	}

	@Override
	public void startClientProcedureImpl() {
		disableHints();
		if (PagesCollection.loginPage == null) {
			CommonAndroidSteps androidSteps = new CommonAndroidSteps();
			long startDate = new Date().getTime();
			long endDate = 0;
			try {
				androidSteps.commonBefore();
				final ZetaAndroidDriver driver = androidSteps
						.resetAndroidDriver(appiumUrl, wirePath, false,
								this.getClass());
				final WebDriverWait wait = PlatformDrivers
						.createDefaultExplicitWait(driver);
				PagesCollection.loginPage = new LoginPage(driver, wait);
				endDate = new Date().getTime();
				ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
			} catch (Exception e) {
				log.debug("Failed to start Android client. Error message: "
						+ e.getMessage());
				e.printStackTrace();
			}

			try {
				startDate = SyncEngineUtil
						.readDateFromAppiumLog(AndroidCommonUtils
								.getAndroidAppiumLogPathFromConfig(CommonSteps.class));
			} catch (Exception e) {
				log.error("Failed to read Android application startup time from Appium log.\n"
						+ "Approximate value will be used. " + e.getMessage());
			}
			startupTime = endDate - startDate;

			log.debug("Android application startup time: " + startupTime + "ms");
		}
	}
}
