Feature: Like

  @C226438 @like @regression
  Scenario Outline: Verify you can like someone's message in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Message1> via device Device1 to user me
    Then I see text message <Message1>
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by users Myself,<Contact>
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is liked by users <Contact>
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 |
      | user1Email | user1Password | user1Name | user2Name | like me  |

  @C250829 @like @edit @regression
  Scenario Outline: Verify I can edit and then like my message in 1:1
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact1>
    And I write message <Message1>
    Then I send message
    And I see text message <Message1>
    And I see 2 messages in conversation
    When I press Up Arrow to edit message
    And I delete 8 characters from the conversation input
    And I write message <Message2>
    And I send message
    Then I do not see text message <Message1>
    And I see text message <Message2>
    And I see 2 messages in conversation
    When I click to like the last message without other likes
    Then I see the last message is only liked by me
    When I open conversation with <Contact2>
    And I see 1 messages in conversation
    And I open conversation with <Contact1>
    And I see text message <Message2>
    And I see 2 messages in conversation
    Then I see the last message is only liked by me

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Message1 | Message2       |
      | user1Email | user1Password | user1Name | user2Name | user3Name | message1 | edited message |

  @C226472 @regression
  Scenario Outline: Verify you can like someone's message in group
    Given There are 6 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device2 with label Label2
    Given user <Contact3> adds a new device Device3 with label Label3
    Given user <Contact4> adds a new device Device4 with label Label4
    Given user <Contact5> adds a new device Device5 with label Label5
    Given I am signed in properly
    When I open conversation with <ChatName>
    And Contact <Contact1> sends message <Message1> via device Device1 to group conversation <ChatName>
    Then I see text message <Message1>
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact1> likes the recent message from group conversation <ChatName> via device Device1
    And User <Contact2> likes the recent message from group conversation <ChatName> via device Device2
    And User <Contact3> likes the recent message from group conversation <ChatName> via device Device3
    And User <Contact4> likes the recent message from group conversation <ChatName> via device Device4
    And User <Contact5> likes the recent message from group conversation <ChatName> via device Device5
    And I see likes below the last message
    And I see the last message is liked by users Myself,<Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    When I open conversation with <Contact1>
    And I see 1 messages in conversation
    And I open conversation with <ChatName>
    And I see likes below the last message
    And I see the last message is liked by users Myself,<Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is liked by users <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
# Everything unliked
    When User <Contact1> unlikes the recent message from group conversation <ChatName> via device Device1
    And User <Contact2> unlikes the recent message from group conversation <ChatName> via device Device2
    And User <Contact3> unlikes the recent message from group conversation <ChatName> via device Device3
    And User <Contact4> unlikes the recent message from group conversation <ChatName> via device Device4
    And User <Contact5> unlikes the recent message from group conversation <ChatName> via device Device5
    And I do not see likes below the last message

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | ChatName | Message1 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | GC1      | like me  |

  @C226427 @linkpreview @regression
  Scenario Outline: Verify liking someone's link preview in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Link> via device Device1 to user me
    Then I see link <LinkInPreview> in link preview message
    And I do not see likes below the last message
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
    When I click to unlike the last message without other likes
    Then I do not see likes below the last message

    Examples:
      | Login      | Password      | Name      | Contact   | Link     | LinkInPreview |
      | user1Email | user1Password | user1Name | user2Name | wire.com | wire.com      |

  @C226428 @regression
  Scenario Outline: Verify liking someone's picture
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I am signed in properly
    When I open conversation with <Contact>
    When User <Contact> sends image <ImageName> to single user conversation <Name>
    And I see sent picture <ImageName> in the conversation view
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message

    Examples:
      | Login      | Password      | Name      | Contact   | ImageName                |
      | user1Email | user1Password | user1Name | user2Name | userpicture_portrait.jpg |

  @C395990 @regression
  Scenario Outline: Verify I can like an image from fullscreen view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I am signed in properly
    When I open conversation with <Contact>
    When User <Contact> sends image <ImageName> to single user conversation <Name>
    And I see sent picture <ImageName> in the conversation view
