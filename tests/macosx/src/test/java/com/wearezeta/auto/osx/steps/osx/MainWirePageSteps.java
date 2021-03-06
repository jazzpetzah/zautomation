package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.common.WebAppTestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.openqa.selenium.Dimension;
import static org.hamcrest.MatcherAssert.assertThat;

public class MainWirePageSteps {

    private final static int OSX_TITLEBAR_HEIGHT = 24;
    private final static int DEVIATION_ALLOWANCE_IN_PX = 16;

    private final WebAppTestContext webContext;
    
    public MainWirePageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I close the app$")
    public void ICloseApp() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).closeWindow();
    }

    @When("^I type shortcut combination to quit the app$")
    public void ITypeShortcutCombinationtoQuit() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForQuit();
    }

    @When("^I type shortcut combination for preferences$")
    public void ITypeShortcutCombinationForPreferences() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForPreferences();
    }

    @When("^I minimize the app$")
    public void IMinimizeApp() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).minimizeWindow();
    }

    @When("^I maximize the app$")
    public void IMaximizeApp() throws Exception {
        int maxHeight;
        MainWirePage page = webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class);

        Dimension desktopSize = page.getDesktopSize();
        // get Dock position & size
        Dimension dockSize = page.getDockSize();
        // calculate full screen height
        if (dockSize.getHeight() > dockSize.getWidth()) {
            // dock on the left size
            maxHeight = desktopSize.getHeight() - OSX_TITLEBAR_HEIGHT;
        } else {
            // dock on the bottom
            maxHeight = desktopSize.getHeight() - dockSize.getHeight() - OSX_TITLEBAR_HEIGHT;
        }

        // check if full screen
        if (page.getHeight() == maxHeight && page.getWidth() == MainWirePage.APP_MAX_WIDTH) {
            page.clickMaximizeButton();
        }
        page.clickMaximizeButton();
    }

    @When("^I verify app is in fullscreen$")
    public void IVerifyAppFullscreen() throws Exception {
        int maxHeight;
        int maxWidth;

        // get Desktop size
        MainWirePage mainPage = webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class);
        Dimension desktopSize = mainPage.getDesktopSize();
        // get Dock position & size
        Dimension dockSize = mainPage.getDockSize();
        // calculate fullscreen height & width
        if (dockSize.getHeight() > dockSize.getWidth()) {
            // dock on the left side
            maxHeight = desktopSize.getHeight() - OSX_TITLEBAR_HEIGHT;
            maxWidth = desktopSize.getWidth() - dockSize.getWidth();
        } else {
            // dock on the bottom
            maxHeight = desktopSize.getHeight() - dockSize.getHeight() - OSX_TITLEBAR_HEIGHT;
            maxWidth = desktopSize.getWidth();
        }
        // check if height in allowance
        assertThat("Height", mainPage.getHeight(), greaterThan(maxHeight - DEVIATION_ALLOWANCE_IN_PX));
        assertThat("Height", mainPage.getHeight(), lessThan(maxHeight + DEVIATION_ALLOWANCE_IN_PX));
        // check if width in allowance
        assertThat("Width", mainPage.getWidth(), greaterThan(maxWidth - DEVIATION_ALLOWANCE_IN_PX));
        assertThat("Width", mainPage.getWidth(), lessThan(maxWidth + DEVIATION_ALLOWANCE_IN_PX));
    }

    @When("^I resize the app to the max by hand$")
    public void IResizeToMaxByHand() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).resizeToMaxByHand();
    }

    @When("^I resize the app to the min by hand$")
    public void IResizeToMinByHand() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).resizeToMinByHand();
    }

    @When("^I ensure initial positioning$")
    public void IEnsureInitialPositioning() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).ensurePosition();
    }

    @When("^I change position of the app to X (\\d+) and Y (\\d+)$")
    public void IPositioningTo(int x, int y) throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).positionByHand(x, y);
    }

    @When("^I verify app X coordinate is (\\d+) and Y coordinate is (\\d+)$")
    public void IVerifyPosition(int x, int y) throws Exception {
        MainWirePage mainWirePage = webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class);
        assertThat("Expected X coordinate does not match the actual value", mainWirePage.getStrippedX(), equalTo(x));
        assertThat("Expected Y coordinate does not match the actual value", mainWirePage.getStrippedY(), equalTo(y));
    }

    @When("^I resize the app to width (\\d+) px and height (\\d+) px$")
    public void IResizeTo(int width, int height) throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).resizeByHand(width, height);
    }

    @When("^I verify app width is (\\d+) px and height is (\\d+) px$")
    public void IVerifySizeOf(int width, int height) throws Exception {
        MainWirePage mainPage = webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class);
        
        // check if height in allowance
        assertThat("Height", mainPage.getHeight(), greaterThan(height - DEVIATION_ALLOWANCE_IN_PX));
        assertThat("Height", mainPage.getHeight(), lessThan(height + DEVIATION_ALLOWANCE_IN_PX));
        // check if width in allowance
        assertThat("Width", mainPage.getWidth(), greaterThan(width - DEVIATION_ALLOWANCE_IN_PX));
        assertThat("Width", mainPage.getWidth(), lessThan(width + DEVIATION_ALLOWANCE_IN_PX));
    }

    @Then("^I type shortcut combination to undo$")
    public void ITypeShortcutCombinationToUndo() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForUndo();
    }

    @Then("^I type shortcut combination to redo$")
    public void ITypeShortcutCombinationToRedo() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForRedo();
    }

    @Then("^I type shortcut combination to select all$")
    public void ITypeShortcutCombinationToSelectAll() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForSelectAll();
    }

    @Then("^I type shortcut combination to cut$")
    public void ITypeShortcutCombinationToCut() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForCut();
    }

    @Then("^I type shortcut combination to paste$")
    public void ITypeShortcutCombinationToPaste() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForPaste();
    }

    @Then("^I type shortcut combination to copy$")
    public void ITypeShortcutCombinationToCopy() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForCopy();
    }

    @Then("^I type shortcut combination to open preferences$")
    public void ITypeShortcutCombinationToOpenPreference() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForPreferences();
    }

    @When("^I type shortcut combination to mute or unmute a conversation$")
    public void ITypeShortcutCombinationToMuteOrUnmute() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutToMute();
    }

    @When("^I type shortcut combination to archive a conversation$")
    public void ITypeShortcutCombinationToArchive() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutToArchive();
    }

    @When("^I type shortcut combination for next conversation$")
    public void ITypeShortcutCombinationForNextConv() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForNextConv();
    }

    @When("^I type shortcut combination for previous conversation$")
    public void ITypeShortcutCombinationForPrevConv() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForPrevConv();
    }

    @Then("^I type shortcut combination to ping$")
    public void ITypeShortcutCombinationToPing() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForPing();
    }

    @Then("^I type shortcut combination to start a call$")
    public void ITypeShortcutCombinationToCall() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForCall();
    }

    @Then("^I type shortcut combination to open search$")
    public void ITypeShortcutCombinationToOpenSearch() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).pressShortCutForSearch();
    }
}
