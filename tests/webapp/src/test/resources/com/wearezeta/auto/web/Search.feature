Feature: Search

  @C352243 @smoke
  Scenario Outline: Verify search by username for unconnected user
    Given There are 2 users where <Name> is me
    Given <Contact> has unique username
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I wait until <Contact> exists in backend search results
    Given I am signed in properly
    When I open search by clicking the people button
    And I see Search is opened
    And I see Bring Your Friends or Invite People button
    And I type <ContactUniqueUsername> in search field of People Picker
    Then I see user <Contact> found in People Picker
    When I clear the search field of People Picker
    Then I do not see user <Contact> found in People Picker
    When I type <ContactUniqueUsername> in search field of People Picker in uppercase
    Then I see user <Contact> found in People Picker

    Examples:
      | Login      | Password      | Name      | Contact   | ContactUniqueUsername |
      | user1Email | user1Password | user1Name | user2Name | user2UniqueUsername   |

  @C399360 @regression
  Scenario Outline: Verify search by username for connected user
    Given There are 2 users where <Name> is me
    Given <Contact> has unique username
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I wait until <Contact> exists in backend search results
    Given I am signed in properly
    When I open search by clicking the people button
    And I see Search is opened
    And I see Bring Your Friends or Invite People button
    And I type <Contact> in search field of People Picker
    Then I see user <Contact> with username <ContactUniqueUsername> found in People Picker
    When I clear the search field of People Picker
    Then I do not see user <Contact> found in People Picker
    When I type <ContactUniqueUsername> in search field of People Picker
    Then I see user <Contact> with username <ContactUniqueUsername> found in People Picker

    Examples:
      | Login      | Password      | Name      | Contact   | ContactUniqueUsername |
      | user1Email | user1Password | user1Name | user2Name | user2UniqueUsername   |

  @C352244 @smoke
  Scenario Outline: Verify you can search by partial username of unconnected user
    Given There are 2 users where <Name> is me
    Given <Contact> has unique username
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I wait until <Contact> exists in backend search results
    Given I am signed in properly
    When I open search by clicking the people button
    And I see Search is opened
    And I see Bring Your Friends or Invite People button
    When I type <ContactUniqueUsername> in search field of People Picker only partially
    Then I see user <Contact> found in People Picker

    Examples:
      | Login      | Password      | Name      | Contact   | ContactUniqueUsername |
      | user1Email | user1Password | user1Name | user2Name | user2UniqueUsername   |

  @C1711 @smoke
  Scenario Outline: Start group chat with users from contact list
    Given There are 3 users where <Name> is me
    Given User <Contact1> changes unique username to <Contact1>
    Given User <Contact2> changes unique username to <Contact2>
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I wait until <Contact1> exists in backend search results
    And I wait until <Contact2> exists in backend search results
    When I open search by clicking the people button
    And I type <Contact1> in search field of People Picker
    And I select <Contact1> from People Picker results
    And I wait for the search field of People Picker to be empty
    And I type <Contact2> in search field of People Picker
    And I select <Contact2> from People Picker results
    And I choose to create conversation from People Picker
    And I am signed in properly
    Then I see Contact list with name <Contact1>,<Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @C1723 @regression
  Scenario Outline: Verify the new conversation is created on the other end from Search UI
    Given There are 3 users where <Name> is me
    Given User <Contact1> changes unique username to <Contact1>
    Given User <Contact2> changes unique username to <Contact2>
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open search by clicking the people button
    And I wait until <Contact1> exists in backend search results
    And I type <Contact1> in search field of People Picker
    And I select <Contact1> from People Picker results
    And I wait until <Contact2> exists in backend search results
    And I wait for the search field of People Picker to be empty
    And I type <Contact2> in search field of People Picker
    And I select <Contact2> from People Picker results
    And I choose to create conversation from People Picker
    And I am signed in properly
    Then I see Contact list with name <Contact1>,<Contact2>
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And User <Contact1> is me
    And I see Sign In page
    And I Sign in using login <Contact1Email> and password <Contact1Password>
    And I am signed in properly
    Then I see Contact list with name <Name>,<Contact2>
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And User <Contact2> is me
    And I see Sign In page
    And I Sign in using login <Contact2Email> and password <Contact2Password>
    And I am signed in properly
    Then I see Contact list with name <Name>,<Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact1Email | Contact1Password | Contact2  | Contact2Email | Contact2Password |
      | user1Email | user1Password | user1Name | user2Name | user2Email    | user2Password    | user3Name | user3Email    | user3Password    |

  @C1698 @regression
  Scenario Outline: Verify you can unblock someone from search list
    Given There are 2 users where <Name> is me
    Given User <Contact> changes unique username to <Contact>
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I wait until <Contact> exists in backend search results
    And I open conversation with <Contact>
    And I click People button in one to one conversation
    Then I see Single User Profile popover
    And I see Block button on Single User Profile popover
    When I click Block button on Single User Profile popover
    And I confirm user blocking on Single User Profile popover
    Then I do not see Contact list with name <Contact>
    Then I do not see Single User Profile popover
    When I open search by clicking the people button
    And I type <ContactUniqueUsername> in search field of People Picker
    And I select <Contact> from People Picker results
    And I see Unblock button on Single User Profile popover
    When I click Unblock button on Single User popover
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | ContactUniqueUsername |
      | user1Email | user1Password | user1Name | user2Name | user2UniqueUsername   |

  @C1722 @regression
  Scenario Outline: Verify you can add new user from search results from the other end
    Given There are 2 users where <Name> is me
    Given User <Name2> changes unique username to <Name2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I wait until <Name2> exists in backend search results
    And I am signed in properly
    And I open search by clicking the people button
    And I see Search is opened
    And I type <Name2> in search field of People Picker
    Then I see user <Name2> found in People Picker
    When I click on not connected user <Name2> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    Then I see conversation with <Name2> is selected in conversations list
    And I see cancel pending request button in the conversation view
    And I verify that conversation input and buttons are not visible
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And User <Name2> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I am signed in properly
    And I see connection request from one user
    And I open the list of incoming connection requests
    When I accept connection request from user <Name>
    Then I see conversation with <Name> is selected in conversations list
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And User <Name> is me
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I see Contact list with name <Name2>
    And I open conversation with <Name2>

    Examples: 
      | Login      | Password      | Name      | Name2     | Login2     | Password2     |
      | user1Email | user1Password | user1Name | user2Name | user2Email | user2Password |

  @C1725 @smoke
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Contact Me sends message <Message1> to user <Contact1>
    Given Contact <Contact1> sends message <Message1> to user <Name>
    Given Contact Me sends message <Message1> to user <Contact2>
    Given Contact <Contact2> sends message <Message1> to user <Name>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    When I am signed in properly
    And Myself waits until 2 people in backend top people results
    And I open search by clicking the people button
    And I wait till Top People list appears
    When I select <Contact1> from Top People
    And I choose to create conversation from People Picker
    Then I see conversation with <Contact1> is selected in conversations list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Message1 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Message1 |

  @C1724 @smoke
  Scenario Outline: Verify you can create a group conversation from Top People list
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Contact Me sends message <Message1> to user <Contact1>
    Given Contact <Contact1> sends message <Message1> to user <Name>
    Given Contact Me sends message <Message1> to user <Contact2>
    Given Contact <Contact2> sends message <Message1> to user <Name>
    Given Contact Me sends message <Message1> to user <Contact3>
    Given Contact <Contact3> sends message <Message1> to user <Name>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    When I am signed in properly
    And Myself waits until 3 people in backend top people results
    And I open search by clicking the people button
    When I select <Contact1>,<Contact2> from Top People
    And I choose to create conversation from People Picker
    Then I see Contact list with name <Contact1>,<Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Message1 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | Message1 |

  @C1802 @smoke
  Scenario Outline: Verify you can not search by email
    Given There are 2 users where <Name> is me
    Given User <Name2> changes unique username to <Name2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I wait until <Name2> exists in backend search results
    Given I am signed in properly
    When I open search by clicking the people button
    When I see Search is opened
    And I see Bring Your Friends or Invite People button
    And I type <Email2> in search field of People Picker
    Then I do not see user <Name2> found in People Picker

    Examples: 
      | Login      | Password      | Name      | Name2     | Email2     |
      | user1Email | user1Password | user1Name | user2Name | user2Email |
    
  @C1818 @regression
  Scenario Outline: Verify I can start a 1:1 call with search ui buttons
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given <Contact1> has unique username
    Given Myself is connected to <Contact1>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I wait until <Contact1> exists in backend search results
    When I open search by clicking the people button
    And I type <Contact1> in search field of People Picker
    And I see user <Contact1> found in People Picker
    And I select <Contact1> from People Picker results
    And I click Call button on People Picker page
    And I see the outgoing call controls for conversation <Contact1>
    When I hang up call with conversation <Contact1>
    Then I do not see the call controls for conversation <Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |

  @C1819 @regression
  Scenario Outline: Verify I can start a group call with search ui buttons
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given User <Contact1> changes unique username to <Contact1>
    Given User <Contact2> changes unique username to <Contact2>
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I wait until <Contact1> exists in backend search results
    And I wait until <Contact2> exists in backend search results
    When I open search by clicking the people button
    And I type <Contact1> in search field of People Picker
    And I see user <Contact1> found in People Picker
    And I select <Contact1> from People Picker results
    And I type <Contact2> in search field of People Picker
    And I see user <Contact2> found in People Picker
    And I select <Contact2> from People Picker results
    And I click Call button on People Picker page
    And I see the outgoing call controls for conversation <Contact1>,<Contact2>
    When I hang up call with conversation <Contact1>,<Contact2>
    Then I do not see the call controls for conversation <Contact1>,<Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @C261732 @regression
  Scenario Outline: Verify you can open a group conversation from search
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I see Contact list with name <ChatName>
    And I open conversation with <Contact1>
    And I open search by clicking the people button
    And I see Search is opened
    And I type <Contact1> in search field of People Picker
    Then I see user <Contact1> found in People Picker
    And I see group conversation <ChatName> found in People Picker
    When I select group <ChatName> from People Picker results
    Then I see conversation with <ChatName> is selected in conversations list
    When I open conversation with <Contact1>
    And I open search by clicking the people button
    And I see Search is opened
    And I type <ChatName> in search field of People Picker
    Then I see group conversation <ChatName> found in People Picker
    When I select group <ChatName> from People Picker results
    Then I see conversation with <ChatName> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | OPEN_ME  |

  @C352082 @regression
  Scenario Outline: Verify search for unconnected user, with the common friends
    Given There are 6 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given <UnknownContact1> has unique username
    Given <UnknownContact1> is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I wait until <UnknownContact1> has 4 common friends on the backend
    Given I am signed in properly
    When I open search by clicking the people button
    And I see Search is opened
    And I see Bring Your Friends or Invite People button
    And I type <UnknownContact1> in search field of People Picker
    Then I see user <UnknownContact1> with username <ContactUniqueUsername> found in People Picker
    When I clear the search field of People Picker
    Then I do not see user <UnknownContact1> found in People Picker
    When I type <ContactUniqueUsername> in search field of People Picker
    Then I see user <UnknownContact1> with username <ContactUniqueUsername> found in People Picker
    And I see 4 common friends on search list for user <UnknownContact1> found in People Picker

    Examples:
      | Login      | Password      | Name      | UnknownContact1 | ContactUniqueUsername | Contact1  | Contact2  | Contact3  | Contact4  |
      | user1Email | user1Password | user1Name | user2Name       | user2UniqueUsername   | user3Name | user4Name | user5Name | user6Name |

  @C399359 @regression
  Scenario Outline: Verify search by username with at (@) symbol
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open search by clicking the people button
    And I see Search is opened
    And I type <SearchNameAlias> in search field of People Picker
    Then I see user <SearchNameAlias> with username <SearchUsername> found in People Picker
    When I clear the search field of People Picker
    Then I do not see user <SearchNameAlias> found in People Picker
    When I type @<SearchUsername> in search field of People Picker
    Then I see user <SearchNameAlias> with username <SearchUsername> found in People Picker

    Examples:
      | Login      | Password      | Name      | SearchNameAlias | SearchUsername  |
      | user1Email | user1Password | user1Name | nameAlias       | unique_username |