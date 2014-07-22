package com.wearezeta.auto.android.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.SwipeDirection;
//TODO: remove this page
public class InstructionsPage extends AndroidPage {

	@FindBy(how = How.ID, using = AndroidLocators.idInstructions)
	private WebElement instructions;
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestDialog)
	private WebElement connectDilog;
	@FindBy(how = How.ID, using = AndroidLocators.idInstructionsRequestIgnoreBtn)
	private List<WebElement> ignoreBtnList;
	@FindBy(how = How.ID, using = AndroidLocators.idInstructionsRequestConnectBtn)
	private List<WebElement> connectBtnList;
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestConnectTo)
	private WebElement requestHeader;
	
	private String url;
	private String path;

	public InstructionsPage(String URL, String path) throws Exception {
		super(URL, path);
		url = URL;
		this.path = path;				
	}

	public boolean isConnectDialogDispalayed()
	{
		wait.until(ExpectedConditions.visibilityOf(connectDilog));
		return connectDilog.isDisplayed();
	}

	public void acceptAllConnections()
	{
		for(WebElement connectBtn : connectBtnList){
			connectBtn.click();
		}
	}

	public String getConnectionRequestHeader()
	{
		return requestHeader.getText();
	}
	public void waitInstructionsPage()
	{
		wait.until(ExpectedConditions.visibilityOf(instructions));
	}
	
	public void ignoreAllConnections()
	{
		for(WebElement ignoreBtn : connectBtnList){
			ignoreBtn.click();
		}
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {

		AndroidPage page = null;
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
			page = new ContactListPage(url, path);
			break;
		}
		}	
		return page;
	}

}
