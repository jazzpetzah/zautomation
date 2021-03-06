package com.wearezeta.auto.common;

import com.google.common.collect.Lists;
import com.wearezeta.auto.common.backend.*;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.wire_actors.RemoteDevicesManager;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.RegistrationStrategy;
import com.wearezeta.auto.common.wire_actors.models.AssetsVersion;
import com.wearezeta.auto.common.wire_actors.models.MessageInfo;
import com.wearezeta.auto.common.wire_actors.models.MessageReaction;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static javax.xml.bind.DatatypeConverter.parseDateTime;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public final class CommonSteps {

    private static final Logger log = ZetaLogger.getLog(CommonSteps.class.getSimpleName());

    private static final String CONNECTION_NAME = "CONNECT TO ";
    public static final String CONNECTION_MESSAGE = "Hello!";
    public static final Timedelta DEFAULT_WAIT_UNTIL_INTERVAL = Timedelta.fromMilliSeconds(1000);
    public static final Timedelta DEFAULT_WAIT_UNTIL_TIMEOUT = Timedelta.fromSeconds(10);

    private static final int BACKEND_USER_SYNC_TIMEOUT = 180; // seconds

    //increased timeout to make it stable on jenkins
    private static final int BACKEND_SUGGESTIONS_SYNC_TIMEOUT = 240; // seconds
    private static final int BACKEND_COMMON_CONTACTS_SYNC_TIMEOUT = 240; // seconds

    public static final String DEFAULT_AUTOMATION_MESSAGE = "1 message";

    private final ClientUsersManager usersManager;
    private final RemoteDevicesManager devicesManager;

    private ClientUsersManager getUsersManager() {
        return this.usersManager;
    }

    private RemoteDevicesManager getDevicesManager() {
        return this.devicesManager;
    }

    public CommonSteps(ClientUsersManager usersManager, RemoteDevicesManager devicesManager) {
        this.usersManager = usersManager;
        this.devicesManager = devicesManager;
    }

    public void ConnectionRequestIsSentTo(String userFromNameAlias,
                                          String usersToNameAliases) throws Exception {
        ClientUser userFrom = getUsersManager().findUserByNameOrNameAlias(userFromNameAlias);
        for (String userToNameAlias : getUsersManager().splitAliases(usersToNameAliases)) {
            ClientUser userTo = getUsersManager().findUserByNameOrNameAlias(userToNameAlias);
            BackendAPIWrappers.sendConnectRequest(userFrom, userTo,
                    CONNECTION_NAME + userTo.getName(), CONNECTION_MESSAGE);
        }
    }

    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
                                             String chatName, String otherParticipantsNameAlises) throws Exception {
        ClientUser chatOwner = getUsersManager().findUserByNameOrNameAlias(chatOwnerNameAlias);
        final List<ClientUser> participants = new ArrayList<>();
        for (String participantNameAlias : getUsersManager().splitAliases(otherParticipantsNameAlises)) {
            participants.add(getUsersManager().findUserByNameOrNameAlias(participantNameAlias));
        }
        BackendAPIWrappers.createGroupConversation(chatOwner, participants, chatName);
        // Set nameAlias for the group
        // Required for group calling tests
        ClientUser groupUser = new ClientUser();
        groupUser.setName(chatName);
        groupUser.addNameAlias(chatName);
        getUsersManager().appendCustomUser(groupUser);
    }

    public void UserRemovesAnotherUserFromGroupConversation(String userWhoRemovesAlias,
                                                            String userToRemoveAlias,
                                                            String chatName) throws Exception {
        ClientUser userWhoRemoves = getUsersManager().findUserByNameOrNameAlias(userWhoRemovesAlias);
        ClientUser userToRemove = getUsersManager().findUserByNameOrNameAlias(userToRemoveAlias);

        chatName = getUsersManager().replaceAliasesOccurences(chatName, ClientUsersManager.FindBy.NAME_ALIAS);
        BackendAPIWrappers.removeUserFromGroupConversation(userWhoRemoves, userToRemove, chatName);
    }

    public void UserIsConnectedTo(String userFromNameAlias, String usersToNameAliases) throws Exception {
        final ClientUser asUser = getUsersManager().findUserByNameOrNameAlias(userFromNameAlias);
        for (String userToName : getUsersManager().splitAliases(usersToNameAliases)) {
            final ClientUser usrTo = getUsersManager().findUserByNameOrNameAlias(userToName);
            BackendAPIWrappers.sendConnectionRequest(usrTo, asUser);
            BackendAPIWrappers.acceptIncomingConnectionRequest(asUser, usrTo);
        }
    }

    public void ThereIsAKnownUser(String name, String email, String password) throws Exception {
        ClientUser user = new ClientUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        getUsersManager().appendCustomUser(user);
    }

    public void ThereAreNUsers(Platform currentPlatform, int count) throws Exception {
        getUsersManager().createUsersOnBackend(count,
                RegistrationStrategy.getRegistrationStrategyForPlatform(currentPlatform));
    }

    public void ThereAreXAdditionalUsers(Platform currentPlatform, int count) throws Exception {
        getUsersManager().createAndAppendUsers(count,
                RegistrationStrategy.getRegistrationStrategyForPlatform(currentPlatform));
    }

    public void ThereAreNUsersWhereXIsMe(Platform currentPlatform, int count, String myNameAlias) throws Exception {
        getUsersManager().createUsersOnBackend(count, RegistrationStrategy
                .getRegistrationStrategyForPlatform(currentPlatform));
        getUsersManager().setSelfUser(getUsersManager().findUserByNameOrNameAlias(myNameAlias));
    }

    public void ThereAreNUsersWhereXIsMeRegOnlyByMail(int count, String myNameAlias) throws Exception {
        getUsersManager().createUsersOnBackend(count, RegistrationStrategy.ByEmailOnly);
        getUsersManager().setSelfUser(getUsersManager().findUserByNameOrNameAlias(myNameAlias));
    }

    public void ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(int count, String myNameAlias) throws Exception {
        getUsersManager().createUsersOnBackend(count, RegistrationStrategy.ByPhoneNumberOnly);
        getUsersManager().setSelfUser(getUsersManager().findUserByNameOrNameAlias(myNameAlias));
    }

    public void IgnoreAllIncomingConnectRequest(String userToNameAlias) throws Exception {
        ClientUser userTo = getUsersManager().findUserByNameOrNameAlias(userToNameAlias);
        BackendAPIWrappers.ignoreAllIncomingConnections(userTo);
    }

    public void CancelAllOutgoingConnectRequests(String userToNameAlias) throws Exception {
        ClientUser userTo = getUsersManager().findUserByNameOrNameAlias(userToNameAlias);
        BackendAPIWrappers.cancelAllOutgoingConnections(userTo);
    }

    private static final int DRIVER_PING_INTERVAL_SECONDS = 60;

    /**
     * Wait for time in seconds
     *
     * @throws Exception
     */
    public void WaitForTime(int seconds) throws Exception {
        final Thread pingThread = new Thread() {
            public void run() {
                do {
                    try {
                        Thread.sleep(DRIVER_PING_INTERVAL_SECONDS * 1000);
                    } catch (InterruptedException e) {
                        return;
                    }
                    try {
                        PlatformDrivers.getInstance().pingDrivers();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                } while (!isInterrupted());
            }
        };
        pingThread.start();
        try {
            Thread.sleep(seconds * 1000);
        } finally {
            pingThread.interrupt();
        }
    }

    public void BlockContact(String blockAsUserNameAlias,
                             String userToBlockNameAlias) throws Exception {
        ClientUser blockAsUser = getUsersManager().findUserByNameOrNameAlias(blockAsUserNameAlias);
        ClientUser userToBlock = getUsersManager().findUserByNameOrNameAlias(userToBlockNameAlias);
        try {
            BackendAPIWrappers.sendConnectRequest(blockAsUser, userToBlock,
                    "connect", CommonSteps.CONNECTION_MESSAGE);
        } catch (BackendRequestException e) {
            // Ignore silently
        }
        BackendAPIWrappers.changeConnectRequestStatus(blockAsUser, userToBlock.getId(), ConnectionStatus.Blocked);
    }

    public void UnblockContact(String unblockAsUserNameAlias,
                               String userToUnblockNameAlias) throws Exception {
        ClientUser unblockAsUser = getUsersManager().findUserByNameOrNameAlias(unblockAsUserNameAlias);
        ClientUser userToUnblock = getUsersManager().findUserByNameOrNameAlias(userToUnblockNameAlias);
        try {
            BackendAPIWrappers.sendConnectRequest(unblockAsUser, userToUnblock, "connect",
                    CommonSteps.CONNECTION_MESSAGE);
        } catch (BackendRequestException e) {
            // Ignore silently
        }
        BackendAPIWrappers.changeConnectRequestStatus(unblockAsUser,
                userToUnblock.getId(), ConnectionStatus.Accepted);
    }

    public void ArchiveConversationWithUser(String ownerUser, String dstConvoName) throws Exception {
        UserArchiveConversation(ownerUser, dstConvoName, null, false);
    }

    public void ArchiveConversationWithGroup(String ownerUser, String dstConvoName) throws Exception {
        UserArchiveConversation(ownerUser, dstConvoName, null, true);
    }

    public void GenerateNewLoginCode(String asUser) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(asUser);
        BackendAPIWrappers.generateNewLoginCode(user);
    }

    public void MuteConversationWithUser(String ownerUser, String dstConvoName) throws Exception {
        UserMutesConversation(ownerUser, dstConvoName, null, false);
    }

    public void MuteConversationWithGroup(String ownerUser, String dstConvoName) throws Exception {
        UserMutesConversation(ownerUser, dstConvoName, null, true);
    }

    public void UnarchiveConversationWithUser(String ownerUser, String dstConvoName) throws Exception {
        UserUnarchiveConversation(ownerUser, dstConvoName, null, false);
    }

    public void UnarchiveConversationWithGroup(String ownerUser, String dstConvoName) throws Exception {
        UserUnarchiveConversation(ownerUser, dstConvoName, null, true);
    }

    public void ChangeGroupChatName(String asUserNameAliases, String conversationToRename, String newConversationName)
            throws Exception {
        ClientUser asUser = getUsersManager().findUserByNameOrNameAlias(asUserNameAliases);
        final String conversationIDToRename = BackendAPIWrappers.getConversationIdByName(asUser,
                conversationToRename);
        BackendAPIWrappers.changeGroupChatName(asUser, conversationIDToRename, newConversationName);
    }

    public void UnregisterPushToken(String pushToken) throws Exception {
        ClientUser asUser = getUsersManager().getSelfUser().orElseThrow(() -> new IllegalStateException("No self user assigned"));
        BackendAPIWrappers.unregisterPushToken(asUser, pushToken);
    }

    public void AcceptAllIncomingConnectionRequests(String userToNameAlias) throws Exception {
        ClientUser userTo = getUsersManager().findUserByNameOrNameAlias(userToNameAlias);
        BackendAPIWrappers.acceptAllIncomingConnectionRequests(userTo);
    }

    public void UserAddsRemoteDeviceToAccount(String userNameAlias,
                                              @Nullable String deviceName, Optional<String> label) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(userNameAlias);
        getDevicesManager().addRemoteDeviceToAccount(user, deviceName, label);
    }

    public void UserPingedConversationOtr(String pingFromUserNameAlias,
                                          String dstConversationName) throws Exception {
        final ClientUser pingFromUser = getUsersManager().findUserByNameOrNameAlias(pingFromUserNameAlias);
        dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        final String convId = BackendAPIWrappers.getConversationIdByName(pingFromUser, dstConversationName);
        getDevicesManager().sendPing(pingFromUser, convId);
    }

    public void UserSendsGiphy(String sendingFromUserNameAlias, String dstConversationName, String searchQuery,
                               @Nullable String deviceName, boolean isGroup) throws Exception {
        final ClientUser userFrom = getUsersManager().findUserByNameOrNameAlias(sendingFromUserNameAlias);
        String dstConvId;
        if (isGroup) {
            dstConvId = BackendAPIWrappers.getConversationIdByName(userFrom, dstConversationName);
        } else {
            dstConvId = getUsersManager().findUserByNameOrNameAlias(dstConversationName).getId();
        }
        getDevicesManager().sendGiphy(userFrom, dstConvId, searchQuery, deviceName);
    }

    public void UserIsTypingInConversation(String typingFromUserNameAlias, String dstConversationName) throws Exception {
        final ClientUser typingFromUser = getUsersManager().findUserByNameOrNameAlias(typingFromUserNameAlias);
        dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        final String convId = BackendAPIWrappers.getConversationIdByName(typingFromUser, dstConversationName);
        getDevicesManager().typing(typingFromUser, convId);
    }

    public void UserDeleteMessage(String msgFromuserNameAlias, String dstConversationName, String messageId,
                                  String deviceName, boolean isGroup) throws Exception {
        //default is local delete, rather than delete everywhere
        UserDeleteMessage(msgFromuserNameAlias, dstConversationName, messageId, deviceName, isGroup, false);
    }

    public void UserDeleteMessage(String msgFromuserNameAlias, String dstConversationName, String messageId,
                                  String deviceName, boolean isGroup, boolean isDeleteEverywhere) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromuserNameAlias);
        if (!isGroup) {
            dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        if (isDeleteEverywhere) {
            getDevicesManager().deleteMessageEverywhere(user, dstConvId, messageId, deviceName);
        } else {
            getDevicesManager().deleteMessage(user, dstConvId, messageId, deviceName);
        }
    }

    public void UserDeleteLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup) throws Exception {
        //default is delete local, rather than delete eveywhere
        UserDeleteLatestMessage(msgFromUserNameAlias, dstConversationName, deviceName, isGroup, false);
    }

    public void UserReadEphemeralMessage(String msgFromUserNameAlias, String dstConversationName, String messageId,
                                         String deviceName, boolean isGroup) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        getDevicesManager().markEphemeralRead(user, dstConvId, messageId, deviceName);
    }

    public void UserReadLastEphemeralMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                             boolean isGroup) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<MessageInfo> messageInfos = getDevicesManager().getConversationMessages(user, dstConvId, deviceName);
        getDevicesManager().markEphemeralRead(user, dstConvId, getRecentMessageId(messageInfos), deviceName);
    }

    public void UserReadSecondLastEphemeralMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                                   boolean isGroup) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<MessageInfo> messageInfos = getDevicesManager().getConversationMessages(user, dstConvId, deviceName);
        getDevicesManager().markEphemeralRead(user, dstConvId, getSecondLastMessageId(messageInfos), deviceName);
    }

    public void UserLikeLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName)
            throws Exception {
        userReactLatestMessage(msgFromUserNameAlias, dstConversationName, deviceName, MessageReaction.LIKE);
    }

    public void UserUnlikeLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName)
            throws Exception {
        userReactLatestMessage(msgFromUserNameAlias, dstConversationName, deviceName, MessageReaction.UNLIKE);
    }

    private void userReactLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        MessageReaction reactionType) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<MessageInfo> messageInfos =
                getDevicesManager().getConversationMessages(user, dstConvId, deviceName);
        getDevicesManager().reactMessage(user, dstConvId, getRecentMessageId(messageInfos), reactionType, deviceName);
    }

    public void UserDeleteLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup, boolean isDeleteEverywhere) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<MessageInfo> messageInfos = getDevicesManager().getConversationMessages(user, dstConvId, deviceName);
        if (isDeleteEverywhere) {
            getDevicesManager().deleteMessageEverywhere(user, dstConvId, getRecentMessageId(messageInfos), deviceName);
        } else {
            getDevicesManager().deleteMessage(user, dstConvId, getRecentMessageId(messageInfos), deviceName);
        }
    }

    public void UserXVerifiesRecentMessageType(String msgFromUserNameAlias, String dstConversationName,
                                               String deviceName, String expectedType) throws Exception {
        expectedType = expectedType.toUpperCase();
        final ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        final String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        final List<MessageInfo> messageInfos = getDevicesManager().getConversationMessages(user, dstConvId, deviceName);
        // TODO: Handle the situation with zero length of messageInfos
        final String actualType = messageInfos.get(messageInfos.size() - 1).getType().toString().toUpperCase();
        Assert.assertEquals(String.format("The type of the recent conversation message '%s' is not equal to the "
                + "expected type '%s'", actualType, expectedType), actualType, expectedType);
    }

    public void UserUpdateLatestMessage(String msgFromUserNameAlias, String dstConversationName, String newMessage,
                                        String deviceName, boolean isGroup) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<MessageInfo> messageInfos = getDevicesManager().getConversationMessages(user, dstConvId, deviceName);
        getDevicesManager().updateMessage(user, getRecentMessageId(messageInfos), newMessage, deviceName);
    }

    public void UserUpdateSecondLastMessage(String msgFromUserNameAlias, String dstConversationName, String newMessage,
                                            String deviceName, boolean isGroup) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<MessageInfo> messageInfos = getDevicesManager().getConversationMessages(user, dstConvId, deviceName);
        getDevicesManager().updateMessage(user, getSecondLastMessageId(messageInfos), newMessage, deviceName);
    }

    public void UserUpdateMessageById(String msgFromUserNameAlias, String messageId,
                                      String newMessage, String deviceName) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        getDevicesManager().updateMessage(user, messageId, newMessage, deviceName);
    }

    /**
     * Note: if there is no message in conversation, it will return Optional.empty()
     */
    public Optional<String> UserGetRecentMessageId(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                                   boolean isGroup) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<MessageInfo> messageInfos = getDevicesManager().getConversationMessages(user, dstConvId, deviceName);
        if (!messageInfos.isEmpty()) {
            return Optional.ofNullable(getRecentMessageId(messageInfos));
        }
        return Optional.empty();
    }

    public void UserSentMessageToUser(String msgFromUserNameAlias,
                                      String dstUserNameAlias, String message) throws Exception {
        ClientUser msgFromUser = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        ClientUser msgToUser = getUsersManager().findUserByNameOrNameAlias(dstUserNameAlias);
        BackendAPIWrappers.sendDialogMessage(msgFromUser, msgToUser, message);
    }

    public void UserSentOtrMessageToUser(String msgFromUserNameAlias,
                                         String dstUserNameAlias, String message, String deviceName) throws Exception {
        ClientUser msgFromUser = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        ClientUser msgToUser = getUsersManager().findUserByNameOrNameAlias(dstUserNameAlias);
        getDevicesManager().sendConversationMessage(msgFromUser, msgToUser.getId(), message, deviceName);
    }

    public void UserSentOtrMessageToUser(String msgFromUserNameAlias,
                                         String dstUserNameAlias, String message) throws Exception {
        UserSentOtrMessageToUser(msgFromUserNameAlias, dstUserNameAlias, message, null);
    }

    public void UserSentMessageToConversation(String userFromNameAlias,
                                              String dstConversationName, String message) throws Exception {
        ClientUser userFrom = getUsersManager().findUserByNameOrNameAlias(userFromNameAlias);
        dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        BackendAPIWrappers.sendDialogMessageByChatName(userFrom, dstConversationName, message);
    }

    public void UserSentOtrMessageToConversation(String userFromNameAlias,
                                                 String dstConversationName, String message, String deviceName)
            throws Exception {
        ClientUser userFrom = getUsersManager().findUserByNameOrNameAlias(userFromNameAlias);
        dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        String dstConvId = BackendAPIWrappers.getConversationIdByName(userFrom, dstConversationName);
        getDevicesManager().sendConversationMessage(userFrom, dstConvId, message, deviceName);
    }

    public void UserSentOtrMessageToConversation(String userFromNameAlias,
                                                 String dstConversationName, String message) throws Exception {
        UserSentOtrMessageToConversation(userFromNameAlias, dstConversationName, message, null);
    }

    public void UserSentImageToConversation(String imageSenderUserNameAlias,
                                            String imagePath, String dstConversationName, boolean isGroup)
            throws Exception {
        ClientUser imageSender = getUsersManager().findUserByNameOrNameAlias(imageSenderUserNameAlias);
        if (!isGroup) {
            ClientUser imageReceiver = getUsersManager().findUserByNameOrNameAlias(dstConversationName);
            BackendAPIWrappers.sendPictureToSingleUserConversation(imageSender, imageReceiver, imagePath);
        } else {
            dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
            BackendAPIWrappers.sendPictureToChatByName(imageSender, dstConversationName, imagePath);
        }
    }

    public void UserSentImageToConversationOtr(String imageSenderUserNameAlias,
                                               String imagePath, String dstConversationName, boolean isGroup)
            throws Exception {
        ClientUser imageSender = getUsersManager().findUserByNameOrNameAlias(imageSenderUserNameAlias);
        if (!isGroup) {
            ClientUser imageReceiver = getUsersManager().findUserByNameOrNameAlias(dstConversationName);
            BackendAPIWrappers.sendPictureToSingleUserConversationOtr(imageSender, imageReceiver, imagePath,
                    getDevicesManager());
        } else {
            dstConversationName = getUsersManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
            BackendAPIWrappers.sendPictureToChatByNameOtr(imageSender, dstConversationName, imagePath,
                    getDevicesManager());
        }
    }

    public void UserSentFileToConversation(String msgFromUserNameAlias, String dstConversationName, String path,
                                           String mime, String deviceName, boolean isGroup) throws Exception {
        ClientUser msgFromUser = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!new File(path).exists()) {
            throw new IllegalArgumentException(String.format("Please make sure the file %s exists and is accessible",
                    path));
        }
        if (!isGroup) {
            ClientUser msgToUser = getUsersManager().findUserByNameOrNameAlias(dstConversationName);
            getDevicesManager().sendFile(msgFromUser, msgToUser.getId(), path, mime, deviceName);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            getDevicesManager().sendFile(msgFromUser, dstConvId, path, mime, deviceName);
        }
    }

    public void UserSentLocationToConversation(String msgFromUserNameAlias, String deviceName, String dstConversationName,
                                               float longitude,
                                               float latitude, String locationName, int zoom, boolean isGroup) throws Exception {
        ClientUser msgFromUser = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);

        if (!isGroup) {
            ClientUser msgToUser = getUsersManager().findUserByNameOrNameAlias(dstConversationName);
            getDevicesManager().sendLocation(msgFromUser, deviceName, msgToUser.getId(), longitude, latitude, locationName,
                    zoom);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            getDevicesManager().sendLocation(msgFromUser, deviceName, dstConvId, longitude, latitude, locationName, zoom);
        }
    }

    public void UserClearsConversation(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                       boolean isGroup) throws Exception {
        ClientUser msgFromUser = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            ClientUser msgToUser = getUsersManager().findUserByNameOrNameAlias(dstConversationName);
            getDevicesManager().clearConversation(msgFromUser, msgToUser.getId(), deviceName);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            getDevicesManager().clearConversation(msgFromUser, dstConvId, deviceName);
        }
    }

    public void UserMutesConversation(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                      boolean isGroup) throws Exception {
        ClientUser msgFromUser = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            ClientUser msgToUser = getUsersManager().findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                getDevicesManager().muteConversation(msgFromUser, msgToUser.getId());
            } else {
                getDevicesManager().muteConversation(msgFromUser, msgToUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            if (deviceName == null) {
                getDevicesManager().muteConversation(msgFromUser, dstConvId);
            } else {
                getDevicesManager().muteConversation(msgFromUser, dstConvId, deviceName);
            }
        }
    }

    public void UserUnmutesConversation(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup) throws Exception {
        ClientUser msgFromUser = getUsersManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            ClientUser msgToUser = getUsersManager().findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                getDevicesManager().unmuteConversation(msgFromUser, msgToUser.getId());
            } else {
                getDevicesManager().unmuteConversation(msgFromUser, msgToUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            if (deviceName == null) {
                getDevicesManager().unmuteConversation(msgFromUser, dstConvId);
            } else {
                getDevicesManager().unmuteConversation(msgFromUser, dstConvId, deviceName);
            }
        }
    }

    public void UserArchiveConversation(String fromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup) throws Exception {
        ClientUser fromUser = getUsersManager().findUserByNameOrNameAlias(fromUserNameAlias);
        if (!isGroup) {
            ClientUser dstUser = getUsersManager().findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                getDevicesManager().archiveConversation(fromUser, dstUser.getId());
            } else {
                getDevicesManager().archiveConversation(fromUser, dstUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(fromUser, dstConversationName);
            if (deviceName == null) {
                getDevicesManager().archiveConversation(fromUser, dstConvId);
            } else {
                getDevicesManager().archiveConversation(fromUser, dstConvId, deviceName);
            }
        }
    }

    public void UserUnarchiveConversation(String fromUserNameAlias, String dstConversationName, String deviceName,
                                          boolean isGroup) throws Exception {
        ClientUser fromUser = getUsersManager().findUserByNameOrNameAlias(fromUserNameAlias);
        if (!isGroup) {
            ClientUser dstUser = getUsersManager().findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                getDevicesManager().unarchiveConversation(fromUser, dstUser.getId());
            } else {
                getDevicesManager().unarchiveConversation(fromUser, dstUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(fromUser, dstConversationName);
            if (deviceName == null) {
                getDevicesManager().unarchiveConversation(fromUser, dstConvId);
            } else {
                getDevicesManager().unarchiveConversation(fromUser, dstConvId, deviceName);
            }
        }
    }

    public void IChangeUserAvatarPicture(String userNameAlias, String picturePath) throws Exception {
        final ClientUser dstUser = getUsersManager().findUserByNameOrNameAlias(userNameAlias);
        if (new File(picturePath).exists()) {
            BackendAPIWrappers.updateUserPicture(dstUser, picturePath);
        } else {
            throw new IOException(String.format("The picture '%s' is not accessible", picturePath));
        }
    }

    public void IChangeUserAvatarPicture(String userNameAlias, String picturePath, AssetsVersion protocol)
            throws Exception {
        final ClientUser dstUser = getUsersManager().findUserByNameOrNameAlias(userNameAlias);
        if (new File(picturePath).exists()) {
            switch (protocol) {
                case V2:
                    BackendAPIWrappers.updateUserPictureV2(dstUser, picturePath);
                    break;
                case V3:
                    BackendAPIWrappers.updateUserPictureV3(dstUser, picturePath);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown protocol '%s'", protocol.name()));
            }
        } else {
            throw new IOException(String.format("The picture '%s' is not accessible", picturePath));
        }
    }

    public void UserDeletesAvatarPicture(String userNameAlias) throws Exception {
        final ClientUser dstUser = getUsersManager().findUserByNameOrNameAlias(userNameAlias);
        BackendAPIWrappers.removeUserPicture(dstUser);
    }

    public void IChangeName(String userNameAlias, String newName) throws Exception {
        BackendAPIWrappers.updateName(getUsersManager().findUserByNameOrNameAlias(userNameAlias), newName);
    }

    public void IChangeUniqueUsername(String userNameAlias, String name) throws Exception {
        name = getUsersManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        BackendAPIWrappers.updateUniqueUsername(getUsersManager().findUserByNameOrNameAlias(userNameAlias), name);
    }

    public void UsersSetUniqueUsername(String userNameAliases) throws Exception {
        for (String userNameAlias : getUsersManager().splitAliases(userNameAliases)) {
            final ClientUser user = getUsersManager().findUserByNameOrNameAlias(userNameAlias);
            BackendAPIWrappers.updateUniqueUsername(getUsersManager().findUserByNameOrNameAlias(userNameAlias), user.getUniqueUsername()
                    .toLowerCase());
        }
    }

    public void IChangeUserAccentColor(String userNameAlias, String colorName) throws Exception {
        BackendAPIWrappers.updateUserAccentColor(getUsersManager().findUserByNameOrNameAlias(userNameAlias),
                AccentColor.getByName(colorName));
    }

    public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix) throws Exception {
        getUsersManager().appendSharedUsers(namePrefix, count);
    }

    public void UserXIsMe(String nameAlias) throws Exception {
        getUsersManager().setSelfUser(getUsersManager().findUserByNameOrNameAlias(nameAlias));
    }

    public void WaitUntilContactIsNotFoundInSearch(String searchByNameAlias,
                                                   String contactAlias, int timeoutSeconds) throws Exception {
        String query = getUsersManager().replaceAliasesOccurences(contactAlias, FindBy.NAME_ALIAS);
        query = getUsersManager().replaceAliasesOccurences(query, FindBy.EMAIL_ALIAS);
        query = getUsersManager().replaceAliasesOccurences(query, FindBy.UNIQUE_USERNAME_ALIAS);
        BackendAPIWrappers.waitUntilContactNotFound(getUsersManager().findUserByNameOrNameAlias(searchByNameAlias), query,
                timeoutSeconds);
    }

    public void WaitUntilContactIsFoundInSearch(String searchByNameAlias,
                                                String contactAlias) throws Exception {
        String query = getUsersManager().replaceAliasesOccurences(contactAlias, FindBy.NAME_ALIAS);
        query = getUsersManager().replaceAliasesOccurences(query, FindBy.EMAIL_ALIAS);
        query = getUsersManager().replaceAliasesOccurences(query, FindBy.UNIQUE_USERNAME_ALIAS);
        BackendAPIWrappers.waitUntilContactsFound(getUsersManager().findUserByNameOrNameAlias(searchByNameAlias), query,
                1, true, BACKEND_USER_SYNC_TIMEOUT);
    }

    public void WaitUntilCommonContactsIsGenerated(String searchByNameAlias, String contactAlias) throws Exception {
        WaitUntilCommonContactsIsGenerated(searchByNameAlias, contactAlias, 1);
    }

    public void WaitUntilCommonContactsIsGenerated(String searchByNameAlias, String contactAlias,
                                                   int expectCountOfCommonContacts) throws Exception {
        ClientUser searchByUser = getUsersManager().findUserBy(searchByNameAlias, FindBy.NAME_ALIAS);
        ClientUser destUser = getUsersManager().findUserBy(contactAlias, FindBy.NAME_ALIAS);
        BackendAPIWrappers.waitUntilCommonContactsFound(searchByUser, destUser, expectCountOfCommonContacts,
                true, BACKEND_COMMON_CONTACTS_SYNC_TIMEOUT);
    }

    public void WaitUntilContactIsSuggestedInSearchResult(String searchByNameAlias,
                                                          String contactAlias) throws Exception {
        String query = getUsersManager().replaceAliasesOccurences(contactAlias, FindBy.NAME_ALIAS);
        BackendAPIWrappers.waitUntilSuggestionFound(getUsersManager().findUserByNameOrNameAlias(searchByNameAlias), query,
                1, true, BACKEND_SUGGESTIONS_SYNC_TIMEOUT);
    }

    public void WaitUntilContactIsFoundInSearchByEmail(String searchByNameAlias, String contactAlias) throws Exception {
        final ClientUser userAs = getUsersManager().findUserByNameOrNameAlias(contactAlias);
        String query = userAs.getEmail();
        BackendAPIWrappers.waitUntilContactsFound(getUsersManager()
                        .findUserByNameOrNameAlias(searchByNameAlias), query, 1, true,
                BACKEND_USER_SYNC_TIMEOUT);
    }

    public void WaitUntilContactIsFoundInSearchByUniqueUsername(String searchByNameAlias, String contactAlias)
            throws Exception {
        final ClientUser userAs = getUsersManager().findUserByNameOrNameAlias(contactAlias);
        String query = userAs.getUniqueUsername();
        BackendAPIWrappers.waitUntilContactsFound(
                getUsersManager().findUserByNameOrNameAlias(searchByNameAlias), query, 1, true,
                BACKEND_USER_SYNC_TIMEOUT);
    }

    public void WaitUntilTopPeopleContactsIsFoundInSearch(String searchByNameAlias, int size) throws Exception {
        BackendAPIWrappers.waitUntilTopPeopleContactsFound(
                getUsersManager().findUserByNameOrNameAlias(searchByNameAlias), size, size,
                true, BACKEND_USER_SYNC_TIMEOUT);
    }

    public void UserXAddedContactsToGroupChat(String userAsNameAlias,
                                              String contactsToAddNameAliases, String chatName) throws Exception {
        final ClientUser userAs = getUsersManager().findUserByNameOrNameAlias(userAsNameAlias);
        List<ClientUser> contactsToAdd = new ArrayList<>();
        for (String contactNameAlias : getUsersManager().splitAliases(contactsToAddNameAliases)) {
            contactsToAdd.add(getUsersManager().findUserByNameOrNameAlias(contactNameAlias));
        }
        BackendAPIWrappers.addContactsToGroupConversation(userAs, contactsToAdd, chatName);
    }

    public void UserXRemoveContactFromGroupChat(String userAsNameAlias,
                                                String contactToRemoveNameAlias, String chatName) throws Exception {
        final ClientUser userAs = getUsersManager().findUserByNameOrNameAlias(userAsNameAlias);
        final ClientUser userToRemove = getUsersManager().findUserByNameOrNameAlias(contactToRemoveNameAlias);

        BackendAPIWrappers.removeUserFromGroupConversation(userAs, userToRemove, chatName);
    }

    public void UserXLeavesGroupChat(String userNameAlias, String chatName) throws Exception {
        final ClientUser userAs = getUsersManager().findUserByNameOrNameAlias(userNameAlias);

        BackendAPIWrappers.removeUserFromGroupConversation(userAs, userAs, chatName);

    }

    private Map<String, String> profilePictureSnapshotsMap = new HashMap<>();
    private Map<String, String> profilePictureV3SnapshotsMap = new HashMap<>();
    private Map<String, String> profilePictureV3PreviewSnapshotsMap = new HashMap<>();

    public void UserXTakesSnapshotOfProfilePicture(String userNameAlias) throws Exception {
        final ClientUser userAs = getUsersManager().findUserByNameOrNameAlias(userNameAlias);
        String email = userAs.getEmail();
        profilePictureSnapshotsMap.put(email, BackendAPIWrappers.getUserPictureHash(userAs));
        profilePictureV3SnapshotsMap.put(email,
                BackendAPIWrappers.getUserAssetKey(userAs,
                        BackendAPIWrappers.PROFILE_PICTURE_JSON_ATTRIBUTE));
        profilePictureV3PreviewSnapshotsMap.put(email,
                BackendAPIWrappers.getUserAssetKey(userAs,
                        BackendAPIWrappers.PROFILE_PREVIEW_PICTURE_JSON_ATTRIBUTE));
    }

    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
            String userNameAlias, int secondsTimeout) throws Exception {
        final ClientUser userAs = getUsersManager().findUserByNameOrNameAlias(userNameAlias);
        String email = userAs.getEmail();
        String previousHash, previousCompleteKey, previousPreviewKey;
        if (profilePictureSnapshotsMap.containsKey(email)
                && profilePictureV3SnapshotsMap.containsKey(email)
                && profilePictureV3PreviewSnapshotsMap.containsKey(email)) {
            previousHash = profilePictureSnapshotsMap.get(email);
            previousCompleteKey = profilePictureV3SnapshotsMap.get(email);
            previousPreviewKey = profilePictureV3PreviewSnapshotsMap.get(email);
        } else {
            throw new RuntimeException(String.format(
                    "Please take user picture snapshot for user '%s' first",
                    userAs.getEmail()));
        }
        long millisecondsStarted = System.currentTimeMillis();
        String actualHash, actualCompleteKey, actualPreviewKey;
        do {
            actualHash = BackendAPIWrappers.getUserPictureHash(userAs);
            actualCompleteKey = BackendAPIWrappers.getUserAssetKey(userAs,
                    BackendAPIWrappers.PROFILE_PICTURE_JSON_ATTRIBUTE);
            actualPreviewKey = BackendAPIWrappers.getUserAssetKey(userAs,
                    BackendAPIWrappers.PROFILE_PREVIEW_PICTURE_JSON_ATTRIBUTE);
            if (!actualHash.equals(previousHash)
                    && !actualCompleteKey.equals(previousCompleteKey)
                    && !actualPreviewKey.equals(previousPreviewKey)) {
                break;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000);
        assertThat("User profile picture is not different (V2)", actualHash, not(equalTo(previousHash)));
        assertThat("User big profile picture is not different (V3)", actualCompleteKey,
                not(equalTo(previousCompleteKey)));
        assertThat("User small profile picture is not different (V3)", actualPreviewKey,
                not(equalTo(previousPreviewKey)));
    }

    private static final int PICTURE_CHANGE_TIMEOUT = 15; // seconds

    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(String userNameAlias) throws Exception {
        UserXVerifiesSnapshotOfProfilePictureIsDifferent(userNameAlias,
                PICTURE_CHANGE_TIMEOUT);
    }

    /**
     * Upload fake addressbook to Backend
     *
     * @param userAsNameAlias the user who upload the addressbook
     * @param contacts        could be a list of phone numbers (+49.....) or emails , seperated by comma
     * @throws Exception
     */
    public void UserXHasContactsInAddressBook(String userAsNameAlias, String contacts) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String contact : getUsersManager().splitAliases(contacts)) {
            if (contact.startsWith("+")) {
                sb.append(contact);
            } else {
                sb.append(getUsersManager().replaceAliasesOccurences(contact, FindBy.EMAIL_ALIAS));
            }
            sb.append(ClientUsersManager.ALIASES_SEPARATOR);
        }
        this.UserXHasEmailsInAddressBook(userAsNameAlias, sb.toString());
    }

    public void UserXHasEmailsInAddressBook(String userAsNameAlias,
                                            String emails) throws Exception {
        final ClientUser userAs = getUsersManager().findUserByNameOrNameAlias(userAsNameAlias);
        BackendAPIWrappers.uploadAddressBookWithContacts(userAs, getUsersManager().splitAliases(emails));
    }

    public void UserXSendsPersonalInvitationWithMessageToUserWithMail(
            String sender, String toMail, String message) throws Exception {
        ClientUser user = getUsersManager().findUserByNameOrNameAlias(sender);
        ClientUser invitee = getUsersManager().findUserByEmailOrEmailAlias(toMail);
        BackendAPIWrappers.sendPersonalInvitation(user, invitee.getEmail(), invitee.getName(), message);
    }

    public void IAddUserToTheListOfTestCaseUsers(String nameAlias) throws Exception {
        ClientUser userToAdd = getUsersManager().findUserByNameOrNameAlias(nameAlias);
        getUsersManager().appendCustomUser(userToAdd);
    }

    public void UserRemovesAllRegisteredOtrClients(String forUser) throws Exception {
        final ClientUser usr = getUsersManager().findUserByNameOrNameAlias(forUser);
        final List<OtrClient> allOtrClients = BackendAPIWrappers.getOtrClients(usr);
        for (OtrClient c : allOtrClients) {
            BackendAPIWrappers.removeOtrClient(usr, c);
        }
    }

    public List<String> GetDevicesIDsForUser(String name) throws Exception {
        final ClientUser usr = getUsersManager().findUserByNameOrNameAlias(name);
        return getDevicesManager().getDeviceIds(usr);
    }

    public void UserKeepsXOtrClients(String userAs, int clientsCountToKeep) throws Exception {
        final ClientUser usr = getUsersManager().findUserByNameOrNameAlias(userAs);
        final List<OtrClient> allOtrClients = BackendAPIWrappers.getOtrClients(usr);
        final String defaultDateStr = "2016-01-01T12:00:00Z";
        // Newly registered clients coming first
        Collections.sort(allOtrClients, (c1, c2)
                        -> parseDateTime(c2.getTime().orElse(defaultDateStr)).getTime().compareTo(
                parseDateTime(c1.getTime().orElse(defaultDateStr)).getTime()
                )
        );
        log.debug(String.format("Clients considered for removal %s", allOtrClients));
        if (allOtrClients.size() > clientsCountToKeep) {
            for (OtrClient c : allOtrClients.subList(clientsCountToKeep, allOtrClients.size())) {
                log.debug(String.format("Removing client with ID %s", c.getId()));
                try {
                    BackendAPIWrappers.removeOtrClient(usr, c);
                } catch (BackendRequestException e) {
                    if (e.getReturnCode() == 404) {
                        // To avoid multithreading issues
                        e.printStackTrace();
                    } else {
                        throw e;
                    }
                }
            }
        }
    }

    public String GetInvitationUrl(String user) throws Exception {
        final ClientUser usr = getUsersManager().findUserByNameOrNameAlias(user);
        return InvitationLinkGenerator.getInvitationUrl(usr.getId());
    }

    public void UserSharesLocationTo(String senderAlias, String dstConversationName, boolean isGroup, String deviceName)
            throws Exception {
        final ClientUser msgFromUser = getUsersManager().findUserByNameOrNameAlias(senderAlias);
        final String dstConvId = isGroup
                ? BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName)
                : getUsersManager().findUserByNameOrNameAlias(dstConversationName).getId();
        getDevicesManager().shareDefaultLocation(msgFromUser, dstConvId, deviceName);
    }

    public void UserSwitchesToEphemeralMode(String senderAlias, String dstConversationName,
                                            long expirationMilliseconds, boolean isGroup, String deviceName)
            throws Exception {
        final ClientUser msgFromUser = getUsersManager().findUserByNameOrNameAlias(senderAlias);
        String dstConvId;
        if (isGroup) {
            dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
        } else {
            dstConvId = getUsersManager().findUserByNameOrNameAlias(dstConversationName).getId();
        }
        getDevicesManager().setEphemeralMode(msgFromUser, dstConvId,
                Timedelta.fromMilliSeconds(expirationMilliseconds), deviceName);
    }

    public void UserSetAssetMode(String actorUserNameAlias, AssetsVersion asset, String deviceName) throws Exception {
        final ClientUser actorUser = getUsersManager().findUserByNameOrNameAlias(actorUserNameAlias);
        switch (asset) {
            case V3:
                getDevicesManager().setAssetToV3(actorUser, deviceName);
                break;
            case V2:
                getDevicesManager().setAssetToV2(actorUser, deviceName);
                break;
            default:
                throw new IllegalArgumentException("Only support AssetProtocol V2 and V3");
        }
    }

    public void UserCancelConnection(String userNameAlias, String canceldUserNameAlias, String deviceName)
            throws Exception {
        final ClientUser user = getUsersManager().findUserByNameOrNameAlias(userNameAlias);
        final ClientUser canceledUser = getUsersManager().findUserByNameOrNameAlias(canceldUserNameAlias);
        getDevicesManager().cancelConnection(user, canceledUser, deviceName);
    }

    public String GetUserUnqiueUsername(String userNameAlias, String deviceName) throws Exception {
        final ClientUser user = getUsersManager().findUserByNameOrNameAlias(userNameAlias);
        return getDevicesManager().getUniqueUsername(user, deviceName);
    }

    public void UpdateUniqueUsername(String userNameAlias, String uniqueUserName, String deviceName) throws Exception {
        final ClientUser user = getUsersManager().findUserByNameOrNameAlias(userNameAlias);
        getDevicesManager().updateUniqueUsername(user, uniqueUserName, deviceName);
    }

    public void UserResetsPassword(String nameAlias, String newPassword) throws Exception {
        final ClientUser usr = getUsersManager().findUserByNameOrNameAlias(nameAlias);
        BackendAPIWrappers.changeUserPassword(usr, usr.getPassword(), newPassword);
    }

    private Map<String, Optional<String>> recentMessageIds = new HashMap<>();

    private String generateConversationKey(String userFrom, String dstName, String deviceName) {
        return String.format("%s:%s:%s", getUsersManager().replaceAliasesOccurences(userFrom,
                ClientUsersManager.FindBy.NAME_ALIAS),
                getUsersManager().replaceAliasesOccurences(dstName, ClientUsersManager.FindBy.NAME_ALIAS), deviceName);
    }

    public void UserXRemembersLastMessage(String userNameAlias, boolean isGroup, String dstNameAlias, String deviceName)
            throws Exception {
        recentMessageIds.put(generateConversationKey(userNameAlias, dstNameAlias, deviceName),
                UserGetRecentMessageId(userNameAlias, dstNameAlias, deviceName, isGroup));
    }

    private Optional<String> getUserXLastMessageId(String rememberedMessage, String userNameAlias, boolean isGroup,
                                                   String dstNameAlias, String deviceName, int durationSeconds)
            throws Exception {
        return CommonUtils.waitUntil(Timedelta.fromSeconds(durationSeconds),
                CommonSteps.DEFAULT_WAIT_UNTIL_INTERVAL,
                () -> {
                    Optional<String> messageId = UserGetRecentMessageId(userNameAlias,
                            dstNameAlias, deviceName, isGroup);

                    String actualMessage = messageId.orElse("");
                    // Try to wait for a different a message id
                    if (actualMessage.equals(rememberedMessage)) {
                        throw new IllegalStateException(
                                String.format("The recent remembered message id %s and the current message id %s"
                                        + " should be different", rememberedMessage, actualMessage));
                    } else {
                        return actualMessage;
                    }
                });
    }

    public void UserXFoundLastMessageChanged(String userNameAlias, boolean isGroup, String dstNameAlias,
                                             String deviceName, int durationSeconds) throws Exception {
        final String convoKey = generateConversationKey(userNameAlias, dstNameAlias, deviceName);
        if (!recentMessageIds.containsKey(convoKey)) {
            throw new IllegalStateException("You should remember the recent message before you check it");
        }
        final String rememberedMessageId = recentMessageIds.get(convoKey).orElse("");
        Optional<String> actualMessageId = getUserXLastMessageId(rememberedMessageId, userNameAlias, isGroup,
                dstNameAlias, deviceName, durationSeconds);
        Assert.assertTrue(String.format("Actual message Id should not equal to '%s'", rememberedMessageId),
                actualMessageId.isPresent());
    }

    public void UserXFoundLastMessageNotChanged(String userNameAlias, boolean isGroup, String dstNameAlias,
                                                String deviceName, int durationSeconds) throws Exception {
        final String convoKey = generateConversationKey(userNameAlias, dstNameAlias, deviceName);
        if (!recentMessageIds.containsKey(convoKey)) {
            throw new IllegalStateException("You should remember the recent message before you check it");
        }
        final String rememberedMessageId = recentMessageIds.get(convoKey).orElse("");
        Optional<String> actualMessageId = getUserXLastMessageId(rememberedMessageId, userNameAlias, isGroup,
                dstNameAlias, deviceName, durationSeconds);
        Assert.assertTrue(String.format("Actual message Id should equal to '%s'", rememberedMessageId),
                !actualMessageId.isPresent());
    }

    private static String getRecentMessageId(List<MessageInfo> messageInfos) {
        return Lists.reverse(messageInfos).stream()
                .filter(x -> x.getType() != MessageInfo.MessageType.UNKNOWN)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find any valid messages"))
                .getId();
    }

    private static String getSecondLastMessageId(List<MessageInfo> messageInfos) {
        final List<MessageInfo> filteredList = Lists.reverse(messageInfos).stream()
                .filter(x -> x.getType() != MessageInfo.MessageType.UNKNOWN)
                .collect(Collectors.toList());
        if (filteredList.size() < 2) {
            throw new IllegalStateException("Cannot find the second valid message");
        }
        return filteredList.get(1).getId();
    }

    public void uploadSelfContact(String userAliases) throws Exception {
        for (String alias : getUsersManager().splitAliases(userAliases)) {
            final ClientUser selfUser = getUsersManager().findUserByNameOrNameAlias(alias);
            BackendAPIWrappers.uploadSelfContact(selfUser);
        }
    }

    private static final Timedelta DEVICES_CREATION_TIMEOUT = Timedelta.fromMinutes(5);

    public void UsersAddDevices(String usersToDevicesMappingAsJson) throws Exception {
        final JSONObject mappingAsJson = new JSONObject(usersToDevicesMappingAsJson);
        final Map<String, List<JSONObject>> devicesMapping = new LinkedHashMap<>();
        int expectedDevicesCount = 0;
        for (String user : mappingAsJson.keySet()) {
            final JSONArray devices = mappingAsJson.getJSONArray(user);
            final List<JSONObject> devicesInfo = new ArrayList<>();
            for (int deviceIdx = 0; deviceIdx < devices.length(); deviceIdx++) {
                devicesInfo.add(devices.getJSONObject(deviceIdx));
                ++expectedDevicesCount;
            }
            devicesMapping.put(getUsersManager().replaceAliasesOccurences(user, FindBy.NAME_ALIAS), devicesInfo);
        }
        final ExecutorService pool = Executors.newFixedThreadPool(expectedDevicesCount);
        final AtomicInteger createdDevicesCount = new AtomicInteger(0);
        for (Map.Entry<String, List<JSONObject>> entry : devicesMapping.entrySet()) {
            for (JSONObject devInfo : entry.getValue()) {
                pool.submit(() -> {
                    try {
                        UserAddsRemoteDeviceToAccount(entry.getKey(),
                                devInfo.has("name") ? devInfo.getString("name") : null,
                                devInfo.has("label") ? Optional.of(devInfo.getString("label")) : Optional.empty());
                        createdDevicesCount.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        pool.shutdown();
        if (!pool.awaitTermination(DEVICES_CREATION_TIMEOUT.asMinutes(), TimeUnit.MINUTES)
                || createdDevicesCount.get() != expectedDevicesCount) {
            throw new IllegalStateException(String.format(
                    "%d devices for users '%s' were not created after %s timeout",
                    expectedDevicesCount, devicesMapping.keySet(), DEVICES_CREATION_TIMEOUT));
        }
    }

    public void UserSendMultipleMedias(String senderUserNameAlias, int count,
                                       String fileType, String fileName,
                                       String dstConversationName) throws Exception {
        final ClientUser srcUser = getUsersManager().findUserByNameOrNameAlias(senderUserNameAlias);
        dstConversationName = getUsersManager()
                .replaceAliasesOccurences(dstConversationName, ClientUsersManager.FindBy.NAME_ALIAS);
        final String dstConvoId = BackendAPIWrappers.getConversationIdByName(srcUser, dstConversationName);
        String filePath;
        for (int i = 0; i < count; ++i) {
            switch (fileType) {
                case "image":
                    filePath = CommonUtils.getImagesPathFromConfig(this.getClass()) + File.separator + fileName;
                    getDevicesManager().sendImage(srcUser, dstConvoId, filePath);
                    break;
                case "video":
                case "audio":
                    filePath = CommonUtils.getAudioPathFromConfig(this.getClass()) + File.separator + fileName;
                    getDevicesManager().sendFile(srcUser, dstConvoId, filePath,
                            fileType.equals("video") ? "video/mp4" : "audio/mp4", null);
                    break;
                case "temporary":
                    filePath = CommonUtils.getBuildPathFromConfig(this.getClass()) + File.separator + fileName;
                    getDevicesManager().
                            sendFile(srcUser, dstConvoId, filePath, "application/octet-stream", null);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unsupported '%s' file type", fileType));
            }
        }
    }

    public void UserSendsMultipleMessages(String senderUserNameAlias, int count, String msg,
                                          String dstConversationName, String defaultMessage) throws Exception {
        if (msg.equals("default")) {
            msg = defaultMessage;
        } else {
            msg = msg.replaceAll("^\"|\"$", "");
        }
        final ClientUser srcUser = getUsersManager().findUserByNameOrNameAlias(senderUserNameAlias);
        dstConversationName = getUsersManager()
                .replaceAliasesOccurences(dstConversationName, ClientUsersManager.FindBy.NAME_ALIAS);
        final String dstConvoId = BackendAPIWrappers.getConversationIdByName(srcUser, dstConversationName);
        for (int i = 0; i < count; ++i) {
            getDevicesManager().
                    sendConversationMessage(srcUser, dstConvoId, msg);
            if (msg.startsWith("http")) {
                // TODO: Remove the delay after multiple links generation for single domain is fixed on SE side
                Thread.sleep(3000);
            }
        }
    }

    public void UserSendsMultipleMessages(String senderUserNameAlias, int count,
                                          String msg, String dstConversationName) throws Exception {
        UserSendsMultipleMessages(senderUserNameAlias, count, msg, dstConversationName,
                CommonSteps.DEFAULT_AUTOMATION_MESSAGE);
    }

    public static final String USER_DETAIL_NOT_SET = "NOT_SET";

    public void UserVerifiesDetails(String user, String detail, String expectedValue) throws Exception {
        final ClientUser dstUser = getUsersManager().findUserByNameOrNameAlias(user);
        switch (detail.toLowerCase()) {
            case "email":
                expectedValue = getUsersManager().replaceAliasesOccurences(expectedValue, FindBy.EMAIL_ALIAS);
                Assert.assertEquals(BackendAPIWrappers.getEmail(dstUser).orElse(USER_DETAIL_NOT_SET),
                        expectedValue);
                break;
            case "name":
                expectedValue = getUsersManager().replaceAliasesOccurences(expectedValue, FindBy.NAME_ALIAS);
                Assert.assertEquals(BackendAPIWrappers.getName(dstUser), expectedValue);
                break;
            case "unique username":
                expectedValue = getUsersManager().replaceAliasesOccurences(expectedValue, FindBy.UNIQUE_USERNAME_ALIAS);
                Assert.assertEquals(BackendAPIWrappers.getUniqueUsername(dstUser).orElse(USER_DETAIL_NOT_SET),
                        expectedValue);
                break;
            case "phone number":
                expectedValue = getUsersManager().replaceAliasesOccurences(expectedValue, FindBy.PHONENUMBER_ALIAS);
                Assert.assertEquals(BackendAPIWrappers.getPhoneNumber(dstUser).orElse(USER_DETAIL_NOT_SET),
                        expectedValue);
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("Unknown value '%s' is passed for user detail", detail)
                );
        }
    }
}
