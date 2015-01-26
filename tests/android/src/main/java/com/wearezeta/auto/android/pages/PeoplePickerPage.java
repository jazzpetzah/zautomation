package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.common.KeyboardMapper;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class PeoplePickerPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearchUsers")
	private List<WebElement> pickerSearchUsers;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearchUsers")
	private WebElement pickerSearchUser;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPeoplePickerSerchConversations")
	private List<WebElement> pickerSearchConversations;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPeoplePickerClearbtn")
	private WebElement pickerClearBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerRows")
	private List<WebElement> pickerSearchRows;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerUsersUnselected")
	private List<WebElement> pickerUsersUnselected;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearch")
	private WebElement pickerSearch;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerGrid")
	private WebElement pickerGrid;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerBtnDone")
	private WebElement addToConversationsButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idCreateConversationIcon")
	private WebElement createConversation;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idNoResultsFound")
	private WebElement noResults;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
	private List<WebElement> connectToHeader;
	
	private String url;
	private String path;

	public PeoplePickerPage(String URL, String path) throws Exception {
		super(URL, path);

		this.url = URL;
		this.path = path;
	}
	
	
	public void tapPeopleSearch() {
		pickerSearch.click();
	}

	public void typeTextInPeopleSearch(String contactName) throws InterruptedException
	{
		pickerSearch.sendKeys(contactName);
		driver.sendKeyEvent(66);
	}
	
	public void addTextToPeopleSearch(String contactName) throws InterruptedException
	{
		for(char ch : contactName.toCharArray()) {
			int keyCode = KeyboardMapper.getPrimaryKeyCode(ch);
			driver.sendKeyEvent(keyCode);
		}
		
	}
	
	public boolean isNoResultsFoundVisible() {
		
		refreshUITree();
		return noResults.isDisplayed();
	}
	
	public AndroidPage selectContact(String contactName) throws Exception {
		AndroidPage page = null;
		refreshUITree();
		pickerSearchUser.click();
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PeoplePickerPage.idPickerSearchUsers));
		refreshUITree();
		if(driver.findElementsById(AndroidLocators.OtherUserPersonalInfoPage.idUnblockBtn).size() > 0) {
			page = new OtherUserPersonalInfoPage(url, path);
		}
		else if(driver.findElementsById(AndroidLocators.ConnectToPage.idConnectToHeader).size() > 0) {
			page = new ConnectToPage(url, path);
		}
		else if(isVisible(addToConversationsButton)) {
			page = this;
		}
		else {
			page = new DialogPage(url, path);
		}
		return page;
	}

	public AndroidPage selectGroup(String contactName) throws Exception {
		AndroidPage page = null;
		WebElement el = driver.findElementByXPath(String.format(AndroidLocators.PeoplePickerPage.xpathPeoplePickerGroup,contactName));
		el.click();
	
		if(isVisible(addToConversationsButton)) {
			page = this;
		}
		else{
			page = new DialogPage(url, path);
		}
		return page;
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean isPeoplePickerPageVisible() throws InterruptedException, IOException {
		refreshUITree();//TODO workaround
		try {
			wait.until(ExpectedConditions.visibilityOf(pickerSearch));
		} catch (NoSuchElementException e) {
			return false;
		} catch (TimeoutException e) {
			return false;
		}
		
		return pickerSearch.isEnabled();
	}


	public void waitUserPickerFindUser(String contactName){
		for(int i = 0; i < 5; i++){
			List<WebElement> elements = pickerSearchUsers;
			for(WebElement element : elements) {
				try{
					if(element.getText().toLowerCase().equals(contactName.toLowerCase())){
						return;
					}
				}	
				catch(Exception ex){
					continue;
				}
			}
		}
	}

	public boolean isAddToConversationBtnVisible(){
		return addToConversationsButton.isDisplayed();
	}

	public DialogPage clickOnAddToCoversationButton() throws Exception{
		driver.navigate().back();
		addToConversationsButton.click();
		return new DialogPage(url, path);
	}
	//TODO: move this to some base page
	private boolean isVisible(WebElement element) {
		boolean value = false;
		try{
			element.isDisplayed();
			value = true;
		}
		catch(NoSuchElementException ex)
		{
			value = false;
		}
		return value;

	}

	public AndroidPage tapCreateConversation() throws Exception {
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(createConversation));
		createConversation.click();
		//DriverUtils.androidMultiTap(driver, createConversation,1,0.3);
		return  new DialogPage(url, path);
	}

	public ContactListPage tapClearButton() throws Exception {
		refreshUITree();
		pickerClearBtn.click();
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn));
		refreshUITree();
		return new ContactListPage(url, path);
	}



	public boolean userIsVisible(String contact) {
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PeoplePickerPage.idNoResultsFound));
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOfAllElements(pickerSearchUsers));
		return isVisible(driver.findElement(By.xpath(String.format(AndroidLocators.PeoplePickerPage.xpathPeoplePickerContact, contact))));	
	}
	
	public boolean groupIsVisible(String contact) {
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PeoplePickerPage.idNoResultsFound));
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOfAllElements(pickerSearchConversations));
		return isVisible(driver.findElement(By.xpath(String.format(AndroidLocators.PeoplePickerPage.xpathPeoplePickerGroup, contact))));	
	}

	public PeoplePickerPage selectContactByLongTap(String contact) {
		refreshUITree();
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PeoplePickerPage.idNoResultsFound));
		refreshUITree();
		WebElement el = driver.findElementByXPath(String.format(AndroidLocators.PeoplePickerPage.xpathPeoplePickerContact,contact));
		DriverUtils.androidLongClick(driver, el);
		return this;
		
	}
	
}
