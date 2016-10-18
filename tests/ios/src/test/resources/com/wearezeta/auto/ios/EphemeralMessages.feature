Feature: Ephemeral Messages

  @C259591 @regression @fastLogin
  Scenario Outline: Verify ephemeral messages don't leave a trace in the database
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    Given I type the default message and send it
    Given I see 1 default message in the conversation view
    When I remember the recent message from user Myself in the local database
    And I wait for <Timeout> seconds
    Then I see 0 default messages in the conversation view
    And I verify the remembered message has been changed in the local database
    When User <Contact> switches user Myself to ephemeral mode with <Timeout> seconds timeout
    And User <Contact> sends 1 encrypted message to user Myself
    # Wait for the message to be delivered
    And I wait for 3 seconds
    And I see 1 default message in the conversation view
    And I remember the state of the recent message from user <Contact> in the local database
    And I wait for <Timeout> seconds
    Then I see 0 default messages in the conversation view
    And I verify the remembered message has been deleted from the local database

    Examples:
      | Name      | Contact   | DeviceName    | Timeout |
      | user1Name | user2Name | ContactDevice | 15      |

  @C259589 @regression @fastLogin
  Scenario Outline: Verify ephemeral messages are disabled in a group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    Then I do not see Hourglass button in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      |

  @C259584 @C259585 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral message - no online receiver (negative case)
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timeout> seconds
    And I type the default message and send it
    And I see 1 default message in the conversation view
    Then I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    When I remember the recent message from user Myself in the local database
    And I wait for <Timeout> seconds
    Then I see 1 message in the conversation view
    And I verify the remembered message has been changed in the local database

    Examples:
      | Name      | Contact   | Timeout | EphemeralTimeLabel |
      | user1Name | user2Name | 15      | seconds            |

  @C259586 @staging @fastLogin
  Scenario Outline: Verify switching on/off ephemeral message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    Then I see Ephemeral input placeholder text
    And I see Time Indicator button in conversation view
    When I type the default message and send it
    And I tap Time Indicator button in conversation view
    And I set ephemeral messages expiration timer to Off
    Then I see Standard input placeholder text

    Examples:
      | Name      | Contact   | Timer |
      | user1Name | user2Name | 15    |

  @C259587 @staging @fastLogin
  Scenario Outline: Verify ephemeral message is not sent to other own device
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When User Myself remembers the recent message from user <Contact> via device <DeviceName>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timeout> seconds
    And I type the default message and send it
    And I see 1 default message in the conversation view
    Then User Myself sees the recent message from user <Contact> via device <DeviceName> is not changed in 5 seconds

    Examples:
      | Name      | Contact   | DeviceName     | Timeout |
      | user1Name | user2Name | MySecondDevice | 15      |
