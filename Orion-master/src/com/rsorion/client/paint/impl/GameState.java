package com.rsorion.client.paint.impl;

import com.rsorion.api.methods.Game;
import com.rsorion.api.methods.Players;
import com.rsorion.api.wrapper.Player;
import com.rsorion.client.paint.Paintable;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Iterator;

public class GameState extends Paintable {
	public void paint(final Graphics2D g, final int x, final int y) {
		String state = "";
		switch (Game.state()) {
			case Game.LOGGED_IN:
				state = "Logged In";
				break;
			case Game.LOGGING_IN:
				state = "Connecting to login server...";
				break;
			case Game.LOGIN_SCREEN:
				state = "Login Screen";
				break;
			default:
				state = "Unknown State: " + Game.state();
				break;
		}
		g.drawString("Game State: " + state, x, y);
	}
}
