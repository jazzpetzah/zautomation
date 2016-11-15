package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.win.pages.win.MainWirePage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.When;

import org.junit.Assert;

public class MainWirePageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public MainWirePageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I close the app$")
    public void ICloseApp() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).closeWindow();
    }

    @When("^I type shortcut combination to quit the app$")
    public void ITypeShortcutCombinationtoQuit() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).pressShortCutForQuit();
    }

    @When("^I type shortcut combination for preferences$")
    public void ITypeShortcutCombinationForPreferences() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).pressShortCutForPreferences();
    }

    @When("^I minimize the app$")
    public void IMinimizeApp() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).minimizeWindow();
    }

    @When("^I maximize the app$")
    public void IMaximizeApp() throws Exception {
        throw new RuntimeException("Not implemented yet");
    }

    @When("^I verify app is in fullscreen$")
    public void IVerifyAppFullscreen() throws Exception {
        throw new RuntimeException("Not implemented yet");
    }

    @When("^I verify app is in minimum size$")
    public void IVerifyAppMini() throws Exception {
        Assert.assertTrue(wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).isMini());
    }

    @When("^I resize the app to the max by hand$")
    public void IResizeToMaxByHand() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).resizeToMaxByHand();
    }

    @When("^I resize the app to the min by hand$")
    public void IResizeToMinByHand() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).resizeToMinByHand();
    }

    @When("^I ensure initial positioning$")
    public void IEnsureInitialPositioning() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).ensurePosition();
    }

    @When("^I change position of the app to X (\\d+) and Y (\\d+)$")
    public void IPositioningTo(int x, int y) throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).positionByHand(x, y);
    }

    @When("^I verify app X coordinate is (\\d+) and Y coordinate is (\\d+)$")
    public void IVerifyPosition(int x, int y) throws Exception {
        MainWirePage mainWirePage = wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class);
        Assert.assertTrue("Expected X coordinate " + x + " does not match the actual value " + mainWirePage.getX(),
                mainWirePage.isX(x));
        Assert.assertTrue("Expected Y coordinate " + y + " does not match the actual value " + mainWirePage.getY(),
                mainWirePage.isY(y));
    }

    @When("^I resize the app to width (\\d+) px and height (\\d+) px$")
    public void IResizeTo(int width, int height) throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).resizeByHand(width, height);
    }

    @When("^I verify app width is (\\d+) px and height is (\\d+) px$")
    public void IVerifySizeOf(int width, int height) throws Exception {
        MainWirePage mainWirePage = wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class);
        Assert.assertTrue(mainWirePage.isApproximatelyHeight(height));
        Assert.assertTrue(mainWirePage.isApproximatelyWidth(width));
    }

}
