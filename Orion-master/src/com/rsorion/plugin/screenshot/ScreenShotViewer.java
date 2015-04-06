package com.rsorion.plugin.screenshot;

import com.rsorion.api.Manifest;
import com.rsorion.gui.widget.Widget;

import javax.swing.JScrollPane;

@Manifest(name = "ScreenShotViewer Viewer", description = "View / Upload previous screenshots!", image = "resources/screenshot.png")
public final class ScreenShotViewer extends Widget {
	public JScrollPane pane() {
		return new JScrollPane();
	}
}
