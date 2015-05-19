package com.wearezeta.auto.web.pages.popovers;

import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class AddPeoplePopoverPage extends AbstractPopoverPage {
	public AddPeoplePopoverPage(Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return String.format("%s%s", this.getContainer().getXpathLocator(),
				PopoverLocators.Shared.xpathSearchInputField);
	}

	private WebElement getSearchFieldElement() throws Exception {
		return this
				.getSharedElement(PopoverLocators.Shared.xpathSearchInputField);
	}

	public void searchForUser(String searchText) throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				getSearchFieldElement());
		getSearchFieldElement().clear();
		getSearchFieldElement().sendKeys(searchText);
	}

	private WebElement getCreateGroupConversationButton() throws Exception {
		return this
				.getSharedElement(PopoverLocators.Shared.xpathCreateConversationButton);
	}

	public void clickCreateGroupConversation() throws Exception {
		final WebElement createGroupConversationButton = getCreateGroupConversationButton();
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				createGroupConversationButton) : "Create Group Convesation button is not clickable after timeout";
		createGroupConversationButton.click();
	}

	private WebElement getFoundItemElement(String name) throws Exception {
		return this
				.getSharedElement(PopoverLocators.Shared.xpathSearchResultByName
						.apply(name));
	}

	public void selectUserFromSearchResult(String name) throws Exception {
		this.getFoundItemElement(name).click();
	}

	public void selectUsersFromSearchResult(int amount) throws Exception {
		List<WebElement> elements = getDriver().findElements(
				By.cssSelector(PopoverLocators.Shared.cssSearchResultItems));
		for (int i = 0; i < amount; i++) {
			elements.get(i).click();
		}
	}
}
