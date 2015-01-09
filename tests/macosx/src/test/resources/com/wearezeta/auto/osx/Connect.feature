Feature: Connect to user

  @smoke @id473
  Scenario Outline: Receive invitation from user
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I see connect invitation
    And I open conversation with One person waiting
    And I accept invitation
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id616
  Scenario Outline: Conversation created on second end after user accept connection request
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I see connect invitation
    And I open conversation with One person waiting
    And I accept invitation
    And I see Contact list with name <Contact>
    And I am signing out
    And I reset Wire defaults and restart client
    And I Sign in using login <Contact> and password <Password>
    And I see my name <Contact> in Contact list
    Then I see Contact list with name <Name>
    And I open conversation with <Name>
    And I see message CONNECTED TO <Name> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1409
  Scenario Outline: Verify sending a connection request to user chosen from people view
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from contact list
    And I search by email for user <Contact>
    And I see user <Contact> in search results
    And I add user <Contact> from search results
    And I send invitation to user
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
