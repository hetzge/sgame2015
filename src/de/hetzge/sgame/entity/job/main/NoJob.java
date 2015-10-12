package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;

public class NoJob extends EntityJob {

	public NoJob(Entity entity) {
		super(entity);
	}

	@Override
	protected void work() {
	}

}
