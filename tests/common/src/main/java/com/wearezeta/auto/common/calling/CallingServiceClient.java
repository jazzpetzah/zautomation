package com.wearezeta.auto.common.calling;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.wearezeta.auto.common.log.ZetaLogger;

public class CallingServiceClient {

	private static final String URL_PROTOCOL = "http://";

	// all timeout constants are in milliseconds
	private static final int INSTANCE_STATUS_CHANGE_PULL_FEQUENZY = 2000;
	private static final int INSTANCE_STATUS_CHANGE_TIMEOUT = 60000;
	private static final int HTTP_CONNECT_TIMEOUT = 5000;
	private static final int HTTP_READ_TIMEOUT = 30000;

	private static final Logger log = ZetaLogger
			.getLog(CallingServiceClient.class.getSimpleName());

	private String host;
	private String port;

	public CallingServiceClient(String host, String port) {
		this.host = host;
		this.port = port;
	}

	public String makeCall(String email, String password,
			String conversationId, String backend,
			CallingServiceBackend callBackend) throws Exception {
		JSONObject call = new JSONObject();
		call.put("email", email);
		call.put("password", password);
		call.put("conversationId", conversationId);
		call.put("backend", backend);
		call.put("callBackend", callBackend.getName());

		return request("/api/call", "POST", call).getString("callId");
	}

	public String waitToAcceptCall(String email, String password,
			String backend, CallingServiceBackend callBackend) throws Exception {
		String status = null;

		JSONObject waitingInstance = new JSONObject();
		waitingInstance.put("email", email);
		waitingInstance.put("password", password);
		waitingInstance.put("backend", backend);
		waitingInstance.put("callBackend", callBackend.getName());

		String callId = request("/api/waitingInstance", "POST", waitingInstance)
				.getString("callId");

		long timeout = System.currentTimeMillis()
				+ INSTANCE_STATUS_CHANGE_TIMEOUT;

		while (System.currentTimeMillis() <= timeout) {
			try {
				status = getWaitingInstanceStatus(callId);
				if (status.equals(CallingServiceStatus.Waiting)) {
					return callId;
				}
			} catch (java.net.SocketTimeoutException e) {
				log.warn("Network problem occured:" + e.getMessage());
			}
			Thread.sleep(INSTANCE_STATUS_CHANGE_PULL_FEQUENZY);
		}

		throw new CallingServiceException(
				"Timeout of "
						+ INSTANCE_STATUS_CHANGE_TIMEOUT
						+ " ms was reached for creating the instance for call "
						+ callId
						+ ". Maybe you used the wrong username/password. Last status was: "
						+ status);
	}

	public void stopCall(String callId) throws JSONException, IOException {
		try {
			stopOngoingCall(callId);
		} catch (FileNotFoundException e) {
			log.error("Could not stop call with id " + callId + ": "
					+ e.getMessage());
		}
		try {
			stopWaitingInstance(callId);
		} catch (FileNotFoundException e) {
			log.error("Could not stop call with id " + callId + ": "
					+ e.getMessage());
		}
	}

	public void stopOngoingCall(String callId) throws IOException {
		request("/api/call/" + callId + "/stop", "PUT", new JSONObject());
	}

	public void stopWaitingInstance(String callId) throws IOException {
		request("/api/waitingForInstances/" + callId + "/stop", "PUT",
				new JSONObject());
	}

	public String getCallStatus(String callId) throws Exception {
		return request("/api/call/" + callId + "/status", "GET",
				new JSONObject()).getString("status");
	}

	public String getWaitingInstanceStatus(String callId) throws Exception {
		return request("/api/waitingInstance/" + callId + "/status", "GET",
				new JSONObject()).getString("status");
	}

	private JSONObject request(String path, String requestMethod,
			JSONObject object) throws IOException {
		log.info("Sending object: " + object.toString());
		HttpURLConnection connection = null;
		String urlString = URL_PROTOCOL + this.host + ":" + this.port + path;
		log.info("To: " + urlString);
		URL url = new URL(urlString);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(requestMethod);
		connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
		connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
		connection.setReadTimeout(HTTP_READ_TIMEOUT);
		if (!requestMethod.equals("GET")) {
			connection.setDoOutput(true);
			OutputStreamWriter out = null;
			try {
				out = new OutputStreamWriter(connection.getOutputStream());
				out.write(object.toString());
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}
		Scanner s = null;
		String response = null;
		try {
			s = new Scanner(connection.getInputStream()).useDelimiter("\\A");
			response = s.hasNext() ? s.next() : "";
		} finally {
			if (s != null) {
				s.close();
			}
		}
		log.info("Response: " + response);
		return new JSONObject(response);
	}

}
