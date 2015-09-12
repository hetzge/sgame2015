package de.hetzge.sgame.world;

import java.io.Serializable;

import de.hetzge.sgame.misc.Constant;

public class World implements IF_Grid, Serializable {

	private final short width;
	private final short height;

	private final CollisionGrid fixedCollisionMap;
	private final TileGrid tileGrid;

	public World(short width, short height) {
		this.width = width;
		this.height = height;

		this.fixedCollisionMap = new CollisionGrid(width, height);
		this.tileGrid = new TileGrid(width, height);
	}

	public short getWidth() {
		return width;
	}

	public short getHeight() {
		return height;
	}

	public CollisionGrid getFixedCollisionGrid() {
		return fixedCollisionMap;
	}

	public TileGrid getTileGrid() {
		return tileGrid;
	}

	public int getPixelWidth() {
		return width * Constant.TILE_SIZE;
	}

	public int getPixelHeight() {
		return height * Constant.TILE_SIZE;
	}

}
