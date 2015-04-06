package com.rsorion.client.paint.impl;

import com.rsorion.api.methods.Game;
import com.rsorion.api.methods.Skills;
import com.rsorion.api.util.IoUtils;
import com.rsorion.api.wrapper.Skill;
import com.rsorion.client.notification.Notification;
import com.rsorion.client.notification.NotificationsManager;
import com.rsorion.client.paint.Paintable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.TrayIcon;

public final class HPPrayerOrbs extends Paintable {

	private long lastNotify = 0;

	public void paint(final Graphics2D g, final int x, final int y) {
		if (Game.isLoggedIn()) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(IoUtils.loadImage("resources/overlays/orbs.png"), 514, 40, null);
			g.setColor(Color.GREEN);
			g.drawString(Skills.level(Skill.Hitpoints) + "", 529, 71);
			g.drawString(Skills.level(Skill.Prayer) + "", 525, 105);
			if (System.currentTimeMillis() - lastNotify > 10000) {
				final double hpPercent = (double)Skills.level(Skill.Hitpoints) / (double)Skills.baseLevel(Skill.Hitpoints);
				final double prayPercent = (double)Skills.level(Skill.Prayer) / (double)Skills.baseLevel(Skill.Prayer);
				if (hpPercent < 0.2) {
					lastNotify = System.currentTimeMillis();
					NotificationsManager.add(new Notification("Orion", "WARNING: Your hitpoints is under 20%!", TrayIcon.MessageType.WARNING));
				} else if (prayPercent < 0.2) {
					lastNotify = System.currentTimeMillis();
					NotificationsManager.add(new Notification("Orion", "WARNING: Your prayer is under 20%!", TrayIcon.MessageType.WARNING));
				}
			}
		}
	}
}
