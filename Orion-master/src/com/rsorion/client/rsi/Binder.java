package com.rsorion.client.rsi;

import com.rsorion.client.wrapper.Canvas;
import com.rsorion.client.wrapper.Keyboard;
import com.rsorion.util.Debug;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public final class Binder {

	public static byte[] bind(final byte[] data) {
		byte[] temp = subclassCanvas(data);
		temp = subclassKeyboard(temp);
		return temp;
	}

	private static byte[] subclassCanvas(final byte[] data) {
		final String superClass = Canvas.class.getCanonicalName().replace('.', '/');
		final ClassReader r = new ClassReader(data);
		final ClassNode node = new ClassNode();
		r.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
		if (node.superName.equals("java/awt/Canvas")) {
			node.superName = superClass;
			Debug.println("Binder", "Injected: " + superClass + " as the parent of " + node.name);
			for (final MethodNode method : (List<MethodNode>) node.methods) {
				if (method.name.equals("<init>")) {
					for (final AbstractInsnNode insn : method.instructions.toArray()) {
						if (insn instanceof MethodInsnNode) {
							final MethodInsnNode min = ((MethodInsnNode) insn);
							if (min.owner.equals("java/awt/Canvas")) {
								min.owner = superClass;
							}
						}
					}
				}
			}
		}
		//Save the changes
		final ClassWriter w = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(w);
		return w.toByteArray();
	}

	private static byte[] subclassKeyboard(final byte[] data) {
		final String superclass = Keyboard.class.getCanonicalName().replace('.', '/');
		ClassReader reader = new ClassReader(data);
		ClassNode node = new ClassNode();
		reader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
		for (String inter : (List<String>) node.interfaces) {
			if (inter.contains("KeyListener")) {
				Debug.println("Binder", "Injected: " + superclass + " as the parent of " + node.name);
				node.superName = superclass;
				for (MethodNode mn : (List<MethodNode>) node.methods) {
					if (mn.name.contains("key") || mn.name.contains("focus")) {
						mn.name = "_" + mn.name;
						//  System.out.println("renamed: " + mn.name);
					}
				}
				subclass(node, superclass, "java/lang/Object");
				for (final MethodNode method : (List<MethodNode>) node.methods) {
					if (!method.name.equals("<init>")) {
						continue;
					}
					for (final AbstractInsnNode ain : method.instructions.toArray()) {
						if (ain.getOpcode() == Opcodes.INVOKESPECIAL) {
							((MethodInsnNode) ain).owner = superclass;
							break;
						}
					}
				}
			}
		}
		subclass(node, superclass, "java/awt/event/KeyListener");
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(writer);
		return writer.toByteArray();
	}


	public static void subclass(final ClassNode node, final String superclass, final String subclass) {
		if (node.superName.equals(subclass))
			node.superName = superclass.replaceAll("\\.", "/");
	}

}
