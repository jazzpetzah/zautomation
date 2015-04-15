package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SelfProfilePageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private BufferedImage userProfileBefore = null;
	private BufferedImage userProfileAfter = null;

	@Given("I open picture settings")
	public void GivenIOpenPictureSettings() throws Exception {
		PagesCollection.selfProfilePage.openPictureSettings();
	}

	@When("I choose to select picture from image file")
	public void WhenIChooseToSelectPictureFromImageFile() throws Exception {
		PagesCollection.choosePicturePage = PagesCollection.selfProfilePage
				.openChooseImageFileDialog();
	}

	@When("I choose to select picture from camera")
	public void WhenIChooseToSelectPictureFromCamera() {
		PagesCollection.selfProfilePage.openCameraDialog();
	}

	@When("I select image file (.*)")
	public void WhenISelectImageFile(String imageFilename) throws Exception {
		Assert.assertTrue(PagesCollection.choosePicturePage.isVisible());

		PagesCollection.choosePicturePage.openImage(imageFilename);

		PagesCollection.selfProfilePage.confirmPictureChoice();
	}

	@When("I shoot picture using camera")
	public void WhenIShootPictureUsingCamera() throws Exception {
		PagesCollection.selfProfilePage.doPhotoInCamera();
		PagesCollection.selfProfilePage.confirmPictureChoice();
	}

	@Then("I see changed user picture from image (.*)")
	public void ThenISeeChangedUserPictureFromImage(String filename)
			throws Exception {
		PagesCollection.selfProfilePage.openPictureSettings();
		BufferedImage referenceImage = PagesCollection.selfProfilePage
				.takeScreenshot();

		final double minOverlapScore = 0.55d;
		BufferedImage templateImage = ImageUtil
				.readImageFromFile(OSXExecutionContext.userDocuments + "/"
						+ filename);
		final double score = ImageUtil.getOverlapScore(referenceImage,
				templateImage);
		Assert.assertTrue(
				String.format(
						"Overlap between two images has no enough score. Expected >= %f, current = %f",
						minOverlapScore, score), score >= minOverlapScore);
	}

	@Then("^I see changed user picture$")
	public void ThenISeeChangedUserPicture() throws IOException {
		if (userProfileAfter != null) {
			userProfileBefore = userProfileAfter;
		}
		userProfileAfter = PagesCollection.selfProfilePage.takeScreenshot();

		final double minOverlapScore = 0.985d;
		final double score = ImageUtil.getOverlapScore(userProfileAfter,
				userProfileBefore, ImageUtil.RESIZE_NORESIZE);
		Assert.assertTrue(
				String.format(
						"Overlap between two images is larger than expected. Picture was not changed. Expected <= %f, current = %f",
						minOverlapScore, score), score <= minOverlapScore);
	}

	@When("I select to remove photo")
	public void ISelectToRemovePhoto() {
		PagesCollection.selfProfilePage.chooseToRemovePhoto();
	}

	@When("I confirm photo removing")
	public void IConfirmPhotoRemoving() throws Exception {
		PagesCollection.selfProfilePage.confirmPhotoRemoving();
	}

	@When("I cancel photo removing")
	public void ICancelPhotoRemoving() {
		PagesCollection.selfProfilePage.cancelPhotoRemoving();
	}

	@Then("I see user profile picture is not set")
	public void ISeeUserProfilePictureIsNotSet() throws IOException {
		ThenISeeChangedUserPicture();
	}

	@When("I see photo in User profile")
	public void ISeePhotoInUserProfile() throws IOException {
		userProfileBefore = PagesCollection.selfProfilePage.takeScreenshot();
	}

	@Then("I see name (.*) in User profile")
	public void ISeeNameInUserProfile(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue(PagesCollection.selfProfilePage
				.selfProfileNameEquals(name));
	}

	@Then("I see email of (.*) in User profile")
	public void ISeeEmailOfUserInUserProfile(String name) throws Exception {
		ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(name);
		name = dstUser.getName();
		String email = dstUser.getEmail();
		PagesCollection.selfProfilePage.selfProfileEmailEquals(email);
	}

	/**
	 * Set new username on self profile page
	 * 
	 * @step. ^I change username to (.*)
	 * 
	 * @param name
	 *            new username string
	 */
	@And("^I change username to (.*)")
	public void IChangeUserNameTo(String name) {
		PagesCollection.selfProfilePage.changeUserName(usrMgr.getSelfUser()
				.getName(), name);
		usrMgr.getSelfUser().setName(name);
	}

}
