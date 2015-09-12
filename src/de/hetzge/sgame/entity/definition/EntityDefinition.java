package de.hetzge.sgame.entity.definition;

public abstract class EntityDefinition {

	protected boolean moveable = false;
	protected short width = 1;
	protected short height = 1;
	protected int energie = 1000;
	protected int buildTime = 3000;
	protected float speed = 0.2f;

	public boolean isMoveable() {
		return moveable;
	}

	public short getWidth() {
		return width;
	}

	public short getHeight() {
		return height;
	}

	public int getEnergie() {
		return energie;
	}

	public int getBuildTime() {
		return buildTime;
	}

	public float getSpeed() {
		return speed;
	}

	public static class Dummy extends EntityDefinition {
		public Dummy() {
			moveable = true;
		}
	}

}
