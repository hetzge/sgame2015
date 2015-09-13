package de.hetzge.sgame.function;

import java.util.List;
import java.util.Map;

import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.world.Path;

public interface IF_EntityFunction {

	void goAway(Entity entity);
	
	void gotoGridPosition(Entity entity, short x, short y);

	void createEntity(E_EntityType entityType, short x, short y, int entityId, byte playerId);
	
	Map<Entity, Path> findPath(List<Entity> entities, short goalX, short goalY);
	
	Map<Entity, Path> findPath(List<Entity> entities, Entity goalEntity);
	
}
