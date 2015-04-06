package com.rsorion.plugin.hiscores.ui;

import com.rsorion.api.util.IoUtils;
import com.rsorion.plugin.hiscores.Hiscores;
import com.rsorion.plugin.hiscores.model.Skill;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SkillLabel extends JLabel {

	static boolean showVirtuals = false;

	private int targetLevel = 0;

	public SkillLabel(final String text, final Image image) {
		super(text, new ImageIcon(image), CENTER);
	}

	public SkillLabel(final int i) {
		this(" - ", IoUtils.loadImage("resources/hiscores/" + i + ".png"));
		this.setOpaque(false);
		this.setEnabled(false);
		switch (i) {
			case 0:
				this.setPreferredSize(new Dimension(75, 31));
				break;
			case 24:
				this.setPreferredSize(new Dimension(65, 32));
				break;
			default:
				this.setPreferredSize(new Dimension(53, 24));
				break;
		}
	}

	public MouseAdapter ma;

	public void apply(final Skill skill, final boolean enabled) {
		if (showVirtuals) {
			final int virtualLevel = skill.getLevelAtExp(skill.EXP);
			this.targetLevel = (((virtualLevel + 1) > 126) ? virtualLevel : virtualLevel + 1);
		} else {
			this.targetLevel = (((skill.LEVEL + 1) > skill.stat.MAX_LEVEL) ? skill.LEVEL : skill.LEVEL + 1);
		}
		if (showVirtuals && (skill.stat.ordinal() != 0 && skill.stat.ordinal() != 24)) {
			this.setText(String.format("%,d", skill.getLevelAtExp(skill.EXP)));
			SkillsPanel.virtualOverall += skill.getLevelAtExp(skill.EXP);
		} else {
			this.setText(String.format("%,d", skill.LEVEL));
		}
		this.setEnabled(enabled);
		ma = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Hiscores.label1.setText("Skill: " + skill.NAME);
				if (skill.RANK > -1) {
					Hiscores.label2.setVisible(true);
					Hiscores.label2.setText(String.format("Rank: %,d", skill.RANK));
				} else
					Hiscores.label2.setVisible(false);
				Hiscores.label3.setText(String.format("Experience: %,d", skill.EXP));
				if (skill.LEVEL >= targetLevel && skill.stat.ordinal() != 0) {
					Hiscores.label4.setVisible(false);
					Hiscores.progress.setVisible(false);
					return;
				} else if (skill.stat.ordinal() == 0 && (skill.LEVEL < 2496 || showVirtuals && SkillsPanel.virtualOverall < 3150)) {
					final Skill closest = SkillsPanel.getClosestToLevel();
					Hiscores.label4.setText(String.format("Closest level: %s (%,.0f)",
							(closest.NAME.equalsIgnoreCase("dungeoneering") ? "Dung" : closest.NAME),
							closest.getExpTillNextLevel()));
					Hiscores.progress.setValue((int) closest.getPercentageTill(closest.getLevelAtExp(closest.EXP) + 1));
					Hiscores.progress.setString(Hiscores.progress.getValue() + "%" + " to level " +
							(closest.LEVEL + 1));
				} else {
					Hiscores.label4.setText(String.format("Exp to level (%d): %,.0f", targetLevel,
							skill.getExpTill(targetLevel)));
					Hiscores.progress.setValue((int) skill.getPercentageTill(targetLevel));
					Hiscores.progress.setString(null);
				}

				Hiscores.label4.setVisible(true);
				Hiscores.progress.setVisible(true);
			}
		};
		this.addMouseMotionListener(ma);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					final JPopupMenu menu = new JPopupMenu();
					final JMenuItem title = new JMenuItem(skill.NAME, getIcon());
					title.setEnabled(false);
					menu.add(title);
					if (skill.stat.ordinal() != 0) {
						final JMenuItem setTarget = new JMenuItem("Set target");
						setTarget.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								try {
									final int localLevel = Integer.parseInt(JOptionPane.showInputDialog(SkillLabel.this,
											"Please enter new " + skill.NAME + "  target level:", "Set new " + skill.NAME +
											" target", JOptionPane.INFORMATION_MESSAGE));
									if (localLevel > Integer.parseInt(SkillLabel.this.getText().replace(",", "")) && localLevel <= 126) {
										targetLevel = localLevel;
									} else {
										JOptionPane.showConfirmDialog(SkillLabel.this, String.format("%d is not a valid level.%sThe level must be greater than %s and less than %d.",
												localLevel, System.getProperty("line.separator"), SkillLabel.this.getText(), 126), "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
									}

								} catch (NumberFormatException ignored) {
								}
							}
						});
						menu.add(setTarget);
					}
					final JCheckBoxMenuItem showVirtuals = new JCheckBoxMenuItem("Display virtual levels?", false);
					showVirtuals.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							showVirtuals.setState(!showVirtuals.getState());
							SkillLabel.showVirtuals = !SkillLabel.showVirtuals;
							Hiscores.update();
						}
					});
					menu.add(new JMenuItem());
					menu.add(showVirtuals);
					menu.show(SkillLabel.this, e.getX(), e.getY());
				}
			}
		});
	}

	public void apply(final String text, final boolean enabled) {
		this.setText(text);
		this.setEnabled(enabled);
	}


}