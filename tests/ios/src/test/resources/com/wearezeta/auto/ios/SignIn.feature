Feature: Sign In

  @C1134 @regression @rc @clumsy @id340
  Scenario Outline: Sign in to ZClient
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I switch to Log In tab
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    And I accept First Time overlay if it is visible
    And I dismiss settings warning
    Then I see conversations list

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C1133 @regression @rc @clumsy @id1398 @noAcceptAlert
  Scenario Outline: Notification if SignIn credentials are wrong
    Given I see sign in screen
    When I switch to Log In tab
    And I enter wrong email <WrongMail>
    And I enter wrong password <WrongPassword>
    And I attempt to press Login button
    Then I see wrong credentials notification

    Examples:
      | WrongMail  | WrongPassword |
      | wrongwrong | wrong         |

  @C1138 @regression @id2719 @ZIOS-5585
  Scenario Outline: Verify phone sign in when email is assigned
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for user <Name>
    And I enter verification code for user <Name>
    And I dismiss settings warning
    Then I see conversations list

    Examples:
      | Name      |
      | user1Name |

  @C1145 @regression @id3813 @noAcceptAlert
  Scenario Outline: Verify impossibility to login with the wrong code
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    And I enter phone number for user Myself
    When I enter random verification code
    Then I see wrong credentials notification

    Examples:
      | Name      |
      | user1Name |

  @C1146 @regression @id3838 @noAcceptAlert
  Scenario Outline: Verify impossibility to resend code within 10 min
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    And I enter phone number for user <Name>
    When I tap RESEND code button
    Then I see Resend will be possible after 10 min alert

    Examples:
      | Name      |
      | user1Name |

  @C1143 @regression @id2724 @noAcceptAlert
  Scenario Outline: Verify impossibility to login with unregistered phone number
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter <Count> digits phone number
    Then I see invalid phone number alert

    Examples:
      | Count |
      | 10    |

  @C1136 @rc @regression @id3851 @ZIOS-5585 @useSpecialEmail
  Scenario Outline: Verify first time phone sign in when email is not assigned
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for user <Name>
    And I enter verification code for user <Name>
    And I have entered login <Email>
    And I start activation email monitoring
    And I have entered password <Password>
    And I click DONE keyboard button
    And I see email verification reminder
    And I verify registration address
    And I dismiss settings warning
    Then I see conversations list
    When I tap settings gear button
    Then I see email <Email> on Personal page

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C1089 @regression @id3863 @noAcceptAlert @ZIOS-5585
  Scenario Outline: Verify error message appears in case of registering already taken email
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    And I accept alert
    And I see set email/password suggesstion page
    When I have entered login <Email>
    And I have entered password <Password>
    When I click DONE keyboard button
    Then I see already registered email alert

    Examples:
      | Email                     | Password      | Name      |
      | smoketester@wearezeta.com | user1Password | user1Name |

  @C1147 @regression @id3857 @noAcceptAlert @ZIOS-5585
  Scenario Outline: Verify error message appears in case of registering already taken email
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    And I accept alert
    And I see set email/password suggesstion page
    When I have entered login <Email>
    And I have entered password <Password>
    When I click DONE keyboard button
    Then I see invalid email alert

    Examples:
      | Email        | Password      | Name      |
      | invalidemail | user1Password | user1Name |