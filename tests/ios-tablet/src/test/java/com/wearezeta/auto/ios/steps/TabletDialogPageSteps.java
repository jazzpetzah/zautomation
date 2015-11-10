package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.TabletDialogPage;

import cucumber.api.java.en.When;

public class TabletDialogPageSteps {
	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private TabletDialogPage getTabletDialogPage() throws Exception {
		return (TabletDialogPage) pagesCollecton
				.getPage(TabletDialogPage.class);
	}
	
	/**
	 * Presses the add picture button on iPad to open a CameraRollPopoverPage
	 * 
	 * @step. ^I press Add Picture button on iPad$
	 * @throws Throwable
	 */
	@When("^I press Add Picture button on iPad$")
	public void IPressAddPictureButton() throws Throwable {
		getTabletDialogPage().pressAddPictureiPadButton();
	}

	/**
	 * Presses the conversation detail button on iPad to open a
	 * ConversationDetailPopoverPage
	 * 
	 * @step. ^I open conversation details on iPad$
	 * @throws Throwable
	 */
	@When("^I open conversation details on iPad$")
	public void IOpenConversationDetailsOniPad() throws Throwable {
		getTabletDialogPage().pressConversationDetailiPadButton();
	}
	
	/**
	 * Presses the conversation detail button on iPad to open a GroupConversationDetailPopoverPage
	 * @step. ^I open group conversation details on iPad$
	 * @throws Throwable
	 */
	@When("^I open group conversation details on iPad$")
	public void IOpenGroupConversationDetailsOniPad() throws Throwable {
		getTabletDialogPage().pressGroupConversationDetailiPadButton();
	}
	
	/**
	 * Compare image on the screen and on the disk
	 * @step. "I verify image in iPad dialog is same as template (.*)"
	 * @param filename
	 * @throws Throwable
	 */
	@When("I verify image in iPad dialog is same as template (.*)")
	public void IVerifyImageInIpadDialogSameAsTemplate(String filename)
			throws Throwable {
		getTabletDialogPage().isImageShown(filename);
	}
}
