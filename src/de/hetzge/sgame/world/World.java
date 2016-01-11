package de.hetzge.sgame.world;

import java.io.Serializable;

import de.hetzge.sgame.misc.Constant;

public class World implements IF_Grid, Serializable {

	private final short width;
	private final short height;

	private final CollisionGrid fixedCollisionMap;
	private final TileGrid tileGrid;
	private final ContainerGrid containerGrid;
	private final OwnerGrid ownerGrid;
	private final EntityGrid entityGrid;

	public World(short width, short height) {
		this.width = width;
		this.height = height;

		this.fixedCollisionMap = new CollisionGrid(width, height);
		this.tileGrid = new TileGrid(width, height);
		this.containerGrid = new ContainerGrid(width, height);
		this.ownerGrid = new OwnerGrid(width, height);
		this.entityGrid = new EntityGrid(width, height);
	}

	@Override
	public short getWidth() {
		return this.width;
	}

	@Override
	public short getHeight() {
		return this.height;
	}

	public CollisionGrid getFixedCollisionGrid() {
		return this.fixedCollisionMap;
	}

	public TileGrid getTileGrid() {
		return this.tileGrid;
	}

	public ContainerGrid getContainerGrid() {
		return this.containerGrid;
	}

	public OwnerGrid getOwnerGrid() {
		return this.ownerGrid;
	}

	public EntityGrid getEntityGrid() {
		return this.entityGrid;
	}

	public int getPixelWidth() {
		return this.width * Constant.TILE_SIZE;
	}

	public int getPixelHeight() {
		return this.height * Constant.TILE_SIZE;
	}

}
