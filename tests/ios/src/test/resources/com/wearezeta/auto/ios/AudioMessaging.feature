Feature: Audio Messaging

  @C129323 @rc @regression @fastLogin
  Scenario Outline: Verify message is started recording by long tapping on the icon
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    Then I see audio message recorder container in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C129327 @rc @fastLogin @regression
  Scenario Outline:  Verify sending voice message by long tap > release the thumb > tap on the check icon
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<DeviceName1>"}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    And I tap Send record control button
    Then I see audio message container in the conversation view
    # Wait for delivery
    And I wait for 3 seconds
    And I see "<DeliveredLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact   | DeviceName1 | DeliveredLabel |
      | user1Name | user2Name | device1     | Delivered      |

  @C129341 @C129345 @rc @regression @fastLogin
  Scenario Outline: Verify receiving a voice message and deleting it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Users add the following devices: {"Myself": [{}], "<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User Myself sends 1 default message to conversation <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    # Small wait to make the appearence of button on jenkins more stable
    Given I wait for 3 seconds
    Given I see audio message container in the conversation view
    When I long tap on audio message placeholder in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I do not see audio message container in the conversation view

    Examples:
      | Name      | Contact1  | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       |

  @C131214 @regression @fastLogin
  Scenario Outline: Verify cancelling recorded audio message preview
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap Audio Message button from input tools
    And I tap Cancel record control button
    Then I do not see audio message container in the conversation view
    And I see Audio Message button in input tools palette

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C131218 @regression @fastLogin
  Scenario Outline: Verify not sent yet audio message is preserved on minimising the app
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I long tap Audio Message button from input tools
    When I minimize Wire for 3 seconds
    Then I see audio message recorder container in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C131219 @regression @fastLogin
  Scenario Outline: Verify not sent yet audio message is deleted on switching between the conversations
    Given There are 3 user where <Name> is me
    Given Myself is connected to all other
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I long tap Audio Message button from input tools
    Given I see audio message recorder container in the conversation view
    When I navigate back to conversations list
    And I tap on contact name <Contact2>
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I do not see audio message recorder container in the conversation view

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C129346 @regression @fastLogin
  Scenario Outline: Verify impossibility of saving voice message before downloading
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User Me sends 1 default message to conversation <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    # Small wait to make the appearence of button on jenkins more stable
    Given I wait for 3 seconds
    When I long tap on audio message placeholder in conversation view
    Then I do not see Save badge item
    When I tap Play audio message button
    # Small wait to make sure download is completed
    And I wait for 5 seconds
    And I long tap on audio message placeholder in conversation view
    Then I see Save badge item

    Examples:
      | Name      | Contact1  | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       |

  @C131217 @rc @regression @fastLogin
  Scenario Outline: Verify playback is stopped when other audio message starts playing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User add the following device: {"<Contact1>": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User Me sends 1 default message to conversation <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    # Small wait to make the appearence of button on jenkins more stable
    Given I wait for 3 seconds
    When I tap Play audio message button on audio message placeholder number 2
    And I see state of button on audio message placeholder number 2 is pause
    And I tap Play audio message button on audio message placeholder number 1
    Then I see state of button on audio message placeholder number 2 is play

    Examples:
      | Name      | Contact1  | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       |

  @C139855 @regression @fastLogin
  Scenario Outline: (ZIOS-6688) Verify playback is stopped when incoming call has appeared
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given User <Contact> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User Me sends 1 default message to conversation <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    # Small wait to make the appearence of button on jenkins more stable
    Given I wait for 3 seconds
    And I remember the state of Play button on audio message placeholder
    And I tap Play audio message button
    # Wait to make sure the audio file is downloaded and starts playback
    And I wait for <AudioDownloadTimeout> seconds
    And <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Ignore button on Calling overlay
    Then I verify the state of Play button on audio message placeholder is not changed

    Examples:
      | Name      | Contact   | FileName | FileMIME  | ContactDevice | CallBackend | AudioDownloadTimeout |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       | chrome      | 5                    |

  @C139860 @regression @fastLogin
  Scenario Outline: Verify playback is stopped when Soundcloud playback is started
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 "<SoundCloudLink>" message to conversation Myself
    Given User <Contact> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User Me sends 1 default message to conversation <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    # Small wait to make the appearence of button on jenkins more stable
    Given I wait for 3 seconds
    When I remember the state of Play button on audio message placeholder
    And I tap Play audio message button
    # Wait until the audio is downloaded and starts playback
    And I wait for <AudioDownloadTimeout> seconds
    And I tap on media container in conversation view
    # Wait for audio message playback to stop
    And I wait for 5 seconds
    Then I verify the state of Pause button on audio message placeholder is not changed

    Examples:
      | Name      | Contact   | FileName | FileMIME  | ContactDevice | AudioDownloadTimeout | SoundCloudLink                                                   |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       | 7                    | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C131215 @regression @fastLogin
  Scenario Outline: Verify playback is stopped when audio message recording is started
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User Me sends 1 default message to conversation <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    # Small wait to make the appearence of button on jenkins more stable
    Given I wait for 3 seconds
    When I remember the state of Play button on audio message placeholder
    And I tap Play audio message button
    # Wait until the audio is downloaded and starts playback
    And I wait for <AudioDownloadTimeout> seconds
    And I long tap Audio Message button for 10 seconds from input tools
    When I tap Cancel record control button
    Then I verify the state of Play button on audio message placeholder is not changed

    Examples:
      | Name      | Contact1  | FileName | FileMIME  | ContactDevice | AudioDownloadTimeout |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       | 7                    |

  @C139856 @regression @fastLogin
  Scenario Outline: (ZIOS-6688) Verify playback is stopped when outgoing call is started
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User Me sends 1 default message to conversation <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    # Waiy until the asset is loaded
    Given I wait for 3 seconds
    When I remember the state of Play button on audio message placeholder
    And I tap Play audio message button
    # Wait until the audio is downloaded and starts playback
    And I wait for <AudioDownloadTimeout> seconds
    And I tap Audio Call button
    And I tap Leave button on Calling overlay
    Then I verify the state of Play button on audio message placeholder is not changed

    Examples:
      | Name      | Contact1  | FileName | FileMIME  | ContactDevice | AudioDownloadTimeout |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       | 7                    |

  @C139862 @regression @fastLogin
  Scenario Outline: Verify Soundcloud playback is stopped when audio message recording is started
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When User Myself sends 1 "<SoundCloudLink>" message to conversation <Contact>
    And I tap on contact name <Contact>
    And I remember media container state
    And I tap on media container in conversation view
    And I long tap Audio Message button for 5 seconds from input tools
    Then I see audio message recorder container in the conversation view
    And I see media container state is not changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C129325 @C129324 @regression @fastLogin
  Scenario Outline: Verify playing the message by tapping on the play icon on record toolbar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact1>
    # Let it record something for specific duration
    Given I see Audio Message button in input tools palette
    When I long tap Audio Message button for <Duration> seconds from input tools
    And I see Play record control button
    And I tap Play record control button
    Then I see state of button on record toolbar is playing
    # TODO: Should be uncommented once ZIOS-6798 is fixed
    #And I see the audio message in record toolbar gets played

    Examples:
      | Name      | Contact1  | Duration |
      | user1Name | user2Name | 20       |

  @C129342 @rc @regression @fastLogin
  Scenario Outline: Verify playing/pausing a received voice message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    When I tap on contact name <Contact1>
    And User Me sends 1 default message to conversation <Contact1>
    And I see audio message container in the conversation view
    And I tap Play audio message button
    Then I see state of button on audio message placeholder is pause
    # TODO: Should be uncommented once ZIOS-6798 is fixed
    #And I see the audio message in placeholder gets played
    When I tap Pause audio message button
    Then I see state of button on audio message placeholder is play
    # TODO: Should be uncommented once ZIOS-6798 is fixed
    #And I see the audio message in placeholder gets paused

    Examples:
      | Name      | Contact1  | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       |

  @C139861 @regression @fastLogin
  Scenario Outline: Verify Soundcloud playback is stopped when audio message playback is started
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 "<SoundCloudLink>" message to conversation Myself
    Given User <Contact> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap on media container in conversation view
    # Wait until audio message is fully loaded
    Given I wait for 7 seconds
    Given I tap Play audio message button
    # Wait until audio track is loaded
    Given I wait for 7 seconds
    When I remember media container state
    And I wait for 3 seconds
    Then I see media container state is not changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                                   | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic | test.m4a | audio/mp4 | Device1       |