Feature: Recall Message

  @C202326 @regression @rc
  Scenario Outline: Verify I can delete my message everywhere and I see others delete the message everywhere(1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    # Delete from otherview
    When I tap on conversation name <Contact1>
    And User <Contact1> send encrypted message "<Message>" via device <ContactDevice> to user Myself
    And I see the message "<Message>" in the conversation view
    And User <Contact1> delete the recent message everywhere from user Myself via device <ContactDevice>
    Then I do not see the message "<Message>" in the conversation view
    # C202328
    And I see the trashcan next to the name of <Contact1> in the conversation view
    # Delete from my view
    When User adds the following device: {"Myself": [{"name": "<MySecondDevice>"}]}
    And User Myself send encrypted message "<Message2>" via device <MySecondDevice> to user <Contact1>
    And I see the message "<Message2>" in the conversation view
    And User <Contact1> remember the recent message from user Myself via device <ContactDevice>
    And I long tap the Text message "<Message2>" in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see the message "<Message2>" in the conversation view
    And User <Contact1> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds
    And I do not see the trashcan next to the name of Myself in the conversation view

    Examples:
      | Name      | Contact1  | Message           | ContactDevice | MySecondDevice | Message2 |
      | user1Name | user2Name | DeleteTextMessage | Device2       | Device1        | Del2     |

  @C225997 @regression
  Scenario Outline: Verify the message deleted everywhere in local Wire database
    Given Device debug mode is supported
    Given Wire debug mode is enabled
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Text>" and send it by cursor Send button
    And I remember the state of the recent message from user <Contact> in the local database
    And I long tap the Text message "<Text>" in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I verify the remembered message has been deleted from the local database

    Examples:
      | Name      | Contact   | Text |
      | user1Name | user2Name | Hi   |

  @C202327 @regression @rc
  Scenario Outline: Verify I can delete my message everywhere and I see others delete the message everywhere(group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
   # Delete from otherview
    When I tap on conversation name <Group>
    And User <Contact1> send encrypted message "<Message>" via device <ContactDevice> to group conversation <Group>
    And I see the message "<Message>" in the conversation view
    And User <Contact1> delete the recent message everywhere from group conversation <Group> via device <ContactDevice>
    Then I do not see the message "<Message>" in the conversation view
    # C202329
    And I see the trashcan next to the name of <Contact1> in the conversation view
   # Delete from my view
    When User adds the following device: {"Myself": [{"name": "<MySecondDevice>"}]}
    And User Myself send encrypted message "<Message2>" via device <MySecondDevice> to group conversation <Group>
    And I see the message "<Message2>" in the conversation view
    And User <Contact1> remember the recent message from group conversation <Group> via device <ContactDevice>
    And I long tap the Text message "<Message2>" in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see the message "<Message2>" in the conversation view
    And User <Contact1> see the recent message from group conversation <Group> via device <ContactDevice> is changed in 15 seconds
    And I do not see the trashcan next to the name of Myself in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Group  | Message           | ContactDevice | MySecondDevice | Message2 |
      | user1Name | user2Name | user3Name | TGroup | DeleteTextMessage | Device2       | Device1        | Del2     |

  @C202332 @regression @rc
  Scenario Outline: Verify I can delete everywhere works for images
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    And I long tap Image container in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see any pictures in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | ContactDevice |
      | user1Name | user2Name | Device1       |

  @C202333 @regression @rc
  Scenario Outline: Verify delete everywhere works for giphy
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap on text input
    And I type the message "<Message>"
    And I tap Gif button from cursor toolbar
    And I select a random gif from the grid preview
    Then I see giphy preview page
    When I tap on the giphy Send button
    Then I see a picture in the conversation view
    And I see the picture in the conversation is animated
    When I long tap Image container in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see any pictures in the conversation view
    And I see the message "<Message> · via giphy.com" in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | Message | ContactDevice |
      | user1Name | user2Name | Yo      | Device1       |

  @C202334 @regression @rc
  Scenario Outline: Verify delete everywhere works for link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Link>" and send it by cursor Send button
    And I long tap Link Preview container in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Link Preview container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | Link                    | ContactDevice |
      | user1Name | user2Name | http://www.facebook.com | Device1       |

  @C202335 @regression @rc
  Scenario Outline: Verify delete everywhere works for Share location
    Given I am on Android with Google Location Service
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Share location button from cursor toolbar
    And I tap Send button on Share Location page
    And I long tap Share Location container in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Share Location container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | ContactDevice |
      | user1Name | user2Name | device1       |

  @C202336 @regression @rc
  Scenario Outline: Verify delete everywhere works for file sharing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap File button from cursor toolbar
    And I wait up to <UploadingTimeout> seconds until <FileSize> file with extension "<FileExtension>" is uploaded
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I long tap File Upload container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see File Upload container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | FileName  | FileExtension | FileSize | ContactDevice | UploadingTimeout |
      | user1Name | user2Name | qa_random | txt           | 1.00MB   | device1       | 20               |

  @C202337 @regression @rc
  Scenario Outline: Verify delete everywhere works for audio messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap audio recording Send button
    And I wait up to 30 seconds until audio message upload is completed
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I long tap Audio Message container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Audio Message container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | TapDuration | ContactDevice |
      | user1Name | user2Name | 5           | Device1       |

  @C202338 @regression @rc
  Scenario Outline: Verify delete everywhere works for video messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I push <FileSize> video file having name "<FileFullName>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Video message button from cursor toolbar
    Then I see Video Message container in the conversation view
    And I wait up to 30 seconds until video message upload is completed
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I long tap Video Message container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Video Message container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | FileSize | FileFullName     | ContactDevice |
      | user1Name | user2Name | 1.00MB   | random_video.mp4 | Device1       |

  @C202330 @regression @rc
  Scenario Outline: Verify deleting everywhere is synchronised across own devices when they are online (1:1 and group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User adds the following device: {"Myself": [{"name": "<Device>"}]}
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And User Myself send encrypted message "<Message>" via device <Device> to user <Contact1>
    Then I see the message "<Message>" in the conversation view
    When User Myself delete the recent message everywhere from user <Contact1> via device <Device>
    Then I do not see the message "<Message>" in the conversation view
    When I tap Back button from top toolbar
    And I tap on conversation name <GroupChatName>
    And User Myself send encrypted message "<Message>" via device <Device> to group conversation <GroupChatName>
    Then I see the message "<Message>" in the conversation view
    When User Myself delete the recent message everywhere from group conversation <GroupChatName> via device <Device>
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Message           | Device  | ContactDevice | GroupChatName |
      | user1Name | user2Name | user3Name | DeleteTextMessage | Device1 | Device2       | MyGroup       |

  @C206251 @regression
  Scenario Outline: Verify I do not see unread dot if a message was deleted from someone in a conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends 1 encrypted messages to user Myself
    And I tap on conversation name <Contact1>
    And I scroll to the bottom of conversation view
    And I navigate back from conversation
    And I remember unread messages indicator state for conversation <Contact1>
    And User <Contact1> delete the recent message everywhere from user Myself via device <ContactDevice>
    Then I see unread messages indicator state is not changed for conversation <Contact1>

    Examples:
      | Name      | Contact1  | ContactDevice |
      | user1Name | user2Name | Device1       |

  @C206252 @C226046 @regression
  Scenario Outline: (AN-4394) Verify I cannot delete message everywhere/like message when I was removed from group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And I type the message "<Message>" and send it by cursor Send button
    And <Contact1> removes Myself from group <GroupChatName>
    And I tap the Text message "<Message>" in the conversation view
    # C226046
    Then I do not see Like button in conversation view
    # C206252
    When I long tap the Text message "<Message>" in the conversation view
    Then I do not see Delete button on the message bottom menu

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName       | Message |
      | user1Name | user2Name | user3Name | RemoveFromGroupChat | YO      |

  @C206264 @regression @rc
  Scenario Outline: Verify delete everywhere works for YouTube
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # Youtube
    When I type the message "<YoutubeLink>" and send it by cursor Send button
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I long tap Youtube container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Youtube container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds
    # Soundcloud
    #When I type the message "<SoundCloudLink>" and send it by cursor Send button
    #And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    #And I long tap Soundcloud container in the conversation view
    #And I tap Delete for everyone button on the message bottom menu
    #And I tap Delete button on the alert
    #Then I do not see Soundcloud container in the conversation view
    #And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | YoutubeLink                                 | SoundCloudLink                                                      | ContactDevice |
      | user1Name | user2Name | https://www.youtube.com/watch?v=gIQS9uUVmgk | https://soundcloud.com/scottisbell/scott-isbell-tonight-feat-adessi | Device1       |

  @C206266 @regression @rc
  Scenario Outline: Verify I cannot delete for everyone/edit message received from other user
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given User <Contact> sends encrypted message "<Message>" to user Myself
    Given I tap on conversation name <Contact>
    When I long tap the Text message "<Message>" in the conversation view
    Then I see Delete button on the message bottom menu
    And I do not see Edit button on the message bottom menu
    And I tap Delete button on the message bottom menu
    And I do not see Delete for everyone button on the message bottom menu

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C206265 @C206274 @C226039 @regression
  Scenario Outline: Verify like/unlike and deleted messages/edit message doesn't unarchive the "archived conversation"
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Myself is connected to <Contact2>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact1> send encrypted message "<Message>" via device <ContactDevice> to user Myself
    Given I see Conversations list with conversations
    Given I swipe right on a <Contact1>
    Given I tap ARCHIVE button on Single conversation options menu
    Given I do not see Conversations list with name <Contact1>
    # C226039
    When User <Contact1> likes the recent message from user Myself via device <ContactDevice>
    Then I do not see Conversations list with name <Contact1>
    When User <Contact1> unlikes the recent message from user Myself via device <ContactDevice>
    Then I do not see Conversations list with name <Contact1>
    # C206274
    When User <Contact1> edits the recent message to "<NewMessage>" from user Myself via device <ContactDevice>
    Then I do not see Conversations list with name <Contact1>
    # C206265
    When User <Contact1> deletes the recent message everywhere from user Myself via device <ContactDevice>
    Then I do not see Conversations list with name <Contact1>

    Examples:
      | Name      | Contact1  | Contact2  | Message | ContactDevice | NewMessage |
      | user1Name | user2Name | user3Name | Yo      | Device1       | YoYo       |

  @C206278 @regression
  Scenario Outline: Verify delete message everywhere offline mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it by cursor Send button
    And I see the message "<Message>" in the conversation view
    And User <Contact1> remembers the recent message from user Myself via device <ContactDevice>
    And I enable Airplane mode on the device
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    And I do not see the message "<Message>" in the conversation view
    And I disable Airplane mode on the device
    And I do not see No Internet bar in <InternetTimeout> seconds
    Then User <Contact1> sees the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact1  | Message | ContactDevice | InternetTimeout |
      | user1Name | user2Name | YO      | Device1       | 15              |
