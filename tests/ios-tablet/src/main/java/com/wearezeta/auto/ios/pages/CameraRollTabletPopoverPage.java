package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.tablet.locators.IOSTabletLocators;

public class CameraRollTabletPopoverPage extends CameraRollPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCameraLibraryButton)
	private WebElement cameraLibraryButton;
	
	@FindBy(how = How.XPATH, using = IOSTabletLocators.CameraRollTabletPopoverPage.xpathIPADCameraLibraryFirstFolder)
	private WebElement cameraLibraryFirstFolderiPadPopover;
	
	@FindBy(how = How.XPATH, using = IOSTabletLocators.CameraRollTabletPopoverPage.xpathIPADCameraLibraryFirtImage)
	private WebElement ipadPopoverLibraryFirstPicture;
	
	@FindBy(how = How.XPATH, using = IOSTabletLocators.CameraRollTabletPopoverPage.xpathIPADCameraLibraryConfirmButton)
	private WebElement confirmPictureButton;
	
	public CameraRollTabletPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
		

	public void pressSelectFromLibraryButtoniPad() throws Exception {
		cameraLibraryButton.click();
	}

	public void selectImageFromLibraryiPad() throws Exception {
		try {
			clickFirstLibraryFolderiPad();
		} catch (NoSuchElementException ex) {

		}

		clickFirstImage();
	}

	public void clickFirstLibraryFolderiPad() throws Exception {
		cameraLibraryFirstFolderiPadPopover.click();
	}

	public void clickFirstImage() throws Exception {
		ipadPopoverLibraryFirstPicture.click();
	}

	public void pressConfirmButtoniPad() throws Exception {
		confirmPictureButton.click();
	}


	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
