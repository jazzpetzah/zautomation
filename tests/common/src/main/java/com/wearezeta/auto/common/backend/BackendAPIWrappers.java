package com.wearezeta.auto.common.backend;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.email.ActivationMessage;
import com.wearezeta.auto.common.email.InvitationMessage;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.PasswordResetMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.image_send.*;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.common.onboarding.AddressBook;
import com.wearezeta.auto.common.onboarding.Card;
import com.wearezeta.auto.common.wire_actors.RemoteDevicesManager;
import com.wearezeta.auto.common.usrmgmt.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Future;

// Almost all methods of this class mutate ClientUser
// argument by performing automatic login (set id and session token attributes)
public final class BackendAPIWrappers {
    public static final int ACTIVATION_TIMEOUT = 120; // seconds
    public static final int INVITATION_RECEIVING_TIMEOUT = ACTIVATION_TIMEOUT; // seconds

    private static final int REQUEST_TOO_FREQUENT_ERROR = 429;
    private static final int LOGIN_CODE_HAS_NOT_BEEN_USED_ERROR = 403;
    private static final int AUTH_FAILED_ERROR = 403;
    private static final int SERVER_SIDE_ERROR = 500;
    private static final int PHONE_NUMBER_ALREADY_REGISTERED_ERROR = 409;
    private static final int MAX_BACKEND_RETRIES = 3;
    private static final int PROFILE_PREVIEW_MAX_WIDTH = 280;
    private static final int PROFILE_PREVIEW_MAX_HEIGHT = 280;

    public static final String PROFILE_PICTURE_JSON_ATTRIBUTE = "complete";
    public static final String PROFILE_PREVIEW_PICTURE_JSON_ATTRIBUTE = "preview";

    private static final Logger log = ZetaLogger.getLog(BackendAPIWrappers.class.getSimpleName());

