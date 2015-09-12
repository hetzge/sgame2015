package de.hetzge.sgame.world;

import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;

public class GridPosition implements IF_GridPosition {

	private short x = (short) 0;
	private short y = (short) 0;

	public GridPosition() {
	}

	public GridPosition(short x, short y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public short getGridX() {
		return x;
	}

	@Override
	public short getGridY() {
		return y;
	}

	public GridPosition setX(short x) {
		this.x = x;
		return this;
	}

	public GridPosition setY(short y) {
		this.y = y;
		return this;
	}

	public GridPosition set(short x, short y) {
		setX(x);
		setY(y);
		return this;
	}

	public GridPosition getAround(E_Orientation orientation) {
		return new GridPosition((short) (getGridX() + orientation.getOffsetX()), (short) (getGridY() + orientation.getOffsetY()));
	}

	public float getCenteredRealX() {
		return x * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
	}

	public float getCenteredRealY() {
		return y * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GridPosition other = (GridPosition) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GridPosition [x=" + x + ", y=" + y + "]";
	}

}
