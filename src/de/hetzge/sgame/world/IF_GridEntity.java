package de.hetzge.sgame.world;

import de.hetzge.sgame.misc.Util;

public interface IF_GridEntity {

	short getRegisteredX();

	short getRegisteredY();

	short getWidth();

	short getHeight();

	public default GridPosition getDoorGridPosition() {
		return getRegisteredGridPosition();
	}

	public default GridPosition getRegisteredGridPosition() {
		return new GridPosition(getRegisteredX(), getRegisteredY());
	}

	public default int getGridOffsetX() {
		short width = getWidth();
		return Util.offset(width);
	}

	public default int getGridOffsetY() {
		short height = getHeight();
		return Util.offset(height);
	}

	public default boolean isEntityGrid(GridPosition gridPosition) {
		short width = getWidth();
		short height = getHeight();
		short x = getRegisteredX();
		short y = getRegisteredY();
		int xOffset = getGridOffsetX();
		int yOffset = getGridOffsetY();
		short gridX = gridPosition.getGridX();
		short gridY = gridPosition.getGridY();

		// TODO das geht auch effektiver
		for (short xi = 0; xi < width; xi++) {
			for (short yi = 0; yi < height; yi++) {
				short posX = (short) (x - xOffset + xi);
				short posY = (short) (y - yOffset - yi);
				if (gridX == posX && gridY == posY) {
					return true;
				}
			}
		}

		return false;
	}

}
