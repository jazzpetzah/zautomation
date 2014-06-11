Feature: Send Message

  Scenario Outline: Send Message to user from my contact list
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message and send it
    Then I see my message in the dialog

    Examples:     
    |	Login						|	Password	|	Name			|	Contact		|
    |	piotr.iazadji@wearezeta.com	|	asdfer123	|	Piotr Iazadji	|	Piotr.mqa2	|


    Scenario Outline: Say Hello to user from contact list
		Given I Sign in using login <Login> and password <Password>
    	And I see Contact list with my name <Name>
    	When I tap on name <Contact>
    	And I see dialog page
    	And I multi tap on text input on dialog screen
    	Then I see Hello message in the dialog
    	
	Examples: 
    |	Login						|	Password	|	Name			|	Contact		|
    |	piotr.iazadji@wearezeta.com	|	asdfer123	|	Piotr Iazadji	|	Piotr.mqa2	|
    
#	Scenario Outline: Start group chat with users from contact list
#		Given I Sign in using login <Login> and password <Password>
#    	And I see Contact list with my name <Name>
#   	When I tap on name <Contact>
#    	And I see dialog page
#   	And I swipe left