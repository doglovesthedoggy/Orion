package com.rsorion.client.wrapper;

import com.rsorion.client.paint.PaintManager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Canvas extends java.awt.Canvas {

	public BufferedImage gameBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
	public BufferedImage clientBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);

	public Graphics getGraphics() {
		final Graphics g = clientBuffer.getGraphics();
		g.drawImage(gameBuffer, 0, 0, null);
		g.dispose();
		final Graphics2D g2 = (Graphics2D) super.getGraphics();
		PaintManager.paint((Graphics2D) clientBuffer.getGraphics());
		g2.drawImage(clientBuffer, 0, 0, null);
		return gameBuffer.getGraphics();
	}


}
