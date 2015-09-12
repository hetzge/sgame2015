package de.hetzge.sgame.entity;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entries;

import de.hetzge.sgame.error.InvalidGameStateException;

public class EntityManager {

	private int nextId = 0;

	private final IntMap<Entity> entitiesById = new IntMap<>();

	public void register(Entity entity) {
		int entityId = entity.getId();
		if (entitiesById.containsKey(entityId)) {
			throw new InvalidGameStateException("Try to register entity with already registered id.");
		}
		entitiesById.put(entity.getId(), entity);
	}

	public void remove(Entity entity) {
		entitiesById.remove(entity.getId());
	}

	public Entity get(int entityId) {
		return entitiesById.get(entityId);
	}

	public Iterable<Entity> getEntities() {
		return entitiesById.values();
	}

	public int getNextId() {
		return nextId++;
	}

}
