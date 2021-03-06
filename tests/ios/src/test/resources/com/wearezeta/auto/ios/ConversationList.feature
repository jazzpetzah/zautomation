Feature: Conversation List

  @C836 @rc @regression @fastLogin
  Scenario Outline: Unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given User Myself archives single user conversation <ArchivedUser>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open archived conversations
    And I tap on contact name <ArchivedUser>
    And I navigate back to conversations list
    Then I see first item in contact list named <ArchivedUser>

    Examples:
      | Name      | ArchivedUser |
      | user1Name | user2Name    |

  @C12 @regression @fastLogin
  Scenario Outline: Verify archiving silenced conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <Contact>
    And I tap Mute conversation action button
    When I swipe right on conversation <Contact>
    And I tap Archive conversation action button
    Then I do not see conversation <Contact> in conversations list
    Given User <Contact> sends 1 default message to conversation Myself
    And I do not see conversation <Contact> in conversations list
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    And I tap on contact name <Contact>
    And I see conversation view page

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C350 @regression @fastLogin
  Scenario Outline: Verify unread dots have different size for 1, 5, 10 incoming messages
    Given There are 2 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I navigate back to conversations list
    And I remember the state of <Contact> conversation item
    When User <Contact> sends 1 default message to conversation Myself
    Then I see the state of <Contact> conversation item is changed
    When I remember the state of <Contact> conversation item
    And User <Contact> sends 4 default messages to conversation Myself
    Then I see the state of <Contact> conversation item is changed
    When I remember the state of <Contact> conversation item
    Given User <Contact> sends 5 default messages to conversation Myself
    Then I see the state of <Contact> conversation item is changed

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C19 @regression @fastLogin
  Scenario Outline: Verify archive a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <GroupChatName>
    And I tap Archive conversation action button
    Then I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    Then I see conversation <GroupChatName> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @C20 @regression @fastLogin
  Scenario Outline: Verify unarchive a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User Myself archives group conversation <GroupChatName>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open archived conversations
    And I tap on contact name <GroupChatName>
    And I see conversation view page
    And I navigate back to conversations list
    Then I see first item in contact list named <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @C104 @rc @regression @fastLogin
  Scenario Outline: Verify conversations are sorted according to most recent activity
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Users add the following devices: {"<Contact1>": [{}], "<Contact2>": [{}], "<Contact3>": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact3> sends 1 default message to conversation Myself
    And I see first item in contact list named <Contact3>
    Given User <Contact2> pings conversation Myself
    And I see first item in contact list named <Contact2>
    Given User <Contact1> sends 1 image file <Picture> to conversation Myself
    Then I see first item in contact list named <Contact1>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Picture     |
      | user1Name | user2Name | user3name | user4name | testing.jpg |

  @C851 @regression @fastLogin
  Scenario Outline: Verify action menu is opened on swipe right on the group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <GroupChatName>
    Then I see actions menu for <GroupChatName> conversation
    And I see Mute conversation action button
    And I see Archive conversation action button
    And I see Delete conversation action button
    And I see Leave conversation action button
    And I see Cancel conversation action button

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3name | ActionMenuChat |

  @C852 @regression @fastLogin
  Scenario Outline: Verify action menu is opened on swipe right on 1to1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <Contact>
    Then I see actions menu for <Contact> conversation
    And I see Mute conversation action button
    And I see Archive conversation action button
    And I see Delete conversation action button
    And I see Block conversation action button
    And I see Cancel conversation action button

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C837 @rc @clumsy @regression @fastLogin
  Scenario Outline: Verify archiving from the action menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <Contact>
    And I see actions menu for <Contact> conversation
    And I see Archive conversation action button
    And I tap Archive conversation action button
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C848 @rc @clumsy @regression @fastLogin
  Scenario Outline: Verify leaving group conversation from the action menu
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <GroupChatName>
    And I see actions menu for <GroupChatName> conversation
    And I tap Leave conversation action button
    And I confirm Leave conversation action
    Then I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    And I see conversation <GroupChatName> in conversations list
    And I tap on group chat with name <GroupChatName>
    Then I see You Left message in group chat

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName   |
      | user1Name | user2Name | user3Name | LeaveActionMenu |

  @C840 @rc @clumsy @regression @fastLogin
  Scenario Outline: Verify removing the content from the group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Users add the following devices: {"<Contact1>": [{}], "Myself": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 image file <Picture> to conversation <GroupChatName>
    Given User <Contact1> sends 1 default message to conversation <GroupChatName>
    Given User Myself sends 1 default message to conversation <GroupChatName>
    When I tap on contact name <GroupChatName>
    Then I see 1 photo in the conversation view
    When I navigate back to conversations list
    And I swipe right on conversation <GroupChatName>
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<GroupChatName>" in Search UI input field
    And I tap on conversation <GroupChatName> in search result
    # Wait for transition
    And I wait for 3 seconds
    Then I see 0 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture     |
      | user1Name | user2Name | user3Name | TESTCHAT      | testing.jpg |

  @C842 @rc @clumsy @regression @fastLogin
  Scenario Outline: ZIOS-6809 Verify removing the history from 1-to1 conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Users add the following devices: {"<Contact1>": [{}], "Myself": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 image file <Picture> to conversation Myself
    Given User <Contact1> sends 1 default message to conversation <Name>
    Given User Myself sends 1 default message to conversation <Contact1>
    Given I wait until <Contact1> exists in backend search results
    When I tap on contact name <Contact1>
    Then I see 1 photo in the conversation view
    When I navigate back to conversations list
    And I swipe right on conversation <Contact1>
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    And I tap Open conversation action button on Search UI page
    Then I see conversation view page
    And I see 0 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | Picture     |
      | user1Name | user2Name | user3Name | testing.jpg |

  @C853 @regression @fastLogin
  Scenario Outline: Verify closing the action menu by clicking on cancel on out of the menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <Contact>
    Then I see actions menu for <Contact> conversation
    And I tap Cancel conversation action button
    Then I see conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C850 @regression @fastLogin
  Scenario Outline: Verify silencing and notify from the action menu
    Given There are 2 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I remember the state of <Contact> conversation item
    And I tap on contact name <Contact>
    And I navigate back to conversations list
    When I swipe right on conversation <Contact>
    And I tap Mute conversation action button
    Then I see the state of <Contact> conversation item is changed
    When I remember the state of <Contact> conversation item
    And I swipe right on conversation <Contact>
    And I tap Unmute conversation action button
    Then I see the state of <Contact> conversation item is changed

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C106 @regression @fastLogin
  Scenario Outline: Verify first conversation in the list is highlighted and opened
    Given There are 3 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I remember the state of conversation item number 1
    And I tap on conversation item number 2
    And I navigate back to conversations list
    Then I see the state of conversation item number 1 is not changed

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C843 @regression @fastLogin
  Scenario Outline: Verify that deleted conversation isn't going to archive
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Users add the following devices: {"<Contact1>": [{}], "Myself": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact1> sends 1 default message to conversation Myself
    Given User Myself sends 1 default message to conversation <Contact1>
    Given I see conversations list
    # Wait for sync
    Given I wait for 5 seconds
    When I swipe right on conversation <Contact1>
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <Contact1> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C844 @regression @fastLogin
  Scenario Outline: Verify deleting 1-to-1 conversation from archive
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <Contact1>
    And I tap Archive conversation action button
    And I do not see conversation <Contact1> in conversations list
    And I open archived conversations
    And I swipe right on conversation <Contact1>
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <Contact1> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C841 @regression @fastLogin
  Scenario Outline: Verify removing the content and leaving from the group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User adds the following device: {"Myself": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 1 default message to conversation <GroupChatName>
    When I swipe right on conversation <GroupChatName>
    And I tap Delete conversation action button
    And I tap Also Leave checkbox on Group info page
    And I confirm Delete conversation action
    # Wait for animation
    And I wait for 2 seconds
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<GroupChatName>" in Search UI input field
    Then I see the conversation "<GroupChatName>" does not exist in Search results
    When I tap X button on Search UI page
    And I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    Then I see conversation <GroupChatName> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C846 @rc @regression @fastLogin
  Scenario Outline: Verify posting in a group conversation without content
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Users add the following devices: {"<Contact1>": [{}], "Myself": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself pings conversation <GroupChatName>
    Given User Myself sends 1 default message to conversation <GroupChatName>
    Given User Myself sends 1 image file <Picture> to conversation <GroupChatName>
    Given User <Contact1> sends 1 default message to conversation <GroupChatName>
    When I swipe right on conversation <GroupChatName>
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <GroupChatName> in conversations list
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<GroupChatName>" in Search UI input field
    And I tap on conversation <GroupChatName> in search result
    # Wait for animation
    And I wait for 2 seconds
    Then I see 0 conversation entries
    When I type the default message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture     |
      | user1Name | user2Name | user3Name | ForDeletion   | testing.jpg |

  @C847 @regression @fastLogin
  Scenario Outline: Verify deleting the history from kicked out conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> removes Myself from group chat <GroupChatName>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <GroupChatName>
    And I see Archive conversation action button
    And I see Cancel conversation action button
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <GroupChatName> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | KICKCHAT      |

  @C839 @regression @fastLogin
  Scenario Outline: Verify canceling blocking person
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <Contact1>
    And I tap Block conversation action button
    And I tap Cancel conversation action button
    Then I see actions menu for <Contact1> conversation

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C838 @regression @fastLogin
  Scenario Outline: Verify blocking person from action menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on conversation <Contact>
    And I tap Block conversation action button
    And I confirm Block conversation action
    Then I do not see conversation <Contact> in conversations list
    And I do not see Archive button at the bottom of conversations list
    And I wait until <Contact> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C366 @rc @regression @fastLogin
  Scenario Outline: Verify messages are marked as read with disappearing unread dot
    Given There are 2 users where <Name> is me
    Given User Myself removes his avatar picture
    Given <Contact> is connected to <Name>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends 1 default message to conversation Myself
    When I remember the state of <Contact> conversation item
    And I tap on contact name <Contact>
    And I see conversation view page
    And I navigate back to conversations list
    Then I see the state of <Contact> conversation item is changed

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C854 @regression @fastLogin
  Scenario Outline: Verify action menu is opened on swipe right on outgoing connection request
    Given There are 2 users where <Name> is me
    Given Myself sent connection request to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I see first item in contact list named <Contact>
    When I swipe right on conversation <Contact>
    Then I see actions menu for <Contact> conversation
    And I see Archive conversation action button
    And I see Cancel conversation action button

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C111308 @regression @forceReset
  Scenario Outline: Verify share contacts dialogue is shown each time on invite more friends click
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I see sign in screen
    Given I switch to Log In tab
    Given I have entered login <Login>
    Given I have entered password <Password>
    Given I tap Login button
    Given I dismiss alert
    Given I accept First Time overlay
    Given I dismiss settings warning if visible
    Given I see conversations list
    When I open search UI
    And I see alert contains text <AlertText>
    And I dismiss alert
    And I tap Send Invite button on Search UI page
    Then I see Share Contacts settings warning
    When I dismiss settings warning
    And I discard my choice
    And I tap Send Invite button on Search UI page
    Then I see Share Contacts settings warning

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | AlertText            |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Access Your Contacts |

  @C95627 @regression @fastLogin
  Scenario Outline: Verify deleting a conversation is synchronised to all devices
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Users add the following devices: {"Myself": [{"name": "<DeviceName>"}], "<Contact1>": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I see conversations list
    Given I see conversation <Contact1> in conversations list
    When User Myself deletes single user conversation <Contact1> using device <DeviceName>
    # Let the stuff to sync up
    Then I wait up to <Timeout> seconds until conversation <Contact1> disappears from the list

    Examples:
      | Name      | Contact1  | DeviceName | Timeout |
      | user1Name | user2Name | device1    | 15      |

  @C95634 @regression @fastLogin
  Scenario Outline: Verify hint is not shown anymore after tapping on it once
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I see Conversations hint text
    When I tap on Conversations hint text
    And I accept alert if visible
    Then I see Search UI
    When I tap X button on Search UI page
    Then I see conversations list
    And I do not see Conversations hint text

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
