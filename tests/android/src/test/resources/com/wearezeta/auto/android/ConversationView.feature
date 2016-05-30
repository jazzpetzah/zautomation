Feature: Conversation View

  @C688 @id324 @regression @rc
  Scenario Outline: Mute conversation from conversation view
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap conversation name from top toolbar
    And I press options menu button
    And I press SILENCE conversation menu button
    And I press back button
    And I press back button
    Then Contact <Contact1> is muted

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C414 @id1514 @regression
  Scenario Outline: Verify unsilence the conversation from conversation view
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to me
    Given <Contact1> is silenced to user <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given Contact <Contact1> is muted
    When I tap on contact name <Contact1>
    And I tap conversation name from top toolbar
    And I press options menu button
    And I press NOTIFY conversation menu button
    And I press back button
    And I navigate back from dialog page
    Then Contact <Contact1> is not muted

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C381 @id316 @regression
  Scenario Outline: Send Message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C682 @id318 @regression @rc @rc42
  Scenario Outline: Send Camera picture to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C700 @id1262 @regression @rc @rc42
  Scenario Outline: Create group conversation from 1:1
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap conversation name from top toolbar
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
    And I see group conversation with <Contact1>,<Contact2> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |
      
  @C18044 @regression
  Scenario Outline: (AN-3428) I see creation header when someone create group conversation with me
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>
    When I tap on contact name <GroupChatName>
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName      |
      | user1Name | user2Name | user3Name | MeAddedToGroupChat |

  @C684 @id320 @regression @rc
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @C671 @id143 @regression @rc
  Scenario Outline: Send Long Message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap on text input
    And I type the message "LONG_MESSAGE" and send it
    Then I see the message "LONG_MESSAGE" in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C377 @id145 @regression
  Scenario Outline: Send Upper and Lower case to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message  |
      | user1Name | user2Name | aaaaAAAA |

  @C378 @id149 @regression
  Scenario Outline: Send emoji message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message  |
      | user1Name | user2Name | :) ;) :( |

  @C379 @id163 @regression
  Scenario Outline: Send existing image from gallery (portrait) in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Gallery button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C419 @id2078 @regression
  Scenario Outline: I want to exit fullscreen view in landscape (rotations)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Gallery button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view
    And I tap the recent picture in the conversation view
    And I rotate UI to landscape
    And I scroll down the conversation view
    And I rotate UI to portrait
    Then I tap the recent picture in the conversation view
    And I rotate UI to landscape
    And I tap Image Close button on Take Picture view
    Then I rotate UI to portrait
    And I navigate back from dialog page
    And I see Contact list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C809 @id3242 @regression @rc @rc42
  Scenario Outline: (CM-717) I can send a sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap Sketch button from cursor toolbar
    And I draw a sketch with <NumColors> colors
    And I send my sketch
    And I tap the recent picture in the conversation view

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 2         |

  @C810 @id3243 @regression @rc @rc42
  Scenario Outline: (CM-717) I can send sketch on image from gallery
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap Add picture button from cursor toolbar
    And I tap Gallery button on Take Picture view
    And I tap Sketch Image Paint button on Take Picture view
    And I draw a sketch on image with <NumColors> colors
    Then I send my sketch
    And I tap the recent picture in the conversation view

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 2         |

  @C432 @id3244 @regression
  Scenario Outline: I can send sketch on photo
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Take Picture view
    And I tap Sketch Image Paint button on Take Picture view
    And I draw a sketch on image with <NumColors> colors
    And I send my sketch
    And I tap the recent picture in the conversation view

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |

  @C787 @id2990 @regression @rc @rc42
  Scenario Outline: I can send giphy image by typing some massage and clicking GIF button
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message>"
    And I click on the GIF button
    Then I see giphy preview page
    When I click on the giphy send button
    Then I see a picture in the conversation view
    And I see the most recent conversation message is "<Message> · via giphy.com"

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C674 @id165 @regression @rc
  Scenario Outline: (BUG AN-3443) Send GIF format pic
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given User <Contact> sends encrypted image <GifName> to single user conversation Myself
    When I tap on contact name <Contact>
    And I scroll to the bottom of conversation view
    Then I see a picture in the conversation view
    And I see the picture in the dialog is animated
    When I tap the recent picture in the conversation view
    Then I see the picture in the preview is animated

    Examples:
      | Name      | Contact   | GifName      |
      | user1Name | user2Name | animated.gif |

  @C672 @id159 @regression @rc
  Scenario Outline: Send image with non default camera (portrait) in group chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Switch Camera button on Take Picture view
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C236 @rc @regression
  Scenario Outline: I can send giphy image from the giphy grid preview
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message>"
    And I click on the GIF button
    Then I see giphy preview page
    Then I click on the giphy link button
    Then I see the giphy grid preview
    Then I select a random gif from the grid preview
    Then I see giphy preview page
    When I click on the giphy send button
    Then I see a picture in the conversation view
    And I see the most recent conversation message is "<Message> · via giphy.com"

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C77948 @C77950 @rc @regression
  Scenario Outline: Upper toolbar displayed in conversation view, I can back to conversation list by toolbar arrow
    Given There is 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    Then I see the upper toolbar
    And I tap back button in upper toolbar
    Then I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    Then I see the upper toolbar
    And I tap back button in upper toolbar
    Then I see Contact list with contacts

    Examples:
      | Name      | Contact1   | Contact2   | GroupChatName |
      | user1Name | user2Name  | user3Name  | GroupChat     |

  @C77958 @regression
  Scenario Outline: Verify video call icon is not shown in a group conversation on the upper toolbar
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>
    When I tap on contact name <GroupChatName>
    Then I see the audio call button in upper toolbar
    And I do not see the video call button in upper toolbar
    And I navigate back from dialog page
    And I see Contact list with contacts
    When I tap on contact name <Contact1>
    Then I see the audio call button in upper toolbar
    And I see the video call button in upper toolbar

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     |
      | user1Name | user2Name | user3Name | SendMessGroupChat |

  @C78372 @regression
  Scenario Outline: Verify title is not changed on receiving messages in other conversations
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And the conversation title should be "<Contact1>"
    And User <Contact2> send message "<Message1>" to user Myself
    And I tap new message notification "<Message1>"
    Then I see the message "<Message1>" in the conversation view
    And the conversation title should be "<Contact2>"
    And I tap conversation name from top toolbar
    And I press back button
    When I tap back button in upper toolbar
    And I tap on contact name <Contact1>
    And User <Contact2> send message "<Message2>" to user Myself
    And I see new message notification "<Message2>"
    Then the conversation title should be "<Contact1>"

    Examples:
      | Name      | Contact1  | Contact2  |  Message1 | Message2 |
      | user1Name | user2Name | user3Name |  Msg1     | Msg2     |

  @C77966 @regression @C87626 @rc @rc42 @C111617
  Scenario Outline: Verify there are no video and audio calling icons under the + button bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    Then I see cursor toolbar
    And I see text input

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C111622 @C111625 @rc @regression
  Scenario Outline: Verify tooltip is shown in different condition
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap Ping button from cursor toolbar
    Then I see tooltip of text input
    When I tap on text input
    Then I see tooltip of text input
    And I see self avatar on text input
    When I type the message "<Message>"
    And I do not see tooltip of text input

    Examples:
      | Name      | Contact1  | Message |
      | user1Name | user2Name | testing |

  @C111631 @C111634 @rc @regression
  Scenario Outline: Verify cursor and toolbar are not shown on left/removed from conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I tap conversation name from top toolbar
    And I press options menu button
    And I press LEAVE conversation menu button
    And I confirm leaving
    And I see Contact list
    And I open Search UI
    And I enter "<GroupChatName>" into Search input on People Picker page
    And I tap on group found on People picker page <GroupChatName>
    Then I see the upper toolbar
    And I do not see text input
    And I do not see cursor toolbar
    When User <Contact1> adds user Myself to group chat <GroupChatName>
    Then I see text input
    And I see cursor toolbar

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3Name | LeaveGroupChat |

  @C111635 @rc @regression
  Scenario Outline: Verify the cursor is not shown on the new device after you leave the group from another device
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Myself leave group chat <GroupChatName>
    Given Myself is unarchived group chat <GroupChatName>
    When I sign in using my email or phone number
    And I accept First Time overlay as soon as it is visible
    And I see Contact list with contacts
    And I tap on contact name <GroupChatName>
    Then I do not see text input
    And I do not see cursor toolbar

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3Name | LeaveGroupChat |
