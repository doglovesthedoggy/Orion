package com.rsorion.client.rsi;

import com.google.common.io.ByteStreams;
import com.google.common.io.CountingInputStream;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rsorion.client.wrapper.Mouse;
import com.rsorion.util.Configuration;
import com.rsorion.util.Debug;

import javax.net.ssl.HttpsURLConnection;
import java.applet.Applet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public final class ClientStore {

	private static Applet applet;
	private static CustomClassLoader loader;
	public static Mouse mouse;

	private final String accessorsPath;
	private static JsonObject accessors;

	private double downloaded = 0D;
	private String clazz = "";

	public ClientStore(final String accessorPath) {
		this.accessorsPath = accessorPath;
		if (accessorPath.startsWith("https://")) {
			try {
				final HttpsURLConnection u = (HttpsURLConnection) new URL(accessorsPath).openConnection();
				u.setRequestMethod("GET");
				u.setConnectTimeout(10000);
				u.connect();
				String s;
				String json = "";
				final BufferedReader r = new BufferedReader(new InputStreamReader(u.getInputStream()));
				while ((s = r.readLine()) != null) {
					json += s;
				}
				accessors = new JsonParser().parse(json).getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void load(final URL url) throws IOException {
		Debug.println("ClientStore", "Fetching gamepack.");
		final HttpURLConnection c = (HttpURLConnection) url.openConnection();
		c.setRequestMethod("GET");
		c.setRequestProperty("User-Agent", Configuration.CLIENT_NAME + " Client " + Configuration.CLIENT_VERSION);
		c.setConnectTimeout(30000);
		c.setReadTimeout(30000);
		loader = new CustomClassLoader();
		final int size = c.getContentLength();
		final CountingInputStream cin = new CountingInputStream(c.getInputStream());
		final JarInputStream is = new JarInputStream(cin) {
			public int read(final byte[] b, final int off, final int len) throws IOException {
				downloaded = (double) cin.getCount() / (double) size;
				return super.read(b, off, len);
			}
		};
		Debug.println("ClientStore", "Download completed.");
		JarEntry entry;
		while ((entry = is.getNextJarEntry()) != null) {
			clazz = entry.getName();
			byte[] data = ByteStreams.toByteArray(is);
			if (clazz.endsWith(".class"))
				data = Binder.bind(data);
			loader.data.put(clazz, data);
		}
	}

	public double downloaded() {
		return downloaded;
	}

	public String current() {
		return clazz;
	}

	public Applet createApplet() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		if (applet != null) throw new InstantiationException("Applet already exists...");
		applet = (Applet) loader.findClass("client").newInstance();
		return applet;
	}


	public static Class get(final String name) {
		try {
			Class c = loader.loadClass(name);
			if (c == null) {
				return applet.getClass().getClassLoader().loadClass(name);
			}
			return c;
		} catch (final ClassNotFoundException e) {
			System.err.println(e.toString());
			return null;
		}
	}

	public static String accessorClass(final String accessorName) {
		return accessors.getAsJsonArray(accessorName).get(0).getAsJsonObject().get("class").getAsString();
	}

	public static String accessorField(final String accessorName, final String accessorField) {
		try {
			final String field = accessors.getAsJsonArray(accessorName).get(0).getAsJsonObject()
					.getAsJsonArray(accessorField).get(0).getAsJsonObject().get("field").getAsString();
			if (field.contains("."))
				return field;
			else {
				return accessorClass(accessorName) + "." + field;
			}
		} catch (final NullPointerException e) {
			System.err.println("Unable to load field: " + accessorName + "." + accessorField);
			return null;
		}
	}

	public static int accessorMultiplier(final String accessorName, final String accessorField) {
		try {
			final int multiplier = accessors.getAsJsonArray(accessorName).get(0).getAsJsonObject()
					.getAsJsonArray(accessorField).get(0).getAsJsonObject().get("multiplier").getAsInt();
				return multiplier;

		} catch (final NullPointerException e) {
			System.err.println("Unable to load field: " + accessorName + "." + accessorField);
			return -1;
		}
	}

	public static Applet applet() {
		return applet;
	}

}
