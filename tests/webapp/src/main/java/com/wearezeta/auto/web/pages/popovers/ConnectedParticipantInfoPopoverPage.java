package com.wearezeta.auto.web.pages.popovers;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

class ConnectedParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {
	public ConnectedParticipantInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container) throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected String getXpathLocator() {
		throw new NotImplementedException("");
	}
}
