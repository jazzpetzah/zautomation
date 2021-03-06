package com.wearezeta.auto.common.usrmgmt;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.MessagingUtils;

import java.util.*;

public class ClientUser {
    private static final int TOKEN_TIMEOUT = 10 * 60; // seconds

    private long lastTokenRequestTimestamp = -1;

    private Optional<String> id = Optional.empty();
    private String name = null;
    private String password = null;
    private String email = null;
    private Set<String> emailAliases = new HashSet<>();
    private Set<String> nameAliases = new HashSet<>();
    private PhoneNumber phoneNumber;
    private Set<String> phoneNumberAliases = new HashSet<>();
    private String uniqueUsername = null;
    private Set<String> uniqueUsernameAliases = new HashSet<>();
    private Set<String> passwordAliases = new HashSet<>();
    private Optional<String> tokenType = Optional.empty();
    private Optional<String> token = Optional.empty();
    private AccentColor accentColor = AccentColor.Undefined;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getNameAliases() {
        return new HashSet<>(this.nameAliases);
    }

    public void addNameAlias(String alias) {
        this.nameAliases.add(alias);
    }

    public void removeNameAlias(String alias) {
        this.nameAliases.remove(alias);
    }

    public void clearNameAliases() {
        this.nameAliases.clear();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getPasswordAliases() {
        return new HashSet<>(this.passwordAliases);
    }

    public void addPasswordAlias(String alias) {
        this.passwordAliases.add(alias);
    }

    public void removePasswordAlias(String alias) {
        this.passwordAliases.remove(alias);
    }

    public void clearPasswordAliases() {
        this.passwordAliases.clear();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getEmailAliases() {
        return new HashSet<>(this.emailAliases);
    }

    public void addEmailAlias(String alias) {
        this.emailAliases.add(alias);
    }

    public void removeEmailAlias(String alias) {
        this.emailAliases.remove(alias);
    }

    public void clearEmailAliases() {
        this.emailAliases.clear();
    }

    private void refreshTokenInfoIfExpired() throws Exception {
        if (!this.id.isPresent() || !this.tokenType.isPresent() || !this.token.isPresent() ||
                lastTokenRequestTimestamp < 0 ||
                System.currentTimeMillis() - lastTokenRequestTimestamp >= TOKEN_TIMEOUT * 1000) {
            final ClientToken clientToken = BackendAPIWrappers.login(
                    this.getEmail(), this.getPassword(), this.getPhoneNumber()
            );
            this.id = Optional.of(clientToken.getId());
            this.tokenType = Optional.of(clientToken.getTokenType());
            this.token = Optional.of(clientToken.getToken());
            lastTokenRequestTimestamp = System.currentTimeMillis();
        }
    }

    public String getId() throws Exception {
        refreshTokenInfoIfExpired();
        return id.get();
    }

    public String getTokenType() throws Exception {
        refreshTokenInfoIfExpired();
        return tokenType.get();
    }

    public String getToken() throws Exception {
        refreshTokenInfoIfExpired();
        return token.get();
    }

    public void setAccentColor(AccentColor newColor) {
        this.accentColor = newColor;
    }

    public AccentColor getAccentColor() {
        return this.accentColor;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<String> getPhoneNumberAliases() {
        return new HashSet<>(this.phoneNumberAliases);
    }

    public void addPhoneNumberAlias(String alias) {
        this.phoneNumberAliases.add(alias);
    }

    public void removePhoneNumberAlias(String alias) {
        this.phoneNumberAliases.remove(alias);
    }

    public void clearPhoneNumberAliases() {
        this.phoneNumberAliases.clear();
    }

    public String getUniqueUsername() {
        return uniqueUsername;
    }

    public void setUniqueUsername(String uniqueUsername) {
        this.uniqueUsername = uniqueUsername;
    }

    public void addUniqueUsernameAlias(String alias) {
        this.uniqueUsernameAliases.add(alias);
    }

    public void removeUniqueUsernameAlias(String alias) {
        this.uniqueUsernameAliases.remove(alias);
    }

    public void clearUniqueUsernameAliases() {
        this.uniqueUsernameAliases.clear();
    }

    public Set<String> getUniqueUsernameAliases() {
        return new HashSet<>(this.uniqueUsernameAliases);
    }

    private static String generateUniqueName() {
        return CommonUtils.generateGUID().replace("-", "").substring(0, 8);
    }

    public ClientUser() throws Exception {
        this.name = generateUniqueName();
        this.phoneNumber = new PhoneNumber(PhoneNumber.WIRE_COUNTRY_PREFIX);
        this.password = CommonUtils.getDefaultPasswordFromConfig(ClientUser.class);
        this.email = MessagingUtils.generateEmail(MessagingUtils.getDefaultAccountName(), name);
        this.uniqueUsername = generateUniqueName();
    }

    public void forceTokenExpiration() {
        this.id = Optional.empty();
        this.tokenType = Optional.empty();
        this.token = Optional.empty();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof ClientUser) && ((ClientUser) other).getEmail().equals(getEmail());
    }
}
