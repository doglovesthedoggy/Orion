package com.rsorion.client.paint.impl;

import com.rsorion.api.methods.Camera;
import com.rsorion.api.methods.Game;
import com.rsorion.api.methods.Players;
import com.rsorion.api.wrapper.Player;
import com.rsorion.client.paint.Paintable;

import java.awt.Graphics2D;

public class CameraInfo extends Paintable {
	public void paint(final Graphics2D g, final int x, final int y) {
		if (Game.isLoggedIn()) {
			g.drawString("Camera: (" + Camera.x() + ", " + Camera.y() + ", " + Camera.z() + ") (pitch: " + Camera.pitch() + ", yaw: " + Camera.yaw() + ") Compass: " + Camera.compassAngle(), x, y);
		}
	}
}
