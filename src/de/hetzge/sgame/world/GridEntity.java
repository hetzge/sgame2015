package de.hetzge.sgame.world;

public class GridEntity implements IF_GridEntity {

	private short registeredX;
	private short registeredY;

	private short width;
	private short height;

	public GridEntity(short registeredX, short registeredY, short width, short height) {
		this.registeredX = registeredX;
		this.registeredY = registeredY;
		this.width = width;
		this.height = height;
	}

	@Override
	public short getRegisteredX() {
		return registeredX;
	}

	public void setRegisteredX(short registeredX) {
		this.registeredX = registeredX;
	}

	@Override
	public short getRegisteredY() {
		return registeredY;
	}

	public void setRegisteredY(short registeredY) {
		this.registeredY = registeredY;
	}

	@Override
	public short getWidth() {
		return width;
	}

	public void setWidth(short width) {
		this.width = width;
	}

	@Override
	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

}
