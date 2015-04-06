package com.rsorion.gui;

import com.rsorion.client.rsi.ClientStore;
import com.rsorion.client.rsi.Crawler;
import com.rsorion.util.Configuration;
import com.rsorion.util.Debug;

import javax.swing.JPanel;
import java.applet.Applet;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The panel that contains the applet. Crawling, class identification, as well as
 * applet initialization are all dispatched from within this class.
 */
public final class AppletPanel extends JPanel {

	enum State {
		SPLASH,
		CONFIG,
		DOWNLOAD,
		IDENTIFY,
		INITIALIZE,
		ERROR,
		OSR
	}

	private static final ExecutorService pool = Executors.newFixedThreadPool(1);

	private final ClientStore store;
	private State state = State.SPLASH;
	private volatile Crawler crawler;
	private volatile Applet applet = null;
	private volatile String error;

	public AppletPanel(final ClientStore store) {
		super(new BorderLayout());
		this.store = store;
		this.setBackground(Color.BLACK);
		this.setMaximumSize(new Dimension(765, 503));
		this.setPreferredSize(new Dimension(765, 503));
		this.setSize(765, 503);
		revalidate();
	}

	public void reload() {
		synchronized (this) {
			if (state == State.CONFIG || state == State.DOWNLOAD || state == State.IDENTIFY) return;
			state = State.CONFIG;
		}
		if (applet != null) {
			remove(applet);
			applet.destroy();
			System.gc();
			applet = null;
			error = null;
		}
		pool.submit(new Runnable() {
			public void run() {
				try {
					crawler = new Crawler(Configuration.DEFAULT_WORLD);
				} catch (IOException e) {
					e.printStackTrace();
					error(e.toString());
					return;
				}
				state = State.DOWNLOAD;
				try {
					Debug.println("AppletPanel", "Downloading...");
					store.load(crawler.getGamePackLocation());
				} catch (Exception e) {
					e.printStackTrace();
					error(e.toString());
					return;
				}
				state = State.INITIALIZE;
				try {
					Debug.println("AppletPanel", "Initializing applet...");
					applet = store.createApplet();
				} catch (Exception e) {
					e.printStackTrace();
					error(e.toString());
					return;
				}
				applet.setStub(crawler);
				applet.init();
				applet.start();
				add(applet, BorderLayout.CENTER);
				state = State.OSR;
				Debug.println("AppletPanel", "Load successful. Welcome to OSR.");
				revalidate();
			}
		});
		new Thread(new Runnable() {
			public void run() {
				while (state != State.OSR && state != State.ERROR) {
					repaint();
					try {
						Thread.sleep(10);
					} catch (InterruptedException ex) {
						break;
					}
				}
				repaint();
			}
		}).start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		switch (state) {
			case CONFIG: {
				String message = "Loading configuration...";
				FontMetrics fm = g.getFontMetrics();
				g.setColor(Color.CYAN);
				g.drawString(message, getWidth() / 2 - fm.stringWidth(message) / 2, 25);
				break;
			}
			case DOWNLOAD: {
				String message = "Processing... " + store.current();
				FontMetrics fm = g.getFontMetrics();
				g.setColor(Color.CYAN);
				g.drawString(message, getWidth() / 2 - fm.stringWidth(message) / 2, 175);
				g.setColor(Color.CYAN);
				g.drawRoundRect(getWidth() / 2 - 100, 200, 200, 50, 10, 10);
				g.fillRoundRect(getWidth() / 2 - 100, 200, (int) (store.downloaded() * 200), 50, 10, 10);
				g.setColor(new Color(255, 255, 255, 50));
				g.fillRoundRect(getWidth() / 2 - 100, 200, 200, 35, 10, 10);
				break;
			}
			case IDENTIFY: {
				String message = "Identifying classes...";
				FontMetrics fm = g.getFontMetrics();
				g.setColor(Color.CYAN);
				g.drawString(message, getWidth() / 2 - fm.stringWidth(message) / 2, 25);
				g.setColor(Color.CYAN);
				break;
			}
			case INITIALIZE: {
				String message = "Initializing...";
				FontMetrics fm = g.getFontMetrics();
				g.setColor(Color.CYAN);
				g.drawString(message, getWidth() / 2 - fm.stringWidth(message) / 2, 25);
				break;
			}
			case ERROR: {
				FontMetrics fm = g.getFontMetrics();
				g.drawString(error, getWidth() / 2 - fm.stringWidth(error) / 2, 50);
				break;
			}
		}
	}

	private synchronized void error(String message) {
		state = State.ERROR;
		this.error = message;
	}

	public void setSize(final Dimension dimension) {
		super.setSize(dimension);
		if (applet != null) {
			applet.setSize(dimension);
		}
	}

	public void setPreferredSize(final Dimension dimension) {
		super.setPreferredSize(dimension);
		if (applet != null) {
			applet.setPreferredSize(dimension);
		}
	}

}
