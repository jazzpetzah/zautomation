Feature: E2EE

  @C3226 @rc @regression
  Scenario Outline: Verify you can receive encrypted and cannot receive non-encrypted messages in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact> sends encrypted message <EncryptedMessage> to user Myself
    And User <Contact> sends message <SimpleMessage> to user Myself
    And I tap on conversation name <Contact>
    Then I see message <SimpleMessage> 0 times in the conversation view
    And I see message <EncryptedMessage> 1 times in the conversation view

    Examples:
      | Name      | Contact   | EncryptedMessage | SimpleMessage |
      | user1Name | user2Name | EncryptedYo      | SimpleYo      |

  @C3227 @rc @regression
  Scenario Outline: Verify in latest version you only can receive encrypted images in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact> sends encrypted image <ImageName> to single user conversation Myself
    And User <Contact> sends image <ImageName> to single user conversation Myself
    And I tap on conversation name <Contact>
    Then I see 1 image in the conversation view

    Examples:
      | Name      | Contact   | ImageName   |
      | user1Name | user2Name | testing.jpg |

  @C3234 @rc @regression
  Scenario Outline: Verify you receive encrypted content in 1:1 conversation after switching online
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And User <Contact1> sends encrypted message <Message1> to user Myself
    Then I see the most recent conversation message is "<Message1>"
    When I enable Airplane mode on the device
    And I see No Internet bar in 15 seconds
    And User <Contact1> sends encrypted image <Picture> to single user conversation Myself
    Then I do not see any pictures in the conversation view
    When User <Contact1> sends encrypted message <Message2> to user Myself
    Then I see the most recent conversation message is "<Message1>"
    When I disable Airplane mode on the device
    And I do not see No Internet bar in 15 seconds
    And I scroll to the bottom of conversation view
    Then I see the most recent conversation message is "<Message2>"
    And I see a picture in the conversation view

    Examples:
      | Name      | Contact1  | Message1 | Message2 | Picture     |
      | user1Name | user2Name | Msg1     | Msg2     | testing.jpg |

  @C3235 @rc @regression
  Scenario Outline: Verify you can receive encrypted content in group conversation after switching online
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And User <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    Then I see the most recent conversation message is "<Message1>"
    When I enable Airplane mode on the device
    And User <Contact1> sends encrypted image <Picture> to group conversation <GroupChatName>
    Then I do not see any pictures in the conversation view
    When User <Contact2> sends encrypted message <Message2> to group conversation <GroupChatName>
    Then I see the most recent conversation message is "<Message1>"
    When I disable Airplane mode on the device
    And I do not see No Internet bar in 15 seconds
    And I scroll to the bottom of conversation view
    Then I see the most recent conversation message is "<Message2>"
    And I see a picture in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Message1 | Message2 | Picture     | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | Msg2     | testing.jpg | GroupConvo    |

  @C3241 @regression
  Scenario Outline: Verify you can receive encrypted and cannot receive non-encrypted messages in group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message <EncryptedMessage> to group conversation <GroupChatName>
    And User <Contact2> sends message <SimpleMessage> to group conversation <GroupChatName>
    And I tap on conversation name <GroupChatName>
    Then I see message <SimpleMessage> 0 times in the conversation view
    And I see message <EncryptedMessage> 1 times in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | EncryptedMessage | SimpleMessage | GroupChatName |
      | user1Name | user2Name | user3Name | EncryptedYo      | SimpleYo      | HybridGroup   |

  @C3242 @regression
  Scenario Outline: Verify you can only receive encrypted images in group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted image <ImageName> to group conversation <GroupChatName>
    And User <Contact1> sends image <ImageName> to group conversation <GroupChatName>
    And I tap on conversation name <GroupChatName>
    And I scroll to the bottom of conversation view
    Then I see 1 image in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | ImageName   | GroupChatName |
      | user1Name | user2Name | user3Name | testing.jpg | GroupConvo    |

  @C3229 @rc @regression
  Scenario Outline: Verify you can see device ids of the other conversation participant in participant details view inside a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    And User <Contact2> sends encrypted message <Message1> to group conversation <GroupChatName>
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact1> on Group info page
    And I switch tab to "Devices" in Group connected user details page
    Then I see 1 device is shown on Device list page
    And I verify all device ids of user <Contact1> are shown on Device list page
    #When I tap X button on Single connected user details page
    When I tap back button
    And I tap on contact <Contact2> on Group info page
    And I switch tab to "Devices" in Group connected user details page
    Then I see 1 device is shown on Device list page
    And I verify all device ids of user <Contact2> are shown on Device list page

    Examples:
      | Name      | Contact1  | Contact2  | Message1 | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | GroupConvo    |

  @C3228 @rc @regression
  Scenario Outline: Verify you can see device ids of the other conversation participant in 1:1 conversation details
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message <Message1> to user Myself
    And I tap on conversation name <Contact1>
    And I tap conversation name from top toolbar
    And I switch tab to "Devices" in Single connected user details page
    Then I see 1 device is shown on Device list page
    And I verify all device ids of user <Contact1> are shown on Device list page

    Examples:
      | Name      | Contact1  | Message1 |
      | user1Name | user2Name | Msg1     |

  @C3232 @regression
  Scenario Outline: CM-997 Verify the device id is not changed after relogin
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message <EncMessage> to user Myself
    And I tap on conversation name <Contact1>
    Then I see message <EncMessage> 1 times in the conversation view
    When I tap back button
    And I tap conversations list settings button
    And I select "Devices" settings menu item
    And I tap current device in devices settings menu
    Then I remember the device id shown in the device detail view
    When I tap back button 2 times
    When I select "Account" settings menu item
    And I select "Log out" settings menu item
    Then I confirm sign out
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    And I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    Then I see message <EncMessage> 1 times in the conversation view
    When I tap back button
    And I tap conversations list settings button
    And I select "Devices" settings menu item
    And I tap current device in devices settings menu
    Then I verify the remembered device id is shown in the device detail view

    Examples:
      | Name      | Contact1  | EncMessage |
      | user1Name | user2Name | Bla        |

  @C3236 @rc @regression
  Scenario Outline: Verify newly added people in a group conversation don't see a history
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to Myself,<Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message <EncMessage> to group conversation <GroupChatName>
    And User <Contact2> sends encrypted message <EncMessage> to group conversation <GroupChatName>
    And User <Contact3> sends encrypted message <EncMessage> to group conversation <GroupChatName>
    And I wait for 5 seconds
    And User <Contact1> adds user Myself to group chat <GroupChatName>
    And I tap on conversation name <GroupChatName>
    Then I see message <EncMessage> 0 times in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | EncMessage |
      | user1Name | user2Name | user3Name | user4Name | EncryptedGrp  | Bla        |

  @C3231 @rc @regression
  Scenario Outline: Verify the appropriate device is signed out if you remove it from settings
    Given There are 1 users where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    When User Myself removes all his registered OTR clients
    Then I see welcome screen
    And I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible

    Examples:
      | Name      |
      | user1Name |

  @C3514 @regression
  Scenario Outline: On first login on 2nd device there should be an explanation that user will not see previous messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    When User <Name> sends encrypted message <EncMessage> to user <Contact1>
    Then I sign in using my email
    And I see First Time overlay
    When I tap Got It button on First Time overlay
    Then I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    Then I see message <EncMessage> 0 times in the conversation view

    Examples:
      | Name      | Contact1  | EncMessage |
      | user1Name | user2Name | Bla        |

  @C3515 @C3237 @regression
  Scenario Outline: Verify green shield showed in other user profile when I verify all his devices
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message <Message1> to user Myself
    And I tap on conversation name <Contact1>
    And I tap conversation name from top toolbar
    And I switch tab to "Devices" in Single connected user details page
    Then I see 1 device is shown on Device list page
    And I remember state of 1st device on Device list page
    And I verify 1st device on Device list page
    And I see state of 1st device is changed on Device list page
    Then I see shield icon on Single connected user details page

    Examples:
      | Name      | Contact1  | Message1 |
      | user1Name | user2Name | Msg1     |

  @C3238 @regression @rc
  Scenario Outline: Verify you see an alert in verified 1:1 conversation when the other participants types something from non-verified device
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message "<Message1>" to user Myself
    And I tap on conversation name <Contact1>
    And I tap conversation name from top toolbar
    And I switch tab to "Devices" in Single connected user details page
    Then I see 1 device is shown on Device list page
    And I verify 1st device on Device list page
    When I tap back button
    Then I see a message informing me conversation is verified
    And User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    When User <Contact1> sends encrypted message "<Message1>" via device <ContactDevice> to user Myself
    Then I see a message informing me conversation is not verified caused by user <Contact1>

    Examples:
      | Name      | Contact1  | Message1 | ContactDevice |
      | user1Name | user2Name | Msg1     | Device2       |

  @C3240 @rc @regression
  Scenario Outline: Verify you get an alert if group conversation participant sends a message from non-verified device
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message "<Message1>" to group conversation <GroupChatName>
    When User <Contact2> sends encrypted message "<Message1>" to group conversation <GroupChatName>
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact1> on Group info page
    And I switch tab to "Devices" in Group connected user details page
    Then I see 1 device is shown on Device list page
    And I verify 1st device on Device list page
    And I tap back button
    And I tap on contact <Contact2> on Group info page
    And I switch tab to "Devices" in Group connected user details page
    Then I see 1 device is shown on Device list page
    And I verify 1st device on Device list page
    And I tap back button
    And I tap back button
    Then I see a message informing me conversation is verified
    And User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    When User <Contact1> sends encrypted message "<Message1>" via device <ContactDevice> to group conversation <GroupChatName>
    Then I see a message informing me conversation is not verified caused by user <Contact1>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message1 | ContactDevice |
      | user1Name | user2Name | user3Name | EncryptedGrp  | Msg1     | Device2       |

  @C12083 @regression
  Scenario Outline: When I'm entering a verified conversation, a green shield will appear at the bottom right
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message "<Message1>" to user Myself
    And I tap on conversation name <Contact1>
    And I remember verified conversation shield state
    And I tap conversation name from top toolbar
    And I switch tab to "Devices" in Single connected user details page
    Then I see 1 device is shown on Device list page
    And I verify 1st device on Device list page
    When I tap back button
    Then I see a message informing me conversation is verified
    And I see verified conversation shield state has changed

    Examples:
      | Name      | Contact1  | Message1 |
      | user1Name | user2Name | Msg1     |

  @C3516 @regression
  Scenario Outline: User should appear in verified list in group conversations details when all of his fingerprints are verified
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Users add the following devices: {"<Contact1>": [{"name": "Device1"}, {"name": "Device2"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact2> sends encrypted message "<Message1>" to group conversation <GroupChatName>
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact1> on Group info page
    And I switch tab to "Devices" in Group connected user details page
    Then I see 2 devices is shown on Device list page
    When I verify 1st device on Device list page
    And I switch tab to "Devices" in Group connected user details page
    And I verify 2nd device on Device list page
    And I tap back button
    Then I see the verified participant avatar for <Contact1> on Group info page
    When I tap on contact <Contact2> on Group info page
    And I switch tab to "Devices" in Group connected user details page
    When I verify 1st device on Device list page
    And I tap back button
    Then I see the verified participant avatars for <Contact1>,<Contact2> on Group info page
    When I tap back button
    Then I see a message informing me conversation is verified

    Examples:
      | Name      | Contact1  | Contact2  | Message1 | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | GroupConvo    |

  @C3239 @regression @rc
  Scenario Outline: Verify I see system message after I verified all participants' devices
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    And User <Contact2> sends encrypted message <Message1> to group conversation <GroupChatName>
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact1> on Group info page
    And I switch tab to "Devices" in Group connected user details page
    Then I see 1 device is shown on Device list page
    And I verify 1st device on Device list page
    #When I tap X button on Single connected user details page
    When I tap back button
    And I tap on contact <Contact2> on Group info page
    And I switch tab to "Devices" in Group connected user details page
    Then I see 1 device is shown on Device list page
    And I verify 1st device on Device list page
    #When I tap X button on Single connected user details page
    When I tap back button
    And I tap back button
    Then I see a message informing me conversation is verified

    Examples:
      | Name      | Contact1  | Contact2  | Message1 | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | GroupConvo    |

  @C12082 @regression
  Scenario Outline: First time when group conversation is degraded - I can ignore takeover screen and send message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    And User <Contact2> sends encrypted message <Message1> to group conversation <GroupChatName>
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact1> on Group info page
    And I switch tab to "Devices" in Group connected user details page
    And I verify 1st device on Device list page
    And I tap Back button
    And I tap on contact <Contact2> on Group info page
    And I switch tab to "Devices" in Group connected user details page
    And I verify 1st device on Device list page
    And I tap Back button 2 times
    Then I see a message informing me conversation is verified
    When User adds the following device: {"<Contact1>": [{"name": "Device1"}]}
    And I tap on text input
    And I type the message "<Message2>" and send it by cursor Send button without hiding keyboard
    Then I see E2EE confirm overlay page from user "<Contact1>"
    When I tap SEND ANYWAY button on E2EE confirm overlay page
    Then I do not see E2EE confirm overlay page
    And I see message <Message2> 1 time in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Message1 | Message2 | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | Msg2     | GroupConvo    |

  @C3513 @regression
  Scenario Outline: If user uses only old Wire builds which don't support E2EE I should see system message inside his profile
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I tap on conversation name <Contact>
    When I tap conversation name from top toolbar
    And I switch tab to "Devices" in Single connected user details page
    Then I see no encrypted device text for user <Contact> in header of device detail page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C3512 @regression
  Scenario Outline: After login by phone on not 1st device I have to be asked for email login
    Given There is 1 user where <Name> is me
    Given User adds the following device: {"Myself": [{"name": "<Device>"}]}
    Given I sign in using my phone number
    Then I see forced email login page
    And I have entered login <Email>
    And I have entered password <Password>
    And I tap Log in button
    And I accept First Time overlay as soon as it is visible
    Then I see Conversations list with no conversations

    Examples:
      | Name      | Email      | Password      | Device  |
      | user1Name | user1Email | user1Password | device1 |

  @C12081 @regression
  Scenario Outline: When 1:1 conversation was degraded - I can ignore takeover screen and send message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message "<Message1>" to user Myself
    And I tap on conversation name <Contact1>
    And I tap conversation name from top toolbar
    And I switch tab to "Devices" in Single connected user details page
    Then I see 1 device is shown on Device list page
    And I verify 1st device on Device list page
    When I tap back button
    Then I see a message informing me conversation is verified
    And User adds the following device: {"<Contact1>": [{"name": "<Device>"}]}
    And I tap on text input
    And I type the message "<Message2>"
    And I tap Send button from cursor toolbar
    Then I see E2EE confirm overlay page from user "<Contact1>"
    When I tap SEND ANYWAY button on E2EE confirm overlay page
    Then I do not see E2EE confirm overlay page
    And I see the message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact1  | Device  | Message1 | Message2        |
      | user1Name | user2Name | device2 | Msg1     | MsgToSendAnyway |

  @C12065 @regression
  Scenario Outline: When 1:1 conversation was degraded - I can manage new device to verified and resend message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends encrypted message "<Message1>" to user Myself
    And I tap on conversation name <Contact1>
    And I tap conversation name from top toolbar
    And I switch tab to "Devices" in Single connected user details page
    Then I see 1 device is shown on Device list page
    And I verify 1st device on Device list page
    When I tap back button
    Then I see a message informing me conversation is verified
    And User adds the following device: {"<Contact1>": [{"name": "<Device>"}]}
    And I tap on text input
    And I type the message "<Message2>"
    And I tap Send button from cursor toolbar
    When I see E2EE confirm overlay page from user "<Contact1>"
    And I tap SHOW DEVICE button on E2EE confirm overlay page
    Then I do not see E2EE confirm overlay page
    # Workaround : tap on devices tab
    When I switch tab to "Devices" in Single connected user details page
    #TODO: detect new device and verify it instead of trying to verify each device
    And I verify 1st device on Device list page
    And I verify 2nd device on Device list page
    When I tap back button
    Then I see a message informing me conversation is verified

    Examples:
      | Name      | Contact1  | Device  | Message1 | Message2    |
      | user1Name | user2Name | device2 | Msg1     | MsgToResend |

  @C200108 @regression
  Scenario Outline: Verify new device notification indicator in conversation list cogweel symbol
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I remember the state of new device indicator on settings button
    And User adds the following device: {"Myself": [{"name": "<Device>"}]}
    Then I verify the state of new device indicator is changed

    Examples:
      | Name      | Device  |
      | user1Name | Device1 |
