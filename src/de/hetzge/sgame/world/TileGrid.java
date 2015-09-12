package de.hetzge.sgame.world;

import java.io.Serializable;

public class TileGrid implements IF_Grid, Serializable {

	private final short width;
	private final short height;

	private final short[] tiles; 
	
	public TileGrid(short width, short height) {
		this.width = width;
		this.height = height;
		
		tiles = new short[width * height];
		
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = (short) (Math.random() * 200);
		}
		
	}

	@Override
	public short getWidth() {
		return width;
	}

	@Override
	public short getHeight() {
		return height;
	}
	
	public void set(short x, short y, short value) {
		tiles[index(x, y)] = value;
	}

	public short get(short x, short y) {
		return tiles[index(x, y)];
	}

}
