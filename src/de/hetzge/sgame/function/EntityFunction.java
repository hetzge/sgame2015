package de.hetzge.sgame.function;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.entity.job.main.DestroyJob;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.world.EntityGrid;
import de.hetzge.sgame.world.GridPosition;
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
				entity.getJob().pauseLong();
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
	}

	private boolean isGoAwayable(Entity entity) {
		return entity.getDefinition().isMoveable() && !entity.hasPath();
	}

	private boolean isGoToAble(GridPosition gridPosition) {
		return !App.game.getEntityGrid().is(gridPosition)
				&& !App.game.getWorld().getFixedCollisionGrid().is(gridPosition);
	}

	public void gotoGridPosition(Entity entity, short x, short y) {
		// TODO nur ein Schritt zulassen

		short gridX = entity.getGridX();
		short gridY = entity.getGridY();

		entity.setPath(new short[] { gridX, x }, new short[] { gridY, y });
	}

}
