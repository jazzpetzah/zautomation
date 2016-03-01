Feature: Registration

  @C2761 @regression @id1392
  Scenario Outline: Automatic email verification [PORTRAIT]
    Given I see sign in screen
    When I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I start activation email monitoring
    And I click Create Account Button
    And I accept terms of service
    And I see confirmation page
    And I verify registration address
    And I press Choose Own Picture button
    And I press Choose Photo button
    And I choose a picture from camera roll
    Then I see conversations list

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |