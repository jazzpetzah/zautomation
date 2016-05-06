Feature: Delete Message

  @C111638 @staging @C111637
  Scenario Outline: Verify deleting own text message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message1>" and send it
    And I type the message "<Message2>" and send it
    When I long tap the Text message "<Message1>" in the conversation view
    Then I see Copy button on the action mode bar
    And I see Delete button on the action mode bar
    When I tap the Text message "<Message2>" in the conversation view
    Then I do not see Copy button on the action mode bar
    When I tap Delete button on the action mode bar
    And I see alert message containing "<AlertText>" in the title
    And I tap Delete button on the alert
    Then I do not see the message "<Message1>" in the conversation view
    And I do not see the message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact   | Message1 | Message2 | AlertText  |
      | user1Name | user2Name | Yo1      | Yo2      | 2 messages |

  @C111644 @staging
  Scenario Outline: Verify deleting is synchronised across own devices when they are online
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User Myself adds new device <Device>
    Given User <Contact1> adds new device <ContactDevice>
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And User Myself send encrypted message "<Message>" via device <Device> to user <Contact1>
    Then I see the message "<Message>" in the conversation view
    When User Myself delete the recent message from user <Contact1> via device <Device>
    Then I do not see the message "<Message>" in the conversation view
    When I tap back button in upper toolbar
    And I tap on contact name <GroupChatName>
    And User Myself send encrypted message "<Message>" via device <Device> to group conversation <GroupChatName>
    Then I see the message "<Message>" in the conversation view
    When User Myself delete the recent message from group conversation <GroupChatName> via device <Device>
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Message           | Device  | ContactDevice | GroupChatName |
      | user1Name | user2Name | user3Name | DeleteTextMessage | Device1 | Device2       | MyGroup       |

  @C111641 @staging
  Scenario Outline: Verify deleting the media file (sound could, youtube)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<YoutubeLink>" and send it
    And I type the message "<SoundcloudLink>" and send it
    And I hide keyboard
    When I scroll down the conversation view
    And I long tap Youtube container in the conversation view
    And I scroll up the conversation view
    And I tap Soundcloud container in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see the message "<YoutubeLink>" in the conversation view
    And I do not see Youtube container in the conversation view
    And I do not see the message "<SoundcloudLink>" in the conversation view
    And I do not see Soundcloud container in the conversation view

    Examples:
      | Name      | Contact   | YoutubeLink                                 | SoundcloudLink                                                      |
      | user1Name | user2Name | https://www.youtube.com/watch?v=gIQS9uUVmgk | https://soundcloud.com/scottisbell/scott-isbell-tonight-feat-adessi |

  @C111639 @staging
  Scenario Outline: Verify deleting received text message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And User <Contact> send encrypted message "<Message>" to user Myself
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message           |
      | user1Name | user2Name | DeleteTextMessage |

  @C111643 @staging
  Scenario Outline: Verfiy deleting ping
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap Ping button from cursor toolbar
    And User <Contact> securely pings conversation Myself
    And I see Ping message "<Message2>" in the conversation view
    And I long tap the Ping message "<Message1>" in the conversation view
    And I tap the Ping message "<Message2>" in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see Ping message "<Message1>" in the conversation view
    And I do not see Ping message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact   | Message1       | CallBackend | Message2         |
      | user1Name | user2Name | You pinged     | autocall    | user2Name pinged |