package de.hetzge.sgame.world;

import java.io.Serializable;

import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;

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

	public byte getOwnership(GridPosition gridPosition) {
		return getOwnership(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public byte getOwnership(int x, int y) {
		return this.owners[index(x, y)];
	}

	public boolean isBorder(GridPosition gridPosition) {
		byte ownership = getOwnership(gridPosition);
		if (ownership == Constant.GAIA_PLAYER_ID) {
			return false;
		}

		for (E_Orientation orientation : E_Orientation.values) {
			GridPosition around = gridPosition.getAround(orientation);
			if (isOnGrid(around)) {
				byte aroundOwnership = getOwnership(around);
				if (aroundOwnership != ownership) {
					return true;
				}
			}
		}
		return false;
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
