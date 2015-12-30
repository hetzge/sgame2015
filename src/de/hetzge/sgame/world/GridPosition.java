package de.hetzge.sgame.world;

import java.io.Serializable;

import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;

public class GridPosition implements IF_GridPosition, IF_GridEntity, Serializable {

	private short x = (short) 0;
	private short y = (short) 0;

	public GridPosition() {
	}

	public GridPosition(GridPosition gridPosition) {
		this(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public GridPosition(short x, short y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public short getGridX() {
		return this.x;
	}

	@Override
	public short getGridY() {
		return this.y;
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
		return new GridPosition((short) (getGridX() + orientation.getOffsetX()),
				(short) (getGridY() + orientation.getOffsetY()));
	}

	public float getCenteredRealX() {
		return this.x * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
	}

	public float getCenteredRealY() {
		return this.y * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
	}

	public float distanceTo(GridPosition other) {
		float a = Math.abs(this.x - other.x);
		float b = Math.abs(this.y - other.y);
		return (float) Math.sqrt(a * a + b * b);
	}

	@Override
	public short getRegisteredX() {
		return getGridX();
	}

	@Override
	public short getRegisteredY() {
		return getGridY();
	}

	@Override
	public short getWidth() {
		return 1;
	}

	@Override
	public short getHeight() {
		return 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.x;
		result = prime * result + this.y;
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
		if (this.x != other.x)
			return false;
		if (this.y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GridPosition [x=" + this.x + ", y=" + this.y + "]";
	}

}
