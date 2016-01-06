package de.hetzge.sgame.world;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import de.hetzge.sgame.entity.Entity;

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
		return this.width;
	}

	@Override
	public short getHeight() {
		return this.height;
	}

	public void swap(Entity entityA, Entity entityB) {
		short registeredAX = entityA.getRegisteredX();
		short registeredAY = entityA.getRegisteredY();
		short registeredBX = entityB.getRegisteredX();
		short registeredBY = entityB.getRegisteredY();
		unset(entityA);
		unset(entityB);
		set(registeredBX, registeredBY, entityA, false);
		set(registeredAX, registeredAY, entityB, false);
	}

	public void set(short x, short y, Entity entity) {
		set(x, y, entity, true);
	}

	public void set(short x, short y, Entity entity, boolean unset) {
		eachEntityGridPosition(entity, x, y, assertEntityOnGridPosition(entity));
		if (unset) {
			unset(entity);
		}
		eachEntityGridPosition(entity, x, y, set(entity));
		entity.setRegisteredGridPosition(x, y);
	}

	/**
	 * TODO unschön alles hier ... blub
	 */
	private Consumer<GridPosition> assertEntityOnGridPosition(Entity entity) {
		return (gridPosition) -> {
			short x = gridPosition.getGridX();
			short y = gridPosition.getGridY();
			Entity entityOnPosition = get(x, y);
			if (entityOnPosition != null && !entityOnPosition.equals(entity)) {
				throw new IllegalStateException(
						"Try to move (entity id: " + entity.getId() + ") to already used tile (entity id: "+entityOnPosition.getId()+") (" + x + "|" + y + ").");
			}
		};
	}

	private Consumer<GridPosition> set(Entity entity) {
		return gridPosition -> this.entities[index(gridPosition.getGridX(), gridPosition.getGridY())] = entity;
	}

	public void unset(Entity entity) {
		eachEntityGridPosition(entity, this::unset);
	}

	public void unset(GridPosition gridPosition) {
		this.entities[index(gridPosition.getGridX(), gridPosition.getGridY())] = null;
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

	public Entity get(short x, short y) {
		return this.entities[index(x, y)];
	}

	public Entity get(GridPosition gridPosition) {
		return get(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public boolean isEmpty(short x, short y) {
		return get(x, y) == null;
	}

	public Set<Entity> getAllEntities() {
		Set<Entity> result = new HashSet<>();
		for (Entity entity : this.entities) {
			if (entity != null) {
				result.add(entity);
			}
		}
		return result;
	}

}
