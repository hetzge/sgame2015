package de.hetzge.sgame.astar;

import java.util.AbstractQueue;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.tuple.Pair;

import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.misc.Util;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.Path;
import net.openhft.koloboke.collect.map.hash.HashIntIntMap;
import net.openhft.koloboke.collect.map.hash.HashIntIntMaps;

public class AStar {

	private static final int LIMIT_DEPTH = 10000;
	private static final int DEFAULT_EXPECTED_RATING_SIZE = 10000;
	private final HashIntIntMap ratingMap = HashIntIntMaps.newMutableMap(DEFAULT_EXPECTED_RATING_SIZE);
	private final Predicate<GridPosition> collisionPredicate;
	private final short areaWidth;

	public AStar(Predicate<GridPosition> collisionPredicate, short areaWidth) {
		this.collisionPredicate = collisionPredicate;
		this.areaWidth = areaWidth;
	}

	public Path findPath(GridPosition start, GridPosition goal, boolean bestFirst) {
		long startTime = System.currentTimeMillis();

		AbstractQueue<GridPosition> nexts = bestFirst ? new PriorityQueue<GridPosition>((a, b) -> {
			return Float.valueOf(a.distanceTo(goal)).compareTo(b.distanceTo(goal));
		}) : new ConcurrentLinkedQueue<>();

		rate(start, Integer.MAX_VALUE);
		nexts.add(start);
		GridPosition next;
		int depth = 0;
		while ((next = nexts.poll()) != null) {
			int nextRating = getRating(next) - 1;
			if (next.equals(goal)) {
				System.out.println("Step 1: " + (System.currentTimeMillis() - startTime));
				return evaluatePath(start, goal);
			}
			for (E_Orientation orientation : E_Orientation.values) {
				GridPosition around = next.getAround(orientation);
				if (!alreadyRated(around) && !this.collisionPredicate.test(around)) {
					rate(around, nextRating);
					nexts.add(around);
				}
			}
			depth++;
			if (depth > LIMIT_DEPTH) {
				return null;
			}
		}

		return null;
	}

	public <GOAL> Pair<GOAL, Path> find(GridPosition start, Function<GridPosition, GOAL> searchFunction) {
		long startTime = System.currentTimeMillis();

		AbstractQueue<GridPosition> nexts = new ConcurrentLinkedQueue<>();

		rate(start, Integer.MAX_VALUE);
		nexts.add(start);
		GridPosition next;
		GOAL goalObject;
		int depth = 0;
		while ((next = nexts.poll()) != null) {
			int nextRating = getRating(next) - 1;
			for (E_Orientation orientation : E_Orientation.values) {
				GridPosition around = next.getAround(orientation);
				if ((goalObject = searchFunction.apply(around)) != null) {
					System.out.println("Step 1: " + (System.currentTimeMillis() - startTime));
					return Pair.of(goalObject, evaluatePath(start, next));
				}
				if (!alreadyRated(around) && !this.collisionPredicate.test(around)) {
					rate(around, nextRating);
					nexts.add(around);
				}
			}
			depth++;
			if (depth > LIMIT_DEPTH) {
				return Pair.of(null, null);
			}
		}

		return Pair.of(null, null);
	}

	private Path evaluatePath(GridPosition start, GridPosition goal) {
		long startTime = System.currentTimeMillis();

		Path path = new Path();
		int rating = getRating(goal);
		GridPosition nextNext = goal;
		while (!nextNext.equals(start)) {
			GridPosition next = nextNext;
			for (E_Orientation orientation : E_Orientation.values) {
				GridPosition position = next.getAround(orientation);
				int nextRating = getRating(position);
				if (nextRating > rating) {
					rating = nextRating;
					nextNext = position;
				}
			}
			path.add(nextNext);
		}

		System.out.println("Step 2: " + (System.currentTimeMillis() - startTime) + " " + path.pathSize());

		path = path.reverse();
		path.add(goal);
		return path;
	}

	private void rate(GridPosition gridPosition, int rating) {
		rate(gridPosition.getGridX(), gridPosition.getGridY(), rating);
	}

	private void rate(short x, short y, int rating) {
		this.ratingMap.put(Util.index(x, y, this.areaWidth), rating);
	}

	private boolean alreadyRated(GridPosition gridPosition) {
		return alreadyRated(gridPosition.getGridX(), gridPosition.getGridY());
	}

	private boolean alreadyRated(short x, short y) {
		return this.ratingMap.containsKey(Util.index(x, y, this.areaWidth));
	}

	private int getRating(GridPosition gridPosition) {
		return getRating(gridPosition.getGridX(), gridPosition.getGridY());
	}

	private int getRating(short x, short y) {
		return this.ratingMap.getOrDefault(Util.index(x, y, this.areaWidth), 0);
	}

	public static void main(String[] args) {

		int size = 10000;

		AStar aStarConnector = new AStar(position -> !(position.getGridX() >= 0 && position.getGridY() >= 0
				&& position.getGridX() < size && position.getGridY() < size), (short) size);
		Path path = aStarConnector.findPath(new GridPosition((short) 12, (short) 12),
				new GridPosition((short) 15, (short) 15), true);

		System.out.println(path);

		AStar aStarConnector2 = new AStar(position -> !(position.getGridX() >= 0 && position.getGridY() >= 0
				&& position.getGridX() < size && position.getGridY() < size), (short) size);
		Pair<GridPosition, Path> find = aStarConnector2.find(new GridPosition((short) 10, (short) 10), (position) -> {
			if (position.getGridX() > 100 && position.getGridY() > 100f) {
				return position;
			} else {
				return null;
			}
		});

		System.out.println();

		// System.out.println(path);

	}

}
