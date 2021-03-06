package com.wearezeta.auto.android.common;

import com.wearezeta.auto.android.pages.AndroidPagesCollection;
import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.performance.PerformanceCommon;
import com.wearezeta.auto.common.test_context.TestContext;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.wire_actors.RemoteDevicesManager;
import cucumber.api.Scenario;

public class AndroidTestContext extends TestContext {
    private final Scenario scenario;
    private final PerformanceCommon performanceCommon;

    public AndroidTestContext(Scenario scenario, AbstractPagesCollection<? extends BasePage> pagesCollection)
            throws Exception {
        super(pagesCollection);
        this.scenario = scenario;
        this.performanceCommon = new PerformanceCommon(this.getUsersManager(), this.getDevicesManager());
    }

    public AndroidTestContext(ClientUsersManager clientUsersManager, RemoteDevicesManager remoteDevicesManager,
                              CommonCallingSteps2 callingManager, CommonSteps commonSteps,
                              Scenario scenario, AbstractPagesCollection<? extends BasePage> pagesCollection)
            throws Exception {
        super(clientUsersManager, remoteDevicesManager, callingManager, commonSteps, pagesCollection);
        this.scenario = scenario;
        this.performanceCommon = new PerformanceCommon(this.getUsersManager(), this.getDevicesManager());
    }

    public Scenario getScenario() {
        return scenario;
    }

    public AndroidPagesCollection getPagesCollection() {
        return (AndroidPagesCollection) super.getPagesCollection();
    }

    public PerformanceCommon getPerformanceCommon() {
        return this.performanceCommon;
    }
}
