package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;

public final class PlatformDrivers {
	private static final Logger log = ZetaLogger.getLog(PlatformDrivers.class
			.getSimpleName());

	private HashMap<Platform, RemoteWebDriver> drivers = new HashMap<Platform, RemoteWebDriver>();

	private static PlatformDrivers instance = null;

	private PlatformDrivers() {
	}

	public static PlatformDrivers getInstance() {
		if (instance == null) {
			instance = new PlatformDrivers();
		}
		return instance;
	}

	public boolean hasDriver(Platform platform) {
		return this.drivers.containsKey(platform);
	}

	public synchronized RemoteWebDriver resetDriver(String url,
			DesiredCapabilities capabilities) throws Exception {
		final Platform platformInCapabilities = Platform
				.getByName((String) capabilities.getCapability("platformName"));
		if (this.hasDriver(platformInCapabilities)) {
			this.quitDriver(platformInCapabilities);
		}
		log.debug(String.format(
				"Creating driver instance for platform '%s'...",
				platformInCapabilities.name()));
		switch (platformInCapabilities) {
		case Mac:
			drivers.put(platformInCapabilities, new ZetaOSXDriver(new URL(url),
					capabilities));
			break;
		case iOS:
			drivers.put(platformInCapabilities, new ZetaIOSDriver(new URL(url),
					capabilities));
			break;
		case Android:
			drivers.put(platformInCapabilities, new ZetaAndroidDriver(new URL(
					url), capabilities));
			break;
		case Web:
			int tryNum = 0;
			final int maxTries = 3;
			WebDriverException savedException = null;
			do {
				// Try to reconnect WebDriver,
				// because sometimes Safari driver is non-responsive
				try {
					drivers.put(platformInCapabilities, new ZetaWebAppDriver(
							new URL(url), capabilities));
					break;
				} catch (WebDriverException e) {
					if (e.getMessage().contains("Failed to connect")) {
						savedException = e;
						tryNum++;
					} else {
						throw e;
					}
				}
			} while (tryNum < maxTries);
			if (tryNum >= maxTries) {
				throw savedException;
			}
			break;
		default:
			throw new RuntimeException(String.format(
					"Platform name '%s' is unknown", platformInCapabilities));
		}

		setDefaultImplicitWaitTimeout(platformInCapabilities);

		return this.getDriver(platformInCapabilities);
	}

	public void setImplicitWaitTimeout(Platform platform, int count,
			TimeUnit unit) {
		this.getDriver(platform).manage().timeouts()
				.implicitlyWait(count, unit);
	}

	public void setDefaultImplicitWaitTimeout(Platform platform)
			throws Exception {
		this.setImplicitWaitTimeout(platform, Integer.parseInt(CommonUtils
				.getDriverTimeoutFromConfig(getClass())), TimeUnit.SECONDS);
	}

	public RemoteWebDriver getDriver(Platform platform) {
		if (!drivers.containsKey(platform)) {
			throw new RuntimeException(String.format(
					"Please initialize %s platform driver first",
					platform.name()));
		}
		return drivers.get(platform);
	}

	public static WebDriverWait createDefaultExplicitWait(RemoteWebDriver driver)
			throws Exception {
		return new WebDriverWait(driver, Integer.parseInt(CommonUtils
				.getDriverTimeoutFromConfig(PlatformDrivers.class)));
	}

	public synchronized void quitDriver(Platform platform) {
		try {
			drivers.get(platform).quit();
			log.debug(String.format(
					"Successfully quit driver instance for platfrom '%s'",
					platform.name()));
		} finally {
			drivers.remove(platform);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		for (Entry<Platform, RemoteWebDriver> entry : drivers.entrySet()) {
			try {
				entry.getValue().quit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		super.finalize();
	}
}
