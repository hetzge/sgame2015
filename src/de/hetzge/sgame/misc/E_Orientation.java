package de.hetzge.sgame.misc;

import java.util.Arrays;
import java.util.List;

import de.hetzge.sgame.world.GridPosition;

public enum E_Orientation {

	SOUTH((byte) 0, (byte) 1), WEST((byte) -1, (byte) 0), NORTH((byte) 0, (byte) -1), EAST((byte) 1, (byte) 0);

	public final static E_Orientation[] values = values();
	public final static List<E_Orientation> orientations = Arrays.asList(values);

	private final byte offsetX;
	private final byte offsetY;

	private E_Orientation(byte offsetX, byte offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public byte getOffsetX() {
		return this.offsetX;
	}

	public byte getOffsetY() {
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
