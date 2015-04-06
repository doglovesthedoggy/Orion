package com.rsorion.plugin.hiscores;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.rsorion.api.Manifest;
import com.rsorion.api.util.Predicate;
import com.rsorion.gui.widget.Widget;
import com.rsorion.plugin.hiscores.methods.Players;
import com.rsorion.plugin.hiscores.model.Player;
import com.rsorion.plugin.hiscores.model.Skill;
import com.rsorion.plugin.hiscores.ui.Java2sAutoComboBox;
import com.rsorion.plugin.hiscores.ui.Java2sAutoTextField;
import com.rsorion.plugin.hiscores.ui.SkillsPanel;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Manifest(name = "Hiscores", author = "Matt", description = "Hiscores lookup.", image = "resources/hiscores.png")
public class Hiscores extends Widget {

	private JScrollPane scrollPane = null;
	private JPanel panel = null;

	private final ExecutorService thread = Executors.newSingleThreadExecutor();

	public static SkillsPanel skillsPanel;

	private static Player player = null;
	public static Predicate<Skill> filter = null;

	private JPanel detailPanel;
	public static JLabel label1;
	public static JLabel label2;
	public static JLabel label3;
	public static JLabel label4;
	public static JProgressBar progress;

	private void putStats() {
		skillsPanel.putStats(player.SKILLS, filter);
		skillsPanel.getOverallLabel();
	}

	public static void update() {
		skillsPanel.putStats(player.SKILLS, filter);
		skillsPanel.getOverallLabel();
	}

	private Predicate<Skill> createFilter(final String input) {
		final Matcher matcher = Pattern.compile("[<>=\\s]+[\\d]+").matcher(input);
		while (matcher.find()) {
			final String match = matcher.group().trim().replaceAll("[\\s]+", "");
			final String tokens[] = match.split("[<>=]+");
			if (tokens.length >= 2) {
				System.out.println(match);
				final int numericValue = Integer.parseInt(tokens[1]);
				if (match.charAt(0) == '=') {
					return new Predicate<Skill>() {
						@Override
						public boolean accept(Skill skill) {
							return skill.VIRTUAL_LEVEL == numericValue || skill.INDEX == 0 || skill.INDEX == 26;
						}
					};
				} else if (match.charAt(0) == '>') {
					return new Predicate<Skill>() {
						@Override
						public boolean accept(Skill skill) {
							if (match.contains("="))
								return skill.VIRTUAL_LEVEL >= numericValue || skill.INDEX == 0 || skill.INDEX == 26;
							else
								return skill.VIRTUAL_LEVEL > numericValue || skill.INDEX == 0 || skill.INDEX == 26;
						}
					};
				} else if (match.charAt(0) == '<') {
					return new Predicate<Skill>() {
						@Override
						public boolean accept(Skill skill) {
							if (match.contains("="))
								return skill.VIRTUAL_LEVEL <= numericValue || skill.INDEX == 0 || skill.INDEX == 26;
							else
								return skill.VIRTUAL_LEVEL < numericValue || skill.INDEX == 0 || skill.INDEX == 26;
						}
					};
				}
			}
		}
		return new Predicate<Skill>() {
			@Override
			public boolean accept(Skill skill) {
				return true;
			}
		};
	}

	public JScrollPane pane() {

		panel = new JPanel();
		scrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		final JPanel vSpacer1 = new JPanel(null);
		final JPanel hSpacer1 = new JPanel(null);
		final Java2sAutoComboBox inputField = new Java2sAutoComboBox(getPlayersAsStringList());
		inputField.setEditable(true);
		inputField.setStrict(false);
		inputField.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
				inputField.setDataList(getPlayersAsStringList());
			}

			@Override
			public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {

			}

			@Override
			public void popupMenuCanceled(final PopupMenuEvent e) {

			}
		});
		inputField.addKeyListener(new KeyAdapter() {
			public void keyReleased(final KeyEvent e) {
			}
		});
		inputField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == 1001 && e.getActionCommand().equals("comboBoxEdited")) {
					final String name = inputField.getSelectedItem().toString().replace('\ufffd', ' ');
					if (!name.isEmpty()) {
						thread.execute(new Runnable() {
							public void run() {
								String name2 = name;
								filter = createFilter(name2);
								name2 = name2.replaceAll("[<>=]+([\\s]+)?[\\d]+", "");
								try {
									player = Players.getPlayer(name2.trim());
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								putStats();
							}
						});
					}
				}
			}
		});

		final JPanel hspacer = new JPanel(null);
		skillsPanel = new SkillsPanel();
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		progress = new JProgressBar();
		progress.setStringPainted(true);
		progress.setVisible(false);

		detailPanel = new JPanel(new GridBagLayout());
		detailPanel.setBackground(new Color(23, 23, 23, 80));
		((GridBagLayout) detailPanel.getLayout()).columnWeights = new double[]{1.0};
		detailPanel.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(3, 3, 3, 3), 0, 0));
		detailPanel.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 3, 3, 3), 0, 0));
		detailPanel.add(label3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 3, 3, 3), 0, 0));
		detailPanel.add(label4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 3, 3, 3), 0, 0));
		detailPanel.add(progress, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 3, 3, 3), 0, 0));
		detailPanel.setPreferredSize(new Dimension(210, 115));

		//======== this ========

		panel.setLayout(new GridBagLayout());
		((GridBagLayout) panel.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		((GridBagLayout) panel.getLayout()).columnWeights = new double[]{0.0, 1.0, 0.0};
		((GridBagLayout) panel.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
		panel.add(vSpacer1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 3, 3), 0, 0));
		panel.add(hSpacer1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 3, 3), 0, 0));
		panel.add(inputField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 3, 3), 0, 0));
		panel.add(hspacer, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 3, 0), 0, 0));


		final JPanel otherPanel = new JPanel();
		otherPanel.setLayout(new BoxLayout(otherPanel, BoxLayout.Y_AXIS));
		otherPanel.setOpaque(false);


		panel.add(skillsPanel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 3, 3), 0, 0));

		panel.add(detailPanel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 3, 3), 0, 0));


		scrollPane = new JScrollPane(panel);
		return scrollPane;
	}

	public static ArrayList<String> getPlayersAsStringList() {
		try {
			if (com.rsorion.api.methods.Players.loaded() != null) {
				final ArrayList<String> list = new ArrayList<>();
				for (final com.rsorion.api.wrapper.Player p : com.rsorion.api.methods.Players.loaded()) {
					if (p != null) {
						list.add(p.name().replace(' ', ' '));
					}
				}
				return list;
			}
		} catch (final Exception e) {
			return new ArrayList<>();
		}
		return new ArrayList<>();
	}

}
