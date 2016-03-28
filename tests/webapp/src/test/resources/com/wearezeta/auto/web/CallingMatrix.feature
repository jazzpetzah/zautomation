Feature: Calling_Matrix

  @C5359 @calling_matrix @calling @torun
  Scenario Outline: Verify I can make 1:1 call to <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I call
    Then <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And I wait for 5 seconds
    And <Contact> verifies to have 1 flows
    And <Contact> verifies that all flows have greater than 0 bytes
    And I hang up call with conversation <Contact>
    Then I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend      | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:latest    | 20      |
#      | user1Email | user1Password | user1Name | user2Name | chrome:latest-1  | 20      |
#      | user1Email | user1Password | user1Name | user2Name | chrome:latest-2  | 20      |
#      | user1Email | user1Password | user1Name | user2Name | firefox:latest   | 20      |
#      | user1Email | user1Password | user1Name | user2Name | firefox:latest-1 | 20      |
#      | user1Email | user1Password | user1Name | user2Name | firefox:latest-2 | 20      |

  @C5360 @calling_matrix @calling
  Scenario Outline: Verify I can make 1:1 video call to <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
#    And I see the ongoing call controls for conversation <Contact>
    And I wait for 5 seconds
    And <Contact> verifies to have 1 flows
    And <Contact> verifies that all flows have greater than 0 bytes
    And I end the video call
#    Then I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend      | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:latest    | 20      |
#      | user1Email | user1Password | user1Name | user2Name | chrome:latest-1  | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:latest-2  | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:latest   | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:latest-1 | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:latest-2 | 20      |

  @C5361 @calling_matrix @calling
  Scenario Outline: Verify I can make 1:1 call to AVS <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I call
    Then <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    #And I see row of avatars on call controls with user <Contact>
    And I wait for 5 seconds
    And I hang up call with conversation <Contact>
    And I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend     | Timeout |
      | user1Email | user1Password | user1Name | user2Name | zcall:latest-1  | 20      |
#      | user1Email | user1Password | user1Name | user2Name | zcall:latest    | 20      |

  @C5362 @calling_matrix @calling
  Scenario Outline: Verify I can receive 1:1 call from <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And <Contact> calls me
    When I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    Then I see the ongoing call controls for conversation <Contact>
    And I wait for 5 seconds
    And <Contact> verifies to have 1 flows
    And <Contact> verifies that all flows have greater than 0 bytes
    And I hang up call with conversation <Contact>
    And I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend      | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:latest    | 20      |
#      | user1Email | user1Password | user1Name | user2Name | chrome:latest-1  | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:latest-2  | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:latest   | 20      |      
      | user1Email | user1Password | user1Name | user2Name | firefox:latest-1 | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:latest-2 | 20      |

  @C5363 @calling_matrix @calling
  Scenario Outline: Verify I can receive 1:1 video call from <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And <Contact> starts a video call to me
    When I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
#    Then I see the ongoing call controls for conversation <Contact>
    And I wait for 5 seconds
    And <Contact> verifies to have 1 flows
    And <Contact> verifies that all flows have greater than 0 bytes
    And I end the video call
