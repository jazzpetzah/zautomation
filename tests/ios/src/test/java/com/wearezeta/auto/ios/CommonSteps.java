package com.wearezeta.auto.ios;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.UsersState;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
	
	@Before
	 public void setUp() throws Exception {
		if(Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class)) && !CommonUtils.yourUserState.equals(UsersState.AllContactsConnected)){
			CommonUtils.generateUsers(2);
		}
		
		String path = CommonUtils.getAppPathFromConfig(TestRun.class);
		
		if ( PagesCollection.loginPage == null)
		{
			PagesCollection.loginPage = new LoginPage(CommonUtils.getUrlFromConfig(TestRun.class), path);
		}
		
	    if(Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class)) && !CommonUtils.yourUserState.equals(UsersState.AllContactsConnected)){
	    	IOSTestPreparation.createContactLinks();
	    }
	 }
	 
	 @After
	 public void tearDown() throws Exception {

		 PagesCollection.loginPage.Close();
		 IOSPage.clearPagesCollection();
	 }

}
