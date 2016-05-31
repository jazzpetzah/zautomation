package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.TakePicturePage;
import com.wearezeta.auto.android.pages.registration.AddNamePage;
import com.wearezeta.auto.android.pages.registration.AreaCodePage;
import com.wearezeta.auto.android.pages.registration.PhoneNumberVerificationPage;
import com.wearezeta.auto.android.pages.registration.WelcomePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class TakePicturePageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private TakePicturePage getTakePicturePage() throws Exception {
        return pagesCollection.getPage(TakePicturePage.class);
    }

    /**
     * Tap the corresponding button on Take Picture view
     *
     * @param buttonName the button to press
     * @throws Exception
     * @step. ^I tap "(Take Photo|Confirm|Gallery|Image Close|Switch Camera|Sketch Image Paint|Close)" button on Take Picture view$
     */
    @When("^I tap (Take Photo|Change Photo|Confirm|Gallery|Image Close|Switch Camera|Sketch Image Paint|Close) button on Take Picture view$")
    public void WhenIPressButton(String buttonName) throws Exception {
        switch (buttonName.toLowerCase()) {
            case "take photo":
                getTakePicturePage().takePhoto();
                break;
            case "change photo":
                getTakePicturePage().tapChangePhotoButton();
                break;
            case "confirm":
                getTakePicturePage().confirm();
                break;
            case "gallery":
                getTakePicturePage().openGallery();
                break;
            case "image close":
                getTakePicturePage().closeFullScreenImage();
                break;
            case "close":
                getTakePicturePage().tapCloseTakePictureViewButton();
                break;
            case "switch camera":
                if (!getTakePicturePage().tapSwitchCameraButton()) {
                    throw new PendingException(
                            "Device under test does not have front camera. " + "Skipping all the further verification...");
                }
                break;
            case "sketch image paint":
                getTakePicturePage().tapSketchOnImageButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name: '%s'", buttonName));
        }
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