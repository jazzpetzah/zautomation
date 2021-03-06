package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.ManageDevicesOverlay;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class ManageDeviceOverlaySteps {
    private ManageDevicesOverlay getManageDevicesOverlay() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(ManageDevicesOverlay.class);
    }

    /**
     * Verify whether Manage Devices overlay is visible
     *
     * @param shouldNotSee equals to null if the overlay should be visible
     * @throws Exception
     * @step. I (do not )?see Manage Devices overlay$
     */
    @Then("^I (do not )?see Manage Devices overlay$")
    public void ISeeManageDevicesOverlay(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Manage Devices overlay is not visible", getManageDevicesOverlay().waitUntilVisible());
        } else {
            Assert.assertTrue("Manage Devices overlay is not visible", getManageDevicesOverlay().waitUntilInvisible());
        }
    }
}
