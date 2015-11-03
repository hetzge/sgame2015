package de.hetzge.sgame.function;

import java.util.function.Predicate;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.Job;
import de.hetzge.sgame.entity.job.main.IF_ProviderJob;
import de.hetzge.sgame.function.EntityFunction.EntityPredicate;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.world.ContainerGrid;
import de.hetzge.sgame.world.GridPosition;

public interface OnMapPredicate extends Predicate<GridPosition> {

	/**
	 * Tests if on a {@link GridPosition} is a {@link Container} which provides
	 * a given {@link E_Item}. Checks {@link Entity} {@link Job}s and
	 * {@link Container} on the {@link ContainerGrid}.
	 */
	public static class ProvideItemAvailablePredicate implements OnMapPredicate {

		private final E_Item item;
		private final EntityPredicate entityPredicate;

		private Container provideContainer;

		public ProvideItemAvailablePredicate(E_Item item) {
			this.item = item;
			this.entityPredicate = entity -> true;
		}

		public ProvideItemAvailablePredicate(E_Item item, EntityPredicate entityPredicate) {
			this.item = item;
			this.entityPredicate = entityPredicate;
		}

		@Override
		public boolean test(GridPosition gridPosition) {
			Entity entity = App.game.getEntityGrid().get(gridPosition);
			if (entity != null) {
				if (entityPredicate.test(entity)) {
					EntityJob job = entity.getJob();
					if (job instanceof IF_ProviderJob) {
						IF_ProviderJob if_ProviderJob = (IF_ProviderJob) job;
						Container container = if_ProviderJob.getProviderJob().getProvides();
						if (container.hasAmountAvailable(this.item, 1)) {
							provideContainer = container;
							return true;
						}
					}
				}
			}

			ContainerGrid containerGrid = App.game.getWorld().getContainerGrid();
			boolean hasItemAvailable = containerGrid.hasItemAvailable(gridPosition, this.item);
			if (hasItemAvailable) {
				provideContainer = containerGrid.get(gridPosition);
			}
			return hasItemAvailable;
		}

		public Container getProvideContainer() {
			return provideContainer;
		}

	}

	/**
	 * Tests a entity (only if exist) on a {@link GridPosition} against a given
	 * {@link EntityPredicate}.
	 */
	public static class EntityOnMapPredicate implements OnMapPredicate {

		private final EntityPredicate entityPredicate;

		private Entity entity;

		public EntityOnMapPredicate(EntityPredicate entityPredicate) {
			this.entityPredicate = entityPredicate;
		}

		@Override
		public boolean test(GridPosition gridPosition) {
			Entity entity = App.game.getEntityGrid().get(gridPosition);
			if (entity != null) {
				if (entityPredicate.test(entity)) {
					this.entity = entity;
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		public Entity getEntity() {
			return entity;
		}
	}

}