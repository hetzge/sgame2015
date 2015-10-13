package de.hetzge.sgame.entity.job.main;

import java.util.Arrays;
import java.util.List;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.sub.MineSubJob;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.function.EntityFunction.SearchPredicate;
import de.hetzge.sgame.item.Booking;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.Path;

public class MinerJob extends EntityJob {

	private List<E_Item> items = Arrays.asList(E_Item.values);
	private Entity workstation = null;
	private Booking booking = null;

	public MinerJob(Entity entity) {
		super(entity);
	}

	// TODO sleep

	@Override
	protected void work() {
		if (hasWorkstation()) {
			if (this.entity.hasItem()) {
				// bring home
				if (this.entity.hasPath()) {
					// walk
				} else {
					// check if is at workstation
					if (isEntityAtWorkstationDoor()) {
						// make transfer complete
						if (hasBooking()) {
							this.entity.unsetItem();
							this.booking.getFrom().transfer(this.booking);
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
						}
					}
				}
			} else {
				if (!hasBooking()) {
					findMineEntityAndBookAndGoto();
				} else {
					if (this.entity.hasPath()) {
						// walk
					} else {
						Entity mineEntity = this.booking.getFrom().getEntity();
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
		SearchPredicate searchPredicate = entityToTest -> {
			EntityJob job = entityToTest.getJob();
			if (job instanceof WorkstationJob) {
				WorkstationJob workstationJob = (WorkstationJob) job;
				if (!workstationJob.hasWorker()) {
					workstationJob.setWorker(this.entity);
					setWorkstation(entityToTest);
					return true;
				}
			}
			return false;
		};
		App.entityFunction.findEntity(this.entity, searchPredicate);
	}

	private void findMineEntityAndBookAndGoto() {
		SearchPredicate searchPredicate = entityToTest -> {
			EntityJob job = entityToTest.getJob();
			if (job instanceof MineProviderJob) {
				MineProviderJob mineProviderJob = (MineProviderJob) job;
				for (E_Item item : this.items) {
					Container from = mineProviderJob.getContainer();
					boolean hasItem = from.has(item);
					if (hasItem) {
						WorkstationJob workstationJob = getWorkstationJob();
						Container to = workstationJob.getContainer();
						Booking booking = from.book(item, 1, to);
						if (booking != null) {
							this.booking = booking;
							return true;
						}
					}
				}
			}
			return false;
		};

		Path path = App.entityFunction.findPath(this.entity, searchPredicate);

		if (path != null) {
			this.entity.setPath(path);
		}
	}

	private void startWorking(Entity mineEntity) {
		EntityJob mineEntityJob = mineEntity.getJob();
		if (mineEntityJob instanceof MineProviderJob) {
			MineProviderJob mineProviderJob = (MineProviderJob) mineEntityJob;
			E_Item item = this.booking.getItem();
			addChild(new MineSubJob(mineProviderJob, item));
		} else {
			throw new InvalidGameStateException();
		}
	}

	private void setPath(Path path) {
		short[] xPath = path.getXPath();
		short[] yPath = path.getYPath();
		this.entity.setPath(xPath, yPath);
	}

	private void dropItem() {
		if (this.booking != null) {
			if (this.entity.hasItem()) {
				GridPosition gridPosition = this.entity.getGridPosition();
				Container worldContainer = App.game.getWorld().getContainerGrid().get(gridPosition);
				this.booking.changeTo(worldContainer);
				boolean successful = this.booking.transfer();
				if (!successful) {
					throw new InvalidGameStateException();
				}
				this.entity.setItem(null);
			} else {
				this.booking.rollback();
			}
		}
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
	public void destroy() {
		dropItem();
		WorkstationJob workstationJob = getWorkstationJob();
		if (workstationJob != null) {
			workstationJob.unsetWorker();
		}
	}
}
