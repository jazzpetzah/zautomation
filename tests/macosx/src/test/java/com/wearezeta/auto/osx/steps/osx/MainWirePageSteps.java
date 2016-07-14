package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.openqa.selenium.Dimension;

public class MainWirePageSteps {

    private final static int OSX_TITLEBAR_HEIGHT = 24;
    private final static int DEVIATION_ALLOWANCE_IN_PX = 15;

    private final OSXPagesCollection osxPagesCollection = OSXPagesCollection
            .getInstance();

    /**
     * Closes the app
     *
     * @step. ^I close the app$
     *
     * @throws Exception
     */
    @When("^I close the app$")
    public void ICloseApp() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class).closeWindow();
    }

    /**
     * Types shortcut combination to quit the app
     *
     * @step. ^I type shortcut combination to quit the app$
     * @throws Exception
     */
    @When("^I type shortcut combination to quit the app$")
    public void ITypeShortcutCombinationtoQuit() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class).pressShortCutForQuit();
    }

    /**
     * Types shortcut combination for preferences
     *
     * @step. ^I type shortcut combination for preferences$
     * @throws Exception
     */
    @When("^I type shortcut combination for preferences$")
    public void ITypeShortcutCombinationForPreferences() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForPreferences();
    }

    /**
     * Minimizes the app
     *
     * @step. ^I minimize the app$
     *
     * @throws Exception
     */
    @When("^I minimize the app$")
    public void IMinimizeApp() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class).minimizeWindow();
    }

    /**
     * Maximize the app
     *
     * @step. ^I maximize the app$
     *
     * @throws Exception
     */
    @When("^I maximize the app$")
    public void IMaximizeApp() throws Exception {
        int maxHeight;
        MainWirePage page = osxPagesCollection.getPage(MainWirePage.class);

        Dimension desktopSize = page.getDesktopSize();
        // get Dock position & size
        Dimension dockSize = page.getDockSize();
        // calculate full screen height
        if (dockSize.getHeight() > dockSize.getWidth()) {
            // dock on the left size
            maxHeight = desktopSize.getHeight() - OSX_TITLEBAR_HEIGHT;
        } else {
            // dock on the bottom
            maxHeight = desktopSize.getHeight() - dockSize.getHeight()
                    - OSX_TITLEBAR_HEIGHT;
        }

        // check if full screen
        if (page.getHeight() == maxHeight
                && page.getWidth() == MainWirePage.APP_MAX_WIDTH) {
            page.clickMaximizeButton();
        }
        page.clickMaximizeButton();
    }

    /**
     * Verifies wether the app is in fullscreen or not
     *
     * @step. ^I verify app is in fullscreen$
     *
     * @throws Exception
     */
    @When("^I verify app is in fullscreen$")
    public void IVerifyAppFullscreen() throws Exception {
        int maxHeight;
        int maxWidth;

        // get Desktop size
        MainWirePage mainPage = osxPagesCollection.getPage(MainWirePage.class);
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
            maxHeight = desktopSize.getHeight() - dockSize.getHeight()
                    - OSX_TITLEBAR_HEIGHT;
            maxWidth = desktopSize.getWidth();
        }
        // check if height in allowance
        assertThat("Height", mainPage.getHeight(), greaterThan(maxHeight
                - DEVIATION_ALLOWANCE_IN_PX));
        assertThat("Height", mainPage.getHeight(), lessThan(maxHeight
                + DEVIATION_ALLOWANCE_IN_PX));
        // check if width in allowance
        assertThat("Width", mainPage.getWidth(), greaterThan(maxWidth
                - DEVIATION_ALLOWANCE_IN_PX));
        assertThat("Width", mainPage.getWidth(), lessThan(maxWidth
                + DEVIATION_ALLOWANCE_IN_PX));
    }

    /**
     * Resizes the app to the max by hand
     *
     * @step. ^I resize the app to the max by hand$
     *
     * @throws Exception
     */
    @When("^I resize the app to the max by hand$")
    public void IResizeToMaxByHand() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class).resizeToMaxByHand();
    }

    /**
     * Resizes the app to the min by hand
     *
     * @step. ^I resize the app to the min by hand$
     *
     * @throws Exception
     */
    @When("^I resize the app to the min by hand$")
    public void IResizeToMinByHand() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class).resizeToMinByHand();
    }

    /**
     * Ensures that the position of the app is in the upper left corner with respect to the dock (assuming it's on the left
     * side).
     *
     * @step. ^I ensure initial positioning$
     *
     * @throws Exception
     */
    @When("^I ensure initial positioning$")
    public void IEnsureInitialPositioning() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class).ensurePosition();
    }

    /**
     * Repositions the app to the given coordinates
     *
     * @step. ^I change position of the app to X (\\d+) and Y (\\d+)$
     *
     * @param x the x coordinate
     * @param y the y coordinate
     *
     *
     * @throws Exception
     */
    @When("^I change position of the app to X (\\d+) and Y (\\d+)$")
    public void IPositioningTo(int x, int y) throws Exception {
        osxPagesCollection.getPage(MainWirePage.class).positionByHand(x, y);
    }

    /**
     * Verifies the position of the app
     *
     * @step. ^I verify app X coordinate is (\\d+) and Y coordinate is (\\d+)$
     *
     * @param x the width to verify
     * @param y the height to verify
     *
     *
     * @throws Exception
     */
    @When("^I verify app X coordinate is (\\d+) and Y coordinate is (\\d+)$")
    public void IVerifyPosition(int x, int y) throws Exception {
        MainWirePage mainWirePage = osxPagesCollection
                .getPage(MainWirePage.class);
        Assert.assertTrue("Expected X coordinate " + x
                + " does not match the actual value " + mainWirePage.getX(),
                mainWirePage.isX(x));
        Assert.assertTrue("Expected Y coordinate " + y
                + " does not match the actual value " + mainWirePage.getY(),
                mainWirePage.isY(y));
    }

    /**
     * Resizes the app to the given size
     *
     * @step. ^I resize the app to width (\\d+)px and height (\\d+)px$
     *
     * @param width the width to resize to
     * @param height the height to resize to
     *
     *
     * @throws Exception
     */
    @When("^I resize the app to width (\\d+) px and height (\\d+) px$")
    public void IResizeTo(int width, int height) throws Exception {
        osxPagesCollection.getPage(MainWirePage.class).resizeByHand(width,
                height);
    }

    /**
     * Verifies size of the app
     *
     * @step. ^I verify app width is (\\d+)px and height is (\\d+)px$
     *
     * @param width the width to verify
     * @param height the height to verify
     *
     *
     * @throws Exception
     */
    @When("^I verify app width is (\\d+) px and height is (\\d+) px$")
    public void IVerifySizeOf(int width, int height) throws Exception {
        MainWirePage mainPage = osxPagesCollection.getPage(MainWirePage.class);
        assertThat("Width", mainPage.getWidth(), equalTo(width));
        assertThat("Height", mainPage.getHeight(), equalTo(height));
    }

    /**
     * Types shortcut combination to undo
     *
     * @step. ^I type shortcut combination to undo$
     * @throws Exception
     */
    @Then("^I type shortcut combination to undo$")
    public void ITypeShortcutCombinationToUndo() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForUndo();
    }

    /**
     * Types shortcut combination to redo
     *
     * @step. ^I type shortcut combination to redo$
     * @throws Exception
     */
    @Then("^I type shortcut combination to redo$")
    public void ITypeShortcutCombinationToRedo() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForRedo();
    }

    /**
     * Types shortcut combination to select all
     *
     * @step. ^I type shortcut combination to select all$
     * @throws Exception
     */
    @Then("^I type shortcut combination to select all$")
    public void ITypeShortcutCombinationToSelectAll() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForSelectAll();
    }

    /**
     * Types shortcut combination to cut
     *
     * @step. ^I type shortcut combination to cut$
     * @throws Exception
     */
    @Then("^I type shortcut combination to cut$")
    public void ITypeShortcutCombinationToCut() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForCut();
    }

    /**
     * Types shortcut combination to paste
     *
     * @step. ^I type shortcut combination to paste$
     * @throws Exception
     */
    @Then("^I type shortcut combination to paste$")
    public void ITypeShortcutCombinationToPaste() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForPaste();
    }

    /**
     * Types shortcut combination to copy
     *
     * @step. ^I type shortcut combination to copy$
     * @throws Exception
     */
    @Then("^I type shortcut combination to copy$")
    public void ITypeShortcutCombinationToCopy() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForCopy();
    }

    /**
     * Types shortcut combination to open preferences
     *
     * @step. ^I type shortcut combination to open preferences$
     * @throws Exception
     */
    @Then("^I type shortcut combination to open preferences$")
    public void ITypeShortcutCombinationToOpenPreference() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForPreferences();
    }

    /**
     * Types shortcut combination to mute or unmute a conversation
     *
     * @step. ^I type shortcut combination to mute or unmute a conversation$
     * @throws Exception
     */
    @When("^I type shortcut combination to mute or unmute a conversation$")
    public void ITypeShortcutCombinationToMuteOrUnmute() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutToMute();
    }

    /**
     * Types shortcut combination to archive a conversation
     *
     * @step. ^I type shortcut combination to archive a conversation$
     * @throws Exception
     */
    @When("^I type shortcut combination to archive a conversation$")
    public void ITypeShortcutCombinationToArchive() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutToArchive();
    }

    /**
     * Types shortcut combination for the next conversation
     *
     * @step. ^I type shortcut combination for next conversation$
     * @throws Exception
     */
    @When("^I type shortcut combination for next conversation$")
    public void ITypeShortcutCombinationForNextConv() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForNextConv();
    }

    /**
     * Types shortcut combination for the previous conversation
     *
     * @step. ^I type shortcut combination for previous conversation$
     * @throws Exception
     */
    @When("^I type shortcut combination for previous conversation$")
    public void ITypeShortcutCombinationForPrevConv() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForPrevConv();
    }

    /**
     * Types shortcut combination to ping
     *
     * @step. ^I type shortcut combination to ping$
     * @throws Exception
     */
    @Then("^I type shortcut combination to ping$")
    public void ITypeShortcutCombinationToPing() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForPing();
    }

    /**
     * Types shortcut combination to call
     *
     * @step. ^I type shortcut combination to ping$
     * @throws Exception
     */
    @Then("^I type shortcut combination to start a call$")
    public void ITypeShortcutCombinationToCall() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForCall();
    }

    /**
     * Types shortcut combination to open search
     *
     * @step. ^I type shortcut combination to open search$
     * @throws Exception
     */
    @Then("^I type shortcut combination to open search$")
    public void ITypeShortcutCombinationToOpenSearch() throws Exception {
        osxPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForSearch();
    }
}
