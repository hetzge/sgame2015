package de.hetzge.sgame.function;

import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.world.GridPosition;

public interface IF_WorldFunction {

	void setOnWorld(Entity entity, short x, short y);

	void moveOnWorld(Entity entity, short oldX, short oldY, short newX, short newY);

	boolean checkSpaceForEntity(Entity entity, short x, short y);
	
	boolean checkSpaceForEntity(E_EntityType entityType, short x, short y);
	
	GridPosition findEmptyGridPositionAround(E_EntityType entityType, short x, short y);
	
	
	
}
