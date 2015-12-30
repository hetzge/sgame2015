package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.App;
import de.hetzge.sgame.booking.Booking;
import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.world.GridPosition;

public interface IF_ItemJob {

	Container<E_Item> getContainer();

	Booking<E_Item> getBooking();

	void setBooking(Booking<E_Item> booking);

	Entity getEntity();

	default void dropItem() {
		Booking<E_Item> booking = getBooking();
		Entity entity = getEntity();
		if (booking != null) {
			if (entity.hasItem()) {
				GridPosition gridPosition = entity.getGridPosition();
				Container<E_Item> worldContainer = App.game.getWorld().getContainerGrid().get(gridPosition);
				Booking<E_Item> bookingWithOtherTo = booking.createWithOtherTo(worldContainer);
				bookingWithOtherTo.transfer();
				setBooking(bookingWithOtherTo);
				entity.unsetItem();
			} else {
				booking.rollback();
			}
		}
	}

	default void takeItem() {
		Booking<E_Item> booking = getBooking();
		E_Item item = booking.getItem();
		Container<E_Item> entityContainer = getContainer();
		Entity entity = getEntity();

		Booking<E_Item> bookingWithOtherFrom = booking.createWithOtherFrom(entityContainer);
		setBooking(bookingWithOtherFrom);
		entity.setItem(item);
		entity.setActivity(E_Activity.CARRY);
	}

}
