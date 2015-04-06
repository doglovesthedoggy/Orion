package com.rsorion.plugin.hiscores.model;

import com.rsorion.plugin.hiscores.Hiscores;
import com.rsorion.plugin.hiscores.methods.Skills;

import javax.swing.SwingUtilities;
import java.io.IOException;

public class Player {

	public final String NAME;
	public Skill[] SKILLS;

	public Player(final String name) {
		this.NAME = name;

		Hiscores.label1.setText("");
		Hiscores.label2.setText("");
		Hiscores.label3.setText("");
		Hiscores.label4.setText("");
		Hiscores.progress.setVisible(false);
		Hiscores.skillsPanel.nullLevels();

		try {
			Player.this.SKILLS = Skills.getAll(name);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
