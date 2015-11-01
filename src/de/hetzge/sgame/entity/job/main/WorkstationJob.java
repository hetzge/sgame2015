package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.IF_RenderItemsJob;
import de.hetzge.sgame.item.Container;

public class WorkstationJob extends EntityJob implements IF_RenderItemsJob, IF_ProviderJob {

	private final ProviderJob providerJob;
	private Entity worker;

	public WorkstationJob(Entity entity) {
		super(entity);
		this.providerJob = new ProviderJob(entity);
	}

	@Override
	protected void work() {
	}

	public Container getContainer() {
		return this.providerJob.getProvides();
	}

	public void setWorker(Entity worker) {
		this.worker = worker;
	}

	public Entity getWorker() {
		return this.worker;
	}

	public void unsetWorker() {
		this.worker = null;
	}

	public boolean hasWorker() {
		return this.worker != null;
	}

	@Override
	public Container getRenderLeftContainer() {
		return getContainer();
	}

	@Override
	public Container getRenderRightContainer() {
		return null;
	}

	@Override
	public ProviderJob getProviderJob() {
		return this.providerJob;
	}

}
