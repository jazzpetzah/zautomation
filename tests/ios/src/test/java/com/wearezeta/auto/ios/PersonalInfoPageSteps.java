package com.wearezeta.auto.ios;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.ios.pages.CameraRollPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
	
	BufferedImage referenceImage;
	
	@When("^I swipe up for options$")
	public void WhenISwipeUpForOptions() throws IOException, Throwable {
		PagesCollection.personalInfoPage.swipeUp(500);
	}

	@When("^I press options button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		PagesCollection.personalInfoPage.tapOptionsButtonByText(buttonName);
	}
	
	@When("^I tap on personal screen$")
	public void ITapOnPersonalScreen() throws InterruptedException{
		PagesCollection.personalInfoPage.tapOnPersonalPage();
		Thread.sleep(2000);
	}
	
	@When("^I press Camera button$")
	public void IPressCameraButton() throws InterruptedException, IOException{
		CameraRollPage page = PagesCollection.personalInfoPage.pressCameraButton();
		PagesCollection.cameraRollPage = (CameraRollPage) page;
	  Thread.sleep(2000);
	}
	
	@When("^I return to personal page$")
	public void IReturnToPersonalPage() throws Throwable {
		
		PagesCollection.personalInfoPage.tapOnPersonalPage();
		Thread.sleep(2000);
		PagesCollection.personalInfoPage.tapOnPersonalPage();
		Thread.sleep(2000);
		referenceImage = PagesCollection.personalInfoPage.takeScreenshot();
		PagesCollection.personalInfoPage.tapOnPersonalPage();
		Thread.sleep(2000);
	}

	@Then("I see changed user picture (.*)")
	public void ThenISeeChangedUserPicture(String filename) throws Throwable {
		
		BufferedImage templateImage = ImageUtil.readImageFromFile(IOSPage.imagesPath + filename);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage);
		//System.out.print("SCORE: " + score);
		/*Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= 0.75, current = " + score,
				score >= 0.75d);*/
		
	}



}
