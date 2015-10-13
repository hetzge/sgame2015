package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;

public class DestroyJob extends EntityJob {

	private final int removeOnFrameId;

	public DestroyJob(Entity entity) {
		super(entity);
		int destroyTimeInFrames = entity.getDefinition().getDestroyTimeInFrames();
		this.removeOnFrameId = App.timing.getNextFrameId(destroyTimeInFrames);
	}

	@Override
	protected void work() {
		if (App.timing.isCurrentOrPast(this.removeOnFrameId)) {
			App.game.getEntityManager().registerRemove(this.entity);
		}
	}

}
