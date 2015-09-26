package de.hetzge.sgame.entity;

import java.io.Serializable;
import java.util.Comparator;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.graphic.IF_GraphicKey;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.world.IF_GridEntity;

public class Entity implements IF_GraphicKey, IF_GridEntity, Serializable {

	private int id;

	/*-
	 * The x and y position is used in different ways:
	 * moveable 	-> x is the position on the world
	 * non moveable -> x is the grid position on the world 
	 */
	private float x;
	private float y;

	private short registeredX;
	private short registeredY;

	private byte orientation = 0;
	private byte activity = 0;
	private byte entityType;

	private byte owner;

	private short nextWaypointPointer;
	private short[] pathXs;
	private short[] pathYs;

	private byte item = -1;

	private EntityJob job;

	public Entity(int id, byte owner, E_EntityType entityType) {
		this.id = id;
		this.entityType = (byte) entityType.ordinal();
		this.owner = owner;
	}

	public void setWorldX(short x) {
		if (getDefinition().isMoveable()) {
			this.x = x * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
		} else {
			this.x = x;
		}
	}

	public void setWorldY(short y) {
		if (getDefinition().isMoveable()) {
			this.y = y * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
		} else {
			this.y = y;
		}
	}

	@Override
	public short getWidth() {
		return getDefinition().getWidth();
	}

	@Override
	public short getHeight() {
		return getDefinition().getHeight();
	}

	public float getRenderX() {
		return getWorldX() - getWidth() * Constant.HALF_TILE_SIZE;
	}

	public float getRenderY() {
		return getWorldY() - getWidth() * Constant.TILE_SIZE;
	}

	public float getWorldX() {
		if (getDefinition().isMoveable()) {
			return x;
		} else {
			return getGridX() * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
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

	public void setRegisteredGridPosition(short x, short y) {
		registeredX = x;
		registeredY = y;
	}

	@Override
	public short getRegisteredX() {
		return registeredX;
	}

	@Override
	public short getRegisteredY() {
		return registeredY;
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

	/*- #################################################
	 * The door is a place around the entity where things can be delivered. 
	 */
	public short getDoorX() {
		EntityDefinition definition = getDefinition();
		short doorOffsetX = definition.getDoorOffsetX();
		return (short) (getGridX() + doorOffsetX);
	}

	public short getDoorY() {
		EntityDefinition definition = getDefinition();
		short doorOffsetY = definition.getDoorOffsetY();
		return (short) (getGridY() + doorOffsetY);
	}
	/*
	 * ##################################################
	 */

	/*- #################################################
	 * A job represents the ki of a entity and stores job specific data.
	 */
	public EntityJob getJob() {
		return job;
	}

	public void popJob() {
		if (job != null) {
			job.pop();
		}
	}
	/*
	 * ##################################################
	 */

	/*- #################################################
	 * A entity can carry a single item at a moment which is represented by the item ordinal.
	 */
	public boolean hasItem() {
		return item != -1;
	}

	public E_Item getItem() {
		return E_Item.values[item];
	}

	public void setItem(E_Item item) {
		if (item == null) {
			this.item = -1;
		} else {
			this.item = (byte) item.ordinal();
		}
	}
	/*
	 * ##################################################
	 */

	/*- #################################################
	 * A entity can have a path where it can walk along.
	 */
	public float getNextWorldX() {
		return getNextX() * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
	}

	public float getNextWorldY() {
		return getNextY() * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
	}

	public short getNextX() {
		return pathXs[nextWaypointPointer];
	}

	public short getNextY() {
		return pathYs[nextWaypointPointer];
	}

	public short getPathGoalX() {
		return pathXs[pathXs.length - 1];
	}

	public short getPathGoalY() {
		return pathYs[pathYs.length - 1];
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

	public void setPath(short[] pathXs, short[] pathYs) {
		this.pathXs = pathXs;
		this.pathYs = pathYs;
		nextWaypointPointer = 0;
	}

	public E_Orientation getOrientationToNext() {
		int worldX = (int) getWorldX();
		int worldY = (int) getWorldY();
		int nextWorldX = (int) getNextWorldX();
		int nextWorldY = (int) getNextWorldY();
		return E_Orientation.orientationTo(worldX, worldY, nextWorldX, nextWorldY);
	}
	/*
	 * ##################################################
	 */

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
		float frameDelta = App.timing.getFrameDelta();
		x += orientation.getOffsetX() * speed * frameDelta;
		y += orientation.getOffsetY() * speed * frameDelta;
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

	public static class IdComparator implements Comparator<Entity> {
		@Override
		public int compare(Entity e1, Entity e2) {
			return Integer.compare(e1.getId(), e2.getId());
		}
	}

}
