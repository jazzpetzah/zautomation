package com.wearezeta.auto.sync.client;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;

public class ZetaInstance {
	private static final Logger log = ZetaLogger.getLog(ZetaInstance.class
			.getSimpleName());
	// settings
	private boolean isEnabled = false;
	private boolean isSendUsingBackend = false;
	private int messagesToSend = -1;
	private int messagesSendingInterval = 0;

	private Platform platform;

	// state
	private InstanceState state = InstanceState.CREATED;

	private ZetaSender sender;
	private ZetaListener listener;

	private ClientUser userInstance;

	// results
	private long startupTimeMs;
	@SuppressWarnings("unused")
	private long loginAndContactListLoadingTimeMs;
	@SuppressWarnings("unused")
	private long conversationLoadingTimeMs;
	private boolean isOrderCorrect;
	private ArrayList<MessageEntry> messagesListAfterTest;
	private BuildVersionInfo versionInfo;
	private ClientDeviceInfo deviceInfo;

	public ZetaInstance(Platform platform) {
		this.platform = platform;

		switch (platform) {
		case Android:
			try {
				isEnabled = SyncEngineUtil
						.getAndroidClientEnabledFromConfig(ExecutionContext.class);
			} catch (Exception e) {
				isEnabled = true;
				log.warn("Failed to read property android.client.enabled from config file. Set to 'true' by default");
			}

			try {
				isSendUsingBackend = SyncEngineUtil
						.getAndroidBackendSenderFromConfig(this.getClass());
			} catch (Exception e) {
				isSendUsingBackend = false;
				log.warn("Failed to read property android.backend.sender from config file. Set to 'false' by default");

			}
			break;
		case iOS:
			try {
				isEnabled = SyncEngineUtil
						.getIosClientEnabledFromConfig(ExecutionContext.class);
			} catch (Exception e) {
				isEnabled = true;
				log.warn("Failed to read property ios.client.enabled from config file. Set to 'true' by default");
			}

			try {
				isSendUsingBackend = SyncEngineUtil
						.getIosBackendSenderFromConfig(this.getClass());
			} catch (Exception e) {
				isSendUsingBackend = false;
				log.warn("Failed to read property ios.backend.sender from config file. Set to 'false' by default");
			}
			break;
		case Mac:
			try {
				isEnabled = SyncEngineUtil
						.getOSXClientEnabledFromConfig(ExecutionContext.class);
			} catch (Exception e) {
				isEnabled = true;
				log.warn("Failed to read property osx.client.enabled from config file. Set to 'true' by default");
			}

			try {
				isSendUsingBackend = SyncEngineUtil
						.getOSXBackendSenderFromConfig(this.getClass());
			} catch (Exception e) {
				isSendUsingBackend = false;
				log.warn("Failed to read property osx.backend.sender from config file. Set to 'false' by default");
			}
			break;
		default:
			state = InstanceState.ERROR_WRONG_PLATFORM;
		}

		try {
			messagesSendingInterval = SyncEngineUtil
					.getAcceptanceMaxSendingIntervalFromConfig(ExecutionContext.class);

		} catch (Exception e) {
			messagesSendingInterval = 0;
			log.warn("Failed to read property acceptance.max.sending.interval.sec from config file. Set to '0' by default");
		}

		try {
			messagesToSend = SyncEngineUtil
					.getClientMessagesCount(ExecutionContext.class);

		} catch (Exception e) {
			messagesToSend = -1;
			log.warn("Failed to read property acceptance.messages.count from config file. Set to '0' by default");
		}

		if (isEnabled) {
			createSender();
			createListener();
		}
	}

	public void createSender() {
		sender = new ZetaSender(this, messagesToSend);
	}

	public void createListener() {
		listener = new ZetaListener(this);
	}

	public ZetaSender sender() {
		return sender;
	}

	public ZetaListener listener() {
		return listener;
	}

	public InstanceState getState() {
		return state;
	}

	public void setState(InstanceState state) {
		this.state = state;
	}

	public void setIsSendUsingBackend(boolean value) {
		isSendUsingBackend = value;
	}

	public boolean getIsSendUsingBackend() {
		return isSendUsingBackend;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public ClientUser getUserInstance() {
		return userInstance;
	}

	public void setUserInstance(ClientUser userInstance) {
		this.userInstance = userInstance;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public long getStartupTimeMs() {
		return startupTimeMs;
	}

	public void setStartupTimeMs(long startupTimeMs) {
		this.startupTimeMs = startupTimeMs;
	}

	public int getMessagesToSend() {
		return messagesToSend;
	}

	public void setMessagesToSend(int messagesToSend) {
		this.messagesToSend = messagesToSend;
	}

	public int getMessagesSendingInterval() {
		return messagesSendingInterval;
	}

	public void setMessagesSendingInterval(int messagesSendingInterval) {
		this.messagesSendingInterval = messagesSendingInterval;
	}

	public boolean isOrderCorrect() {
		return isOrderCorrect;
	}

	public void setOrderCorrect(boolean isOrderCorrect) {
		this.isOrderCorrect = isOrderCorrect;
	}

	public ArrayList<MessageEntry> getMessagesListAfterTest() {
		return messagesListAfterTest;
	}

	public void setMessagesListAfterTest(
			ArrayList<MessageEntry> messagesListAfterTest) {
		this.messagesListAfterTest = messagesListAfterTest;
	}

	public BuildVersionInfo getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(BuildVersionInfo versionInfo) {
		this.versionInfo = versionInfo;
	}

	public ClientDeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(ClientDeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
}
