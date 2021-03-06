package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DummyElement;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class LoginPage extends IOSPage {

    private static final By nameSwitchToLoginButton = MobileBy.AccessibilityId("LOG IN");

    private static final By nameSwitchToEmailLogin = MobileBy.AccessibilityId("EMAIL");

    private static final By nameSwitchToPhoneLogin = MobileBy.AccessibilityId("PHONE");

    private static final By nameLoginButton = MobileBy.AccessibilityId("RegistrationConfirmButton");

    private static final By fbNameLoginField = FBBy.AccessibilityId("EmailField");

    private static final By fbNamePasswordField = FBBy.AccessibilityId("PasswordField");

    private static final By nameWrongCredentialsNotification = MobileBy.AccessibilityId("Please verify your details and try again.");

    private static final By nameMaybeLater = MobileBy.AccessibilityId("MAYBE LATER");

    private static final By nameInvalidEmail = MobileBy.AccessibilityId("Please enter a valid email address");

    private static final By nameAlreadyRegisteredEmail =
            MobileBy.AccessibilityId("The email address you provided has already been registered. Please try again.");

    private static final By nameNotNowButton = MobileBy.AccessibilityId("NOT NOW");

    public String message;

    public LoginPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        try {
            if (isLocatorDisplayed(MobileBy.AccessibilityId(nameStrMainWindow))) {
                return true;
            }
        } catch (IllegalStateException e) {
            // Workaround for extra alert
        }
        acceptAlert();
        return isLocatorDisplayed(MobileBy.AccessibilityId(nameStrMainWindow));
    }

    public void switchToEmailLogin() throws Exception {
        final WebElement emailSwitchBtn = getElement(nameSwitchToEmailLogin);
        emailSwitchBtn.click();
        //Work around: to click again, when it didnt got clicked the first time
        if (!isLocatorInvisible(nameSwitchToEmailLogin)) {
            emailSwitchBtn.click();
        }
    }

    public void switchToPhoneLogin() throws Exception {
        getElement(nameSwitchToPhoneLogin).click();
    }

    public void waitForLoginToFinish() throws Exception {
        if (!isLocatorInvisible(nameSwitchToLoginButton, LOGIN_TIMEOUT)) {
            throw new IllegalStateException("Login button is still visible after the timeout");
        }
    }

    public void tapLoginButton() throws Exception {
        getElement(nameLoginButton).click();
    }

    public void setLogin(String login) throws Exception {
        final FBElement el = ((FBElement) getElement(fbNameLoginField));
        el.click();
        el.clear();
        el.sendKeys(login);
    }

    public void setPassword(String password) throws Exception {
        final FBElement el = ((FBElement) getElement(fbNamePasswordField));
        el.click();
        el.clear();
        el.sendKeys(password);
    }

    public static final Timedelta LOGIN_TIMEOUT = Timedelta.fromSeconds(30);

    public void dismissSettingsWarning() throws Exception {
        final WebElement maybeLaterBtn = getElement(nameMaybeLater, "MAYBE LATER link is not visible",
                LOGIN_TIMEOUT);
        maybeLaterBtn.click();
        if (!isLocatorInvisible(nameMaybeLater, Timedelta.fromSeconds(3))) {
            maybeLaterBtn.click();
        }
    }

    public void dismissSettingsWarningIfVisible() throws Exception {
        getElementIfDisplayed(nameMaybeLater).orElseGet(DummyElement::new).click();
    }

    public boolean isContactsButtonVisible() throws Exception {
        return isLocatorDisplayed(ConversationsListPage.nameContactsButton, LOGIN_TIMEOUT);
    }

    public void tapHoldEmailInput() throws Exception {
        final FBElement loginField = (FBElement) getElement(fbNameLoginField);
        message = loginField.getText();
        loginField.longTap();
    }

    public void tapPasswordField() throws Exception {
        getElement(fbNamePasswordField).click();
    }

    public boolean wrongCredentialsNotificationIsShown() throws Exception {
        return isLocatorDisplayed(nameWrongCredentialsNotification, Timedelta.fromSeconds(30));
    }

    public boolean isInvalidEmailAlertShown() throws Exception {
        return readAlertText().isPresent() && isLocatorDisplayed(nameInvalidEmail);
    }

    public boolean isAlreadyRegisteredEmailAlertShown() throws Exception {
        return readAlertText().isPresent() && isLocatorDisplayed(nameAlreadyRegisteredEmail);
    }

    public void tapPhoneNotNow() throws Exception {
        getElement(nameNotNowButton).click();
    }

    public void switchToLogin() throws Exception {
        final Optional<WebElement> switchToLoginButton = getElementIfExists(nameSwitchToLoginButton);
        if (switchToLoginButton.isPresent()) {
            try {
                switchToLoginButton.get().click();
                return;
            } catch (IllegalStateException e) {
                // just ignore
            }
        }
        acceptAlertIfVisible();
        getElement(nameSwitchToLoginButton).click();
    }

    public void inputLoginCode(PhoneNumber forNumber) throws Exception {
        final WebElement codeInput = getElement(RegistrationPage.nameVerificationCodeInput,
                "Login code input is not visible");
        final String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(forNumber);
        codeInput.sendKeys(code);
        getElement(RegistrationPage.nameConfirmButton, "Confirm button is not visible",
                Timedelta.fromSeconds(2)).click();
    }
}
