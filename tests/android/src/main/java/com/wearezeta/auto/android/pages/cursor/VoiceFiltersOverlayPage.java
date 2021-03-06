package com.wearezeta.auto.android.pages.cursor;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class VoiceFiltersOverlayPage extends CursorOverlayPage {

    private static final By idVoiceRecordingContainer = By.id("vfc");
    private static final By idVoiceFiltersContainer = By.id("ll__voice_filter__grid_container");
    private static final By idRecordButton = By.id("gtv__record_button");
    private static final By idApproveButton = By.id("v__voice_approve");
    private static final By idVoiceGraph = By.id("wbv__voice_filter");

    private static final Function<Integer, String> xpathFilterItemButton = index -> String
            .format("(//*[@id='gtv__voice_filter_icon'])[%d]", index);


    public VoiceFiltersOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapButton(String buttonName) throws Exception {
        By locator = getButtonLocatorByName(buttonName);
        getElement(locator).click();
    }

    public void tapFilterButton(int index) throws Exception {
        By locator = By.xpath(xpathFilterItemButton.apply(index));
        getDriver().longTap(getElement(locator), DriverUtils.SINGLE_TAP_DURATION);
    }

    public boolean isVoiceRecordingDialogVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idVoiceRecordingContainer);
    }

    public boolean isVoiceGraphVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idVoiceGraph);
    }

    private By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "start record":
            case "stop record":
                return idRecordButton;
            case "approve":
                return idApproveButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown tool button name '%s'", name));
        }
    }

    public boolean isVoiceFiltersDialogVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idVoiceFiltersContainer);
    }
}
