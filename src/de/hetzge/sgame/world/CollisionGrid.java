package de.hetzge.sgame.world;

import java.io.Serializable;
import java.util.BitSet;

import de.hetzge.sgame.error.InvalidGameStateException;

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
		return width;
	}

	@Override
	public short getHeight() {
		return height;
	}

	public void set(short x, short y) {
		collision.set(index(x, y));
	}

	public void unset(short x, short y) {
		collision.clear(index(x, y));
	}

	public boolean is(short x, short y) {
		return collision.get(index(x, y));
	}

	public boolean is(GridPosition gridPosition) {
		return is(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public boolean is(GridPosition gridPosition, short width, short height) {
		return is(gridPosition.getGridX(), gridPosition.getGridY(), width, height);
	}

	public boolean is(short x, short y, short width, short height) {
		int xOffset = width % 2 == 0 ? width / 2 : (width - 1) / 2;
		int yOffset = height % 2 == 0 ? height / 2 : (height - 1) / 2;
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
			throw new InvalidGameStateException("Try to set collision where already is collision.");
		}
		int xOffset = width % 2 == 0 ? width / 2 : (width - 1) / 2;
		int yOffset = height % 2 == 0 ? height / 2 : (height - 1) / 2;
		for (short xi = 0; xi < width; xi++) {
			for (short yi = 0; yi < height; yi++) {
				short posX = (short) (x - xOffset + xi);
				short posY = (short) (y - yOffset - yi);
				if (isOnGrid(posX, posY)) {
					set(posX, posY);
				}
			}
		}
	}

	public void unsetCollision(short x, short y, short width, short height) {
		int xOffset = width % 2 == 0 ? width / 2 : (width - 1) / 2;
		int yOffset = height % 2 == 0 ? height / 2 : (height - 1) / 2;
		for (short xi = 0; xi < width; xi++) {
			for (short yi = 0; yi < height; yi++) {
				short posX = (short) (x - xOffset + xi);
				short posY = (short) (y - yOffset - yi);
				if (isOnGrid(posX, posY)) {
					unset(posX, posY);
				}
			}
		}
	}

}