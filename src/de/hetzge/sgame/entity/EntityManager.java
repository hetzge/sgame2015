package de.hetzge.sgame.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.utils.IntMap;

import de.hetzge.sgame.error.InvalidGameStateException;

public class EntityManager {

	private int nextId = 0;

	private final IntMap<Entity> entitiesById = new IntMap<>();
	private final Set<Entity> entities = new TreeSet<>(new Entity.IdComparator());

	public void register(Entity entity) {
		int entityId = entity.getId();
		if (entitiesById.containsKey(entityId)) {
			throw new InvalidGameStateException("Try to register entity with already registered id.");
		}
		System.out.println(entity.getId());
		entitiesById.put(entity.getId(), entity);
		entities.add(entity);
	}

	public void remove(Entity entity) {
		entitiesById.remove(entity.getId());
		entities.remove(entity);
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
		return entities;
	}

	public int getNextId() {
		return nextId++;
	}

}
