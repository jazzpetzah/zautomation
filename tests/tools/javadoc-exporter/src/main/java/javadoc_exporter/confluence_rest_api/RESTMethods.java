package javadoc_exporter.confluence_rest_api;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

import javadoc_exporter.ResourceHelpers;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * https://developer.atlassian.com/display/CONFDEV/Confluence+REST+API+Examples
 * https://bunjil.jira-dev.com/wiki/plugins/servlet/restbrowser
 */
class RESTMethods {
    private static final String CONFLUENCE_URL_KEY = "ConfluenceURL";
    private static final String CONFLUENCE_AUTH_TOKEN_KEY = "ConfluenceAuthToken";
    private static final String MAIN_PROPERTIES = "main.properties";

    private static String getConfluenceURL() throws IOException {
        return ResourceHelpers.getValueFromConfigFile(CONFLUENCE_URL_KEY,
                MAIN_PROPERTIES);
    }

    private static String getConfluenceAuthToken() throws IOException {
        return ResourceHelpers.getValueFromConfigFile(
                CONFLUENCE_AUTH_TOKEN_KEY, MAIN_PROPERTIES);
    }

    private static Client client = Client.create();

    private static void verifyRequestResult(String responseStr, int currentResponseCode,
                                            int[] acceptableResponseCodes) throws RESTApiError {
        if (acceptableResponseCodes.length > 0) {
            boolean isResponseCodeAcceptable = false;
            for (int code : acceptableResponseCodes) {
                if (code == currentResponseCode) {
                    isResponseCodeAcceptable = true;
                    break;
                }
            }
            if (!isResponseCodeAcceptable) {
                System.out.println(String.format(
                        "%s\n\nConfluence request failed. Request return code is: %d. Expected codes are: %s",
                        responseStr, currentResponseCode, Arrays.toString(acceptableResponseCodes)));
//				throw new RESTApiError(
//						String.format(
//								"%s\n\nConfluence request failed. Request return code is: %d. Expected codes are: %s",
//                                responseStr, currentResponseCode, Arrays.toString(acceptableResponseCodes)),
//						currentResponseCode);
            }
        }
    }

    private static String httpPost(Builder webResource, Object entity,
                                   int[] acceptableResponseCodes) throws RESTApiError {
        ClientResponse response;
        response = webResource.post(ClientResponse.class, entity);
        final String responseText = response.getEntity(String.class);
        verifyRequestResult(responseText, response.getStatus(), acceptableResponseCodes);
        return responseText;
    }

    private static String httpPut(Builder webResource, Object entity,
                                  int[] acceptableResponseCodes) throws RESTApiError {
        ClientResponse response = webResource.put(ClientResponse.class, entity);
        final String responseText = response.getEntity(String.class);
        verifyRequestResult(responseText, response.getStatus(), acceptableResponseCodes);
        return responseText;
    }

    private static String httpGet(Builder webResource,
                                  int[] acceptableResponseCodes) throws RESTApiError {
        ClientResponse response = webResource.get(ClientResponse.class);
        final String responseText = response.getEntity(String.class);
        verifyRequestResult(responseText, response.getStatus(), acceptableResponseCodes);
        return responseText;
    }

    private static String httpDelete(Builder webResource,
                                     int[] acceptableResponseCodes) throws RESTApiError {
        ClientResponse response = webResource.delete(ClientResponse.class);
        final String responseText = response.getEntity(String.class);
        verifyRequestResult(responseText, response.getStatus(), acceptableResponseCodes);
        return responseText;
    }

    private static Builder buildRequestWithAuth(String restAction)
            throws Exception {
        return client
                .resource(
                        String.format("%s/%s", getConfluenceURL(), restAction))
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,
                        String.format("Basic %s", getConfluenceAuthToken()));
    }

    private static final String TYPE_PAGE = "page";
    // Do we ever reach this limit?
    private static final int PAGES_LIMIT = 1000;
    private static final String DEFAULT_ENCODING = "utf-8";

    public static JSONObject getChildrenPages(long parentId, String[] expansions)
            throws Exception {
        final String expand = URLEncoder.encode(
                org.apache.commons.lang3.StringUtils.join(expansions, ","),
                DEFAULT_ENCODING);
        Builder webResource = buildRequestWithAuth(String.format(
                "content/%s/child/%s?limit=%s&expand=%s", parentId, TYPE_PAGE,
                PAGES_LIMIT, expand));
        final String output = httpGet(webResource,
                new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject createChildPage(long parentId, String spaceKey,
                                             String title, String pageBody) throws Exception {
        Builder webResource = buildRequestWithAuth("content");
        JSONObject requestBody = new JSONObject();
        requestBody.put("type", TYPE_PAGE);
        requestBody.put("title", title);

        JSONObject space = new JSONObject();
        space.put("key", spaceKey);
        requestBody.put("space", space);

        JSONArray ancestors = new JSONArray();
        JSONObject ancestor = new JSONObject();
        ancestor.put("type", TYPE_PAGE);
        ancestor.put("id", parentId);
        ancestors.put(ancestor);
        requestBody.put("ancestors", ancestors);

        JSONObject body = new JSONObject();
        JSONObject storage = new JSONObject();
        storage.put("value", pageBody);
        storage.put("representation", "storage");
        body.put("storage", storage);
        requestBody.put("body", body);

        final String output = httpPost(webResource, requestBody.toString(),
                new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static JSONObject updateChildPage(long parentId, long pageId,
                                             String spaceKey, String newTitle, String newBody, int versionNumber)
            throws Exception {
        Builder webResource = buildRequestWithAuth(String.format("content/%s",
                pageId));
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", pageId);
        requestBody.put("type", TYPE_PAGE);
        requestBody.put("title", newTitle);

        JSONObject space = new JSONObject();
        space.put("key", spaceKey);
        requestBody.put("space", space);

        JSONArray ancestors = new JSONArray();
        JSONObject ancestor = new JSONObject();
        ancestor.put("type", TYPE_PAGE);
        ancestor.put("id", parentId);
        ancestors.put(ancestor);
        requestBody.put("ancestors", ancestors);

        JSONObject body = new JSONObject();
        JSONObject storage = new JSONObject();
        storage.put("value", newBody);
        storage.put("representation", "storage");
        body.put("storage", storage);
        requestBody.put("body", body);

        JSONObject version = new JSONObject();
        version.put("number", versionNumber);
        version.put("message", getBodyHash(newBody));
        requestBody.put("version", version);

        final String output = httpPut(webResource, requestBody.toString(),
                new int[]{HttpStatus.SC_OK});
        return new JSONObject(output);
    }

    public static void removePage(long pageId) throws Exception {
        Builder webResource = buildRequestWithAuth(String.format("content/%s",
                pageId));
        try {
            httpDelete(webResource, new int[]{HttpStatus.SC_NO_CONTENT});
        } catch (UniformInterfaceException e) {
            // ignore silently
        }
    }

    public static String getBodyHash(String body) {
        return DigestUtils.sha1Hex(body);
    }

}
