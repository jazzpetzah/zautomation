package com.wearezeta.auto.android.steps;

import java.awt.image.BufferedImage;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.SketchPage;
import com.wearezeta.auto.common.ImageUtil;

import cucumber.api.java.en.When;

public class SketchPageSteps {

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
		.getInstance();

	private SketchPage getSketchPage() throws Exception {
		return (SketchPage) pagesCollection.getPage(SketchPage.class);
	}

	private DialogPage getDialogPage() throws Exception {
		return (DialogPage) pagesCollection.getPage(DialogPage.class);
	}

	/**
	 * Draws a sketch consisting of at least numColors colors in random patterns
	 * around the canvas
	 * 
	 * @step. ^I draw a sketch with (.*) colors$
	 * 
	 * @throws Exception
	 */
	@When("^I draw a sketch with (.*) colors$")
	public void WhenIDrawASketchWithXColors(int numColors) throws Exception {
		SketchPage page = getSketchPage();

		for (int i = 0; i < numColors; i++) {
			page.setColor(i);
			int numLines = 3;
			page.drawRandomLines(numLines);
		}
	}

	private BufferedImage sketch = null;

	/**
	 * Takes a screenshot of the sketch for later comparison
	 * 
	 * @step. ^I remember what my sketch looks like$
	 * 
	 * @throws Exception
	 */
	@When("^I remember what my sketch looks like$")
	public void WhenIRememberWhatMySketchLooksLike() throws Exception {
		sketch = getSketchPage().screenshotCanvas().orElseThrow(
			AssertionError::new);
	}

	/**
	 * Presses the send button from the sketch page
	 * 
	 * @step. ^I send my sketch$
	 * 
	 * @throws Exception
	 */
	@When("^I send my sketch$")
	public void WhenISendMySketch() throws Exception {
		getSketchPage().sendSketch();
	}

	/**
	 * Compares the photo of the drawn sketch to what appears in the
	 * conversation list
	 * 
	 * @step. ^I verify that my sketch is the same as what I drew$
	 * 
	 * @throws Exception
	 */
	@When("^I verify that my sketch is the same as what I drew$")
	public void WhenIVerifyThatMySketchMatchesWhatIDrew() throws Exception {
		final double MAX_OVERLAP_SCORE = 0.95;

		BufferedImage lastImageInConversation = getDialogPage()
			.getLastImageInConversation().orElseThrow(AssertionError::new);

		double score = ImageUtil.getOverlapScore(sketch,
			lastImageInConversation, ImageUtil.RESIZE_NORESIZE);

		if (score < MAX_OVERLAP_SCORE) {
			Assert.fail();
		}
	}

}
