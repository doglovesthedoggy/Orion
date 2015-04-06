package com.rsorion.api.methods;

import com.rsorion.api.wrapper.Skill;
import com.rsorion.client.rsi.ReflectionEngine;

public class Skills {

	public static int exp(final int ordinal) {
		return exps()[ordinal];
	}

	public static int exp(final Skill skill) {
		return exp(skill.ordinal());
	}

	public static int level(final int ordinal) {
		return levels()[ordinal];
	}

	public static int level(final Skill skill) {
		return level(skill.ordinal());
	}

	public static int baseLevel(final int ordinal) {
		return baseLevels()[ordinal];
	}

	public static int baseLevel(final Skill skill) {
		return baseLevel(skill.ordinal());
	}

	public static int[] levels() {
		return (int[]) ReflectionEngine.getStaticField("client", "skillLevelArray");
	}

	public static int[] exps() {
		return (int[]) ReflectionEngine.getStaticField("client", "skillExpArray");
	}

	public static int[] baseLevels() {
		return (int[]) ReflectionEngine.getStaticField("client", "skillBaseLevelArray");
	}

}
