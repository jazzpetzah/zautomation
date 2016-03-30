Feature: Registration

  @C2768 @rc @regression @id1392 @useSpecialEmail
  Scenario Outline: Automatic email verification [LANDSCAPE]
    Given I rotate UI to landscape
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
    And I tap Share Contacts button on Share Contacts overlay
    Then I see People picker page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |