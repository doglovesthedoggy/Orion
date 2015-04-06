package com.rsorion.plugin.hiscores.model;

public class Skill {

	/**
	 * Stores the index of the skill in the array
	 */
	public final int INDEX;

	/**
	 * Stores the name of the skill corresponding to the given index
	 */
	public final String NAME;

	/**
	 * Stores the exp of the skill corresponding to the given index
	 */
	public final long EXP;

	/**
	 * Stores the level of the skill corresponding to the given index.
	 */
	public final int LEVEL;

	/**
	 * Stores the rank of the skill corresponding to the given index.
	 */
	public final int RANK;

	/**
	 * Stores the calculated value of the virtual level at the current EXP
	 */
	public final int VIRTUAL_LEVEL;

	public final Stat stat;

	/**
	 * The constructor for the Skill class.
	 *
	 * @param index The index of which it occurs in the Skill array
	 * @param rank  The rank in the skill
	 * @param level The level in the skill
	 * @param exp   The exp in the skill
	 */
	public Skill(final int index, final int rank, final int level, final long exp) {
		this.stat = Stat.values()[index];
		this.INDEX = index;
		this.NAME = this.stat.name();
		this.EXP = exp;
		this.LEVEL = level;
		this.VIRTUAL_LEVEL = getLevelAtExp(EXP);
		this.RANK = rank;
	}

	private double getExpAtLevel(final double level) {
		double exp = 0;
		for (int i = 1; i < level; i++) {
			exp += Math.floor(i + 300.0d * Math.pow(2, (i / 7.0d)));
		}
		return Math.floor(exp / 4.0d);
	}

	public int getLevelAtExp(final double expCap) {
		int i = 1;
		while (getExpAtLevel(i) < expCap) {
			i++;
		}
		return --i;
	}

	private double getExpAtLevel() {
		return getExpAtLevel(LEVEL);
	}

	private double getExpBetween(final int start, final int end) {
		return (getExpAtLevel(end) - getExpAtLevel(start));
	}

	public double getPercentageTill(final int level) {
		return ((EXP - getExpAtLevel()) / getExpBetween(level, level + 1)) * 100;
	}

	public double getExpTill(final int level) {
		return getExpAtLevel(level) - EXP;
	}

	public double getExpTillNextLevel() {
		return getExpTill(LEVEL + 1);
	}

	@Override
	public String toString() {
		return String.format("%-20s %-10s %-5s %-10s", NAME, RANK, LEVEL, EXP);
	}


	/**
	 * Simple enum that stores the index of each skill, along with the index that
	 * they should appear in the widget... along with the max level in that stat.
	 */
	public enum Stat {
		Overall(0, 2496),
		Attack(1),
		Defence(7),
		Strength(4),
		Constitution(2),
		Ranging(10),
		Prayer(13),
		Magic(16),
		Cooking(12),
		Woodcutting(18),
		Fletching(17),
		Fishing(9),
		Firemaking(15),
		Crafting(14),
		Smithing(6),
		Mining(3),
		Herblore(8),
		Agility(5),
		Thieving(11),
		Slayer(20),
		Farming(21),
		Runecrafting(19),
		Hunter(23),
		Construction(22),
		Combat(24);

		/**
		 * The index of which this stat is displayed in the RuneScape skills tab.
		 */
		public final int RS_INDEX;

		/**
		 * The maximum level achievable in the skill (99 is default)
		 */
		public final int MAX_LEVEL;


		/**
		 * Adds a Stat enum with the given RuneScape index, and automatically sets the
		 * max level to 99.
		 *
		 * @param rsIndex The index at which it appears in the RuneScape skills tab.
		 */
		private Stat(final int rsIndex) {
			this(rsIndex, 99);
		}

		/**
		 * Adds a Stat enum with the given RuneScape index, and max level.
		 *
		 * @param rsIndex  The index at which it appears in the RuneScape skills tab.
		 * @param maxLevel The maximum level achievable in the skill.
		 */
		private Stat(final int rsIndex, final int maxLevel) {
			this.RS_INDEX = rsIndex;
			this.MAX_LEVEL = maxLevel;
		}

	}


}
