package de.hetzge.sgame.entity.job.sub;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.item.Booking;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.world.GridPosition;

public class MineSubJob extends EntityJob {

	private final int finishMineFrameId;
	private final Booking booking;

	public MineSubJob(Entity entity, Booking booking) {
		super(entity);
		this.booking = booking;
		GridPosition entityGridPosition = entity.getGridPosition();
		GridPosition mineFromGridPosition = booking.getFrom().getEntity().getGridPosition();
		E_Orientation orientationTo = E_Orientation.orientationTo(entityGridPosition, mineFromGridPosition);
		entity.setOrientation(orientationTo);
		entity.setActivity(E_Activity.WORKING);
		this.finishMineFrameId = App.timing.getNextFrameId(Constant.DEFAULT_MINE_TIME_IN_FRAMES);
	}

	@Override
	protected void work() {
		if (App.timing.isCurrentOrPast(this.finishMineFrameId)) {
			E_Item mineItem = this.entity.getDefinition().getMineItem();
			App.entityFunction.takeItem(this.entity, mineItem);
			destroyMineProviderIfEmpty();
			this.entity.popJob();
		}
	}

	private void destroyMineProviderIfEmpty() {
		Container fromContainer = this.booking.getFrom();
		boolean isFromContainerEmpty = fromContainer.isEmpty();
		if (isFromContainerEmpty) {
			Entity fromEntity = fromContainer.getEntity();
			App.entityFunction.destroyEntity(fromEntity);
		}
	}

}
