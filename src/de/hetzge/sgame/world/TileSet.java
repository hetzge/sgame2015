package de.hetzge.sgame.world;

import java.io.File;

import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.Util;

/**
 * Describes a texture tile set.
 */
public class TileSet {

	private final E_Ground[] grounds;
	private final File file;

	protected int tileSize = Constant.TILE_SIZE;
	protected int border = 0;
	protected int space = 0;
	protected int width = 10;
	protected int height = 10;

	public TileSet(File file) {
		this.file = file;
		this.grounds = new E_Ground[this.width * this.height];
		for (int i = 0; i < this.grounds.length; i++) {
			this.grounds[i] = E_Ground.DEFAULT;
		}
	}

	public File getFile() {
		return this.file;
	}

	public void setGround(E_Ground ground, int x, int y) {
		setGround(ground, Util.index(x, y, this.width));
	}

	public void setGround(E_Ground ground, int index) {
		this.grounds[index] = ground;
	}

	public E_Ground getGround(int x, int y) {
		return getGround(Util.index(x, y, this.width));
	}

	public E_Ground getGround(int index) {
		return this.grounds[index];
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getBorder() {
		return this.border;
	}

	public int getSpace() {
		return this.space;
	}

	public int getTileSize() {
		return this.tileSize;
	}

}
