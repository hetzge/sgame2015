package de.hetzge.sgame.entity;

import java.util.ArrayList;
import java.util.List;

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
		Entity entity = entitiesById.get(entityId);
		if (entity == null) {
			throw new InvalidGameStateException("Try to access non existing entity " + entityId);
		} else {
			return entity;
		}
	}

	public List<Entity> get(List<Integer> entityIds) {
		List<Entity> result = new ArrayList<>(entityIds.size());
		for (Integer entityId : entityIds) {
			Entity entity = get(entityId);
			result.add(entity);
		}
		return result;
	}

	public Iterable<Entity> getEntities() {
		return entitiesById.values();
	}

	public int getNextId() {
		return nextId++;
	}

}
