package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.App;
import de.hetzge.sgame.booking.Booking;
import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.sub.MineSubJob;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.function.EntityFunction.EntityPredicate;
import de.hetzge.sgame.function.OnMapPredicate.EntityOnMapPredicate;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.item.GridEntityContainerWithoutLimit;
import de.hetzge.sgame.item.IF_GridEntityContainer;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.Path;

public class MinerJob extends EntityJob implements IF_ItemJob {

	private Entity workstation = null;
	private Booking<E_Item> booking = null;
	private final Container<E_Item> container;

	public MinerJob(Entity entity) {
		super(entity);
		this.container = new GridEntityContainerWithoutLimit(entity);
	}

	@Override
	protected void work() {
		if (hasWorkstation()) {
			if (this.entity.hasItem()) {
				// bring home
				if (this.entity.hasPath()) {
					// walk
					pauseMedium();
				} else {
					// check if is at workstation
					if (isEntityAtWorkstationDoor()) {
						// make transfer complete
						if (hasBooking()) {
							this.entity.unsetItem();
							this.booking.transfer();
							unsetBooking();
						} else {
							throw new InvalidGameStateException("No transfer without booking.");
						}
					} else {
						GridPosition doorGridPosition = this.workstation.getDoorGridPosition();
						Path path = App.entityFunction.findPath(this.entity, doorGridPosition);
						if (path != null) {
							setPath(path);
						} else {
							// item fallen lassen
							unsetWorkstation();
							dropItem();
							unsetBooking();
						}
					}
				}
			} else {
				if (!hasBooking()) {
					findMineEntityAndBookAndGoto();
				} else {
					if (this.entity.hasPath()) {
						// walk
						pauseMedium();
					} else {
						Entity mineEntity = this.booking.<IF_GridEntityContainer>getFrom().getEntity();
						if (mineEntity != null) {
							startWorking(mineEntity);
						} else {
							// remove invalid booking
							this.booking.rollback();
							this.booking = null;
						}
					}
				}
			}
		} else {
			findWorkstation();
		}

	}

	private void findWorkstation() {
		EntityPredicate searchPredicate = entityToTest -> {
			EntityJob job = entityToTest.getJob();
			if (!(job instanceof WorkstationJob)) {
				return false;
			}

			WorkstationJob workstationJob = (WorkstationJob) job;
			if (workstationJob.hasWorker()) {
				return false;
			}

			E_Item WorkstationItem = workstationJob.getEntity().getDefinition().getMineItem();
			E_Item minerItem = this.entity.getDefinition().getMineItem();
			if (minerItem != WorkstationItem) {
				return false;
			}

			workstationJob.setWorker(this.entity);
			setWorkstation(entityToTest);
			return true;
		};
		App.entityFunction.iterateMap(this.entity, new EntityOnMapPredicate(searchPredicate));
	}

	private void findMineEntityAndBookAndGoto() {
		EntityPredicate searchPredicate = entityToTest -> {
			EntityJob job = entityToTest.getJob();
			if (job instanceof MineProviderJob) {
				E_Item item = this.entity.getDefinition().getMineItem();
				MineProviderJob mineProviderJob = (MineProviderJob) job;
				Container<E_Item> from = mineProviderJob.getContainer();
				boolean hasItem = from.has(item);
				if (hasItem) {
					WorkstationJob workstationJob = getWorkstationJob();
					Container<E_Item> to = workstationJob.getContainer();
					Booking<E_Item> booking = from.book(item, 1, to);
					if (booking != null) {
						this.booking = booking;
						return true;
					}
				}

			}
			return false;
		};

		Path path = App.entityFunction.findPath(this.entity, new EntityOnMapPredicate(searchPredicate));

		if (path != null) {
			this.entity.setPath(path);
		}
	}

	private void startWorking(Entity mineEntity) {
		EntityJob mineEntityJob = mineEntity.getJob();
		if (mineEntityJob instanceof MineProviderJob) {
			addChild(new MineSubJob(this.entity, this));
		} else {
			throw new InvalidGameStateException();
		}
	}

	private void setPath(Path path) {
		short[] xPath = path.getXPath();
		short[] yPath = path.getYPath();
		this.entity.setPath(xPath, yPath);
	}

	private boolean isEntityAtWorkstationDoor() {
		GridPosition doorGridPosition = this.workstation.getDoorGridPosition();
		GridPosition gridPosition = this.entity.getGridPosition();
		return gridPosition.equals(doorGridPosition);
	}

	public void unsetWorkstation() {
		this.workstation = null;
	}

	public void setWorkstation(Entity workstation) {
		this.workstation = workstation;
	}

	public WorkstationJob getWorkstationJob() {
		if (hasWorkstation()) {
			EntityJob workstationEntityJob = this.workstation.getJob();
			if (workstationEntityJob instanceof WorkstationJob) {
				WorkstationJob workstationJob = (WorkstationJob) workstationEntityJob;
				return workstationJob;
			} else {
				throw new InvalidGameStateException("Workstation without workstation job");
			}
		} else {
			return null;
		}
	}

	public boolean hasWorkstation() {
		return this.workstation != null;
	}

	public boolean hasBooking() {
		return this.booking != null;
	}

	public void unsetBooking() {
		this.booking = null;
	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public Booking getBooking() {
		return this.booking;
	}

	@Override
	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	@Override
	public void destroy() {
		dropItem();
		WorkstationJob workstationJob = getWorkstationJob();
		if (workstationJob != null) {
			workstationJob.unsetWorker();
		}
	}

}
