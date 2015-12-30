package de.hetzge.sgame.item;

import de.hetzge.sgame.booking.IF_Item;
import de.hetzge.sgame.world.IF_GridEntity;

public class GridEntityContainerWithoutLimit extends GridEntityContainer {

	public GridEntityContainerWithoutLimit(IF_GridEntity gridEntity) {
		super(gridEntity);
	}

	@Override
	public synchronized boolean can(IF_Item item) {
		return true;
	}

	@Override
	public synchronized boolean canAddAmount(IF_Item item, int amount) {
		return true;
	}

}
