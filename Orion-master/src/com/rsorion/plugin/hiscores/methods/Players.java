package com.rsorion.plugin.hiscores.methods;

import com.rsorion.plugin.hiscores.model.Player;

import java.io.IOException;

public class Players {

	public static Player getPlayer(final String name) throws IOException {
		return new Player(name);
	}


}