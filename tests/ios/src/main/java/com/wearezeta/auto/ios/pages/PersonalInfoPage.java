package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.SwipeDirection;
import com.wearezeta.auto.common.IOSLocators;

public class PersonalInfoPage extends IOSPage{
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathEmailField)
	private WebElement emailField;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathUserProfileName)
	private WebElement profileNameField;
	
	@FindBy(how = How.NAME, using = IOSLocators.classNameUIAButton)
	private List<WebElement> optionsButtons;
	
	public PersonalInfoPage(String URL, String path) throws IOException {
		super(URL, path);

	}
	
	public void waitForEmailFieldVisible(){
		
		wait.until(ExpectedConditions.visibilityOf(emailField));
	}
	
	public void tapOptionsButtonByText(String buttonText){
		
		 for(WebElement button : optionsButtons)
		 {
			 if (button.getText().equals(buttonText))
			 {
				 button.click();
				 break;
			 }
		 }
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) {
		
		IOSPage page = null;
		switch (direction){
		case DOWN:
		{
			break;
		}
		case UP:
		{
			page = this;
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
