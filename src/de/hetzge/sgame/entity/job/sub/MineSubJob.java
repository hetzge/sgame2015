package de.hetzge.sgame.entity.job.sub;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.main.IF_ItemJob;
import de.hetzge.sgame.frame.FrameModule;
import de.hetzge.sgame.item.Booking;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.world.GridPosition;

public class MineSubJob extends EntityJob {

	private final int finishMineFrameId;
	private final IF_ItemJob itemJob;

	public MineSubJob(Entity entity, IF_ItemJob itemJob) {
		super(entity);
		this.itemJob = itemJob;
		Booking booking = itemJob.getBooking();
		GridPosition entityGridPosition = entity.getGridPosition();
		GridPosition mineFromGridPosition = booking.getFrom().getEntity().getGridPosition();
		E_Orientation orientationTo = E_Orientation.orientationTo(entityGridPosition, mineFromGridPosition);
		entity.setOrientation(orientationTo);
		entity.setActivity(E_Activity.WORKING);
		this.finishMineFrameId = FrameModule.instance.getNextFrameId(Constant.DEFAULT_MINE_TIME_IN_FRAMES);
	}

	@Override
	protected void work() {
		if (FrameModule.instance.isCurrentOrPast(this.finishMineFrameId)) {
			Container fromContainerBefore = this.itemJob.getBooking().getFrom();
			this.itemJob.takeItem();
			destroyMineProviderIfEmpty(fromContainerBefore);
			this.entity.popJob();
		}
	}

	private void destroyMineProviderIfEmpty(Container fromContainer) {
		boolean isFromContainerEmpty = fromContainer.isEmpty();
		if (isFromContainerEmpty) {
			Entity fromEntity = fromContainer.getEntity();
			if (fromEntity != null) {
				App.entityFunction.destroyEntity(fromEntity);
			}
		}
	}

}
