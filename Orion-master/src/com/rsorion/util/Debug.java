package com.rsorion.util;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * A shitty quick Debug class for a formatted output.
 */
public final class Debug {

	//The format for the timestamp.
	private static final Format time = new SimpleDateFormat("[hh:mm:ss]");

	//Used so it does not print the class every time.
	private static String lastClass = "";

	public static void println(final String from, final String message) {
		System.out.printf("%s: %15s | %s%n", time.format(System.currentTimeMillis()),
				(from.equals(lastClass) ? "" : from), message);
		if (!from.equals(lastClass))
			lastClass = from;
	}

}
