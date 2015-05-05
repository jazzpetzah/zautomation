Feature: Sign Out

  @id329 @smoke
  Scenario Outline: Sign out from ZClient
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on my avatar
    And I tap options button
    And I tap sign out button
    Then I see welcome screen

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
