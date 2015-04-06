package com.rsorion.api.methods;

import com.rsorion.client.rsi.ReflectionEngine;

public class Camera {

	public static int x() {
		return ReflectionEngine.getStaticIntField("client", "cameraX");
	}

	public static int y() {
		return ReflectionEngine.getStaticIntField("client", "cameraY");
	}

	public static int z() {
		return ReflectionEngine.getStaticIntField("client", "cameraZ");
	}

	public static int pitch() {
		return (int) ((ReflectionEngine.getStaticIntField("client", "cameraPitch") - 1024) / 20.48);
	}

	public static int yaw() {
		double yaw = ReflectionEngine.getStaticIntField("client", "cameraYaw");
		yaw /= 2048;
		yaw *= 360;
		return (int)yaw;
	}

	public static int compassAngle() {
		return ReflectionEngine.getStaticIntField("client", "compassAngle");
	}

}
