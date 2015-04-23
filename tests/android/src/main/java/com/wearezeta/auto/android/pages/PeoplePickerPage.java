package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.common.KeyboardMapper;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class PeoplePickerPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearchUsers")
	private List<WebElement> pickerSearchUsers;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearchUsers")
	private WebElement pickerSearchUser;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerTopPeopleHeader")
	private WebElement pickerTopPeopleHeader;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerUserSlidingRow")
	private List<WebElement> pickerUserSlidingRow;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPeoplePickerSerchConversations")
	private List<WebElement> pickerSearchConversations;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPeoplePickerClearbtn")
	private WebElement pickerClearBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerRows")
	private List<WebElement> pickerSearchRows;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerUserHideMenu")
	private WebElement pickerUserHideMenu;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerUsersUnselected")
	private List<WebElement> pickerUsersUnselected;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearch")
	private WebElement pickerSearch;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearch")
	private List<WebElement> pickerSearchList;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerGrid")
	private WebElement pickerGrid;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerBtnDone")
	private WebElement addToConversationsButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idCreateConversationIcon")
	private WebElement createConversation;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idNoResultsFound")
	private WebElement noResults;

	@ZetaFindBy(how = ZetaHow.XPATH, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "xpathSendInvitationFrame")
	private WebElement sendInvitationFrame;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerListContainer")
	private WebElement content;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
	private List<WebElement> connectToHeader;

	@FindBy(xpath = AndroidLocators.PeoplePickerPage.xpathGmailLink)
	private WebElement gmailLink;

	public PeoplePickerPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void tapPeopleSearch() {
		pickerSearch.click();
	}

	public void tapOnContactInTopPeoples(String name) throws Exception {
		WebElement el = driver.findElement(By.xpath(String.format(
				AndroidLocators.PeoplePickerPage.xpathTopConversationContact,
				name.toUpperCase())));
		el.click();
	}

	public void typeTextInPeopleSearch(String contactName)
			throws InterruptedException {
		refreshUITree();
		// pickerSearch.sendKeys(contactName);
		for (char ch : contactName.toCharArray()) {
			int keyCode = KeyboardMapper.getPrimaryKeyCode(ch);
			this.getDriver().sendKeyEvent(keyCode);
		}
		this.getDriver().sendKeyEvent(66);
	}

	public void addTextToPeopleSearch(String contactName)
			throws InterruptedException {
		for (char ch : contactName.toCharArray()) {
			int keyCode = KeyboardMapper.getPrimaryKeyCode(ch);
			this.getDriver().sendKeyEvent(keyCode);
		}

	}

	public boolean isNoResultsFoundVisible() {

		refreshUITree();
		return noResults.isDisplayed();
	}

	public Boolean ispTopPeopleHeaderVisible() throws Exception {
		return isVisible(pickerTopPeopleHeader);
	}

	public AndroidPage selectContact(String contactName) throws Exception {
		AndroidPage page = null;
		refreshUITree();
		pickerSearchUser.click();
		/*
		 * if (CommonUtils.getAndroidApiLvl(PeoplePickerPage.class) > 42) {
		 * DriverUtils .waitUntilElementDissapear( driver,
		 * By.id(AndroidLocators.PeoplePickerPage.idPickerSearchUsers)); } else
		 * { DriverUtils.waitUntilElementDissapear(driver,
		 * By.xpath(AndroidLocators.PeoplePickerPage.xpathOtherText)); }
		 */
		refreshUITree();
		DriverUtils.turnOffImplicitWait(this.getDriver());
		if (driver.findElements(
				AndroidLocators.OtherUserPersonalInfoPage
						.getByForOtherUserPersonalInfoUnlockButton()).size() > 0) {
			page = new OtherUserPersonalInfoPage(this.getDriver(),
					this.getWait());
		} else if (driver.findElements(
				AndroidLocators.ConnectToPage.getByForConnectToPageHeader())
				.size() > 0) {
			page = new ConnectToPage(this.getDriver(), this.getWait());
		} else if (isVisible(addToConversationsButton)) {
			page = this;
		} else {
			page = new DialogPage(this.getDriver(), this.getWait());
		}
		DriverUtils.setDefaultImplicitWait(this.getDriver());
		return page;
	}

	public AndroidPage selectGroup(String contactName) throws Exception {
		AndroidPage page = null;
		WebElement el = driver.findElementByXPath(String.format(
				AndroidLocators.PeoplePickerPage.xpathPeoplePickerGroup,
				contactName));
		el.click();

		if (isVisible(addToConversationsButton)) {
			page = this;
		} else {
			page = new DialogPage(this.getDriver(), this.getWait());
		}
		return page;
	}

	@Override
	public AndroidPage swipeDown(int time) throws Exception {
		refreshUITree();
		elementSwipeDown(content, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		AndroidPage page = null;
		switch (direction) {
		case DOWN: {
			page = new ContactListPage(this.getDriver(), this.getWait());
			break;
		}
		case UP: {
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			break;
		}
		}
		return page;
	}

	public boolean isPeoplePickerPageVisible() throws InterruptedException,
			IOException {
		Boolean flag = false;
		refreshUITree();// TODO workaround
		try {
			this.getWait().until(ExpectedConditions.visibilityOf(pickerSearch));
		} catch (NoSuchElementException e) {
			return false;
		} catch (TimeoutException e) {
			return false;
		}
		if (pickerSearchList.size() > 0) {
			flag = true;
		}
		return flag;
	}

	public void waitUserPickerFindUser(String contactName)
			throws InterruptedException {
		for (int i = 0; i < 50; i++) {
			refreshUITree();
			List<WebElement> elements = pickerSearchUsers;
			for (WebElement element : elements) {
				try {
					if (element.getText().toLowerCase()
							.equals(contactName.toLowerCase())) {
						return;
					}
				} catch (Exception ex) {
					continue;
				}
			}
			Thread.sleep(100);
		}
	}

	public ContactListPage navigateBack() throws Exception {
		refreshUITree();
		// driver.navigate().back();
		pickerClearBtn.click();
		return new ContactListPage(this.getDriver(), this.getWait());
	}

	public boolean isAddToConversationBtnVisible() {
		return addToConversationsButton.isDisplayed();
	}

	public DialogPage clickOnAddToCoversationButton() throws Exception {
		driver.navigate().back();
		addToConversationsButton.click();
		return new DialogPage(this.getDriver(), this.getWait());
	}

	// TODO: move this to some base page

	public AndroidPage tapCreateConversation() throws Exception {
		refreshUITree();
		this.getWait().until(
				ExpectedConditions.visibilityOf(createConversation));
		try {
			this.getDriver().hideKeyboard();
		} catch (Exception ex) {

		}
		if (isVisible(createConversation)) {
			createConversation.click();
		}
		return new DialogPage(this.getDriver(), this.getWait());
	}

	public ContactListPage tapClearButton() throws Exception {
		refreshUITree();
		pickerClearBtn.click();
		// DriverUtils.waitUntilElementDissapear(driver,
		// By.id(AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn));
		refreshUITree();
		return new ContactListPage(this.getDriver(), this.getWait());
	}

	public boolean userIsVisible(String contact) throws Exception {
		DriverUtils.waitUntilElementDissapear(driver,
				By.id(AndroidLocators.PeoplePickerPage.idNoResultsFound));
		refreshUITree();
		this.getWait().until(
				ExpectedConditions.visibilityOfAllElements(pickerSearchUsers));
		return isVisible(driver.findElement(By.xpath(String.format(
				AndroidLocators.PeoplePickerPage.xpathPeoplePickerContact,
				contact))));
	}

	public boolean groupIsVisible(String contact) throws Exception {
		DriverUtils.waitUntilElementDissapear(driver,
				By.id(AndroidLocators.PeoplePickerPage.idNoResultsFound));
		refreshUITree();
		this.getWait().until(
				ExpectedConditions
						.visibilityOfAllElements(pickerSearchConversations));
		return isVisible(driver.findElement(By.xpath(String.format(
				AndroidLocators.PeoplePickerPage.xpathPeoplePickerGroup,
				contact))));
	}

	public PeoplePickerPage selectContactByLongTap(String contact)
			throws Exception {
		refreshUITree();
		DriverUtils.waitUntilElementDissapear(driver,
				By.id(AndroidLocators.PeoplePickerPage.idNoResultsFound));
		refreshUITree();
		WebElement el = driver.findElementByXPath(String.format(
				AndroidLocators.PeoplePickerPage.xpathPeoplePickerContact,
				contact));
		DriverUtils.androidLongClick(this.getDriver(), el);
		return this;
	}

	public void tapOnSendInvitation() {
		sendInvitationFrame.click();
	}

	public CommonAndroidPage tapOnGmailLink() throws NumberFormatException,
			Exception {
		if (!isVisible(gmailLink)) {
			DriverUtils
					.swipeUp(
							this.getDriver(),
							this.getDriver()
									.findElementByXPath(
											AndroidLocators.PeoplePickerPage.xpathDestinationFrame),
							500, 50, 50);
			this.getWait().until(
					ExpectedConditions.elementToBeClickable(gmailLink));
		}
		gmailLink.click();
		return new CommonAndroidPage(this.getDriver(), this.getWait());
	}

	public WebElement selectRandomConnect() {
		refreshUITree();
		Random rand = new Random();
		int n = rand.nextInt(pickerUserSlidingRow.size() - 1);
		return pickerUserSlidingRow.get(n);
	}

	public String pressPlusOnContact(WebElement randomConnect) {
		refreshUITree();
		String name = randomConnect.findElement(By.id(AndroidLocators.PeoplePickerPage.idPickerRecomendedName)).getText();
		randomConnect.findElement(By.id(AndroidLocators.PeoplePickerPage.idPickerRecomendedQuickAdd)).click();
		return name;
	}
	public String swipePYMKContact(WebElement randomConnect) {
		refreshUITree();
		WebElement element =  randomConnect.findElement(By.id(AndroidLocators.PeoplePickerPage.idPickerRecomendedName));
		String name = element.getText();
		DriverUtils.swipeRight(getDriver(), randomConnect, 1000, 90, 50);
		return name;
	}

	public void clickPYMKHideButton() {
		refreshUITree();
		 WebElement hideButton = pickerUserHideMenu.findElement(By.className("android.widget.TextView"));
		hideButton.click();
	}
	
	public boolean pYMKcontactIsVisible(String randomConnectName, WebElement element) {
		refreshUITree();
		return  (element.findElement(By.id(AndroidLocators.PeoplePickerPage.idPickerRecomendedName)).getText()).equals(randomConnectName);
	}
}
