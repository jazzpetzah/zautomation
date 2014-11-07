package com.wearezeta.auto.common;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;
import gherkin.formatter.model.Tag;

public class ZetaFormatter implements Formatter, Reporter {
	
	private static RemoteWebDriver driver = null;
	
	private String feature = "";
	private String scenario = "";
	private String scope = "Unknown";
	private Queue<String> step = new LinkedList<String>();

	private long startDate;
	private long endDate;
	private int lineNumber = 0;
	
	private static final String LOGIN = "smoketester+bot@wearezeta.com";
	private static final String PASSWORD = "aqa123456";
	private static final String CONTACT_ANDROID = "Android Smoke Feedback";
	private static final String CONTACT_IOS = "iOS Smoke Feedback";
	private static final String CONTACT_OSX = "SmokeAutomation";
	
	private static String buildNumber = "unknown";
	
	@Override
	public void background(Background arg0) {
		
	}

	@Override
	public void close() {
	
	}

	@Override
	public void done() {
	
	}

	@Override
	public void eof() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void examples(Examples arg0) {
		
	}

	@Override
	public void feature(Feature arg0) {
		feature = arg0.getName(); 
		System.out.println("Feature: " + feature);
	}

	@Override
	public void scenario(Scenario arg0) {
		scenario = arg0.getName(); 
		lineNumber = arg0.getLine();
		for (Tag t : arg0.getTags()) {
			if(t.getName().equals("@torun")) {
				scope = "Dev Test";
				break;
			}
			
			if(t.getName().equals("@smoke")) {
				scope = "Smoke Test";
			}
			
			if(t.getName().equals("@regression")) {
				scope = "Regression Test";
			}
			
			if(t.getName().equals("@staging")) {
				scope = "Staging Test";
			}
		}

		System.out.println("\n\nScenario: " + scenario);
	}

	@Override
	public void scenarioOutline(ScenarioOutline arg0) {

	}

	@Override
	public void step(Step arg0) {
		step.add(arg0.getName());
	}

	@Override
	public void syntaxError(String arg0, String arg1, List<String> arg2,
			String arg3, Integer arg4) {
	
	}

	@Override
	public void uri(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void after(Match arg0, Result arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void before(Match arg0, Result arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void embedding(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void match(Match arg0) {
		
		startDate = new Date().getTime();
	}

	@Override
	public void result(Result arg0) {
		endDate = new Date().getTime();
		String currentStep = step.poll();
		System.out.println(currentStep + " (status: " + arg0.getStatus() + ", time: " + (endDate-startDate) + "ms)");
		//send chat notification
		if (arg0.getStatus().equals("failed") && scope.equals("Smoke Test")) {
			try {
				String errorMsg = arg0.getError().getMessage();
				if (errorMsg.length() > 255) {
					errorMsg = errorMsg.substring(0, 255);
				}
				
				sendNotification("\n============Automatic notification============\n" +
						driver.getCapabilities().getCapability("platformName") + " " + scope + 
						"(build " + buildNumber + ") \n" + "Feature: " + feature + 
						", Scenario: " + scenario + "(line number: " + Integer.toString(lineNumber) + ")" + "\nStep: " + 
						currentStep + ", failed with error: \n" + errorMsg + "...");
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		//take screenshot
		if (driver != null) {
			try {
				BufferedImage image = DriverUtils.takeScreenshot((ZetaDriver) driver);
				String picturePath = CommonUtils.getPictureResultsPathFromConfig(this.getClass());
				File outputfile = new File(picturePath + feature + "/" +
						scenario + "/" + currentStep + ".png");
				
				if (!outputfile.getParentFile().exists()) {
					outputfile.getParentFile().mkdirs();
				}
			    ImageIO.write(image, "png", outputfile);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			catch (org.openqa.selenium.remote.SessionNotFoundException ex) {
				((ZetaDriver) driver).setSessionLost(true);
			}
			
			catch (WebDriverException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void write(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void sendNotification(String message) throws Exception {
		
		ClientUser yourСontact = new ClientUser(LOGIN, PASSWORD, "AutomationBot", UsersState.AllContactsConnected);
		switch (driver.getCapabilities().getCapability("platformName").toString()) {
			case "Android":
				BackEndREST.sendDialogMessageByChatName(yourСontact, CONTACT_ANDROID, message);
				break;
			case "Mac":
				BackEndREST.sendDialogMessageByChatName(yourСontact, CONTACT_OSX, message);
				break;
			case "iOS":
				BackEndREST.sendDialogMessageByChatName(yourСontact, CONTACT_IOS, message);
				break;
		}
		
	}

	public static RemoteWebDriver getDriver() {
		return driver;
	}

	public static void setDriver(RemoteWebDriver driver) {
		ZetaFormatter.driver = driver;
	}

	@Override
	public void startOfScenarioLifeCycle(Scenario scenario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endOfScenarioLifeCycle(Scenario scenario) {
		// TODO Auto-generated method stub
		
	}

	public static String getBuildNumber() {
		return buildNumber;
	}

	public static void setBuildNumber(String buildNumber) {
		ZetaFormatter.buildNumber = buildNumber;
	}

}
