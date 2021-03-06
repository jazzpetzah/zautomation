package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.TabletTakePicturePage;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class TakePicturePageSteps {
    private TabletTakePicturePage getTakePicturePage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletTakePicturePage.class);
    }

    /**
     * Tap the corresponding button on Take Picture view
     *
     * @param buttonName the button to press
     * @throws Exception
     * @step. ^I tap (Take Photo|Change Photo|Confirm|Cancel|Gallery Camera|Image Close|Switch Camera|Sketch Image Paint|Sketch Emoji Paint|Sketch Text Paint|Close) button on Take Picture view$
     */
    @When("^I tap (Take Photo|Change Photo|Confirm|Cancel|Gallery Camera|Image Close|Switch Camera|Sketch Image Paint|Sketch Emoji Paint|Sketch Text Paint|Close) button on Take Picture view$")
    public void WhenIPressButton(String buttonName) throws Exception {
        getTakePicturePage().tapOnButton(buttonName);
    }

    /**
     * Verify whether the particular button is visible on Take Picture view
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param buttonName   one of possible button names
     * @throws Exception
     * @step. ^I (do not )?see (Take Photo|Change Photo) button on Take Picture view$
     */
    @Then("^I (do not )?see (Take Photo|Change Photo) button on Take Picture view$")
    public void ISeeButtonOnTakePictureView(String shouldNotSee, String buttonName) throws Exception {
        FunctionalInterfaces.ISupplierWithException<Boolean> verificationFunc;
        switch (buttonName.toLowerCase()) {
            case "take photo":
                verificationFunc = (shouldNotSee == null) ? getTakePicturePage()::isTakePhotoButtonVisible :
                        getTakePicturePage()::isTakePhotoButtonInvisible;
                break;
            case "change photo":
                verificationFunc = (shouldNotSee == null) ? getTakePicturePage()::isChangePhotoButtonVisible :
                        getTakePicturePage()::isChangePhotoButtonInvisible;
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name: '%s'", buttonName));
        }
        Assert.assertTrue(String.format("The %s button should %s visible on the Take Picture view",
                buttonName, (shouldNotSee == null) ? "be" : "not be"), verificationFunc.call());
    }

}
