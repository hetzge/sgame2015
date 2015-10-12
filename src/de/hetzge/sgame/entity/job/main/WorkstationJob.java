package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.IF_RenderItemsJob;
import de.hetzge.sgame.item.Container;

public class WorkstationJob extends EntityJob implements IF_RenderItemsJob {

	private final Container container;
	private Entity worker;

	public WorkstationJob(Entity entity) {
		super(entity);
		container = entity.getDefinition().createDefaultNeedContainer(entity);
	}

	@Override
	protected void work() {
	}

	public Container getContainer() {
		return container;
	}

	public void setWorker(Entity worker) {
		this.worker = worker;
	}

	public Entity getWorker() {
		return worker;
	}

	public void unsetWorker() {
		worker = null;
	}

	public boolean hasWorker() {
		return worker != null;
	}

	@Override
	public Container getRenderLeftContainer() {
		return container;
	}

	@Override
	public Container getRenderRightContainer() {
		return null;
	}

}
