package de.hetzge.sgame.item;

import de.hetzge.sgame.booking.IF_Container;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.world.IF_GridEntity;

public interface IF_GridEntityContainer extends IF_Container<E_Item> {
	IF_GridEntity getObject();

	Entity getEntity();
}
