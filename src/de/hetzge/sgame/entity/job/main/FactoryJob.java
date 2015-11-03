package de.hetzge.sgame.entity.job.main;

import java.util.List;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.IF_RenderItemsJob;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.item.Booking;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.item.Receipt;
import de.hetzge.sgame.misc.Constant;

public class FactoryJob extends EntityJob implements IF_RenderItemsJob, IF_ProviderJob, IF_ConsumerJob {

	private final ProviderJob providerJob;
	private final ConsumerJob consumerJob;

	private Receipt workingAt = null;
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

		List<Receipt> receipts = this.entity.getDefinition().getReceipts();

		Container provides = this.providerJob.getProvides();
		Container needs = this.consumerJob.getNeeds();
		if (!isWorkingAt()) {
			// start
			for (Receipt receipt : receipts) {
				E_Item result = receipt.getResult();
				boolean canAddAmount = provides.canAddAmount(result, 1);
				if (canAddAmount && receipt.possible(needs)) {
					this.finishProductFrameId = App.timing.getNextFrameId(Constant.DEFAULT_PRODUCTION_TIME_IN_FRAMES);
					this.workingAt = receipt;
					this.entity.setActivity(E_Activity.WORKING);
					break;
				}
			}
		} else if (App.timing.isCurrentOrPast(this.finishProductFrameId)) {
			// finish
			E_Item build = this.workingAt.build(needs);
			boolean buildSuccessful = build != null;
			if (buildSuccessful) {
				Container tempContainer = new Container(null);
				tempContainer.set(build, 1, 1);
				Booking booking = tempContainer.book(build, 1, provides);
				booking.transfer();
			} else {
				throw new InvalidGameStateException();
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
	public Container getRenderLeftContainer() {
		return this.consumerJob.getNeeds();
	}

	@Override
	public Container getRenderRightContainer() {
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
