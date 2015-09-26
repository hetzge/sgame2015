package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.item.Container;

public class MineProviderJob extends EntityJob {

	private final Container container;

	public MineProviderJob(Entity entity) {
		container = entity.getDefinition().createDefaultMineProvideContainer(entity.getId());
	}

	public Container getContainer() {
		return container;
	}

	@Override
	protected void work(Entity entity) {
	}

}
