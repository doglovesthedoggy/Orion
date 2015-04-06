package com.rsorion.client.notification;

import com.rsorion.api.util.IoUtils;
import com.rsorion.util.Configuration;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class NotificationsManager {

	private static final Queue<Notification> notifications = new LinkedList<>();

	private static final ExecutorService thread = Executors.newSingleThreadExecutor();

	private static boolean running;

	public static void add(final Notification notification) {
		notifications.add(notification);
	}

	public static void dispatch() {
		running = true;
		thread.submit(new Runnable() {
			public void run() {
				try {
					TrayIcon icon;
					if (SystemTray.isSupported()) {
						final SystemTray tray = SystemTray.getSystemTray();
						final Image image = IoUtils.loadImage("resources/icon.png");
						icon = new TrayIcon(image, Configuration.CLIENT_NAME);
						tray.add(icon);
						while (running) {
							if (!notifications.isEmpty()) {
								final Notification n = notifications.poll();
								icon.displayMessage(n.caption(), n.message(), n.messageType());
							}
							Thread.sleep(500);
						}
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public static void halt() {
		running = false;
	}


}
