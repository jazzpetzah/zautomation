package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.TabletFirstTimeOverlay;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class FirstTimeOverlaySteps {
	private TabletFirstTimeOverlay getFirstTimeOverlay() throws Exception {
		return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
				.getPage(TabletFirstTimeOverlay.class);
	}


    /**
     * Confirms the First Time overlay is visible or not
     *
     * @param shouldNotBeVisible is set to null if "do not" part is not present
     * @throws Exception
     * @step. ^I( do not)? see First Time overlay$"
     */
    @Then("^I( do not)? see First Time overlay$")
    public void ThenISeeFirstTimeOverlay(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("First Time overlay is not visible after timeout",
                    getFirstTimeOverlay().isVisible(20));
        } else {
            Assert.assertTrue("First Time overlay is still visible after timeout",
                    getFirstTimeOverlay().isInvisible());
        }
    }

    /**
     * Tap Got It button
     * <p/>
     * @step. ^I tap Got It button on First Time overlay$
     *
     * @throws Exception
     */
    @When("^I tap Got It button on First Time overlay$")
    public void ITapGotItButton() throws Exception {
        getFirstTimeOverlay().tapGotItButton();
    }

	/**
	 * Accept the First Time overlay as soon as it is visible
	 *
	 * @step. ^I accept First Time overlay as soon as it is visible$
	 *
	 * @throws Exception
	 */
	@And("^I accept First Time overlay as soon as it is visible$")
	public void IAcceptTheOverLayWhenItIsVisible() throws Exception {
		getFirstTimeOverlay().acceptWhenVisible(CommonAndroidTabletSteps.FIRST_TIME_OVERLAY_TIMEOUT);
	}
}
