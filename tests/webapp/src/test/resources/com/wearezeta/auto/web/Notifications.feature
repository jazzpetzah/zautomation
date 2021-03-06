Feature: Notifications

  @C318637 @notifications @preferences @regression
  Scenario Outline: Sender name and a message are shown in notification when 'Show sender and message' item is selected in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I listen for notifications
    And I open conversation with <Contact2>
    And I open preferences by clicking the gear button
    And I open options in preferences
    Then I see notification setting is set to on
    When I close preferences
    Then Soundfile new_message did not start playing
    When I click next notification from <NotificationSender> with text <ExpectedMessage>
    And Contact <Contact1> sends message <ExpectedMessage> to user Myself
    Then I see text message <ExpectedMessage>
    And Soundfile new_message did start playing
    And I got 1 notification
    And I saw notification from <NotificationSender> with text <ExpectedMessage>
    And I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ExpectedMessage | NotificationSender |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DEFAULT         | user2Name          |

  @C395989 @notifications @preferences @regression
  Scenario Outline: No message content is written on notification when 'Show sender' item is selected in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I listen for notifications
    And I open conversation with <Contact2>
    And I open preferences by clicking the gear button
    And I open options in preferences
    And I set notification setting to sender
    Then I see notification setting is set to obfuscate-message
    When I close preferences
    Then Soundfile new_message did not start playing
    When I click next notification from <NotificationSender> with text <ExpectedMessage>
    And Contact <Contact1> sends message <OriginalMessage> to user Myself
    Then I see text message <OriginalMessage>
    And Soundfile new_message did start playing
    And I got 1 notification
    And I saw notification from <NotificationSender> with text <ExpectedMessage>
    And I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | OriginalMessage    | ExpectedMessage    | NotificationSender |
      | user1Email | user1Password | user1Name | user2Name | user3Name | MESSAGE_OBFUSCATED | Sent you a message | user2Name          |

  @C318638 @notifications @preferences @regression
  Scenario Outline: No sender name, profile image or message content is written on notification when choose 'Hide details' in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I listen for notifications
    And I open conversation with <Contact2>
    And I open preferences by clicking the gear button
    And I open options in preferences
    And I set notification setting to hide details
    Then I see notification setting is set to obfuscate
    When I close preferences
    Then Soundfile new_message did not start playing
    When I click next notification from <NotificationSender> with text <ExpectedMessage>
    And Contact <Contact1> sends message <OriginalMessage> to user Myself
    Then I see text message <OriginalMessage>
    And Soundfile new_message did start playing
    And I got 1 notification
    And I saw notification from <NotificationSender> with text <ExpectedMessage>
    And I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | OriginalMessage               | ExpectedMessage    | NotificationSender |
      | user1Email | user1Password | user1Name | user2Name | user3Name | MESSAGE_AND_SENDER_OBFUSCATED | Sent you a message | Someone            |

  @C318639 @notifications @preferences @regression
  Scenario Outline: No notification shown when selecting 'Off' in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I listen for notifications
    And I open conversation with <Contact2>
    And Soundfile new_message did not start playing
    And I click next notification from <Contact1> with text <OriginalMessage1>
    And Contact <Contact1> sends message <OriginalMessage1> to user Myself
    Then I see text message <OriginalMessage1>
    And Soundfile new_message did start playing
    And I got 1 notification
    When I open preferences by clicking the gear button
    And I open options in preferences
    And I set notification setting to off
    Then I see notification setting is set to none
    When I close preferences
    And I open conversation with <Contact2>
    Then Soundfile new_message did not start playing
    And Contact <Contact1> sends message <OriginalMessage2> to user Myself
    Then Soundfile new_message did start playing
    And I see unread dot in conversation <Contact1>
    When I open conversation with <Contact1>
    Then I see text message <OriginalMessage2>
    And I got 1 notification

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | OriginalMessage1 | OriginalMessage2 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DEFAULT          | OFF              |

  @C404413 @notifications @preferences @known @WEBAPP-3586
  Scenario Outline: Verify I can click notification while preferences are opened
    Given There are 2 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I listen for notifications
    Then I open conversation with <Contact1>
    And I open preferences by clicking the gear button
    And I open options in preferences
    Then I see notification setting is set to on
    And Soundfile new_message did not start playing
    Then I click next notification from <NotificationSender> with text <ExpectedMessage>
    When Contact <Contact1> sends message <ExpectedMessage> to user Myself
    Then I see text message <ExpectedMessage>
    Then Soundfile new_message did start playing
    And I got 1 notification
    Then I saw notification from <NotificationSender> with text <ExpectedMessage>
    Then I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | ExpectedMessage | NotificationSender |
      | user1Email | user1Password | user1Name | user2Name | DEFAULT         | user2Name          |

  @C404414 @notifications @ping @regression
  Scenario Outline: Verify I can click ping notification while other conversation is opened
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    Then I listen for notifications
    When I open conversation with <Contact2>
    Then Soundfile ping_from_them did not start playing
    And I click next notification from <Contact1> with text <ExpectedMessage>
    When User <Contact1> pinged in the conversation with Myself
    Then I see <PING> action in conversation
    And Soundfile ping_from_them did start playing
    And I got 1 notification
    And I saw notification from <Contact1> with text <ExpectedMessage>
    And I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ExpectedMessage | PING   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Pinged          | pinged |

  @C415141 @notifications @calling @staging
  Scenario Outline: Verify I can click calling notification while other conversation is opened
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> has unique username
    Given <Contact1> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    Then I listen for notifications
    When I open conversation with <Contact2>
    Then Soundfile ringing_from_them did not start playing in loop
    And I click next notification from <Contact1> with text <ExpectedMessage>
    When <Contact1> calls me
    And Soundfile ringing_from_them did start playing in loop
    And I got 1 notification
    And I saw notification from <Contact1> with text <ExpectedMessage>
    And I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ExpectedMessage | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Calling         | chrome      | 20      |

  @C262538 @notifications @ephemeral @staging
  Scenario Outline: Verify notification for ephemeral messages does not contain the message, sender's name or image
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I listen for notifications
    Then I open conversation with <Contact2>
    And I open preferences by clicking the gear button
    And I open options in preferences
    Then I see notification setting is set to on
    When I close preferences
    And Soundfile new_message did not start playing
    Then I click next notification from <NotificationSender> with text <ExpectedMessage>
    When User <Contact1> switches user Myself to ephemeral mode via device Device1 with <TimeLong> timeout
    And Contact <Contact1> sends message "<OriginalMessage>" via device Device1 to user Myself
    Then I see text message <OriginalMessage>
    And I see timer next to the last message
    And Soundfile new_message did start playing
    And I got 1 notification
# TODO check image in notification
    And I saw notification from <NotificationSender> with text <ExpectedMessage>
    And I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | OriginalMessage | ExpectedMessage    | NotificationSender | TimeLong   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DEFAULT         | Sent you a message | Someone            | 30 seconds |