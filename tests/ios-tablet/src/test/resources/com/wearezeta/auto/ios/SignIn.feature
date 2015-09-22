Feature: Sign In

  @regression @rc @id2607
  Scenario Outline: Sign in to ZClient [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    Then I see Contact list with my name <Name>

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id2925
  Scenario Outline: Sign in to ZClient [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    Then I see Contact list with my name <Name>

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id2749 @noAcceptAlert
  Scenario Outline: Notification if SignIn credentials are wrong [PORTRAIT]
    Given I see sign in screen
    When I press Sign in button
    And I enter wrong email <WrongMail>
    And I enter wrong password <WrongPassword>
    And I attempt to press Login button
    Then I see wrong credentials notification

    Examples: 
      | WrongMail  | WrongPassword |
      | wrongwrong | wrong         |

  @regression @id2924 @noAcceptAlert
  Scenario Outline: Notification if SignIn credentials are wrong [LANDSCAPE]
    Given I see sign in screen
    Given I rotate UI to landscape
    When I press Sign in button
    And I enter wrong email <WrongMail>
    And I enter wrong password <WrongPassword>
    And I attempt to press Login button
    Then I see wrong credentials notification

    Examples: 
      | WrongMail  | WrongPassword |
      | wrongwrong | wrong         |

  @regression @rc @id2608
  Scenario Outline: Verify possibility of reseting password (welcome page) [PORTRAIT]
    Given I see sign in screen
    And I press Sign in button
    And I click on Change Password button on SignIn
    Then I see reset password page
    And I change URL to staging
    And I type in email <Login> to change password
    And I press Change Password button in browser
    And I copy link from email and past it into Safari
    And I type in new password <NewPassword>
    And I press Change Password button in browser
    And Return to Wire app
    And I sign in using my email
    Then I see Contact list with my name <Name>

    Examples: 
      | Login      | Password      | Name      | NewPassword  |
      | user1Email | user1Password | user1Name | aqa123456789 |

  @regression @id2923
  Scenario Outline: Verify possibility of reseting password (welcome page) [LANDSCAPE]
    Given I see sign in screen
    Given I rotate UI to landscape
    And I press Sign in button
    And I click on Change Password button on SignIn
    Then I see reset password page
    And I change URL to staging
    And I type in email <Login> to change password
    And I press Change Password button in browser
    And I copy link from email and past it into Safari
    And I type in new password <NewPassword>
    And I press Change Password button in browser
    And Return to Wire app
    And I sign in using my email
    Then I see Contact list with my name <Name>

    Examples: 
      | Login      | Password      | Name      | NewPassword  |
      | user1Email | user1Password | user1Name | aqa123456789 |

  @staging @id3817
  Scenario Outline: Verify phone sign in when email is assigned [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    Then I see PHONE SIGN IN button
    When I tap on PHONE SIGN IN button
    Then I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    Then I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id3818
  Scenario Outline: Verify phone sign in when email is assigned [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    Then I see PHONE SIGN IN button
    When I tap on PHONE SIGN IN button
    Then I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    Then I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
