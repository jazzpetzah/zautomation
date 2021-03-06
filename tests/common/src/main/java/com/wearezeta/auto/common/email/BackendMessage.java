package com.wearezeta.auto.common.email;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

public class BackendMessage {
	private String content;
	private Map<String, String> mapHeaders = new HashMap<String, String>();

	public BackendMessage(String rawMsg) throws Exception {
		final Message msg = MessagingUtils.stringToMsg(rawMsg);

		@SuppressWarnings("unchecked")
		final Enumeration<Header> hdrs = msg.getAllHeaders();
		while (hdrs.hasMoreElements()) {
			final Header hdr = hdrs.nextElement();
			mapHeaders.put(hdr.getName(), hdr.getValue());
		}

		final Object msgContent = msg.getContent();
		if (msgContent instanceof Multipart) {
			final Multipart multipart = (Multipart) msgContent;
			final StringBuilder multipartContent = new StringBuilder();
			for (int j = 0; j < multipart.getCount(); j++) {
				final BodyPart bodyPart = multipart.getBodyPart(j);
				if (bodyPart.getDisposition() == null) {
					multipartContent.append(getText(bodyPart));
				}
			}
			this.content = multipartContent.toString();
		} else {
			this.content = msgContent.toString();
		}
	}

	public final String getHeaderValue(String headerName)
			throws MessagingException {
		return this.mapHeaders.get(headerName);
	}

	public String getContent() {
		return this.content;
	}

	/**
	 * http://www.oracle.com/technetwork/java/javamail/faq/index.html#mainbody
	 * 
	 * @param p
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			p.isMimeType("text/html");
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

	public String getDeliveredToEmail() throws MessagingException {
		return this.getHeaderValue(MessagingUtils.DELIVERED_TO_HEADER);
	}

	private static final String SUBJECT_HEADER_NAME = "Subject";

	public String getMailSubject() throws MessagingException {
		return this.getHeaderValue(SUBJECT_HEADER_NAME);
	}
}
