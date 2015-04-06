package com.rsorion.api.methods;

import com.rsorion.client.rsi.ReflectionEngine;

public class Game {

	public static int state() {
		return ReflectionEngine.getStaticIntField("client", "state");
	}

	public static boolean isLoggedIn() {
		return state() == LOGGED_IN;
	}

	public static final int LOGIN_SCREEN = 10;
    public static final int LOGGING_IN = 20;
	public static final int LOGGED_IN = 30;


}
