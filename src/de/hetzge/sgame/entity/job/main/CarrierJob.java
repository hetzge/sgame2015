package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.item.Booking;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.Path;

public class CarrierJob extends EntityJob {

	private Booking booking;

	public CarrierJob(Entity entity) {
		super(entity);
	}

	@Override
	protected void work() {
		if (hasBooking()) {
			if (this.entity.hasPath()) {
				// walk
				pauseMedium();
			} else {
				GridPosition gridPosition = this.entity.getGridPosition();
				if (this.entity.hasItem()) {
					GridPosition toDoorGridPosition = this.booking.getTo().getObject().getDoorGridPosition();
					boolean reachedTo = gridPosition.equals(toDoorGridPosition);
					if (reachedTo) {
						// transfer
						this.booking.transfer();
						this.entity.unsetItem();
						unsetBooking();
					} else {
						// goto to
						Path path = App.entityFunction.findPath(this.entity, toDoorGridPosition);
						if (path != null) {
							this.entity.setPath(path);
						} else {
							// failed
							App.entityFunction.dropItem(this.entity, this.booking);
							unsetBooking();
							pauseLong();
						}
					}
				} else {
					// goto from
					Container from = this.booking.getFrom();
					GridPosition fromDoorGridPosition = from.getObject().getDoorGridPosition();
					boolean reachedFrom = gridPosition.equals(fromDoorGridPosition);
					if (reachedFrom) {
						this.booking.hide();
						E_Item item = this.booking.getItem();
						App.entityFunction.takeItem(this.entity, item);
					} else {
						// goto
						Path path = App.entityFunction.findPath(this.entity, fromDoorGridPosition);
						if (path != null) {
							this.entity.setPath(path);
						} else {
							// failed
							unsetBooking();
							this.entity.unsetItem();
							pauseLong();
						}
					}
				}
			}
		} else {
			pauseMedium();
		}
	}

	public void unsetBooking() {
		this.booking = null;
	}

	public boolean hasBooking() {
		return this.booking != null;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

}
