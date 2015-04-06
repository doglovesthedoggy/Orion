package com.rsorion.gui;

import com.rsorion.client.notification.NotificationsManager;
import com.rsorion.client.paint.PaintManager;
import com.rsorion.client.paint.Paintable;
import com.rsorion.client.paint.impl.CameraInfo;
import com.rsorion.client.paint.impl.GameState;
import com.rsorion.client.paint.impl.HPPrayerOrbs;
import com.rsorion.client.paint.impl.MyPlayerInfo;
import com.rsorion.client.rsi.ClientStore;
import com.rsorion.gui.widget.SideWidgetPanel;
import com.rsorion.gui.widget.WidgetPanel;
import com.rsorion.plugin.hiscores.Hiscores;
import com.rsorion.plugin.screenshot.ScreenShot;
import com.rsorion.plugin.screenshot.ScreenShotViewer;
import com.rsorion.plugin.tracking.Tracking;
import com.rsorion.util.Configuration;
import com.rsorion.util.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class MainFrame extends JFrame {

	private final SideWidgetPanel east;

	public MainFrame(final ClientStore store) {

		super(Configuration.CLIENT_NAME + " " + Configuration.CLIENT_VERSION);
		NotificationsManager.dispatch();

		final Container c = getContentPane();
		c.setLayout(new GridBagLayout());
		((GridBagLayout)c.getLayout()).columnWeights = new double[] {1.0, 0.0};
		((GridBagLayout)c.getLayout()).rowWeights = new double[] {1.0};
		c.setBackground(Color.BLACK);

		final AppletPanel appletPanel = new AppletPanel(store);
		appletPanel.reload();
		c.add(appletPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE,	new Insets(0, 0, 0, 0), 0, 0));

		east = new SideWidgetPanel(WidgetPanel.SIDE);
		east.addWidget(new Hiscores());
		east.addWidget(new Tracking());
		east.addWidget(new ScreenShotViewer());
		c.add(east, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

		final JMenuBar menu = new JMenuBar();
		menu.add(new ViewMenu());
		menu.add(new OverlayMenu());

		menu.add(new UtilitiesMenu());
		setJMenuBar(menu);

		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(final WindowEvent e) {
				pack();
			}
		});


		//Window options
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		if (Boolean.parseBoolean(Settings.get("compactMode").toString()))
			setMinimumSize(Configuration.MINIMUM_COMPACT_MODE);
		else
			setMinimumSize(Configuration.MINIMUM_NORMAL_MODE);
		pack();
		setVisible(true);

	}


	private class OverlayMenu extends JMenu {

		private final Class[] classes = new Class[]{
				CameraInfo.class,
				GameState.class,
				MyPlayerInfo.class,
				HPPrayerOrbs.class
		};

		public OverlayMenu() {
			setText("Overlays");
			for (final Class clazz : classes) {
				final JMenuItem mi = new JCheckBoxMenuItem(clazz.getSimpleName(), false);
				mi.addItemListener(new ItemListener() {
					public void itemStateChanged(final ItemEvent e) {
						try {
							if (mi.isSelected()) {
								PaintManager.add((Paintable) clazz.newInstance());
							} else {
								PaintManager.remove((Paintable) clazz.newInstance());
							}
							Settings.add(clazz.getSimpleName().toLowerCase(), mi.isSelected());
						} catch (InstantiationException | IllegalAccessException e1) {
							e1.printStackTrace();
						}
					}
				});
				if (Settings.get(clazz.getSimpleName().toLowerCase()) == null) {
					Settings.add(clazz.getSimpleName().toLowerCase(), false);
				}
				mi.setSelected(Boolean.parseBoolean(Settings.get(clazz.getSimpleName().toLowerCase()).toString()));
				add(mi);
			}
		}
	}

	private class ViewMenu extends JMenu {
		public ViewMenu() {
			setText("View");
			final JMenuItem compactMode = new JCheckBoxMenuItem("Compact Mode", false);
			compactMode.addItemListener(new ItemListener() {
				public void itemStateChanged(final ItemEvent e) {
					if (compactMode.isSelected()) {
						MainFrame.this.getContentPane().remove(east);
						MainFrame.this.setMinimumSize(Configuration.MINIMUM_COMPACT_MODE);
					} else {
						MainFrame.this.getContentPane().add(east, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
								GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
						MainFrame.this.setMinimumSize(Configuration.MINIMUM_NORMAL_MODE);
					}
					Settings.add("compactMode", compactMode.isSelected());
					pack();
				}
			});
			if (Settings.get("compactMode") == null) {
				Settings.add("compactMode", false);
			}
			compactMode.setSelected(Boolean.parseBoolean(Settings.get("compactMode").toString()));
			add(compactMode);
		}
	}

	private class UtilitiesMenu extends JMenu {
		public UtilitiesMenu() {
			setText("Utilities");
			final JMenuItem screenShot = new JMenuItem("Screenshot");
			screenShot.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					final ExecutorService thread = Executors.newSingleThreadExecutor();
					thread.submit(new ScreenShot());
				}
			});
			add(screenShot);
		}
	}

}
