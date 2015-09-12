package de.hetzge.sgame.entity;

import java.io.Serializable;

import com.badlogic.gdx.Gdx;

import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.graphic.IF_GraphicKey;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;

public class Entity implements IF_GraphicKey, Serializable {

	private int id;

	/*-
	 * The x and y position is used in different ways:
	 * moveable 	-> x is the position on the world
	 * non moveable -> x is the grid position on the world 
	 */
	private float x;
	private float y;

	private byte orientation;
	private byte activity;
	private byte entityType;

	private byte owner;

	private short nextWaypointPointer;
	private short[] pathXs;
	private short[] pathYs;

	public Entity(int id, byte owner, E_EntityType entityType) {
		this.id = id;
		this.entityType = (byte) entityType.ordinal();
		this.owner = owner;
	}

	public float getRenderX() {
		return getWorldX() - getDefinition().getWidth() * Constant.TILE_SIZE / 2;
	}

	public float getRenderY() {
		return getWorldY() - getDefinition().getHeight() * Constant.TILE_SIZE;
	}

	public float getWorldX() {
		if (getDefinition().isMoveable()) {
			return x;
		} else {
			return getGridX() * Constant.TILE_SIZE + Constant.TILE_SIZE / 2;
		}
	}

	public float getWorldY() {
		if (getDefinition().isMoveable()) {
			return y;
		} else {
			return getGridY() * Constant.TILE_SIZE + Constant.TILE_SIZE;
		}
	}

	public short getGridX() {
		if (getDefinition().isMoveable()) {
			return (short) (getWorldX() / Constant.TILE_SIZE);
		} else {
			return (short) x;
		}
	}

	public short getGridY() {
		if (getDefinition().isMoveable()) {
			return (short) (getWorldY() / Constant.TILE_SIZE);
		} else {
			return (short) y;
		}
	}

	public boolean isOwnedByGaia() {
		return owner == Constant.GAIA_PLAYER_ID;
	}

	public byte getOwner() {
		return owner;
	}

	public void setOwner(byte owner) {
		this.owner = owner;
	}

	public int getId() {
		return id;
	}

	public short getNextX() {
		return pathXs[nextWaypointPointer];
	}

	public short getNextY() {
		return pathXs[nextWaypointPointer];
	}

	public void nextWaypoint() {
		if (nextWaypointPointer + 1 < pathXs.length) {
			nextWaypointPointer++;
		}
	}

	public boolean isNextEndOfPath() {
		return nextWaypointPointer == pathXs.length - 1;
	}

	public boolean hasPath() {
		return pathXs != null && pathYs != null;
	}

	public void unsetPath() {
		nextWaypointPointer = 0;
		pathXs = null;
		pathYs = null;
	}

	public short[] getPathXs() {
		return pathXs;
	}

	public void setPathXs(short[] pathXs) {
		this.pathXs = pathXs;
	}

	public short[] getPathYs() {
		return pathYs;
	}

	public void setPathYs(short[] pathYs) {
		this.pathYs = pathYs;
	}

	public EntityDefinition getDefinition() {
		return E_EntityType.values[entityType].getEntityDefinition();
	}

	@Override
	public E_Orientation getOrientation() {
		return E_Orientation.values[orientation];
	}

	public IF_GraphicKey setOrientation(E_Orientation orientation) {
		this.orientation = (byte) orientation.ordinal();
		return this;
	}

	@Override
	public E_Activity getActivity() {
		return E_Activity.values[activity];
	}

	public IF_GraphicKey setActivity(E_Activity activity) {
		this.activity = (byte) activity.ordinal();
		return this;
	}

	@Override
	public E_EntityType getEntityType() {
		return E_EntityType.values[entityType];
	}

	public IF_GraphicKey setEntityType(E_EntityType entityType) {
		this.entityType = (byte) entityType.ordinal();
		return this;
	}

	public void move(E_Orientation orientation) {
		setOrientation(orientation);
		float speed = getDefinition().getSpeed();
		float deltaTime = Gdx.graphics.getDeltaTime();
		x += orientation.getOffsetX() * speed * deltaTime;
		y += orientation.getOffsetY() * speed * deltaTime;
	}

	@Override
	public int hashGraphicKey() {
		final int prime = 31;
		int result = 1;
		result = prime * result + activity;
		result = prime * result + entityType;
		result = prime * result + orientation;
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Entity other = (Entity) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