#    And I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend      | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:latest    | 20      |
#      | user1Email | user1Password | user1Name | user2Name | chrome:latest-1  | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:latest-2  | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:latest   | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:latest-1 | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:latest-2 | 20      |

  @C5364 @calling_matrix @calling
  Scenario Outline: Verify I can receive 1:1 call from AVS <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And <Contact> calls me
    When I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    Then I see the ongoing call controls for conversation <Contact>
    And I hang up call with conversation <Contact>
    And I do not see the call controls for conversation <Contact>
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And <Contact> verifies that call to conversation <Contact> was successful

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend       | Timeout |
      | user1Email | user1Password | user1Name | user2Name | autocall:latest-1 | 20      |
      | user1Email | user1Password | user1Name | user2Name | autocall:latest   | 20      |

  @C5365 @calling_matrix @calling
  Scenario Outline: Verify I can make group call with multiple <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given <Contact1>,<Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And I call
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And I wait for 5 seconds
    And <Contact1>,<Contact2> verifies to have 2 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    And I hang up call with conversation <ChatName1>
    And I see the join call controls for conversation <ChatName1>
    And I wait for 5 seconds
    And <Contact1>,<Contact2> verifies to have 1 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    # Stops all waiting instance calls
    And <Contact1> stops calling

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | WaitBackend      | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:latest    | 20      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:latest-1  | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:latest-2  | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:latest   | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:latest-1 | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:latest-2 | 20      |

  @C5366 @calling_matrix @calling
  Scenario Outline: Verify I can make group call with multiple AVS <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And I call
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    When I hang up call with conversation <ChatName1>
    Then I see the join call controls for conversation <ChatName1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | WaitBackend    | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | zcall:latest-1 | 20      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | zcall:latest   | 20      |

  @C5367 @calling_matrix @calling
  Scenario Outline: Verify I can join group call with multiple <Backend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <Backend>
    Given <Contact1> accept next incoming call automatically
    Given <Contact1> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact2> calls <ChatName1>
    Then <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact2> verifies that call status to <ChatName1> is changed to active in <Timeout> seconds
    When I accept the call from conversation <ChatName1>
    And I see the ongoing call controls for conversation <ChatName1>
    And I wait for 5 seconds
    And <Contact1>,<Contact2> verify to have 2 flows
    And <Contact1>,<Contact2> verify that all flows have greater than 0 bytes
    And I hang up call with conversation <ChatName1>
    And I see the join call controls for conversation <ChatName1>
    And I wait for 5 seconds
    And <Contact1>,<Contact2> verifies to have 1 flows
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    # Stops all waiting instance calls
    And <Contact1> stops calling

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:latest    | 20      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:latest-1  | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | chrome:latest-2  | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:latest   | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:latest-1 | 20      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | firefox:latest-2 | 20      | 

  @C5368 @calling_matrix @calling
  Scenario Outline: Verify I can join group call with AVS <Backend> and <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact1> starts instance using <Backend>
    Given <Contact2> accept next incoming call automatically
    Given <Contact2> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact1> calls <ChatName1>
    When I accept the call from conversation <ChatName1>
    Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName1> is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And I see row of avatars on call controls with users <Contact1>,<Contact2>
    And I wait for 5 seconds
    And <Contact2> verifies to have 2 flows
    And <Contact2> verifies that all flows have greater than 0 bytes
    And I hang up call with conversation <ChatName1>
    And I see the join call controls for conversation <ChatName1>
    And I wait for 5 seconds
    And <Contact2> verifies to have 1 flows
    And <Contact2> verifies that all flows have greater than 0 bytes
    # Stops all autocall instance calls
    And <Contact1> stops calling <ChatName1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend           | WaitBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest-1 | chrome:latest    | 30      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest-1 | chrome:latest-1  | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest-1 | chrome:latest-2  | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest-1 | firefox:latest   | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest-1 | firefox:latest-1 | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest-1 | firefox:latest-2 | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest   | chrome:latest    | 30      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest   | chrome:latest-1  | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest   | chrome:latest-2  | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest   | firefox:latest   | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest   | firefox:latest-1 | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest   | firefox:latest-2 | 30      |
  
  @C5369 @calling_matrix @calling
  Scenario Outline: Verify I can join group call with AVS <Backend> and <WaitBackend>
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1> starts instance using <Backend>
    Given <Contact2> starts instance using <WaitBackend>
    Given <Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName1>
    And <Contact1> calls <ChatName1>
    When I accept the incoming call
    Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then <Contact1> verifies that call status to <ChatName1> is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <ChatName1>
    And I hang up call with conversation <ChatName1>
    Then I see the join call controls for conversation <ChatName1>
    # Stops all autocall instance calls
    And <Contact1> stops calling <ChatName1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName1 | Backend           | WaitBackend    | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest-1 | zcall:latest-1 | 30      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest-1 | zcall:latest    | 30      |
#      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest   | zcall:latest    | 30      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupCall | autocall:latest   | zcall:latest-1 | 30      |
