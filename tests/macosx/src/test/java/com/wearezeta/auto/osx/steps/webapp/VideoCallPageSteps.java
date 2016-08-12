package com.wearezeta.auto.osx.steps.webapp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import com.wearezeta.auto.common.CommonCallingSteps2;
import static com.wearezeta.auto.common.CommonSteps.splitAliases;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.pages.VideoCallPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

public class VideoCallPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(VideoCallPageSteps.class.getName());

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();
    private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2.getInstance();
    private final OSXPagesCollection osxPagesCollection = OSXPagesCollection.getInstance();

    /**
     * Maximizes video call
     *
     * @throws Exception
     * @step. ^I maximize video call$
     */
    @When("^I maximize video call via titlebar$")
    public void IMaximizeVideoCall() throws Exception {
        webappPagesCollection.getPage(VideoCallPage.class).clickMaximizeVideoCallButton();
    }
    
    /**
     * Checks if the video call minimized/maximized
     *
     * @param videoCallSize is either minimized|maximized
     * @throws Exception
     * @step. ^I see video call is (minimized|maximized)$
     */
    @Then("^I see video call is (minimized|maximized)$")
    public void ISeeVideoCallMinimized(String videoCallSize) throws Exception {
        VideoCallPage videoCallPage = webappPagesCollection.getPage(VideoCallPage.class);
        if (videoCallSize.equals("minimized")) {
            //Assert.assertTrue("Video is in portrait mode", videoCallPage.isVideoNotInPortrait());
            Assert.assertTrue("Minimize Video Call button is visible", videoCallPage.isMinimizeVideoCallButtonNotVisible());
        } else {
            //Assert.assertTrue("Video is not in portrait mode", videoCallPage.isVideoInPortrait());
            Assert.assertTrue("Minimize Video Call button is not visible", videoCallPage.isMinimizeVideoCallButtonVisible());
        }
    }

    @Then("^I click on screen share button$")
    public void IClickScreenShareButton() throws Exception {
        VideoCallPage videoCallPage = webappPagesCollection.getPage(VideoCallPage.class);
        videoCallPage.clickScreenShareButton();
    }

    @Then("^I verify my self video shows my screen$")
    public void IVerifyMySelfVideoShowsScreen() throws Exception {
        VideoCallPage videoCallPage = webappPagesCollection.getPage(VideoCallPage.class);

        // get position and size of self video element
        final Point elementLocation = videoCallPage.getLocalScreenShareVideoElementLocation();
        final Dimension elementSize = videoCallPage.getLocalScreenShareVideoElementSize();

        // get local screenshot
        MainWirePage mainWirePage = osxPagesCollection.getPage(MainWirePage.class);
        Optional<BufferedImage> localScreenshot = mainWirePage.getScreenshot();
        Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());

        // cutout from screenshot
        ZetaOSXDriver osxDriver = (ZetaOSXDriver) PlatformDrivers.getInstance().getDrivers().get(
                OSXExecutionContext.CURRENT_PLATFORM).get();
        int retinaMultiplicator = OSXCommonUtils.screenPixelsMultiplier(osxDriver);
        int x = mainWirePage.getX() * retinaMultiplicator;
        int y = mainWirePage.getY() * retinaMultiplicator;
        // NOTE: for some reason X and Y are interchanged so we switch it here
        int elementY = elementLocation.getX() * retinaMultiplicator;
        int elementX = elementLocation.getY() * retinaMultiplicator;
        int elementWidth = elementSize.getWidth() * retinaMultiplicator;
        int elementHeight = elementSize.getHeight() * retinaMultiplicator;

        LOG.debug("App location is X=" + x + " and Y=" + y);
        LOG.debug("Relative element location inside of app is X=" + elementX + " and Y=" + elementY);
        LOG.debug("Absolute element location is X=" + (x + elementX) + " and Y=" + (y + elementY));
        LOG.debug("Element size is Width=" + elementWidth + " and Height=" + elementHeight);

        BufferedImage localScreenShareVideo = localScreenshot.get().getSubimage(x + elementX, y + elementY,
                elementWidth, elementHeight);

        // resize local screenshot to cutout
        BufferedImage resizedScreenshot = ImageUtil.scaleTo(localScreenshot.get(), elementWidth, elementHeight);

        // Write images to disk
        String resizedScreenshotName = "target/resizedScreenshot" + System.currentTimeMillis() + ".png";
        String localScreenShareVideoName = "target/localScreenshare" + System.currentTimeMillis() + ".png";
        ImageUtil.storeImage(resizedScreenshot, new File(resizedScreenshotName));
        ImageUtil.storeImage(localScreenShareVideo, new File(localScreenShareVideoName));
        String reportPath = "../artifact/tests/macosx/";

        // do feature Matching + homography to find objects
        assertThat("Not enough good matches between "
                + "<a href='" + reportPath + resizedScreenshotName + "'>screenshot</a> and <a href='" + reportPath + localScreenShareVideoName + "'>self video</a>",
                ImageUtil.getMatches(resizedScreenshot, localScreenShareVideo), greaterThan(5));
    }

    @Then("^I verify (.*) sees my screen$")
    public void IVerifyUserXVideoShowsScreen(String callees) throws Exception {
        for (String callee : splitAliases(callees)) {
            final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(callee);

            // get screenshot from remote user
            BufferedImage remoteScreenshot = commonCallingSteps.getScreenshot(userAs);

            // get local screenshot and resize to remote size
            Optional<BufferedImage> localScreenshot = osxPagesCollection.getPage(MainWirePage.class).getScreenshot();
            Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());
            BufferedImage resizedScreenshot = ImageUtil.scaleTo(localScreenshot.get(), remoteScreenshot.getWidth(),
                    remoteScreenshot.getHeight());

            // Write images to disk
            long currentTimeMillis = System.currentTimeMillis();
            String resizedScreenshotName = "target/resizedScreenshot" + currentTimeMillis + ".png";
            String remoteScreenshotName = "target/remoteScreenshot" + currentTimeMillis + ".png";
            ImageUtil.storeImage(resizedScreenshot, new File(resizedScreenshotName));
            ImageUtil.storeImage(remoteScreenshot, new File(remoteScreenshotName));
            String reportPath = "../artifact/tests/macosx/";

            // do feature Matching + homography to find objects
            assertThat("Not enough good matches between "
                    + "<a href='" + reportPath + resizedScreenshotName + "'>screenshot</a> and <a href='" + reportPath + remoteScreenshotName + "'>remote</a>",
                    ImageUtil.getMatches(resizedScreenshot, remoteScreenshot), greaterThan(38));
        }
    }

    /**
     * Click minimize button on video call page
     *
     * @throws Exception
     * @step. ^I minimize video call$
     */
    @When("^I minimize video call$")
    public void IMinimizeVideoCall() throws Exception {
        webappPagesCollection.getPage(VideoCallPage.class).clickMinimizeVideoCallButton();
    }

    /**
     * Turn off and on the camera on video call page
     *
     * @throws Exception
     * @step. ^I see video call is (minimized|maximized)$
     */
    @When("^I click on video button$")
    public void IClickVideoButton() throws Exception {
        VideoCallPage videoCallPage = webappPagesCollection.getPage(VideoCallPage.class);
        videoCallPage.clickVideoButton();
    }

    /**
     * Checks if the self video is black
     *
     * @throws Exception
     * @step. ^I see my self video is black$
     */
    @Then("^I see my self video is( not)? black$")
    public void ISeeSelfVideoBlack(String not) throws Exception {
        VideoCallPage videoCallPage = webappPagesCollection.getPage(VideoCallPage.class);
        Optional<BufferedImage> selfVideo = videoCallPage.getSelfVideo();
        Assert.assertTrue("Self video is not present", selfVideo.isPresent());
        BufferedImage image = selfVideo.get();
        Color pixel = new Color(image.getRGB(image.getWidth() / 2, image.getHeight() / 2));
        if (not == null) {
            Assert.assertThat("RGB red", pixel.getRed(), lessThan(2));
            Assert.assertThat("RGB green", pixel.getGreen(), lessThan(2));
            Assert.assertThat("RGB blue", pixel.getBlue(), lessThan(2));
        } else {
            Assert.
                    assertThat("All RGB values summarized", pixel.getRed() + pixel.getGreen() + pixel.getGreen(),
                            greaterThan(20));
        }
    }

    /**
     * Checks whether the self video is on or off
     *
     * @throws Exception
     * @step. ^I see my self video is (off|on)$
     */
    @Then("^I see my self video is (off|on)$")
    public void ISeeSelfVideoOff(String onOffToggle) throws Exception {
        VideoCallPage videoCallPage = webappPagesCollection.getPage(VideoCallPage.class);
        if ("off".equals(onOffToggle)) {
            assertTrue("Disabled video icon is still shown", videoCallPage.isDisabledVideoIconVisible());
        } else {
            assertTrue("Disabled video icon is not shown", videoCallPage.isDisabledVideoIconInvisible());
        }
    }
}
