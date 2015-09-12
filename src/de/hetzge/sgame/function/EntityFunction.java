package de.hetzge.sgame.function;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.world.CollisionGrid;
import de.hetzge.sgame.world.EntityGrid;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.PathGridPosition;
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
			short width = definition.getWidth();
			short height = definition.getHeight();
			App.game.getWorld().getFixedCollisionGrid().setCollision(x, y, width, height);
		}
	}

	@Override
	public void goAway(Entity entity) {
		goAway(entity, new LinkedList<>());
	}

	public void goAway(Entity entity, List<GridPosition> blacklist) {
		EntityGrid entityGrid = App.game.getEntityGrid();
		short gridX = entity.getGridX();
		short gridY = entity.getGridY();

		Stream<GridPosition> aroundStream = entityGrid.getAroundStream(gridX, gridY);
		Stream<GridPosition> notBlacklistedAroundStream = aroundStream.filter(gridPosition -> !blacklist.contains(gridPosition));
		Stream<GridPosition> emptyNotBlacklistedAroundStream = notBlacklistedAroundStream.filter(entityGrid::isNot);
		GridPosition goalGridPosition = emptyNotBlacklistedAroundStream.findFirst().orElse(aroundStream.findFirst().get());
		Entity entityOnGoal = entityGrid.get(goalGridPosition.getGridX(), goalGridPosition.getGridY());
		if (entityOnGoal != null) {
			blacklist.add(goalGridPosition);
			goAway(entityOnGoal, blacklist);
		}
		gotoGridPosition(entity, goalGridPosition.getGridX(), goalGridPosition.getGridY());
	}

	@Override
	public void gotoGridPosition(Entity entity, short x, short y) {
		// TODO
	}

	@Override
	public Map<Entity, PathGridPosition> findPath(List<Entity> entities, short goalX, short goalY) {
		if (entities.isEmpty()) {
			throw new IllegalArgumentException();
		}

		HashMap<Entity, PathGridPosition> result = new HashMap<>();

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

	private PathGridPosition findPath(Entity entity, short goalX, short goalY) {
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

		boolean isPossible = false;
		main: while (true) {
			List<GridPosition> nextNexts = new LinkedList<>();
			for (GridPosition gridPosition : nexts) {
				for (E_Orientation orientation : E_Orientation.values) {
					GridPosition next = gridPosition.getAround(orientation);
					boolean isStart = next.getGridX() == startX && next.getGridY() == startY;
					if (isStart) {
						isPossible = true;
						break main;
					}
					boolean isCollision = fixedCollisionGrid.is(next);
					if (isCollision) {
						continue;
					}
					boolean isOnWorld = world.isOnGrid(gridPosition);
					if (!isOnWorld) {
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
			PathGridPosition pathGridPosition = new PathGridPosition(startX, startY);
			GridPosition next = new GridPosition(startX, startY);
			while (next.getGridX() != goalX || next.getGridY() != goalY) {
				for (E_Orientation orientation : E_Orientation.values) {
					GridPosition around = next.getAround(orientation);
					Short aroundRating = ratings.get(around);
					if (aroundRating != null) {
						if (aroundRating < min) {
							min = aroundRating;
							next = around;
						}
					}
				}
				pathGridPosition.setNext(next);
			}

			return pathGridPosition;
		} else {
			return null;
		}
	}

	@Override
	public Map<Entity, PathGridPosition> findPath(List<Entity> entities, Entity goalEntity) {
		// TODO Auto-generated method stub
		return null;
	}

}
