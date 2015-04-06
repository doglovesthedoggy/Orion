package com.rsorion.client.paint;

import java.awt.Graphics2D;
import java.util.LinkedList;

public final class PaintManager {
	public static LinkedList<Paintable> paintables = new LinkedList<>();
	private static Paintable debug;

	public static void add(Paintable paint) {
		paintables.add(paint);
	}

	public static void setDebug(final Paintable paintable) {
		debug = paintable;
	}

	public static void clearDebug() {
		debug = null;
	}

	public static void paint(final Graphics2D g) {
		int x = 10;
		int y = 35;
		for (final Paintable p : paintables) {
			p.paint(g, x, y += 15);
		}
	}

	public static void remove(Paintable paint) {
		paintables.remove(paint);
	}


}
