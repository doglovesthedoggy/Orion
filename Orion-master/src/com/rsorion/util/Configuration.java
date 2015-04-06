package com.rsorion.util;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

public final class Configuration {

	public final static String CLIENT_NAME = "Orion";
	public final static String CLIENT_VERSION = "v1.0";

	public final static String BUILD = "000007";

	public final static int DEFAULT_WORLD = 1;

	public static final Dimension MINIMUM_NORMAL_MODE = new Dimension(1030, 560);
	public static final Dimension MINIMUM_COMPACT_MODE = new Dimension(765, 557);
	public static final Dimension WIDGET_SIDE_PANEL = new Dimension(265, 0);

	public static final String STORAGE = System.getProperty("user.home") + File.separator + "Orion";

	static {
		try {
			final File f = new File(STORAGE);
			if (!f.exists() && f.mkdirs())
				Debug.println("Orion", "Created storage directory");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
