Feature: Conversation View

  @staging @id330
  Scenario Outline: Send Message to contact
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
