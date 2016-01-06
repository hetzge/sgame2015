package de.hetzge.sgame.world;

import java.io.Serializable;
import java.util.BitSet;

import de.hetzge.sgame.misc.Util;

public class CollisionGrid implements IF_Grid, Serializable {

	private final BitSet collision;
	private final short width;
	private final short height;

	public CollisionGrid(short width, short height) {
		this.collision = new BitSet(width * height);
		this.width = width;
		this.height = height;
	}

	@Override
	public short getWidth() {
		return this.width;
	}

	@Override
	public short getHeight() {
		return this.height;
	}

	public void set(boolean[] values) {
		if (values.length != this.width * this.height) {
			throw new IllegalArgumentException(
					"parameter values has not expected length: " + values.length + " from " + this.width * this.height);
		}
		for (int i = 0; i < values.length; i++) {
			if (values[i]) {
				set(i);
			}
		}
	}

	public void set(int index) {
		this.collision.set(index);
	}

	public void set(short x, short y) {
		set(x, y);
	}

	public void set(GridPosition gridPosition) {
		set(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public void unset(GridPosition gridPosition) {
		unset(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public void unset(short x, short y) {
		unset(index(x, y));
	}

	public void unset(int index) {
		this.collision.clear(index);
	}

	public boolean is(int index) {
		return this.collision.get(index);
	}

	public boolean is(short x, short y) {
		return is(index(x, y));
	}

	public boolean is(GridPosition gridPosition) {
		return is(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public boolean is(GridPosition gridPosition, short width, short height) {
		return is(gridPosition.getGridX(), gridPosition.getGridY(), width, height);
	}

	public boolean is(short x, short y, short width, short height) {
		int xOffset = Util.offset(width);
		int yOffset = Util.offset(height);
		for (short xi = 0; xi < width; xi++) {
			for (short yi = 0; yi < height; yi++) {
				short posX = (short) (x - xOffset + xi);
				short posY = (short) (y - yOffset - yi);
				if (!isOnGrid(posX, posY) || is(posX, posY)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setCollision(short x, short y, short width, short height) {
		if (is(x, y, width, height)) {
			throw new IllegalStateException("Try to set collision where already is collision.");
		}
		eachEntityGridPosition(new GridEntity(x, y, width, height), this::set);
	}

	public void unsetCollision(IF_GridEntity gridEntity) {
		eachEntityGridPosition(gridEntity, this::unset);
	}

	public void unsetCollision(short x, short y, short width, short height) {
		unsetCollision(new GridEntity(x, y, width, height));
	}

}