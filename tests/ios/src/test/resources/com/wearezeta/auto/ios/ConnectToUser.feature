Feature: Connect to User

  #Need to rework test according to updates in UI
  @mute
  @smoke
  @id345
  Scenario Outline: Send invitation message to a user
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I input message in connect to dialog
    Then I see first item in contact list named <Contact>
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page

    Examples: 
      | Login   | Password    | Name    | Contact  |
      | aqaUser | aqaPassword | aqaUser | yourUser |

  #Need to rework test according to updates in UI
  @mute 
  @smoke 
  @id337
  Scenario Outline: Get invitation message from user
    Given I have connection request from <Contact>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I see connection request from <Contact>
    And I confirm connection request
    Then I see first item in contact list named <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | yourContact |

#Muted due to relogin issue
@staging
@id611
  Scenario Outline: Verify 1:1 conversation is not created on the second end after you ignore connection request(UI)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I input message in connect to dialog
    And I click Send button on connect to dialog
    And I see People picker page
    And I click clear button
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I see Personal page
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I click on Ignore button on Pending requests page
    And I dont see Pending request link in contact list
    And I don't see conversation with not connected user <Name>
    And I tap on my name <Contact>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Name> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see conversation with not connected user <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact  |
      | aqaUser | aqaPassword | aqaUser | yourUser |


#Muted due to relogin issue
@staging 
@id611
Scenario Outline: Verify 1:1 conversation is not created on the second end after you ignore connection request(BE)
    Given I send invitation to <Name> by <Contact>
    And I Sign in using login <Name> and password <Password>
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I click on Ignore button on Pending requests page
    And I dont see Pending request link in contact list
    And I don't see conversation with not connected user <Contact>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see conversation with not connected user <Name>
    
Examples: 
      | Login   | Password    | Name    | Contact     		|
      | aqaUser | aqaPassword | aqaUser | yourNotContact1 	|
      

#Muted due relogin issue and blank Personal page screen issue
@staging 
@id610
Scenario Outline: Verify 1:1 conversation is successfully created on the second end after you accept connection request(BE)
    Given I send invitation to <Name> by <Contact>
    And I Sign in using login <Name> and password <Password>
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I click Connect button on Pending request page
    And I dont see Pending request link in contact list
    And I see conversation with not connected user <Contact>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see conversation with not connected user <Name>
    
Examples: 
      | Login   | Password    | Name    | Contact     		|
      | aqaUser | aqaPassword | aqaUser | yourNotContact1 	|