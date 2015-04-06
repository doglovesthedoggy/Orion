package com.rsorion.plugin.hiscores.ui;

import com.rsorion.api.util.Predicate;
import com.rsorion.plugin.hiscores.model.Skill;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class SkillsPanel extends JPanel {

	private static Skill[] skills;
	public SkillLabel[] levels;
	public static int virtualOverall = 0;

	public SkillsPanel() {

		this.levels = new SkillLabel[25];
		this.buildLevelLabels();

		final JPanel vSpacer = new JPanel(null);
		setLayout(new GridBagLayout());
		((GridBagLayout) getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
		this.add(vSpacer, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 8, 12), 0, 0));

		for (final Skill.Stat stat : Skill.Stat.values()) {
			if (stat.RS_INDEX != 0 && stat.RS_INDEX != 24) {
				final int INDEX = stat.RS_INDEX - 1;
				final int column = INDEX % 3;
				final int row = (INDEX / 3) + 1;
				add(levels[stat.ordinal()], new GridBagConstraints(column, row, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 8, (column != 2 ? 12 : 0)), 0, 0));
			}
			add(levels[0], new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 8, 12), 0, 0));
			add(levels[24], new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 8, 0), 0, 0));
		}
		setBackground(new Color(23, 23, 23, 80));
	}

	public void buildLevelLabels() {
		for (final Skill.Stat stat : Skill.Stat.values()) {
			levels[stat.RS_INDEX] = new SkillLabel(stat.RS_INDEX);
		}
	}

	public void nullLevels() {
		for (final Skill.Stat stat : Skill.Stat.values()) {
			levels[stat.RS_INDEX].setText(" - ");
			levels[stat.RS_INDEX].setEnabled(false);
			levels[stat.RS_INDEX].removeMouseMotionListener(levels[stat.RS_INDEX].ma);
		}
	}

	public void putStats(final Skill[] skills, final Predicate<Skill> filter) {
		SkillsPanel.skills = skills;
		virtualOverall = 0;
		try {
			for (final Skill skill : skills) {
				levels[skill.stat.ordinal()].apply(skill, filter.accept(skill));
			}
		} catch (NullPointerException npe) {
			nullLevels();
		}
	}

	public SkillLabel getCombatLabel() {
		return levels[levels.length - 1];
	}

	public SkillLabel getOverallLabel() {
		if (SkillLabel.showVirtuals) {
			levels[0].setText(String.format("%,d", virtualOverall));
		}
		return levels[0];
	}

	public static Skill getClosestToLevel() {
		Skill currentLowest = null;
		try {
			for (final Skill skill : skills) {
				if (currentLowest != null) {
					if ((Math.abs(skill.getExpTillNextLevel()) < Math.abs(currentLowest.getExpTillNextLevel()) &&
							skill.LEVEL > 15 && skill.LEVEL < skill.stat.MAX_LEVEL && !SkillLabel.showVirtuals)
							|| (Math.abs(skill.getExpTill(skill.getLevelAtExp(skill.EXP) + 1)) < Math.abs(currentLowest.getExpTill(currentLowest.getLevelAtExp(currentLowest.EXP)))
							&& skill.getLevelAtExp(skill.EXP) < 126 && SkillLabel.showVirtuals)) {
						currentLowest = skill;
					}
				} else {
					currentLowest = skill;
				}
			}
		} catch (Exception ignored) {
		}
		return currentLowest;
	}

}
