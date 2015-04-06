package com.rsorion.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class Settings {

	private static final String SETTINGS_FILE = Configuration.STORAGE + File.separator + "settings";

	private static Map<String, Object> settings = new HashMap<>();

	public static void load() {
		try {
			final File file = new File(SETTINGS_FILE);
			if (file.exists()) {
				final BufferedReader reader = new BufferedReader(new FileReader(file));
				String line;
				String jsonString = "";
				while ((line = reader.readLine()) != null)
					jsonString += line;
				final JsonObject object = new JsonParser().parse(jsonString).getAsJsonObject();
				final Iterator<Map.Entry<String, JsonElement>> it = object.entrySet().iterator();
				while (it.hasNext()) {
					final Map.Entry<String, JsonElement> entry = it.next();
					System.out.println(entry.getKey()  + " - " + entry.getValue().getAsString());
					settings.put(entry.getKey(), entry.getValue().getAsString());
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		try {
			final JsonObject gson = new JsonObject();
			for (final String key : settings.keySet()) {
				gson.addProperty(key, settings.get(key).toString());
			}
			final File file = new File(SETTINGS_FILE);
			if (!file.exists()) file.createNewFile();
			final FileWriter writer = new FileWriter(file);
			writer.write(gson.toString());
			writer.flush();
			writer.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void add(final String key, final Object value) {
		settings.put(key, value);
		save();
	}

	public static Object get(final String key) {
		if (settings.containsKey(key))
			return settings.get(key);
		else
			return null;
	}

}
