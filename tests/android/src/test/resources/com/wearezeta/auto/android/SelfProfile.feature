Feature: Self Profile

  @id205 @smoke
  Scenario Outline: Change user picture
    Given I have 1 users and 0 contacts for 1 users
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
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  @id325 @smoke
  Scenario Outline: Check contact personal info
    Given I have 1 users and 1 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe up on dialog page
    Then I see <Contact> user name and email

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @id328 @smoke
  Scenario Outline: ZClient change name
    Given I have 1 users and 0 contacts for 1 users
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
      | Login   | Password    | Name    | NewName     |
      | aqaUser | aqaPassword | aqaUser | NewTestName |