    public static Future<String> initMessageListener(ClientUser forUser,
                                                     Map<String, String> additionalExpectedHeaders) throws Exception {
        IMAPSMailbox mbox = IMAPSMailbox.getInstance(forUser.getEmail(), forUser.getPassword());
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, forUser.getEmail());
        if (additionalExpectedHeaders != null) {
            expectedHeaders.putAll(additionalExpectedHeaders);
        }
        return mbox.getMessage(expectedHeaders, ACTIVATION_TIMEOUT);
    }

    public static Future<String> initMessageListener(String forEmail, String forPassword,
                                                     Map<String, String> additionalExpectedHeaders) throws Exception {
        IMAPSMailbox mbox = IMAPSMailbox.getInstance(forEmail, forPassword);
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, forEmail);
        if (additionalExpectedHeaders != null) {
            expectedHeaders.putAll(additionalExpectedHeaders);
        }
        return mbox.getMessage(expectedHeaders, ACTIVATION_TIMEOUT);
    }

    private static <T> T retryOnBackendFailure(int retriesCount,
                                               FunctionalInterfaces.ISupplierWithException<T> r) throws Exception {
        int ntry = 1;
        BackendRequestException savedException = null;
        while (ntry <= retriesCount) {
            try {
                return r.call();
            } catch (BackendRequestException e) {
                if (e.getReturnCode() != 500) {
                    throw e;
                }
                savedException = e;
                Thread.sleep(1000 * ntry);
            }
            ntry++;
        }
        throw savedException;
    }

    /**
     * Creates a new user by sending the corresponding request to the backend
     *
     * @param user ClientUser instance with initial user parameters
     *             (name/email/password)
     * @return Created ClientUser instance (with id property filled)
     * @throws Exception
     */
    public static ClientUser createUserViaBackdoor(ClientUser user, RegistrationStrategy strategy) throws Exception {
        String activationCode;
        switch (strategy) {
            case ByEmail:
                BackendREST.registerNewUser(user.getEmail(), user.getName(), user.getPassword());
                activationCode = getActivationCodeForRegisteredEmail(user.getEmail());
                activateRegisteredEmailByBackdoorCode(user.getEmail(), activationCode, false);
                while (true) {
                    try {
                        attachUserPhoneNumber(user);
                        break;
                    } catch (BackendRequestException e) {
                        if (e.getReturnCode() == PHONE_NUMBER_ALREADY_REGISTERED_ERROR) {
                            user.forceTokenExpiration();
                            user.setPhoneNumber(new PhoneNumber(PhoneNumber.WIRE_COUNTRY_PREFIX));
                        } else {
                            throw e;
                        }
                    }
                }
                break;
            case ByEmailOnly:
                BackendREST.registerNewUser(user.getEmail(), user.getName(), user.getPassword());
                activationCode = getActivationCodeForRegisteredEmail(user.getEmail());
                activateRegisteredEmailByBackdoorCode(user.getEmail(), activationCode, false);
                break;
            case ByPhoneNumber:
            case ByPhoneNumberOnly:
                final int maxRetries = 5;
                int nTry = 0;
                while (true) {
                    try {
                        BackendREST.bookPhoneNumber(user.getPhoneNumber());
                        activationCode = getActivationCodeForBookedPhoneNumber(user.getPhoneNumber());
                        activateRegisteredUserByPhoneNumber(user.getPhoneNumber(), activationCode, true);
                        BackendREST.registerNewUser(user.getPhoneNumber(), user.getName(), activationCode);
                        break;
                    } catch (BackendRequestException e) {
                        if ((e.getReturnCode() == PHONE_NUMBER_ALREADY_REGISTERED_ERROR ||
                                e.getReturnCode() == 403) && nTry < maxRetries) {
                            // Assign different phone number to this user
                            // The current has been most likely already already created
                            user.forceTokenExpiration();
                            user.setPhoneNumber(new PhoneNumber(PhoneNumber.WIRE_COUNTRY_PREFIX));
                            nTry++;
                        } else {
                            throw new IllegalStateException(String.format(
                                    "User account with phone number '%s' cannot be created",
                                    user.getPhoneNumber().toString()), e);
                        }
                    }
                }
                if (strategy != RegistrationStrategy.ByPhoneNumberOnly) {
                    attachUserEmailUsingBackdoor(user);
                }
                break;
            default:
                throw new RuntimeException(String.format("Unknown registration strategy '%s'", strategy.name()));
        }
        return user;
    }

    public static void activateRegisteredUserByEmail(Future<String> activationMessage) throws Exception {
        final ActivationMessage registrationInfo = new ActivationMessage(activationMessage.get());
        final String key = registrationInfo.getXZetaKey();
        final String code = registrationInfo.getXZetaCode();
        log.debug(String
                .format("Received activation email message with key: %s, code: %s. Proceeding with activation...",
                        key, code));
        BackendREST.activateNewUser(key, code);
        log.debug(String.format("User '%s' is successfully activated", registrationInfo.getDeliveredToEmail()));
    }

    private static void activateRegisteredEmailByBackdoorCode(String email,
                                                              String code, boolean isDryRun) throws Exception {
        BackendREST.activateNewUser(email, code, isDryRun);
        log.debug(String.format("User '%s' is successfully activated", email));
    }

    private static String getActivationCodeForRegisteredEmail(String email) throws Exception {
        return BackendREST.getActivationDataViaBackdoor(email).getString("code");
    }

    public static String getActivationCodeForBookedPhoneNumber(PhoneNumber phoneNumber) throws Exception {
        return BackendREST.getActivationDataViaBackdoor(phoneNumber).getString("code");
    }

    public static void activateRegisteredUserByPhoneNumber(
            PhoneNumber phoneNumber, String activationCode, boolean isDryRun) throws Exception {
        BackendREST.activateNewUser(phoneNumber, activationCode, isDryRun);
        log.debug(String.format("User '%s' is successfully activated", phoneNumber.toString()));
    }

    private final static int MAX_ACTIVATION_CODE_GET_RETRIES = 6;
    private final static int BACKEND_ERROR_PHONE_NUMBER_NOT_BOOKED = 404;

    public static String getActivationCodeByPhoneNumber(PhoneNumber phoneNumber) throws Exception {
        int ntry = 1;
        BackendRequestException savedException;
        do {
            try {
                return getActivationCodeForBookedPhoneNumber(phoneNumber);
            } catch (BackendRequestException e) {
                if (e.getReturnCode() == BACKEND_ERROR_PHONE_NUMBER_NOT_BOOKED) {
                    // the number booking request has not been delivered to the
                    // backend yet
                    savedException = e;
                    log.debug(String
                            .format("The phone number '%s' seems to be not booked yet. Trying to get the activation code one more time (%d of %d)...",
                                    phoneNumber.toString(), ntry,
                                    MAX_ACTIVATION_CODE_GET_RETRIES));
                    Thread.sleep(2000 * ntry);
                } else {
                    throw e;
                }
            }
            ntry++;
        } while (ntry <= MAX_ACTIVATION_CODE_GET_RETRIES);
        throw savedException;
    }

    private final static int MAX_LOGIN_CODE_QUERIES = 6;

    public static String getLoginCodeByPhoneNumber(PhoneNumber phoneNumber) throws Exception {
        int ntry = 1;
        Exception savedException;
        do {
            try {
                return BackendREST.getLoginCodeViaBackdoor(phoneNumber)
                        .getString("code");
            } catch (BackendRequestException e) {
                log.error(String
                        .format("Failed to get login code for phone number '%s'. Retrying (%s of %s)...",
                                phoneNumber.toString(), ntry,
                                MAX_LOGIN_CODE_QUERIES));
                savedException = e;
                Thread.sleep(2000 * ntry);
            }
            ntry++;
        } while (ntry <= MAX_LOGIN_CODE_QUERIES);
        throw savedException;
    }

    public static void attachUserPhoneNumber(ClientUser user) throws Exception {
        BackendREST.updateSelfPhoneNumber(receiveAuthToken(user), user.getPhoneNumber());
        final String activationCode = getActivationCodeForBookedPhoneNumber(user.getPhoneNumber());
        activateRegisteredUserByPhoneNumber(user.getPhoneNumber(), activationCode, false);
    }

    public static void changeUserPassword(ClientUser user, String oldPassword, String newPassword) throws Exception {
        BackendREST.updateSelfPassword(receiveAuthToken(user), oldPassword, newPassword);
        user.setPassword(newPassword);
    }

    private static void attachUserEmailUsingBackdoor(ClientUser user) throws Exception {
        BackendREST.updateSelfEmail(receiveAuthToken(user), user.getEmail());
        final String activationCode = getActivationCodeForRegisteredEmail(user.getEmail());
        activateRegisteredEmailByBackdoorCode(user.getEmail(), activationCode, false);
        try {
            changeUserPassword(user, null, user.getPassword());
        } catch (BackendRequestException e) {
            // FIXME: I have no idea why this happens randomly
            if (e.getReturnCode() == LOGIN_CODE_HAS_NOT_BEEN_USED_ERROR) {
                changeUserPassword(user, user.getPassword(), user.getPassword());
            }
        }
    }

    public static String getUserActivationLink(Future<String> activationMessage) throws Exception {
        ActivationMessage registrationInfo = new ActivationMessage(activationMessage.get());
        return registrationInfo.extractActivationLink();
    }

    public static String getPasswordResetLink(Future<String> passwordResetMessage) throws Exception {
        PasswordResetMessage resetPassword = new PasswordResetMessage(passwordResetMessage.get());
        return resetPassword.extractPasswordResetLink();
    }

    public static String getMessageContent(Future<String> activationMessage) throws Exception {
        ActivationMessage sentence = new ActivationMessage(activationMessage.get());
        return sentence.getContent();
    }

    public static void sendConnectionRequest(ClientUser userFrom, ClientUser userTo) throws Exception {
        sendConnectRequest(userFrom, userTo, userTo.getName(), CommonSteps.CONNECTION_MESSAGE);
    }

    public static void sendDialogMessage(ClientUser fromUser, ClientUser toUser, String message) throws Exception {
        final String convId = getConversationWithSingleUser(fromUser, toUser);
        sendConversationMessage(fromUser, convId, message);
    }

    public static void sendDialogMessageByChatName(ClientUser fromUser, String toChat, String message) throws Exception {
        String id = getConversationIdByName(fromUser, toChat);
        sendConversationMessage(fromUser, id, message);
    }

    private static AuthToken receiveAuthToken(ClientUser user) throws Exception {
        return new AuthToken(user.getTokenType(), user.getToken());
    }

    public static void sendPictureToSingleUserConversation(ClientUser userFrom,
                                                           ClientUser userTo, String path) throws Exception {
        final String convId = getConversationWithSingleUser(userFrom, userTo);
        final byte[] srcImageAsByteArray = Files.readAllBytes(Paths.get(path));
        BackendREST.sendPicture(receiveAuthToken(userFrom), convId, srcImageAsByteArray, getImageMimeType(path));
    }

    public static void sendPictureToSingleUserConversationOtr(ClientUser userFrom, ClientUser userTo, String path,
                                                              RemoteDevicesManager remoteDevicesManager) throws Exception {
        final String convId = getConversationWithSingleUser(userFrom, userTo);
        remoteDevicesManager.sendImage(userFrom, convId, path);
    }

    private static String getImageMimeType(String path) {
        if (path.toLowerCase().endsWith(".png")) {
            return ImageAssetProcessor.MIME_TYPE_PNG;
        } else if (path.toLowerCase().endsWith(".jpg")
                || path.toLowerCase().endsWith(".jpeg")) {
            return ImageAssetProcessor.MIME_TYPE_JPEG;
        } else if (path.toLowerCase().endsWith(".gif")) {
            return ImageAssetProcessor.MIME_TYPE_GIF;
        } else {
            throw new RuntimeException(String.format(
                    "Cannot detect MIME type for the image by path %s", path));
        }
    }

    public static void sendPictureToChatByName(ClientUser userFrom, String chatName, String path) throws Exception {
        final byte[] srcImageAsByteArray = Files.readAllBytes(Paths.get(path));
        BackendREST.sendPicture(receiveAuthToken(userFrom),
                getConversationIdByName(userFrom, chatName),
                srcImageAsByteArray, getImageMimeType(path));
    }

    public static void sendPictureToChatByNameOtr(ClientUser userFrom, String chatName, String path,
                                                  RemoteDevicesManager remoteDevicesManager) throws Exception {
        final String convId = getConversationIdByName(userFrom, chatName);
        remoteDevicesManager.sendImage(userFrom, convId, path);
    }

    public static String getConversationIdByName(ClientUser ownerUser, String conversationName) throws Exception {
        JSONArray jsonArray = getConversations(ownerUser);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject conversation = (JSONObject) jsonArray.get(i);
            final String conversationId = conversation.getString("id");
            String name = "null";
            if (conversation.get("name") instanceof String) {
                name = conversation.getString("name");
            }
            name = name.replaceAll("\uFFFC", "").trim();
            if (name.equals("null") || name.equals(ownerUser.getName())) {
                conversation = (JSONObject) conversation.get("members");
                JSONArray otherArray = (JSONArray) conversation.get("others");
                if (otherArray.length() == 1) {
                    String id = ((JSONObject) otherArray.get(0))
                            .getString("id");
                    String contactName = getUserNameByID(id, ownerUser);
                    if (contactName.equals(conversationName)) {
                        return conversationId;
                    }
                }
            }
            if (name.equals(conversationName)) {
                return conversationId;
            }
        }
        throw new NoSuchElementException(String.format(
                "Conversation '%s' does not exist for user '%s'",
                conversationName, ownerUser.getName()));
    }

    private static String getConversationWithSingleUser(ClientUser fromUser, ClientUser toUser) throws Exception {
        String conversationId;
        JSONArray jsonArray = getConversations(fromUser);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject conversation = (JSONObject) jsonArray.get(i);
            conversationId = conversation.getString("id");
            conversation = (JSONObject) conversation.get("members");
            JSONArray otherArray = (JSONArray) conversation.get("others");
            if (otherArray.length() == 1) {
                String id = ((JSONObject) otherArray.get(0)).getString("id");
                if (id.equals(toUser.getId())) {
                    return conversationId;
                }
            }
        }
        throw new RuntimeException(
                String.format(
                        "There is no conversation between users '%s' and '%s' on the backend",
                        fromUser.getName(), toUser.getName()));
    }

    public static void generateNewLoginCode(ClientUser user) throws Exception {
        BackendREST.generateLoginCode(user.getPhoneNumber());
    }

    public static ClientToken login(final String email, final String password,
                                    final PhoneNumber phoneNumber) throws Exception {
        JSONObject loggedUserInfo = new JSONObject();
        int tryNum = 0;
        while (tryNum < MAX_BACKEND_RETRIES) {
            try {
                try {
                    loggedUserInfo = BackendREST.login(email, password);
                } catch (BackendRequestException e) {
                    // Retry in case the user has only phone number attached
                    if (e.getReturnCode() == AUTH_FAILED_ERROR) {
                        try {
                            BackendREST.generateLoginCode(phoneNumber);
                        } catch (BackendRequestException e1) {
                            if (e1.getReturnCode() != LOGIN_CODE_HAS_NOT_BEEN_USED_ERROR) {
                                throw e1;
                            }
                        }
                        final String code = BackendREST.getLoginCodeViaBackdoor(phoneNumber).getString("code");
                        loggedUserInfo = BackendREST.login(phoneNumber, code);
                    }
                }
                break;
            } catch (BackendRequestException e) {
                if (e.getReturnCode() == REQUEST_TOO_FREQUENT_ERROR) {
                    log.debug(String.format(
                            "Login request failed. Retrying (%d of %d)...",
                            tryNum + 1, MAX_BACKEND_RETRIES));
                    e.printStackTrace();
                    tryNum++;
                    if (tryNum >= MAX_BACKEND_RETRIES) {
                        throw e;
                    } else {
                        Thread.sleep((tryNum + 1) * 2000);
                    }
                } else {
                    throw e;
                }
            }
        }

        final ClientToken result = new ClientToken();
        result.setToken(loggedUserInfo.getString("access_token"));
        result.setTokenType(loggedUserInfo.getString("token_type"));
        final JSONObject additionalUserInfo = BackendREST.getUserInfo(
                new AuthToken(result.getTokenType(), result.getToken()));
        result.setId(additionalUserInfo.getString("id"));
        return result;
    }

    private static String getUserNameByID(String id, ClientUser user) throws Exception {
        final JSONObject userInfo = BackendREST.getUserInfoByID(id, receiveAuthToken(user));
        return userInfo.getString("name");
    }

    public static String getUserPictureHash(ClientUser user) throws Exception {
        final JSONObject userInfo = BackendREST.getUserInfo(receiveAuthToken(user));
        final String picture = userInfo.getJSONArray("picture").toString();
        return DigestUtils.sha256Hex(picture);
    }

    public static String getUserAssetKey(ClientUser user, String size) throws Exception {
        final JSONObject userInfo = BackendREST.getUserInfo(receiveAuthToken(user));
        final JSONArray assets = userInfo.getJSONArray("assets");
        for (int i = 0; i < assets.length(); i++) {
            JSONObject asset = assets.getJSONObject(i);
            if (size.equals(asset.getString("size"))) {
                return asset.getString("key");
            }
        }
        throw new IllegalArgumentException("No user asset found with size: " + size + " in " + assets);
    }

    public static void sendConnectRequest(ClientUser user, ClientUser contact,
                                          String connectName, String message) throws Exception {
        BackendREST.sendConnectRequest(receiveAuthToken(user), contact.getId(), connectName, message);
    }

    private static List<JSONObject> getAllConnections(ClientUser user) throws Exception {
        String startId = null;
        JSONObject connectionsInfo;
        final List<JSONObject> result = new ArrayList<>();
        do {
            connectionsInfo = BackendREST.getConnectionsInfo(receiveAuthToken(user), null, startId);
            final JSONArray connections = connectionsInfo.getJSONArray("connections");
            for (int i = 0; i < connections.length(); i++) {
                result.add(connections.getJSONObject(i));
            }
            if (connections.length() > 0) {
                startId = connections.getJSONObject(connections.length() - 1).getString("to");
            }
        } while (connectionsInfo.getBoolean("has_more"));
        return result;
    }

    public static void acceptAllIncomingConnectionRequests(ClientUser asUser) throws Exception {
        updateConnections(asUser, ConnectionStatus.Pending, ConnectionStatus.Accepted, Optional.empty());
    }

    public static void acceptIncomingConnectionRequest(ClientUser asUser, ClientUser fromUser) throws Exception {
        updateConnections(asUser, ConnectionStatus.Pending, ConnectionStatus.Accepted,
                Optional.of(Collections.singletonList(fromUser.getId())));
    }

    public static void cancelAllOutgoingConnections(ClientUser asUser) throws Exception {
        updateConnections(asUser, ConnectionStatus.Sent, ConnectionStatus.Canceled, Optional.empty());
    }

    public static void ignoreAllIncomingConnections(ClientUser asUser) throws Exception {
        updateConnections(asUser, ConnectionStatus.Pending, ConnectionStatus.Ignored, Optional.empty());
    }

    private static void updateConnections(ClientUser asUser, ConnectionStatus srcStatus, ConnectionStatus dstStatus,
                                          Optional<List<String>> forUserIds) throws Exception {
        final List<JSONObject> connections = getAllConnections(asUser);
        for (final JSONObject connection : connections) {
            final String to = connection.getString("to");
            final String status = connection.getString("status");
            if (status.equals(srcStatus.toString())) {
                if (forUserIds.isPresent() && forUserIds.get().contains(to) || !forUserIds.isPresent()) {
                    changeConnectRequestStatus(asUser, to, dstStatus);
                }
            }
        }
    }

    public static void changeConnectRequestStatus(ClientUser user,
                                                  String connectionId, ConnectionStatus newStatus) throws Exception {
        BackendREST.changeConnectRequestStatus(receiveAuthToken(user), connectionId, newStatus);
    }

    public static void createGroupConversation(ClientUser user,
                                               List<ClientUser> contacts, String conversationName) throws Exception {
        List<String> ids = new ArrayList<>();
        for (ClientUser contact : contacts) {
            ids.add(contact.getId());
        }
        BackendREST.createGroupConversation(receiveAuthToken(user), ids, conversationName);
    }

    public static void addContactsToGroupConversation(ClientUser asUser,
                                                      List<ClientUser> contacts, String conversationName)
            throws Exception {
        List<String> ids = new ArrayList<>();
        for (ClientUser contact : contacts) {
            ids.add(contact.getId());
        }
        BackendREST.addContactsToGroupConvo(receiveAuthToken(asUser), ids,
                getConversationIdByName(asUser, conversationName));
    }

    public static void removeUserFromGroupConversation(ClientUser asUser,
                                                       ClientUser contact, String conversationName) throws Exception {
        String contactId = contact.getId();
        BackendREST.removeContactFromGroupConvo(receiveAuthToken(asUser),
                contactId, getConversationIdByName(asUser, conversationName));
    }

    public static void sendConversationMessage(ClientUser userFrom, String convId, String message) throws Exception {
        BackendREST.sendConversationMessage(receiveAuthToken(userFrom), convId, message);
    }

    public static void sendConversationMessagesOtr(ClientUser userFrom, String convId, List<String> messages,
                                                   RemoteDevicesManager remoteDevicesManager) throws Exception {
        for (String message : messages) {
            remoteDevicesManager.sendConversationMessage(userFrom, convId, message);
            Thread.sleep(50);
        }
    }

    public static void uploadSelfContact(ClientUser selfUser) throws Exception {
        final AddressBook addressBook = new AddressBook();
        final List<String> selfData = new ArrayList<>();
        selfData.add(selfUser.getEmail());
        selfData.add(selfUser.getPhoneNumber().toString());
        addressBook.setSelfData(selfData);
        BackendREST.uploadAddressBook(receiveAuthToken(selfUser), addressBook);
    }

    public static void uploadAddressBookWithContacts(ClientUser user, List<String> emailsToAdd) throws Exception {
        final AddressBook addressBook = new AddressBook();
        for (String email : emailsToAdd) {
            Card card = new Card();
            card.addContact(email);
            addressBook.addCard(card);
        }
        BackendREST.uploadAddressBook(receiveAuthToken(user), addressBook);
    }

    public static String sendConversationPing(ClientUser userFrom, String convId) throws Exception {
        JSONObject response = BackendREST.sendConversationPing(receiveAuthToken(userFrom), convId);
        return response.getString("id");
    }

    public static JSONArray getConversations(ClientUser user) throws Exception {
        final JSONArray result = new JSONArray();
        String startId = null;
        JSONObject conversationsInfo = new JSONObject();
        do {
            int tryNum = 0;
            while (tryNum < MAX_BACKEND_RETRIES) {
                try {
                    conversationsInfo = BackendREST.getConversationsInfo(receiveAuthToken(user), startId);
                    break;
                } catch (BackendRequestException e) {
                    if (e.getReturnCode() == SERVER_SIDE_ERROR) {
                        log.debug(String
                                .format("Server side request failed. Retrying (%d of %d)...",
                                        tryNum + 1, MAX_BACKEND_RETRIES));
                        e.printStackTrace();
                        tryNum++;
                        if (tryNum >= MAX_BACKEND_RETRIES) {
                            throw e;
                        } else {
                            Thread.sleep((tryNum + 1) * 2000);
                        }
                    } else {
                        throw e;
                    }
                }
            }
            final JSONArray response = conversationsInfo.getJSONArray("conversations");
            for (int i = 0; i < response.length(); i++) {
                result.put(response.getJSONObject(i));
            }
            if (response.length() > 0) {
                startId = response.getJSONObject(response.length() - 1)
                        .getString("id");
            }
        } while (conversationsInfo.getBoolean("has_more"));
        return result;
    }

    public static void removeUserPicture(ClientUser user) throws Exception {
        retryOnBackendFailure(2,
                () -> {
                    // v2 assets
                    BackendREST.updateSelfInfo(receiveAuthToken(user),
                            Optional.empty(), Optional.of(new HashMap<>()), Optional.empty());
                    // v3 assets
                    BackendREST.updateSelfAssets(receiveAuthToken(user), new HashSet<>());
                    return null;
                }
        );
    }

    public static void updateUserPicture(ClientUser user, String picturePath) throws Exception {
        // upload user picture through the old asset v2 way
        updateUserPictureV2(user, picturePath);
        // upload user picture through the new asset v3 way
        updateUserPictureV3(user, picturePath);
    }

    public static void updateUserPictureV3(ClientUser user, String picturePath) throws Exception {
        BufferedImage image = ImageUtil.readImageFromFile(picturePath);
        BufferedImage square = ImageUtil.cropToSquare(image);
        BufferedImage preview = ImageUtil.scaleTo(square, PROFILE_PREVIEW_MAX_WIDTH, PROFILE_PREVIEW_MAX_HEIGHT);
        String previewKey = BackendREST.uploadAssetV3(receiveAuthToken(user), true, "persistent",
                ImageUtil.asByteArray(preview));
        String completeKey = BackendREST.uploadAssetV3(receiveAuthToken(user), true, "persistent",
                ImageUtil.asByteArray(image));
        Set<AssetV3> assets = new HashSet<>();
        assets.add(new AssetV3(previewKey, "image", PROFILE_PREVIEW_PICTURE_JSON_ATTRIBUTE));
        assets.add(new AssetV3(completeKey, "image", PROFILE_PICTURE_JSON_ATTRIBUTE));
        retryOnBackendFailure(2,
                () -> {
                    BackendREST.updateSelfAssets(receiveAuthToken(user), assets);
                    return null;
                }
        );
    }

    public static void updateUserPictureV2(ClientUser user, String picturePath) throws Exception {
        final String convId = user.getId();
        final byte[] srcImageAsByteArray = Files.readAllBytes(new File(picturePath).toPath());
        ImageAssetData srcImgData = new ImageAssetData(convId, srcImageAsByteArray, getImageMimeType(picturePath));
        srcImgData.setIsPublic(true);
        srcImgData.setCorrelationId(String.valueOf(UUID.randomUUID()));
        srcImgData.setNonce(srcImgData.getCorrelationId());
        ImageAssetProcessor imgProcessor = new SelfImageProcessor(srcImgData);
        ImageAssetRequestBuilder reqBuilder = new ImageAssetRequestBuilder(imgProcessor);
        retryOnBackendFailure(2,
                () -> {
                    final Map<JSONObject, AssetData> sentPictures = BackendREST.sendPicture(
                            receiveAuthToken(user), convId, reqBuilder);
                    final Map<String, AssetData> processedAssets = new LinkedHashMap<>();
                    for (Map.Entry<JSONObject, AssetData> entry : sentPictures.entrySet()) {
                        final String postedImageId = entry.getKey().getJSONObject("data").getString("id");
                        processedAssets.put(postedImageId, entry.getValue());
                    }
                    BackendREST.updateSelfInfo(receiveAuthToken(user),
                            Optional.empty(), Optional.of(processedAssets), Optional.empty());
                    return null;
                }
        );
    }

    public static void updateName(ClientUser user, String newName) throws Exception {
        BackendREST.updateSelfInfo(receiveAuthToken(user),
                Optional.empty(), Optional.empty(), Optional.of(newName));
        user.setName(newName);
    }

    public static void updateUserAccentColor(ClientUser user, AccentColor color) throws Exception {
        BackendREST.updateSelfInfo(receiveAuthToken(user),
                Optional.of(color.getId()), Optional.empty(), Optional.empty());
        user.setAccentColor(color);
    }

    public static void updateUniqueUsername(ClientUser user, String username) throws Exception {
        BackendREST.updateSelfHandle(receiveAuthToken(user), username);
        user.setUniqueUsername(username);
    }

    public static Optional<String> getUniqueUsername(ClientUser user) throws Exception {
        final JSONObject userInfo = BackendREST.getUserInfo(receiveAuthToken(user));
        if (userInfo.has("handle")) {
            return Optional.of(userInfo.getString("handle"));
        }
        return Optional.empty();
    }

    public static Optional<String> getEmail(ClientUser user) throws Exception {
        final JSONObject userInfo = BackendREST.getUserInfo(receiveAuthToken(user));
        if (userInfo.has("email")) {
            return Optional.of(userInfo.getString("email"));
        }
        return Optional.empty();
    }

    public static String getName(ClientUser user) throws Exception {
        final JSONObject userInfo = BackendREST.getUserInfo(receiveAuthToken(user));
        return userInfo.getString("name");
    }

    public static Optional<String> getPhoneNumber(ClientUser user) throws Exception {
        final JSONObject userInfo = BackendREST.getUserInfo(receiveAuthToken(user));
        if (userInfo.has("phone")) {
            return Optional.of(userInfo.getString("phone"));
        }
        return Optional.empty();
    }

    public static void changeGroupChatName(ClientUser asUser, String conversationIDToRename, String newConversationName)
            throws Exception {
        BackendREST.changeConversationName(receiveAuthToken(asUser), conversationIDToRename, newConversationName);
    }

    public static void unregisterPushToken(ClientUser asUser, String pushToken) throws Exception {
        BackendREST.unregisterPushToken(receiveAuthToken(asUser), pushToken);
    }

    public static class NoContactsFoundException extends Exception {
        private static final long serialVersionUID = -7682778364420522320L;

        public NoContactsFoundException(String msg) {
            super(msg);
        }

    }

    public static void waitUntilCommonContactsFound(ClientUser searchByUser, ClientUser destUser, int expectedCount,
                                                    boolean orMore, int timeoutSeconds) throws Exception {
        final long startTimestamp = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTimestamp <= timeoutSeconds * 1000) {
            JSONObject searchResult = new JSONObject();
            try {
                searchResult = BackendREST.searchForCommonContacts(receiveAuthToken(searchByUser), destUser.getId());
            } catch (BackendRequestException e) {
                if (e.getReturnCode() == SERVER_SIDE_ERROR) {
                    Thread.sleep(1000);
                    continue;
                }
            }
            int currentCount = 0;
            if (searchResult.has("documents") && (searchResult.get("documents") instanceof JSONArray)) {
                currentCount = searchResult.getJSONArray("documents").length();
            }
            if (currentCount == expectedCount || (orMore && currentCount >= expectedCount)) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new NoContactsFoundException(String.format("%s 's common contact(s) with user '%s' were not found within %s second(s) timeout",
                expectedCount, destUser.getName(), timeoutSeconds));
    }

    public static void waitUntilContactsFound(ClientUser searchByUser,
                                              String query, int expectedCount, boolean orMore, int timeoutSeconds)
            throws Exception {
        final long startTimestamp = System.currentTimeMillis();
        int currentCount;
        while (System.currentTimeMillis() - startTimestamp <= timeoutSeconds * 1000) {
            JSONObject searchResult;
            try {
                searchResult = BackendREST.searchForContacts(receiveAuthToken(searchByUser), query);
            } catch (BackendRequestException e) {
                if (e.getReturnCode() == 500) {
                    Thread.sleep(1000);
                    continue;
                } else {
                    throw e;
                }
            }
            if (searchResult.has("documents") && (searchResult.get("documents") instanceof JSONArray)) {
                currentCount = searchResult.getJSONArray("documents").length();
            } else {
                currentCount = 0;
            }
            if (currentCount == expectedCount || (orMore && currentCount >= expectedCount)) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new NoContactsFoundException(String.format("%s contact(s) '%s' were not found within %s second(s) timeout",
                expectedCount, query, timeoutSeconds));
    }

    public static void waitUntilSuggestionFound(ClientUser searchByUser,
                                                String query, int expectedCount, boolean orMore,
                                                int timeoutSeconds) throws Exception {
        final long startTimestamp = System.currentTimeMillis();
        int currentCount;
        AuthToken token = receiveAuthToken(searchByUser);
        while (System.currentTimeMillis() - startTimestamp <= timeoutSeconds * 1000) {
            JSONObject searchResult;
            try {
                searchResult = BackendREST.searchForSuggestionsForContact(token, query);
            } catch (BackendRequestException e) {
                if (e.getReturnCode() == 500) {
                    Thread.sleep(1000);
                    continue;
                } else {
                    throw e;
                }
            }
            if (searchResult.has("documents") && (searchResult.get("documents") instanceof JSONArray)) {
                currentCount = searchResult.getJSONArray("documents").length();
            } else {
                currentCount = 0;
            }
            if (currentCount == expectedCount || (orMore && currentCount >= expectedCount)) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new NoContactsFoundException(String.format("%s contact(s) '%s' no suggestions found " +
                        "within %s second(s) timeout",
                expectedCount, query, timeoutSeconds));
    }

    public static void waitUntilTopPeopleContactsFound(ClientUser searchByUser,
                                                       int size, int expectedCount, boolean orMore, int timeoutSeconds)
            throws Exception {
        final long startTimestamp = System.currentTimeMillis();
        int currentCount;
        while (System.currentTimeMillis() - startTimestamp <= timeoutSeconds * 1000) {
            JSONObject searchResult;
            try {
                searchResult = BackendREST.searchForTopPeopleContacts(receiveAuthToken(searchByUser), size);
            } catch (BackendRequestException e) {
                if (e.getReturnCode() == 500) {
                    Thread.sleep(1000);
                    continue;
                } else {
                    throw e;
                }
            }
            if (searchResult.has("documents") && (searchResult.get("documents") instanceof JSONArray)) {
                currentCount = searchResult.getJSONArray("documents").length();
            } else {
                currentCount = 0;
            }
            if (currentCount == expectedCount || (orMore && currentCount >= expectedCount)) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new NoContactsFoundException(String.format(
                "%s contact(s) '%s' were not found within %s second(s) timeout",
                expectedCount, size, timeoutSeconds));
    }

    public static void waitUntilContactNotFound(ClientUser searchByUser,
                                                String query, int timeoutSeconds) throws Exception {
        final long startTimestamp = System.currentTimeMillis();
        int currentCount = 0;
        do {
            JSONObject searchResult;
            try {
                searchResult = BackendREST.searchForContacts(receiveAuthToken(searchByUser), query);
            } catch (BackendRequestException e) {
                if (e.getReturnCode() == 500) {
                    Thread.sleep(1000);
                    continue;
                } else {
                    throw e;
                }
            }
            if (searchResult.has("documents") && (searchResult.get("documents") instanceof JSONArray)) {
                currentCount = searchResult.getJSONArray("documents").length();
            }
            if (currentCount <= 0) {
                return;
            }
            Thread.sleep(1000);
        } while (System.currentTimeMillis() - startTimestamp <= timeoutSeconds * 1000);
        throw new AssertionError(String.format(
                "%s contact(s) '%s' are still found after %s second(s) timeout",
                currentCount, query, timeoutSeconds));
    }

    public static void sendPersonalInvitation(ClientUser ownerUser,
                                              String toEmail, String toName, String message) throws Exception {
        BackendREST.sendPersonalInvitation(receiveAuthToken(ownerUser), toEmail, toName, message);
    }

    public static Optional<InvitationMessage> getInvitationMessage(ClientUser user) throws Exception {
        IMAPSMailbox mbox = IMAPSMailbox.getInstance(user.getEmail(), user.getPassword());
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, user.getEmail());
        try {
            final String msg = mbox.getMessage(expectedHeaders,
                    INVITATION_RECEIVING_TIMEOUT, 0).get();
            return Optional.of(new InvitationMessage(msg));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static List<OtrClient> getOtrClients(ClientUser forUser) throws Exception {
        final List<OtrClient> result = new ArrayList<>();
        final JSONArray responseList = BackendREST.getClients(receiveAuthToken(forUser));
        for (int clientIdx = 0; clientIdx < responseList.length(); clientIdx++) {
            result.add(new OtrClient(responseList.getJSONObject(clientIdx)));
        }
        return result;
    }

    public static void removeOtrClient(ClientUser forUser, OtrClient otrClientInfo) throws Exception {
        BackendREST.deleteClient(receiveAuthToken(forUser), forUser.getPassword(), otrClientInfo.getId());
    }
}
