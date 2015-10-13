package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.IF_RenderItemsJob;
import de.hetzge.sgame.item.Container;

public abstract class ConsumerJob extends EntityJob implements IF_RenderItemsJob {

	protected final Container needs;

	public ConsumerJob(Entity entity) {
		super(entity);
		this.needs = entity.getDefinition().createDefaultNeedContainer(entity);
	}

	@Override
	protected void work() {
	}

	@Override
	public Container getRenderLeftContainer() {
		return this.needs;
	}

}
