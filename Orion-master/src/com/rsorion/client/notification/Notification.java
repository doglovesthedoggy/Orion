package com.rsorion.client.notification;

import java.awt.TrayIcon;

public final class Notification {

	private final String caption;
	private final String message;
	private final TrayIcon.MessageType messageType;


	public Notification(final String caption, final String message, final TrayIcon.MessageType messageType) {
		this.caption = caption;
		this.message = message;
		this.messageType = messageType;
	}

	public String caption() {
		return caption;
	}

	public String message() {
		return message;
	}

	public TrayIcon.MessageType messageType() {
		return messageType;
	}

}
