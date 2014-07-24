package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.ArrayList;

import javax.mail.MessagingException;

import org.apache.xalan.xsltc.runtime.MessageHandler;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.EmailHeaders;
import com.wearezeta.auto.common.IMAPMailbox;
import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.common.DriverUtils;
>>>>>>> 64fd2294c4afa4dca8dd62772ee823045b208671
import com.wearezeta.auto.common.SwipeDirection;

public class RegistrationPage extends IOSPage {
	
	
	@FindBy(how = How.NAME, using = IOSLocators.nameRegistrationCameraButton)
	private WebElement cameraButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCameraShootButton)
	private WebElement cameraShootButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePhotoButton)
	private WebElement photoButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSwitchCameraButton)
	private WebElement switchCameraButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCameraFlashButton)
	private WebElement cameraFlashButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathAlbum)
	private WebElement photoAlbum;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNamePhotos)
	private List<WebElement> photos;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConfirmImageButton)
	private WebElement confirmImageButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCancelImageButton)
	private WebElement cancelImageButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathYourName)
	private WebElement yourName;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameYourEmail)
	private WebElement yourEmail;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameYourPassword)
	private WebElement yourPassword;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathRevealPasswordButton)
	private WebElement revealPasswordButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathHidePasswordButton)
	private WebElement hidePasswordButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCreateAccountButton)
	private WebElement createAccountButton;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameConfirmationMessage)
	private WebElement confirmationText;
	
	@FindBy(how = How.ID, using = IOSLocators.idProvideValidEmailMessage)
	private WebElement provideValidEmailMessage;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameBackToWelcomeButton)
	private WebElement backToWelcomeButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameForwardWelcomeButton)
	private WebElement forwardWelcomeButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameKeyboardNextButton)
	private WebElement keyboardNextButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathTakePhotoSmile)
	private WebElement takePhotoSmile;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameTakePhotoHintLabel)
	private WebElement takePhotoHintLabel;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameVignetteOverlay)
	private WebElement vignetteLayer;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameErrorPageButton)
	private WebElement errorPageButton; 
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathCloseColorModeButton)
	private WebElement closeColorModeButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathReSendButton)
	private WebElement reSendButton;
	
	private String name;
	private String email;
	private String password;
	
	private String usernameTextFieldValue;
	private String emailTextFieldValue;
	private String passwordTextFieldValue;
	
	private String confirmMessage = "We sent an email to %s. Check your Inbox and follow the link to verify your address. You won’t be able to use Zeta until you do.\n\nDidn’t get the message?\n\nRe-send";

	private String[] listOfEmails;
	private String recipientInboxCount;
	
	public RegistrationPage(String URL, String path)
			throws MalformedURLException {
		super(URL, path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public boolean isTakePhotoSmileDisplayed(){
		return takePhotoSmile.isEnabled();
	}
	
	public boolean isTakeOrSelectPhotoLabelVisible(){
		return DriverUtils.isElementDisplayed(takePhotoHintLabel);
	}
	
	public boolean isNameLabelVisible(){
		return yourName.isDisplayed();
	}
	
	public void clickCameraButton()
	{
		cameraButton.click();
	}
	
	public void clickCameraShootButton()
	{
		cameraShootButton.click();
	}
	
	public void takePhotoByFrontCamera(){
		clickCameraShootButton();
	}
	
	public void clickSwitchCameraButton(){
		switchCameraButton.click();
	}
	
	public void switchToFrontCamera(){
			clickSwitchCameraButton();
		}		
	
	public void takePhotoByRearCamera(){
		switchToRearCamera();
		clickCameraShootButton();
	}
	
	public void switchToRearCamera(){
			clickSwitchCameraButton();
		}
	
	public void selectPicture()
	{
		photoButton.click();
		
	}
	
	public void chooseFirstPhoto() {
		photoAlbum.click();
		photos.get(0).click();
	}
	
	public void clickVignetteLayer(){
		vignetteLayer.click();
	}
	
	public void dismissVignetteBakground(){
		vignetteLayer.click();
		driver.tap(1, vignetteLayer.getLocation().x + 10 , vignetteLayer.getLocation().y + 10, 1);
	}
	
	public boolean isVignetteOverlayVisible(){
		return vignetteLayer.isDisplayed();
	}
	
	public void tapCloseColorModeButton(){
		closeColorModeButton.click();
	}
	
	public boolean isColorModeVisible(){
		return closeColorModeButton.isDisplayed();
	}

	public void waitForConfirmationMessage(){
		DriverUtils.waitUntilElementAppears(driver, By.className(IOSLocators.classNameConfirmationMessage));
	}
	
	public boolean isConfirmationShown()
	{
		String expectedMessage = null;
		String actualMessage = null;
		expectedMessage = String.format(confirmMessage, getEmail());
		actualMessage = confirmationText.getText();
		return actualMessage.equals(expectedMessage);
	}
	
	public void confirmPicture()
	{
		confirmImageButton.click();
	}
	
	public void cancelImageSelection(){
		cancelImageButton.click();
	}
	
	
	public void hideKeyboard(){
		driver.hideKeyboard();
	}
	
	public void inputName(){
		yourName.sendKeys(getName() + "\n");
	}
	
	public void inputEmail(){
		yourEmail.sendKeys(getEmail() + "\n");
	}
	
	public void inputPassword(){
		yourPassword.sendKeys(getPassword() + "\n");
	}
	
	public void clickCreateAccountButton(){
		createAccountButton.click();
	}
		
	public void createAccount()
	{
		try {
			if(ExpectedConditions.presenceOfElementLocated(By.xpath(IOSLocators.xpathYourName)) != null) {
				yourName.sendKeys(getName() + "\n");
			}
		} catch (NoSuchElementException e) { }
		if(ExpectedConditions.presenceOfElementLocated(By.name(IOSLocators.nameYourEmail)) != null) {
			yourEmail.sendKeys(getEmail() + "\n");
		}
		if(ExpectedConditions.presenceOfElementLocated(By.name(IOSLocators.nameYourPassword)) != null) {
			yourPassword.sendKeys(getPassword());
		}
		createAccountButton.click();
	}
	
	public void typeEmail()
	{
		yourName.sendKeys(getName() + "\n");
		yourEmail.sendKeys(getEmail());
	}

	public void retypeEmail() {
		if (ExpectedConditions.presenceOfElementLocated(By
				.name(IOSLocators.nameYourEmail)) != null) {
			yourEmail.sendKeys(getEmail());
		}
	}

	public void returnToConfirmRegistration() {
		forwardWelcomeButton.click();
		createAccountButton.click();
	}
	
	public boolean typeAllInvalidEmails()
	{
		yourName.sendKeys(getName() + "\n");
		for(int i=0; i<listOfEmails.length;i++){
			yourEmail.sendKeys(listOfEmails[i]+"\n");
			if(!provideValidEmailMessage.isDisplayed()){
			return false;
			}

		}
		return true;
	}
	
	
	public void typeInRegistrationData()
	{
		yourName.sendKeys(getName() + "\n");
		yourEmail.sendKeys(getEmail() + "\n");
		yourPassword.sendKeys(getPassword());
	}
	
	public boolean isCreateAccountEnabled(){
		return createAccountButton.isEnabled();
	}
	
	public void typeAndStoreAllValues()
	{
		yourName.sendKeys(getName());
		usernameTextFieldValue = yourName.getText();
		yourName.sendKeys(getName()+"\n");
		yourEmail.sendKeys(getEmail());
		emailTextFieldValue = yourEmail.getText();
		yourEmail.sendKeys(getEmail()+"\n");
		yourPassword.sendKeys(getPassword());
		driver.tap(1, revealPasswordButton.getLocation().x + 1, revealPasswordButton.getLocation().y + 1, 1);
		passwordTextFieldValue = yourPassword.getText();
	}

	
	public boolean verifyUserInputIsPresent()
	//this test skips photo verification
	{
		
		PagesCollection.loginPage.clickJoinButton();
		forwardWelcomeButton.click(); //skip photo
		if(!yourName.getText().equals(usernameTextFieldValue)){
			return false;
		}
		forwardWelcomeButton.click();
		if(!yourEmail.getText().equals(emailTextFieldValue)){
			return false;
		}
		forwardWelcomeButton.click();
		if(!yourPassword.getText().equals(passwordTextFieldValue)){
			driver.tap(1, revealPasswordButton.getLocation().x + 1, revealPasswordButton.getLocation().y + 1, 1);
			if(!yourPassword.getText().equals(passwordTextFieldValue)){
			return false;
			}
		}
		System.out.println("password verified\n");
		return true;
	}
	
	public void navigateToCreateAccount()
	{
		forwardWelcomeButton.click();
	}
	
	public void typeUsername() 
	{
		yourName.sendKeys(getName());
	}
	
	public String getUsernameFieldValue()  
	{
		return yourName.getText();
	}

	
	public String getEmailFieldValue()
	{
		return yourEmail.getText();		
	}
	
	public boolean isPictureSelected()
	{
		return confirmImageButton.isDisplayed();
	}
	
	public boolean isConfirmationVisible()
	{
		return confirmationText.isDisplayed();
	}
	
	public boolean confirmErrorPage() {
		return errorPageButton.isDisplayed();
	}
	
	public void backToEmailPageFromErrorPage() {
		backToWelcomeButton.click();
		backToWelcomeButton.click();
	}
	
	
	public void navigateToWelcomePage()
	{
		while(backToWelcomeButton.isDisplayed()){
			backToWelcomeButton.click();
		}
	}
	
	public boolean isNextButtonPresented(){
		return forwardWelcomeButton.isDisplayed();
	}
		
	public void tapBackButton(){
		backToWelcomeButton.click();
	}
	
	public void tapForwardButton(){
		forwardWelcomeButton.click();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String[] getListOfEmails() {
		return listOfEmails;
	}
	
	public void setListOfEmails(String[] list){
		this.listOfEmails = list;
	}
	
	public void reSendEmail() {
		driver.tap(1, (reSendButton.getLocation().x)
				+ (reSendButton.getSize().width / 2),
				(reSendButton.getLocation().y)
						+ (reSendButton.getSize().height - 5), 1);
	}
	
	public boolean emailInboxCount() throws IOException {
	  List<EmailHeaders> messageHeaders = CreateZetaUser.getLastMailHeaders(CommonUtils.getDefaultEmailFromConfig(CreateZetaUser.class),CommonUtils.getDefaultPasswordFromConfig(CreateZetaUser.class),0);
      String inboxCount = messageHeaders.get(messageHeaders.size()).getLastUserEmail();
    if (inboxCount.equals(null)){
    	return false;
    }
    else {
    	System.out.println(inboxCount);
    	return true;
    }
    	  
    }
	
	 public int getRecentEmailsCountForRecipient(int allRecentEmailsCnt, String expectedRecipient) throws MessagingException, IOException {
	     IMAPMailbox mailbox = new IMAPMailbox(CreateZetaUser.MAIL_SERVER, CreateZetaUser.MAILS_FOLDER,
	    		 CommonUtils.getDefaultEmailFromConfig(RegistrationPage.class), 
	    		 CommonUtils.getDefaultPasswordFromConfig(RegistrationPage.class));
		 int actualCnt = 0;
		 try {
			 mailbox.open();
			 List<EmailHeaders> allEmailsHeaders = mailbox.getLastMailHeaders(allRecentEmailsCnt);
			 for (EmailHeaders emailHeaders: allEmailsHeaders) {
				 if (emailHeaders.getLastUserEmail().equals(expectedRecipient)) {
					 actualCnt++; 
				 }
			 }
		 } finally {
			 mailbox.close();
		 }
		 return actualCnt;
	 }
	
}
 
