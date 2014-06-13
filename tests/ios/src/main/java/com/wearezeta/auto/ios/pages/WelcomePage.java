package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.ios.pages.PersonalInfoPage;
import com.wearezeta.auto.common.*;

public class WelcomePage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.nameWelcomeLabel)
	private WebElement welcomeLabel;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAcceptConnectionButton)
	private List<WebElement> acceptBtnList;
	
	private String url;
	private String path;
	
	public WelcomePage(String URL, String path) throws IOException {
		super(URL, path);
		url = URL;
		this.path = path;				
	}
	
	public void acceptAllConnections() {
		for(WebElement connectBtn : acceptBtnList){
			try {
				connectBtn.click();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		
		IOSPage page = null;
		switch (direction){
			case DOWN:
			{
				break;
			}
			case UP:
			{
				break;
			}
			case LEFT:
			{
				page = new PersonalInfoPage(url, path);
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
