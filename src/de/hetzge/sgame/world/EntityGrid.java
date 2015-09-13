package de.hetzge.sgame.world;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.error.InvalidGameStateException;

public class EntityGrid implements IF_Grid, Serializable {

	private final short width;
	private final short height;

	private final Entity[] entities;

	public EntityGrid(short width, short height) {
		this.width = width;
		this.height = height;

		this.entities = new Entity[width * height];
	}

	@Override
	public short getWidth() {
		return width;
	}

	@Override
	public short getHeight() {
		return height;
	}

	public void swap(Entity entityA, Entity entityB) {
		short registeredAX = entityA.getRegisteredX();
		short registeredAY = entityA.getRegisteredY();
		short registeredBX = entityB.getRegisteredX();
		short registeredBY = entityB.getRegisteredY();

		entityA.setRegisteredGridPosition(registeredBX, registeredBY);
		entities[index(registeredBX, registeredBY)] = entityA;
		entityB.setRegisteredGridPosition(registeredAX, registeredAY);
		entities[index(registeredAX, registeredAY)] = entityB;
	}

	public void set(short x, short y, Entity entity) {
		Entity entityOnPosition = get(x, y);
		if (entityOnPosition != null && !entityOnPosition.equals(entity)) {
			throw new InvalidGameStateException("Try to move to already used tile.");
		}
		unset(entity);
		entity.setRegisteredGridPosition(x, y);
		entities[index(x, y)] = entity;
	}

	private void unset(Entity entity) {
		short x = entity.getRegisteredX();
		short y = entity.getRegisteredY();
		entities[index(x, y)] = null;
	}

	public boolean is(short x, short y) {
		return get(x, y) != null;
	}

	public boolean is(GridPosition gridPosition) {
		return is(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public boolean isNot(GridPosition gridPosition) {
		return !is(gridPosition);
	}

	public boolean isEntity(short x, short y, Entity entity) {
		Entity entityOnGrid = get(x, y);
		if (entityOnGrid != null) {
			return entityOnGrid.equals(entity);
		} else {
			return false;
		}
	}

	public void remove(short x, short y) {
		entities[index(x, y)] = null;
	}

	public Entity get(short x, short y) {
		return entities[index(x, y)];
	}

	public boolean isEmpty(short x, short y) {
		return get(x, y) == null;
	}

	public List<Entity> getAllEntities() {
		List<Entity> result = new LinkedList<>();
		for (Entity entity : entities) {
			if (entity != null) {
				result.add(entity);
			}
		}
		return result;
	}

}
