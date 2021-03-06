Feature: Recall Message

  @C202306 @rc @regression @fastLogin
  Scenario Outline: Verify I can delete my message everywhere (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"<Contact>": [{"name": "<HisDevice>"}], "Myself": [{"name": "<MySecondDevice>"}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the conversation view
    When I remember the state of the recent message from user Myself in the local database
    And User <Contact> remembers the recent message from user Myself via device <HisDevice>
    And User Myself remembers the recent message from user <Contact> via device <MySecondDevice>
    And I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for Everyone item from Delete menu
    Then I see 0 default messages in the conversation view
    And I verify the remembered message has been deleted from the local database
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in <Wait1> seconds
    And User Myself sees the recent message from user <Contact> via device <MySecondDevice> is changed in <Wait2> seconds
    When User Myself send 1 message using device <MySecondDevice> to user <Contact>
    # Wait until the message is synchronized
    And I wait for 5 seconds
    And I see 1 default message in the conversation view
    And User <Contact> remembers the recent message from user Myself via device <HisDevice>
    And User Myself remembers the recent message from user <Contact> via device <MySecondDevice>
    And User Myself delete the recent message everywhere from user <Contact> via device <MySecondDevice>
    Then I see 0 default messages in the conversation view
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in <Wait1> seconds
    And User Myself sees the recent message from user <Contact> via device <MySecondDevice> is changed in <Wait2> seconds

    Examples:
      | Name      | Contact   | HisDevice | MySecondDevice | Wait1 | Wait2 |
      | user1Name | user2Name | device1   | device2        | 15    | 3     |

  @C202318 @rc @regression @fastLogin
  Scenario Outline: Verify delete everywhere works for file sharing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"<Contact>": [{"name": "<HisDevice>"}], "Myself": [{"name": "<MySecondDevice>"}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap File Transfer button from input tools
    # Wait for animation
    And I wait for 5 seconds
    And I tap file transfer menu item <FileName>
    # Wait to be ready uploading for slower jenkins slaves
    And I wait for 10 seconds
    And User <Contact> remembers the recent message from user Myself via device <HisDevice>
    And User Myself remembers the recent message from user <Contact> via device <MySecondDevice>
    And I long tap on file transfer placeholder in conversation view
    And I tap on Delete badge item
    And I select Delete for Everyone item from Delete menu
    Then I do not see file transfer placeholder
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in 15 seconds
    And User Myself sees the recent message from user <Contact> via device <MySecondDevice> is changed in 3 seconds

    Examples:
      | Name      | Contact   | HisDevice | MySecondDevice | FileName                   |
      | user1Name | user2Name | device1   | device2        | FTRANSFER_MENU_DEFAULT_PNG |

  @C202314 @rc @regression @fastLogin
  Scenario Outline: Verify delete everywhere works for images
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"<Contact>": [{"name": "<HisDevice>"}], "Myself": [{"name": "<MySecondDevice>"}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    And I tap Confirm button on Picture preview page
    # Wait until the picture is loaded
    And I wait for 5 seconds
    And I see 1 photo in the conversation view
    And User <Contact> remembers the recent message from user Myself via device <HisDevice>
    And User Myself remembers the recent message from user <Contact> via device <MySecondDevice>
    And I long tap on image in conversation view
    And I tap on Delete badge item
    And I select Delete for Everyone item from Delete menu
    Then I see 0 photos in the conversation view
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in 15 seconds
    # FIXME: Commeneted out due to a bug in SE actors lib
    # And User Myself sees the recent message from user <Contact> via device <MySecondDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | HisDevice | MySecondDevice |
      | user1Name | user2Name | device1   | device2        |

  @C202307 @regression @fastLogin
  Scenario Outline: Verify I can delete my message everywhere (group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given Users add the following devices: {"<Contact1>": [{"name": "<Contact1Device>"}], "<Contact2>": [{"name": "<Contact2Device>"}], "Myself": [{"name": "<MySecondDevice>"}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Group>
    And I type the default message and send it
    Then I see 1 default message in the conversation view
    When User <Contact1> remembers the recent message from group conversation <Group> via device <Contact1Device>
    And User <Contact2> remembers the recent message from group conversation <Group> via device <Contact2Device>
    And User Myself remembers the recent message from group conversation <Group> via device <MySecondDevice>
    And I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for Everyone item from Delete menu
    Then I see 0 default messages in the conversation view
    And User <Contact1> sees the recent message from group conversation <Group> via device <Contact1Device> is changed in <Wait1> seconds
    And User <Contact2> sees the recent message from group conversation <Group> via device <Contact2Device> is changed in <Wait2> seconds
    And User Myself sees the recent message from group conversation <Group> via device <MySecondDevice> is changed in <Wait2> seconds
    When User Myself send 1 message using device <MySecondDevice> to group conversation <Group>
    # Wait until the message is delivered
    And I wait for 5 seconds
    Then I see 1 default message in the conversation view
    When User <Contact1> remembers the recent message from group conversation <Group> via device <Contact1Device>
    And User <Contact2> remembers the recent message from group conversation <Group> via device <Contact2Device>
    And User Myself remembers the recent message from group conversation <Group> via device <MySecondDevice>
    When User Myself deletes the recent message everywhere from group conversation <Group> via device <MySecondDevice>
    Then I see 0 default messages in the conversation view
    And User <Contact1> sees the recent message from group conversation <Group> via device <Contact1Device> is changed in <Wait1> seconds
    And User <Contact2> sees the recent message from group conversation <Group> via device <Contact2Device> is changed in <Wait2> seconds
    And User Myself sees the recent message from group conversation <Group> via device <MySecondDevice> is changed in <Wait2> seconds

    Examples:
      | Name      | Contact1  | Contact2  | Contact1Device | Contact2Device | Group       | MySecondDevice | Wait1 | Wait2 |
      | user1Name | user2Name | user3Name | device1        | device2        | RecallGroup | device3        | 15    | 3     |

  @C202320 @regression @fastLogin
  Scenario Outline: Verify delete everywhere works for video messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"<Contact>": [{"name": "<HisDevice>"}], "Myself": [{"name": "<MySecondDevice>"}]}
    Given I prepare <FileName> to be uploaded as a video message
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Message button from input tools
    Then I see video message container in the conversation view
    # Wait until video preview is fully loaded
    And I wait for 5 seconds
    When User <Contact> remembers the recent message from user Myself via device <HisDevice>
    And User Myself remembers the recent message from user <Contact> via device <MySecondDevice>
    And I long tap on video message in conversation view
    And I tap on Delete badge item
    And I select Delete for Everyone item from Delete menu
    Then I do not see video message container in the conversation view
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in 15 seconds
    And User Myself sees the recent message from user <Contact> via device <MySecondDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | HisDevice | MySecondDevice | FileName    |
      | user1Name | user2Name | device1   | device2        | testing.mp4 |

  @C202308 @regression @fastLogin
  Scenario Outline: Verify I see status message if other user deletes his message everywhere (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<Contact1Device>"}]}
    Given I sign in using my email or phone number
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I see 1 default message in the conversation view
    When User <Contact1> deletes the recent message everywhere from user <Name> via device <Contact1Device>
    Then I see 0 default messages in the conversation view

    Examples:
      | Name      | Contact1  | Contact1Device |
      | user1Name | user2Name | device1        |

  @C202309 @rc @regression @fastLogin
  Scenario Outline: Verify I see status message if other user deletes his message everywhere (group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given User adds the following device: {"<Contact1>": [{"name": "<Contact1Device>"}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 default message to conversation <Group>
    When I tap on contact name <Group>
    Then I see 1 default message in the conversation view
    When User <Contact1> deletes the recent message everywhere from group conversation <Group> via device <Contact1Device>
    Then I see 0 default messages in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Contact1Device | Group       |
      | user1Name | user2Name | user3Name | device1        | RecallGroup |

  @C202341 @rc @regression @fastLogin
  Scenario Outline: Verify delete everywhere works for Soundcloud, YouTube, Vimeo
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"<Contact>": [{"name": "<HisDevice>"}], "Myself": [{"name": "<MySecondDevice>"}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<YouTubeLink>" message and send it
    Then I see media container in the conversation view
    And User <Contact> remembers the recent message from user Myself via device <HisDevice>
    And User Myself remembers the recent message from user <Contact> via device <MySecondDevice>
    # This is to hide the keyboard
    When I navigate back to conversations list
    And I tap on contact name <Contact>
    And I long tap on media container in conversation view
    And I tap on Delete badge item
    And I select Delete for Everyone item from Delete menu
    Then I do not see media container in the conversation view
    And I do not see the conversation view contains message <YouTubeLink>
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in <Wait1> seconds
    And User Myself sees the recent message from user <Contact> via device <MySecondDevice> is changed in <Wait2> seconds
    When User Myself sends 1 "<SoundCloudLink>" message to conversation <Contact>
    # Wait for sync
    And I wait for 5 seconds
    Then I see media container in the conversation view
    And User <Contact> remembers the recent message from user Myself via device <HisDevice>
    And User Myself remembers the recent message from user <Contact> via device <MySecondDevice>
    When User Myself deletes the recent message everywhere from user <Contact> via device <MySecondDevice>
    # Wait for sync
    And I wait for 3 seconds
    Then I do not see media container in the conversation view
    And I do not see the conversation view contains message <SoundCloudLink>
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in <Wait1> seconds
    And User Myself sees the recent message from user <Contact> via device <MySecondDevice> is changed in <Wait2> seconds
    When User <Contact> sends 1 "<VimeoLink>" message to conversation Myself
    # Wait for the preview to be generated
    And I wait for 5 seconds
    Then I see media container in the conversation view
    And User <Contact> remembers the recent message from user Myself via device <HisDevice>
    And User Myself remembers the recent message from user <Contact> via device <MySecondDevice>
    When User <Contact> deletes the recent message everywhere from user <Name> via device <HisDevice>
    Then I do not see media container in the conversation view
    And I do not see the conversation view contains message <VimeoLink>
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in <Wait1> seconds
    And User Myself sees the recent message from user <Contact> via device <MySecondDevice> is changed in <Wait2> seconds

    Examples:
      | Name      | Contact   | YouTubeLink                                | SoundCloudLink                                   | VimeoLink                   | HisDevice | MySecondDevice | Wait1 | Wait2 |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU | https://soundcloud.com/sodab/256-ra-robag-wruhme | https://vimeo.com/129426512 | device1   | device2        | 15    | 3     |

  @C202315 @regression @fastLogin
  Scenario Outline: Verify delete everywhere works for giphy
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<HisDevice>"}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I select the first item from Giphy grid
    And I tap Send button on Giphy preview page
    # Wait for sync
    And I wait for 3 seconds
    Then I see 1 photo in the conversation view
    And User <Contact> remembers the recent message from user Myself via device <HisDevice>
    # Wait for sync
    When I wait for 3 seconds
    And I long tap on image in conversation view
    And I tap on Delete badge item
    And I select Delete for Everyone item from Delete menu
    Then I see 0 photos in the conversation view
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | HisDevice | GiphyTag |
      | user1Name | user2Name | device1   | cool     |

  @C202316 @regression @fastLogin
  Scenario Outline: Verify delete everywhere works for link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<HisDevice>"}]}
    Given I sign in using my email or phone number
    Given User Myself sends 1 "Try this app <Link>" message to conversation <Contact>
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    And User <Contact> remembers the recent message from user Myself via device <HisDevice>
    When I long tap on link preview in conversation view
    And I tap on Delete badge item
    And I select Delete for Everyone item from Delete menu
    Then I do not see link preview container in the conversation view
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | HisDevice | Link                  |
      | user1Name | user2Name | device1   | https://www.wire.com/ |

  @C202317 @regression @fastLogin
  Scenario Outline: Verify delete everywhere works for location sharing
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> shares the default location to user Myself via device <DeviceName>
    Given I see conversations list
    When I tap on contact name <Contact>
    # wait for sync
    And I wait for 5 seconds
    Then I see location map container in the conversation view
    And User <Contact> remembers the recent message from user Myself via device <DeviceName>
    When User <Contact> deletes the recent message everywhere from user <Name> via device <DeviceName>
    Then I do not see location map container in the conversation view
    And User <Contact> sees the recent message from user Myself via device <DeviceName> is changed in 15 seconds

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |

  @C206257 @regression @fastLogin
  Scenario Outline: Verify delete everywhere works for audio messages
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<DeviceName>"}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    And I tap Send record control button
    Then I see audio message container in the conversation view
    And User <Contact> remembers the recent message from user Myself via device <DeviceName>
    When I long tap on audio message placeholder in conversation view
    And I tap on Delete badge item
    And I select Delete for Everyone item from Delete menu
    Then I do not see audio message container in the conversation view
    And User <Contact> sees the recent message from user Myself via device <DeviceName> is changed in 15 seconds

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |

  @C202342 @regression @fastLogin
  Scenario Outline: Verify delete for everyone system message doesn't produce an unread dot
    Given There are 2 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<DeviceName>"}]}
    Given I sign in using my email or phone number
    Given User <Contact1> sends 1 default message to conversation Myself
    # Wait for message to be delivered
    Given I wait for 3 seconds
    Given I see conversations list
    When I remember the state of <Contact1> conversation item
    And I tap on contact name <Contact1>
    And I navigate back to conversations list
    Then I see the state of <Contact1> conversation item is changed
    And I remember the state of <Contact1> conversation item
    And User <Contact1> remembers the recent message from user Myself via device <DeviceName>
    When User <Contact1> deletes the recent message everywhere from user <Name> via device <DeviceName>
    Then User <Contact1> sees the recent message from user Myself via device <DeviceName> is changed in 15 seconds
    And I see the state of <Contact1> conversation item is not changed

    Examples:
      | Name      | Contact1  | DeviceName |
      | user1Name | user2Name | device1    |

  @C206262 @regression @fastLogin
  Scenario Outline: Verify deleted messages/edit message doesn't unarchive the "archived conversation"
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given User adds the following device: {"<Contact1>": [{"name": "<Contact1Device>"}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 default message to conversation <Group>
    When I swipe right on conversation <Group>
    And I tap Archive conversation action button
    Then I do not see conversation <Group> in conversations list
    When User <Contact1> edits the recent message to "<Message1>" from user Myself via device <Contact1Device>
    Then I do not see conversation <Group> in conversations list
    When User <Contact1> deletes the recent message everywhere from user <Name> via device <Contact1Device>
    Then I do not see conversation <Group> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Contact1Device | Group       | Message1 |
      | user1Name | user2Name | user3Name | device1        | RecallGroup | test1    |