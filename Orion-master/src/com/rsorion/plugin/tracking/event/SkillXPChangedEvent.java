package com.rsorion.plugin.tracking.event;

import com.rsorion.api.wrapper.Skill;

public class SkillXPChangedEvent {

	private final Skill skill;
	private final String name;
	private final int index;
	private final int xp;
	private final int xpDiff;

	public SkillXPChangedEvent(final Skill skill, final int xp, final int xpDiff) {
		this.skill = skill;
		this.name = skill.name();
		this.index = skill.ordinal();
		this.xp = xp;
		this.xpDiff = xpDiff;
	}


	public Skill skill() {
		return skill;
	}

	public String name() {
		return name;
	}

	public int index() {
		return index;
	}

	public int xp() {
		return xp;
	}

	public int xpDifference() {
		return xpDiff;
	}

}
