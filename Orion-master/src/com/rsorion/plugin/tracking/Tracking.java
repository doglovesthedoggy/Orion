package com.rsorion.plugin.tracking;

import com.rsorion.api.Manifest;
import com.rsorion.api.methods.Skills;
import com.rsorion.api.wrapper.Skill;
import com.rsorion.gui.widget.Widget;
import com.rsorion.plugin.tracking.event.SkillChangeListener;
import com.rsorion.plugin.tracking.event.SkillXPChangedEvent;
import com.rsorion.plugin.tracking.ui.SkillTrackerPanel;
import com.rsorion.util.Debug;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Manifest(name = "XP Tracker", author = "Matt", description = "Tracks XP gains in real time!", image = "resources/tracking.png")
public final class Tracking extends Widget implements SkillChangeListener {

	private final Map<Integer, Integer> startExps = new HashMap<>();
	private final Map<Integer, Integer> currentExps = new HashMap<>();
	private final Map<Skill, SkillTrackerPanel> panels = new HashMap<>();

	private final JPanel mainPanel;
	private boolean enabled = false;

	private ExecutorService thread = Executors.newSingleThreadExecutor();

	public Tracking() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(new HeaderPanel());
	}

	private void toggle() {
		if (!enabled) {
			enabled = true;
			Debug.println("Tracker", "Beginning XP tracking...");
			final int[] exps = Skills.exps();
			for (int i = 0; i < exps.length; i++) {
				startExps.put(i, exps[i]);
				currentExps.put(i, exps[i]);
				Debug.println("Tracker", "Skill #" + i + " is at " + exps[i]);
			}
			thread.submit(new Runnable() {
				public void run() {
					while (enabled) {
						try {
							final int exps[] = Skills.exps();
							for (int i = 0; i < exps.length; i++) {
								if (!currentExps.get(i).equals(exps[i])) {
									currentExps.put(i, exps[i]);
									onXPChanged(new SkillXPChangedEvent(Skill.values()[i], exps[i], (exps[i] - startExps.get(i))));
								}
							}
							Thread.sleep(200);
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		} else {
			startExps.clear();
			enabled = false;
		}
	}

	public JScrollPane pane() {
		return new JScrollPane(mainPanel);
	}

	public void onLevelChanged() {

	}

	public void onXPChanged(final SkillXPChangedEvent e) {
		if (!panels.containsKey(e.skill())) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					final SkillTrackerPanel panel = new SkillTrackerPanel(e.skill());
					panels.put(e.skill(), panel);
					mainPanel.add(panel);
				}
			});
		}
		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					panels.get(e.skill()).update(e);
				}
			});
	}

	public final class HeaderPanel extends JPanel {


		public HeaderPanel() {
			super.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			final JButton reset = new JButton("Reset");
			reset.setEnabled(false);
			final JButton toggle = new JButton("Enable");
			toggle.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					toggle();
					if (enabled) {
						toggle.setText("Disable");
						reset.setEnabled(true);
					} else {
						toggle.setText("Enable");
						reset.setEnabled(false);
					}
				}
			});
			add(Box.createHorizontalGlue());
			add(toggle);
			add(Box.createHorizontalGlue());
			add(reset);
			add(Box.createHorizontalGlue());
			super.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		}

	}
}
