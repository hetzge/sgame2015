package de.hetzge.sgame.game.format;

import java.io.Serializable;

import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.item.E_Item;

/**
 * This class describes all basic information to create a new game. From this
 * informations a game will be initialized.
 */
public class GameFormat implements Serializable {

	private final int worldWidth;

	private final int worldHeight;

	/**
	 * The indexes of the tiles in the tileset
	 */
	private final short[] tiles;

	/**
	 * The fixed collision of the world
	 */
	private final boolean[] collision;

	/**
	 * The entities on the world
	 */
	private final EntityFormat[] entities;

	/**
	 * Container with items on the world
	 */
	private final ContainerFormat[][] containers;

	public GameFormat(int worldWidth, int worldHeight, short[] tiles, boolean[] collision, EntityFormat[] entityFormats,
			ContainerFormat[][] containers) {
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.tiles = tiles;
		this.collision = collision;
		this.entities = entityFormats;
		this.containers = containers;
	}

	public int getWorldWidth() {
		return this.worldWidth;
	}

	public int getWorldHeight() {
		return this.worldHeight;
	}

	public short[] getTiles() {
		return this.tiles;
	}

	public boolean[] getCollision() {
		return this.collision;
	}

	public EntityFormat[] getEntities() {
		return this.entities;
	}

	public ContainerFormat[][] getContainers() {
		return this.containers;
	}

	public static class ContainerFormat {
		
		/**
		 * The item of the container
		 */
		private final E_Item item;
		
		/**
		 * The count of the item in this container
		 */
		private final int count;

		public ContainerFormat(E_Item item, int count) {
			this.item = item;
			this.count = count;
		}

		public E_Item getItem() {
			return this.item;
		}

		public int getCount() {
			return this.count;
		}
	}

	public static class EntityFormat {

		/**
		 * The type of the entity
		 */
		private final E_EntityType entityType;

		/**
		 * The id of the owner of the entity
		 */
		private final byte owner;

		public EntityFormat(E_EntityType entityType, byte owner) {
			this.entityType = entityType;
			this.owner = owner;
		}

		public E_EntityType getEntityType() {
			return this.entityType;
		}

		public byte getOwner() {
			return this.owner;
		}
	}
}
