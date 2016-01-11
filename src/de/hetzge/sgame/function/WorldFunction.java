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
		World world = App.getGame().getWorld();
		EntityGrid entityGrid = App.getGame().getWorld().getEntityGrid();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();

		EntityDefinition entityDefinition = entityType.getEntityDefinition();
		short width = entityDefinition.getWidth();
		short height = entityDefinition.getHeight();

		if (fixedCollisionGrid.is(x, y, width, height)) {
			return false;
		}

		// TODO Hier auch Dimensionen einbeziehen ?
		if (entityGrid.is(x, y)) {
			return false;
		}

		return true;
	}

	public boolean checkSpace(short x, short y) {
		return !App.getGame().getWorld().getFixedCollisionGrid().is(x, y)
				&& !App.getGame().getWorld().getEntityGrid().is(x, y);
	}

	public GridPosition findEmptyGridPositionAround(E_EntityType entityType, short x, short y) {
		EntityDefinition entityDefinition = entityType.getEntityDefinition();
		boolean isMoveable = entityDefinition.isMoveable();
		if (!isMoveable) {
			throw new IllegalArgumentException("This only works with moveable entities");
		}

		World world = App.getGame().getWorld();
		CollisionGrid fixedCollisionGrid = world.getFixedCollisionGrid();
		EntityGrid entityGrid = App.getGame().getWorld().getEntityGrid();

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
