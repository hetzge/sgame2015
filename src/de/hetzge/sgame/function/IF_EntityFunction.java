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

	Path findPath(Entity entity, short goalX, short goalY);

	Path findPath(Entity entity, Entity goalEntity);

	Map<Entity, Path> findPath(List<Entity> entities, short goalX, short goalY);

}
