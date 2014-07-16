package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;
import com.wearezeta.auto.osx.pages.ConversationPage;
import com.wearezeta.auto.osx.pages.PeoplePickerPage;
import com.wearezeta.auto.osx.pages.UserProfilePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {
	
	public static final String RANDOM_KEYWORD = "RANDOM";
	
	public String conversationName;
	
	@Given ("I see Contact list with name (.*)")
	public void GivenISeeContactListWithName(String name) throws IOException {
		if (name.equals(RANDOM_KEYWORD)) {
			name = conversationName;
		} else {
			name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		}
		Assert.assertTrue(CommonSteps.senderPages.getContactListPage().isContactWithNameExists(name));
	}
	
	@Given ("I do not see conversation (.*) in contact list")
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
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		boolean isConversationExist = false;
		for (int i = 0; i < 10; i++) {
			isConversationExist = CommonSteps.senderPages.getContactListPage().openConversation(contact);
			if(isConversationExist) break;
			try {Thread.sleep(1000); } catch (InterruptedException e) { }
		}
		Assert.assertTrue("Conversation with name " + contact + " was not found.", isConversationExist);
		CommonSteps.senderPages.setConversationPage(new ConversationPage(
				CommonUtils.getUrlFromConfig(ContactListPageSteps.class),
				CommonUtils.getAppPathFromConfig(ContactListPageSteps.class)));
	 }
	
	@When("I open People Picker from contact list")
	public void WhenIOpenPeoplePickerFromContactList() throws MalformedURLException, IOException {
		CommonSteps.senderPages.getContactListPage().openPeoplePicker();
		CommonSteps.senderPages.setPeoplePickerPage(new PeoplePickerPage(
				CommonUtils.getUrlFromConfig(ContactListPageSteps.class),
				CommonUtils.getAppPathFromConfig(ContactListPageSteps.class)));
	}
	
	@Given("I go to user (.*) profile") 
	public void GivenIGoToUserProfile(String user) throws MalformedURLException, IOException {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		GivenIOpenConversationWith(user);
		CommonSteps.senderPages.setUserProfilePage(new UserProfilePage(
				CommonUtils.getUrlFromConfig(ContactListPageSteps.class),
				CommonUtils.getAppPathFromConfig(ContactListPageSteps.class)));
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
	public void ISeeConnectInvitation() {
		ContactListPage contactList = CommonSteps.senderPages.getContactListPage();
		Assert.assertTrue("No connect requests found.", contactList.isInvitationExist());
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
	
	@When("I set name (.*) for conversation")
	public void ISetRandomNameForConversation(String name) throws MalformedURLException, IOException {
		if (name.equals(RANDOM_KEYWORD)) {
			conversationName = CommonUtils.generateGUID();
		} else {
			conversationName = name;
		}
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		conversationInfo.setNewConversationName(conversationName);
	}
}
