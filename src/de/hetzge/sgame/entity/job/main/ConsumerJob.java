package de.hetzge.sgame.entity.job.main;

import java.util.Set;
import java.util.function.Function;

import de.hetzge.sgame.App;
import de.hetzge.sgame.booking.Booking;
import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.world.ContainerGrid;
import de.hetzge.sgame.world.GridPosition;

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

				// ########################

				Function<Entity, Entity> searchCarrierFunction = entityToTest -> {
					if (entityToTest.isEntityType(E_EntityType.CARRIER)) {
						CarrierJob carrierJob = getCarrierJob(entityToTest);
						boolean hasBooking = carrierJob.hasBooking();
						if (!hasBooking) {
							return entityToTest;
						}
					}
					return null;
				};

				// ########################

				Function<GridPosition, Container<E_Item>> searchProviderContainer = gridPosition -> {
					boolean isOnGrid = App.game.getWorld().getFixedCollisionGrid().isOnGrid(gridPosition);
					if (!isOnGrid) {
						return null;
					}

					Entity entityToTest = getEntityOnPosition(gridPosition);
					if (entityToTest == null) {
						// check providing grid container
						ContainerGrid containerGrid = App.game.getWorld().getContainerGrid();
						boolean hasItemAvailable = containerGrid.hasItemAvailable(gridPosition, item);
						if (hasItemAvailable) {
							return containerGrid.get(gridPosition);
						} else {
							return null;
						}
					} else {
						// check providing entities
						EntityJob job = this.entity.getJob();
						if (job instanceof IF_ProviderJob) {
							IF_ProviderJob if_ProviderJob = (IF_ProviderJob) job;
							Container<E_Item> container = if_ProviderJob.getProviderJob().getProvides();
							if (container.hasAmountAvailable(item, 1)) {
								return container;
							}
						}
					}
					return null;
				};

				// #######################

				Container<E_Item> providerContainer = App.searchFunction.byGridPosition.find(this.entity,
						searchProviderContainer);
				if (providerContainer != null && providerContainer.hasAmountAvailable(item, 1)) {
					Entity carrierEntity = App.searchFunction.byEntity.find(this.entity, searchCarrierFunction);
					if (carrierEntity != null) {
						Booking<E_Item> booking = providerContainer.book(item, 1, this.needs);
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

	private Entity getEntityOnPosition(GridPosition gridPosition) {
		return App.game.getEntityGrid().get(gridPosition);
	}

	private CarrierJob getCarrierJob(Entity carrierEntity) {
		EntityJob carrierEntityJob = carrierEntity.getJob();
		if (carrierEntityJob instanceof CarrierJob) {
			return (CarrierJob) carrierEntityJob;
		} else {
			throw new IllegalStateException();
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
