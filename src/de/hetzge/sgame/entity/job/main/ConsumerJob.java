package de.hetzge.sgame.entity.job.main;

import java.util.Set;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.function.EntityFunction.SearchPredicate;
import de.hetzge.sgame.item.Booking;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.E_Item;

public class ConsumerJob extends EntityJob implements IF_ConsumerJob {

	protected final Container needs;

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
				// search for item
				final SearchResult searchResult = new SearchResult();
				SearchPredicate searchPredicate = searchEntity -> {
					if (!searchResult.hasCarrier()) {
						if (searchEntity.isEntityType(E_EntityType.CARRIER)) {
							CarrierJob carrierJob = getCarrierJob(searchEntity);
							boolean hasNoBooking = !carrierJob.hasBooking();
							if (hasNoBooking) {
								searchResult.carrier = searchEntity;
								if (searchResult.isComplete()) {
									return true;
								}
							}
						}
					}
					if (!searchResult.hasBooking()) {
						EntityJob job = searchEntity.getJob();
						if (job instanceof IF_ProviderJob) {
							IF_ProviderJob if_ProviderJob = (IF_ProviderJob) job;
							ProviderJob providerJob = if_ProviderJob.getProviderJob();
							Container provides = providerJob.getProvides();
							if (provides.has(item)) {
								Booking booking = provides.book(item, 1, this.needs);
								searchResult.booking = booking;
								if (searchResult.isComplete()) {
									return true;
								}
							}
						}
					}
					return false;
				};
				App.entityFunction.findEntity(this.entity, searchPredicate);
				if (searchResult.isComplete()) {
					CarrierJob carrierJob = getCarrierJob(searchResult.carrier);
					carrierJob.setBooking(searchResult.booking);
					foundItemCount++;
				} else if (!searchResult.isComplete() && searchResult.hasBooking()) {
					searchResult.booking.rollback();
				}
			}
		}
		if (foundItemCount == 0) {
			pauseVeryLong();
		}

		// TODO search world container

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

	public Container getNeeds() {
		return this.needs;
	}

	private static class SearchResult {
		private Booking booking;
		private Entity carrier;

		public boolean hasBooking() {
			return this.booking != null;
		}

		public boolean hasCarrier() {
			return this.carrier != null;
		}

		public boolean isComplete() {
			return hasBooking() && hasCarrier();
		}
	}

	@Override
	public ConsumerJob getConsumerJob() {
		return this;
	}

}
