package com.wearezeta.auto.android;


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = {"html:target/report", "json:target/staging_report.json", "com.wearezeta.auto.common.ZetaFormatter"}, tags = {"@mute"})
public class VerificationRun {

}
