Feature: Conversation View

  @id2252 @smoke
  Scenario Outline: Send Message to contact in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap the text input in the conversation view
    When I type the message "<Message>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples: 
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @id2238 @smoke
  Scenario Outline: Send Message to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap the text input in the conversation view
    When I type the message "<Message>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples: 
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |


  @id2254 @smoke
  Scenario Outline: Send Camera picture to contact in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I swipe left on text input in the conversation view
    When I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2240 @smoke
  Scenario Outline: Send Camera picture to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I swipe left on text input in the conversation view
    When I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2255 @smoke
  Scenario Outline: Add people to 1:1 chat in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I tap the conversation <Contact1>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    When I tap Add People button on Single user popover
    And I enter "<Contact2>" into the Search input on Single user popover
    And I tap the avatar of <Contact2> in search results on Single user popover
    And I tap the Add To Conversation button on Single user popover
    Then I do not see the Single user popover
    And I see the conversation view
    And I see the chat header message contains "<Action>" text on conversation view page
    And I see the chat header message contains "<Contact1>" text on conversation view page
    And I see the chat header message contains "<Contact2>" text on conversation view page

    Examples: 
      | Name      | Contact1  | Contact2  | Action                     |
      | user1Name | user2Name | user3Name | YOU STARTED A CONVERSATION |

  @id2241 @smoke
  Scenario Outline: Add people to 1:1 chat in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I tap the conversation <Contact1>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    When I tap Add People button on Single user popover
    And I enter "<Contact2>" into the Search input on Single user popover
    And I tap the avatar of <Contact2> in search results on Single user popover
    And I tap the Add To Conversation button on Single user popover
    Then I do not see the Single user popover
    And I see the conversation view
    And I see the chat header message contains "<Action>" text on conversation view page
    And I see the chat header message contains "<Contact1>" text on conversation view page
    And I see the chat header message contains "<Contact2>" text on conversation view page

    Examples: 
      | Name      | Contact1  | Contact2  | Action                     |
      | user1Name | user2Name | user3Name | YOU STARTED A CONVERSATION |

  @id2256 @smoke
  Scenario Outline: Send message to group chat in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap the text input in the conversation view
    When I type the message "<Message>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @id2242 @smoke
  Scenario Outline: Send message to group chat in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    And I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap the text input in the conversation view
    When I type the message "<Message>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @id2047 @smoke
  Scenario Outline: Check ability to open and close one-to-one pop-over in different ways
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to <Orientation>
    Given I sign in using my email
    And I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    When I tap Show Details button on conversation view page
    Then I see the Single user popover
    And I see the user name <Contact> on Single user popover
    When I tap Close button on Single user popover
    Then I do not see the Single user popover
    When I tap Show Details button on conversation view page
    Then I see the Single user popover
    #When I tap in the center of Single user popover
    When I tap on center of screen
    #Then I see the Single user popover
    Then I do not see the Single user popover
    When I tap Close Tools button on conversation view page
    And I tap the text input in the conversation view
    And I tap Show Tools button on conversation view page
    When I tap Show Details button on conversation view page
    Then I see the Single user popover
    When I navigate back
    Then I do not see the Single user popover

    Examples: 
      | Name      | Contact   | Orientation |
      | user1Name | user2Name | landscape   |
      | user1Name | user2Name | portrait    |

  @id2050 @smoke
  Scenario Outline: One-to-one pop-over hidden after rotations
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    When I tap the text input in the conversation view
    And I tap Show Details button on conversation view page
    Then I do not see the Single user popover
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    When I rotate UI to landscape
    Then I do not see the Single user popover
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    When I rotate UI to portrait
    Then I do not see the Single user popover

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |
