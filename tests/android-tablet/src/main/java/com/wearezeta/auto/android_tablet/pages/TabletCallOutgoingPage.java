package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.common.Memory;
import com.wearezeta.auto.android.pages.CallOutgoingPage;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class TabletCallOutgoingPage extends AndroidTabletPage {


    public TabletCallOutgoingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
    
    private CallOutgoingPage getPage() throws Exception {
        return this.getAndroidPageInstance(CallOutgoingPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return getPage().waitUntilVisible();
    }

    public boolean waitUntilNotVisible() throws Exception {
        return getPage().waitUntilNotVisible();
    }
    
    public boolean waitUntilNameAppearsOnCallingBarCaption(String name) throws Exception {
        return getPage().waitUntilNameAppearsOnCallingBarCaption(name);
    }
    
    public void rememberSpecialActionButtonState() throws Exception {
        getPage().rememberSpecialActionButtonState();
    }
    
    public void rememberMuteButtonState() throws Exception {
        getPage().rememberMuteButtonState();
    }

    public boolean specialActionButtonStateHasChanged() throws Exception {
        return getPage().specialActionButtonStateHasChanged();
    }
    
    public boolean muteButtonStateHasChanged() throws Exception {
        return getPage().muteButtonStateHasChanged();
    }
    
    public boolean hangupIsVisible() throws Exception {
        return getPage().hangupIsVisible();
    }
    
    public boolean toggleMuteIsVisible() throws Exception {
        return getPage().toggleMuteIsVisible();
    }

    public boolean toggleSpeakerIsVisible() throws Exception {
        return getPage().toggleSpeakerIsVisible();
    }
    
    public boolean toggleVideoIsVisible() throws Exception {
        return getPage().toggleVideoIsVisible();
    }

    public void toggleMute() throws Exception {
        getPage().toggleMute();
    }
    
    public void hangup() throws Exception {
        getPage().hangup();
    }
    
    public void toggleSpeaker() throws Exception {
        getPage().toggleSpeaker();
    }
    
    public void toggleVideo() throws Exception {
        getPage().toggleVideo();
    }

    public int getNumberOfParticipants() throws Exception {
        return getPage().getNumberOfParticipants();
    }
    
}
