package com.wearezeta.auto.android.pages.cursor;


import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class CameraOverlayPage extends CursorOverlayPage {

    private static final String strIdPictureThumbnail = "iv__cursor_gallery_item";

    private static final By idCameraSwitchButton = By.id("gtv__cursor_image__front_back");

    private static final By idOpenCameraButton = By.id("gtv__cursor_image__open_camera");

    private static final By idOpenExternalVideoButton = By.id("gtv__cursor_image__open_video");

    private static final By idTakePictureButton = By.id("gtv__cursor_image__take_picture");

    private static final By idNavigationOpenGalleryButton = By.id("gtv__cursor_image__nav_open_gallery");

    private static final By idNavigationCameraBackButton = By.id("gtv__cursor_image__nav_camera_back");

    private static final By idPictureThumbnail = By.id(strIdPictureThumbnail);

    private static final Function<Integer, String>
            xpathStrThumbnailItem = index -> String.format("(//*[@id='%s'])[%d]", strIdPictureThumbnail, index);

    private static final int NUMBER_OF_EACH_COLUMN = 3;

    public CameraOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapOnButton(String buttonName) throws Exception {
        By locator = getButtonLocator(buttonName);
        getElement(locator, String.format("The button %s is not visible in extended cursor camera layout", buttonName))
                .click();
    }

    public void tapOnThumbnail(int row, int col) throws Exception {
        if (row < 1 || col < 1) {
            throw new IllegalStateException(
                    String.format("The row and col of thumbnail should be start from 1. (%d,%d)", row, col));
        }
        int index = (row - 1) * NUMBER_OF_EACH_COLUMN + col;
        By locator = By.xpath(xpathStrThumbnailItem.apply(index));
        getElement(locator, String.format("Cannot find the thumbnail in row %d, col %d", row, col)).click();
    }

    public boolean waitUntilButtonVisible(String buttonName) throws Exception {
        By locator = getButtonLocator(buttonName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilButtonInvisible(String buttonName) throws Exception {
        By locator = getButtonLocator(buttonName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilOverlayVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idExtendedCursorContainer);
    }

    public boolean waitUntilOverlayInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idExtendedCursorContainer);
    }

    public boolean waitUntilThumbnailsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idPictureThumbnail);
    }

    public void swipeLeftOnOverlay(int durationMilliseconds)
            throws Exception {
        DriverUtils.swipeRight(this.getDriver(),
                this.getDriver().findElement(idExtendedCursorContainer), durationMilliseconds,
                90, 50, 20, 50);
    }

    private By getButtonLocator(String buttonName) {
        switch (buttonName.toLowerCase()) {
            case "take photo":
                return idTakePictureButton;
            case "switch camera":
                return idCameraSwitchButton;
            case "gallery":
                return idNavigationOpenGalleryButton;
            case "camera":
                return idOpenCameraButton;
            case "external video":
                return idOpenExternalVideoButton;
            case "back":
                return idNavigationCameraBackButton;
            default:
                throw new IllegalStateException(
                        String.format("Do not support button %s in extended cursor camera layout", buttonName));
        }
    }

}
