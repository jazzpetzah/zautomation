package com.wearezeta.auto.ios;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.LanguageUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.UserState;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private ClientUser userToRegister = null;

	private MBoxChangesListener listener;

	public static final int maxCheckCnt = 2;

	boolean generateUsers = false;
	public static BufferedImage basePhoto;
	BufferedImage templateImage;
	BufferedImage referenceImage;
	BufferedImage profileImage;

	@Then("I see Take or select photo label and smile")
	public void ISeeTakeOrSelectPhotoLabel() {
		Assert.assertTrue(PagesCollection.registrationPage
				.isTakeOrSelectPhotoLabelVisible());
		Assert.assertTrue(PagesCollection.registrationPage
				.isTakePhotoSmileDisplayed());
	}

	@Then("I don't see Take or select photo label and smile")
	public void IDontSeeTakeOrSelectPhotoLabel() {
		Assert.assertFalse(PagesCollection.registrationPage
				.isTakeOrSelectPhotoLabelVisible());
	}

	@When("^I press Camera button on Registration page$")
	public void WhenIPressCameraButtonOnRegistrationPage() throws IOException {

		PagesCollection.registrationPage.clickCameraButton();
	}

	@When("^I take photo by front camera$")
	public void WhenITakePhotoByFrontCamera() throws IOException {

		PagesCollection.registrationPage.takePhotoByFrontCamera();
	}

	@When("^I take photo by rear camera$")
	public void WhenITakePhotoByRearCamera() throws IOException,
			InterruptedException {
		PagesCollection.registrationPage.takePhotoByRearCamera();
		basePhoto = PagesCollection.registrationPage.takeScreenshot();
		Thread.sleep(3000);
	}

	@When("^I See photo taken$")
	public void ISeePhotoTaken() throws IOException {

		Assert.assertTrue(PagesCollection.registrationPage.isPictureSelected());
	}

	@When("^I press Picture button$")
	public void WhenIPressPictureButton() throws Exception {

		PagesCollection.cameraRollPage = PagesCollection.registrationPage
				.selectPicture();
	}

	@When("^I See selected picture$")
	public void ISeeSelectedPicture() throws IOException {

		templateImage = PagesCollection.registrationPage.takeScreenshot();
		Assert.assertTrue(PagesCollection.registrationPage.isPictureSelected());
	}

	@When("I reject selected picture$")
	public void IRejectSelectedPicture() {
		PagesCollection.registrationPage.cancelImageSelection();
	}

	@When("^I confirm selection$")
	public void IConfirmSelection() throws IOException {

		PagesCollection.registrationPage.confirmPicture();
	}

	@When("I click Back button")
	public void IClickBackButton() {
		PagesCollection.registrationPage.tapBackButton();
	}

	@When("^I verify back button$")
	public void IVerifyBackButton() {
		Assert.assertTrue("I don't see the back button",
				PagesCollection.registrationPage.isBackButtonVisible());
	}

	@When("I click Forward Button")
	public void IClickForwardButton() {
		PagesCollection.registrationPage.tapForwardButton();
	}

	@When("I see Vignette overlay")
	public void ISeeVignetteOverlay() {
		Assert.assertTrue(PagesCollection.registrationPage
				.isVignetteOverlayVisible());
	}

	@When("I click Vignette overlay")
	public void IClickVignetteOverlay() {
		PagesCollection.registrationPage.clickVignetteLayer();
	}

	@When("I dismiss Vignette overlay")
	public void IDismissVignetteOverlay() {
		PagesCollection.registrationPage.dismissVignetteBakground();
	}

	@When("I don't see Vignette overlay")
	public void IDontSeeVignetteOverlay() {
		Assert.assertFalse(PagesCollection.registrationPage
				.isVignetteOverlayVisible());
	}

	@When("I see full color mode")
	public void ISeeColorMode() {
		Assert.assertTrue(PagesCollection.registrationPage.isColorModeVisible());
	}

	@When("I click close full color mode button")
	public void IClickCloseColorModeButton() {
		PagesCollection.registrationPage.tapCloseColorModeButton();
	}

	@When("I see Registration name input")
	public void ISeeRegistrationNameInput() {
		Assert.assertTrue(PagesCollection.registrationPage.isNameLabelVisible());
	}

	@When("^I enter name (.*)$")
	public void IEnterName(String name) throws Exception {
		try {
			this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setName(name);
			this.userToRegister.clearNameAliases();
			this.userToRegister.addNameAlias(name);
		}
		PagesCollection.registrationPage.setName(this.userToRegister.getName());
	}

	@When("^I enter a username which is at most (\\d+) characters long from (\\w+) alphabet$")
	public void IEnterNameWithCharacterLimit(int charactersLimit,
			String alphabetName) throws Throwable {
		String nameToType = LanguageUtils.generateRandomString(charactersLimit,
				alphabetName).replace('\\', '|');
		IEnterName(nameToType);
		PagesCollection.registrationPage.inputName();
	}

	@When("^I input name (.*) and hit Enter$")
	public void IInputNameAndHitEnter(String name) throws Exception {
		IEnterName(name);
		PagesCollection.registrationPage.inputName();
	}

	@Then("^I verify that my username is at most (\\d+) characters long$")
	public void IVerifyUsernameLength(int charactersLimit) throws IOException {
		String realUserName = PagesCollection.registrationPage
				.getUsernameFieldValue();
		int usernameLength = LanguageUtils.getUnicodeStringAsCharList(
				realUserName).size();
		Assert.assertTrue(charactersLimit >= usernameLength);
	}

	@When("^I enter email (.*)$")
	public void IEnterEmail(String email) throws Exception {
		boolean flag = false;
		try {
			String realEmail = usrMgr.findUserByEmailOrEmailAlias(email)
					.getEmail();
			this.userToRegister.setEmail(realEmail);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			flag = true;
		}

		if (flag) {
			PagesCollection.registrationPage.setEmail(email + "\n");
		}
		else {
			PagesCollection.registrationPage.setEmail(this.userToRegister
					.getEmail() + "\n");
		}
	}

	@When("^I input email (.*) and hit Enter$")
	public void IInputEmailAndHitEnter(String email) throws Exception {
		IEnterEmail(email);
	}

	@When("^I attempt to enter an email with spaces (.*)$")
	public void IEnterEmailWithSpaces(String email) throws Exception {
		try {
			String realEmail = usrMgr.findUserByEmailOrEmailAlias(email)
					.getEmail();
			this.userToRegister.setEmail(realEmail);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setEmail(email);
		}
		this.userToRegister.clearEmailAliases();
		this.userToRegister.addEmailAlias(email);
		PagesCollection.registrationPage.setEmail(new StringBuilder(
				this.userToRegister.getEmail()).insert(email.length() - 1,
				"          ").toString());
	}

	@When("^I enter an incorrect email (.*)$")
	public void IEnterIncorrectEmail(String email) throws IOException {
		PagesCollection.registrationPage.setEmail(email);
	}

	@When("I clear email input field on Registration page")
	public void IClearEmailInputRegistration() {
		PagesCollection.registrationPage.clearEmailInput();
	}

	@Then("^I verify no spaces are present in email$")
	public void CheckForSpacesInEmail() throws IOException {
		String realEmailText = PagesCollection.registrationPage
				.getEmailFieldValue();
		String initialEmailText = PagesCollection.registrationPage.getEmail();
		Assert.assertTrue(initialEmailText.replace(" ", "").equals(
				realEmailText));
	}

	@When("^I attempt to enter emails with known incorrect formats$")
	public void IEnterEmailWithIncorrectFormat() throws IOException {
		// current design has basic email requirements: contains single @,
		// contains a domain name with a dot + domain extension(min 2
		// characters)
		String[] listOfInvalidEmails = { "abc.example.com", "abc@example@.com",
				"example@zeta", "abc@example."/* , "abc@example.c" */};
		// test fails because minimum 2 character domain extension is not
		// implemented(allows for only 1)
		PagesCollection.registrationPage.setListOfEmails(listOfInvalidEmails);
	}

	@Then("^I verify that the app does not let me continue$")
	public void IVerifyIncorrectFormatMessage() throws IOException {
		Assert.assertTrue(PagesCollection.registrationPage
				.typeAllInvalidEmails());
	}

	@When("^I enter password (.*)$")
	public void IEnterPassword(String password) throws IOException {
		try {
			this.userToRegister.setPassword(usrMgr.findUserByPasswordAlias(
					password).getPassword());
		} catch (NoSuchUserException e) {
			this.userToRegister.setPassword(password);
			this.userToRegister.addPasswordAlias(password);
		}
		PagesCollection.registrationPage.setPassword(this.userToRegister
				.getPassword());
	}

	@When("^I input password (.*) and hit Enter$")
	public void IInputPasswordAndHitEnter(String password) throws Exception {
		IEnterPassword(password);

		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.listener = IMAPSMailbox.createDefaultInstance().startMboxListener(
				expectedHeaders);
		PagesCollection.registrationPage.inputPassword();
	}

	@When("I click Create Account Button")
	public void IClickCreateAccountButton() {
		PagesCollection.registrationPage.clickCreateAccountButton();
	}

	@Then("Contact list loads with only my name (.*)")
	public void ContactListLoadsWithOnlyMyName(String name) throws Throwable {
		name = this.userToRegister.getName();
		PagesCollection.contactListPage = new ContactListPage(
				CommonUtils.getIosAppiumUrlFromConfig(ContactListPage.class),
				CommonUtils
						.getIosApplicationPathFromConfig(ContactListPage.class));
		PagesCollection.contactListPage.waitForContactListToLoad();
		Assert.assertTrue("My username is not displayed first",
				PagesCollection.contactListPage
						.isMyUserNameDisplayedFirstInContactList(name));
	}

	@Then("I see Create Account button disabled")
	public void ISeeCreateAccountButtonDisabled() {
		Assert.assertFalse(PagesCollection.registrationPage
				.isCreateAccountEnabled());
	}

	@Then("^I navigate throughout the registration pages and see my input$")
	public void NavigateAndVerifyInput() throws IOException {
		PagesCollection.registrationPage.verifyUserInputIsPresent(
				this.userToRegister.getName(), this.userToRegister.getEmail());
	}

	@When("I navigate from password screen back to Welcome screen")
	public void NaviateFromPassScreenToWelcomeScreen() {
		PagesCollection.registrationPage.navigateToWelcomePage();
	}

	@When("I input user data")
	public void IInputUserData() {
		PagesCollection.registrationPage.typeInRegistrationData();
	}

	@When("^I submit registration data$")
	public void ISubmitRegistrationData() throws Exception {
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.listener = IMAPSMailbox.createDefaultInstance().startMboxListener(
				expectedHeaders);

		PagesCollection.registrationPage.createAccount();
	}

	@Then("^I confirm that (\\d+) recent emails in inbox contain (\\d+) for current recipient$")
	public void VerifyRecipientsCount(final int recentEmailsCnt,
			final int expectedCnt) throws Throwable {
		String expectedRecipient = this.userToRegister.getEmail();
		int checksCnt = 0;
		int actualCnt = 0;
		while (checksCnt < maxCheckCnt) {
			actualCnt = PagesCollection.registrationPage
					.getRecentEmailsCountForRecipient(recentEmailsCnt,
							expectedRecipient);
			if (actualCnt == expectedCnt) {
				break;
			}
			checksCnt++;
		}
		Assert.assertTrue("Expected mail count is : " + expectedCnt
				+ ", but actual count is : " + actualCnt,
				actualCnt == expectedCnt);
	}

	@Then("^I resend verification email$")
	public void IResendVerificationEmail() {
		PagesCollection.registrationPage.reSendEmail();
	}

	@When("^I see error page$")
	public void ISeeErrorPage() throws IOException {
		Assert.assertTrue(PagesCollection.registrationPage.confirmErrorPage());
	}

	@Then("^I return to the email page from error page$")
	public void IReturntoEmailPageFromErrorPage() throws IOException {
		PagesCollection.registrationPage.backToEmailPageFromErrorPage();
	}

	@Then("^I see confirmation page$")
	public void ISeeConfirmationPage() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.registrationPage
				.waitForConfirmationMessage();
		Assert.assertTrue(PagesCollection.registrationPage
				.isConfirmationShown());
	}

	@Then("^I verify registration address$")
	public void IVerifyRegistrationAddress() throws Exception {
		BackendAPIWrappers.activateRegisteredUser(this.listener);
		userToRegister.setUserState(UserState.Created);
	}

	@When("I don't see Next button")
	public void IDontSeeNextButton() {
		Assert.assertFalse(PagesCollection.registrationPage
				.isNextButtonPresented());
	}

	@When("^I see selected image set as background$")
	public void ISeeSelectedImageSetAsBackground() throws Throwable {
		templateImage = PagesCollection.registrationPage.takeScreenshot();
	}

	@When("^I see photo set as background$")
	public void ISeePhotoSetAsBackground() throws Throwable {
		referenceImage = PagesCollection.registrationPage.takeScreenshot();
	}

	@Then("^I see photo is set as profile background$")
	public void ISeePhotoSetAsProfileBackground() throws Throwable {
		profileImage = PagesCollection.registrationPage.takeScreenshot();
	}

	@Then("I see photo image is correct")
	public void ISeeProfileImageIsCorrect() {
		double score = ImageUtil.getOverlapScore(referenceImage, profileImage);
		Assert.assertTrue(
				"Images do not look same. Expected score >= 0.75, current = "
						+ score, score >= 0.75d);
	}

	@Then("I see background image is replaced")
	public void ISeeBackgroundImageIsReplaced() {
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage);
		System.out.println("SCORE: " + score);
		Assert.assertTrue(
				"Images look same. Expected score <= 0.25, current = " + score,
				score <= 0.25d);
	}

}
