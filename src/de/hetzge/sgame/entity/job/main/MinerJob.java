package de.hetzge.sgame.entity.job.main;

import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import de.hetzge.sgame.App;
import de.hetzge.sgame.booking.Booking;
import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.sub.MineSubJob;
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
							throw new IllegalStateException("No transfer without booking.");
						}
					} else {
						GridPosition doorGridPosition = this.workstation.getDoorGridPosition();
						Path path = App.searchFunction.findPath(this.entity, doorGridPosition);
						if (path != null) {
							this.entity.setPath(path);
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
						Entity mineEntity = this.booking.<IF_GridEntityContainer> getFrom().getEntity();
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
			Entity workstation = findWorkstation();
			if (workstation != null) {
				setupWorkstation(workstation);
			} else {
				pauseMedium();
			}
		}

	}

	private void setupWorkstation(Entity workstation) {
		EntityJob job = workstation.getJob();
		WorkstationJob workstationJob = (WorkstationJob) job;
		workstationJob.setWorker(this.entity);
		setWorkstation(workstation);
	}

	private Entity findWorkstation() {
		Function<Entity, Entity> entitySearchFunction = entityToTest -> {
			EntityJob job = entityToTest.getJob();
			if (!(job instanceof WorkstationJob)) {
				return null;
			}

			WorkstationJob workstationJob = (WorkstationJob) job;
			if (workstationJob.hasWorker()) {
				return null;
			}

			E_Item WorkstationItem = workstationJob.getEntity().getDefinition().getMineItem();
			E_Item minerItem = this.entity.getDefinition().getMineItem();
			if (minerItem != WorkstationItem) {
				return null;
			}

			return entityToTest;
		};

		return App.searchFunction.byEntity.find(this.entity, entitySearchFunction);
	}

	private void findMineEntityAndBookAndGoto() {

		Pair<Entity, Path> searchResult = findMineEntity();
		Entity mineEntity = searchResult.getLeft();
		Path path = searchResult.getRight();

		if (mineEntity != null && path != null) {
			WorkstationJob workstationJob = getWorkstationJob();
			Container<E_Item> to = workstationJob.getContainer();
			E_Item item = this.entity.getDefinition().getMineItem();
			EntityJob job = mineEntity.getJob();
			if (job instanceof MineProviderJob) {
				MineProviderJob mineProviderJob = (MineProviderJob) job;
				Container<E_Item> from = mineProviderJob.getContainer();
				Booking<E_Item> booking = from.book(item, 1, to);
				if (booking != null) {
					this.booking = booking;
					this.entity.setPath(path);
				}
			} else {
				throw new IllegalStateException();
			}
		} else {
			pauseLong();
		}
	}

	private Pair<Entity, Path> findMineEntity() {
		Function<Entity, Entity> searchFunction = entityToTest -> {
			EntityJob job = entityToTest.getJob();
			if (job instanceof MineProviderJob) {
				MineProviderJob mineProviderJob = (MineProviderJob) job;
				Container<E_Item> from = mineProviderJob.getContainer();
				E_Item item = this.entity.getDefinition().getMineItem();
				boolean hasItem = from.has(item);
				if (hasItem) {
					return entityToTest;
				}
			}
			return null;
		};

		Pair<Entity, Path> searchResult = App.searchFunction.byEntity.findAndPath(this.entity, searchFunction);
		return searchResult;
	}

	private void startWorking(Entity mineEntity) {
		EntityJob mineEntityJob = mineEntity.getJob();
		if (mineEntityJob instanceof MineProviderJob) {
			addChild(new MineSubJob(this.entity, this));
		} else {
			throw new IllegalStateException();
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
				throw new IllegalStateException("Workstation without workstation job");
			}
		} else {
			throw new IllegalStateException("Try to access workstation job that do not exist.");
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
	public Container<E_Item> getContainer() {
		return this.container;
	}

	@Override
	public Booking<E_Item> getBooking() {
		return this.booking;
	}

	@Override
	public void setBooking(Booking<E_Item> booking) {
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
