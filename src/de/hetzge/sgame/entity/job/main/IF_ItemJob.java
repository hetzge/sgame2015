package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.App;
import de.hetzge.sgame.booking.Booking;
import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.booking.IF_Item;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.world.GridPosition;

public interface IF_ItemJob {

	Container getContainer();

	Booking getBooking();

	void setBooking(Booking booking);

	Entity getEntity();

	default void dropItem() {
		Booking booking = getBooking();
		Entity entity = getEntity();
		if (booking != null) {
			if (entity.hasItem()) {
				GridPosition gridPosition = entity.getGridPosition();
				Container worldContainer = App.game.getWorld().getContainerGrid().get(gridPosition);
				Booking bookingWithOtherTo = booking.createWithOtherTo(worldContainer);
				bookingWithOtherTo.transfer();
				setBooking(bookingWithOtherTo);
				entity.unsetItem();
			} else {
				booking.rollback();
			}
		}
	}

	default void takeItem() {
		Booking booking = getBooking();
		IF_Item item = booking.getItem();
		Container entityContainer = getContainer();
		Entity entity = getEntity();

		Booking bookingWithOtherFrom = booking.createWithOtherFrom(entityContainer);
		setBooking(bookingWithOtherFrom);
		entity.setItem(item);
		entity.setActivity(E_Activity.CARRY);
	}

}
