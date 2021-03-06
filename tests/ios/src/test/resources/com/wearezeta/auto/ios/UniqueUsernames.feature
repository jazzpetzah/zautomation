Feature: Unique Usernames

  @C352028 @regression @rc
  Scenario Outline: Verify autogeneration of a username for a user with latin characters only
    Given I see sign in screen
    Given I enter phone number for <Name>
    Given I enter activation code
    Given I accept terms of service
    Given I input name <Name> and commit it
    Given I accept alert if visible
    Given I tap Keep This One button
    # Wait for picture to be applied
    Given I wait for 3 seconds
    Given I accept alert if visible
    Given I tap Not Now button on Share Contacts overlay
    When I see username <Name> on Unique Username Takeover page
    Then I see unique username @<Name> on Unique Username Takeover page
    When I tap Keep This One button on Unique Username Takeover page
    Then I see conversations list

    Examples:
      | Name      |
      | user1Name |

  @C352060 @addressbookStart @forceReset @regression
  Scenario Outline: Verify incoming connection view
    Given There are 7 users where <Name> is me
    Given Users <Name>,<Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact5WithSameNameInAB>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact6Common> upload own details
    Given <Contact1WithABEmail> sent connection request to Me
    Given <Contact2WithABPhoneNumber> sent connection request to Me
    Given <Contact3WithUniqueUserName> sent connection request to Me
    Given User <Contact3WithUniqueUserName> sets the unique username
    Given <Contact4WithCommonFriends> sent connection request to Me
    Given <Contact4WithCommonFriends> is connected to <Contact6Common>
    Given <Contact5WithSameNameInAB> sent connection request to Me
    Given <Contact6Common> is connected to me
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I add name <Contact1ABName> and email <Contact1Email> to Address Book
    Given I add name <Contact2ABName> and phone <Contact2PhoneNumber> to Address Book
    Given I add name <Contact5WithSameNameInAB> and email <Contact5Email> to Address Book
    Given I restore Wire
    Given I sign in using my email
    Given I see conversations list
    Given I wait until <Contact1WithABEmail> exists in backend search results
    Given I wait until <Contact2WithABPhoneNumber> exists in backend search results
    Given I wait until <Contact3WithUniqueUserName> exists in backend search results
    Given I wait until <Contact4WithCommonFriends> exists in backend search results
    Given I wait until <Contact4WithCommonFriends> has 1 common friend on the backend
    Given I wait until <Contact5WithSameNameInAB> exists in backend search results
    Given I open search UI
    Given I accept alert
    Given I tap input field on Search UI page
    When I type "<Contact3WithUniqueUserName>" in Search UI input field
    And I tap on conversation <Contact3WithUniqueUserName> in search result
    Then I see name "<Contact3WithUniqueUserName>" on Single user Pending incoming connection page
    And I see unique username "<Contact3UniqueUserName>" on Single user Pending incoming connection page
    And I do not see Address Book name on Single user Pending incoming connection page
    And I tap Ignore button on Single user Pending incoming connection page
    When I type "<Contact1WithABEmail>" in cleared Search UI input field
    And I tap on conversation <Contact1WithABEmail> in search result
    And I see name "<Contact1WithABEmail>" on Single user Pending incoming connection page
    Then I see Address Book name "<Contact1ABName>" on Single user Pending incoming connection page
    And I do not see unique username on Single user Pending incoming connection page
    And I tap Ignore button on Single user Pending incoming connection page
    When I type "<Contact2WithABPhoneNumber>" in cleared Search UI input field
    And I tap on conversation <Contact2WithABPhoneNumber> in search result
    Then I see name "<Contact2WithABPhoneNumber>" on Single user Pending incoming connection page
    And I see Address Book name "<Contact2ABName>" on Single user Pending incoming connection page
    And I do not see unique username on Single user Pending incoming connection page
    And I tap Ignore button on Single user Pending incoming connection page
    When I type "<Contact4WithCommonFriends>" in cleared Search UI input field
    And I tap on conversation <Contact4WithCommonFriends> in search result
    Then I see name "<Contact4WithCommonFriends>" on Single user Pending incoming connection page
    And I do not see unique username on Single user Pending incoming connection page
    And I see common friends count "1" on Single user Pending incoming connection page
    And I tap Ignore button on Single user Pending incoming connection page
    When I type "<Contact5WithSameNameInAB>" in cleared Search UI input field
    And I tap on conversation <Contact5WithSameNameInAB> in search result
    Then I see name "<Contact5WithSameNameInAB>" on Single user Pending incoming connection page
    And I do not see unique username on Single user Pending incoming connection page
    And I see Address Book name "" on Single user Pending incoming connection page
    And I tap Ignore button on Single user Pending incoming connection page

    Examples:
      | Name      | Contact1WithABEmail | Contact1ABName | Contact1Email | Contact2WithABPhoneNumber | Contact2ABName | Contact2PhoneNumber | Contact3WithUniqueUserName | Contact3UniqueUserName | Contact4WithCommonFriends | Contact5WithSameNameInAB | Contact5Email | Contact6Common |
      | user1Name | user2Name           | user2ABName    | user2Email    | user3Name                 | user3ABName    | user3PhoneNumber    | user4Name                  | user4UniqueUsername    | user5Name                 | user6Name                | user6Email    | user7Name      |

  @C352039 @regression @fastLogin @rc
  Scenario Outline: Verify impossibility to save incorrect username
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item Username
    When I enter "" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    And I enter "<MinChars>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    Then I type unique usernames from the data table and verify they cannot be committed on Unique Username page
      | Charset      | Chars  |
      | Cyrillic     | МоёИмя |
      | Arabic       | اسمي   |
      | Chinese      | 我的名字   |
      | SpecialChars | %^&@#$ |

    Examples:
      | Name      | MinChars |
      | user1Name | 1        |

  @C375777 @regression @fastLogin
  Scenario Outline: Verify impossibility to enter too long username
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item Username
    When I attempt to enter <MaxChars> random latin alphanumeric chars as name on Unique Username page
    Then I see that name length is less than <MaxChars> chars on Unique Username page

    Examples:
      | Name      | MaxChars |
      | user1Name | 22       |

  @C352059 @addressbookStart @forceReset @regression
  Scenario Outline: Verify outgoing connection request view
    Given There are 7 users where <Name> is me
    Given Users <Name>,<Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact5WithSameNameInAB>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact6Common> upload own details
    Given Myself sent connection request to <Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact5WithSameNameInAB>
    Given User <Contact3WithUniqueUserName> sets the unique username
    Given <Contact4WithCommonFriends> is connected to <Contact6Common>
    Given <Contact6Common> is connected to me
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I add name <Contact1ABName> and email <Contact1Email> to Address Book
    Given I add name <Contact2ABName> and phone <Contact2PhoneNumber> to Address Book
    Given I add name <Contact5WithSameNameInAB> and email <Contact5Email> to Address Book
    Given I restore Wire
    Given I sign in using my email
    Given I see conversations list
    Given I wait until <Contact1WithABEmail> exists in backend search results
    Given I wait until <Contact2WithABPhoneNumber> exists in backend search results
    Given I wait until <Contact3WithUniqueUserName> exists in backend search results
    Given I wait until <Contact4WithCommonFriends> exists in backend search results
    Given I wait until <Contact4WithCommonFriends> has 1 common friend on the backend
    Given I wait until <Contact5WithSameNameInAB> exists in backend search results
    Given I open search UI
    Given I accept alert
    Given I tap input field on Search UI page
    When I type "<Contact3WithUniqueUserName>" in Search UI input field
    And I tap on conversation <Contact3WithUniqueUserName> in search result
    Then I see name "<Contact3WithUniqueUserName>" on Single user Pending outgoing connection page
    And I see unique username "<Contact3UniqueUserName>" on Single user Pending outgoing connection page
    And I do not see Address Book name on Single user Pending outgoing connection page
    And I tap Cancel Request button on Single user Pending outgoing connection page
    And I confirm Cancel Request conversation action
    When I type "<Contact1WithABEmail>" in cleared Search UI input field
    And I tap on conversation <Contact1WithABEmail> in search result
    And I see name "<Contact1WithABEmail>" on Single user Pending outgoing connection page
    Then I see Address Book name "<Contact1ABName>" on Single user Pending outgoing connection page
    And I do not see unique username on Single user Pending outgoing connection page
    And I tap Cancel Request button on Single user Pending outgoing connection page
    And I confirm Cancel Request conversation action
    When I type "<Contact2WithABPhoneNumber>" in cleared Search UI input field
    And I tap on conversation <Contact2WithABPhoneNumber> in search result
    Then I see name "<Contact2WithABPhoneNumber>" on Single user Pending outgoing connection page
    And I see Address Book name "<Contact2ABName>" on Single user Pending outgoing connection page
    And I do not see unique username on Single user Pending outgoing connection page
    And I tap Cancel Request button on Single user Pending outgoing connection page
    And I confirm Cancel Request conversation action
    When I type "<Contact4WithCommonFriends>" in cleared Search UI input field
    And I tap on conversation <Contact4WithCommonFriends> in search result
    Then I see name "<Contact4WithCommonFriends>" on Single user Pending outgoing connection page
    And I do not see unique username on Single user Pending outgoing connection page
    And I see common friends count "1" on Single user Pending outgoing connection page
    And I tap Cancel Request button on Single user Pending outgoing connection page
    And I confirm Cancel Request conversation action
    When I type "<Contact5WithSameNameInAB>" in cleared Search UI input field
    And I tap on conversation <Contact5WithSameNameInAB> in search result
    Then I see name "<Contact5WithSameNameInAB>" on Single user Pending outgoing connection page
    And I do not see unique username on Single user Pending outgoing connection page
    And I see Address Book name "" on Single user Pending outgoing connection page

    Examples:
      | Name      | Contact1WithABEmail | Contact1ABName | Contact1Email | Contact2WithABPhoneNumber | Contact2ABName | Contact2PhoneNumber | Contact3WithUniqueUserName | Contact3UniqueUserName | Contact4WithCommonFriends | Contact5WithSameNameInAB | Contact5Email | Contact6Common |
      | user1Name | user2Name           | user2ABName    | user2Email    | user3Name                 | user3ABName    | user3PhoneNumber    | user4Name                  | user4UniqueUsername    | user5Name                 | user6Name                | user6Email    | user7Name      |

  @C352036 @regression @fastLogin @rc
  Scenario Outline: Verify setting correct user name
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item Username
    When I attempt to enter <RegularLength> random latin alphanumeric chars as name on Unique Username page
    And I tap Save button on Unique Username page
    Then I see previously set unique username is displayed on Settings Page
    When I select settings item Username
    And I attempt to enter <MinLength> random latin alphanumeric chars as name on Unique Username page
    And I tap Save button on Unique Username page
    Then I see previously set unique username is displayed on Settings Page
    When I select settings item Username
    And I attempt to enter <MaxLength> random latin alphanumeric chars as name on Unique Username page
    And I tap Save button on Unique Username page
    Then I see previously set unique username is displayed on Settings Page

    Examples:
      | Name      | RegularLength | MinLength | MaxLength |
      | user1Name | 6             | 2         | 21        |

  @C352027 @regression @fastLogin @rc
  Scenario Outline: Verify Settings are opened on choosing generating your own username
    Given There is 1 user
    Given User <Name> is me
    Given I prepare Wire to perform fast log in by email as <Name>
    Given I sign in using my email or phone number
    Given I open search UI
    Given I accept alert if visible
    Given I tap X button on Search UI page
    When I tap Choose Yours button on Unique Username Takeover page
    Then I see Unique Username page
    And I see unique username input is prefilled with <Name> on Unique Username page
    When I attempt to enter <NewNameLength> random latin alphanumeric chars as name on Unique Username page
    And I tap Save button on Unique Username page
    And I tap settings gear button
    And I select settings item Account
    Then I see previously set unique username is displayed on Settings Page

    Examples:
      | Name      | NewNameLength |
      | user1Name | 8             |

  @C352042 @regression @fastLogin
  Scenario Outline: Verify username is unique and not a reserved name
    Given There are 2 users
    Given User <Name> is me
    Given User <Contact> changes the unique username to "<MyUniqueUsername>"
    Given I prepare Wire to perform fast log in by email as <Name>
    Given I sign in using my email or phone number
    Given I open search UI
    Given I accept alert if visible
    Given I tap X button on Search UI page
    When I tap Choose Yours button on Unique Username Takeover page
    And I enter "<MyUniqueUsername>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    And I see "<ExpectedText>" error label on Unique Username page
    When I enter "<ReservedUniqueUsername>" name on Unique Username page
    And I tap Save button on Unique Username page
    Then I see alert contains text <ReservedText>

    Examples:
      | Name      | MyUniqueUsername    | Contact   | ExpectedText  | ReservedUniqueUsername | ReservedText           |
      | user1Name | user1UniqueUsername | user2Name | Already taken | admin                  | Unable to set username |

  @C352058 @addressbookStart @forceReset @regression @rc
  Scenario Outline: Verify 1-to-1 conversation details
    Given There are 7 users where <Name> is me
    Given Users <Name>,<Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact5WithSameNameInAB>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact6Common> upload own details
    Given Myself is connected to <Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact5WithSameNameInAB>,<Contact6Common>
    Given User <Contact3WithUniqueUserName> sets the unique username
    Given <Contact4WithCommonFriends> is connected to <Contact6Common>
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I add name <Contact1ABName> and email <Contact1Email> to Address Book
    Given I add name <Contact2ABName> and phone <Contact2PhoneNumber> to Address Book
    Given I add name <Contact5WithSameNameInAB> and email <Contact5Email> to Address Book
    Given I restore Wire
    Given I sign in using my email
    Given I see conversations list
    Given I wait until <Contact4WithCommonFriends> has 1 common friend on the backend
    Given I open search UI
    Given I accept alert
    Given I tap X button on Search UI page
    When I tap on contact name <Contact3WithUniqueUserName>
    And I open conversation details
    Then I see name "<Contact3WithUniqueUserName>" on Single user Pending outgoing connection page
    And I see unique username "<Contact3UniqueUserName>" on Single user Pending outgoing connection page
    And I do not see Address Book name on Single user Pending outgoing connection page
    And I tap X button on Single user profile page
    When I navigate back to conversations list
    And I tap on contact name <Contact1WithABEmail>
    And I open conversation details
    And I see name "<Contact1WithABEmail>" on Single user profile page
    Then I see Address Book name "<Contact1ABName>" on Single user profile page
    And I do not see unique username on Single user profile page
    And I tap X button on Single user profile page
    When I navigate back to conversations list
    And I tap on contact name <Contact2WithABPhoneNumber>
    And I open conversation details
    Then I see name "<Contact2WithABPhoneNumber>" on Single user profile page
    And I see Address Book name "<Contact2ABName>" on Single user profile page
    And I do not see unique username on Single user profile page
    And I tap X button on Single user profile page
    When I navigate back to conversations list
    And I tap on contact name <Contact4WithCommonFriends>
    And I open conversation details
    Then I see name "<Contact4WithCommonFriends>" on Single user Pending outgoing connection page
    And I do not see unique username on Single user Pending outgoing connection page
    And I do not see common friends count on Single user Pending outgoing connection page
    And I tap X button on Single user profile page
    When I navigate back to conversations list
    And I tap on contact name <Contact5WithSameNameInAB>
    And I open conversation details
    Then I see name "<Contact5WithSameNameInAB>" on Single user Pending outgoing connection page
    And I do not see unique username on Single user Pending outgoing connection page
    And I see Address Book name "" on Single user Pending outgoing connection page

    Examples:
      | Name      | Contact1WithABEmail | Contact1ABName | Contact1Email | Contact2WithABPhoneNumber | Contact2ABName | Contact2PhoneNumber | Contact3WithUniqueUserName | Contact3UniqueUserName | Contact4WithCommonFriends | Contact5WithSameNameInAB | Contact5Email | Contact6Common |
      | user1Name | user2Name           | user2ABName    | user2Email    | user3Name                 | user3ABName    | user3PhoneNumber    | user4Name                  | user4UniqueUsername    | user5Name                 | user6Name                | user6Email    | user7Name      |

  @C352666 @regression @fastLogin
  Scenario Outline: Verify search is not case sensitive
    Given There are 2 users where <Name> is me
    Given User <Contact> sets the unique username
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until <ContactUniqueUserName> exists in backend search results
    Given I open search UI
    Given I accept alert if visible
    When I type "<ContactUniqueUserName>" in Search UI input field
    Then I see the conversation "<Contact>" exists in Search results
    When I type "<ContactUniqueUserName>" in cleared Search UI input field in upper case
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   | ContactUniqueUserName |
      | user1Name | user2Name | user2UniqueUsername   |

  @C352054 @regression @fastLogin @rc
  Scenario Outline: Verify search by full and partial user name
    Given There are 2 users where <Name> is me
    Given User <Contact> sets the unique username
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until <ContactWithUniqueUserName> exists in backend search results
    Given I open search UI
    Given I accept alert if visible
    Given I tap input field on Search UI page
    When I type "<ContactWithUniqueUserName>" in Search UI input field
    Then I see the conversation "<Contact>" exists in Search results
    When I type first 6 letter of user name "<ContactWithUniqueUserName>" into cleared Search UI input field
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   | ContactWithUniqueUserName |
      | user1Name | user2Name | user2UniqueUsername       |

  @C352052 @addressbookStart @forceReset @regression
  Scenario Outline: Verify search for connected users returns proper results
    Given There are 8 users where <Name> is me
    Given Users <Name>,<Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact5WithSameNameInAB>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact6Common>,<Contact7WoCF> upload own details
    Given Myself is connected to <Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact5WithSameNameInAB>,<Contact6Common>,<Contact7WoCF>
    Given User <Contact3WithUniqueUserName> sets the unique username
    Given User <Contact1WithABEmail> sets the unique username
    Given <Contact6Common> is connected to <Contact4WithCommonFriends>,<Contact1WithABEmail>
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I add name <Contact1ABName> and email <Contact1Email> to Address Book
    Given I add name <Contact2ABName> and phone <Contact2PhoneNumber> to Address Book
    Given I add name <Contact5WithSameNameInAB> and email <Contact5Email> to Address Book
    Given I restore Wire
    Given I sign in using my email
    Given I see conversations list
    Given I wait until <Contact1WithABEmail> exists in backend search results
    Given I wait until <Contact1WithABEmail> has 1 common friend on the backend
    Given I wait until <Contact2WithABPhoneNumber> exists in backend search results
    Given I wait until <Contact3WithUniqueUserName> exists in backend search results
    Given I wait until <Contact4WithCommonFriends> exists in backend search results
    Given I wait until <Contact4WithCommonFriends> has 1 common friend on the backend
    Given I wait until <Contact5WithSameNameInAB> exists in backend search results
    Given I wait until <Contact7WoCF> exists in backend search results
    When I open search UI
    And I accept alert
    Then I verify correct details are shown for the found users
      | Name                         | Details                                                  |
      | <Contact1WithABEmail>        | @<Contact1UniqueUsername> · <Contact1ABName> in Contacts |
      | <Contact2WithABPhoneNumber>  | <Contact2ABName> in Contacts                             |
      | <Contact3WithUniqueUserName> | @<Contact3UniqueUserName>                                |
      | <Contact5WithSameNameInAB>   | in Contacts                                              |
      | <Contact7WoCF>               |                                                          |
      | <Contact4WithCommonFriends>  |                                                          |

    Examples:
      | Name      | Contact1WithABEmail | Contact1ABName | Contact1UniqueUsername | Contact1Email | Contact2WithABPhoneNumber | Contact2ABName | Contact2PhoneNumber | Contact3WithUniqueUserName | Contact3UniqueUserName | Contact4WithCommonFriends | Contact5WithSameNameInAB | Contact5Email | Contact6Common | Contact7WoCF |
      | user1Name | user2Name           | user2ABName    | user2UniqueUsername    | user2Email    | user3Name                 | user3ABName    | user3PhoneNumber    | user4Name                  | user4UniqueUsername    | user5Name                 | user6Name                | user6Email    | user7Name      | user8Name    |

  @C352029 @regression @fastLogin @rc
  Scenario Outline: Verify autogeneration of a username for an existing user (different scenarios)
    Given There is 1 user
    Given User <NameAlias> is me
    Given User <NameAlias> changes name to <Name>
    Given I prepare Wire to perform fast log in by email as <Name>
    Given I sign in using my email or phone number
    Given I open search UI
    Given I accept alert if visible
    Given I tap X button on Search UI page
    When I see username <NameAlias> on Unique Username Takeover page
    Then I see unique username starts with <UniqueName> on Unique Username Takeover page

    Examples:
      | NameAlias | Name        | UniqueName |
      | user1Name | Tom         | @tom       |
      | user1Name | Tom.Sawyer  | @tomsawyer |
      | user1Name | Æéÿüíøšłźçñ | @eyuiszcn  |
      | user1Name | Даша        | @dasa      |
      | user1Name | داريا       | @darya     |
      | user1Name | 明麗          | @mingli    |

  @C352049 @addressbookStart @forceReset @regression
  Scenario Outline: Verify search for unconnected users returns proper results
    Given There are 8 users where <Name> is me
    Given Users <Name>,<Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact5WithSameNameInAB>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact6Common>,<Contact7WoCF> upload own details
    Given Myself is connected to <Contact6Common>
    Given User <Contact3WithUniqueUserName> sets the unique username
    Given User <Contact1WithABEmail> sets the unique username
    Given <Contact6Common> is connected to <Contact4WithCommonFriends>,<Contact1WithABEmail>
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I add name <Contact1ABName> and email <Contact1Email> to Address Book
    Given I add name <Contact2ABName> and phone <Contact2PhoneNumber> to Address Book
    Given I add name <Contact5WithSameNameInAB> and email <Contact5Email> to Address Book
    Given I restore Wire
    Given I sign in using my email
    Given I see conversations list
    Given I open search UI
    Given I accept alert
    Given I tap X button on Search UI page
    Given I wait until <Contact1WithABEmail> exists in backend search results
    Given I wait until <Contact1WithABEmail> has 1 common friend on the backend
    Given I wait until <Contact2WithABPhoneNumber> exists in backend search results
    Given I wait until <Contact3WithUniqueUserName> exists in backend search results
    Given I wait until <Contact4WithCommonFriends> exists in backend search results
    Given I wait until <Contact4WithCommonFriends> has 1 common friend on the backend
    Given I wait until <Contact5WithSameNameInAB> exists in backend search results
    Given I wait until <Contact7WoCF> exists in backend search results
    Given I see conversation <Contact2WithABPhoneNumber> in conversations list
    When I open search UI
    Then I verify correct details are shown for the found users
      | Name                         | Details                                                  |
      | <Contact7WoCF>               |                                                          |
      | <Contact1WithABEmail>        | @<Contact1UniqueUsername> · <Contact1ABName> in Contacts |
      | <Contact2WithABPhoneNumber>  | <Contact2ABName> in Contacts                             |
      | <Contact3WithUniqueUserName> | @<Contact3UniqueUserName>                                |
      | <Contact4WithCommonFriends>  | 1 person in common                                       |
      | <Contact5WithSameNameInAB>   | in Contacts                                              |

    Examples:
      | Name      | Contact1WithABEmail | Contact1ABName | Contact1UniqueUsername | Contact1Email | Contact2WithABPhoneNumber | Contact2ABName | Contact2PhoneNumber | Contact3WithUniqueUserName | Contact3UniqueUserName | Contact4WithCommonFriends | Contact5WithSameNameInAB | Contact5Email | Contact6Common | Contact7WoCF |
      | user1Name | user2Name           | user2ABName    | user2UniqueUsername    | user2Email    | user3Name                 | user3ABName    | user3PhoneNumber    | user4Name                  | user4UniqueUsername    | user5Name                 | user6Name                | user6Email    | user7Name      | user8Name    |

  @C352025 @regression @fastLogin
  Scenario Outline: Verify take over screen doesn't go away on app killing / reload
    Given There is 1 user
    Given User <Name> is me
    Given I prepare Wire to perform fast log in by email as <Name>
    Given I sign in using my email or phone number
    Given I open search UI
    Given I accept alert if visible
    Given I tap X button on Search UI page
    Given I see username <Name> on Unique Username Takeover page
    When I restart Wire
    And I accept alert if visible
    Then I see username <Name> on Unique Username Takeover page

    Examples:
      | Name      |
      | user1Name |

  @C352048 @regression @fastLogin
  Scenario Outline: Verify last username overwrites the previous one
    Given There are 2 users where <Name> is me
    Given User <Contact> sets the unique username
    Given I sign in using my email or phone number
    Given I wait until <ContactUsername> exists in backend search results
    Given I open search UI
    Given I accept alert if visible
    Given I tap input field on Search UI page
    When I type "<ContactUsername>" in Search UI input field
    Then I see the conversation "<Contact>" exists in Search results
    When User <Contact> changes the unique username to "<NewContactUsername>"
    And I wait until <NewContactUsername> exists in backend search results
    And I type "<NewContactUsername>" in cleared Search UI input field
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   | ContactUsername     | NewContactUsername  |
      | user1Name | user2Name | user2UniqueUsername | user3UniqueUsername |

  @C352061 @addressbookStart @forceReset @regression
  Scenario Outline: Verify user info from Contact List->Options (swipe)
    Given There are 8 users where <Name> is me
    Given Users <Name>,<Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact5WithSameNameInAB>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact6Common> upload own details
    Given Myself is connected to <Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact5WithSameNameInAB>,<Contact6Common>
    Given Myself sent connection request to <Contact7Unconnected>
    Given User <Contact3WithUniqueUserName> sets the unique username
    Given <Contact6Common> is connected to <Contact4WithCommonFriends>,<Contact7Unconnected>
    Given Myself has group chat <GroupChatName> with <Contact1WithABEmail>,<Contact2WithABPhoneNumber>
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I add name <Contact1ABName> and email <Contact1Email> to Address Book
    Given I add name <Contact2ABName> and phone <Contact2PhoneNumber> to Address Book
    Given I add name <Contact5WithSameNameInAB> and email <Contact5Email> to Address Book
    Given I restore Wire
    Given I sign in using my email
    Given I see conversations list
    Given I wait until <Contact4WithCommonFriends> has 1 common friend on the backend
    Given I wait until <Contact7Unconnected> has 1 common friend on the backend
    Given I open search UI
    Given I accept alert
    Given I tap X button on Search UI page
    When I swipe right on conversation <Contact7Unconnected>
    Then I do not see common friends count on Conversation actions page
    And I tap Cancel conversation action button
    When I swipe right on conversation <Contact1WithABEmail>
    Then I see Address Book name "<Contact1ABName>" on Conversation actions page
    And I do not see unique username on Conversation actions page
    And I tap Cancel conversation action button
    When I swipe right on conversation <Contact2WithABPhoneNumber>
    Then I see Address Book name "<Contact2ABName>" on Conversation actions page
    And I do not see unique username on Conversation actions page
    And I tap Cancel conversation action button
    When I swipe right on conversation <Contact3WithUniqueUserName>
    Then I see unique username "<Contact3UniqueUserName>" on Conversation actions page
    And I do not see Address Book name on Conversation actions page
    And I tap Cancel conversation action button
    When I swipe right on conversation <Contact4WithCommonFriends>
    Then I do not see unique username on Conversation actions page
    And I do not see common friends count on Conversation actions page
    And I tap Cancel conversation action button
    When I swipe right on conversation <Contact5WithSameNameInAB>
    Then I do not see unique username on Conversation actions page
    And I see Address Book name "" on Conversation actions page
    And I tap Cancel conversation action button
    When I swipe right on conversation <GroupChatName>
    Then I do not see Address Book name on Conversation actions page
    And I do not see unique username on Conversation actions page

    Examples:
      | Name      | Contact1WithABEmail | Contact1ABName | Contact1Email | Contact2WithABPhoneNumber | Contact2ABName | Contact2PhoneNumber | Contact3WithUniqueUserName | Contact3UniqueUserName | Contact4WithCommonFriends | Contact5WithSameNameInAB | Contact5Email | Contact6Common | Contact7Unconnected | GroupChatName |
      | user1Name | user2Name           | user2ABName    | user2Email    | user3Name                 | user3ABName    | user3PhoneNumber    | user4Name                  | user4UniqueUsername    | user5Name                 | user6Name                | user6Email    | user7Name      | user8Name           | Groupchat     |

  @C352053 @regression @fastLogin
  Scenario Outline: Verify common friends are shown even if I don't share the AB
    Given There are 4 users where <Name> is me
    Given User <Contact> sets the unique username
    Given <Contact> is connected to <Contact2>, <Contact3>
    Given Myself is connected to <Contact2>, <Contact3>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    Given I wait until <Contact> has 2 common friends on the backend
    Given I open search UI
    When I dismiss alert if visible
    Then I verify correct details are shown for the found users
      | Name      | Details                                       |
      | <Contact> | @<ContactUniqueUserName> · 2 people in common |

    Examples:
      | Name      | Contact   | ContactUniqueUserName | Contact2  | Contact3  |
      | user1Name | user2Name | user2UniqueUsername   | user3Name | user4Name |

  @C352062 @addressbookStart @forceReset @regression
  Scenario Outline: Verify connected user in 1-to-1 conversation view
    Given There are 7 users where <Name> is me
    Given Users <Name>,<Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact5WithSameNameInAB>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact6Common> upload own details
    Given Myself is connected to <Contact1WithABEmail>,<Contact2WithABPhoneNumber>,<Contact3WithUniqueUserName>,<Contact4WithCommonFriends>,<Contact5WithSameNameInAB>,<Contact6Common>
    Given User <Contact3WithUniqueUserName> sets the unique username
    Given <Contact4WithCommonFriends> is connected to <Contact6Common>
    Given I minimize Wire
    Given I install Address Book Helper app
    Given I launch Address Book Helper app
    Given I add name <Contact1ABName> and email <Contact1Email> to Address Book
    Given I add name <Contact2ABName> and phone <Contact2PhoneNumber> to Address Book
    Given I add name <Contact5WithSameNameInAB> and email <Contact5Email> to Address Book
    Given I restore Wire
    Given I sign in using my email
    Given I see conversations list
    Given I wait until <Contact4WithCommonFriends> has 1 common friend on the backend
    Given I open search UI
    Given I accept alert
    Given I tap X button on Search UI page
    When I tap on contact name <Contact3WithUniqueUserName>
    Then I see unique username "<Contact3UniqueUserName>" on Conversation view page
    And I do not see Address Book name on Single user Pending outgoing connection page
    When I navigate back to conversations list
    And I tap on contact name <Contact1WithABEmail>
    Then I see Address Book name "<Contact1ABName>" on Conversation view page
    And I do not see unique username on Conversation view page
    When I navigate back to conversations list
    And I tap on contact name <Contact2WithABPhoneNumber>
    Then I see Address Book name "<Contact2ABName>" on Conversation view page
    And I do not see unique username on Single user profile page
    When I navigate back to conversations list
    And I tap on contact name <Contact4WithCommonFriends>
    Then I do not see unique username on Conversation view page
    And I do not see common friends count on Conversation view page
    When I navigate back to conversations list
    And I tap on contact name <Contact5WithSameNameInAB>
    Then I do not see unique username on Conversation view page
    And I see Address Book name "" on Conversation view page

    Examples:
      | Name      | Contact1WithABEmail | Contact1ABName | Contact1Email | Contact2WithABPhoneNumber | Contact2ABName | Contact2PhoneNumber | Contact3WithUniqueUserName | Contact3UniqueUserName | Contact4WithCommonFriends | Contact5WithSameNameInAB | Contact5Email | Contact6Common |
      | user1Name | user2Name           | user2ABName    | user2Email    | user3Name                 | user3ABName    | user3PhoneNumber    | user4Name                  | user4UniqueUsername    | user5Name                 | user6Name                | user6Email    | user7Name      |

  @C352045 @regression @fastLogin
  Scenario Outline: Verify new username is synced across the devices
    Given There are 2 users where <Name> is me
    Given Myself is connected to user2Name
    Given I sign in using my email or phone number
    Given User adds the following device: {"Myself": [{"name": "<DeviceName>"}]}
    Given I accept alert if visible
    Given I tap settings gear button
    Given I accept alert if visible
    When I select settings item Account
    Then I see "<UniqueUsername>" unique username is displayed on Settings Page
    And I tap Back navigation button on Settings page
    When User Me changes the unique username to "<NewUniqueUsername>" via device <DeviceName>
    And I select settings item Account
    Then I see "<NewUniqueUsername>" unique username is displayed on Settings Page

    Examples:
      | Name      | UniqueUsername      | NewUniqueUsername   | DeviceName |
      | user1Name | user1UniqueUsername | user3UniqueUsername | Device1    |

  @C352032 @regression @fastLogin
  Scenario Outline: Verify autogeneration of a username for a user with emoji in a name
    Given There is 1 user
    Given User <NameAlias> is me
    Given User <NameAlias> changes name to <Name>
    Given I prepare Wire to perform fast log in by email as <Name>
    Given I sign in using my email or phone number
    Given I open search UI
    Given I accept alert if visible
    Given I tap X button on Search UI page
    When I see username <NameAlias> on Unique Username Takeover page
    Then I see unique username on Unique Username Takeover page contains latin characters only

    Examples:
      | NameAlias | Name |
      | user1Name | 😼   |

  @C368978 @regression @fastLogin
  Scenario Outline: Verify impossibility of autogenerating reserved username (e.g. admin)
    Given There is 1 user
    Given User <NameAlias> is me
    Given User <NameAlias> changes name to <Name>
    Given I prepare Wire to perform fast log in by email as <Name>
    Given I sign in using my email or phone number
    Given I open search UI
    Given I accept alert if visible
    Given I tap X button on Search UI page
    When I see username <NameAlias> on Unique Username Takeover page
    Then I do not see unique username <Name> on Unique Username Takeover page
    When I remember the unique username on Unique Username Takeover page
    When I tap Keep This One button on Unique Username Takeover page
    # Wait for animation
    And I wait for 1 second
    Then I verify my unique username on the backend is equal to the one which was visible on Unique Username Takeover page

    Examples:
      | NameAlias | Name  |
      | user1Name | admin |
