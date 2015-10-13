package de.hetzge.sgame.world;

public class SimpleGridEntity implements IF_GridEntity {

	private final short x;
	private final short y;

	public SimpleGridEntity(short x, short y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public short getRegisteredX() {
		return this.x;
	}

	@Override
	public short getRegisteredY() {
		return this.y;
	}

	@Override
	public short getWidth() {
		return 1;
	}

	@Override
	public short getHeight() {
		return 1;
	}

}
