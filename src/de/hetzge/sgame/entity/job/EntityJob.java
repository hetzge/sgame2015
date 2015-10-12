package de.hetzge.sgame.entity.job;

import de.hetzge.sgame.entity.Entity;

public abstract class EntityJob extends Job {

	protected final Entity entity;

	public EntityJob(Entity entity) {
		this.entity = entity;
	}

	protected abstract void work();

	@Override
	protected void work(Entity entity) {
		work();
	}

	public Entity getEntity() {
		return entity;
	}

}
