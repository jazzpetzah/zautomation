package com.wearezeta.auto.android_tablet.common;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.test_context.TestContext;
import cucumber.api.Scenario;

public class AndroidTabletTestContext extends TestContext {
    private final Scenario scenario;
    private final ScreenOrientationHelper screenOrientationHelper;

    public AndroidTabletTestContext(Scenario scenario, AbstractPagesCollection<? extends BasePage> pagesCollection)
            throws Exception {
        super(pagesCollection);
        this.scenario = scenario;
        this.screenOrientationHelper = new ScreenOrientationHelper();
    }

    public Scenario getScenario() {
        return scenario;
    }

    public AndroidTabletPagesCollection getPagesCollection() {
        return (AndroidTabletPagesCollection) super.getPagesCollection();
    }

    public ScreenOrientationHelper getScreenOrientationHelper() {
        return screenOrientationHelper;
    }
}
