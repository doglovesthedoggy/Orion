package com.rsorion;

import com.rsorion.api.util.Browser;
import com.rsorion.client.rsi.ClientStore;
import com.rsorion.gui.MainFrame;
import com.rsorion.util.Configuration;
import com.rsorion.util.Debug;
import com.rsorion.util.Settings;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteGlassLookAndFeel;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * Orion is an Oldschool RuneScape client designed to give users the ultimate game
 * experience. It uses reflection (and a little injection) to detect certain conditions
 * and states of the client in order to provide useful information to the user.
 *
 * @author Matt
 */
public final class Orion {

	//The accessors (hooks) for the client.
	private static String accessors = "https://raw.github.com/mahabal/Orion/master/resources/Accessors.json";

	private static MainFrame gui;

	public static void main(final String[] args) throws Exception {
		for (final String arg : args) {
			if (arg.startsWith("accessors=")) {
				accessors = arg.replace("accessors=", "");
				if (!new File(accessors).exists()) {
					System.err.println("Unable to locate accessors file");
					System.exit(-1);
				}
				Debug.println("Entry", "Reading accessors from local file: " + accessors);
			}
		}
		Settings.load();
		final String build = "https://raw.github.com/mahabal/Orion/master/resources/version.txt";
		final String newBin = "https://github.com/mahabal/Orion/tree/master/bin";
		final URL url = new URL(build);
		final Browser browser = new Browser(url);
		Debug.println("Updater", "Checking for new update...");
		final String remoteBuild = browser.source();
		Debug.println("Updater", "Remote build version: " + remoteBuild);
		if (Integer.parseInt(remoteBuild, 16) > Integer.parseInt(Configuration.BUILD, 16)) {
			int answer = JOptionPane.showConfirmDialog(null, "There is a newer version of orion.\nPlease redownload from: http://github.com/mahabal/Orion/bin/Orion.jar", "Update Required!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
			if (answer == 0 && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI(newBin));
				System.exit(0);
			}
		}
		//Quick hack for multi logging, lol
		final File random = new File(System.getProperty("user.home") + File.separator + "random.dat");
		if (random.exists())
			random.setReadOnly();
		Settings.add("defaultworld", 1);
		final ClientStore store = new ClientStore(accessors);
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				JPopupMenu.setDefaultLightWeightPopupEnabled(false);
				JFrame.setDefaultLookAndFeelDecorated(false);
				try {
					UIManager.setLookAndFeel(new SubstanceGraphiteGlassLookAndFeel());
					setUIFont(new FontUIResource("Sans", Font.PLAIN, 12));
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				gui = new MainFrame(store);
			}
		});
	}

	public static void setUIFont (javax.swing.plaf.FontUIResource f){
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put (key, f);
		}
	}


	public static MainFrame gui() {
		return gui;
	}

}