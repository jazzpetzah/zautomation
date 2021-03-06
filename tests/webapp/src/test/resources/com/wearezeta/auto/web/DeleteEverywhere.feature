Feature: Delete Everywhere

  @C206234 @delete-everywhere @regression
  Scenario Outline: Verify I can delete my message everywhere (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message1>
    And I send message
    And I see text message <Message1>
    And I see 2 messages in conversation
    And I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see text message <Message1>
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 |
      | user1Email | user1Password | user1Name | user2Name | message1 |

  @C206235 @delete-everywhere @regression
  Scenario Outline: Verify I can delete my message everywhere (group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I write message <Message1>
    And I send message
    And I see text message <Message1>
    And I see 2 messages in conversation
    And I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see text message <Message1>
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName | Message1 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | New name | message1 |

  @C206242 @delete-everywhere @regression
  Scenario Outline: Verify delete everywhere works for images
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    And I see sent picture <PictureName> in the conversation view
    And I see 2 messages in conversation
    And I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see any picture in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |

  @C206244 @linkpreview @delete-everywhere @regression
  Scenario Outline: Verify delete everywhere works for link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact Myself sends message <Link> via device Device1 to user <Contact>
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see 2 messages in conversation
    And I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see a title <LinkTitle> in link preview in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Link                           | LinkTitle   |
      | user1Email | user1Password | user1Name | user2Name | http://www.heise.de/newsticker | 7-Tage-News |

  @C206245 @delete-everywhere @regression
  Scenario Outline: Verify delete everywhere works for location sharing
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And User Myself sends location <LocationName> with <Latitude> and <Longitude> to group conversation <ChatName> via device Device1
    And I see location message <LocationName> with <Latitude> and <Longitude> in the conversation view
    And I see 2 messages in conversation
    And I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see location message <LocationName> with <Latitude> and <Longitude> in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName | Latitude  | Longitude   | LocationName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | New name | 33.747252 | -112.633853 | Triangle     |

  @C206246 @delete-everywhere @regression
  Scenario Outline: Verify delete everywhere works for file sharing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send <Size> sized file with name <File> to the current conversation
    And I wait until file <File> is uploaded completely
    And I see 2 messages in conversation
    And I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see file transfer for file <File> in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  |
      | user1Email | user1Password | user1Name | user2Name | C206246.zip | 512KB |

  @C206247 @delete-everywhere @regression
  Scenario Outline: Verify delete everywhere works for audio messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send audio file with length <Time> and name <File> to the current conversation
    And I wait until audio <File> is uploaded completely
    And I see 2 messages in conversation
    And I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see audio message <File> in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Time  |
      | user1Email | user1Password | user1Name | user2Name | example.wav | 00:20 |

  @C206248 @delete-everywhere @regression
  Scenario Outline: Verify delete everywhere works for video messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send <Size> sized video with name <File> to the current conversation
    And I wait until video <File> is uploaded completely
    And I see 2 messages in conversation
    And I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see video message <File> in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size |
      | user1Email | user1Password | user1Name | user2Name | C206248.mp4 | 1MB  |

  @C206236 @delete-everywhere @regression
  Scenario Outline: Verify I see status message if other user deletes his message everywhere (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Message_1> via device Device1 to user <Name>
    And Contact <Contact> sends message <Message_2> via device Device1 to user <Name>
    Then I see text message <Message_1>
    And I see text message <Message_2>
    When User <Contact> deletes the recent 1 message everywhere in single conversation <Name> via device Device1
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    And I see 1 deleted messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message_1      | Message_2      | Message_3      |
      | user1Email | user1Password | user1Name | user2Name | Test_Message_1 | Test_Message_2 | Test_Message_3 |

  @C206237 @delete-everywhere @regression
  Scenario Outline: Verify I see status message if other user deletes his message everywhere (group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device2 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And Contact <Contact1> sends message <Message_1> via device Device1 to group conversation <ChatName>
    And Contact <Contact1> sends message <Message_2> via device Device1 to group conversation <ChatName>
    Then I see text message <Message_1>
    And I see text message <Message_2>
    When User <Contact1> deletes the recent 1 message everywhere in group conversation <ChatName> via device Device1
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    And I see 1 deleted messages in conversation
    When Contact <Contact2> sends message <Message_3> via device Device2 to group conversation <ChatName>
    And Contact <Contact2> sends message <Message_4> via device Device2 to group conversation <ChatName>
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    And I see 1 deleted messages in conversation
    And I see text message <Message_3>
    And I see text message <Message_4>
    When User <Contact2> deletes the recent 1 message everywhere in group conversation <ChatName> via device Device2
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    And I see text message <Message_3>
    And I do not see text message <Message_4>
    And I see 2 deleted messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName | Message_1      | Message_2      | Message_3      | Message_4      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GC1      | Test_Message_1 | Test_Message_2 | Test_Message_3 | Test_Message_4 |

  @C206240 @delete-everywhere @regression
  Scenario Outline: When I delete my message everywhere on a different device (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given user <Name> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message_1>
    And I send message
    And I write message <Message_2>
    And I send message
    Then I see text message <Message_2>
    And I see text message <Message_2>
    When User <Name> deletes the recent 1 message everywhere in single conversation <Contact> via device Device1
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    And I see 0 deleted messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message_1      | Message_2      |
      | user1Email | user1Password | user1Name | user2Name | Test_Message_1 | Test_Message_2 |

  @C206241 @delete-everywhere @regression
  Scenario Outline: When I delete my message everywhere on a different device (group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given user <Name> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I write message <Message_1>
    And I send message
    And I write message <Message_2>
    And I send message
    Then I see text message <Message_1>
    And I see text message <Message_2>
    When User <Name> deletes the recent 1 message everywhere in group conversation <ChatName> via device Device1
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    And I see 0 deleted messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName | Message_1      | Message_2      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GC1      | Test_Message_1 | Test_Message_2 |

  @C206250 @delete-everywhere @regression
  Scenario Outline: Verify deleted messages remain deleted everywhere after I archive and unarchive a conversation
    Given There are 2 users where <Name> is me
    Given user <Name> adds a new device Device1 with label Label1
    Given user <Contact> adds a new device Device2 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message_1>
    And I send message
    And I write message <Message_2>
    And I send message
    Then I see text message <Message_1>
    And I see text message <Message_2>
    When User Myself deletes the recent 1 message everywhere in single conversation <Contact> via device Device1
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    When Contact <Contact> sends message <Message_3> via device Device2 to user <Name>
    Then I see text message <Message_3>
    When User <Contact> deletes the recent 1 message everywhere in single conversation <Name> via device Device2
    Then I do not see text message <Message_3>
    And I see 1 deleted messages in conversation
    When I archive conversation <Contact>
    And I do not see Contact list with name <Contact>
    And I open archive
    And I see archive list with name <Contact>
    And I unarchive conversation <Contact>
    And I see Contact list with name <Contact>
    And I open conversation with <Contact>
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    And I do not see text message <Message_3>
    And I see 1 deleted messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message_1      | Message_2      | Message_3      |
      | user1Email | user1Password | user1Name | user2Name | Test_Message_1 | Test_Message_2 | Test_Message_3 |

  @C206249 @delete-everywhere @regression
  Scenario Outline: Verify I see no unread dot if a message was deleted from someone in a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact1>
    And Contact <Contact1> sends message <Message_1> via device Device1 to user <Name>
    And Contact <Contact1> sends message <Message_2> via device Device1 to user <Name>
    Then I see text message <Message_1>
    And I see text message <Message_2>
    When I open conversation with <Contact2>
    And User <Contact1> deletes the recent 1 message everywhere in single conversation <Name> via device Device1
    And I wait for 5 seconds
    Then I do not see unread dot in conversation <Contact1>
    When I open conversation with <Contact1>
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    And I see 1 deleted messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Message_1      | Message_2      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Test_Message_1 | Test_Message_2 |

  @C206258 @delete-everywhere @regression @WEBAPP-3040
  Scenario Outline: Verify delete everywhere works for Soundcloud, Spotify, YouTube, Vimeo
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given user <Name> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <Contact>
    When I write message <Youtubelink>
    And I send message
    And I see embedded youtube message <Youtubelink>
    When I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    And I do not see text message <Youtubelink>
    Then I do not see embedded youtube message <Youtubelink>
    When I write message <Soundcloudlink>
    And I send message
    Then I see embedded soundcloud message <Soundcloudlink>
    And I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see embedded soundcloud message <Soundcloudlink>
    When I write message <Vimeolink>
    And I send message
    Then I see embedded vimeo message <Vimeolink>
    And I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see embedded vimeo message <Vimeolink>
    And I write message <Spotifylink>
    And I send message
    And I see embedded spotify message <Spotifylink>
    When I click context menu of the last message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see embedded spotify message <Spotifylink>
    When Contact <Contact> sends message <Youtubelink> via device Device1 to user me
    Then I see embedded youtube message <Youtubelink>
    When User <Contact> deletes the recent 1 message everywhere in single conversation <Name> via device Device1
    And I do not see text message <Youtubelink>
    And I see 1 deleted messages in conversation
    When Contact <Contact> sends message <Soundcloudlink> via device Device1 to user me
    Then I see text message <Soundcloudlink>
    When User <Contact> deletes the recent 1 message everywhere in single conversation <Name> via device Device1
    And I do not see text message <Soundcloudlink>
    And I see 2 deleted messages in conversation
    When Contact <Contact> sends message <Vimeolink> via device Device1 to user me
    Then I see text message <Vimeolink>
    When User <Contact> deletes the recent 1 message everywhere in single conversation <Name> via device Device1
    And I do not see text message <Vimeolink>
    And I see 3 deleted messages in conversation
    When Contact <Contact> sends message <Spotifylink> via device Device1 to user me
    Then I see text message <Spotifylink>
    When User <Contact> deletes the recent 1 message everywhere in single conversation <Name> via device Device1
    And I do not see text message <Spotifylink>
    And I see 4 deleted messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Youtubelink                                 | Soundcloudlink                                                      | Vimeolink                 | Spotifylink                                           |
      | user1Email | user1Password | user1Name | user2Name | https://www.youtube.com/watch?v=ncHd3sxpEbo | https://soundcloud.com/nour-moukhtar/ludwig-van-beethoven-fur-elise | https://vimeo.com/7265982 | https://play.spotify.com/album/7buEcyw6fJF3WPgr06BomH |

  @C399361 @regression
  Scenario Outline: Verify I can delete a picture for everyone from fullscreen view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    And I see sent picture <PictureName> in the conversation view
    And I click on picture
    Then I see picture <PictureName> in picture fullscreen
    And I see delete everywhere button in picture fullscreen
    When I click delete everywhere button in picture fullscreen
    And I click cancel to delete message for for everyone
    Then I see picture <PictureName> in picture fullscreen
    When I click delete everywhere button in picture fullscreen
    And I click confirm to delete message for everyone
    Then I see only 0 pictures in the conversation

    Examples:
      | Login      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |

  @C399366 @regression
  Scenario Outline: Verify I can't delete a picture from others for everyone from fullscreen view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And User <Contact> sends image <PictureName> to single user conversation <Name>
    And I see only 1 pictures in the conversation
    And I click on picture
    Then I see picture <PictureName> in picture fullscreen
    And I do not see delete everywhere button in picture fullscreen

    Examples:
      | Login      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |
