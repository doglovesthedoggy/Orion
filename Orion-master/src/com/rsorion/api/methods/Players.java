package com.rsorion.api.methods;

import com.rsorion.api.wrapper.Player;
import com.rsorion.client.rsi.ReflectionEngine;

import java.util.ArrayList;
import java.util.Collection;

public class Players {

	public static Collection<Player> loaded() {
		final ArrayList<Player> players = new ArrayList<>();
		final Object[] list = (Object[]) ReflectionEngine.getStaticField("client", "players");
		for (final Object playerInst : list) {
			if (playerInst != null) players.add(new Player(playerInst));
		}
		return players;
	}

	public static Player me() {
		return new Player(ReflectionEngine.getStaticField("client", "player"));
	}


}
