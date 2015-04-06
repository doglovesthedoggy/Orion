package com.rsorion.gui.widget;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public abstract class WidgetPanel extends JPanel {

	public static final int SIDE = 0;

	private final int loc;

	public WidgetPanel(final int location) {
		this.loc = location;
		this.setLayout(new BorderLayout());
	}

	public int loc() {
		return loc;
	}

	public abstract void addWidget(final Widget widget);

}
