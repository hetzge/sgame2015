package de.hetzge.sgame.entity.job.main;

import java.util.Arrays;
import java.util.List;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.sub.MineSubJob;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.item.Booking;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.Path;

public class MinerJob extends EntityJob {

	private List<E_Item> items = Arrays.asList(E_Item.values);
	private int workstationEntityId = 2; // TODO
	private Booking booking = null;

	// TODO sleep

	@Override
	protected void work(Entity entity) {
		if (workstationEntityId != Constant.NO_ENTITY_ID) {
			if (entity.hasItem()) {
				// bring home
				if (entity.hasPath()) {
					// walk
				} else {
					// check if is at workstation
					Entity workstation = getWorkstation();
					GridPosition doorGridPosition = workstation.getDoorGridPosition();
					GridPosition gridPosition = entity.getGridPosition();
					if (gridPosition.equals(doorGridPosition)) {
						// make transfer complete
						entity.setItem(null);
						if (booking != null) {
							booking.getFrom().transfer(booking);
						} else {
							throw new InvalidGameStateException("No transfer without booking.");
						}
						booking = null;
					} else {
						Path path = App.entityFunction.findPath(entity, doorGridPosition);
						if (path == null) {
							// item fallen lassen
							workstationEntityId = Constant.NO_ENTITY_ID;
							Container worldContainer = App.game.getWorld().getContainerGrid().get(gridPosition);
							booking.changeTo(worldContainer);
							booking.transfer();
							entity.setItem(null);
						} else {
							short[] xPath = path.getXPath();
							short[] yPath = path.getYPath();
							entity.setPath(xPath, yPath);
						}
					}
				}
			} else {
				if (booking == null) {
					// TODO extract predicate
					Path path = App.entityFunction.findPath(entity, searchEntity -> {
						System.out.println("yjsgdsuiogkd jklsdjklgk ");
						EntityJob job = searchEntity.getJob();
						if (job instanceof MineProviderJob) {
							MineProviderJob mineProviderJob = (MineProviderJob) job;
							itemsLoop: for (E_Item item : items) {
								Container fromContainer = mineProviderJob.getContainer();
								boolean hasItem = fromContainer.has(item);
								if (!hasItem) {
									continue itemsLoop;
								}
								Entity workstation = getWorkstation();
								EntityJob workstationEntityJob = workstation.getJob();
								if (workstationEntityJob instanceof WorkstationJob) {
									WorkstationJob workstationJob = (WorkstationJob) workstationEntityJob;
									Container toContainer = workstationJob.getContainer();
									Booking booking = fromContainer.book(item, 1, toContainer);
									if (booking != null) {
										this.booking = booking;
										return true;
									}
								}
							}
						}
						return false;
					});

					if (path != null) {
						entity.setPath(path);
					}
				} else {
					if (entity.hasPath()) {

					} else {
						E_Item item = booking.getItem();
						int entityId = booking.getFrom().getEntityId();
						Entity mineEntity = App.game.getEntityManager().get(entityId);

						if (mineEntity != null) {
							EntityJob mineEntityJob = mineEntity.getJob();
							if (mineEntityJob != null) {
								if (mineEntityJob instanceof MineProviderJob) {
									MineProviderJob mineProviderJob = (MineProviderJob) mineEntityJob;
									addChild(new MineSubJob(mineProviderJob, item));
								} else {
									throw new InvalidGameStateException();
								}
							} else {
								throw new InvalidGameStateException();
							}
						} else {
							// remove invalid booking
							booking.rollback();
							booking = null;
						}
					}
				}
			}
		}

	}

	private Entity getWorkstation() {
		if (workstationEntityId == Constant.NO_ENTITY_ID) {
			throw new InvalidGameStateException();
		} else {
			return App.game.getEntityManager().get(workstationEntityId);
		}
	}
}
