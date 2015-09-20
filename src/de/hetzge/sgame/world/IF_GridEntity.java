package de.hetzge.sgame.world;

public interface IF_GridEntity {

	short getRegisteredX();

	short getRegisteredY();

	short getWidth();

	short getHeight();

	public default int getGridOffsetX() {
		short width = getWidth();
		return width % 2 == 0 ? width / 2 : (width - 1) / 2;
	}

	public default int getGridOffsetY() {
		short height = getHeight();
		return height % 2 == 0 ? height / 2 : (height - 1) / 2;
	}

}
