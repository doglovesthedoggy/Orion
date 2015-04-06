package com.rsorion.api;

public abstract class Plugin {

	public final String name;
	public final String author;
	public final String description;
	public final String image;

	public Plugin() {
		final Manifest manifest = manifest();
		if (manifest != null) {
			this.name = manifest.name();
			this.author = manifest.author();
			this.description = manifest.description();
			this.image = manifest.image();
		} else {
			this.name = "Unknown Plugin";
			this.author = "Unknown Author";
			this.description = "Unknown Description";
			this.image = "Unknown Image";
		}
	}

	private Manifest manifest() {
		if (getClass().isAnnotationPresent(Manifest.class))
			return getClass().getAnnotation(Manifest.class);
		return null;
	}

}
