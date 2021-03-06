Feature: Video Message

  @C123927 @videomessage @regression @localytics
  Scenario Outline: Verify sender can play video message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized video with name <File> to the current conversation
    And I wait until video <File> is uploaded completely
    And I click play button of video <File> in the conversation view
    Then I wait until video <File> is downloaded and starts to play
    And I verify time for video <File> is changing in the conversation view
    And I verify seek bar is shown for video <File> in the conversation view
    And I see localytics event <Event> with attributes <Attributes>

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  | Event                        | Attributes                                                                                            |
      | user1Email | user1Password | user1Name | user2Name | C123927.mp4 | 20MB  | media.completed_media_action | {\"action\":\"file\",\"conversation_type\":\"one_to_one\",\"is_ephemeral\":false,\"with_bot\":false}" |

  @C123938 @videomessage @regression
  Scenario Outline: Verify user can delete video message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized video with name <File> to the current conversation
    And I wait until video <File> is uploaded completely
    And I click context menu of the last message
    And I click to delete message for me in context menu
    And I click confirm to delete message for me
    Then I do not see video message <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size |
      | user1Email | user1Password | user1Name | user2Name | C123938.mp4 | 5MB  |

  @C123926 @videomessage @regression
  Scenario Outline: Verify receiver can play video message in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I see Contact list with name <Contact>
    And I open conversation with <Contact>
    And <Contact> sends <Size> sized video with name <File> via device Device1 to user <Name>
    Then I see video message <File> in the conversation view
    And I wait until video <File> is uploaded completely
    When I click play button of video <File> in the conversation view
    Then I wait until video <File> is downloaded and starts to play
    And I verify seek bar is shown for video <File> in the conversation view
    And I verify time for video <File> is changing in the conversation view
    When I click pause button of video <File> in the conversation view
    Then I see play button of video <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  |
      | user1Email | user1Password | user1Name | user2Name | C123926.mp4 | 15MB  |

  @C123939 @videomessage @regression
  Scenario Outline: Verify receivers can play video message in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    And <Contact1> sends <Size> sized video with name <File> via device Device1 to group conversation <ChatName>
    Then I see video message <File> in the conversation view
    And I wait until video <File> is uploaded completely
    When I click play button of video <File> in the conversation view
    Then I wait until video <File> is downloaded and starts to play
    And I verify time for video <File> is changing in the conversation view
    And I verify seek bar is shown for video <File> in the conversation view
    When I click pause button of video <File> in the conversation view
    Then I see play button of video <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | File        | ChatName  | Size  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | C123939.mp4 | GroupChat | 15MB  |

  @C123929 @videomessage @regression
  Scenario Outline: Verify sender can cancel video message upload
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    And I see file transfer button in conversation input
    And I send <Size> sized video with name <File> to the current conversation
    And I see cancel upload button for video <File>
    Then I cancel video upload of video <File>
    And I do not see video message <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size |
      | user1Email | user1Password | user1Name | user2Name | C123929.mp4 | 23MB |

  @C123928 @videomessage @mute
  Scenario Outline: Verify receiver can cancel video message download
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    And <Contact1> sends <Size> sized video with name <File> via device Device1 to group conversation <ChatName>
    Then I see video message <File> in the conversation view
    And I wait until video <File> is uploaded completely
    And I see play button of video <File> in the conversation view
    When I click play button of video <File> in the conversation view
    Then I cancel video download of video <File>
    And I see play button of video <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | File        | ChatName  | Size  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | C123928.mp4 | GroupChat | 18MB  |