Feature: Conversation View

  @id324 @regression @rc
  Scenario Outline: Mute conversation from conversation view
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I press options menu button
    And I press SILENCE conversation menu button
    #And I return to group chat page
    #Some elements seem to be missing (e.g. "X" button) so
    #Instead of searching for elements, it works perfectly fine (and faster) just to press back 3 times
    And I press back button
    And I press back button
    #And I navigate back from dialog page
    Then Contact <Contact1> is muted

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @id1514 @regression
  Scenario Outline: Verify unsilence the conversation from conversation view
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to me
    Given <Contact1> is silenced to user <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    Given Contact <Contact1> is muted
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I press options menu button
    And I press NOTIFY conversation menu button
    And I press back button
    And I navigate back from dialog page
    Then Contact <Contact1> is not muted

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @id316 @regression
  Scenario Outline: Send Message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @id318 @regression @rc @rc42
  Scenario Outline: Send Camera picture to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id1262 @regression @rc @rc42
  Scenario Outline: Create group conversation from 1:1
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I see <Contact1> user profile page
    And I press add contact button
    And I tap on Search input on People picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I see user <Contact2> found on People picker page
    And I tap on user name found on People picker page <Contact2>
    And I see Add to conversation button
    And I click on Add to conversation button
    Then I see group chat page with users <Contact1>,<Contact2>
    And I navigate back from dialog page
    And I see <Contact1> and <Contact2> chat in contact list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id320 @regression @rc
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @id143 @regression @rc
  Scenario Outline: Send Long Message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "LONG_MESSAGE" and send it
    Then I see my message "LONG_MESSAGE" in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id145 @regression
  Scenario Outline: Send Upper and Lower case to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples:
      | Name      | Contact   | Message  |
      | user1Name | user2Name | aaaaAAAA |

  @id149 @regression
  Scenario Outline: Send emoji message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples:
      | Name      | Contact   | Message  |
      | user1Name | user2Name | :) ;) :( |

  @id163 @regression
  Scenario Outline: Send existing image from gallery (portrait) in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Gallery" button
    And I rotate UI to portrait
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id162 @regression @rc @rc42
  Scenario Outline: Send existing image from gallery (landscape) in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Gallery" button
    When I rotate UI to landscape
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2078 @regression
  Scenario Outline: I want to exit fullscreen view in landscape (rotations)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Gallery" button
    And I press "Confirm" button
    Then I see new photo in the dialog
    And I select last photo in dialog
    And I rotate UI to landscape
    And I swipe down on dialog page
    And I rotate UI to portrait
    Then I select last photo in dialog
    And I rotate UI to landscape
    And I tap on center of screen
    And I press "Image Close" button
    Then I rotate UI to portrait
    And I navigate back from dialog page
    And I see Contact list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id1504 @regression @rc @rc42
  Scenario Outline: Verify you can play/pause media from the Media Bar (SoundCloud)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And Contact <Contact1> send 18 messages to user Myself
    And I scroll to the bottom of conversation view
    And I tap on text input
    And I type the message "<SoudCloudLink>" and send it
    And Contact <Contact1> send message to user Myself
    And I scroll to the bottom of conversation view
    And I press PlayPause media item button
    And I remember the state of PlayPause media item button
    And I swipe down on dialog page until Mediabar appears
    Then I see PAUSE on Mediabar
    And I press PlayPause on Mediabar button
    When I scroll to the bottom of conversation view
    Then I verify the state of PlayPause media item button is changed

    Examples:
      | Name      | Contact1  | SoudCloudLink                                              |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @id170 @regression @rc @rc42
  Scenario Outline: Verify you can send youtube link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I type the message "<YoutubeLink>" and send it
    Then I see Play button on Youtube container

    Examples:
      | Name      | Contact1  | YoutubeLink                                 |
      | user1Name | user2Name | https://www.youtube.com/watch?v=wTcNtgA6gHs |

  @id3242 @regression @rc @rc42
  Scenario Outline: I can send a sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe on text input
    And I press Sketch button
    And I draw a sketch with <NumColors> colors
    And I send my sketch
    And I select last photo in dialog

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |

  @id3243 @regression @rc @rc42
  Scenario Outline: I can send sketch on image from gallery
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I rotate UI to portrait
    And I swipe on text input
    And I press Add Picture button
    And I press "Gallery" button
    And I press "Sketch Image Paint" button
    And I draw a sketch on image with <NumColors> colors
    Then I send my sketch
    And I select last photo in dialog

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |

  @id3244 @regression
  Scenario Outline: I can send sketch on photo
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Sketch Image Paint" button
    And I draw a sketch on image with <NumColors> colors
    And I send my sketch
    And I select last photo in dialog

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |

  @id2990 @regression @rc @rc42
  Scenario Outline: I can send giphy image by typing some massage and clicking GIF button
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>"
    And I click on the GIF button
    Then I see giphy preview page
    When I click on the giphy send button
    Then I see dialog page
    And I see new photo in the dialog
    And Last message is <Message> · via giphy.com

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @id165 @regression @rc
  Scenario Outline: Send GIF format pic
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given Contact <Contact> sends image <GifName> to single user conversation <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I scroll to the bottom of conversation view
    Then I see new picture in the dialog
    And I see the picture in the dialog is animated
    When I select last photo in dialog
    Then I see the picture in the preview is animated

    Examples:
      | Name      | Contact   | GifName      |
      | user1Name | user2Name | animated.gif |
