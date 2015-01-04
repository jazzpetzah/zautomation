package com.wearezeta.auto.osx.steps;

import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationPage;

import cucumber.api.java.en.When;

public class PerformanceSteps {
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();

	private String randomMessage;
	private static final String picturename = "testing.jpg";

	private Random random = new Random();

	@When("^I (.*) start testing cycle for (\\d+) minutes$")
	public void WhenIStartTestingCycleForMinutes(String nameAlias, int time)
			throws Throwable {
		LocalDateTime startDateTime = LocalDateTime.now();
		long diffInMinutes = 0;

		final String name = perfCommon.getUserManager()
				.findUserByNameAlias(nameAlias).getName();
		CommonOSXSteps.senderPages
				.setContactListPage(new ContactListPage(
						CommonUtils
								.getOsxAppiumUrlFromConfig(LoginPageSteps.class),
						CommonUtils
								.getOsxApplicationPathFromConfig(LoginPageSteps.class)));

		// Main cycle
		while (diffInMinutes < time) {

			// Send messages and image by BackEnd
			try {
				perfCommon.sendRandomMessagesToUser(
						PerformanceCommon.BACK_END_MESSAGE_COUNT,
						PerformanceCommon.PARALLEL_MSGS);
				perfCommon
						.sendDefaultImageToUser(PerformanceCommon.BACK_END_MESSAGE_COUNT / 5);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Thread.sleep(1000);

			// Send messages cycle by UI
			for (int j = 1; j <= PerformanceCommon.SEND_MESSAGE_NUM; ++j) {
				ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
						CommonOSXSteps.senderPages.getContactListPage()
								.getContacts());
				// remove self user from the list
				for (int i = 0; i < visibleContactsList.size(); i++) {
					if (visibleContactsList.get(i).getText().equals(name)) {
						visibleContactsList.remove(i);
						break;
					}
				}
				if (visibleContactsList.size() <= 0) {
					continue;
				}
				int randomInt = random.nextInt(visibleContactsList.size() - 1);
				String contact = visibleContactsList.get(randomInt).getText();
				CommonOSXSteps.senderPages.getContactListPage()
						.openConversation(contact, false);
				Thread.sleep(100);

				CommonOSXSteps.senderPages
						.setConversationPage(new ConversationPage(
								CommonUtils
										.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
								CommonUtils
										.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
				Thread.sleep(2000);
				int numberMessages = CommonOSXSteps.senderPages
						.getConversationPage().getNumberOfMessageEntries(
								contact);
				int numberPictures = CommonOSXSteps.senderPages
						.getConversationPage().getNumberOfImageEntries();
				if (numberMessages > 0 || numberPictures > 0) {
					try {
						CommonOSXSteps.senderPages.getConversationPage()
								.scrollDownToLastMessage();
					} catch (Exception exep) {
						System.out.println("no need to scroll");
					}
				}
				randomMessage = CommonUtils.generateGUID();
				CommonOSXSteps.senderPages.getConversationPage()
						.writeNewMessage(randomMessage);
				Thread.sleep(2000);
				CommonOSXSteps.senderPages.getConversationPage()
						.sendNewMessage();
				Thread.sleep(2000);
				try {
					CommonOSXSteps.senderPages.getConversationPage()
							.scrollDownToLastMessage();
					CommonOSXSteps.senderPages.getConversationPage()
							.scrollDownToLastMessage();
				} catch (Exception ex) {
					perfCommon.getLogger().debug("Scrolling fail: ", ex);
				}
				Thread.sleep(2000);
				try {
					CommonOSXSteps.senderPages.getConversationPage()
							.shortcutChooseImageDialog();
					CommonOSXSteps.senderPages
							.setChoosePicturePage(new ChoosePicturePage(
									CommonUtils
											.getOsxAppiumUrlFromConfig(ContactListPage.class),
									CommonUtils
											.getOsxApplicationPathFromConfig(ContactListPage.class)));

					ChoosePicturePage choosePicturePage = CommonOSXSteps.senderPages
							.getChoosePicturePage();
					Assert.assertTrue(choosePicturePage.isVisible());

					choosePicturePage.openImage(picturename);
				} catch (Exception ex) {
					perfCommon.getLogger().debug("Image posting failed: ", ex);
				}
				Thread.sleep(1000);
				try {
					CommonOSXSteps.senderPages.getConversationPage()
							.scrollDownToLastMessage();
				} catch (Exception exep) {
					perfCommon.getLogger().debug("Scrolling fail: ", exep);
				}
			}

			MinimizeZClient();
			SetRandomSleepInterval();
			RestoreZClient();
			Thread.sleep(2000);

			LocalDateTime currentDateTime = LocalDateTime.now();
			diffInMinutes = java.time.Duration.between(startDateTime,
					currentDateTime).toMinutes();
		}

	}

	@When("Minimize ZClient")
	public void MinimizeZClient() throws Exception {
		CommonOSXSteps.senderPages.getContactListPage()
				.waitUntilMainWindowAppears();
		CommonOSXSteps.senderPages.getContactListPage().minimizeZClient();
		perfCommon.getLogger().debug("Client minimized");
	}

	@When("Set random sleep interval")
	public void SetRandomSleepInterval() throws InterruptedException {
		int sleepTimer = ((random
				.nextInt(PerformanceCommon.MAX_WAIT_VALUE_IN_MIN) + PerformanceCommon.MIN_WAIT_VALUE_IN_MIN) * 60 * 1000);
		perfCommon.getLogger().debug("Sleep time: " + sleepTimer / 1000 + " sec.");
		Thread.sleep(sleepTimer);
	}

	@When("Restore ZClient")
	public void RestoreZClient() throws Exception {
		CommonOSXSteps.senderPages.getContactListPage().restoreZClient();
		perfCommon.getLogger().debug("Client restored");
	}

}
