Feature: Search

  @id218 @regression
  Scenario Outline: I can do full name search for existing 1:1 non-archive
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I wait until <Contact> exists in backend search results
    When I press Open StartUI
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    Then I see user <Contact> in People picker

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id220 @regression
  Scenario Outline: I can do full name search for existing group convo non-archive
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I press Open StartUI
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<GroupChatName>" into Search input on People Picker page
    Then I see group <GroupChatName> in People picker

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName          |
      | user1Name | user3Name | user2Name | PeoplePicker GroupChat |

  @id223 @regression
  Scenario Outline: I can do partial name search for existing 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I press Open StartUI
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field part <Size> of user name to connect to <Contact>
    Then I see user <Contact> in People picker

    Examples: 
      | Name      | Contact   | Size |
      | user1Name | user2Name | 12   |

  @id225 @regression
  Scenario Outline: I can do partial name search for existing group convo non-archive
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I press Open StartUI
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field part <Size> of user name to connect to <GroupChatName>
    Then I see group <GroupChatName> in People picker

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName           | Size |
      | user1Name | user3Name | user2Name | PeoplePicker GroupChat1 | 5    |

  @id327 @smoke
  Scenario Outline: Open/Close People picker
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    When I press Open StartUI
    And I see People picker page
    And I press Clear button
    Then I see Contact list

    Examples: 
      | Name      |
      | user1Name |

  @id319 @regression
  Scenario Outline: I can create group chat from Search
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I press Open StartUI
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I add in search field user name to connect to <Contact2>
    And I tap on user name found on People picker page <Contact2>
    And I tap on create conversation
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName           |
      | user1Name | user3Name | user2Name | PeoplePicker GroupChat2 |

  @id2214 @regression
  Scenario Outline: I can dismiss PYMK by Hide button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I press Open StartUI
    And I see People picker page
    And I keep reopening People Picker until PYMK are visible
    And I remember the name of the first PYMK item
    And I do short swipe right on the first PYMK item
    And I click hide button on the first PYMK item
    Then I do not see the previously remembered PYMK item
    When I press Clear button
    And I press Open StartUI
    Then I do not see the previously remembered PYMK item

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id2213 @regression
  Scenario Outline: I can dismiss PYMK by swipe
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I press Open StartUI
    And I see People picker page
    And I keep reopening People Picker until PYMK are visible
    And I remember the name of the first PYMK item
    And I do long swipe right on the first PYMK item
    Then I do not see the previously remembered PYMK item
    When I press Clear button
    And I press Open StartUI
    Then I do not see the previously remembered PYMK item

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |
      