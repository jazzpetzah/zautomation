package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.external.DownloadPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DownloadPageSteps {

    private final WebAppTestContext context;

    public DownloadPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @When("^I navigate to download page$")
    public void INavigateToDownloadPage() throws Exception {
        final String website = CommonUtils.getWebsitePathFromConfig(DownloadPageSteps.class);
        context.getPagesCollection().getPage(DownloadPage.class).setUrl(website + "/download/");
        context.getPagesCollection().getPage(DownloadPage.class).navigateTo();
    }

    @Then("^I see button for iOS$")
    public void ISeeButtonForIOS() throws Exception {
        String storeUrl = "https://itunes.apple.com/app/wire/id930944768?mt=8";
        assertThat(context.getPagesCollection().getPage(DownloadPage.class).getIOSDownloadHref(), equalTo(storeUrl));
    }

    @Then("^I see button for Android$")
    public void ISeeButtonForAndroid() throws Exception {
        String storeUrl = "https://play.google.com/store/apps/details?id=com.wire";
        assertThat(context.getPagesCollection().getPage(DownloadPage.class).getAndroidDownloadHref(), equalTo(storeUrl));
    }

    @Then("^I see button for OS X$")
    public void ISeeButtonForOSX() throws Exception {
        String storeUrl = "https://itunes.apple.com/app/wire/id931134707?mt=12";
        assertThat(context.getPagesCollection().getPage(DownloadPage.class).getOSXDownloadHref(), equalTo(storeUrl));
    }

    @Then("^I see button for Windows$")
    public void ISeeButtonForWindows() throws Exception {
        String storeUrl = "https://wire-app.wire.com/win/prod/WireSetup.exe";
        assertThat(context.getPagesCollection().getPage(DownloadPage.class).getWindowsDownloadHref(), equalTo(storeUrl));
    }

    @Then("^I see button for Webapp$")
    public void ISeeButtonForWebapp() throws Exception {
        String storeUrl = "https://wire-webapp-staging.zinfra.io/";
        assertThat(context.getPagesCollection().getPage(DownloadPage.class).getWebappDownloadHref(), equalTo(storeUrl));
    }
}