# No likes
    And I do not see likes below the last message
    When I click on picture
    Then I see picture <ImageName> in picture fullscreen
    And I see like button in picture fullscreen
    And I see the picture in fullscreen is not liked by me
# Only liked by me
    When I click like button in picture fullscreen
    Then I see the picture in fullscreen is liked by me
    When I click x button to close picture fullscreen mode
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
    When I click on picture
    Then I see picture <ImageName> in picture fullscreen
    Then I see the picture in fullscreen is liked by me
# Only liked by others
    When I click like button in picture fullscreen
    Then I see the picture in fullscreen is not liked by me
    When I click x button to close picture fullscreen mode
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message
    When I click on picture
    Then I see picture <ImageName> in picture fullscreen
    And I see the picture in fullscreen is not liked by me

    Examples:
      | Login      | Password      | Name      | Contact   | ImageName                |
      | user1Email | user1Password | user1Name | user2Name | userpicture_portrait.jpg |

  @C226429 @like @regression
  Scenario Outline: Verify liking someone's audio message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And <Contact> sends audio file <File> via device Device1 to user Myself
    Then I wait until audio <File> is uploaded completely
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message

    Examples:
      | Login      | Password      | Name      | Contact   | File        |
      | user1Email | user1Password | user1Name | user2Name | example.m4a |

  @C226430 @regression
  Scenario Outline: Verify liking someone's video message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And <Contact> sends <Size> sized video with name <File> via device Device1 to user me
    Then I see video message <File> in the conversation view
    And I wait until video <File> is uploaded completely
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  |
      | user1Email | user1Password | user1Name | user2Name | C226430.mp4 | 15MB  |

  @C226431 @regression
  Scenario Outline: Verify liking someone's Soundcloud, youtube, vimeo and spotify
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    When Contact <Contact> sends message <Youtubelink> via device Device1 to user me
    Then I see embedded youtube message <Youtubelink>
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message
    When Contact <Contact> sends message <Soundcloudlink> via device Device1 to user me
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message
    When Contact <Contact> sends message <Vimeolink> via device Device1 to user me
    Then I see text message <Vimeolink>
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message
    When Contact <Contact> sends message <Spotifylink> via device Device1 to user me
    Then I see text message <Spotifylink>
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message

    Examples:
      | Login      | Password      | Name      | Contact   | Youtubelink                                 | Soundcloudlink                                                      | Vimeolink                  | Spotifylink                                           |
      | user1Email | user1Password | user1Name | user2Name | https://www.youtube.com/watch?v=ncHd3sxpEbo | https://soundcloud.com/nour-moukhtar/ludwig-van-beethoven-fur-elise | https://vimeo.com/51350323 | https://play.spotify.com/album/7buEcyw6fJF3WPgr06BomH |

  @C226433 @like @regression
  Scenario Outline: Verify liking someone's shared file
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    When <Contact> sends <Size> sized file with name <File> via device Device1 to user <Name>
    When I wait until placeholder for file <File> disappears
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message

    Examples:
      | Login      | Password      | Name      | Contact   | File       | Size |
      | user1Email | user1Password | user1Name | user2Name | C87933.txt | 15MB |

  @C226434 @like @regression
  Scenario Outline: Verify liking gif from GIPHY
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I am signed in properly
    When I open conversation with <Contact>
    When I write message <Message>
    And I click GIF button
    Then I see Giphy popup
    And I see gif image in Giphy popup
    When I click send button in Giphy popup
    Then I see sent gif in the conversation view
    And I verify the second last text message equals to <ExpectedMessage>
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message

    Examples:
      | Login      | Password      | Name      | Contact   | Message | ExpectedMessage     |
      | user1Email | user1Password | user1Name | user2Name | cat     | cat • via giphy.com |

  @C226435 @like @regression
  Scenario Outline: Verify liking someone's location
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And User <Contact> sends location <LocationName> with <Latitude> and <Longitude> to user Myself via device Device1
    Then I see location message <LocationName> with <Latitude> and <Longitude> in the conversation view
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message
    And I verify browser log does not have errors

    Examples:
      | Login      | Password      | Name      | Contact   | Latitude | Longitude | LocationName |
      | user1Email | user1Password | user1Name | user2Name | 12.94    | 54.29     | Stralsund    |

  @C234612 @like @regression
  Scenario Outline: Verify locally deleted message can be liked by others
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Message1> via device Device1 to user me
    Then I see text message <Message1>
    And I see 2 messages in conversation
    And I do not see likes below the last message
    When I click context menu of the last message
    And I click to delete message for me in context menu
    And I click confirm to delete message for me
    Then I do not see text message <Message1>
    And I see 1 messages in conversation
    When User <Contact> likes the recent message from user <Name> via device Device1
    Then I do not see likes below the last message
    And I see 1 messages in conversation
    And I verify browser log does not have errors

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 |
      | user1Email | user1Password | user1Name | user2Name | like me  |

  @C234614 @like @regression
  Scenario Outline: Verify message that is not in my history can be liked by others
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And Contact <Contact> sends message <Message1> via device Device1 to user me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    Then I do not see text message <Message1>
    And I see 1 messages in conversation
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see 1 messages in conversation
    And I verify browser log does not have errors

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 |
      | user1Email | user1Password | user1Name | user2Name | like me  |

  @C226443 @like @edit @regression
  Scenario Outline: Verify likes are reset if you edited message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I am signed in properly
    When I open conversation with <Contact>
    When I write message <Message1>
    And I send message
    Then I see text message <Message1>
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Edit
    When I click context menu of the last message
    And I click to edit message in context menu
    And I delete 7 characters from the conversation input
    And I write message <EditedMessage>
    And I send message
    Then I see text message <EditedMessage>
