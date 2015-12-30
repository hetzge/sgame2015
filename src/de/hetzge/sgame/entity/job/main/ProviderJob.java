package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;

public class ProviderJob extends EntityJob implements IF_ProviderJob {

	private final Container provides;

	public ProviderJob(Entity entity) {
		super(entity);
		this.provides = entity.getDefinition().createDefaultProvideContainer(entity);
	}

	@Override
	protected void work() {
		// TODO brauch ich hier Logik oder reicht Consumer
	}

	public Container getProvides() {
		return this.provides;
	}

	@Override
	public ProviderJob getProviderJob() {
		return this;
	}

}
