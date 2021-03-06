Feature: File Transfer

  @C87628 @rc @regression
  Scenario Outline: (AN-4830) Verify placeholder is shown for the sender and the message bottom menu for sended file
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push <FileSize><FileSizeType> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap File button from cursor toolbar
    And I wait up to <UploadingTimeout> seconds until <FileSize> <FileSizeType> file with extension "<FileExtension>" is uploaded
    Then I see the result of "<FileSize><FileSizeType>" file upload having name "<FileName>.<FileExtension>" and extension "<FileExtension>"
    # Verify Sender side message bottom menu
    When I long tap File Upload container in the conversation view
    Then I see Delete button on the message bottom menu
    And I see Forward button on the message bottom menu
    And I see Like button on the message bottom menu
    And I see Open button on the message bottom menu

    Examples:
      | Name      | Contact1  | FileName  | FileExtension | FileSize | UploadingTimeout | FileSizeType |
      | user1Name | user2Name | qa_random | txt           | 1.00     | 20               | MB           |

  @C87636 @rc @regression
  Scenario Outline: Verify warning is shown for file size more than 25Mb
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileFullName>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap File button from cursor toolbar
    Then I see alert message containing "<AlertMessage>" in the body

    Examples:
      | Name      | Contact1  | FileFullName  | FileSize | AlertMessage                   |
      | user1Name | user2Name | qa_random.txt | 26.00MB  | You can send files up to 25MB. |

  @C87629 @rc @regression
  Scenario Outline: Verify I can receive notification and I can see file transfer in conversation when I receive a file sharing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And <Contact1> sends local file named "<FileName>.<FileExtension>" and MIME type "<MimeType>" via device Device1 to user Myself
    Then I see new message notification "Shared a photo"
    And I see a picture in the conversation view

    Examples:
      | Name      | Contact1  | FileName   | FileExtension | MimeType  |
      | user1Name | user2Name | avatarTest | png           | image/png |

  @C87639 @rc @regression
  Scenario Outline: Verify retry sending a file
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push <FileSize><FileSizeType> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I enable Airplane mode on the device
    And I wait for 5 seconds
    And I tap on conversation name <Contact1>
    And I tap File button from cursor toolbar
    Then I see the result of "<FileSize><FileSizeType>" file upload having name "<FileName>.<FileExtension>" and extension "<FileExtension>" in <UploadingTimeout> seconds failed
    When I disable Airplane mode on the device
    And I do not see No Internet bar in 15 seconds
    And I tap Retry button on file upload placeholder
    And I wait up to <UploadingTimeout> seconds until <FileSize> file with extension "<FileExtension>" is uploaded
    Then I see the result of "<FileSize><FileSizeType>" file upload having name "<FileName>.<FileExtension>" and extension "<FileExtension>"

    Examples:
      | Name      | Contact1  | FileName  | FileExtension | FileSize | UploadingTimeout | FileSizeType |
      | user1Name | user2Name | qa_random | txt           | 2.00     | 20               | MB           |

  @C87635 @rc @regression
  Scenario Outline: Verify downloading file by sender
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I remove the file "1_<FileName>.<FileExtension>" from device's sdcard
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap File button from cursor toolbar
    And I wait up to <UploadingTimeout> seconds until <FileSize> file with extension "<FileExtension>" is uploaded
    And I tap View button on file upload placeholder
    And I save file from file dialog
    Then I wait up <DownloadTimeout> seconds until <FileSize> file having name "1_<FileName>.<FileExtension>" is downloaded to the device

    Examples:
      | Name      | Contact1  | FileName  | FileExtension | FileSize | UploadingTimeout | DownloadTimeout |
      | user1Name | user2Name | qa_random | txt           | 1.00MB   | 20               | 10              |

  @C87634 @rc @regression
  Scenario Outline: Verify downloading file by receiver
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I remove the file "<FileName>.<FileExtension>" from device's sdcard
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    When <Contact1> sends <FileSize><FileSizeType> file having name "<FileName>.<FileExtension>" and MIME type "<MIMEType>" via device Device1 to user Myself
    And I see the result of "<FileSize><FileSizeType>" file received having name "<FileName>.<FileExtension>" and extension "<FileExtension>" in <ReceivingTimeout> seconds
    And I tap View button on file download placeholder
    And I save file from file dialog
    Then I wait up <DownloadTimeout> seconds until <FileExactSize> file having name "<FileName>.<FileExtension>" is downloaded to the device

    Examples:
      | Name      | Contact1  | FileName  | FileExtension | FileSize | FileSizeType | MIMEType   | DownloadTimeout | FileExactSize | ReceivingTimeout |
      | user1Name | user2Name | qa_random | txt           | 1.00     | MB           | text/plain | 10              | 1.00MB        | 60               |

  @C87638 @rc @regression
  Scenario Outline: Verify canceling sending a file
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push <FileSize><FileSizeType> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap File button from cursor toolbar
    And I tap Cancel button on file upload placeholder
    Then I do not see the result of "<FileSize><FileSizeType>" file upload having name "<FileName>.<FileExtension>" and extension "<FileExtension>"

    Examples:
      | Name      | Contact1  | FileName  | FileExtension | FileSize | FileSizeType |
      | user1Name | user2Name | qa_random | txt           | 24.00    | MB           |

  @C87645 @regression
  Scenario Outline: Verify I see usual image if it was sent by file transfer
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push local file named "<ImageFile>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap File button from cursor toolbar
    Then I see a picture in the conversation view
    When I long tap Image container in the conversation view
    And I tap Delete button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see a picture in the conversation view
    When User <Contact1> sends encrypted image <ImageFile> to single user conversation Myself
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact1  | ImageFile      |
      | user1Name | user2Name | avatarTest.png |
