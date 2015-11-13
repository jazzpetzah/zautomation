Feature: Sign In

  @regression @id1788
  Scenario Outline: Sign in to Wire for Web
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    Given I see Sign In page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then I am signed in properly
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @smoke @id1792
  Scenario Outline: Verify sign in error appearance in case of wrong credentials
    Given There is 1 user where user1Name is me
    Given I switch to sign in page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then the sign in error message reads <Error>
    And the email field on the sign in form is marked as error
    And the password field on the sign in form is marked as error

    Examples: 
      | Email      | Password      | Error                                      |
      | user1Email | wrongPassword | Wrong email or password. Please try again. |

  @smoke @id4014
  Scenario Outline: Verify sign in button is disabled in case of empty credentials
    Given There is 1 user where user1Name is me
    When I switch to sign in page
    Then Sign In button is disabled
    When I enter email "<Email>"
    And I enter password ""
    Then Sign In button is disabled
    When I enter email ""
    And I enter password "<Password>"
    Then Sign In button is disabled

    Examples: 
      | Email      | Password      |
      | user1Email | user1Password |

  @smoke @id2714
  Scenario Outline: Verify you can sign in with a phone number with correct credentials
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I switch to phone number sign in page
    When I sign in using phone number of user <Name>
    And I click on sign in button on phone number sign in
    And I enter phone verification code for user <Name>
    Then I am signed in properly
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list

    Examples: 
      | Name      |
      | user1Name |

  @regression @id2715
  Scenario Outline: Verify you see correct error message when sign in with incorrect phone number
    Given I switch to sign in page
    When I switch to phone number sign in page
    And I enter country code <CountryCode> on phone number sign in
    And I enter phone number <PhoneNumber> on phone number sign in
    And I click on sign in button on phone number sign in
    Then I see invalid phone number error message saying <Error>

    Examples: 
      | CountryCode | PhoneNumber | Error                |
      | +49         | 9999999999  | Unknown Phone Number |
      | +49         | qwerqwer    | Invalid Phone Number |
      | +49         | !@$!@$      | Invalid Phone Number |

  @regression @id2716
  Scenario Outline: Verify you see correct error message when sign in with a phone number with incorrect code
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I switch to phone number sign in page
    When I sign in using phone number of user <Name>
    And I click on sign in button on phone number sign in
    And I enter wrong phone verification code for user <Name>
    Then I see invalid phone code error message saying <Error>

    Examples: 
      | Name      | Error        |
      | user1Name | Invalid Code |

  @regression @id2707
  Scenario Outline: Verify you are asked to add an email address after sign in with a phone number
    Given There is 1 user where <Name> is me with phone number only
    Given I switch to sign in page
    When I switch to phone number sign in page
    When I sign in using phone number of user <Name>
    And I click on sign in button on phone number sign in
    And I enter phone verification code for emailless user <Name>
    And I enter email address <EmailOfOtherUser> on add email address dialog
    And I enter password <PasswordOfOtherUser> on add email address dialog
    And I click add button on add email address dialog
    Then I see error message on add email address dialog saying <ErrorAlready>
    And the email field on add email address dialog is marked as error
    When I enter email address <InvalidEmail> on add email address dialog
    And I enter password <PasswordOfOtherUser> on add email address dialog
    And I click add button on add email address dialog
    Then I see error message on add email address dialog saying <ErrorInvalidEmail>
    And the email field on add email address dialog is marked as error
    When I enter email of user <Name> on add email address dialog
    And I enter password <InvalidPassword> on add email address dialog
    And I click add button on add email address dialog
    Then I see error message on add email address dialog saying <ErrorInvalidPassword>
    And the password field on add email address dialog is marked as error
    When I enter email of user <Name> on add email address dialog
    And I enter password <PasswordOfOtherUser> on add email address dialog
    And I click add button on add email address dialog
    Then I verify that an envelope icon is shown

    Examples: 
      | Name      | EmailOfOtherUser      | PasswordOfOtherUser | ErrorAlready                | InvalidEmail | ErrorInvalidEmail          | InvalidPassword | ErrorInvalidPassword                      |
      | user1Name | qa1+qa1@wearezeta.com | aqa123456!          | Email address already taken | @example.com | Not a valid email address. | 123             | Your password needs at least 8 characters |

  @regression @id2227
  Scenario Outline: Show invitation button when Gmail import on registration has no suggestions
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    Given I see Sign In page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then I am signed in properly
    And I see Contacts Upload dialog
    And I click button to import Gmail Contacts
    And I see Google login popup
    When I sign up at Google with email <Gmail> and password <GmailPassword>
    Then I see Search is opened
    And I see Bring Your Friends button on People Picker page
    When I click Bring Your Friends button on People Picker page
    Then I remember invitation link on Bring Your Friends popover
    And I do not see Gmail Import button on People Picker page

    Examples: 
      | Email      | Password      | Name      | Gmail                       | GmailPassword |
      | user1Email | user1Password | user1Name | smoketester.wire2@gmail.com | aqa123456!    |

  @regression @id4070
  Scenario Outline: Verify Skip for now button is shown when youre adding an email address after sign in with a phone number
    Given There is 1 user where <Name> is me with phone number only
    Given I switch to sign in page
    When I switch to phone number sign in page
    When I sign in using phone number of user <Name>
    And I click on sign in button on phone number sign in
    And I enter phone verification code for emailless user <Name>
    Then I see Skip for now button on add email address dialog
    When I click Skip for now button on add email address dialog
    Then I am signed in properly
    And I see Self Picture Upload dialog

    Examples: 
      | Name      |
      | user1Name |
