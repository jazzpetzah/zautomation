Feature: Self Profile

  @id205 @smoke
  Scenario Outline: Change user picture
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    #workaround
    And I minimize the application
    And I restore the application
    #workaround
    And I tap on personal info screen
    And I tap change photo button
    And I press Gallery button
    And I select picture
    And I press Confirm button
    And I tap on personal info screen
    Then I see changed user picture

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id325 @smoke
  Scenario Outline: Check contact personal info
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe up on dialog page
    Then I see <Contact> user name and email

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id328 @smoke
  Scenario Outline: ZClient change name
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    #workaround
    And I minimize the application
    And I restore the application
    #workaround
    And I tap on my name
    And I change <Name> to <NewName>
    And I swipe right to contact list
    Then I see contact list loaded with User name <NewName>
    When I tap on my name <NewName>
    Then I see my new name <NewName> and return old <Name>

    Examples: 
      | Login      | Password      | Name      | NewName     |
      | user1Email | user1Password | user1Name | NewTestName |
