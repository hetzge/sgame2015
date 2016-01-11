package de.hetzge.sgame.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.tuple.Pair;

import de.hetzge.sgame.App;
import de.hetzge.sgame.astar.AStar;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.Path;

public class SearchFunction {

	public final Search<Entity> byEntity = new Search<Entity>() {

		@Override
		public <SEARCH_RESULT> Pair<SEARCH_RESULT, Path> findAndPath(Entity entity,
				Function<Entity, SEARCH_RESULT> entitySearchFunction) {
			GridPosition start = entity.getGridPosition();
			return createAStar().find(start, createGridPositionSearchFunction(entitySearchFunction));
		}

		@Override
		public <SEARCH_RESULT> SEARCH_RESULT find(Entity entity, Function<Entity, SEARCH_RESULT> searchFunction) {
			return findAndPath(entity, searchFunction).getLeft();
		}

		@Override
		public Path findPath(Entity entity, Predicate<Entity> entitySearchPredicate) {
			GridPosition start = entity.getGridPosition();
			return createAStar().find(start, createGridPositionSearchFunction(entityToTest -> {
				return entitySearchPredicate.test(entityToTest) ? entityToTest : null;
			})).getRight();
		}

		private <SEARCH_RESULT> Function<GridPosition, SEARCH_RESULT> createGridPositionSearchFunction(
				Function<Entity, SEARCH_RESULT> searchFunction) {
			return gridPosition -> {
				if (!isOnGrid(gridPosition)) {
					return null;
				}
				Entity entityToTest = getEntityOnPosition(gridPosition);
				if (entityToTest == null) {
					return null;
				}
				return searchFunction.apply(entityToTest);
			};
		}

	};

	public final Search<GridPosition> byGridPosition = new Search<GridPosition>() {

		@Override
		public <SEARCH_RESULT> Pair<SEARCH_RESULT, Path> findAndPath(Entity entity,
				Function<GridPosition, SEARCH_RESULT> searchFunction) {
			GridPosition start = entity.getGridPosition();
			return createAStar().find(start, searchFunction);
		}

		@Override
		public <SEARCH_RESULT> SEARCH_RESULT find(Entity entity, Function<GridPosition, SEARCH_RESULT> searchFunction) {
			return findAndPath(entity, searchFunction).getLeft();
		}

		@Override
		public Path findPath(Entity entity, Predicate<GridPosition> goalPredicate) {
			return findAndPath(entity, gridPosition -> goalPredicate.test(gridPosition) ? gridPosition : null)
					.getRight();
		}

	};

	public Map<Entity, Path> findPath(List<Entity> entities, GridPosition goal) {
		Map<Entity, Path> result = new HashMap<>();
		for (Entity entity : entities) {
			result.put(entity, findPath(entity, goal));
		}
		return result;
	}

	public Path findPath(Entity entity, GridPosition goal) {
		GridPosition start = entity.getGridPosition();
		return createAStar().findPath(start, goal, true);
	}

	private Entity getEntityOnPosition(GridPosition gridPosition) {
		return App.getGame().getWorld().getEntityGrid().get(gridPosition);
	}

	private boolean isOnGrid(GridPosition gridPosition) {
		return App.getGame().getWorld().isOnGrid(gridPosition);
	}

	private AStar createAStar() {
		short worldWidth = App.getGame().getWorld().getWidth();
		Predicate<GridPosition> onGridPredicate = App.getGame().getWorld().getFixedCollisionGrid()::isOnGrid;
		Predicate<GridPosition> collisionPredicate = App.getGame().getWorld().getFixedCollisionGrid()::is;
		return new AStar(onGridPredicate.and(collisionPredicate), worldWidth);
	}

	public static interface Search<BY> {
		public <SEARCH_RESULT> Pair<SEARCH_RESULT, Path> findAndPath(Entity entity,
				java.util.function.Function<BY, SEARCH_RESULT> searchFunction);

		public <SEARCH_RESULT> SEARCH_RESULT find(Entity entity,
				java.util.function.Function<BY, SEARCH_RESULT> searchFunction);

		public Path findPath(Entity entity, Predicate<BY> goalPredicate);
	}

}
