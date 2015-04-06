package com.rsorion.api.wrapper;

import com.rsorion.client.rsi.ReflectionEngine;

public class Actor {

	Object instance;

	public Actor(final Object instance) {
		this.instance = instance;
	}

	public String text() {
		return (String) ReflectionEngine.getObjectField(instance, "Actor", "text");
	}

	public int x() {
		return ReflectionEngine.getStaticIntField("client", "baseX") + (ReflectionEngine.getIntField(instance, "Actor", "regionX") >> 7);
	}

	public int y() {
		return  ReflectionEngine.getStaticIntField("client", "baseY") + (ReflectionEngine.getIntField(instance, "Actor", "regionY") >> 7);
	}

	public Tile location() {
		return new Tile(x(), y(), plane());
	}

	public int plane() {
		return ReflectionEngine.getStaticIntField("client", "plane");
	}


}
