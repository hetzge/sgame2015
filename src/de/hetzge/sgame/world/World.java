package de.hetzge.sgame.world;

import java.io.Serializable;

import de.hetzge.sgame.misc.Constant;

public class World implements IF_Grid, Serializable {

	private final short width;
	private final short height;

	private final CollisionGrid fixedCollisionMap;
	private final TileGrid tileGrid;
	private final ContainerGrid containerGrid;

	public World(short width, short height) {
		this.width = width;
		this.height = height;

		fixedCollisionMap = new CollisionGrid(width, height);
		tileGrid = new TileGrid(width, height);
		containerGrid = new ContainerGrid(width, height);
	}

	@Override
	public short getWidth() {
		return width;
	}

	@Override
	public short getHeight() {
		return height;
	}

	public CollisionGrid getFixedCollisionGrid() {
		return fixedCollisionMap;
	}

	public TileGrid getTileGrid() {
		return tileGrid;
	}

	public ContainerGrid getContainerGrid() {
		return containerGrid;
	}

	public int getPixelWidth() {
		return width * Constant.TILE_SIZE;
	}

	public int getPixelHeight() {
		return height * Constant.TILE_SIZE;
	}

}
