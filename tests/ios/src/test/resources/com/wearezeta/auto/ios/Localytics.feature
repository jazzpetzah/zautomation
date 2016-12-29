Feature: Localytics

  @C375780 @staging @enableLocalyticsLogs @torun
  Scenario Outline: Verify key tracking events
    Given There are 2 users
    Given I prepare <FileName> to be uploaded as a video message
    Given I see sign in screen
    Given I enter phone number for <Name>
    Given I enter activation code
    Given I accept terms of service
    Given I input name <Name> and commit it
    Given I accept alert
    Given I tap Keep This One button
    # Wait for sync
    Given I wait for 3 seconds
    Given I accept alert if visible
    Given I tap Share Contacts button on Share Contacts overlay
    Given User <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User Myself sets the unique username
    Given I accept alert if visible
    Given I dismiss settings warning if visible
    Given I see conversation <Contact1> in conversations list
    Then I see "registration.succeeded" event is sent to Localytics at least 1 time
    When I tap on contact name <Contact1>
    And I type the default message and send it
    Then I see "media.completed_media_action" event with {"action": "text", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 1 time
    When I tap Audio Call button
    And I tap Leave button on Calling overlay
    Then I see "media.completed_media_action" event with {"action": "audio_call", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 1 time
    And I see "calling.initiated_call" event is sent to Localytics at least 1 time
    When I tap Video Call button
    And I tap Leave button on Calling overlay
    Then I see "media.completed_media_action" event with {"action": "video_call", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 1 time
    And I see "calling.initiated_video_call" event is sent to Localytics at least 1 time
    When I tap Ping button from input tools
    And I see "YOU PINGED" system message in the conversation view
    Then I see "media.completed_media_action" event with {"action": "ping", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 1 time
    When I tap Sketch button from input tools
    And I draw a random sketch
    And I tap Send button on Sketch page
    And I wait for 3 seconds
    Then I see "media.completed_media_action" event with {"action": "photo", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 1 time
    And I see "media.sent_picture" event with {"source": "sketch"} attribute is sent to Localytics at least 1 time
    When I type the "hello" message
    And I tap GIF button from input tools
    And I wait for 10 seconds
    And I select the first item from Giphy grid
    And I tap Send button on Giphy preview page
    And I wait for 3 seconds
    Then I see "media.completed_media_action" event with {"action": "photo", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 2 times
    And I see "media.sent_picture" event with {"source": "giphy"} attribute is sent to Localytics at least 1 time
    When I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    And I tap Confirm button on Picture preview page
    And I wait for 3 seconds
    Then I see "media.completed_media_action" event with {"action": "photo", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 3 times
    And I see "media.sent_picture" event with {"source": "gallery"} attribute is sent to Localytics at least 1 time
    When I long tap Audio Message button from input tools
    And I tap Send record control button
    And I see audio message container in the conversation view
    Then I see "media.completed_media_action" event with {"action": "file", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 1 time
    And I see "media.sent_audio_message" event is sent to Localytics at least 1 time
    When I tap Video Message button from input tools
    And I see video message container in the conversation view
    Then I see "media.completed_media_action" event with {"action": "file", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 2 times
    And I see "media.sent_video_message" event is sent to Localytics at least 1 time
    When I tap File Transfer button from input tools
    And I wait for 5 seconds
    And I tap file transfer menu item <ItemName>
    And I see file transfer placeholder
    Then I see "media.completed_media_action" event with {"action": "file", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 3 times
    When I tap Share Location button from input tools
    And I accept alert if visible
    And I wait for 5 seconds
    And I tap Send location button from map view
    And I see location map container in the conversation view
    Then I see "media.completed_media_action" event with {"action": "location", "conversation_type": "one_to_one", "with_bot": "false"} attributes is sent to Localytics at least 1 time
    When I navigate back to conversations list
    And I tap on contact name <GroupChatName>
    And I type the default message and send it
    Then I see "media.completed_media_action" event with {"action": "text", "conversation_type": "group", "with_bot": "false"} attributes is sent to Localytics at least 1 time

    Examples:
      | Contact1  | Contact2  | Name      | GroupChatName | FileName    | ItemName                   |
      | user1Name | user2Name | user3Name | Group         | testing.mp4 | FTRANSFER_MENU_DEFAULT_PNG |

