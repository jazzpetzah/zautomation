package com.wearezeta.auto.android_tablet.steps.details_overlay.single;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.details_overlay.single.TabletSinglePendingOutgoingConnectionPage;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class SinglePendingOutgoingConnectionPageSteps {
    private TabletSinglePendingOutgoingConnectionPage getSinglePendingOutgoingConnectionPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletSinglePendingOutgoingConnectionPage.class);
    }

    /**
     * Verify I see avatar
     *
     * @throws Exception
     * @step. ^I see avatar on Single pending outgoing connection page$
     */
    @Then("^I see avatar on Single pending outgoing connection page$")
    public void ISeeAvatar() throws Exception {
        Assert.assertTrue("The pending avatar is invisible",
                getSinglePendingOutgoingConnectionPage().waitUntilAvatarVisible());
    }

    /**
     * Verify user data (User name, Unique user name)
     *
     * @param shouldNotSee  equals null means the item should be visible
     * @param type          which could be user name, unique user name
     * @param text the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Single pending outgoing connection page$
     */
    @Then("^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Single pending outgoing connection page$")
    public void ISeeUserData(String shouldNotSee, String type, String text) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getSinglePendingOutgoingConnectionPage().waitUntilUserDataVisible(type, text));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getSinglePendingOutgoingConnectionPage().waitUntilUserDataInvisible(type, text));
        }
    }
}
