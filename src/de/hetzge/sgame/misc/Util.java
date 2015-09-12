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

	public static int index(short x, short y, short width) {
		return x + y * width;
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

}
