Feature: Calling

  @regression @calling @id1860
  Scenario Outline: Verify I can send text, image and ping while in the same convo
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    And I write random message
    And I send message
    And I click ping button
    And I send picture <PictureName> to the current conversation
    Then I see random message in conversation
    And I see ping message <PING>
    And I see sent picture <PictureName> in the conversation view
    And I end the call
    Then <Contact> stops all waiting instances

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PictureName               | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | pinged | userpicture_landscape.jpg | webdriver   | 120     |

  @regression @calling @id2080
  Scenario Outline: Verify I can get pinged by callee during call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    And User <Contact> pinged in the conversation with <Contact>
    And I see ping message <PING>
    And User <Contact> pinged twice in the conversation with <Contact>
    And I see ping message <HOTPING>
    And I end the call
    Then <Contact> stops all waiting instances

    Examples: 
       | Login      | Password      | Name      | Contact   | PING   | HOTPING      | CallBackend | Timeout |
       | user1Email | user1Password | user1Name | user2Name | pinged | pinged again | webdriver   | 120     |

  @staging @calling @id1892
  Scenario Outline: Verify the corresponding conversations list item gets sticky on outgoing call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then I see ongoing call item with name <Contact> is shown on top of conversations list
    Then <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    Then I see ongoing call item with name <Contact> is shown on top of conversations list
    And I end the call
    Then <Contact> stops all waiting instances

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | webdriver   | 120     |

  @regression @calling @id1891
  Scenario Outline: Verify the corresponding conversations list item gets sticky on incoming call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And <Contact> calls me using <CallBackend>
    Then I see ongoing call item with name <Contact> is shown on top of conversations list
    And I accept the incoming call
    Then <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I see the calling bar
    Then I see ongoing call item with name <Contact> is shown on top of conversations list
    And I end the call

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | autocall    | 120     |

  @smoke @calling @id2237
  Scenario Outline: Verify I can call a user twice in a row
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    And I end the call
    Then <Contact> verifies that waiting instance status is changed to ready in <Timeout> seconds
    And <Contact> accepts next incoming call automatically
    Then <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    And <Contact> stops all waiting instances

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | webdriver   | 120     |

  @regression @calling @id1866
  Scenario Outline: Verify I can call a user for more than 15 mins
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I wait for 60 seconds
    And I see the calling bar
    And I end the call
    And <Contact> stops all waiting instances

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | webdriver   | 120     |

  @staging @calling @id1902
  Scenario Outline: Verify that current call is terminated if you want to call someone else (as caller)
	Given My browser supports calling
	Given There are 3 users where <Name> is me
	Given Myself is connected to <Contact1>,<Contact2>
	Given <Contact1> starts waiting instance using <CallBackend>
	Given <Contact1> accepts next incoming call automatically
	Given <Contact1> verifies that waiting instance status is changed to waiting in <Timeout> seconds
	Given <Contact2> starts waiting instance using <CallBackend>
	Given <Contact2> accepts next incoming call automatically
	Given <Contact2> verifies that waiting instance status is changed to waiting in <Timeout> seconds
	Given I switch to Sign In page
	Given I Sign in using login <Login> and password <Password>
	And I see my avatar on top of Contact list
	And I open conversation with <Contact1>
	And I call
	Then <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
	Then I see the calling bar from user <Contact1>
	And I open conversation with <Contact2>
	And I call
	Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
	Then I see the calling bar from user <Contact2>
	And I end the call
	And <Contact1> stops all waiting instances
	And <Contact2> stops all waiting instances

	Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | webdriver   | 120     |

  @smoke @calling @id1839
  Scenario Outline: Verify I can not call in browsers without WebRTC
    Given My browser does not support calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And <Contact> calls me using <CallBackend>
    Then I do not see the calling bar
    And I wait for 3 seconds
    And I see a warning
    And I see "Learn more" link in warning
    When I close the warning
    Then I do not see a warning
    And I see calling button
    When I call
    Then I see a warning
    And I see "Learn more" link in warning
    And I verify browser log is empty

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
	  | user1Email | user1Password | user1Name | user2Name | autocall    | 120     |

  @staging @calling @id3083
  Scenario Outline: Verify that current call is terminated if you want to call someone else (as callee)
	Given My browser supports calling
	Given There are 3 users where <Name> is me
	Given Myself is connected to <Contact1>,<Contact2>
	Given <Contact2> starts waiting instance using <WaitBackend>
	Given <Contact2> accepts next incoming call automatically
	Given <Contact2> verifies that waiting instance status is changed to waiting in <Timeout> seconds
	Given I switch to Sign In page
	Given I Sign in using login <Login> and password <Password>
	And I see my avatar on top of Contact list
	And I open conversation with <Contact1>
	And <Contact1> calls me using <CallBackend>
	And I accept the incoming call
	Then <Contact1> verifies that call status to Myself is changed to active in <Timeout> seconds
	Then I see the calling bar from user <Contact1>
	And I open conversation with <Contact2>
	And I call
	Then <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
	Then I see the calling bar from user <Contact2>
	And I end the call
    And <Contact2> stops all waiting instances

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | CallBackend | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | autocall    | webdriver   | 120     |

  @regression @calling @id2013
  Scenario Outline: Verify I get missed call notification when I call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I call
    Then I wait for 2 seconds
    And I end the call
    When I open conversation with <Contact>
    Then I see conversation with my missed call

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  # This has to work even in browsers, which don't support calling
  @regression @calling @id2014
  Scenario Outline: Verify I get missed call notification when someone calls me
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open self profile
    When <Contact1> calls me using <CallBackend>
    And I wait for 1 seconds
    And <Contact1> stops all calls to me
    And I wait for 1 seconds
    Then I see missed call notification for conversation <Contact1>
    When I open conversation with <Contact1>
    Then I do not see missed call notification for conversation <Contact1>
    Then I see conversation with missed call from <Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name  | autocall    |

  @staging @calling @id1882
  Scenario Outline: People trying to call me while I'm not signed in
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    When <Contact> calls me using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I refresh page
    And I wait for 5 seconds
    Then I see the calling bar from user <Contact>
    And <Contact> stops all calls to me
    Then I see missed call notification for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | autocall    | 120     |

  @regression @calling @id1875
  Scenario Outline: Already on call and try to make another call (caller)
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<OtherContact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When <Contact> calls me using <CallBackend>
    And I see the calling bar from user <Contact>
    When I open conversation with <OtherContact>
    Then I see the calling bar from user <Contact>
    When I call
    Then I see the calling bar from user <Contact>
    When I silence the incoming call
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | OtherContact | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name    | autocall    | 120     |

  @regression @calling @id1906
  Scenario Outline: Verify I can make another call while current one is ignored
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact2> starts waiting instance using <CallWaitBackend>
    Given <Contact2> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact1>
    When <Contact1> calls me using <CallBackend>
    And I see the calling bar
    When I silence the incoming call
    When I open conversation with <Contact2>
    Then I do not see the calling bar
    When I call
    Then I see the calling bar from user <Contact2>
    When I end the call
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | CallBackend | CallWaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | autocall    | webdriver       | 120     |

  @regression @calling @id1883
  Scenario Outline: Verify I can not see blocked contact trying to call me
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    # OtherContact is needed otherwise the search will show up sometimes
    Given Myself is connected to <Contact>,<OtherContact>
    Given Myself blocked <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When <Contact> calls me using <CallBackend>
    Then <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | OtherContact | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name    | autocall    | 120     |

  @regression @calling @id1884
  Scenario Outline: Verify I can see muted conversation person trying to call me
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    Given I muted conversation with <Contact>
    When <Contact> calls me using <CallBackend>
    Then <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | autocall    | 120     |

  @staging @calling @id1907
  Scenario Outline: Verify call button is not visible in the conversation view while incoming call is in progress
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When <Contact> calls me using <CallBackend>
    Then <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I see the calling bar
    And I accept the incoming call
    And I do not see calling button
    When I end the call
    Then I do not see the calling bar
    And I see calling button

    Examples: 
       | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
       | user1Email | user1Password | user1Name | user2Name | autocall    | 120     |

  @staging @calling @id1905
  Scenario Outline: Verify that outgoing call is terminated after within 1 minute timeout if nobody responds
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I call
    And I see the calling bar
    And I wait for 60 seconds
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @calling @id2477
  Scenario Outline: Already on call and try to make another call (adressee)
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<OtherContact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    When I open conversation with <OtherContact>
    Then I do not see the calling bar
    When I call
    Then I do not see the calling bar
    When I open call conversation with <Contact>
    And I see the calling bar
    When I end the call
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   | OtherContact | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name    | webdriver   | 120     |
