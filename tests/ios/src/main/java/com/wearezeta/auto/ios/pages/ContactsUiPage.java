package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ContactsUiPage extends IOSPage {

    private static final By xpathSearchInput =
            By.xpath("//XCUIElementTypeTextView[ .//XCUIElementTypeStaticText[@name='SEARCH BY NAME'] ]");

    private static final By nameInviteOthersButton = MobileBy.AccessibilityId("INVITE OTHERS");

    private static final Function<String, String> xpathStrConvoCellByName = name ->
            String.format("//XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s'] ]", name);

    private static final Function<String, String> xpathStrOpenButtonByConvoName = name ->
            String.format("%s/XCUIElementTypeButton[@name='OPEN']", xpathStrConvoCellByName.apply(name));

    private static final By nameXButton = MobileBy.AccessibilityId("ContactsViewCloseButton");

    public ContactsUiPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isSearchInputVisible() throws Exception {
        return isLocatorDisplayed(xpathSearchInput);
    }

    public void inputTextToSearch(String text) throws Exception {
        final WebElement input = getElement(xpathSearchInput);
        input.click();
        input.sendKeys(text);
    }

    public boolean isContactVisible(String contact) throws Exception {
        final By locator = By.xpath(xpathStrConvoCellByName.apply(contact));
        return isLocatorDisplayed(locator);
    }

    public void tapOpenButtonNextToUser(String contact) throws Exception {
        final By locator = By.xpath(xpathStrOpenButtonByConvoName.apply(contact));
        getElement(locator).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    private static By getButtonByName(String name) {
        switch (name.toLowerCase()) {
            case "invite others":
                return nameInviteOthersButton;
            case "x":
                return nameXButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String btnName) throws Exception {
        final By locator = getButtonByName(btnName);
        getElement(locator).click();
        if (locator.equals(nameXButton)) {
            // Wait for animation
            Thread.sleep(2000);
        }
    }

    public boolean isButtonVisible(String btnName) throws Exception {
        final By locator = getButtonByName(btnName);
        return isLocatorDisplayed(locator);
    }

    public boolean isContactInvisible(String contact) throws Exception {
        final By locator = By.xpath(xpathStrConvoCellByName.apply(contact));
        return isLocatorInvisible(locator);
    }
}
