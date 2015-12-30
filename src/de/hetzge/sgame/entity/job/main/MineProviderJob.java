package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;

public class MineProviderJob extends EntityJob {

	private final Container container;

	public MineProviderJob(Entity entity) {
		super(entity);
		this.container = entity.getDefinition().createDefaultMineProvideContainer(entity);
	}

	public Container getContainer() {
		return this.container;
	}

	@Override
	protected void work() {
	}

}
