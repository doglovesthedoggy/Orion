package com.rsorion.plugin.tracking.event;

public interface SkillChangeListener {

	void onLevelChanged();
	void onXPChanged(SkillXPChangedEvent e);

}
