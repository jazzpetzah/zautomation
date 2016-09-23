package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class GroupChatInfoPage extends IOSPage {
    private static final By nameRightActionButton = MobileBy.AccessibilityId("metaControllerRightButton");

    private static final By nameLeaveConversationButton = MobileBy.AccessibilityId("LEAVE");

    private static final By fbNameConversationNameTextField = FBBy.AccessibilityId("ParticipantsView_GroupName");

    private static final Function<String, String> xpathStrConversationNameByText = text ->
            String.format("//*[@name='ParticipantsView_GroupName' and @value='%s']", text);

    private static final Function<String, String> xpathStrConversationNameByExpr = expr ->
            String.format("//*[@name='ParticipantsView_GroupName' and %s]", expr);

    private static final By nameExitParticipantInfoPageButton = MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    private static final By nameExitGroupInfoPageButton = MobileBy.AccessibilityId("metaControllerCancelButton");

    private static final By namLeftActionButton = MobileBy.AccessibilityId("metaControllerLeftButton");

    private static final By nameAddPeopleContinueButton = MobileBy.AccessibilityId("CONTINUE");

    private static final By nameLeaveConversationAlert = MobileBy.AccessibilityId("Leave conversation?");

    private static final Function<String, String> xpathStrUserNameLabelByText = text ->
            String.format("//XCUIElementTypeCollectionView[preceding-sibling::" +
                    "XCUIElementTypeTextView[@name='ParticipantsView_GroupName']]" +
                    "/XCUIElementTypeCell//XCUIElementTypeStaticText[@name='%s']", text);

    private static final Function<Integer, String> nameStrNumberPeopleByCount =
            count -> String.format("%s PEOPLE", count);

    private static final Function<String, String> xpathPeopleViewCollectionCellByName = name ->
            String.format("//XCUIElementTypeButton[@name='metaControllerCancelButton']/following::" +
                            "XCUIElementTypeCollectionView/XCUIElementTypeCell//XCUIElementTypeStaticText[@name='%s']",
                    name.toUpperCase());

    private static final By classNameParticipantAvatarCell = By.className("XCUIElementTypeCell");

    public GroupChatInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isGroupNameEqualTo(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrConversationNameByText.apply(expectedName));
        return isElementDisplayed(locator);
    }

    public void setGroupChatName(String name) throws Exception {
        final FBElement nameInputField = (FBElement) getElement(fbNameConversationNameTextField);
        nameInputField.click();
        this.isKeyboardVisible();
        nameInputField.clear();
        nameInputField.setValue(name);
        tapKeyboardCommitButton();
    }

    public boolean isCorrectConversationName(List<String> expectedNames) throws Exception {
        final String xpathExpr = String.join(" and ", expectedNames.stream().
                map(x -> String.format("contains(@value, '%s')", x)).
                collect(Collectors.toList()));
        final By locator = By.xpath(xpathStrConversationNameByExpr.apply(xpathExpr));
        return isElementDisplayed(locator);
    }

    public boolean isNumberOfPeopleEquals(int expectedNumber) throws Exception {
        final By locator = MobileBy.AccessibilityId(nameStrNumberPeopleByCount.apply(expectedNumber));
        return isElementDisplayed(locator);
    }

    public int getParticipantsAvatarsCount() throws Exception {
        return selectVisibleElements(classNameParticipantAvatarCell).size();
    }

    public void exitParticipantInfoPage() throws Exception {
        getElement(nameExitParticipantInfoPageButton).click();
    }

    public void exitGroupInfoPage() throws Exception {
        final WebElement closeBtn = getElement(nameExitGroupInfoPageButton);
        closeBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameExitGroupInfoPageButton, 3)) {
            // Sometimes we don't click this button in time because of animated transitions
            closeBtn.click();
        }
    }

    public void tapLeaveConversation() throws Exception {
        getElement(nameRightActionButton).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameRightActionButton)) {
            throw new IllegalStateException("Menu button is still shown");
        }
        getElement(nameLeaveConversationButton).click();
    }

    public void confirmLeaveConversation() throws Exception {
        getElement(nameLeaveConversationButton).click();
    }

    public void selectParticipant(String name) throws Exception {
        final By locator = FBBy.xpath(xpathPeopleViewCollectionCellByName.apply(name));
        this.tapAtTheCenterOfElement((FBElement) getElement(locator));
    }

    public boolean isLeaveConversationAlertVisible() throws Exception {
        return isElementDisplayed(nameLeaveConversationAlert);
    }

    public void clickOnAddButton() throws Exception {
        getElement(namLeftActionButton).click();
    }

    public void clickOnAddDialogContinueButton() throws Throwable {
        getElement(nameAddPeopleContinueButton).click();
    }

    public boolean waitForContactToDisappear(String contact) throws Exception {
        final By locator = By.xpath(xpathStrUserNameLabelByText.apply(contact));
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }
}
