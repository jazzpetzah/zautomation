package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import java.util.function.Function;
import org.openqa.selenium.By;

public class CallOngoingPage extends CallingOverlayPage {

    private static final By xpathOngoingCallContainer =
            By.xpath("//*[@id='ttv__calling__header__subtitle' and contains(@value, ':') and //*[@id='ccbv__button_middle']]");

    private static final By idParticipants = By.id("chv__calling__participants_grid__chathead");

    private static final Function<String, String> xpathCallingHeaderByName = name -> String
            .format("//*[@id='ttv__calling__header__name' and contains(@value, '%s')]", name);

    public CallOngoingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOngoingCallContainer,
                VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOngoingCallContainer, 
                VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNameAppearsOnCallingBarCaption(String name) throws Exception {
        final By locator = By.xpath(xpathCallingHeaderByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public int getNumberOfParticipants() throws Exception {
        return selectVisibleElements(idParticipants).size();
    }
}