# Everything unliked
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message
    And I verify browser log does not have errors

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name | like me  | edited        |

  @C226444 @like @regression
  Scenario Outline: Verify I can see likers count instead of names (ex. 5 people)
    Given There are 6 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device2 with label Label2
    Given user <Contact3> adds a new device Device3 with label Label3
    Given user <Contact4> adds a new device Device4 with label Label4
    Given user <Contact5> adds a new device Device5 with label Label5
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And Contact <Contact1> sends message <Message> via device Device1 to group conversation <ChatName>
    Then I see text message <Message>
    When I click to like the last message without other likes
    And User <Contact1> likes the recent message from group conversation <ChatName> via device Device1
    And User <Contact2> likes the recent message from group conversation <ChatName> via device Device2
    And User <Contact3> likes the recent message from group conversation <ChatName> via device Device3
    And User <Contact4> likes the recent message from group conversation <ChatName> via device Device4
    Then I see likes below the last message
    And I see names in like string of last message
    When User <Contact5> likes the recent message from group conversation <ChatName> via device Device5
    And I see likes below the last message
    And I wait for 1 seconds
    Then I see count of 6 people in like string of last message
    When User <Contact1> unlikes the recent message from group conversation <ChatName> via device Device1
    And I see likes below the last message
    And I wait for 1 seconds
    And I see the last message is liked by users Myself,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Then I see names in like string of last message

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | ChatName | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | GROUP    | like me |

  @C226446 @like @regression
  Scenario Outline: Verify I can open like list by tapping on the number of people who liked
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device2 with label Label2
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And Contact <Contact1> sends message <Message> via device Device1 to group conversation <ChatName>
    Then I see text message <Message>
    When I click to like the last message without other likes
    And User <Contact1> likes the recent message from group conversation <ChatName> via device Device1
    And User <Contact2> likes the recent message from group conversation <ChatName> via device Device2
    Then I see likes below the last message
    And I see the last message is liked by users Myself,<Contact1>,<Contact2>
    When I open like list of the latest message
    Then I see like list of last message
    And I see 3 avatars in like list of last message
    When I close like list of last message
    Then I do not see like list of last message
    When I click to unlike the last message with other likes
    And I do not see like symbol for last message
    Then I see the last message is liked by users <Contact1>,<Contact2>
    When I open like list of the latest message
    And I see like list of last message
    Then I see 2 avatars in like list of last message
    And I close like list of last message
    And I do not see like list of last message

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GROUPCHAT | like me |

  @C226447 @like @regression
  Scenario Outline: Verify likes list is sorted by time, most recent liker is on the right
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device2 with label Label2
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And Contact <Contact1> sends message <Message> via device Device1 to group conversation <ChatName>
    Then I see text message <Message>
    When I click to like the last message without other likes
    And User <Contact2> likes the recent message from group conversation <ChatName> via device Device2
    Then I see likes below the last message
    And I see the last message is liked by users Myself,<Contact2>
    And I see <Contact2> is the most recent liker of last message
    When User <Contact1> likes the recent message from group conversation <ChatName> via device Device1
    And I see likes below the last message
    Then I see the last message is liked by users Myself,<Contact2>,<Contact1>
    And I see <Contact1> is the most recent liker of last message
    When User <Contact2> unlikes the recent message from group conversation <ChatName> via device Device2
    And I see likes below the last message
    And I see the last message is liked by users Myself,<Contact1>
    And User <Contact2> likes the recent message from group conversation <ChatName> via device Device2
    And I see likes below the last message
    Then I see the last message is liked by users Myself,<Contact1>,<Contact2>
    And I see <Contact2> is the most recent liker of last message

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GROUPCHAT | like me |

  @C234613 @like @regression
  Scenario Outline: Verify likes are reset if sender edits his message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Message1> via device Device1 to user me
    Then I see text message <Message1>
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Edit
    When User <Contact> edits the recent message to "<EditedMessage>" from user <Name> via device Device1
    Then I see text message <EditedMessage>
