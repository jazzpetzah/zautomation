Feature: Search

  @staging @id2147
  Scenario Outline: Verify search by email [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open search by clicking plus button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see user <ContactName> found on People picker page

    Examples: 
      | Login      | Password      | Name      | ContactEmail | ContactName |
      | user1Email | user1Password | user1Name | user2Email   | user2Name   |

  @staging @id2147
  Scenario Outline: Verify search by email [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open search by clicking plus button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see user <ContactName> found on People picker page

    Examples: 
      | Login      | Password      | Name      | ContactEmail | ContactName |
      | user1Email | user1Password | user1Name | user2Email   | user2Name   |

  @staging @id2148
  Scenario Outline: Verify search by name [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I swipe down contact list on iPad
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id2148
  Scenario Outline: Verify search by name [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I swipe down contact list on iPad
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id2531 @noAcceptAlert
  Scenario Outline: Verify denying address book uploading [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I dismiss alert
    And I swipe down contact list on iPad
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I dismiss alert
    And I press maybe later button
    And I click clear button
    And I swipe down contact list on iPad
    And I click hide keyboard button
    Then I dont see Upload contacts dialog

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id2531 @noAcceptAlert
  Scenario Outline: Verify denying address book uploading [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    And I dismiss alert
    And I swipe down contact list on iPad
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I dismiss alert
    And I press maybe later button
    And I click clear button
    And I swipe down contact list on iPad
    And I click hide keyboard button
    Then I dont see Upload contacts dialog

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id2656
  Scenario Outline: Start 1:1 chat with users from Top Connections [PORTRAIT]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I wait for 30 seconds
    And I open search by clicking plus button
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on 1 top connections
    #And I click Go button to create 1:1 conversation
    And I click Create Conversation button on People picker page
    And I wait for 2 seconds
    And I see dialog page

    Examples: 
      | Login      | Password      | Name      | UserCount | Contact   |
      | user1Email | user1Password | user1Name | 2         | user2Name |

  @staging @id2656
  Scenario Outline: Start 1:1 chat with users from Top Connections [LANDSCAPE]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I wait for 30 seconds
    And I open search by clicking plus button
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on 1 top connections
    #And I click Go button to create 1:1 conversation
    And I click Create Conversation button on People picker page
    And I wait for 2 seconds
    And I see dialog page

    Examples: 
      | Login      | Password      | Name      | UserCount | Contact   |
      | user1Email | user1Password | user1Name | 2         | user2Name |

  @staging @id2550
  Scenario Outline: Start group chat with users from Top Connections [PORTRAIT]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    #And I wait for 30 seconds
    And I open search by clicking plus button
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on 2 top connections
    And I click Create Conversation button on People picker page
    And I wait for 2 seconds
    And I open group conversation details
    And I change group conversation name to <ConvoName>
    And I dismiss popover on iPad
    And I return to the chat list
    Then I see first item in contact list named <ConvoName>

    Examples: 
      | Login      | Password      | Name      | ConvoName    | UserCount | Contact   |
      | user1Email | user1Password | user1Name | TopGroupTest | 3         | user2Name |

  @staging @id2550
  Scenario Outline: Start group chat with users from Top Connections [LANDSCAPE]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    #And I wait for 30 seconds
    And I open search by clicking plus button
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 2 top connections
    And I click hide keyboard button
    And I click Create Conversation button on People picker page
    And I wait for 2 seconds
    And I open group conversation details
    And I change group conversation name to <ConvoName>
    And I dismiss popover on iPad
    And I return to the chat list
    And I see first item in contact list named <ConvoName>

    Examples: 
      | Login      | Password      | Name      | ConvoName    | UserCount | Contact   |
      | user1Email | user1Password | user1Name | TopGroupTest | 3         | user2Name |

  @staging @id1456 
  Scenario Outline: Verify you can unblock someone from search list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I Sign in using phone number or login <Login> and password <Password>
    When I dont see conversation <Contact> in contact list
    And I wait until <Contact> exists in backend search results
    And I open search by clicking plus button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I unblock user
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id1456
  Scenario Outline: Verify you can unblock someone from search list [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    When I dont see conversation <Contact> in contact list
    And I wait until <Contact> exists in backend search results
    And I open search by clicking plus button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I click hide keyboard button
    And I tap on connected user <Contact> on People picker page
    And I unblock user
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id2547
  Scenario Outline: Verify dismissing with clicking on Hide [PORTRAIT]
    Given There are 5 users where <Name> is me
    Given <ContactWithFriends> is connected to <Name>
    Given <ContactWithFriends> is connected to <Friend1>
    Given <ContactWithFriends> is connected to <Friend2>
    Given <ContactWithFriends> is connected to <Friend3>
    Given I Sign in using phone number or login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe to reveal hide button for suggested contact <Friend1>
    And I tap hide for suggested contact <Friend1>
    Then I do not see suggested contact <Friend1>

    Examples: 
      | Login      | Password      | Name      | ContactWithFriends | Friend1   | Friend2   | Friend3   |
      | user1Email | user1Password | user1Name | user2Name          | user3Name | user4Name | user5Name |

  @staging @id2547
  Scenario Outline: Verify dismissing with clicking on Hide [LANDSAPE]
    Given There are 5 users where <Name> is me
    Given <ContactWithFriends> is connected to <Name>
    Given <ContactWithFriends> is connected to <Friend1>
    Given <ContactWithFriends> is connected to <Friend2>
    Given <ContactWithFriends> is connected to <Friend3>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe to reveal hide button for suggested contact <Friend1>
    And I tap hide for suggested contact <Friend1>
    Then I do not see suggested contact <Friend1>

    Examples: 
      | Login      | Password      | Name      | ContactWithFriends | Friend1   | Friend2   | Friend3   |
      | user1Email | user1Password | user1Name | user2Name          | user3Name | user4Name | user5Name |

  @staging @id2546
  Scenario Outline: Verify dismissing with one single gesture [PORTRAIT]
    Given There are 5 users where <Name> is me
    Given <ContactWithFriends> is connected to <Name>
    Given <ContactWithFriends> is connected to <Friend1>
    Given <ContactWithFriends> is connected to <Friend2>
    Given <ContactWithFriends> is connected to <Friend3>
    Given I Sign in using phone number or login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe completely to dismiss suggested contact <Friend1>
    Then I do not see suggested contact <Friend1>

    Examples: 
      | Login      | Password      | Name      | ContactWithFriends | Friend1   | Friend2   | Friend3   |
      | user1Email | user1Password | user1Name | user2Name          | user3Name | user4Name | user5Name |

  @staging @id2546
  Scenario Outline: Verify dismissing with one single gesture [LANDSAPE]
    Given There are 5 users where <Name> is me
    Given <ContactWithFriends> is connected to <Name>
    Given <ContactWithFriends> is connected to <Friend1>
    Given <ContactWithFriends> is connected to <Friend2>
    Given <ContactWithFriends> is connected to <Friend3>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe completely to dismiss suggested contact <Friend1>
    Then I do not see suggested contact <Friend1>

    Examples: 
      | Login      | Password      | Name      | ContactWithFriends | Friend1   | Friend2   | Friend3   |
      | user1Email | user1Password | user1Name | user2Name          | user3Name | user4Name | user5Name |
