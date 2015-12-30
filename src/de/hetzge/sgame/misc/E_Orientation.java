package de.hetzge.sgame.misc;

import java.util.Arrays;
import java.util.List;

import de.hetzge.sgame.world.GridPosition;

public enum E_Orientation {

	SOUTH((short) 0, (short) 1), WEST((short) -1, (short) 0), NORTH((short) 0, (short) -1), EAST((short) 1, (short) 0);

	public final static E_Orientation[] values = values();
	public final static List<E_Orientation> orientations = Arrays.asList(values);

	private final short offsetX;
	private final short offsetY;
	
	private E_Orientation(short offsetX, short offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public short getOffsetX() {
		return this.offsetX;
	}

	public short getOffsetY() {
		return this.offsetY;
	}

	public static E_Orientation orientationTo(GridPosition from, GridPosition to) {
		return orientationTo(from.getGridX(), from.getGridY(), to.getGridX(), to.getGridY());
	}

	public static E_Orientation orientationTo(int fromX, int fromY, int toX, int toY) {
		int distanceX = fromX - toX;
		int distanceY = fromY - toY;

		if (Math.abs(distanceX) > Math.abs(distanceY)) {
			if (distanceX > 0) {
				return E_Orientation.WEST;
			} else {
				return E_Orientation.EAST;
			}
		} else {
			if (distanceY > 0) {
				return E_Orientation.NORTH;
			} else {
				return E_Orientation.SOUTH;
			}
		}
	}

}
