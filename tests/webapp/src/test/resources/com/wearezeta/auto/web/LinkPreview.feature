Feature: Link Preview

  @C169241 @linkpreview @regression
  Scenario Outline: Verify you can see preview for link sent from mobile
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Link> via device Device1 to user me
    Then I see link <LinkInPreview> in link preview message
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see a picture <LinkPreviewImage> from link preview
    When I open conversation with <Contact2>
    And I see 1 messages in conversation
    When I open conversation with <Contact>
    Then I see link <LinkInPreview> in link preview message
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see a picture <LinkPreviewImage> from link preview

    Examples:
      | Login      | Password      | Name      | Contact   | Contact2  | Link                                                                                                               | LinkInPreview                                                                                           | LinkTitle                                                                                   | LinkPreviewImage |
      | user1Email | user1Password | user1Name | user2Name | user3Name | https://wire.com                                                                                                   | wire.com                                                                                                | Wire · Modern communication, full privacy. For iOS, Android, macOS, Windows, Linux and web. | linkpreview0.png |
      | user1Email | user1Password | user1Name | user2Name | user3Name | http://www.heise.de/newsticker/meldung/Wire-Neuer-WebRTC-Messenger-soll-WhatsApp-Co-Konkurrenz-machen-2477770.html | heise.de/newsticker/meldung/Wire-Neuer-WebRTC-Messenger-soll-WhatsApp-Co-Konkurrenz-machen-2477770.html | Wire: Neuer WebRTC-Messenger soll WhatsApp &amp; Co. Konkurrenz machen                      | linkpreview1.png |

  @C169235 @linkpreview @regression
  Scenario Outline: Verify you can delete link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Link> via device Device1 to user me
    Then I see link <LinkInPreview> in link preview message
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see a picture <LinkPreviewImage> from link preview
    And I see 2 messages in conversation
    When I click context menu of the last message
    And I click to delete message for me in context menu
    And I click confirm to delete message for me
    And I see 1 messages in conversation
    Then I do not see a title <LinkTitle> in link preview in the conversation view
    And I do not see a picture <LinkPreviewImage> from link preview

    Examples:
      | Login      | Password      | Name      | Contact   | Link             | LinkInPreview | LinkTitle                                                                                   | LinkPreviewImage |
      | user1Email | user1Password | user1Name | user2Name | https://wire.com | wire.com      | Wire · Modern communication, full privacy. For iOS, Android, macOS, Windows, Linux and web. | linkpreview0.png |

  @C234615 @linkpreview @edit @regression @WEBAPP-3179
  Scenario Outline: Verify sender can edit link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Link> via device Device1 to user me
    Then I see link <LinkInPreview> in link preview message
    When User <Contact> edits the recent message to "<EditedMessage>" from user me via device Device1
    And I see text message <EditedMessage>
    Then I do not see latest message is link preview message
    And I write message <Message>
    And I send message
    Then I see text message <Message>
    When User <Contact> edits the second last message to "<Link>" from user me via device Device1
    Then I see link <LinkInPreview> in link preview message
    Then I do not see latest message is link preview message

    Examples:
      | Login      | Password      | Name      | Contact   | Message       | EditedMessage | Link             | LinkInPreview |
      | user1Email | user1Password | user1Name | user2Name | other message | edited        | https://wire.com | wire.com      |

  @C169238 @linkpreview @regression
  Scenario Outline: I see link preview for link mixed with text
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    And Contact <Contact> sends message <TextWithLink> via device Device1 to user me
    Then I see link <LinkInPreview> in link preview message
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see a picture <LinkPreviewImage> from link preview
    And I see text message <TextWithLink>

    Examples:
      | Login      | Password      | Name      | Contact   | TextWithLink                                          | LinkInPreview | LinkTitle                                                                                   | LinkPreviewImage |
      | user1Email | user1Password | user1Name | user2Name | You can go to wire.com and download the best app ever | wire.com      | Wire · Modern communication, full privacy. For iOS, Android, macOS, Windows, Linux and web. | linkpreview0.png |