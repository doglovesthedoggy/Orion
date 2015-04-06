package com.rsorion.client.rsi;

import com.rsorion.util.Debug;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Crawler implements AppletStub {

	/**
	 * The format of the URL for loading OSR worlds.
	 */
	private static final String WORLD_FORMAT = "http://oldschool%d.runescape.com/j1";
	/**
	 * Matches the name of the gamepack jar within the HTML source.
	 */
	private static final Pattern GAMEPACK_PATTERN = Pattern.compile("archive=(.*) ");
	/**
	 * Matches each parameter required to effectively launch the applet.
	 */
	private static final Pattern PARAM_PATTERN = Pattern.compile("<param name=\"([^\\s]+)\"\\s+value=\"([^>]*)\">");

	/**
	 * Stores the params used by the applet stub to load the applet
	 */
	private final Map<String, String> params = new HashMap<>();


	private final int world;                //The world to be loading
	private final String gamepack;          //Name of the gamepack jar

	public Crawler(final int world) throws IOException {
		this.world = world;
		Debug.println("Crawler", "Loading World #3" + world);
		final String page_source = download(String.format(WORLD_FORMAT, world));
		Debug.println("Crawler", "Fetching parameters...");
		findParams(page_source);
		Debug.println("Crawler", "Locating gamepack...");
		gamepack = findGamePack(page_source);
	}

	/**
	 * Fetches the HTML source of a provided URL. The remote host must respond
	 * within five seconds or the connection will time out. Data is then
	 * read into a buffer and a new String object is returned. In order to keep
	 * object creation at a minimum (instead of appending to a String each time).
	 *
	 * @param url The string representation of the URL to download from.
	 * @return The HTML source as a String.
	 * @throws java.io.IOException The provided string was not a valid URL
	 *                             The connection timed out.
	 */
	private String download(final String url) throws IOException {
		final URLConnection uc = new URL(url).openConnection();
		uc.setReadTimeout(5000);
		final DataInputStream di = new DataInputStream(uc.getInputStream());
		byte[] buffer = new byte[uc.getContentLength()];
		di.readFully(buffer);
		di.close();
		return new String(buffer);
	}

	/**
	 * Concatenates the code base and the game pack name to form the URL at which
	 * the gamepack resides.
	 *
	 * @return The URL of the gamepack.
	 */
	public URL getGamePackLocation() {
		try {
			return new URL(getCodeBase() + "/" + gamepack);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Attempts to find the URL to the OSR game pack within the provided HTML source.
	 * The method will return only the first game pack found. This may become an issue
	 * later depending on what attempts Jagex takes against bots.
	 *
	 * @param source The HTML source to search.
	 */
	private String findGamePack(final String source) {
		final Matcher m = GAMEPACK_PATTERN.matcher(source);
		if (m.find()) {
			return m.group(1).trim();
		}
		return null;
	}

	/**
	 * Attempts to use the provided html source and apply the parameter regex
	 * pattern to it. Matches that are not already added will be added to the
	 * parameter hash map. The method will run as long as there are more matches
	 * found
	 *
	 * @param source The HTML source to search.
	 */
	private void findParams(final String source) {
		final Matcher m = PARAM_PATTERN.matcher(source);
		while (m.find()) {
			if (!params.containsKey(m.group(1)))
				params.put(m.group(1), m.group(2));
		}
	}

	/**
	 * Determines if the applet is active. An applet is active just
	 * before its <code>start</code> method is called. It becomes
	 * inactive just before its <code>stop</code> method is called.
	 *
	 * @return <code>true</code> if the applet is active;
	 *         <code>false</code> otherwise.
	 */
	public boolean isActive() {
		return true;
	}

	/**
	 * Gets the URL of the document in which the applet is embedded.
	 * For example, suppose an applet is contained
	 * within the document:
	 * <blockquote><pre>
	 *    http://java.sun.com/products/jdk/1.2/index.html
	 * </pre></blockquote>
	 * The document base is:
	 * <blockquote><pre>
	 *    http://java.sun.com/products/jdk/1.2/index.html
	 * </pre></blockquote>
	 *
	 * @return the {@link java.net.URL} of the document that contains the
	 *         applet.
	 * @see java.applet.AppletStub#getCodeBase()
	 */
	public URL getDocumentBase() {
		try {
			return new URL("http://oldschool" + world + ".runescape.com");
		} catch (final MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the base URL. This is the URL of the directory which contains the applet.
	 *
	 * @return the base {@link java.net.URL} of
	 *         the directory which contains the applet.
	 * @see java.applet.AppletStub#getDocumentBase()
	 */
	public URL getCodeBase() {
		try {
			return new URL("http://oldschool" + world + ".runescape.com");
		} catch (final MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the value of the named parameter in the HTML tag. For
	 * example, if an applet is specified as
	 * <blockquote><pre>
	 * &lt;applet code="Clock" width=50 height=50&gt;
	 * &lt;param name=Color value="blue"&gt;
	 * &lt;/applet&gt;
	 * </pre></blockquote>
	 * <p/>
	 * then a call to <code>getParameter("Color")</code> returns the
	 * value <code>"blue"</code>.
	 *
	 * @param name a parameter name.
	 * @return the value of the named parameter,
	 *         or <tt>null</tt> if not set.
	 */
	public String getParameter(final String name) {
		return params.get(name);
	}

	/**
	 * Returns the applet's context.
	 *
	 * @return the applet's context.
	 */
	public AppletContext getAppletContext() {
		return null;
	}

	/**
	 * Called when the applet wants to be resized.
	 *
	 * @param width  the new requested width for the applet.
	 * @param height the new requested height for the applet.
	 */
	public void appletResize(final int width, final int height) {
	}
}
