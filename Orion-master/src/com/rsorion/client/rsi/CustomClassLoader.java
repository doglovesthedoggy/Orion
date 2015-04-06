package com.rsorion.client.rsi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class CustomClassLoader extends ClassLoader {

	//package access
	final Map<String, byte[]> data = new HashMap<>();

	protected Class<?> findClass(final String name) throws ClassNotFoundException {
		final byte[] d = data.get(name + ".class");
		if (d != null)
			return defineClass(name, d, 0, d.length);
		return super.findClass(name);
	}

	public InputStream getResourceAsStream(final String name) {
		return new ByteArrayInputStream(data.get(name));
	}

}
