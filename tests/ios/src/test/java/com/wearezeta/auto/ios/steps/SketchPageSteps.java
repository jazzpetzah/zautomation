package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.SketchPage;

import cucumber.api.java.en.When;

public class SketchPageSteps {
    private SketchPage getSketchPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(SketchPage.class);
    }

    /**
     * randomly draws lines in sketch feature
     *
     * @throws Exception
     * @step. ^I draw a random sketch$
     */
    @When("^I draw a random sketch$")
    public void IDrawRandomSketches() throws Exception {
        getSketchPage().sketchRandomLines();
    }

    /**
     * Tap the corresponding button on sketch page
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Send|Draw|Open Gallery|Emoji|Undo) button on Sketch page$
     */
    @When("^I tap (Send|Draw|Open Gallery|Emoji|Undo) button on Sketch page$")
    public void ITapButton(String btnName) throws Exception {
        getSketchPage().tapButton(btnName);
    }
}
