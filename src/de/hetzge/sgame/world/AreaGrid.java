package de.hetzge.sgame.world;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import de.hetzge.sgame.misc.E_Orientation;

public class AreaGrid implements IF_Grid {

	public static final short NO_AREA = 0;

	private final short width;
	private final short height;

	private final Set<Short> availableAreas = new HashSet<>();
	private final short[] areas;
	private final Predicate<GridPosition> collisionPredicate;

	public AreaGrid(short width, short height, Predicate<GridPosition> collisionPredicate) {
		this.width = width;
		this.height = height;
		this.collisionPredicate = collisionPredicate;
		this.areas = new short[width * height];
		init();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			long one = System.currentTimeMillis();
			new AreaGrid((short) 10000, (short) 10000, p -> false);
			System.out.println(System.currentTimeMillis() - one);
		}
	}

	public void init() {
		for (short y = 0; y < this.height; y++) {
			for (short x = 0; x < this.width; x++) {
				fill(x, y);
			}
		}
	}

	public void set(GridPosition gridPosition, short area) {
		set(gridPosition.getGridX(), gridPosition.getGridY(), area);
	}

	public void set(short x, short y, short area) {
		this.areas[index(x, y)] = area;
	}

	public short get(GridPosition gridPosition) {
		return get(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public short get(short x, short y) {
		return this.areas[index(x, y)];
	}

	public void fill(short x, short y) {
		fill(new GridPosition(x, y));
	}

	public void fill(GridPosition gridPosition) {
		short newValue = Arrays.asList(E_Orientation.values).stream().map(gridPosition::getAround)
				.filter(this::isOnGrid).map(this::get).filter(v -> v != NO_AREA).findFirst().orElse(getNewArea());
		set(gridPosition, newValue);
		final List<GridPosition> nexts = new LinkedList<>();
		final List<GridPosition> nextNexts = new LinkedList<>();
		nextNexts.add(gridPosition);

		while (!nextNexts.isEmpty()) {
			nexts.clear();
			nexts.addAll(nextNexts);
			nextNexts.clear();
			for (GridPosition next : nexts) {
				for (E_Orientation orientation : E_Orientation.values) {
					GridPosition around = next.getAround(orientation);

					if (isOnGrid(around) && !this.collisionPredicate.test(gridPosition) && get(around) != newValue) {
						nextNexts.add(around);
						set(around, newValue);
					}
				}
			}
		}
	}

	public void unfill(short x, short y) {
		unfill(new GridPosition(x, y));
	}

	public void unfill(GridPosition gridPosition) {
		// TODO
	}

	private short getNewArea() {
		for (short i = 1; i <= Short.MAX_VALUE; i++) {
			if (!this.availableAreas.contains(i)) {
				return i;
			}
		}
		throw new IllegalStateException("Max area id reached.");
		// TODO
	}

	@Override
	public short getWidth() {
		return this.width;
	}

	@Override
	public short getHeight() {
		return this.height;
	}

}
