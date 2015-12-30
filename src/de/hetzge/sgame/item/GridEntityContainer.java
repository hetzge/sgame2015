package de.hetzge.sgame.item;

import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.world.IF_GridEntity;

public class GridEntityContainer extends Container<E_Item> implements IF_GridEntityContainer {

	private final IF_GridEntity gridEntity;

	public GridEntityContainer(IF_GridEntity gridEntity) {
		this.gridEntity = gridEntity;
	}

	@Override
	public IF_GridEntity getObject() {
		return this.gridEntity;
	}

	/**
	 * TODO unsauber ... 
	 */
	@Override
	public Entity getEntity() {
		if (this.gridEntity instanceof Entity) {
			return (Entity) this.gridEntity;
		} else {
			throw new InvalidGameStateException();
		}
	}

}
