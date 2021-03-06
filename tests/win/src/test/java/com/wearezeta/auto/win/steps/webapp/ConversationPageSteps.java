package com.wearezeta.auto.win.steps.webapp;


import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.win.pages.webapp.ConversationPage;
import com.wearezeta.auto.win.pages.win.MainWirePage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import cucumber.api.java.en.Then;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.openqa.selenium.Point;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class ConversationPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(ConversationPageSteps.class.getName());

    private final WebAppTestContext webContext;

    public ConversationPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }
    
    @Then("^I (do not )?see a picture (.*) from link preview$")
    public void ISeePictureInLinkPreview(String doNot, String pictureName) throws Exception {
        if (doNot == null) {
            assertThat("I do not see a picture from link preview in the conversation",
                    webContext.getPagesCollection().getPage(ConversationPage.class).isImageFromLinkPreviewVisible());

            BufferedImage expectedImage = ImageUtil.readImageFromFile(WebCommonUtils.getFullPicturePath(pictureName));
            BufferedImage actualImage = webContext.getPagesCollection().getPage(ConversationPage.class).
                    getImageFromLastLinkPreview();
            
            // Image matching with SIFT does not work very well on really small images
            // because it defines the maximum number of matching keys
            // so we scale them to double size to get enough matching keys
            final int scaleMultiplicator = 2;
            expectedImage = ImageUtil.resizeImage(expectedImage, scaleMultiplicator);
            actualImage = ImageUtil.resizeImage(actualImage, scaleMultiplicator);

            // Convert screenshots to data uris
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(expectedImage, "png", baos);
            String expectedDataURI = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            ImageIO.write(actualImage, "png", baos2);
            String actualDataURI = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos2.toByteArray());

            assertThat("Not enough good matches between expected " + "<img width='200px' src='" + expectedDataURI
                    + "' /> and actual <img width='200px' src='" + actualDataURI + "' />",
                    ImageUtil.getMatches(expectedImage, actualImage), greaterThan(80));
        } else {
            assertThat("I see a picture in the conversation", webContext.getPagesCollection().getPage(ConversationPage.class)
                    .isImageFromLinkPreviewNotVisible());
        }
    }
    
    @Then("^I type shortcut combination to ping$")
    public void ITypeShortcutCombinationToPing() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForPing();
    }
    
    @Then("^I type shortcut combination to undo$")
    public void ITypeShortcutCombinationToUndo() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForUndo();
    }

    @Then("^I type shortcut combination to redo$")
    public void ITypeShortcutCombinationToRedo() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForRedo();
    }

    @Then("^I type shortcut combination to select all$")
    public void ITypeShortcutCombinationToSelectAll() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForSelectAll();
    }

    @Then("^I type shortcut combination to cut$")
    public void ITypeShortcutCombinationToCut() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForCut();
    }

    @Then("^I type shortcut combination to paste$")
    public void ITypeShortcutCombinationToPaste() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForPaste();
    }

    @Then("^I type shortcut combination to copy$")
    public void ITypeShortcutCombinationToCopy() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForCopy();
    }

    @When("^I click on ephemeral button$")
    public void IClickEphemeralButton() throws Exception {
        Point point = webContext.getPagesCollection(WebappPagesCollection.class).
                getPage(com.wearezeta.auto.web.pages.ConversationPage.class).
                getCenterOfEphemeralButton();

        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).
                clickOnWebView(point);
    }
}
