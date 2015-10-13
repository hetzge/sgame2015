package de.hetzge.sgame.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.utils.IntMap;

import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.misc.Constant;

public class EntityManager {

	private int nextId = 0;

	private final IntMap<Entity> entitiesById = new IntMap<>();
	private final Set<Entity> entities = new TreeSet<>(new Entity.IdComparator());
	private final Set<Entity> removeEntities = new TreeSet<>(new Entity.IdComparator());

	public void register(Entity entity) {
		int entityId = entity.getId();
		if (this.entitiesById.containsKey(entityId)) {
			throw new InvalidGameStateException("Try to register entity with already registered id.");
		}
		this.entitiesById.put(entity.getId(), entity);
		this.entities.add(entity);
	}

	public void registerRemove(Entity entity) {
		this.removeEntities.add(entity);
	}

	public void remove(Entity entity) {
		int entityId = entity.getId();
		this.entitiesById.remove(entityId);
		this.entities.remove(entity);
	}

	public Entity get(int entityId) {
		Entity entity = this.entitiesById.get(entityId);
		if (entity == null) {
			if (entityId == Constant.NO_ENTITY_ID) {
				return null;
			} else {
				throw new InvalidGameStateException("Try to access non existing entity " + entityId);
			}
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

	public Iterable<Entity> flushRemoveEntities() {
		if (this.removeEntities.isEmpty()) {
			return Collections.emptyList();
		} else {
			Iterable<Entity> result = new ArrayList<>(this.removeEntities);
			this.removeEntities.clear();
			return result;
		}
	}

	public Iterable<Entity> getEntities() {
		return this.entities;
	}

	public int getNextId() {
		return this.nextId++;
	}

}
