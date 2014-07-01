package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.*;

public class ContactListPage extends IOSPage {
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameContactListNames)
	private List<WebElement> contactListNames;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameProfileName)
	private WebElement profileName;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMuteButton)
	private List<WebElement> muteButtons;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConnectAlertYes)
	private WebElement connectAlertButton;

	private String url;
	private String path;
	private int oldLocation = 0;
	
	public ContactListPage(String URL, String path) throws IOException {
		super(URL, path);
		url = URL;
		this.path = path;
	}
	
	public void muteConversation() {
		
		for (WebElement el : muteButtons) {
			if (el.isDisplayed()) {
				el.click();
			}
		}
	}
	
	public boolean isContactMuted(String contact) {
		
		//potential floating bug, the icon is always visible, but simply changes x coordinate
		return driver.findElementByXPath(String.format(IOSLocators.xpathMutedIcon, contact)).getLocation().x < oldLocation;
	}
	
	private boolean isProfilePageVisible() {
		boolean result = false;
		
		try {
			result = profileName.isDisplayed();
		}
		catch (org.openqa.selenium.NoSuchElementException ex) {
			//do nothing
		}
		
		return result;
	}
	
	public void acceptConnectionRequest() {
		connectAlertButton.click();
	}

	public IOSPage tapOnName(String name) throws IOException {
		IOSPage page = null;
		findNameInContactList(name).click();
		if(isProfilePageVisible()){
			page = new PersonalInfoPage(url, path);
		}
		else{
			page = new DialogPage(url, path);
		}
		return page;
	}
	
	public String getFirstDialogName(String name){
		
		DriverUtils.waitUntilElementAppears(driver, By.xpath(String.format(IOSLocators.xpathFirstInContactList, name)));
		WebElement contact = driver.findElement(By.xpath(String.format(IOSLocators.xpathFirstInContactList, name)));
		return contact.getText();
	}
	
	private WebElement findNameInContactList(String name)
	 {
		 Boolean flag = true;
		 WebElement contact = null;
		 for(WebElement listName : contactListNames )
		 {
			 if(listName.getText().equals(name)) {
				 contact = listName;
				 flag = false;
				 break;
			 }
		 }
		 if(flag) {
			 DriverUtils.scrollToElement(driver,contactListNames.get(contactListNames.size() - 1));
			 findNameInContactList(name);
		 }
		return contact;
	 }	
	
	public boolean isGroupChatAvailableInContactList() {
		boolean flag = false;
		
		for (WebElement el: contactListNames) {
			if(el.getAttribute("name").contains(","))
			{
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	public IOSPage swipeRightOnContact(int time, String contact) throws IOException
	{
		oldLocation = driver.findElementByXPath(String.format(IOSLocators.xpathMutedIcon, contact)).getLocation().x;
		DriverUtils.swipeRight(driver, findNameInContactList(contact), time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}
	
	public GroupChatPage tapOnGroupChat(String contact1, String contact2) throws IOException {
		
		findChatInContactList(contact1, contact2).click();

		return new GroupChatPage(url, path);
	}
	
	public void waitForContactListToLoad() {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(IOSLocators.xpathFirstInContactList)));
	}
	
	private WebElement findChatInContactList(String contact1, String contact2) {

		String contact = "";
		WebElement el = null;
		
		for (WebElement element: contactListNames) {
			contact = element.getAttribute("name");
			if(contact.contains(",") && contact.contains(contact1) && contact.contains(contact2))
			{
				el = element;
				break;
			}
		}
		
		return el;
	}
	
	public boolean waitForConnectionAllert() {
			
		return DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameConnectAlert));
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		
		IOSPage page = null;
		switch (direction){
			case DOWN:
			{
				page = new PeoplePickerPage(url, path);
				break;
			}
			case UP:
			{
				break;
			}
			case LEFT:
			{
				break;
			}
			case RIGHT:
			{
				break;
			}
		}	
		return page;
	}
}