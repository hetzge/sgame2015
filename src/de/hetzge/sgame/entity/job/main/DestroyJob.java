package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.frame.FrameModule;

public class DestroyJob extends EntityJob {

	private final int removeOnFrameId;

	public DestroyJob(Entity entity) {
		super(entity);
		int destroyTimeInFrames = entity.getDefinition().getDestroyTimeInFrames();
		this.removeOnFrameId = FrameModule.instance.getNextFrameId(destroyTimeInFrames);
	}

	@Override
	protected void work() {
		if (FrameModule.instance.isCurrentOrPast(this.removeOnFrameId)) {
			App.getGame().getEntityManager().registerRemove(this.entity);
		}
	}

}
