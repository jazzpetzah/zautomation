package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class SingleUserInfoPopoverPage extends AbstractUserInfoPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton)
	private WebElement blockButton;

	public SingleUserInfoPopoverPage(Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton;
	}

	private WebElement getAddButtonElement() {
		return this.getSharedElement(PopoverLocators.Shared.xpathAddButton);
	}

	public boolean isAddButtonVisible() {
		return getAddButtonElement().isDisplayed();
	}

	public boolean isBlockButtonVisible() {
		return blockButton.isDisplayed();
	}

	public void clickAddPeopleButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				getAddButtonElement());
		getAddButtonElement().click();
	}

	public void clickBlockButton() {
		blockButton.click();
	}
}
