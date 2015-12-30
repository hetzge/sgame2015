package de.hetzge.sgame.entity.job.main;

import java.util.Set;

import de.hetzge.sgame.App;
import de.hetzge.sgame.booking.Booking;
import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.function.EntityFunction.EntityPredicate;
import de.hetzge.sgame.function.OnMapPredicate.EntityOnMapPredicate;
import de.hetzge.sgame.function.OnMapPredicate.ProvideItemAvailablePredicate;
import de.hetzge.sgame.item.E_Item;

public class ConsumerJob extends EntityJob implements IF_ConsumerJob {

	protected final Container<E_Item> needs;

	public ConsumerJob(Entity entity) {
		super(entity);
		this.needs = entity.getDefinition().createDefaultNeedContainer(entity);
	}

	@Override
	protected void work() {
		Set<E_Item> items = this.needs.getItems();

		int foundItemCount = 0;
		for (E_Item item : items) {
			int missingAmount = this.needs.getMissingAmount(item);
			if (missingAmount > 0) {
				EntityPredicate searchCarrierPredicate = searchEntity -> {
					if (searchEntity.isEntityType(E_EntityType.CARRIER)) {
						CarrierJob carrierJob = getCarrierJob(searchEntity);
						boolean hasNoBooking = !carrierJob.hasBooking();
						if (hasNoBooking) {
							return true;
						}
					}
					return false;
				};
				ProvideItemAvailablePredicate provideItemAvailablePredicate = new ProvideItemAvailablePredicate(item);
				EntityOnMapPredicate entityOnMapPredicate = new EntityOnMapPredicate(searchCarrierPredicate);

				App.entityFunction.iterateMap(this.entity, provideItemAvailablePredicate);
				App.entityFunction.iterateMap(this.entity, entityOnMapPredicate);

				Container<E_Item> provideContainer = provideItemAvailablePredicate.getProvideContainer();
				Entity carrierEntity = entityOnMapPredicate.getEntity();

				if (provideContainer != null && carrierEntity != null) {
					if (provideContainer.hasAmountAvailable(item, 1)) {
						Booking<E_Item> booking = provideContainer.book(item, 1, this.needs);
						CarrierJob carrierJob = getCarrierJob(carrierEntity);
						carrierJob.setBooking(booking);
						foundItemCount++;
					}
				}
			}
		}
		if (foundItemCount == 0) {
			pauseMedium();
		}
	}

	private CarrierJob getCarrierJob(Entity carrierEntity) {
		EntityJob carrierEntityJob = carrierEntity.getJob();
		if (carrierEntityJob instanceof CarrierJob) {
			CarrierJob carrierJob = (CarrierJob) carrierEntityJob;
			return carrierJob;
		} else {
			throw new InvalidGameStateException();
		}
	}

	public Container<E_Item> getNeeds() {
		return this.needs;
	}

	@Override
	public ConsumerJob getConsumerJob() {
		return this;
	}

}
