package de.hetzge.sgame.function;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.entity.job.main.DestroyJob;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.world.CollisionGrid;
import de.hetzge.sgame.world.EntityGrid;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.Path;
import de.hetzge.sgame.world.World;

public class EntityFunction {

	public void destroyEntity(Entity entity) {
		Validate.notNull(entity);
		Logger.info("destroy entity " + entity.getId());
		entity.setOwner(Constant.DEATH_PLAYER_ID);
		entity.setActivity(E_Activity.DESTROY);
		entity.setJob(new DestroyJob(entity));
	}

	public void removeEntity(Entity entity) {
		Logger.info("remove entity " + entity.getId());
		App.game.getEntityManager().remove(entity);
		App.game.getEntityGrid().unset(entity);
		if (!entity.getDefinition().isMoveable()) {
			App.game.getWorld().getFixedCollisionGrid().unsetCollision(entity);
		}
	}

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

	public void goAway(Entity entity) {
		goAway(entity, new LinkedList<>());
	}

	private void goAway(Entity entity, List<GridPosition> blacklist) {
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

	public void gotoGridPosition(Entity entity, short x, short y) {
		// TODO nur ein Schritt zulassen

		short gridX = entity.getGridX();
		short gridY = entity.getGridY();

		entity.setPath(new short[] { gridX, x }, new short[] { gridY, y });
	}

	public Map<Entity, Path> findPath(List<Entity> entities, GridPosition goal) {
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
			result.put(entity, findPath(entity, goal));
		}

		return result;
	}

	public Path findPathToEntity(Entity entity, Entity goalEntity) {
		World world = App.game.getWorld();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();
		GridPosition goalGridPosition = goalEntity.getGridPosition();

		// ignore goal entity
		CollisionPredicate collisionPredicate = (gridPosition) -> {
			boolean collision = fixedCollisionGrid.is(gridPosition);
			if (collision) {
				collision = !entity.isEntityGrid(gridPosition);
			}
			return collision;
		};

		Path path = findPath(entity, goalGridPosition, collisionPredicate);
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

	/**
	 * Finds a path to given <code>goal</code>
	 */
	public Path findPath(Entity entity, GridPosition goal) {
		World world = App.game.getWorld();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();
		return findPath(entity, goal, fixedCollisionGrid::is);
	}

	/**
	 * Finds a path to given <code>goal</code>
	 */
	public Path findPath(Entity entity, GridPosition goal, CollisionPredicate collisionPredicate) {
		GridPosition start = entity.getGridPosition();
		RatingMap ratings = rate(start, goal, start::equals, collisionPredicate);
		return ratings != null ? evaluatePath(start, goal, ratings) : null;
	}

	/**
	 * Finds a path to a unknown goal defined by <code>searchPredicate</code>
	 */
	public Path findPath(Entity entity, OnMapPredicate searchPredicate) {
		World world = App.game.getWorld();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();
		return findPath(entity, fixedCollisionGrid::is, searchPredicate);
	}

	/**
	 * Finds a path to a unknown goal defined by <code>searchPredicate</code>
	 */
	public Path findPath(Entity entity, CollisionPredicate collisionPredicate, OnMapPredicate searchPredicate) {
		GridPosition start = entity.getDoorGridPosition();
		RatingMap ratings = rate(entity, collisionPredicate, searchPredicate);
		Path path = ratings != null ? evaluatePath(ratings.goal, start, ratings) : null;
		if (path != null) {
			path.removeFirst();
			return path.reverse();
		} else {
			return null;
		}
	}

	public void iterateMap(Entity entity, OnMapPredicate searchPredicate) {
		World world = App.game.getWorld();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();
		iterateMap(entity, fixedCollisionGrid::is, searchPredicate);
	}

	public void iterateMap(Entity entity, CollisionPredicate collisionPredicate, OnMapPredicate searchPredicate) {
		rate(entity, collisionPredicate, searchPredicate);
	}

	private RatingMap rate(Entity entity, CollisionPredicate collisionPredicate, OnMapPredicate searchPredicate) {
		return rate(entity.getDoorGridPosition(), searchPredicate, collisionPredicate);
	}

	private RatingMap rate(GridPosition start, GridPosition goal, OnMapPredicate searchPredicate, CollisionPredicate collisionPredicate) {
		boolean isStartIsGoal = start.equals(goal);
		if (isStartIsGoal) {
			return null;
		}

		World world = App.game.getWorld();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();
		boolean isGoalCollision = fixedCollisionGrid.is(goal);
		if (isGoalCollision) {
			return null;
		}

		return rate(goal, searchPredicate, collisionPredicate);
	}

	private RatingMap rate(GridPosition beginAtGridPosition, OnMapPredicate endOfPathPredicate, CollisionPredicate collisionPredicate) {
		World world = App.game.getWorld();

		short rating = 0;
		RatingMap ratings = new RatingMap();
		List<GridPosition> nexts = new LinkedList<>();
		nexts.add(beginAtGridPosition);
		ratings.put(beginAtGridPosition, rating++);

		while (true) {
			List<GridPosition> nextNexts = new LinkedList<>();
			for (GridPosition gridPosition : nexts) {
				for (E_Orientation orientation : E_Orientation.values) {
					GridPosition next = gridPosition.getAround(orientation);
					boolean isOnWorld = world.isOnGrid(next);
					if (!isOnWorld) {
						continue;
					}
					if (endOfPathPredicate.test(next)) {
						ratings.goal = next;
						return ratings;
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
			if (rating >= Constant.MAX_A_STAR_DEPTH) {
				Logger.trace("Stoped pathfinding because reached max rating.");
				return null;
			}

			nexts = nextNexts;
			if (nexts.isEmpty()) {
				return null;
			}
		}
	}

	private Path evaluatePath(GridPosition start, GridPosition goal, RatingMap ratings) {
		short min = Short.MAX_VALUE;
		Path path = new Path();
		GridPosition next = new GridPosition(start);
		GridPosition nextNext = null;
		path.add(next);
		int i = 0;
		while (!next.equals(goal)) {
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

			i++;
			if (i > Constant.MAX_A_STAR_DEPTH * 2) {
				throw new InvalidGameStateException("Try to evaluate impossible path (start: " + start + ", gaol: " + goal + ")");
			}
		}

		Logger.info("Found path: " + path);

		return path;
	}

	public static class RatingMap extends HashMap<GridPosition, Short> {
		private GridPosition goal;
	}

	public static interface CollisionPredicate extends OnMapPredicate {
	}

	public static interface EntityPredicate extends Predicate<Entity> {
	}

	public static interface ProviderPredicate extends Predicate<Container> {
	}

}
