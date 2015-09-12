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

	public void set(short x, short y, Entity entity) {
		if (get(x, y) != null) {
			throw new InvalidGameStateException("Try to move to already used tile.");
		}
		entities[index(x, y)] = entity;
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

	public void remove(short x, short y) {
		entities[index(x, y)] = null;
	}

	public Entity get(short x, short y) {
		return entities[index(x, y)];
	}

	public boolean isEmpty(short x, short y) {
		return get(x, y) == null;
	}
	
	public List<Entity> getAllEntities(){
		List<Entity> result = new LinkedList<>();
		for (Entity entity : entities) {
			if(entity != null){
				result.add(entity);
			}
		}
		return result;
	}

}
