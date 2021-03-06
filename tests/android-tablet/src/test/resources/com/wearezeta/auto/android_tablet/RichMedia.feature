Feature: Rich Media

  @C759 @regression @rc
  Scenario Outline: Send GIF format pic (portrait)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given User <Contact> sends encrypted image <GifName> to single user conversation Myself
    And I tap on conversation name <Contact>
    When I scroll to the bottom of the conversation view
    Then I see a picture in the conversation view
    And I see the picture in the conversation view is animated
    When I tap Image container in the conversation view
    And I tap on Fullscreen button on the recent image in the conversation view
    Then I see the picture in the preview is animated

    Examples:
      | Name      | Contact   | GifName      |
      | user1Name | user2Name | animated.gif |

  @C796 @regression @rc
  Scenario Outline: Send GIF format pic (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given User <Contact> sends encrypted image <GifName> to single user conversation Myself
    And I tap on conversation name <Contact>
    When I scroll to the bottom of the conversation view
    Then I see a picture in the conversation view
    And I see the picture in the conversation view is animated
    When I tap Image container in the conversation view
    And I tap on Fullscreen button on the recent image in the conversation view
    Then I see the picture in the preview is animated

    Examples:
      | Name      | Contact   | GifName      |
      | user1Name | user2Name | animated.gif |

  @C774 @soundcloud
  Scenario Outline: Verify you can play/pause media from the conversation list (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I remember the coordinates of conversation item <Contact>
    And I tap on conversation name <Contact>
    And I tap on text input
    When I type the message "<SoundCloudLink>" in the conversation view
    And I send the typed message by cursor Send button in the conversation view
    And I scroll to the bottom of the conversation view
    And I tap Play button in the conversation view
    And I swipe right to show the conversations list
    Then I see Pause button next to the conversation name <Contact>
    When I remember the state of Pause button next to the conversation name <Contact>
    And I tap Pause button next to the conversation name <Contact>
    Then I see the state of Pause button next to the conversation name <Contact> is changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                                      |
      | user1Name | user2Name | https://soundcloud.com/scottisbell/scott-isbell-tonight-feat-adessi |

  @C798 @soundcloud
  Scenario Outline: (AN-3300) Verify you can play/pause media from the conversation list (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I remember the coordinates of conversation item <Contact>
    And I tap on conversation name <Contact>
    And I tap on text input
    When I type the message "<SoundCloudLink>" in the conversation view
    And I send the typed message by cursor Send button in the conversation view
    And I scroll to the bottom of the conversation view
    And I tap Play button in the conversation view
    Then I see Pause button next to the conversation name <Contact>
    When I remember the state of Pause button next to the conversation name <Contact>
    And I tap Pause button next to the conversation name <Contact>
    Then I see the state of Pause button next to the conversation name <Contact> is changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                                      |
      | user1Name | user2Name | https://soundcloud.com/scottisbell/scott-isbell-tonight-feat-adessi |

  @C788 @regression @rc
  Scenario Outline: I can send giphy image by typing some massage and tapping GIF button (portrait)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <Contact>
    When I tap on text input
    And I type the message "<Message>" in the conversation view
    And I tap Gif button from cursor toolbar
    And I select a random gif from the grid preview
    Then I see Giphy preview page
    When I tap Send button on the Giphy preview page
    Then I see the conversation view
    And I see a picture in the conversation view
    And I scroll to the bottom of the Conversation view
    # And I see the picture in the conversation view is animated

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | H       |

  @C797 @regression @rc
  Scenario Outline: I can send giphy image by typing some massage and tapping GIF button (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <Contact>
    When I tap on text input
    And I type the message "<Message>" in the conversation view
    And I tap Gif button from cursor toolbar
    And I select a random gif from the grid preview
    Then I see Giphy preview page
    When I tap Send button on the Giphy preview page
    Then I see the conversation view
    And I see a picture in the conversation view
    And I scroll to the bottom of the Conversation view
    # And I see the picture in the conversation view is animated

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | H       |

  @C502 @regression
  Scenario Outline: (AN-4030) Verify you can play/pause media from the Media Bar in conversation view (portrait only)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    # This is to be able to scroll up until Media Bar appears
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given User <Contact> sends <MsgsInConvo> encrypted messages to user Myself
    And I tap on conversation name <Contact>
    And I tap on text input
    When I type the message "<SoundCloudLink>" in the conversation view
    And I send the typed message by cursor Send button in the conversation view
    And I scroll to the bottom of the conversation view
    And I tap Play button in the conversation view
    And I remember the state of media button in the conversation view
    And I scroll up until Media Bar is visible in the conversation view
    When I tap Pause button on Media Bar in the conversation view
    And I scroll to the bottom of the conversation view
    Then I see the state of media button in the conversation view is changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                                      | MsgsInConvo |
      | user1Name | user2Name | https://soundcloud.com/scottisbell/scott-isbell-tonight-feat-adessi | 40          |