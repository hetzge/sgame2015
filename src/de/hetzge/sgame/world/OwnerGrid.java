package de.hetzge.sgame.world;

import java.io.Serializable;

import de.hetzge.sgame.misc.Constant;

public class OwnerGrid implements IF_Grid, Serializable {

	private final short width;
	private final short height;

	private final byte[] owners;

	public OwnerGrid(short width, short height) {
		this.width = width;
		this.height = height;
		this.owners = new byte[width * height];

		for (int i = 0; i < this.owners.length; i++) {
			this.owners[i] = Constant.GAIA_PLAYER_ID;
		}
	}

	public void setOwnership(int x, int y, byte owner) {
		this.owners[index(x, y)] = owner;
	}

	public byte getOwnership(int x, int y) {
		return this.owners[index(x, y)];
	}

	@Override
	public short getWidth() {
		return this.width;
	}

	@Override
	public short getHeight() {
		return this.height;
	}

}
