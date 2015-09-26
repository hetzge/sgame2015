package de.hetzge.sgame.function;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Predicate;

import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.world.CollisionGrid;
import de.hetzge.sgame.world.EntityGrid;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.Path;
import de.hetzge.sgame.world.World;

public class EntityFunction implements IF_EntityFunction {

	@Override
	public void createEntity(E_EntityType entityType, short x, short y, int entityId, byte playerId) {
		EntityDefinition definition = entityType.getEntityDefinition();
		boolean moveable = definition.isMoveable();
		Entity entity = new Entity(entityId, playerId, entityType);
		entity.setWorldX(x);
		entity.setWorldY(y);
		App.game.getEntityManager().register(entity);
		App.game.getEntityGrid().set(x, y, entity);
		if (!moveable) {
			short width = entity.getWidth();
			short height = entity.getHeight();
			App.game.getWorld().getFixedCollisionGrid().setCollision(x, y, width, height);
		}
	}

	@Override
	public void goAway(Entity entity) {
		goAway(entity, new LinkedList<>());
	}

	public void goAway(Entity entity, List<GridPosition> blacklist) {
		EntityGrid entityGrid = App.game.getEntityGrid();
		World world = App.game.getWorld();

		short gridX = entity.getGridX();
		short gridY = entity.getGridY();

		blacklist.add(new GridPosition(gridX, gridY));

		List<GridPosition> aroundOnMap = world.getAroundOnMap(gridX, gridY);
		for (GridPosition aroundGridPosition : aroundOnMap) {
			if (isGoToAble(aroundGridPosition)) {
				gotoGridPosition(entity, aroundGridPosition.getGridX(), aroundGridPosition.getGridY());
				return;
			}
		}
		for (GridPosition aroundGridPosition : aroundOnMap) {
			if (blacklist.contains(aroundGridPosition)) {
				continue;
			}
			Entity entityAround = entityGrid.get(aroundGridPosition);
			if (entityAround != null && isGoAwayable(entityAround)) {
				goAway(entityAround, blacklist);
				return;
			}
		}

		// Stream<GridPosition> notBlacklistedAroundStream =
		// aroundStream.filter(gridPosition ->
		// !blacklist.contains(gridPosition));
		// Stream<GridPosition> emptyNotBlacklistedAroundStream =
		// notBlacklistedAroundStream.filter(entityGrid::isNot);
		// GridPosition goalGridPosition =
		// emptyNotBlacklistedAroundStream.findFirst().orElse(entityGrid.getAroundStream(gridX,
		// gridY).filter(gridPosition ->
		// !blacklist.contains(gridPosition)).findFirst().get());
		// Entity entityOnGoal = entityGrid.get(goalGridPosition.getGridX(),
		// goalGridPosition.getGridY());
		// if (entityOnGoal != null) {
		// blacklist.add(goalGridPosition);
		// goAway(entityOnGoal, blacklist);
		// }
		// gotoGridPosition(entity, goalGridPosition.getGridX(),
		// goalGridPosition.getGridY());
	}

	private boolean isGoAwayable(Entity entity) {
		return entity.getDefinition().isMoveable() && !entity.hasPath();
	}

	private boolean isGoToAble(GridPosition gridPosition) {
		return !App.game.getEntityGrid().is(gridPosition) && !App.game.getWorld().getFixedCollisionGrid().is(gridPosition);
	}

	@Override
	public void gotoGridPosition(Entity entity, short x, short y) {
		// TODO nur ein Schritt zulassen

		short gridX = entity.getGridX();
		short gridY = entity.getGridY();

		entity.setPath(new short[] { gridX, x }, new short[] { gridY, y });
	}

