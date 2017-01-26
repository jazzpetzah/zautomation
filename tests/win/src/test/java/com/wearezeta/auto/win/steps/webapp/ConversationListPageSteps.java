package com.wearezeta.auto.win.steps.webapp;


import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.win.pages.win.MainWirePage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.win.pages.webapp.ContactListPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebElement;

public class ConversationListPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(ConversationListPageSteps.class.getName());
    private final WebAppTestContext webContext;

    public ConversationListPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }
    
    @Given("^I click context menu of the last message$")
    public void IClickContextMenuOfLast() throws Exception {
        webContext.getPagesCollection().getPage(com.wearezeta.auto.web.pages.ConversationPage.class).clickContextMenuOnMessage(1);
    }

    //TODO move to webapp
    @Then("^I verify active conversation is at index (\\d+)$")
    public void IVerifyActiveConversationIsAtIndex(int expectedIndex) throws Exception {
        final int actualIndex = webContext.getPagesCollection().getPage(ContactListPage.class).getActiveConversationIndex();
        Assert.assertTrue(String.format(
                "The index of active item in Conversations list does not equal to %s (current value is %s)", expectedIndex,
                actualIndex), actualIndex == expectedIndex);
    }
    
    @When("^I type shortcut combination to archive a conversation$")
    public void ITypeShortcutCombinationToArchive() throws Exception {
        webContext.getPagesCollection().getPage(ContactListPage.class).pressShortCutToArchive();
    }
    
    @When("^I type shortcut combination to mute or unmute a conversation$")
    public void ITypeShortcutCombinationToMuteOrUnmute() throws Exception {
        webContext.getPagesCollection().getPage(ContactListPage.class).pressShortCutToMute();
    }
    
    @Then("^I type shortcut combination to open search$")
    public void ITypeShortcutCombinationToOpenSearch() throws Exception {
        webContext.getPagesCollection().getPage(ContactListPage.class).pressShortCutToSearch();
    }

    @When("^I type shortcut combination for next conversation$")
    public void ITypeShortcutCombinationForNextConv() throws Exception {
        webContext.getPagesCollection().getPage(ContactListPage.class).pressShortCutForNextConv();
    }

    @When("^I type shortcut combination for previous conversation$")
    public void ITypeShortcutCombinationForPrevConv() throws Exception {
        webContext.getPagesCollection().getPage(ContactListPage.class).pressShortCutForPrevConv();
    }
    
    @Given("^I open context menu of conversation (.*)$")
    public void IOpenContextMenuOfContact(String name) throws Exception {
        name = webContext.getUsersManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = webContext.getPagesCollection().getPage(ContactListPage.class);
        Assert.assertTrue("No contact list loaded.", contactListPage.waitForContactListVisible());
        WebElement element = contactListPage.getConversationListElement(name);

        MainWirePage mainWirePage = webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(
                MainWirePage.class);
        // get x and y positions to right click
        int x = mainWirePage.getX() + element.getLocation().getX();
        int y = mainWirePage.getY() + element.getLocation().getY();
        mainWirePage.rightClickOn(x, y);
    }

}
