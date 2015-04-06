package com.rsorion.client.paint;

import java.awt.Graphics2D;

public abstract class Paintable {

	public abstract void paint(Graphics2D g, int x, int y);

	public boolean equals(final Object obj) {
		return obj instanceof Paintable && getClass().getSimpleName().equals(obj.getClass().getSimpleName());
	}
}