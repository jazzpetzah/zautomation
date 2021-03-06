Feature: Bring Your Friends

  @C2312 @smoke
  Scenario Outline: Use Gmail contacts import from search UI
    Given There are 1 users where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    Then I open search by clicking the people button
    And I click button to bring friends from Gmail
    And I see Google login popup
    And I sign up at Google with email <Gmail> and password <GmailPassword>
    Then I see Search is opened
    And I see more than 5 suggestions in people picker

    Examples:
      | Login      | Password      | Name      | Gmail                      | GmailPassword |
      | user1Email | user1Password | user1Name | smoketester.wire@gmail.com | aqa123456!    |

  @C2311 @smoke
  Scenario Outline: Use Gmail contacts import from settings
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I type shortcut combination to open preferences
    And I open options in preferences
    And I click button to import contacts from Gmail
    And I see Google login popup
    And I sign up at Google with email smoketester.wire@gmail.com and password aqa123456!
    Then I see Search is opened
    Then I see more than 5 suggestions in people picker

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |