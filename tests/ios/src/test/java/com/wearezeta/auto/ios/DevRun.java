package com.wearezeta.auto.ios;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "html:target/report", "json:target/report.json",
		"com.wearezeta.auto.common.ZetaFormatter", "rerun:target/rerun.txt" }, tags = { "@torun" })
public class DevRun {

}
