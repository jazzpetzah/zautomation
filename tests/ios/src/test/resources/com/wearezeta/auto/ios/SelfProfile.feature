Feature: Self Profile

  @smoke @id344
  Scenario Outline: Change your profile picture
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on personal screen
    And I press Camera button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    And I return to personal page
    Then I see changed user picture <Picture>

    Examples:
      | Login      | Password      | Name      | Picture                      |
      | user1Email | user1Password | user1Name | userpicture_mobile_check.jpg |

  @regression @id1055
  Scenario Outline: Attempt to enter a name with 0 chars
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I attempt to input an empty name and tap the screen
    And I see error message asking for more characters

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  #ZIOS-2975
  @staging @id1056
  Scenario Outline: Attempt to enter a name with 1 char
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to enter <username> and press return
    And I see error message asking for more characters
    And I attempt to enter <username> and tap the screen
    And I see error message asking for more characters

    Examples: 
      | Login   | Password    | Name    | username |
      | aqaUser | aqaPassword | aqaUser | c        |
