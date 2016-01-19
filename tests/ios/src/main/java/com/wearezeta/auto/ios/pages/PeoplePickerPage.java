package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DummyElement;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PeoplePickerPage extends IOSPage {
    private static final By xpathPickerSearch = By.xpath("//UIATextView[@name='textViewSearch' and @visible='true']");

    private static final By xpathPickerClearButton =
            By.xpath("//*[@name='PeoplePickerClearButton' and @visible='true']");

    private static final By nameKeyboardEnterButton = By.name("Return");

    private static final By nameCreateConversationButton = By.name("CREATE");

    private static final By namePeoplePickerTopPeopleLabel = By.name("TOP PEOPLE");

    private static final By namePeoplePickerAddToConversationButton = By.name("ADD TO CONVERSATION");

    private static final By nameShareButton = By.name("SHARE CONTACTS");

    private static final By nameNotNowButton = By.name("NOT NOW");

    private static final By nameContinueUploadButton = By.name("SHARE CONTACTS");

    private static final By namePeopleYouMayKnowLabel = By.name("CONNECT");

    private static final By nameUnblockButton = By.name("UNBLOCK");

    private static final By xpathPeoplePickerAllTopPeople = By.xpath(
            xpathStrMainWindow + "/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell");

    public static final By xpathInviteCopyButton = By.xpath("//UIACollectionCell[@name='Copy']");

    private static final By nameSendAnInviteButton = By.name("INVITE MORE PEOPLE");

    private static final By nameInstantConnectButton = By.name("instantPlusConnectButton");

    private static final By xpathSearchResultCell = By.xpath(
            xpathStrMainWindow + "/UIACollectionView[1]/UIACollectionCell[1]");

    private static final By nameLaterButton = By.name("MAYBE LATER");

    private static final By nameOpenConversationButton = By.name("OPEN");

    private static final By nameCallButton = By.name("actionBarCallButton");

    private static final By nameSendImageButton = By.name("actionBarCameraButton");

    private static final By xpathContactViewCloseButton =
            By.xpath("//*[@name='ContactsViewCloseButton' and @visible='true']");

    private static final Function<String, String> xpathStrFoundContactByName =
            name -> String.format("//UIAStaticText[@name='%s' and @visible='true']", name);

    private static final Function<String, String> xpathStrSuggestedContactToSwipeByName = name ->
            String.format("//UIACollectionCell[descendant::UIAStaticText[@name='%s']]", name);

    private static final Function<String, String> xpathStrHideButtonForContactByName = name ->
            String.format("//UIAButton[@name='HIDE'][ancestor::UIACollectionCell[descendant::UIAStaticText[@name='%s']]]",
                    name);

    private static final By nameHideSuggestedContactButton = By.name("HIDE");

    private static final Function<String, String> xpathStrSuggestedContactByName = name ->
            String.format("//UIACollectionCell/UIAStaticText[@name='%s']", name);

    private static final Function<Integer, String> xpathStrPeoplePickerTopConnectionsAvatarByIdx = idx ->
            String.format("%s/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell[%s]",
                    xpathStrMainWindow, idx);

    private static final Function<String, String> xpathStrPeoplePickerSelectedCellByName = name ->
            String.format("%s/UIATableView[2]/UIATableCell[@name='%s']", xpathStrMainWindow, name);

    private static final Function<Integer, String> xpathStrPeoplePickerTopConnectionsItemByIdx = idx ->
            String.format(
                    "%s/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell[%d]/UIAStaticText[last()]",
                    xpathStrMainWindow, idx);

    public PeoplePickerPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickMaybeLaterButton() throws Exception {
        getElement(nameLaterButton).click();
    }

    public void closeShareContactsIfVisible() throws Exception {
        final Optional<WebElement> notNowButton = getElementIfDisplayed(nameNotNowButton, 1);
        if (notNowButton.isPresent()) {
            notNowButton.get().click();
        }
    }

    public boolean isPeoplePickerPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathPickerSearch);
    }

    public void tapOnPeoplePickerSearch() throws Exception {
        final WebElement peoplePickerSearch = getElement(xpathPickerSearch);
        this.getDriver().tap(1, peoplePickerSearch.getLocation().x + 40,
                peoplePickerSearch.getLocation().y + 30, 1);
        // workaround for people picker activation
    }

    public void tapOnPeoplePickerClearBtn() throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), getElement(xpathPickerClearButton));
    }

    public double checkAvatarClockIcon(String name) throws Exception {
        BufferedImage clockImage = getAvatarClockIconScreenShot(name);
        String path = CommonUtils.getAvatarWithClockIconPathIOS(GroupChatPage.class);
        BufferedImage templateImage = ImageUtil.readImageFromFile(path);
        return ImageUtil.getOverlapScore(clockImage, templateImage,
                ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
    }

    public BufferedImage getAvatarClockIconScreenShot(String name) throws Exception {
        int multiply = 1;
        String device = CommonUtils.getDeviceName(this.getClass());
        if (device.equalsIgnoreCase("iPhone 6")
                || device.equalsIgnoreCase("iPad Air")) {
            multiply = 2;
        } else if (device.equalsIgnoreCase("iPhone 6 Plus")) {
            multiply = 3;
        }

        final WebElement searchResultCell = getElement(xpathSearchResultCell);
        int x = multiply * searchResultCell.getLocation().x;
        int y = multiply * searchResultCell.getLocation().y;
        int w = multiply
                * (searchResultCell.getLocation().x + searchResultCell
                .getSize().width / 4);
        int h = multiply * searchResultCell.getSize().height;
        return getScreenshotByCoordinates(x, y, w, h).orElseThrow(
                IllegalStateException::new);
    }

    public void fillTextInPeoplePickerSearch(String text) throws Exception {
        // FIXME: Optimize this flow
        final WebElement peoplePickerSearch = getElement(xpathPickerSearch, "Search UI input is not visible");
        try {
            sendTextToSearchInput(text);
            clickSpaceKeyboardButton();
        } catch (WebDriverException ex) {
            peoplePickerSearch.clear();
            sendTextToSearchInput(text);
            clickSpaceKeyboardButton();
        }
    }

    public void sendTextToSearchInput(String text) throws Exception {
        ((IOSElement) getElement(xpathPickerSearch)).setValue(text);
    }

    public boolean waitUserPickerFindUser(String user) throws Exception {
        final By locator = By.xpath(xpathStrFoundContactByName.apply(user));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator, 5);
    }

    public void clickOnNotConnectedUser(String name) throws Exception {
        final By locator = By.xpath(xpathStrFoundContactByName.apply(name));
        if (this.waitUserPickerFindUser(name)) {
            WebElement foundContact = getDriver().findElement(locator);
            DriverUtils.waitUntilElementClickable(getDriver(), foundContact);
            foundContact.click();
        } else {
            throw new IllegalArgumentException(String.format("'%s' is not present in search results", name));
        }
    }

    public void pickUserAndTap(String name) throws Exception {
        pickUser(name).click();
    }

    public void pickIgnoredUserAndTap(String name) throws Exception {
        pickUser(name).click();
    }

    public void dismissPeoplePicker() throws Exception {
        getElement(xpathPickerClearButton, "Clear button is not visible in the search field").click();
    }

    public void swipeToRevealHideSuggestedContact(String contact) throws Exception {
        assert this.waitUserPickerFindUser(contact) :
                String.format("'%s' is not visible in People Picker", contact);
        final By locator = By.xpath(xpathStrSuggestedContactToSwipeByName.apply(contact));
        final WebElement contactToSwipe = this.getDriver().findElement(locator);
        int count = 0;
        do {
            DriverUtils.swipeRight(this.getDriver(), contactToSwipe, 500, 50, 50);
            count++;
        } while (!isHideButtonVisible() || count > 3);
    }

    public void swipeCompletelyToDismissSuggestedContact(String contact) throws Exception {
        assert this.waitUserPickerFindUser(contact) : String.format("'%s' is not visible in People Picker", contact);
        final By locator = By.xpath(xpathStrSuggestedContactToSwipeByName.apply(contact));
        DriverUtils.swipeRight(this.getDriver(), getDriver().findElement(locator), 1000, 100, 50);
    }

    public void tapHideSuggestedContact(String contact) throws Exception {
        final By locator = By.xpath(xpathStrHideButtonForContactByName.apply(contact));
        getElement(locator, String.format("Hide button is not visible for '%s'", contact)).click();
    }

    public boolean isHideButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameHideSuggestedContactButton);
    }

    public boolean isSuggestedContactVisible(String contact) throws Exception {
        final By locator = By.xpath(xpathStrSuggestedContactByName.apply(contact));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 2);
    }

    public boolean addToConversationNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), namePeoplePickerAddToConversationButton);
    }

    public void clickOnGoButton() throws Exception {
        getElement(nameKeyboardEnterButton).click();
    }

    private WebElement pickUser(String name) throws Exception {
        fillTextInPeoplePickerSearch(name);
        waitUserPickerFindUser(name);
        return getDriver().findElementByName(name);
    }

    public void selectUser(String name) throws Exception {
        List<WebElement> elements = getDriver().findElements(By.name(name));
        if (elements.size() == 0) {
            throw new NoSuchElementException("Element not found");
        }
        for (WebElement el : elements) {
            if (el.isDisplayed() && el.isEnabled()) {
                DriverUtils.tapByCoordinates(getDriver(), el);
                break;
            }
        }
    }

    public void tapNumberOfTopConnections(int numberToTap) throws Exception {
        for (int i = 1; i < numberToTap + 1; i++) {
            final By locator = By.xpath(xpathStrPeoplePickerTopConnectionsAvatarByIdx.apply(i));
            getDriver().findElement(locator).click();
        }
    }

    public boolean isCreateConversationButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameCreateConversationButton);
    }

    public void clickCreateConversationButton() throws Throwable {
        getElement(nameCreateConversationButton).click();
    }

    public boolean isTopPeopleLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), namePeoplePickerTopPeopleLabel, 2);
    }

    public boolean isConnectLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), namePeopleYouMayKnowLabel);
    }

    public boolean isUserSelected(String name) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerSelectedCellByName.apply(name));
        return getDriver().findElement(locator).getAttribute("value").equals("1");
    }

    public void clickConnectedUserAvatar(String name) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerSelectedCellByName.apply(name));
        getDriver().findElement(locator).click();
    }

    public void hitDeleteButton() throws Exception {
        getElement(xpathPickerSearch).sendKeys(Keys.DELETE);
    }

    public void clickAddToCoversationButton() throws Exception {
        getElement(namePeoplePickerAddToConversationButton).click();
    }

    public void clickOnUserOnPending(String contact) throws Exception {
        WebElement el = getDriver().findElement(By.name(contact));
        DriverUtils.tapByCoordinates(getDriver(), el);
    }

    public boolean isUploadDialogShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameShareButton, 2);
    }

    public void clickContinueButton() throws Exception {
        getElementIfDisplayed(nameContinueUploadButton).orElseGet(DummyElement::new).click();
    }

    public boolean isPeopleYouMayKnowLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), namePeopleYouMayKnowLabel);
    }

    private void unblockButtonDoubleClick() throws Exception {
        DriverUtils.multiTap(getDriver(), getDriver().findElement(nameUnblockButton), 2);
    }

    public void unblockUser() throws Exception {
        getElement(nameUnblockButton).click();
    }

    public void unblockUserOniPad() throws Exception {
        unblockButtonDoubleClick();
    }

    public int getNumberOfSelectedTopPeople() throws Exception {
        int selectedPeople = 0;
        for (WebElement people : getElements(xpathPeoplePickerAllTopPeople)) {
            if (people.getAttribute("value").equals("1")) {
                selectedPeople++;
            }
        }
        return selectedPeople;
    }

    public void tapSendInviteButton() throws Exception {
        getElement(nameSendAnInviteButton).click();
    }

    public void tapSendInviteCopyButton() throws Exception {
        getElement(xpathInviteCopyButton).click();
    }

    public void pressInstantConnectButton() throws Exception {
        getElement(nameInstantConnectButton).click();
    }

    public String getNameOfNuser(int i) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerTopConnectionsItemByIdx.apply(i));
        return this.getDriver().findElement(locator).getAttribute("name");
    }

    public void tapNumberOfTopConnectionsButNotUser(int numberToTap,
                                                    String contact) throws Exception {
        for (int i = 1; i < numberToTap + 1; i++) {
            if (!contact.equals(getNameOfNuser(i).toLowerCase())) {
                final By locator = By.xpath(xpathStrPeoplePickerTopConnectionsAvatarByIdx.apply(i));
                getDriver().findElement(locator).click();
            } else {
                numberToTap++;
            }
        }
    }

    public void tapOnTopConnectionAvatarByOrder(int i) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerTopConnectionsAvatarByIdx.apply(i));
        getDriver().findElement(locator).click();
    }

    public boolean isOpenConversationButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameOpenConversationButton);
    }

    public void clickOpenConversationButton() throws Exception {
        getElement(nameOpenConversationButton, "Open conversation button is not visible").click();
    }

    public boolean isCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCallButton);
    }

    public void clickCallButton() throws Exception {
        getElement(nameCallButton, "Call button is not visible").click();
    }

    public boolean isSendImageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameSendImageButton);
    }

    public void clickSendImageButton() throws Exception {
        getElement(nameSendImageButton, "Send image button is not visible").click();
    }

    public void inputTextInSearch(String text) throws Exception {
        getElement(xpathPickerSearch, "Search input is not visible").sendKeys(text);
    }

    public void closeInviteList() throws Exception {
        getElement(xpathContactViewCloseButton).click();
    }
}
