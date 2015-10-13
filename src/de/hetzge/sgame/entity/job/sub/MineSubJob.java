package de.hetzge.sgame.entity.job.sub;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.Job;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;

public class MineSubJob extends Job {

	private final E_Item item;
	private final int finishMineFrameId;

	public MineSubJob(E_Item item) {
		this.item = item;
		this.finishMineFrameId = App.timing.getNextFrameId(Constant.DEFAULT_MINE_TIME_IN_FRAMES);
	}

	@Override
	protected void work(Entity entity) {
		if (App.timing.isCurrentOrPast(this.finishMineFrameId)) {
			App.entityFunction.takeItem(entity, this.item);
			entity.popJob();
		}
	}

}
