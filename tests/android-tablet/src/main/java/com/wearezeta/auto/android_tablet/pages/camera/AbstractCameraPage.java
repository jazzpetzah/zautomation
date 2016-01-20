package com.wearezeta.auto.android_tablet.pages.camera;

import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AbstractCameraPage extends AndroidTabletPage {
    public static final String idGalleryButton = "gtv__camera_control__pick_from_gallery";
    @FindBy(id = idGalleryButton)
    private WebElement galleryButton;

    public static final String idTakePhotoButton = "gtv__camera_control__take_a_picture";
    @FindBy(id = idTakePhotoButton)
    protected WebElement takePhotoButton;

    public AbstractCameraPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    protected abstract By getLensButtonLocator();

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                getLensButtonLocator(), 20);
    }

    public void tapLensButton() throws Exception {
        this.getDriver().findElement(getLensButtonLocator()).click();
    }

    public void tapTakePhotoButton() throws Exception {
        assert waitUntilTakePhotoButtonVisible() : "Take Photo button is not visible, but it should be";
        takePhotoButton.click();
    }

    public boolean waitUntilTakePhotoButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idTakePhotoButton));
    }

    private boolean isGalleryModeActivated = false;

    public void confirmPictureSelection() throws Exception {
        final Optional<WebElement> confirmButton = getElementIfDisplayed(DialogPage.xpathConfirmOKButton);
        if (confirmButton.isPresent()) {
            confirmButton.get().click();
        } else {
            // Workaround for unexpected orientation change issue
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getLensButtonLocator())) {
                tapLensButton();
            }
            if (isGalleryModeActivated) {
                tapGalleryButton();
                isGalleryModeActivated = false;
            } else {
                tapTakePhotoButton();
            }
            getElement(DialogPage.xpathConfirmOKButton,
                    "Picture selection confirmation has not been shown after the timeout", 5);
        }
        ScreenOrientationHelper.getInstance().fixOrientation(getDriver());
    }

    public void tapGalleryButton() throws Exception {
        getElement(By.id(idGalleryButton), "Open gallery button is not visible").click();
        isGalleryModeActivated = true;
    }

}
