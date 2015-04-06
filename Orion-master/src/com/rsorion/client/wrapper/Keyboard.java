package com.rsorion.client.wrapper;

import com.rsorion.client.rsi.ClientStore;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class Keyboard extends Focus implements KeyListener {

	//public KeyEvent(Component source, int id, long when, int modifiers,
	//              int keyCode, char keyChar, int keyLocation) {
	public void keyPressed(KeyEvent e) {
		_keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		_keyReleased(e);
	}

	public void keyTyped(final KeyEvent e) {
		_keyTyped(e);
	}

	public abstract void _keyPressed(final KeyEvent e);

	public abstract void _keyReleased(final KeyEvent e);

	public abstract void _keyTyped(final KeyEvent e);

}
