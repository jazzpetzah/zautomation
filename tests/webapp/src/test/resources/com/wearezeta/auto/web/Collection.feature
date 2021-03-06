Feature: Collection

  @C378049 @collection @localytics @regression
  Scenario Outline: Verify message is shown if no media is in collection
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click collection button in conversation
    And I see localytics event <Event> with attributes <Attributes>
    Then I see info about no collection items

    Examples:
      | Email      | Password      | Name      | Contact   | Event                          | Attributes                                                                   |
      | user1Email | user1Password | user1Name | user2Name | collections.opened_collections | {\"is_empty\":true,\"conversation_type\":\"one_to_one\",\"with_bot\":false}" |

  @C378050 @linkpreview @collection @regression
  Scenario Outline: Verify main overview shows media from all categories
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I send picture <Picture> to the current conversation
    And I send audio file with length <Time> and name <File> to the current conversation
    And I send <FileSize> sized file with name <FileName> to the current conversation
    And Contact <Contact> sends message <Link> via device Device1 to user me
    And I see link <LinkInPreview> in link preview message
    And I click collection button in conversation
    Then I see 1 picture in collection
    And I see 1 audio in collection
    And I see 1 file in collection
    And I see 1 link in collection

    Examples:
      | Email      | Password      | Name      | Contact   | Picture                   | FileSize | FileName        | Link                                                                                                               | LinkInPreview                                                                                           | File        | Time  |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg | 1MB      | collections.txt | http://www.heise.de/newsticker/meldung/Wire-Neuer-WebRTC-Messenger-soll-WhatsApp-Co-Konkurrenz-machen-2477770.html | heise.de/newsticker/meldung/Wire-Neuer-WebRTC-Messenger-soll-WhatsApp-Co-Konkurrenz-machen-2477770.html | example.wav | 00:20 |

  @C378052 @collection @regression
  Scenario Outline: Verify no pictures from different conversations are in the overview
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact1>
    And User <Contact1> sends image <Picture1> to single user conversation <Name>
    And I send picture <Picture1> to the current conversation
    Then I see only 2 pictures in the conversation
    When I click collection button in conversation
    Then I see 2 picture in collection
    When I open conversation with <Contact2>
    And I click collection button in conversation
    Then I see info about no collection items

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | Picture1                  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | userpicture_landscape.jpg |

  @C378053 @collection @regression
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

  @C378054 @collection @ephemeral @regression
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
      | user1Email | user1Password | user1Name | user2Name | 30   | 30 seconds | s             | userpicture_landscape.jpg | C261733.mp4 | 1 MB      | example.wav | 00:20     | C261733.zip  | 512KB    |

  @C378055 @collection @regression
  Scenario Outline: Verify opening overview of all pictures from sender and receiver in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    When User <Contact1> sends 6 images <Picture> to group conversation <ChatName>
    And I am signed in properly
    When I open conversation with <ChatName>
    When User <Contact2> sends 8 images <Picture2> to group conversation <ChatName>
    And I send picture <Picture> to the current conversation
    #And I see sent picture <Picture> in the conversation view
    And I wait for 10 seconds
    And I see 16 messages in conversation
    And I click collection button in conversation
    Then I see 15 pictures in collection
    When I click on Show all pictures button in collections
    Then I see 15 pictures in pictures details in collections
    And I see conversation <ChatName> is on the top
    When I click on back button on collection details page
    Then I see 15 pictures in collection

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | ChatName   | Picture                   | Picture2                 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | group conv | userpicture_landscape.jpg | userpicture_portrait.jpg |

  @C382595 @collection @regression
  Scenario Outline: Verify opening overview of all links
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Link> via device Device1 to user me
    Then I see link <LinkInPreview> in link preview message
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see a picture <LinkPreviewImage> from link preview
    And I see 2 messages in conversation
    When I click collection button in conversation
    Then I see 1 link in collection
    And I see link with link <LinkInPreview> in collection overview
    And I see link with title <LinkTitle> in collection overview
    And I see link with image <LinkPreviewImage> in collection overview
    And I see link from contact <Contact> in collection overview
    When I close collection overview
    And Contact <Contact> sends message <LinkWithoutImage> via device Device1 to user me
    Then I see link <LinkWithoutImage> in link preview message
    And I see 3 messages in conversation
    When I click collection button in conversation
    Then I see 2 link in collection
    And I see link with link <LinkWithoutImage> in collection overview
    And I see link with title <LinkTitleWithoutImage> in collection overview
    And I see link from contact <Contact> in collection overview

    Examples:
      | Email      | Password      | Name      | Contact   | Link                 | LinkInPreview | LinkTitle | LinkPreviewImage | LinkWithoutImage                                                              | LinkTitleWithoutImage                                 |
      | user1Email | user1Password | user1Name | user2Name | https://app.wire.com | app.wire.com  | Wire      | linkpreview0.png | medium.com/wire-news/simple-privacy-policy-72-hour-log-retention-33d183ea0fb3 | Simpler Privacy Policy, 72h log retention – Wire News |

  @C378056 @collection @localytics @regression
  Scenario Outline: Verify opening single picture from all shared media overview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation
    When I click collection button in conversation
    And I see 1 picture in collection
    And I see localytics event <Event1> with attributes <Attributes1>
    And I click on picture 1 in collection
    Then I see picture <PictureName> in picture fullscreen
    And I see a name <Name> in picture fullscreen
    And I see a timestamp in picture fullscreen
    And I see localytics event <Event2> with attributes <Attributes2>

    Examples:
      | Email      | Password      | Name      | Contact   | PictureName               | Event1                         | Attributes1                                                                   | Event2                  | Attributes2                                                                   |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg | collections.opened_collections | {\"is_empty\":false,\"conversation_type\":\"one_to_one\",\"with_bot\":false}" | collections.opened_item | {\"type\":\"image\",\"conversation_type\":\"one_to_one\",\"with_bot\":false}" |

  @C378058 @collection @regression
  Scenario Outline: Verify opening overview of all files
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send <FileSize> sized file with name collections1.txt to the current conversation
    Then I see file transfer for file collections1.txt in the conversation view
    When I send <FileSize> sized file with name collections2.txt to the current conversation
    Then I see file transfer for file collections2.txt in the conversation view
    When I send <FileSize> sized file with name collections3.txt to the current conversation
    Then I see file transfer for file collections3.txt in the conversation view
    When I send <FileSize> sized file with name collections4.txt to the current conversation
    Then I see file transfer for file collections4.txt in the conversation view
    When I send <FileSize> sized file with name collections5.txt to the current conversation
    Then I see file transfer for file collections5.txt in the conversation view
    And I see 6 messages in conversation
    When I click collection button in conversation
    And I see <Amount> files in collection
    And I click show all files label
    Then I see <Amount> files in files detail page

    Examples:
      | Email      | Password      | Name      | Contact   | FileSize | Amount |
      | user1Email | user1Password | user1Name | user2Name | 700KB    | 5      |

  @C399357 @collection @regression
  Scenario Outline: Verify deleted media isn't in collection on other side
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I switch to Sign in page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And User <Contact> sends image <PictureName> to single user conversation <Name>
    Then I see only 1 pictures in the conversation
    When I click collection button in conversation
    And I see 1 pictures in collection
    And I close collection overview
    And User <Contact> deletes the recent 1 messages everywhere in single conversation <Name> via device Device1
    Then I see only 0 pictures in the conversation
    And I click collection button in conversation
    And I see info about no collection items

    Examples:
      | Email      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |

  @C421364 @collection @regression
  Scenario Outline: Verify opening overview of all audio files from sender and receiver in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    When User <Contact1> sends 3 audio files <AudioFile> via Device1 to group conversation <ChatName>
    And I am signed in properly
    When I open conversation with <ChatName>
    When User <Contact2> sends 3 audio files <AudioFile> via Device2 to group conversation <ChatName>
    And I send audio file with length <AudioTime> and name <AudioFile> to the current conversation
    And I wait for 10 seconds
    And I see 8 messages in conversation
    And I click collection button in conversation
    Then I see 7 audios in collection
    When I click on Show all audio files button in collections
    Then I see 7 audios in audio details in collections
    And I see conversation <ChatName> is on the top
    When I click on back button on collection details page
    Then I see 7 audios in collection

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | ChatName   | AudioFile   | AudioTime |
      | user1Email | user1Password | user1Name | user2Name | user3Name | group conv | example.wav | 00:20     |
