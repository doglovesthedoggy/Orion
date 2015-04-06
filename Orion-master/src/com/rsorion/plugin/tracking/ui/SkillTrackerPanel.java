package com.rsorion.plugin.tracking.ui;

import com.rsorion.api.wrapper.Skill;
import com.rsorion.plugin.tracking.event.SkillXPChangedEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;

public final class SkillTrackerPanel extends JPanel {
	private final JLabel xp = new JLabel();
	private final JLabel diff = new JLabel();

	public SkillTrackerPanel(final Skill skill) {

		setBackground(new Color(23, 23, 23, 80));

		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(new JLabel(skill.name()));

		final JPanel xpPanel = new JPanel();
		xpPanel.setLayout(new BoxLayout(xpPanel, BoxLayout.X_AXIS));
		xpPanel.add(xp);
		xpPanel.add(Box.createHorizontalGlue());
		xpPanel.add(diff);

		super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(topPanel);
		add(xpPanel);

	}

	public void update(final SkillXPChangedEvent e) {
		this.xp.setText(String.format("XP: %,d", e.xp()));
		this.diff.setText(String.format("(+%,d)", e.xpDifference()));
	}

}
