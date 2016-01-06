package de.hetzge.sgame.world;

import java.io.Serializable;

public class TileGrid implements IF_Grid, Serializable {

	private final short width;
	private final short height;

	private final short[] tiles;

	public TileGrid(short width, short height) {
		this.width = width;
		this.height = height;
		this.tiles = new short[width * height];
	}

	@Override
	public short getWidth() {
		return this.width;
	}

	@Override
	public short getHeight() {
		return this.height;
	}

	public void set(short[] values) {
		if (values.length != this.tiles.length) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < values.length; i++) {
			set(i, values[i]);
		}
	}

	public void set(int index, short value) {
		this.tiles[index] = value;
	}

	public void set(short x, short y, short value) {
		set(index(x, y), value);
	}

	public short get(int index) {
		return this.tiles[index];
	}

	public short get(short x, short y) {
		return get(index(x, y));
	}

}
