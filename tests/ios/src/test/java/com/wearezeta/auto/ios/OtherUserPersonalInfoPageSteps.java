package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
	
	@When("^I see (.*) user profile page$")
	public void WhenISeeOherUserProfilePage(String name){
		
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.otherUserPersonalInfoPage.isOtherUserNameVisible(name);
	}
	
	@When("^I swipe down other user profile page$")
	public void WhenISwipeDownOtherUserProfilePage() throws IOException, InterruptedException {
		Thread.sleep(5000);
		PagesCollection.peoplePickerPage = (PeoplePickerPage)PagesCollection.otherUserPersonalInfoPage.swipeDown(1000);
	}
	
	@When("^I swipe up on other user profile page$")
	public void ISwipeUpOnUserProfilePage() throws IOException {
		PagesCollection.otherUserPersonalInfoPage.swipeUp(1000);
	}
	
	@When("^I click Remove$")
	public void IClickRemove() {
		PagesCollection.otherUserPersonalInfoPage.removeFromConversation();
	}
	
	@When("^I see warning message$")
	public void ISeeAreYouSure() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage.isRemoveFromConversationAlertVisible());
	}
	
	@When("^I confirm remove$")
	public void IConfirmRemove() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.confirmRemove();
	}

}
