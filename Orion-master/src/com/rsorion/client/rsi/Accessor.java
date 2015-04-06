package com.rsorion.client.rsi;

public final class Accessor {

	private final String owner;
	private final String name;
	private final String field;
	private final int multiplier;
	private final boolean isStatic;

	public Accessor(final String owner, final String name, final String field) {
		this(owner, name, field, 1, false);
	}

	public Accessor(final String owner, final String name, final String field, final int multiplier, final boolean isStatic) {
		this.owner = owner;
		this.name = name;
		this.field = field;
		this.multiplier = multiplier;
		this.isStatic = isStatic;
	}

	public String owner() {
		return owner;
	}

	public String name() {
		return name;
	}

	public String field() {
		return field;
	}

	public int multiplier() {
		return multiplier;
	}

	public boolean isStatic() {
		return isStatic;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (o instanceof String) {
			if (((String) o).contains(".")) {
				final String[] args = ((String) o).split(".");
				if (args.length == 2 && args[0].equals(owner) && args[1].equals(name))
					return true;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = owner.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + field.hashCode();
		result = 31 * result + multiplier;
		result = 31 * result + (isStatic ? 1 : 0);
		return result;
	}
}
