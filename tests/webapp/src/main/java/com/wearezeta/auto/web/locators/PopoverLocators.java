package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class PopoverLocators {

	public static final class Shared {

		public static final String xpathUserName = "//*[@data-uie-name='status-user']";
		public static final String xpathUserEmail = "//*[@data-uie-name='status-user-email']";
		public static final String xpathAddButton = "//*[@data-uie-name='do-add-people']";
		public static final String xpathSearchInputField = "//input[@type='text' and contains(@class, 'search-input')]";
		public static final String cssSearchResultItems = ".participants-search-list .search-list-item";
		public static final String cssCreateGroupConversationButton = "#participants-bubble [data-uie-name='do-create']";
		public static final Function<String, String> xpathSearchResultByName = (
				name) -> String.format(
				"//*[@data-uie-name='item-user' and .//*[text()='%s']]", name);
		public static final String xpathContinueButton = "//*[@data-uie-value='continue' and @data-uie-name='do-confirm']";
		public static final String xpathBackButton = "//*[@data-uie-name='do-back']";
	}

	public static final class ConnectToPopover {

		public static final String xpathRootLocator = "//*[@id='start-ui-user-bubble' or @id='participants-bubble']";

		public static final class ConnectToPage {

			public static final String xpathNameConnectionMessage = xpathRootLocator
					+ "//*[@data-uie-name='enter-connect-message']";

			public static final String xpathConnectButton = xpathRootLocator
					+ "//*[@data-uie-name='do-connect']";
		}

		public static final class PendingOutgoingConnectionPage {

			public static final String xpathCancelRequestButton = xpathRootLocator
					+ "//*[@data-uie-name='go-cancel']";

			public static final String cssUniqueUsernameOutgoing = "#start-ui-user-bubble,#participants-bubble [data-uie-name='status-username']";
		}

		public static final class CancelRequestConfirmationPage {

			public static final String xpathNoButton = "//*[@data-uie-name='do-cancel']";

			public static final String xpathYesButton = "//*[@data-uie-name='do-confirm']";
		}
	}

	public static final class BringYourFriendsPopover {

		public static final String idRootLocator = "invite-bubble";

		public static final String cssInvitationTextarea = "#invite-bubble textarea";

		public static final String cssInvitationText = ".invite-link-box .message";

		public static final String cssShareContactsButton = "#invite-bubble [data-uie-name='go-import-form']";

		public static final String cssInvitePeopleButton = "#invite-bubble [data-uie-name='go-invite-form']";

		public static final String cssGmailImportButton = "#invite-bubble [data-uie-name='go-import-google']";
	}

	public static final class DeviceDetailPopoverPage {
		public static final String cssDeviceIds = "#participants-bubble .user-profile-devices [data-uie-name='device-id']";
		public static final Function<String, String> cssDeviceById = (id) -> String.format("#participants-bubble .user-profile-devices [data-uie-uid='%s']", id);
	}

	public static final class SingleUserPopover {

		public static final String xpathRootLocator = "//div[@id='participants-bubble']";

		public static final String xpathUserName = "//*[@data-uie-name='status-user']";

		public static final class SingleUserInfoPage {

			public static final String xpathPageRootLocator = "//user-profile";

			public static final String xpathBlockButton = xpathRootLocator
					+ xpathPageRootLocator + "//*[@data-uie-name='do-block']";

			public static final String xpathOpenConversationButton = xpathRootLocator
					+ xpathPageRootLocator
					+ "//*[@data-uie-name='go-conversation']";

			public static final String xpathUnblockButton = xpathPageRootLocator
					+ "//*[@data-uie-name='do-unblock']";
			public static final String cssDevicesTab = "#participants-bubble [data-uie-name='go-profile-devices']";
			public static final String cssDetailsTab = "#participants-bubble [data-uie-name='go-profile-details']";
			public static final String cssDevicesText = "#participants-bubble .user-profile-devices";
			public static final String cssUserVerifiedIcon = "#participants-bubble .user-profile-user-verified";
			public static final String cssDevices = "#participants-bubble .user-profile-device";
		}

		public static final class BlockUserConfirmationPage {

			public static final String xpathConfirmBlockButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='block']";
		}
	}

	public static final class GroupPopover {

		public static final String xpathRootLocator = "//div[@id='participants-bubble']";

		public static final class ParticipantsListPage {

			public static final String xpathPageRootLocator = "//div[contains(@class, 'participants-group')]";

			public static final String xpathVerifiedSection = "//*[@params='user: participants_verified, click: show_participant, mode: z.components.UserListMode.COMPACT']";

			private static final String xpathHeaderDiv = xpathRootLocator
					+ xpathPageRootLocator
					+ "//div[contains(@class, 'participants-group-header')]";

			public static final String xpathConversationTitle = xpathRootLocator
					+ "//*[@data-uie-name='status-name']";

			public static final String xpathConversationTitleInput = xpathHeaderDiv
					+ "/div[contains(@class, 'name')]/textarea";

			public static final String xpathLeaveGroupChat = xpathRootLocator
					+ xpathPageRootLocator + "//*[@data-uie-name='do-leave']";

			public static final String cssParticipants = "#participants-bubble [data-uie-name='item-user']";

			public static final Function<String, String> xpathParticipantByName = (
					name) -> String.format(
					"%s//*[@data-uie-name='item-user' and .//*[text()='%s']]",
					xpathRootLocator + xpathPageRootLocator, name);

			public static final Function<String, String> xPathVerifiedParticipant = (
					name) -> String.format(
					"%s//*[@data-uie-name='item-user' and .//*[text()='%s']]",
					xpathRootLocator + xpathVerifiedSection, name);

			public static final String cssPeopleCount = ".participants-group-header .people";

			public static final String cssEveryoneAlreadyAdded = "#participants-bubble .no-results";
		}

		public static final class LeaveGroupConfirmationPage {

			public static final String xpathConfirmLeaveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='leave']";
		}

		public static final class RemoveParticipantConfirmationPage {

			public static final String xpathConfirmRemoveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='remove']";
		}

		public static final class UnblockUserConfirmationPage {

			public static final String xpathConfirmUnblockButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='unblock']";
		}

		public static final class ConnectParticipantConfirmationPage {

			public static final String xpathConfirmConnectButton = xpathRootLocator
					+ "//*[@data-uie-name='do-connect']";

			public static final String xpathIgnoreConnectButton = xpathRootLocator
					+ "//*[@data-uie-name='do-ignore']";
		}

		public static final class ParticipantInfoPage {

			public static final String xpathRemoveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-remove']";

			public static final String xpathEmailLabel = xpathRootLocator
					+ "//*[@data-uie-name='status-user-email']";

			public static final String xpathAvatar = xpathRootLocator
					+ "//*[@data-uie-name='status-profile-picture']";

			public static final String xpathUserName = xpathRootLocator
					+ "//*[@data-uie-name='status-user']";

			public static final String xpathUniqueUserName = xpathRootLocator
					+ "//*[@data-uie-name='status-username']";

			public static final String xpathCommonFriends = xpathRootLocator
					+ "//*[@data-uie-name='status-user-contacts']";
		}

		public static final class SelfInfoPage {

			public static final String xpathProfileButton = xpathRootLocator
					+ "//*[@data-uie-name='go-profile']";
		}

		public static final class PendingParticipantPage {

			public static final String xpathPendingButton = xpathRootLocator
					+ "//*[@data-uie-name='go-conversation']";

			public static final String xpathPendingTextBox = xpathRootLocator
					+ "//*[@data-uie-name='enter-connect-message']";
		}

		public static final class ConnectedParticipantPage {

			public static final String xpathOpenConversationButton = xpathRootLocator
					+ "//*[@data-uie-name='go-conversation']";
		}

		public static final class NonConnectedParticipantPage {

			public static final String xpathConnectButton = xpathRootLocator
					+ "//*[@data-uie-name='do-connect']";
		}

		public static final class BlockedParticipantPage {

			public static final String xpathUnblockButton = xpathRootLocator
					+ "//*[@data-uie-name='do-unblock']";
		}

	}
}
