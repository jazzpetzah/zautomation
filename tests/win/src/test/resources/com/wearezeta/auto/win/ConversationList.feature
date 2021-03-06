Feature: Conversation List

  @C2301 @smoke
  Scenario Outline: Verify I can block user from conversation list with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I open context menu of conversation <Contact>
    And I click block in context menu
    Then I see a block warning modal

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2302 @smoke
  Scenario Outline: Verify I can leave group conversation list with right click
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    And I open context menu of conversation <ChatName>
    And I click leave in context menu
    Then I see a leave warning modal

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2307 @smoke
  Scenario Outline: Mute and unmute 1:1 conversation with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I open context menu of conversation <Contact>
    And I click silence in context menu
    Then I see that conversation <Contact> is muted
    When I open context menu of conversation <Contact>
    And I click notify in context menu
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2308 @smoke
  Scenario Outline: Mute and unmute 1:1 conversation with menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Mute"
    Then I see that conversation <Contact> is muted
    When I click menu bar item "Conversation" and menu item "Mute"
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2342 @smoke
  Scenario Outline: Mute and unmute 1:1 conversation with keyboard shortcut
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I type shortcut combination to mute or unmute a conversation
    And I wait for 2 seconds
    Then I see that conversation <Contact> is muted
    When I type shortcut combination to mute or unmute a conversation
    And I wait for 2 seconds
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2305 @smoke
  Scenario Outline: Mute and unmute group conversation with right click
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I open context menu of conversation <ChatName>
    And I click silence in context menu
    Then I see that conversation <ChatName> is muted
    When I open context menu of conversation <ChatName>
    And I click notify in context menu
    Then I see that conversation <ChatName> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2306 @smoke
  Scenario Outline: Mute and unmute group conversation with menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Mute"
    Then I see that conversation <ChatName> is muted
    When I click menu bar item "Conversation" and menu item "Mute"
    Then I see that conversation <ChatName> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2343 @smoke
  Scenario Outline: Mute and unmute group conversation with keyboard shortcut
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I type shortcut combination to mute or unmute a conversation
    And I wait for 2 seconds
    Then I see that conversation <ChatName> is muted
    When I type shortcut combination to mute or unmute a conversation
    And I wait for 2 seconds
    Then I see that conversation <ChatName> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2303 @smoke
  Scenario Outline: Delete a 1:1 conversation with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I open context menu of conversation <Contact>
    And I click delete in context menu
    Then I see a delete warning modal for 1:1 conversations

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2350 @smoke
  Scenario Outline: Delete a 1:1 conversation with menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Delete"
    Then I see a delete warning modal for 1:1 conversations

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2304 @smoke
  Scenario Outline: Delete a group conversation with right click
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I open context menu of conversation <ChatName>
    And I click delete in context menu
    Then I see a delete warning modal for group conversations

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2351 @smoke
  Scenario Outline: Delete a group conversation with menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Delete"
    Then I see a delete warning modal for group conversations

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2300 @smoke
  Scenario Outline: Archive a 1:1 conversation with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I open context menu of conversation <Contact>
    And I click archive in context menu
    Then I do not see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2353 @smoke
  Scenario Outline: Archive a 1:1 conversation with menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Archive"
    Then I do not see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2354 @smoke
  Scenario Outline: Archive a 1:1 conversation with keyboard shortcut
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I type shortcut combination to archive a conversation
    And I wait for 2 seconds
    Then I do not see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2352 @smoke
  Scenario Outline: Archive a group conversation with right click
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I open context menu of conversation <ChatName>
    And I click archive in context menu
    Then I do not see Contact list with name <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2355 @smoke
  Scenario Outline: Archive a group conversation with menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Archive"
    Then I do not see Contact list with name <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2356 @smoke
  Scenario Outline: Archive a group conversation with keyboard shortcut
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I type shortcut combination to archive a conversation
    And I wait for 2 seconds
    Then I do not see Contact list with name <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2357 @smoke
  Scenario Outline: Verify I can start a conversation with menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I click menu bar item "Conversation" and menu item "Start"
    Then I see Search is opened

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

   @C2332 @smoke
   Scenario Outline: Verify Start (Search) is opened when you press Ctrl N
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I type shortcut combination to open search
    Then I see Search is opened

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C3147 @staging @WEBAPP-3079
  Scenario Outline: Verify switching to next and previous conversation using shortcuts Ctrl Alt Up and Ctrl Alt Down
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I verify active conversation is at index 1
    When I type shortcut combination for next conversation
    And I wait for 1 seconds
    And I verify active conversation is at index 1
    When I type shortcut combination for previous conversation
    And I wait for 1 seconds
    Then I verify active conversation is at index 2
    When I type shortcut combination for previous conversation
    And I wait for 1 seconds
    Then I verify active conversation is at index 3
    When I type shortcut combination for previous conversation
    And I wait for 1 seconds
    Then I verify active conversation is at index 3
    When I type shortcut combination for next conversation
    And I wait for 1 seconds
    And I verify active conversation is at index 2

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | Contact3  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | user4Name | GroupChat |

  @C3148 @smoke
  Scenario Outline: Verify switching to next and previous conversation using menu bar
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I verify active conversation is at index 1
    When I click menu bar item "View" and menu item "Next Conversation"
    And I verify active conversation is at index 1
    When I click menu bar item "View" and menu item "Previous Conversation"
    Then I verify active conversation is at index 2
    When I click menu bar item "View" and menu item "Previous Conversation"
    Then I verify active conversation is at index 3
    When I click menu bar item "View" and menu item "Previous Conversation"
    Then I verify active conversation is at index 3

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | Contact3  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | user4Name | GroupChat |
