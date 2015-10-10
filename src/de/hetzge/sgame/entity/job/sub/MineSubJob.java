package de.hetzge.sgame.entity.job.sub;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.main.MineProviderJob;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;

public class MineSubJob extends EntityJob {

	private final MineProviderJob mineProviderJob;
	private final E_Item item;
	private short mineCounter;

	public MineSubJob(MineProviderJob mineProviderJob, E_Item item) {
		this.mineProviderJob = mineProviderJob;
		this.item = item;
	}

	@Override
	protected void work(Entity entity) {
		short mineSpeed = entity.getDefinition().getMineSpeed();
		mineCounter += mineSpeed;
		if (mineCounter > Constant.MINE_VALUE) {
			entity.setItem(item);
			entity.popJob();
		}
	}

}