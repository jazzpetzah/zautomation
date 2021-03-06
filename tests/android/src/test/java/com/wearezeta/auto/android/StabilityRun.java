package com.wearezeta.auto.android;


import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format = {"html:target/report", "json:target/report.json", "com.wearezeta.auto.common.ZetaFormatter"}, tags
        = {"@stability"})
public class StabilityRun {

}
