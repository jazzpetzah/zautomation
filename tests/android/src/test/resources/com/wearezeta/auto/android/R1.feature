Feature: Registration1

  @C6671 @regression @rc @torun
  Scenario Outline: Register new user by phone and set profile picture using camera 11
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to choose my own picture
    And I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |

  @C6671 @regression @rc @torun
  Scenario Outline: Register new user by phone and set profile picture using camera 12
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to choose my own picture
    And I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |

  @C6671 @regression @rc @torun
  Scenario Outline: Register new user by phone and set profile picture using camera 13
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to choose my own picture
    And I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |

  @C6671 @regression @rc @torun
  Scenario Outline: Register new user by phone and set profile picture using camera 14
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to choose my own picture
    And I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |

  @C6671 @regression @rc @torun
  Scenario Outline: Register new user by phone and set profile picture using camera 15
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to choose my own picture
    And I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |
