package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.item.Container;

public class WorkstationJob extends EntityJob {

	private final Container container;

	public WorkstationJob(Entity entity) {
		int entityId = entity.getId();
		container = entity.getDefinition().createDefaultNeedContainer(entityId);
	}

	@Override
	protected void work(Entity entity) {
	}

	public Container getContainer() {
		return container;
	}

}
