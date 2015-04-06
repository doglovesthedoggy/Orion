package com.rsorion.gui.widget;

import com.rsorion.api.Plugin;
import com.rsorion.util.Debug;

import javax.swing.JScrollPane;

public abstract class Widget extends Plugin {

	public void prepare() {
		Debug.println("Widget", "Loaded plugin: " + name + " by " + author + " (" + description + ")");
	}

	public abstract JScrollPane pane();

}
