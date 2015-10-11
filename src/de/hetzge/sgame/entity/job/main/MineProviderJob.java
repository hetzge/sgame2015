package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.item.Container;

public class MineProviderJob extends EntityJob {

	private final Container container;

	public MineProviderJob(Entity entity) {
		int entityId = entity.getId();
		container = entity.getDefinition().createDefaultMineProvideContainer(entityId);
	}

	public Container getContainer() {
		return container;
	}

	@Override
	protected void work(Entity entity) {
	}

}
