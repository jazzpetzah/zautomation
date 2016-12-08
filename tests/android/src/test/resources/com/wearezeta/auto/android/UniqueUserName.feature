Feature: Unique Username

  @C352018 @staging
  Scenario Outline: Verify I see Unique User Name and AB name within Incoming Request and Conversation View in different condition
    Given I delete all contacts from Address Book
    Given There are 5 users where <Name> is me
    Given I add <ContactInABEmail> having custom name "<ABNameEmail>" into Address Book with email
    Given I add <ContactInABPhone> having custom name "<ABNamePhone>" into Address Book with phone
    Given I add <ContactInABSameName> into Address Book with email
    Given User <ContactInABEmail> sets the unique username
    Given User <ContactInABPhone> sets the unique username
    Given User <Connected> sets the unique username
    Given Myself is connected to <Connected>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list
    # Connect in AB with Email
    When <ContactInABEmail> sent connection request to me
    And I tap on conversation name <WaitingMess1>
    Then I see unique user name of user <ContactInABEmail> on Single pending incoming connection page
    And I see user info "<ABNameEmail> in Address Book" on Single pending incoming connection page
    When I tap connect button for user <ContactInABEmail> on Single pending incoming connection page
    # Connect in AB with Phone
    When <ContactInABPhone> sent connection request to me
    And I tap on conversation name <WaitingMess1>
    And I see unique user name of user <ContactInABPhone> on Single pending incoming connection page
    # And I see user info "<ABNamePhone> in Address Book" on Single pending incoming connection page
    And I tap connect button for user <ContactInABPhone> on Single pending incoming connection page
    # Connect in AB with Same Name
    When <ContactInABSameName> sent connection request to me
    And I tap on conversation name <WaitingMess1>
    Then I do not see unique user name of user <ContactInABSameName> on Single pending incoming connection page
    And I see user info "in Address Book" on Single pending incoming connection page
    And I tap connect button for user <ContactInABSameName> on Single pending incoming connection page

    Examples:
      | Name      | ContactInABEmail | ContactInABPhone | ContactInABSameName | Connected | WaitingMess1     | ABNameEmail | ABNamePhone |
      | user1Name | user2Name        | user3Name        | user4Name           | user5Name | 1 person waiting | Email       | Phone       |

  @C352019 @staging
  Scenario Outline: (AN-4758) Verify number of common friends is shown on the incoming connection request
    Given I delete all contacts from Address Book
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given Myself is connected to <Contact2>,<Contact3>
    Given <Contact1> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with name <WaitingMess1>
    Given Myself wait until <CommonContact> common contacts with <Contact1> exists in backend
    When I tap on conversation name <WaitingMess1>
    Then I see user info "<CommonContact> people in common" on Single pending incoming connection page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | WaitingMess1     | CommonContact |
      | user1Name | user2Name | user3Name | user4Name | 1 person waiting | 2             |

  @C352020 @staging
  Scenario Outline: Verify I see Unique User Name and AB name within outgoing Request in different condition
    Given I delete all contacts from Address Book
    Given There are 4 users where <Name> is me
    Given I add <ContactInABEmail> having custom name "<ABNameEmail>" into Address Book with email
    Given I add <ContactInABPhone> having custom name "<ABNamePhone>" into Address Book with phone
    Given I add <ContactInABSameName> into Address Book with phone and email
    Given User <ContactInABEmail> sets the unique username
    Given User <ContactInABPhone> sets the unique username
    Given Myself sent connection request to <ContactInABEmail>
    Given Myself sent connection request to <ContactInABPhone>
    Given Myself sent connection request to <ContactInABSameName>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    # Contact In AB with Email
    When I tap on conversation name <ContactInABEmail>
    Then I see user name of user <ContactInABEmail> on Single pending outgoing connection page
    And I see unique user name of user <ContactInABEmail> on Single pending outgoing connection page
    And I see user info "<ABNameEmail> in Address Book" on Single pending outgoing connection page
    And I tap Back button
    # Contact In AB with Phone
    When I tap on conversation name <ContactInABPhone>
    Then I see user name of user <ContactInABPhone> on Single pending outgoing connection page
    And I see unique user name of user <ContactInABPhone> on Single pending outgoing connection page
    # And I see user info "<ABNamePhone> in Address Book" on Single pending outgoing connection page
    And I tap Back button
    # Contact in AB with Same name
    When I tap on conversation name <ContactInABSameName>
    Then I see user name of user <ContactInABSameName> on Single pending outgoing connection page
    And I do not see unique user name of user <ContactInABSameName> on Single pending outgoing connection page
    And I see user info "in Address Book" on Single pending outgoing connection page

    Examples:
      | Name      | ContactInABEmail | ContactInABPhone | ContactInABSameName | ABNameEmail | ABNamePhone |
      | user1Name | user2Name        | user3Name        | user4Name           | Email       | Phone       |

  @C352021 @staging
  Scenario Outline: Verify number of common friends is shown on the outgoing connection request
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself sent connection request to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given Myself wait until <CommonContact> common contacts with <Contact1> exists in backend
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on Search page <Contact1>
    Then I see user info "<CommonContact> people in common" on Single unconnected user details page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | CommonContact |
      | user1Name | user2Name | user3Name | user4Name | 2             |

  @C352072 @staging
  Scenario Outline: (AN-4760) Verify search works with username and doesn't work with email
    Given There are 2 users where <Name> is me
    Given User <Contact2> sets the unique username
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list
    When I open Search UI
    And I type user email "<Contact2Email>" in search field
    Then I do not see user <Contact2> in Search result list
    And I tap Clear button
    When I open Search UI
    And I type unique user name "<Contact2UniqueUsername>" in search field
    Then I see user <Contact2> in Search result list

    Examples:
      | Name      | Contact2  | Contact2Email | Contact2UniqueUsername |
      | user1Name | user2Name | user2Email    | user2UniqueUsername    |

  @C352075 @staging
  Scenario Outline: Verify search shows correct user info for unconnected user
    Given There are 9 users where <Name> is me
    Given I add <Contact1InABWithCF> having custom name "<Contact1NameInAB>" into Address Book with email
    Given I add <Contact2InABWoCF> having custom name "<Contact2NameInAB>" into Address Book with email
    Given I add <Contact5SameName> into Address Book with email
    Given Myself is connected to <CF1>,<CF2>,<CF3>
    Given <Contact1InABWithCF> is connected to <CF1>,<CF2>
    Given <Contact3WithCF> is connected to <CF2>,<CF3>
    Given <Contact5SameName> is connected to <CF3>
    Given Myself wait until 2 common contacts with <Contact1InABWithCF> exists in backend
    Given Myself wait until 2 common contacts with <Contact3WithCF> exists in backend
    Given Myself wait until 1 common contacts with <Contact5SameName> exists in backend
    When I sign in using my email or phone number
    And I accept First Time overlay as soon as it is visible
    And I see Conversations list
    And I open Search UI
    Then I verify results in search page, according to datatable
      | Name                 | UserInfo                           |
      | <Contact1InABWithCF> | <Contact1NameInAB> in Address Book |
      | <Contact2InABWoCF>   | <Contact2NameInAB> in Address Book |
      | <Contact3WithCF>     | 2 people in common                 |
      | <Contact4WoCF>       |                                    |
      | <Contact5SameName>   | in Address Book                    |

    Examples:
      | Name      | Contact1InABWithCF | Contact1NameInAB | Contact2InABWoCF | Contact2NameInAB | Contact3WithCF | Contact4WoCF | Contact5SameName | CF1       | CF2       | CF3       |
      | user1Name | user2Name          | user2ABName      | user3Name        | user3ABName      | user4Name      | user5Name    | user6Name        | user7Name | user8Name | user9Name |

  @C352086 @staging
  Scenario Outline: Verify I can see unregistered person from AB in Contacts UI
    Given There are 1 users where <Name> is me
    Given I add <Contact1InABWithCF> having custom name "<Contact1NameInAB>" into Address Book with email
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I open Search UI
    Then I see user <Contact1NameInAB> in Contact list

    Examples:
      | Name      | Contact1InABWithCF | Contact1NameInAB |
      | user1Name | user2Name          | user2ABName      |

  @C352076 @staging
  Scenario Outline: Verify search shows correct user info for connected user
    Given There are 9 users where <Name> is me
    Given I add <Contact1InABWithCF> having custom name "<Contact1NameInAB>" into Address Book with email
    Given I add <Contact2InABWoCF> having custom name "<Contact2NameInAB>" into Address Book with email
    Given I add <Contact5SameName> into Address Book with email
    Given Myself is connected to <Contact1InABWithCF>,<Contact2InABWoCF>,<Contact3WithCF>,<Contact4WoCF>,<Contact5SameName>,<CF1>,<CF2>,<CF3>
    Given <Contact1InABWithCF> is connected to <CF1>,<CF2>
    Given <Contact3WithCF> is connected to <CF2>,<CF3>
    Given <Contact5SameName> is connected to <CF3>
    Given User <Contact3WithCF> sets the unique username
    Given User <Contact2InABWoCF> sets the unique username
    Given Myself wait until 2 common contacts with <Contact1InABWithCF> exists in backend
    Given Myself wait until 2 common contacts with <Contact3WithCF> exists in backend
    Given Myself wait until 1 common contacts with <Contact5SameName> exists in backend
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list
    When I open Search UI
    Then I verify results in search page, according to datatable
      | Name                 | UserInfo                                                       |
      | <Contact1InABWithCF> | <Contact1NameInAB> in Address Book                             |
      | <Contact2InABWoCF>   | @<Contact2UniqueUsername> - <Contact2NameInAB> in Address Book |
      | <Contact3WithCF>     | @<Contact3UniqueUsername>                                      |
      | <Contact4WoCF>       |                                                                |
      | <Contact5SameName>   | in Address Book                                                |

    Examples:
      | Name      | Contact1InABWithCF | Contact1NameInAB | Contact2InABWoCF | Contact2NameInAB | Contact3WithCF | Contact3UniqueUsername | Contact4WoCF | Contact5SameName | Contact2UniqueUsername | CF1       | CF2       | CF3       |
      | user1Name | user2Name          | user2ABName      | user3Name        | user3ABName      | user4Name      | user4UniqueUsername    | user5Name    | user6Name        | user3UniqueUsername    | user7Name | user8Name | user9Name |