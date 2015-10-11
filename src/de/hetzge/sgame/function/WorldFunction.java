package de.hetzge.sgame.function;

import java.util.Iterator;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.world.CollisionGrid;
import de.hetzge.sgame.world.EntityGrid;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.World;

public class WorldFunction {

	public void setOnWorld(Entity entity, short x, short y) {
	}

	public void moveOnWorld(Entity entity, short oldX, short oldY, short newX, short newY) {
	}

	public boolean checkSpaceForEntity(Entity entity, short x, short y) {
		return checkSpaceForEntity(entity.getEntityType(), x, y);
	}

	public boolean checkSpaceForEntity(E_EntityType entityType, short x, short y) {
		World world = App.game.getWorld();
		EntityGrid entityGrid = App.game.getEntityGrid();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();

		EntityDefinition entityDefinition = entityType.getEntityDefinition();
		short width = entityDefinition.getWidth();
		short height = entityDefinition.getHeight();

		if (fixedCollisionGrid.is(x, y, width, height)) {
			return false;
		}

		if (entityGrid.is(x, y)) {
			return false;
		}

		return true;
	}

	public GridPosition findEmptyGridPositionAround(E_EntityType entityType, short x, short y) {
		EntityDefinition entityDefinition = entityType.getEntityDefinition();
		boolean isMoveable = entityDefinition.isMoveable();
		if (!isMoveable) {
			throw new IllegalArgumentException("This only works with moveable entities");
		}

		World world = App.game.getWorld();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();
		EntityGrid entityGrid = App.game.getEntityGrid();

		Iterator<GridPosition> spiralIterator = world.getSpiralIterator(x, y, (short) 30);
		while (spiralIterator.hasNext()) {
			GridPosition gridPosition = spiralIterator.next();
			boolean isFixedCollision = fixedCollisionGrid.is(gridPosition);
			boolean isEntityCollision = entityGrid.is(gridPosition);
			if (!isFixedCollision && !isEntityCollision) {
				return gridPosition;
			}
		}
		return null;
	}

}
