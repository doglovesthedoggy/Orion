package com.rsorion.client.wrapper;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public abstract class Focus implements FocusListener {

	public void focusGained(final FocusEvent e) {
		_focusGained(e);
	}

	public void focusLost(final FocusEvent e) {
		_focusLost(e);
	}

	public abstract void _focusGained(final FocusEvent e);

	public abstract void _focusLost(final FocusEvent e);


}
