package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.StringParser;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;
import com.wearezeta.auto.osx.pages.ConversationPage;
import com.wearezeta.auto.osx.pages.PeoplePickerPage;
import com.wearezeta.auto.osx.pages.UserProfilePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {
	private static final Logger log = ZetaLogger.getLog(ContactListPageSteps.class.getSimpleName());
	
	@Given ("^I see my name (.*) in Contact list$")
	public void ISeeMyNameInContactList(String name) throws Exception {
		CommonSteps.senderPages.getLoginPage().sendProblemReportIfFound();
		CommonSteps.senderPages.getContactListPage().pressLaterButton();
		Thread.sleep(1000);
		CommonSteps.senderPages.setPeoplePickerPage(new PeoplePickerPage(
				CommonUtils.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
				CommonUtils.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
		PeoplePickerPage peoplePickerPage = CommonSteps.senderPages.getPeoplePickerPage();
		if (peoplePickerPage.isPeoplePickerPageVisible()) {
			log.debug("People picker appears. Closing it.");
			peoplePickerPage.closePeoplePicker();
		}
		GivenISeeContactListWithName(name);
	}
	
	@Given ("I see Contact list with name (.*)")
	public void GivenISeeContactListWithName(String name) throws Exception {
		if (name.equals(OSXLocators.RANDOM_KEYWORD)) {
			name = CommonSteps.senderPages.getConversationInfoPage().getCurrentConversationName();
		} else {
			name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		}
		log.debug("Looking for contact with name " + name);
		Assert.assertTrue(CommonSteps.senderPages.getContactListPage().isContactWithNameExists(name));
	}
	
	@Given ("I do not see conversation {1}(.*) {1}in contact list")
	public void IDoNotSeeConversationInContactList(String conversation) throws IOException {
		conversation = CommonUtils.retrieveRealUserContactPasswordValue(conversation);
		Assert.assertTrue(CommonSteps.senderPages.getContactListPage().isContactWithNameDoesNotExist(conversation));
	}
	
	@Then ("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String name) {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		Assert.assertTrue("Login finished", CommonSteps.senderPages.getLoginPage().waitForLogin());
		Assert.assertTrue(name + " were not found in contact list", CommonSteps.senderPages.getLoginPage().isLoginFinished(name));
	}
	
	@Given("I open conversation with (.*)")
	public void GivenIOpenConversationWith(String contact) throws MalformedURLException, IOException {
		if (contact.equals(OSXLocators.RANDOM_KEYWORD)) {
			contact = CommonSteps.senderPages.getConversationInfoPage().getCurrentConversationName();
		} else {
			contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		}
		
		boolean isConversationExist = false;
		for (int i = 0; i < 10; i++) {
			isConversationExist = CommonSteps.senderPages.getContactListPage().openConversation(contact);
			if(isConversationExist) break;
			try {Thread.sleep(1000); } catch (InterruptedException e) { }
		}
		Assert.assertTrue("Conversation with name " + contact + " was not found.", isConversationExist);
		CommonSteps.senderPages.setConversationPage(new ConversationPage(
				CommonUtils.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
				CommonUtils.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
	 }
	
	@When("I open People Picker from contact list")
	public void WhenIOpenPeoplePickerFromContactList() throws MalformedURLException, IOException {
		CommonSteps.senderPages.getContactListPage().openPeoplePicker();
		CommonSteps.senderPages.setPeoplePickerPage(new PeoplePickerPage(
				CommonUtils.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
				CommonUtils.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
	}
	
	@Given("I go to user (.*) profile") 
	public void GivenIGoToUserProfile(String user) throws MalformedURLException, IOException {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		GivenIOpenConversationWith(user);
		CommonSteps.senderPages.setUserProfilePage(new UserProfilePage(
				CommonUtils.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
				CommonUtils.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
	}
	
	@When("I change conversation mute state")
	public void IChangeConversationMuteState() {
		ContactListPage contactList = CommonSteps.senderPages.getContactListPage();
		contactList.changeMuteStateForSelectedConversation();
	}
	
	@Then("I see conversation (.*) is muted")
	public void ISeeConversationIsMuted(String conversation) {
		conversation = CommonUtils.retrieveRealUserContactPasswordValue(conversation);
		ContactListPage contactList = CommonSteps.senderPages.getContactListPage();
		Assert.assertTrue(
				"Conversation with name " + conversation + " were not muted.",
				contactList.isConversationMutedButtonVisible(conversation));
	}
	
	@Then("I see conversation (.*) is unmuted")
	public void ISeeConversationIsUnmuted(String conversation) {
		conversation = CommonUtils.retrieveRealUserContactPasswordValue(conversation);
		ContactListPage contactList = CommonSteps.senderPages.getContactListPage();
		Assert.assertFalse(
				"Conversation with name " + conversation + " is still muted.",
				contactList.isConversationMutedButtonVisible(conversation));
	}
	
	@When("I see connect invitation")
	public void ISeeConnectInvitation() throws Exception {
		GivenISeeContactListWithName(OSXLocators.CONTACT_LIST_ONE_CONNECT_REQUEST);
	}
	
	@When("I accept invitation")
	public void IAcceptInvitation() {
		ContactListPage contactList = CommonSteps.senderPages.getContactListPage();
		contactList.acceptAllInvitations();
	}
	
	@When("I archive conversation")
	public void IArchiveConversation() {
		ContactListPage contactList = CommonSteps.senderPages.getContactListPage();
		contactList.moveSelectedConversationToArchive();
	}
	
	@When("I go to archive")
	public void IGoToArchive() {
		ContactListPage contactList = CommonSteps.senderPages.getContactListPage();
		contactList.showArchivedConversations();
	}
	
	@When("I set name {1}(.*) {1}for conversation$")
	public void ISetRandomNameForConversation(String name) throws MalformedURLException, IOException {
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		name = StringParser.unescapeString(name);
		if (name.equals(OSXLocators.RANDOM_KEYWORD)) {
			conversationInfo.setCurrentConversationName(CommonUtils.generateGUID());
		} else {
			conversationInfo.setCurrentConversationName(name);
		}
		conversationInfo.setNewConversationName(conversationInfo.getCurrentConversationName());
	}
}
