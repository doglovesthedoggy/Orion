package com.rsorion.client.paint.impl;

import com.rsorion.api.methods.Game;
import com.rsorion.api.methods.Players;
import com.rsorion.api.wrapper.Player;
import com.rsorion.client.paint.Paintable;

import java.awt.Graphics2D;

public class MyPlayerInfo extends Paintable {
	public void paint(final Graphics2D g, final int x, final int y) {
		if (Game.isLoggedIn()) {
			final Player me = Players.me();
			g.drawString("Me: " + me.name() + " " + me.location(), x, y);
		}
	}
}
