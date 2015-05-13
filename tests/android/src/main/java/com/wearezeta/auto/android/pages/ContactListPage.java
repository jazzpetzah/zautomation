package com.wearezeta.auto.android.pages;

import java.util.*;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.locators.ZetaHow;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.log.ZetaLogger;

public class ContactListPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPeoplePickerClearbtn")
	private WebElement pickerClearBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConversationListFrame")
	private WebElement contactListFrame;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idMissedCallIcon")
	private WebElement missedCallIcon;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idContactListNames")
	private List<WebElement> contactListNames;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classEditText)
	private WebElement cursorInput;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameFrameLayout)
	private List<WebElement> frameLayout;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idProfileOptionsButton")
	private WebElement laterButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idOpenStartUIButton")
	private WebElement openStartUIButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idNameField")
	private WebElement selfUserName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idSelfUserAvatar")
	protected WebElement selfUserAvatar;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConfirmCancelButton")
	private List<WebElement> laterBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConfirmCancelButtonPicker")
	private List<WebElement> laterBtnPicker;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idPlayPauseMedia")
	private WebElement playPauseMedia;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idMutedIcon")
	private WebElement mutedIcon;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConvList")
	private WebElement convList;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement mainControl;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
	private WebElement connectToHeader;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idSearchHintClose")
	private WebElement closeHintBtn;

	@FindBy(xpath = AndroidLocators.CommonLocators.xpathGalleryCameraAlbum)
	private WebElement galleryCameraAlbumButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idConversationSendOption")
	private WebElement conversationShareOption;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idConfirmBtn")
	private WebElement confirmShareButton;

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	public ContactListPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public AndroidPage tapOnName(String name) throws Exception {
		AndroidPage page = null;
		WebElement el = findInContactList(name, 5);
		this.getWait().until(ExpectedConditions.visibilityOf(el));
		el.click();
		page = getPages();
		// workaround for incorrect tap
		if (page == null) {
			el = findInContactList(name, 1);
			if (el != null && DriverUtils.isElementPresentAndDisplayed(el)) {
				this.restoreApplication();
				el.click();
				log.debug("tap on contact for the second time");
			}
		}
		return page;
	}

	public void contactListSwipeUp(int time) {
		elementSwipeUp(contactListFrame, time);
	}

	public void waitForConversationListLoad() throws Exception {
		getWait().until(ExpectedConditions.visibilityOf(contactListFrame));
	}

	public AndroidPage tapOnContactByPosition(List<WebElement> contacts, int id)
			throws Exception {
		AndroidPage page = null;
		contacts.get(id).click();
		page = new DialogPage(this.getLazyDriver());
		return page;
	}

	public List<WebElement> GetVisibleContacts() throws Exception {
		return contactListNames;
	}

	public WebElement findInContactList(String name, int cyclesNumber)
			throws Exception {
		WebElement contact = null;
		Boolean flag = false;
		if (CommonUtils.getAndroidApiLvl(ContactListPage.class) > 42) {
			if (DriverUtils.isElementPresentAndDisplayed(convList)) {
				flag = true;
			}
		} else if (DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.className(AndroidLocators.CommonLocators.classEditText))
				&& DriverUtils.waitUntilLocatorDissapears(getDriver(),
						By.id(AndroidLocators.PersonalInfoPage.idNameField))) {
			flag = true;
		}
		if (flag) {
			List<WebElement> contactsList = this
					.getDriver()
					.findElements(
							By.xpath(String
									.format(AndroidLocators.ContactListPage.xpathContacts,
											name)));
			if (contactsList.size() > 0) {
				contact = contactsList.get(0);
			} else {
				if (cyclesNumber > 0) {
					cyclesNumber--;
					DriverUtils.swipeUp(this.getDriver(), mainControl, 500);
					contact = findInContactList(name, cyclesNumber);
				}
			}
		}
		return contact;
	}

	public AndroidPage swipeRightOnContact(int time, String contact)
			throws Exception {
		AndroidPage page = null;
		WebElement el = this.getDriver().findElementByXPath(
				String.format(
						AndroidLocators.ContactListPage.xpathContactFrame,
						contact));
		elementSwipeRight(el, time);
		if (DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.className(AndroidLocators.CommonLocators.classEditText))) {
			page = new ContactListPage(this.getLazyDriver());
		} else if (DriverUtils.isElementPresentAndDisplayed(cursorInput)) {
			page = new DialogPage(this.getLazyDriver());
		}
		return page;
	}

	public AndroidPage swipeOnArchiveUnarchive(String contact) throws Exception {
		WebElement el = getDriver()
				.findElementByXPath(
						String.format(
								AndroidLocators.ContactListPage.xpathContactListArchiveUnarchive,
								contact));
		DriverUtils.swipeRight(this.getDriver(), el, 1000);
		AndroidPage page = null;
		if (DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.className(AndroidLocators.CommonLocators.classEditText))) {
			page = new ContactListPage(this.getLazyDriver());
		} else if (DriverUtils.isElementPresentAndDisplayed(cursorInput)) {
			page = new DialogPage(this.getLazyDriver());
		}
		return page;
	}

	public boolean isContactMuted() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.id(AndroidLocators.ContactListPage.idMutedIcon));
	}

	public boolean isHintVisible() throws Exception {
		try {
			this.getWait().until(
					ExpectedConditions.elementToBeClickable(closeHintBtn));
		} catch (NoSuchElementException e) {
			return false;
		} catch (TimeoutException e) {
			return false;
		}
		return closeHintBtn.isEnabled();
	}

	public void closeHint() {
		closeHintBtn.click();
	}

	@Override
	public AndroidPage swipeDown(int time) throws Exception {
		elementSwipeDown(contactListFrame, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public PeoplePickerPage pressOpenStartUIButton() throws Exception {
		openStartUIButton.click();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {

		AndroidPage page = null;
		switch (direction) {
		case DOWN: {
			page = new PeoplePickerPage(this.getLazyDriver());
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

	public ContactListPage pressLaterButton() throws Exception {
		/*
		 * try {
		 * wait.until(ExpectedConditions.elementToBeClickable(laterButton)); }
		 * catch (NoSuchElementException e) {
		 * 
		 * } catch (TimeoutException e) {
		 * 
		 * }
		 */
		// DriverUtils.waitUntilElementDissapear(driver,
		// By.id(AndroidLocators.PersonalInfoPage.idProfileOptionsButton));

		if (laterBtn.size() > 0) {
			laterBtn.get(0).click();
		} else if (laterBtnPicker.size() > 0) {
			laterBtnPicker.get(0).click();
		}

		DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.ContactListPage.idSimpleDialogPageText));
		// TODO: we need this as sometimes we see people picker after login
		PagesCollection.peoplePickerPage = new PeoplePickerPage(
				this.getLazyDriver());
		return this;
	}

	public Boolean isContactExists(String name) throws Exception {
		return findInContactList(name, 0) != null;
	}

	public Boolean isContactExists(String name, int cycles) throws Exception {
		return findInContactList(name, cycles) != null;
	}

	private AndroidPage getPages() throws Exception {
		AndroidPage page = null;
		if (DriverUtils.isElementPresentAndDisplayed(connectToHeader)) {
			page = new ConnectToPage(this.getLazyDriver());
		} else if (DriverUtils.isElementPresentAndDisplayed(selfUserName)) {
			page = new PersonalInfoPage(this.getLazyDriver());
		} else if (DriverUtils.isElementPresentAndDisplayed(cursorInput)) {
			page = new DialogPage(this.getLazyDriver());
		}

		return page;
	}

	public boolean isPlayPauseMediaButtonVisible()
			throws NumberFormatException, Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AndroidLocators.ContactListPage.idPlayPauseMedia));
		return DriverUtils.isElementPresentAndDisplayed(playPauseMedia);
	}

	public void waitForContactListLoadFinished() throws InterruptedException {
		if (contactListNames.size() > 0) {
			waitForContacListLoading();
		}
	}

	private void waitForContacListLoading() throws InterruptedException {
		for (WebElement contact : contactListNames) {
			if (contact.getText().contains("…")) {
				Thread.sleep(500);
				waitForContacListLoading();
			}
		}
	}

	public boolean isVisibleMissedCallIcon() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(missedCallIcon);
	}

	public void shareImageToWireFromGallery() throws Exception {
		galleryCameraAlbumButton.click();
		List<WebElement> galleryImageViews = this.getDriver()
				.findElementsByClassName("android.widget.ImageView");
		for (WebElement imageView : galleryImageViews) {
			if (imageView.getAttribute("name").equals("Share with")) {
				imageView.click();
			}
		}
		List<WebElement> textViewElements = this.getDriver()
				.findElementsByClassName("android.widget.TextView");
		for (WebElement textView : textViewElements) {
			if (textView.getAttribute("text").equals("See all")) {
				textView.click();
			}
		}
		// find elements again
		textViewElements = this.getDriver().findElementsByClassName(
				"android.widget.TextView");
		for (WebElement textView : textViewElements) {
			if (textView.getAttribute("text").equals("Wire")) {
				textView.click();
			}
		}
		conversationShareOption.click();
		confirmShareButton.click();
	}

	public void shareURLFromNativeBrowser() throws Exception {
		List<WebElement> imageButtonElements = this.getDriver()
				.findElementsByClassName(
						AndroidLocators.Browsers.nameNativeBrowserMenuButton);
		for (WebElement imageButton : imageButtonElements) {
			if (imageButton.getAttribute("name").equals("More options")) {
				imageButton.click();
			}
		}
		List<WebElement> textViewElements = this
				.getDriver()
				.findElementsByClassName(
						AndroidLocators.Browsers.nameNativeBrowserMoreOptionsButton);
		for (WebElement textView : textViewElements) {
			if (textView.getAttribute("text").equals("Share page")) {
				textView.click();
				break;
			}
		}
		List<WebElement> textElements = this
				.getDriver()
				.findElementsByClassName(
						AndroidLocators.Browsers.nameNativeBrowserShareWireButton);
		for (WebElement textView : textElements) {
			if (textView.getAttribute("text").equals("Wire")) {
				textView.click();
			}
		}
		conversationShareOption.click();
	}

	public PersonalInfoPage tapOnMyAvatar() throws Exception {
		selfUserAvatar.click();
		return new PersonalInfoPage(getLazyDriver());
	}

}
