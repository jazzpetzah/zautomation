package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.pages.external.VerifyPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VerifyPageSteps {
	
        private final TestContext context;
        
    public VerifyPageSteps() {
        this.context = new TestContext();
    }

    public VerifyPageSteps(TestContext context) {
        this.context = context;
    }
	
	/**
	 * Open verify page on website
	 *
	 * @step. ^I navigate to broken verify page for (.*)$
	 *
	 * @throws Exception
	 */
	@When("^I go to verify page for (.*)$")
	public void IGoToVerifyPage(String agent) throws Exception {
		context.getPagesCollection().getPage(VerifyPage.class).setUrl(
				WebAppConstants.STAGING_SITE_ROOT + "/verify/%3Fsuccess%26agent=" + agent);
		context.getPagesCollection().getPage(VerifyPage.class).navigateTo();		
	}
	
	@When("^I go to broken verify page for (.*)$")
	public void IGoToBrokenVerifyPage(String agent) throws Exception {
		context.getPagesCollection().getPage(VerifyPage.class).setUrl(
				WebAppConstants.STAGING_SITE_ROOT + "/verify/%3Fagent=" + agent);
		context.getPagesCollection().getPage(VerifyPage.class).navigateTo();		
	}
	
	@Then("^I see download button for (.*)$")
	public void ISeeDownloadButton(String agent) throws Exception {
		String downloadLink = "";
		switch (agent) {
			case "iphone":
				downloadLink = "https://itunes.apple.com/app/wire/id930944768?mt=8";
				break;
			case "android":
				downloadLink = "https://play.google.com/store/apps/details?id=com.wire";
				break;
			case "osx":
				downloadLink = "https://itunes.apple.com/app/wire/id931134707?mt=12";
				break;
			case "windows":
				downloadLink = "https://staging-website.zinfra.io/download/";
				break;
			default: break;
		}
		assertThat(context.getPagesCollection().getPage(VerifyPage.class).getDownloadUrl(agent), equalTo(downloadLink));
	}
	
	@Then("^I see webapp button$")
	public void ISeeWebappButton() throws Exception{
		assertThat(context.getPagesCollection().getPage(VerifyPage.class).getWebappUrl(), equalTo("https://wire-webapp-staging.zinfra.io/"));
	}
	
	@Then("^I see error message$")
	public void ISeeErrorMessage() throws Exception {
		assertThat(context.getPagesCollection().getPage(VerifyPage.class).isErrorMessageVisible(), is(true));
	}
	

}
