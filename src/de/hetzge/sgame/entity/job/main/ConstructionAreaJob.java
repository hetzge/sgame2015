package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.item.Container;

public class ConstructionAreaJob extends ConsumerJob {

	public ConstructionAreaJob(Entity entity) {
		super(entity);
	}

	@Override
	public Container getRenderRightContainer() {
		return null;
	}

}
