package de.hetzge.sgame.entity.job.main;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.sub.MineSubJob;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.item.Booking;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.world.Path;

public class MinerJob extends EntityJob {

	private int workstationEntityId = Constant.NO_ENTITY_ID;
	private Booking booking = null;

	// TODO sleep

	@Override
	protected void work(Entity entity) {
		if (workstationEntityId != Constant.NO_ENTITY_ID) {
			if (entity.hasItem()) {
				// bring home
				if (entity.hasPath()) {

				} else {
					// check if is at workstation
					Entity workstation = getWorkstation();
					short doorX = workstation.getDoorX();
					short doorY = workstation.getDoorY();
					short gridX = entity.getGridX();
					short gridY = entity.getGridY();
					if (gridX == doorX && gridY == doorY) {
						// make transfer complete
						entity.setItem(null);
						if (booking != null) {
							booking.getFrom().transfer(booking);
						} else {
							throw new InvalidGameStateException("No transfer without booking.");
						}
						booking = null;
					} else {
						Path path = App.entityFunction.findPath(entity, doorX, doorY);
						if (path == null) {
							workstationEntityId = Constant.NO_ENTITY_ID;
							// TODO item fallen lassen
						} else {
							short[] xPath = path.getXPath();
							short[] yPath = path.getYPath();
							entity.setPath(xPath, yPath);
						}
					}
				}
			} else {
				if (booking == null) {
					// search MineProvider ... by A*
					// get Booking
					// goto Booking goal
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
