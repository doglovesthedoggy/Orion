package com.rsorion.api.wrapper;

public class Tile {

	private final int x;
	private final int y;
	private final int plane;

	public Tile(final int x, final int y, final int plane) {
		this.x = x;
		this.y = y;
		this.plane = plane;
	}

	public Tile(final int x, final int y) {
		this(x, y, 0);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int plane() {
		return plane;
	}

	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final Tile tile = (Tile) o;
		return plane == tile.plane && x == tile.x && y == tile.y;
	}

	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		result = 31 * result + plane;
		return result;
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + plane + ")";
	}

}
