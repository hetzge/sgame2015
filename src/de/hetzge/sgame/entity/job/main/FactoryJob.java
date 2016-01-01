package de.hetzge.sgame.entity.job.main;

import java.util.List;

import de.hetzge.sgame.booking.Booking;
import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.booking.Receipt;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.IF_RenderItemsJob;
import de.hetzge.sgame.frame.FrameModule;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;

public class FactoryJob extends EntityJob implements IF_RenderItemsJob, IF_ProviderJob, IF_ConsumerJob {

	private final ProviderJob providerJob;
	private final ConsumerJob consumerJob;

	private Receipt<E_Item> workingAt = null;
	private int finishProductFrameId = 0;

	public FactoryJob(Entity entity) {
		super(entity);
		this.providerJob = new ProviderJob(entity);
		this.consumerJob = new ConsumerJob(entity);
	}

	@Override
	protected void work() {
		this.providerJob.doWork(this.entity);
		this.consumerJob.doWork(this.entity);

		List<Receipt<E_Item>> receipts = this.entity.getDefinition().getReceipts();

		Container<E_Item> provides = this.providerJob.getProvides();
		Container<E_Item> needs = this.consumerJob.getNeeds();
		if (!isWorkingAt()) {
			// start
			for (Receipt<E_Item> receipt : receipts) {
				E_Item result = receipt.getResult();
				boolean canAddAmount = provides.canAddAmount(result, 1);
				if (canAddAmount && receipt.possible(needs)) {
					this.finishProductFrameId = FrameModule.instance
							.getNextFrameId(Constant.DEFAULT_PRODUCTION_TIME_IN_FRAMES);
					this.workingAt = receipt;
					this.entity.setActivity(E_Activity.WORKING);
					break;
				}
			}
		} else if (FrameModule.instance.isCurrentOrPast(this.finishProductFrameId)) {
			// finish
			E_Item build = this.workingAt.build(needs);
			boolean buildSuccessful = build != null;
			if (buildSuccessful) {
				Container<E_Item> tempContainer = new Container<>();
				tempContainer.set(build, 1, 1);
				Booking<E_Item> booking = tempContainer.book(build, 1, provides);
				booking.transfer();
			} else {
				throw new IllegalStateException();
			}

			this.entity.setActivity(E_Activity.IDLE);
			unsetWorkingAt();
		} else {
			pauseMedium();
		}
	}

	public boolean isWorkingAt() {
		return this.workingAt != null;
	}

	public void unsetWorkingAt() {
		this.workingAt = null;
	}

	@Override
	public Container<E_Item> getRenderLeftContainer() {
		return this.consumerJob.getNeeds();
	}

	@Override
	public Container<E_Item> getRenderRightContainer() {
		return this.providerJob.getProvides();
	}

	@Override
	public ConsumerJob getConsumerJob() {
		return this.consumerJob;
	}

	@Override
	public ProviderJob getProviderJob() {
		return this.providerJob;
	}

}
