package com.rsorion.api.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IoUtils {

	public static BufferedImage loadImage(final String path) {
		final File imageFile = new File(path);
		if (imageFile.exists()) {
			try {
				return ImageIO.read(imageFile);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				return ImageIO.read(IoUtils.class.getResourceAsStream("/" + path));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
