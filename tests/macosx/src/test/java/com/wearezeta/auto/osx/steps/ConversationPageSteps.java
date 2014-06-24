package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationPageSteps {
	 private String randomMessage;
	 private int beforeNumberOfKnocks = -1;
	 private int beforeNumberOfImages = -1;
	 
	 @When("I write random message")
	 public void WhenIWriteRandomMessage() {	
		 randomMessage = CommonUtils.generateGUID();
		 CommonSteps.senderPages.getConversationPage().writeNewMessage(randomMessage);
	 }
	 
	 @When("I send message")
	 public void WhenISendMessage() {
		 CommonSteps.senderPages.getConversationPage().sendNewMessage();
	 }
	 
	 @When("I send picture (.*)")
	 public void WhenISendPicture(String imageFilename) throws MalformedURLException, IOException {
		 if (beforeNumberOfImages < 0) {
			 beforeNumberOfImages =
				 CommonSteps.senderPages.getConversationPage()
				 		.getNumberOfImageEntries();
		 }
		 CommonSteps.senderPages.getConversationPage().writeNewMessage("");
		 CommonSteps.senderPages.getConversationPage().openChooseImageDialog();
		 CommonSteps.senderPages.setChoosePicturePage(new ChoosePicturePage(
				 CommonUtils.getUrlFromConfig(ContactListPage.class),
				 CommonUtils.getAppPathFromConfig(ContactListPage.class)
				 ));
		 
		 ChoosePicturePage choosePicturePage = CommonSteps.senderPages.getChoosePicturePage();
		 Assert.assertTrue(choosePicturePage.isVisible());
		 
		 choosePicturePage.openImage(imageFilename);
	 }
	 
	 @Then("I see random message in conversation")
	 public void ThenISeeRandomMessageInConversation() {
		 Assert.assertTrue(CommonSteps.senderPages.getConversationPage().isMessageSent(randomMessage));
	 }
	 
	 @Then("I see picture in conversation")
	 public void ThenISeePictureInConversation() {
		 int afterNumberOfImages = -1;
		 
		 boolean isNumberIncreased = false;
		 for (int i = 0; i < 60; i++) {
			 afterNumberOfImages = CommonSteps.senderPages.getConversationPage().getNumberOfImageEntries();
			 if (afterNumberOfImages == beforeNumberOfImages + 2) {
				 isNumberIncreased = true;
				 break;
			 }
			 try { Thread.sleep(1000); } catch (InterruptedException e) { }
		 }
		 
		 Assert.assertTrue("Incorrect images count: before - "
				 + beforeNumberOfImages + ", after - " + afterNumberOfImages, isNumberIncreased);
	 }
	 
	 @When("I am knocking to user")
	 public void WhenIAmKnockingToUser() {
		 if (beforeNumberOfKnocks < 0) {
			 beforeNumberOfKnocks =
				 CommonSteps.senderPages.getConversationPage()
				 		.getNumberOfMessageEntries(OSXLocators.YOU_KNOCKED_MESSAGE);
		 }
		 CommonSteps.senderPages.getConversationPage().knock();
	 }
	 
	 @Then("I see message (.*) in conversation")
	 public void ThenISeeMessageInConversation(String message) {
		 if (message.equals(OSXLocators.YOU_KNOCKED_MESSAGE)) {
			 boolean isNumberIncreased = false;
			 int afterNumberOfKnocks = -1;
			 for (int i = 0; i < 60; i++) {
				 afterNumberOfKnocks = CommonSteps.senderPages.getConversationPage().getNumberOfMessageEntries(message);
				 if (afterNumberOfKnocks == beforeNumberOfKnocks + 1) {
					 isNumberIncreased = true;
					 break;
				 }
				 try { Thread.sleep(1000); } catch (InterruptedException e) { }
			 }
			 
			 Assert.assertTrue("Incorrect messages count: before - "
					 + beforeNumberOfKnocks + ", after - " + afterNumberOfKnocks, isNumberIncreased);
			 
		} else if (message.contains(OSXLocators.YOU_ADDED_MESSAGE)) {
			message = CommonUtils.retrieveRealUserContactPasswordValue(message);
			message = message.toUpperCase();
			Assert.assertTrue("User was not added to conversation. Message " + message + " not found.", 
					CommonSteps.senderPages.getConversationPage().isMessageExist(message));
	 	} else if (message.contains(OSXLocators.YOU_REMOVED_MESSAGE)) {
	 		message = CommonUtils.retrieveRealUserContactPasswordValue(message);
			message = message.toUpperCase();
			Assert.assertTrue("User was not removed from conversation. Message " + message + " not found.", 
					CommonSteps.senderPages.getConversationPage().isMessageExist(message));
	 	} else {
			 Assert.assertTrue(CommonSteps.senderPages.getConversationPage().isMessageExist(message));
		 }
	 }
	 
	 @When("I open People Picker from conversation")
	 public void WhenIOpenPeoplePickerFromConversation() throws MalformedURLException, IOException {
		 CommonSteps.senderPages.getConversationPage().writeNewMessage("");
		 CommonSteps.senderPages.getConversationPage().openConversationPeoplePicker();
		 CommonSteps.senderPages.setConversationInfoPage(new ConversationInfoPage(
				 CommonUtils.getUrlFromConfig(ConversationInfoPage.class),
				 CommonUtils.getAppPathFromConfig(ConversationInfoPage.class)
				 ));
		 ConversationInfoPage conversationPeople = CommonSteps.senderPages.getConversationInfoPage();
		 CommonSteps.senderPages.setPeoplePickerPage(conversationPeople.openPeoplePicker());
	 }
	 
	 @When("I open Conversation info")
	 public void WhenIOpenConversationInfo() throws MalformedURLException, IOException {
		 CommonSteps.senderPages.getConversationPage().writeNewMessage("");
		 CommonSteps.senderPages.getConversationPage().openConversationPeoplePicker();
		 CommonSteps.senderPages.setConversationInfoPage(new ConversationInfoPage(
				 CommonUtils.getUrlFromConfig(ConversationInfoPage.class),
				 CommonUtils.getAppPathFromConfig(ConversationInfoPage.class)
				 ));
	 }
	 
	 @Given("I create group chat with (.*) and (.*)")
	 public void WhenICreateGroupChatWithUser1AndUser2(String user1, String user2) throws MalformedURLException, IOException {
		 user1 = CommonUtils.retrieveRealUserContactPasswordValue(user1);
		 user2 = CommonUtils.retrieveRealUserContactPasswordValue(user2);
		 ContactListPageSteps clSteps = new ContactListPageSteps();
		 PeoplePickerPageSteps ppSteps = new PeoplePickerPageSteps();
		 clSteps.GivenIOpenConversationWith(user1);
		 this.WhenIOpenPeoplePickerFromConversation();
		 ppSteps.WhenISearchForUser(user2);
		 ppSteps.WhenISeeUserFromSearchResults(user2);
		 ppSteps.WhenIAddUserFromSearchResults(user2);
	 }
}