	@Override
	public Map<Entity, Path> findPath(List<Entity> entities, short goalX, short goalY) {
		if (entities.isEmpty()) {
			throw new IllegalArgumentException();
		}

		HashMap<Entity, Path> result = new HashMap<>();

		Entity alphaEntitiy = entities.get(0);
		for (int i = 1; i < entities.size(); i++) {
			Entity entity = entities.get(i);

			// TODO Gruppen verhalten
		}

		for (Entity entity : entities) {
			result.put(entity, findPath(entity, goalX, goalY));
		}

		return result;
	}

	@Override
	public Map<Entity, Path> findPath(List<Entity> entities, Entity goalEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path findPath(Entity entity, Entity goalEntity) {
		World world = App.game.getWorld();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();
		short goalX = goalEntity.getGridX();
		short goalY = goalEntity.getGridY();

		Predicate<GridPosition> predicate = (gridPosition) -> {
			boolean collision = fixedCollisionGrid.is(gridPosition);
			if (collision) {
				collision = !entity.isEntityGrid(gridPosition);
			}
			return collision;
		};

		Path path = findPath(entity, goalX, goalY, predicate);
		if (path != null) {
			ListIterator<GridPosition> listIterator = path.listIterator(path.pathSize());
			while (listIterator.hasPrevious()) {
				GridPosition previous = listIterator.previous();
				if (entity.isEntityGrid(previous)) {
					listIterator.remove();
				} else {
					break;
				}
			}
		}

		return path;
	}

	@Override
	public Path findPath(Entity entity, short goalX, short goalY) {
		World world = App.game.getWorld();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();
		return findPath(entity, goalX, goalY, gridPosition -> fixedCollisionGrid.is(gridPosition));
	}

	private Path findPath(Entity entity, short goalX, short goalY, Predicate<GridPosition> collisionPredicate) {
		World world = App.game.getWorld();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();
		boolean isGoalCollision = fixedCollisionGrid.is(goalX, goalY);
		if (isGoalCollision) {
			return null;
		}

		short startX = entity.getGridX();
		short startY = entity.getGridY();

		final short MAX_RATING = 1000;
		short rating = 0;
		Map<GridPosition, Short> ratings = new HashMap<>();
		List<GridPosition> nexts = new LinkedList<>();
		GridPosition goal = new GridPosition(goalX, goalY);
		nexts.add(goal);
		ratings.put(goal, rating++);

		boolean isPossible = false;
		main: while (true) {
			List<GridPosition> nextNexts = new LinkedList<>();
			for (GridPosition gridPosition : nexts) {
				for (E_Orientation orientation : E_Orientation.values) {
					GridPosition next = gridPosition.getAround(orientation);
					boolean isOnWorld = world.isOnGrid(next);
					if (!isOnWorld) {
						continue;
					}
					boolean isStart = next.getGridX() == startX && next.getGridY() == startY;
					if (isStart) {
						isPossible = true;
						break main;
					}
					boolean isCollision = collisionPredicate.test(next);
					if (isCollision) {
						continue;
					}
					boolean alreadyRated = ratings.containsKey(next);
					if (!alreadyRated) {
						ratings.put(next, rating);
						nextNexts.add(next);
					}
				}
			}
			rating++;
			if (rating >= MAX_RATING) {
				Logger.warn("Stoped pathfinding because reached max rating.");
				break main;
			}
			nexts = nextNexts;
			if (nexts.isEmpty()) {
				break main;
			}
		}

		short min = Short.MAX_VALUE;
		if (isPossible) {
			Path path = new Path();
			GridPosition next = new GridPosition(startX, startY);
			GridPosition nextNext = null;
			path.add(next);
			while (next.getGridX() != goalX || next.getGridY() != goalY) {
				for (E_Orientation orientation : E_Orientation.values) {
					GridPosition around = next.getAround(orientation);
					Short aroundRating = ratings.get(around);
					if (aroundRating != null) {
						if (aroundRating < min) {
							min = aroundRating;
							nextNext = around;
						}
					}
				}
				next = nextNext;
				path.add(next);
			}

			Logger.info("Found path: " + path);

			return path;
		} else {
			return null;
		}
	}

}
