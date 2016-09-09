Feature: Likes

  @C225979 @C225994 @regression @fastLogin
  Scenario Outline: Verify liking/unliking a message by tapping on like icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap default message in conversation view
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C225980 @C225995 @regression @fastLogin
  Scenario Outline: Verify liking/unliking a message from a message menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap default message in conversation view
    And I tap on Like badge item
    When I remember the state of Like icon in the conversation
    And I long tap default message in conversation view
    And I tap on Unlike badge item
    Then I see the state of Like icon is changed in the conversation
    And I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C226008 @regression @fastLogin
  Scenario Outline: Verify impossibility of liking the message after leaving (being removed) from a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <Group>
    Given I tap on contact name <Group>
    When <Contact1> removes Myself from group chat <Group>
    And I tap default message in conversation view
    Then I do not see Like icon in the conversation
    When I double tap default message in conversation view
    Then I do not see Like icon in the conversation
    When I long tap default message in conversation view
    Then I do not see Like badge item

    Examples:
      | Name      | Contact1  | Contact2  | Group            |
      | user1Name | user2Name | user3Name | RemovedFromGroup |

  @C225993 @regression @fastLogin
  Scenario Outline: Verify liking a message tapping on like icon, when someone liked this message before
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 1 encrypted message to user <Contact>
    Given I see conversations list
    Given User <Contact> likes the recent message from user Myself
    Given I tap on contact name <Contact>
    When I tap default message in conversation view
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C225999 @regression @fastLogin
  Scenario Outline: Verify deleted for myself my message doesn't reappear after someone liked it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 1 encrypted message to user <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I see 0 default messages in the conversation view
    When User <Contact> likes the recent message from user Myself
    Then I see 0 default messages in the conversation view
    And I do not see Like icon in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C225998 @C226001 @regression @fastLogin
  Scenario Outline: Verify editing already liked message and like after edit
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 1 encrypted message to user <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap default message in conversation view
    And I tap Like icon in the conversation
    And I remember the state of Like icon in the conversation
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "<Text>" message
    And I tap Confirm button on Edit control
    Then I see last message in the conversation view is expected message <Text>
    And I do not see Like icon in the conversation
    When I tap "<Text>" message in conversation view
    And I tap Like icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   | Text  |
      | user1Name | user2Name | aloha |

  @C226004 @regression @fastLogin
  Scenario Outline: Verify receiving a like in a conversation which was removed
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends encrypted image <Picture> to group conversation <Group>
    When I swipe right on a <Group>
    And I tap Delete action button
    And I confirm delete conversation content
    And User <Contact1> likes the recent message from group conversation <Group>
    Then I do not see conversation <Group> in conversations list
    When I open search UI
    And I input in People picker search field conversation name <Group>
    And I tap on conversation <Group> in search result
    Then I see 0 photos in the conversation view
    And I do not see Like icon in the conversation

    Examples:
      | Name      | Contact1  | Contact2  | Picture     | Group        |
      | user1Name | user2Name | user3Name | testing.jpg | DeletedGroup |

  @C226005 @regression @fastLogin
  Scenario Outline: Verify receiving like from a blocked person
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given User <Contact1> adds a new device <Contact1Device> with label <Contact1DeviceLabel>
    Given User Myself blocks user <Contact1>
    Given I sign in using my email or phone number
    Given User Myself sends file <FileName> having MIME type <FileMIME> to group conversation <Group> using device <MyDevice>
    Given I see conversations list
    Given I tap on contact name <Group>
    When I do not see Like icon in the conversation
    And User <Contact1> likes the recent message from group conversation <Group>
    And I tap toolbox of the recent message
    Then I see <Contact1> in likers list

    Examples:
      | Name      | Contact1  | Contact2  | Group            | FileName | FileMIME  | Contact1Device | Contact1DeviceLabel | MyDevice |
      | user1Name | user2Name | user3Name | BlockedContGroup | test.m4a | audio/mp4 | C1Device       | C1DeviceLabel       | MyDev    |

  @C225987 @regression @fastLogin
  Scenario Outline: Verify liking a shared file
    Given There are 3 users where <Name> is me
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User <Contact1> sends temporary file <FileName>.<FileExt> having MIME type <FileMIME> to group conversation <Group> using device <Contact1Device>
    Given I see conversations list
    Given I tap on contact name <Group>
    When I do not see Like icon in the conversation
    And I long tap on file transfer placeholder in conversation view
    And I tap on Like badge item
    And I tap toolbox of the recent message
    Then I see Myself in likers list

    Examples:
      | Name      | Contact1  | Contact2  | Group         | FileName | FileExt | FileSize | FileMIME                 | Contact1Device |
      | user1Name | user2Name | user3Name | FileLikeGroup | testing  | tmp     | 240 KB   | application/octet-stream | C1Device       |

  @C225984 @regression @fastLogin
  Scenario Outline: Verify liking a video message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User <Contact1> sends file <FileName> having MIME type <MIMEType> to group conversation <Group> using device <Contact1Device>
    Given I see conversations list
    Given I tap on contact name <Group>
    When I do not see Like icon in the conversation
    And I long tap on video message in conversation view
    And I tap on Like badge item
    And I tap toolbox of the recent message
    Then I see Myself in likers list

    Examples:
      | Name      | Contact1  | Contact2  | Group          | FileName    | MIMEType  | Contact1Device |
      | user1Name | user2Name | user3Name | VideoLikeGroup | testing.mp4 | video/mp4 | C1Device       |

  @C225985 @regression @fastLogin
  Scenario Outline: Verify liking Soundcloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted message "<SCLink>" to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap at 90% of width and 90% of height of the recent message from <Contact>
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   | SCLink                                                           |
      | user1Name | user2Name | https://soundcloud.com/trevorjasper14/lateef-two-birds-one-stone |
      | user1Name | user2Name | user3Name | BlockedContGroup | test.m4a | audio/mp4 | C1Device       | C1DeviceLabel       | MyDev    |

  @C225992 @C225996 @staging @fastLogin @torun
  Scenario Outline: Verify liking/unliking a message by double tapping
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap default message in conversation view
    And I remember the state of Like icon in the conversation
    When I double tap default message in conversation view
    Then I see the state of Like icon is changed in the conversation
    And I double tap default message in conversation view
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

