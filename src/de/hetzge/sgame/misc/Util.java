package de.hetzge.sgame.misc;

import java.util.Iterator;
import java.util.function.Predicate;

public final class Util {

	private Util() {
	}

	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int offset(int size) {
		return size % 2 == 0 ? size / 2 : (size - 1) / 2;
	}

	public static int index(short x, short y, short width) {
		return x + y * width;
	}

	public static int index(int x, int y, short width) {
		return x + y * width;
	}

	public static int index(int x, int y, int width) {
		return x + y * width;
	}

	public static int unIndexX(int index, int width) {
		return index % width;
	}

	public static int unIndexY(int index, int width) {
		return (int) Math.floor(index / width);
	}

	public static String toString(short x, short y) {
		return "(" + x + "|" + y + ")";
	}

	public static <T> T findOne(Iterator<T> iterator, Predicate<T> predicate) {
		while (iterator.hasNext()) {
			T next = iterator.next();
			if (predicate.test(next)) {
				return next;
			}
		}
		return null;
	}

	public static short worldToGrid(float world) {
		return (short) Math.floor(world / Constant.TILE_SIZE);
	}

	public static float gridToWorld(short grid) {
		return grid * Constant.TILE_SIZE;
	}

}
