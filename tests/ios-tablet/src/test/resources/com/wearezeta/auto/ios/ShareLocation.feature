Feature: Share Location

  @C165160 @C165167 @staging
  Scenario Outline: Receive and share location
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>, <Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email or phone number
    Given User <Contact1> shares the default location to user Myself via device <DeviceName>
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I see Share Location container in the conversation view
    And I see the default received Share Location address in the conversation view
    When I navigate back to conversations list
    And I tap on group chat with name <GroupChatName>
    And I tap Share Location button from input tools
    # Small delay waiting location detection animation to finish
    And I wait for 5 seconds
    And I tap Send location button from map view
    Then I see Share Location container in the conversation view
    And I see the default sent Share Location address in the conversation view
    When I tap on location map in conversation view
    Then I see map application is opened

    Examples:
      | Name      | Contact1  | DeviceName | Contact2  | GroupChatName |
      | user1Name | user2Name | device1    | user3Name | ShareAddress  |