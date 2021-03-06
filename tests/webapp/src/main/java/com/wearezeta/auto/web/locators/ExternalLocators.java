package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class ExternalLocators {

	public static final class DeleteAccountPage {
		public final static String cssSubmitButton = "[type='submit']";
		public final static String idWrongKey = "400";
		public final static String idWrongCode = "403";
		public final static Function<String, String> xpathLabelByText = txt -> String
				.format("//*[contains(text(),'%s')]", txt);
	}

	public static final class PasswordChangePage {
		public final static String cssPasswordInput = "[name='password']";
		public final static String cssSubmitButton = "[type='submit']";
	}

	public static final class PasswordChangeRequestPage {
		public final static String cssEmailInput = "[name='email']";
		public final static String cssSubmitButton = "button";
	}

	public static final class PasswordChangeRequestSuccessfullPage {
		public final static Function<String, String> xpathLabelByText = txt -> String
				.format("//*[contains(text(),'%s')]", txt);
	}

	public static final class PasswordChangeSuccessfullPage {
		public final static Function<String, String> xpathLabelByText = txt -> String
				.format("//*[contains(text(),'%s')]", txt);
	}
	
	public static final class DownloadPage {
		public final static String cssDownloadIOS = "[data-ga-category='download'][data-ga-action='download'][data-ga-value='ios']";
		public final static String cssDownloadAndroid = "[data-ga-category='download'][data-ga-action='download'][data-ga-value='android']";
		public final static String cssDownloadOSX = "[data-ga-category='download'][data-ga-action='download'][data-ga-value='osx']";
		public final static String cssDownloadWindows = "[data-ga-category='download'][data-ga-action='download'][data-ga-value='windows']";
		public final static String cssDownloadWebapp = "[data-ga-category='download'][data-ga-action='download'][data-ga-value='webapp']";
	}
	
	public static final class VerifyPage {
		public final static String cssDownloadIPhone = "[data-ga-category='verify'][data-ga-action='download'][data-ga-value='ios']";
		public final static String cssDownloadAndroid = "[data-ga-category='verify'][data-ga-action='download'][data-ga-value='android']";
		public final static String cssDownloadOSX = "a[href*='mt=12']";
		public final static String cssDownloadWindows = "a[href*='download']";
		public final static String cssWebappButton = "[href='https://wire-webapp-staging.zinfra.io']";
		public final static String cssOpenWireButton = ".wbtn";
		
		public final static Function<String, String> xpathLabelByText = txt -> String
				.format("//*[contains(text(),'%s')]", txt);
	}

	public static final class StartPage {
		public final static Function<String, String> xpathLabelByText = txt -> String
				.format("//*[contains(text(),'%s')]", txt);

		public static final String cssGermanValue = "option[value='/l/de/']";
		public static final String cssEnglishValue = "option[value='/l/en/']";

		public static final String cssEnglishButton = "[class='locale en']";
		public static final String cssGermanButton = "[class='locale de']";

		public static final String cssGermanSite = "[lang='de']";
		public static final String cssEnglishSite = "[lang='en']";


	}
}