# Everything unliked
    And I do not see likes below the last message
# Only liked by me
    When I click to like the last message without other likes
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click to unlike the last message with other likes
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message
    And I verify browser log does not have errors

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name | like me  | edited        |

  @C226437 @like @regression
  Scenario Outline: Verify you cannot like a system message
    Given There are 3 user where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    Then I see YOU STARTED A CONVERSATION WITH action for <Contact1>,<Contact2> in conversation
    And I do not see like symbol for last message
    And I do not see likes below the last message
    When I write random message
    And I send message
    And I see random message in conversation
    Then I see like symbol for last message
    When I click People button in group conversation
    And I see Group Participants popover
    And I click on participant <Contact1> on Group Participants popover
    And I click Remove button on Group Participants popover
    And I confirm remove from group chat on Group Participants popover
    And I open conversation with <ChatName>
    Then I see YOU REMOVED action for <Contact1> in conversation
    And I do not see like symbol for last message
    And I do not see likes below the last message
    When I add <Contact1> to group chat
    Then I see YOU ADDED action for <Contact1> in conversation
    And I do not see like symbol for last message
    And I do not see likes below the last message
    When I click People button in group conversation
    And I see Group Participants popover
    And I change group conversation title to <NewName> on Group Participants popover
    And I click People button in group conversation
    And I see RENAMED action in conversation
    And I do not see like symbol for last message
    And I do not see likes below the last message
    When I call
    And I wait for 5 seconds
    And I hang up call with conversation <NewName>
    And I see CALLED action in conversation
    Then I do not see like symbol for last message
    And I do not see likes below the last message

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  | NewName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GROUPCHAT | NEWNAME |

  @C226439 @regression
  Scenario Outline: Verify you can like someone's message from message context menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Message1> via device Device1 to user me
    Then I see text message <Message1>
# No likes
    And I do not see likes below the last message
# Only liked by me
    When I click context menu of the last message
    And I click like button in context menu for last message
    And I do not see likes below the last message
    Then I see the last message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the last message
    And I see the last message is liked by others and me
# Only liked by others
    When I click context menu of the last message
    And I click unlike button in context menu for last message
    Then I see likes below the last message
    And I see the last message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the last message

      Examples:
        | Login      | Password      | Name      | Contact   | Message1 |
        | user1Email | user1Password | user1Name | user2Name | like me  |
