Feature: Sign In

  @smoke @id1788
  Scenario Outline: Sign in to ZClient
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    Given I see Sign In page
    When I enter email <Email>
    And I enter password <Password>
    And I press Sign In button
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    Then I see my name on top of Contact list

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
