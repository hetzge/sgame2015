package de.hetzge.sgame.entity.job.sub;

import de.hetzge.sgame.App;
import de.hetzge.sgame.booking.Booking;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.main.IF_ItemJob;
import de.hetzge.sgame.frame.FrameModule;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.item.GridEntityContainer;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.world.GridPosition;

public class MineSubJob extends EntityJob {

	private final int finishMineFrameId;
	private final IF_ItemJob itemJob;

	public MineSubJob(Entity entity, IF_ItemJob itemJob) {
		super(entity);
		this.itemJob = itemJob;
		Booking<E_Item> booking = itemJob.getBooking();
		GridPosition entityGridPosition = entity.getGridPosition();
		GridPosition mineFromGridPosition = booking.<GridEntityContainer> getFrom().getEntity().getGridPosition();
		E_Orientation orientationTo = E_Orientation.orientationTo(entityGridPosition, mineFromGridPosition);
		entity.setOrientation(orientationTo);
		entity.setActivity(E_Activity.WORKING);
		this.finishMineFrameId = FrameModule.instance.getNextFrameId(Constant.DEFAULT_MINE_TIME_IN_FRAMES);
	}

	@Override
	protected void work() {
		if (FrameModule.instance.isCurrentOrPast(this.finishMineFrameId)) {
			GridEntityContainer fromContainerBefore = this.itemJob.getBooking().<GridEntityContainer> getFrom();
			this.itemJob.takeItem();
			destroyMineProviderIfEmpty(fromContainerBefore);
			this.entity.popJob();
		}
	}

	private void destroyMineProviderIfEmpty(GridEntityContainer fromContainer) {
		boolean isFromContainerEmpty = fromContainer.isEmpty();
		if (isFromContainerEmpty) {
			Entity fromEntity = fromContainer.getEntity();
			if (fromEntity != null) {
				App.entityFunction.destroyEntity(fromEntity);
			}
		}
	}

}
