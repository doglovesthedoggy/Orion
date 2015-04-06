package com.rsorion.client.rsi;

import java.lang.reflect.Field;

public final class ReflectionEngine {

	public static Field getField(final String clazz, String fieldName) {
		try {
			final String[] tokens = ClientStore.accessorField(clazz, fieldName).split("\\.");
			if (tokens.length == 2) {
				final Field f = ClientStore.get(tokens[0]).getDeclaredField(tokens[1]);
				if (!f.isAccessible())
					f.setAccessible(true);
				return f;
			}
			throw new NoSuchFieldException();
		} catch (final NoSuchFieldException e) {
			throw new IllegalArgumentException("No such field: " + fieldName + " in " + clazz);
		}
	}

	public static Object getStaticField(final String clazz, String fieldName) {
		try {
			return getField(clazz, fieldName).get(null);
		} catch (final IllegalAccessException e) {
			throw new IllegalArgumentException("No such field: " + fieldName + " in " + clazz);
		}
	}

	public static int getStaticIntField(final String clazz, String fieldName) {
		try {
			return (int)getField(clazz, fieldName).get(null) * ClientStore.accessorMultiplier(clazz, fieldName);
		} catch (final IllegalAccessException e) {
			throw new IllegalArgumentException("No such field: " + fieldName + " in " + clazz);
		}
	}

	public static int getIntField(final Object instance, final String clazz, String fieldName) {
		try {
			return (int)getField(clazz, fieldName).get(instance) * ClientStore.accessorMultiplier(clazz, fieldName);
		} catch (final IllegalAccessException e) {
			throw new IllegalArgumentException("No such field: " + fieldName + " in " + clazz);
		}
	}

	public static Object getObjectField(final Object instance, final String clazz, final String field) {
		try {
			return getField(clazz, field).get(instance);
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
