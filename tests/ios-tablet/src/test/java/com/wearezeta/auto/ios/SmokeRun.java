package com.wearezeta.auto.ios;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources", format = {
		"html:target/report", "json:target/smoke_report.json",
		"com.wearezeta.auto.common.ZetaFormatter" }, tags = { "~@mute", "~@bug", "@smoke" })
public class SmokeRun {

}
