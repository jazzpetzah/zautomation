Feature: Collections

  @C378049 @collection @staging
  Scenario Outline: Verify message is shown if no media is in collection
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click collection button in conversation
    #And I see localytics event <Event> without attributes
    Then I see info about no collection items

    Examples:
      | Email      | Password      | Name      | Contact   | Event                          |
      | user1Email | user1Password | user1Name | user2Name | collections.opened_collections |

  @C378050 @linkpreview @collection @staging
  Scenario Outline: Verify main overview shows media from all categories
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I send picture <Picture> to the current conversation
    And I send <FileSize> sized file with name <FileName> to the current conversation
    And Contact <Contact> sends message <Link> via device Device1 to user me
    And I see link <LinkInPreview> in link preview message
    And I click collection button in conversation
    Then I see 1 picture in collection
    And I see 1 file in collection
    And I see 1 link in collection

    Examples:
      | Email      | Password      | Name      | Contact   | Picture                   | FileSize | FileName        | Link                                                                                                               | LinkInPreview                                                                                           |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg | 1MB      | collections.txt | http://www.heise.de/newsticker/meldung/Wire-Neuer-WebRTC-Messenger-soll-WhatsApp-Co-Konkurrenz-machen-2477770.html | heise.de/newsticker/meldung/Wire-Neuer-WebRTC-Messenger-soll-WhatsApp-Co-Konkurrenz-machen-2477770.html |

  @C378052 @collection @staging
  Scenario Outline: Verify no pictures from different conversations are in the overview
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact1>
    And I send picture <Picture> to the current conversation
    And User <Contact1> sends image <Picture> to single user conversation <Name>
    Then I see only 2 pictures in the conversation
    When I click collection button in conversation
    Then I see 2 picture in collection
    When I open conversation with <Contact2>
    And I click collection button in conversation
    Then I see info about no collection items

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | Picture                   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | userpicture_landscape.jpg |

  @C378053 @collection @staging
  Scenario Outline: Verify GIF pictures are not presented in library
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send picture <Gif> to the current conversation
    And I wait for 5 seconds
    And I write message <Text>
    And I click GIF button
    Then I see Giphy popup
    And I verify that the search of the Giphy popup contains <Text>
    And I see gif image in Giphy popup
    When I click send button in Giphy popup
    Then I see sent gif in the conversation view
    And I see only 2 pictures in the conversation
    When I click collection button in conversation
    Then I see info about no collection items

    Examples:
      | Email      | Password      | Name      | Contact   | Gif         | Text |
      | user1Email | user1Password | user1Name | user2Name | example.gif | test |

  @C378054 @collection @staging
  Scenario Outline: Verify ephemeral messages aren't shown in collection
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
#timer
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
#picture
    When I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I see 2 messages in conversation
#video
    When I see file transfer button in conversation input
    When I send <SizeVideo> sized video with name <VideoFile> to the current conversation
    And I wait until video <VideoFile> is uploaded completely
    And I see video message <VideoFile> in the conversation view
    And I see timer next to the last message
    When I wait for <Time> seconds
    And I do not see video message <VideoFile> in the conversation view
    Then I see block replaces the last message in the conversation view
    And I see 3 messages in conversation
#audio
    When I send audio file with length <AudioTime> and name <AudioFile> to the current conversation
    And I wait until audio <AudioFile> is uploaded completely
    Then I see audio message <AudioFile> in the conversation view
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I see 4 messages in conversation
 #file
    When I send <SizeFile> sized file with name <File> to the current conversation
    And I wait until file <File> is uploaded completely
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I see 5 messages in conversation
    When I click collection button in conversation
    Then I see info about no collection items

    Examples:
      | Email      | Password      | Name      | Contact   | Time | TimeLong   | TimeShortUnit | PictureName               | VideoFile   | SizeVideo | AudioFile   | AudioTime | File         | SizeFile |
      | user1Email | user1Password | user1Name | user2Name | 5    | 5 seconds  | s             | userpicture_landscape.jpg | C261733.mp4 | 1 MB      | example.wav | 00:20     | C261733.zip  | 512KB    |

  @C378056 @collection @staging
  Scenario Outline: Verify opening single picture from all shared media overview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation
    When I click collection button in conversation
    And I see 1 picture in collection
    And I click on picture 1 in collection
    Then I see picture <PictureName> in fullscreen

    Examples:
      | Email      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |