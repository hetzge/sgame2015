package de.hetzge.sgame.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.utils.IntMap;

import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.misc.Constant;

public class EntityManager {

	private int nextId = 0;

	private final IntMap<Entity> entitiesById = new IntMap<>();
	private final IntMap<Container> needsById = new IntMap<>();
	private final IntMap<Container> providesById = new IntMap<>();
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
		int entityId = entity.getId();
		entitiesById.remove(entityId);
		needsById.remove(entityId);
		providesById.remove(entityId);
		entities.remove(entity);
	}

	public Entity get(int entityId) {
		Entity entity = entitiesById.get(entityId);
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

	public Iterable<Entity> getEntities() {
		return entities;
	}

	public Container getNeeds(int entityId) {
		Container container = needsById.get(entityId);
		if (container == null) {
			Entity entity = get(entityId);
			Container newContainer = entity.getDefinition().createDefaultNeedContainer(entityId);
			needsById.put(entityId, newContainer);
			return getNeeds(entityId);
		} else {
			return container;
		}
	}

	public Container getProvides(int entityId) {
		Container container = providesById.get(entityId);
		if (container == null) {
			Entity entity = get(entityId);
			Container newContainer = entity.getDefinition().createDefaultProvideContainer(entityId);
			providesById.put(entityId, newContainer);
			return getProvides(entityId);
		} else {
			return container;
		}
	}

	public int getNextId() {
		return nextId++;
	}

}
