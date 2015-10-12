package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.item.Container;

public class MineProviderJob extends EntityJob {

	private final Container container;

	public MineProviderJob(Entity entity) {
		super(entity);
		container = entity.getDefinition().createDefaultMineProvideContainer(entity);
	}

	public Container getContainer() {
		return container;
	}

	@Override
	protected void work() {
	}

}
