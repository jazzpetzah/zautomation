package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * This page appears when the user selects either the area code in front of the phone number,
 * or the country name above the phone number
 * @author deancook
 *
 */
public class AreaCodePage extends AndroidPage {
	
	public static final String idCodeName = "ttv_new_reg__signup__phone__country__row";
	
	public static final Function<String, String> xpathFindAreaCode = text -> String
		.format("//*[@id='" + idCodeName + "' and @value='%s']", text);
	
	public AreaCodePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public WelcomePage selectAreaCode(String codeName) throws Exception {
		this.getDriver().findElementByXPath(xpathFindAreaCode.apply(codeName)).click();
		return new WelcomePage(this.getLazyDriver());
	}

}
