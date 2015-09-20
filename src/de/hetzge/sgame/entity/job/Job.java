package de.hetzge.sgame.entity.job;

import de.hetzge.sgame.entity.Entity;

public abstract class Job {

	protected abstract void work(Entity entity);

	public void doWork(Entity entity) {
		work(entity);
	}

}
