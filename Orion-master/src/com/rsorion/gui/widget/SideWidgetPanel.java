package com.rsorion.gui.widget;

import com.rsorion.api.util.IoUtils;
import com.rsorion.util.Configuration;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

public class SideWidgetPanel extends WidgetPanel {

	private boolean active = true;

	private final JTabbedPane widgets;

	public SideWidgetPanel(final int location) {
		super(location);
		this.widgets = new JTabbedPane(SwingConstants.RIGHT, JTabbedPane.SCROLL_TAB_LAYOUT);
		add(widgets, BorderLayout.CENTER);
		super.setPreferredSize(Configuration.WIDGET_SIDE_PANEL);
	}

	public void addWidget(final Widget widget) {
		widget.prepare();
		widgets.addTab("", new ImageIcon(IoUtils.loadImage(widget.image)), widget.pane(), widget.description);
	}

}
