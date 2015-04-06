package com.rsorion.api.wrapper;

import com.rsorion.client.rsi.ReflectionEngine;

public class Player extends Actor {

	public Player(final Object instance) {
		super(instance);
	}

	public String name() {
		return (String) ReflectionEngine.getObjectField(instance, "Player", "name");
	}

}
