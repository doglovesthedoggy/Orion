package com.rsorion.plugin.hiscores.methods;

import com.rsorion.api.util.Browser;
import com.rsorion.plugin.hiscores.model.Skill;
import com.rsorion.util.Debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Skills {

	/**
	 * The base URL of the lite hiscores page.
	 */
	private static final String BASE_URL = "http://services.runescape.com/m=hiscore_oldschool/index_lite.ws?player=";

	/**
	 * Retrieves of the skills of the given name
	 *
	 * @return The skill array containing off of the skills of
	 *         that player.
	 * @throws java.io.IOException There was an issue retrieving the skills.
	 */
	public static Skill[] getAll(final String NAME) throws IOException {
		System.setProperty("http.client", "");
		final List<Skill> skills = new ArrayList<>();
		Debug.println("Browser", "Loading " + BASE_URL + URLEncoder.encode(NAME, "UTF-8"));
		final Browser browser = new Browser(new URL(BASE_URL + URLEncoder.encode(NAME, "UTF-8")));
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				browser.getInputSteam()));
		String line;
		int index = 0;
		while ((line = reader.readLine()) != null) {
			final String[] tokens = line.split(",");
			switch (tokens.length) {
				case 3:
					if (index < 24)
						skills.add(new Skill(index, Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]),
								Long.parseLong(tokens[2])));
					break;
			}
			index++;
		}
		browser.connection().disconnect();
		//Add combat level.

		final int combatLevel = getCombatLevel(skills.get(1).LEVEL, skills.get(3).LEVEL,
				skills.get(2).LEVEL, skills.get(4).LEVEL, skills.get(5).LEVEL, skills.get(6).LEVEL,
				skills.get(7).LEVEL);
		final long combatExp = skills.get(1).EXP + skills.get(2).EXP + skills.get(3).EXP + skills.get(4).EXP +
				skills.get(5).EXP + skills.get(6).EXP + skills.get(7).EXP;
		skills.add(new Skill(skills.size(), -1, combatLevel, combatExp));
		return skills.toArray(new Skill[skills.size()]);
	}

	public static int getCombatLevel(final int attack, final int strength, final int defence, final int constitution,
	                                 final int range, final int prayer, final int magic) {
		return (int) ((defence + constitution + (prayer / 2F) + 1.3F *
				max(attack + strength, 1.5 * magic, 1.5 * range)) / 4F);
	}

	private static double max(final double... numbers) {
		Arrays.sort(numbers);
		return numbers[numbers.length - 1];
	}


}