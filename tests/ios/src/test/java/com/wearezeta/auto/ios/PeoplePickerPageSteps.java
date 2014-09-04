package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.ConnectToPage;
import com.wearezeta.auto.ios.pages.GroupChatPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	
	@When("^I see People picker page$")
	public void WhenISeePeoplePickerPage() throws Throwable {
		Assert.assertTrue(PagesCollection.peoplePickerPage.isPeoplePickerPageVisible());
	}
	
	@When("^I tap on Search input on People picker page$")
	public void WhenITapOnSearchInputOnPeoplePickerPage() throws Throwable {
	    PagesCollection.peoplePickerPage.tapOnPeoplePickerSearch();
	}
	
	@When("^I input in People picker search field user name (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserName(String contact) throws Throwable {
		
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    PagesCollection.peoplePickerPage.fillTextInPeoplePickerSearch(contact);
	}
	
	@When("^I see user (.*) found on People picker page$")
	public void WhenISeeUserFoundOnPeoplePickerPage(String contact) throws Throwable {
		
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);
	}
	
	@When("^I tap on user name found on People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact) throws Throwable {
		
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		IOSPage page = PagesCollection.peoplePickerPage.clickOnFoundUser(contact);
		
		if(page instanceof ConnectToPage) {
			PagesCollection.connectToPage = (ConnectToPage)page;
		}
		
		else {
			PagesCollection.peoplePickerPage = (PeoplePickerPage)page;
		}
	}
	
	@When("^I search for user name (.*) and tap on it on People picker page$")
	public void WhenISearchForUserNameAndTapOnItOnPeoplePickerPage(String contact) throws Throwable {
		
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    PagesCollection.peoplePickerPage.pickUserAndTap(contact);
	}
	
	@When("^I see Add to conversation button$")
	public void WhenISeeAddToConversationButton(){
		Assert.assertTrue("Add to conversation button is not visible", PagesCollection.peoplePickerPage.isAddToConversationBtnVisible());
	}
	
	@When("^I click on Add to conversation button$")
	public void WhenIClickOnAddToConversationButton() throws IOException{
		PagesCollection.groupChatPage = (GroupChatPage)PagesCollection.peoplePickerPage.clickOnAddToCoversationButton();
	}

	@When("^I click clear button$")
	public void WhenIClickClearButton() throws IOException{
		PagesCollection.contactListPage = PagesCollection.peoplePickerPage.dismissPeoplePicker();
	}
	
	@When("I click on user icon to add it to existing group chat")
	public void IClickOnUserIconToAddItToExistingGroupChat(String contact) throws Throwable{
		String name = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		PagesCollection.groupChatInfoPage = PagesCollection.peoplePickerPage.clickOnUserToAddToExistingGroupChat(name);
	}
	
	@When("I see contact list on People picker page")
	public void ISeeContactListOnPeoplePickerPage(){
		Assert.assertTrue("Contacts label is not shown", PagesCollection.peoplePickerPage.isContactsLabelVisible());
	}
	
	@When("I see top people list on People picker page")
	public void ISeeTopPeopleListOnPeoplePickerPage(){
		Assert.assertTrue("Top People label is not shown", PagesCollection.peoplePickerPage.isTopPeopleLabelVisible());
	}
	
	@When("I select user (.*) on People picker page")
	public void ISelectUserOnPeoplePickerPage(String name){
		name = CommonUtils.retrieveRealUserContactPasswordValue(name).toUpperCase();
		PagesCollection.peoplePickerPage.selectUser(name);
	}
	
	@When("I see Create Conversation button on People picker page")
	public void ISeeCreateConversationButton(){
		Assert.assertTrue("Create Conversation button is not visible.", PagesCollection.peoplePickerPage.isCreateConversationButtonVisible());
	}
	
	@When("I click Create Conversation button  on People picker page")
	public void IClickCreateConversationButton() throws Throwable{
		PagesCollection.groupChatPage = PagesCollection.peoplePickerPage.clickCreateConversationButton();
	}

}
