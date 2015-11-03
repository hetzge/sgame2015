package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.item.Booking;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.ContainerWithoutLimit;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.Path;

public class CarrierJob extends EntityJob implements IF_ItemJob {

	private Booking booking;
	private final Container container;

	public CarrierJob(Entity entity) {
		super(entity);
		this.container = new ContainerWithoutLimit(entity);
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
							dropItem();
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
						takeItem();
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

	@Override
	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public Booking getBooking() {
		return this.booking;
	}

}
