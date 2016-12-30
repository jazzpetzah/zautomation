package com.wearezeta.auto.common.wire_actors;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Optional;

class ActorsREST {
    private static final Logger log = ZetaLogger.getLog(ActorsREST.class.getSimpleName());

    private static final String EMPTY_BODY = new JSONObject().toString();

    private static final CommonRESTHandlers restHandlers = new CommonRESTHandlers(
            ActorsREST::verifyRequestResult, 1);

    public static String getBaseURI() throws Exception {
        return CommonUtils.getActorsServerUrl(ActorsREST.class);
    }

    private static void verifyRequestResult(int currentResponseCode, int[] acceptableResponseCodes, String message)
            throws ActorsRequestException {
        if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
            throw new ActorsRequestException(
                    String.format("Actors request failed. Request return code is: %d. " +
                                    "Expected codes are: %s. Message from service is: %s",
                            currentResponseCode, Arrays.toString(acceptableResponseCodes), message),
                    currentResponseCode);
        }
    }

    private static Invocation.Builder buildRequest(String restAction)
            throws Exception {
        final String dstUrl = String.format("%s/%s", getBaseURI(), restAction);
        log.debug(String.format("Making request to %s...", dstUrl));
        final Client client = ClientBuilder.newClient();
        return client
                .target(dstUrl)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON);
    }

    public static JSONObject getDevices() throws Exception {
        final Invocation.Builder webResource = buildRequest("devices/");
        final String output = restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject createDevice(Optional<String> name) throws Exception {
        final Invocation.Builder webResource = buildRequest("devices/create");
        final JSONObject requestBody = new JSONObject();
        name.ifPresent(x -> requestBody.put("name", x));
        final String output = restHandlers.httpPost(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static void removeDevice(String uuid) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s", uuid));
        restHandlers.httpDelete(webResource, new int[]{HttpStatus.SC_OK});
    }

    public static JSONObject loginToDevice(String uuid, String email, String password) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/login", uuid));
        final JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);
        final String output = restHandlers.httpPost(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject getDeviceFingerprint(String uuid) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/fingerprint", uuid));
        final String output = restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject getDeviceId(String uuid) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/id", uuid));
        final String output = restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject getUniqueUsername(String uuid) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/user/uniqueName", uuid));
        final String output = restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject setDeviceLabel(String uuid, String newLabel) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/label", uuid));
        final JSONObject requestBody = new JSONObject();
        requestBody.put("label", newLabel);
        final String output = restHandlers.httpPut(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject setDeviceAssetsVersion(String uuid, String newVersion) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/assets/version", uuid));
        final JSONObject requestBody = new JSONObject();
        requestBody.put("version", newVersion);
        final String output = restHandlers.httpPut(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject sendMessage(String uuid, String convoId, String message) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/send/message",
                uuid, convoId));
        final JSONObject requestBody = new JSONObject();
        requestBody.put("message", message);
        final String output = restHandlers.httpPost(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject sendGiphy(String uuid, String convoId, String giphyTag) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/send/giphy",
                uuid, convoId));
        final JSONObject requestBody = new JSONObject();
        requestBody.put("message", giphyTag);
        final String output = restHandlers.httpPost(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject sendLocation(String uuid, String convoId,
                                          float lon, float lat, String address, int zoom) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/send/location",
                uuid, convoId));
        final JSONObject requestBody = new JSONObject();
        requestBody.put("lon", lon);
        requestBody.put("lat", lat);
        requestBody.put("address", address);
        requestBody.put("zoom", zoom);
        final String output = restHandlers.httpPost(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject sendLocation(String uuid, String convoId) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/send/location",
                uuid, convoId));
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject sendImage(String uuid, String convoId, String base64Content, String fileName)
            throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/send/image",
                uuid, convoId));
        final JSONObject requestBody = new JSONObject();
        requestBody.put("message", base64Content);
        requestBody.put("fileName", fileName);
        final String output = restHandlers.httpPost(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject sendFile(String uuid, String convoId, String base64Content,
                                      String mimeType, String fileName) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/send/file",
                uuid, convoId));
        final JSONObject requestBody = new JSONObject();
        requestBody.put("message", base64Content);
        requestBody.put("mimeType", mimeType);
        requestBody.put("fileName", fileName);
        final String output = restHandlers.httpPost(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject sendPing(String uuid, String convoId) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/send/ping",
                uuid, convoId));
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject sendTyping(String uuid, String convoId) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/send/typing",
                uuid, convoId));
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject clearConversation(String uuid, String convoId) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/clear",
                uuid, convoId));
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject muteConversation(String uuid, String convoId) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/mute",
                uuid, convoId));
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject unmuteConversation(String uuid, String convoId) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/unmute",
                uuid, convoId));
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject archiveConversation(String uuid, String convoId) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/archive",
                uuid, convoId));
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject unarchiveConversation(String uuid, String convoId) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/unarchive",
                uuid, convoId));
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject setEpehemeralTimeout(String uuid, String convoId, Timedelta timeout) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/conversations/%s/unarchive",
                uuid, convoId));
        final JSONObject requestBody = new JSONObject();
        requestBody.put("msTimeout", timeout.asMilliSeconds());
        final String output = restHandlers.httpPost(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject cancelConnection(String uuid, String connId) throws Exception {
        final Invocation.Builder webResource = buildRequest(String.format("devices/%s/connections/%s/cancel",
                uuid, connId));
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject deleteMessage(String uuid, String convId, String msgId) throws Exception {
        final Invocation.Builder webResource = buildRequest(
                String.format("devices/%s/conversations/%s/messages/%s/delete", uuid, convId, msgId)
        );
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject deleteMessageEverywhere(String uuid, String convId, String msgId) throws Exception {
        final Invocation.Builder webResource = buildRequest(
                String.format("devices/%s/conversations/%s/messages/%s/deleteEverywhere", uuid, convId, msgId)
        );
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject updateMessage(String uuid, String convId, String msgId, String newMessage)
            throws Exception {
        final Invocation.Builder webResource = buildRequest(
                String.format("devices/%s/conversations/%s/messages/%s/update", uuid, convId, msgId)
        );
        final JSONObject requestBody = new JSONObject();
        requestBody.put("message", newMessage);
        final String output = restHandlers.httpPost(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject readEphemeralMessage(String uuid, String convId, String msgId) throws Exception {
        final Invocation.Builder webResource = buildRequest(
                String.format("devices/%s/conversations/%s/messages/%s/readEphemeral", uuid, convId, msgId)
        );
        final String output = restHandlers.httpPost(webResource, EMPTY_BODY, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject reactMessage(String uuid, String convId, String msgId, String reaction) throws Exception {
        final Invocation.Builder webResource = buildRequest(
                String.format("devices/%s/conversations/%s/messages/%s/react", uuid, convId, msgId)
        );
        final JSONObject requestBody = new JSONObject();
        requestBody.put("reaction", reaction);
        final String output = restHandlers.httpPost(webResource, requestBody.toString(), new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject getMessagesInfo(String uuid, String convId) throws Exception {
        final Invocation.Builder webResource = buildRequest(
                String.format("devices/%s/conversations/%s/messagesInfo", uuid, convId)
        );
        final String output = restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }
}
