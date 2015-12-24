Feature: Search

  @regression @id3289
  Scenario Outline: Verify starting a call with action button [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page
    When I tap on connected user <Contact> on People picker page
    And I see call action button on People picker page
    And I click call action button on People picker page
    Then I see mute call, end call buttons
    And I see calling to contact <Contact> message

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3290
  Scenario Outline: Verify starting a call with action button [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page
    When I tap on connected user <Contact> on People picker page
    And I see call action button on People picker page
    And I click call action button on People picker page
    Then I see mute call, end call buttons
    And I see calling to contact <Contact> message

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3291
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 3 top connections
    When I see Send image action button on People picker page
    And I click Send image action button on People picker page
    And I press Camera Roll button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I see new photo in the dialog
    When I navigate back to conversations view
    Then I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @regression @id3292
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button [LANDSAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 3 top connections
    When I see Send image action button on People picker page
    And I click Send image action button on People picker page
    And I press Camera Roll button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I see new photo in the dialog
    And I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @regression @id3295
  Scenario Outline: Verify action buttons appear after selecting person from Top People [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on first 1 top connections
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3296
  Scenario Outline: Verify action buttons appear after selecting person from Top People [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on first 1 top connections
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3297
  Scenario Outline: Verify action buttons appear after choosing user from search results [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3298
  Scenario Outline: Verify action buttons appear after choosing user from search results [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3299
  Scenario Outline: Verify button Open is changed on Create after checking second person [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see open conversation action button on People picker page
    And I tap on 2nd top connection contact
    And I see Create Conversation button on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id3300
  Scenario Outline: Verify button Open is changed on Create after checking second person [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see open conversation action button on People picker page
    When I tap on 2nd top connection contact
    Then I see Create Conversation button on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id3301
  Scenario Outline: Verify action buttons disappear by unchecking the avatar [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see action buttons appeared on People picker page
    When I tap on 1st top connection contact
    Then I see action buttons disappeared on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id3302
  Scenario Outline: Verify action buttons disappear by unchecking the avatar [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see action buttons appeared on People picker page
    When I tap on 1st top connection contact
    Then I see action buttons disappeared on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id3819
  Scenario Outline: Verify action buttons disappear by deleting token from a search field [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see action buttons appeared on People picker page
    And I tap on 2nd top connection contact
    And I see Create Conversation button on People picker page
    And I press backspace button
    And I press backspace button
    Then I see open conversation action button on People picker page
    And I press backspace button
    And I press backspace button
    Then I see action buttons disappeared on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id3820
  Scenario Outline: Verify action buttons disappear by deleting token from a search field [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see action buttons appeared on People picker page
    And I tap on 2nd top connection contact
    And I see Create Conversation button on People picker page
    And I press backspace button
    And I press backspace button
    Then I see open conversation action button on People picker page
    And I press backspace button
    And I press backspace button
    Then I see action buttons disappeared on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id3821
  Scenario Outline: Verify opening conversation with action button [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see open conversation action button on People picker page
    And I click open conversation action button on People picker page
    Then I see dialog page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id3822
  Scenario Outline: Verify opening conversation with action button [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see open conversation action button on People picker page
    And I click open conversation action button on People picker page
    Then I see dialog page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id2482
  Scenario Outline: Verify label hiding after dismissing all PYMK [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe to reveal hide button for suggested contact <Contact2>
    And I tap hide for suggested contact <Contact2>
    And I swipe to reveal hide button for suggested contact <Contact3>
    And I tap hide for suggested contact <Contact3>
    Then I dont see CONNECT label

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @regression @id3824
  Scenario Outline: Verify label hiding after dismissing all PYMK [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I click hide keyboard button
    And I swipe to reveal hide button for suggested contact <Contact2>
    And I tap hide for suggested contact <Contact2>
    And I swipe to reveal hide button for suggested contact <Contact3>
    And I tap hide for suggested contact <Contact3>
    Then I dont see CONNECT label

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @regression @id2481
  Scenario Outline: Verify impossibility of dismissing if search isn't empty [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I swipe completely to dismiss suggested contact <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3823
  Scenario Outline: Verify impossibility of dismissing if search isn't empty [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I swipe completely to dismiss suggested contact <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id4119
  Scenario Outline: Verify action buttons appear after choosing user from search results [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see Invite more people button
    And I tap on 1st top connection contact
    And I DONT see Invite more people button
    And I see action buttons appeared on People picker page
    And I tap on 1st top connection contact
    And I see action buttons disappeared on People picker page
    And I see Invite more people button
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I DONT see Invite more people button
    And I see action buttons appeared on People picker page
    And I press backspace button
    And I press backspace button
    And I see action buttons disappeared on People picker page
    Then I see Invite more people button

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id4120
  Scenario Outline: Verify action buttons appear after choosing user from search results [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see Invite more people button
    And I tap on 1st top connection contact
    And I DONT see Invite more people button
    And I see action buttons appeared on People picker page
    And I tap on 1st top connection contact
    And I see action buttons disappeared on People picker page
    And I see Invite more people button
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I DONT see Invite more people button
    And I see action buttons appeared on People picker page
    And I press backspace button
    And I press backspace button
    And I see action buttons disappeared on People picker page
    Then I see Invite more people button

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |
