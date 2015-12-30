package de.hetzge.sgame.game;

import java.util.Arrays;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.EntityManager;
import de.hetzge.sgame.frame.FrameModule;
import de.hetzge.sgame.frame.IF_Update;
import de.hetzge.sgame.game.event.request.EventRequestGoto;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.network.NetworkModule;
import de.hetzge.sgame.world.EntityGrid;

public class Updater implements IF_Update {

	@Override
	public void update() {
		updateEntities();
	}

	private void updateEntities() {
		EntityManager entityManager = App.game.getEntityManager();
		Iterable<Entity> removeEntities = entityManager.flushRemoveEntities();
		for (Entity entity : removeEntities) {
			App.entityFunction.removeEntity(entity);
		}

		Iterable<Entity> entities = entityManager.getEntities();
		for (Entity entity : entities) {
			if (FrameModule.instance.isXthFrame(Constant.DO_JOB_EVERY_XTH_FRAMES)) {
				entity.getJob().doWork(entity);
			}
		}

		for (Entity entity : entities) {
			E_Activity activity = entity.getActivity();
			switch (activity) {
			case IDLE:
				updateEntityIdle(entity);
				break;
			case WALKING:
				updateEntityWalking(entity);
				break;
			case WORKING:
				updateEntityWorking(entity);
				break;
			case CARRY:
				updateEntityWalking(entity);
				break;
			case DESTROY:
				updateEntityDestroy(entity);
			default:
				break;
			}
		}
	}

	private void updateEntityIdle(Entity entity) {
		if (entity.hasPath()) {
			entity.setActivity(E_Activity.WALKING);
		}
	}

	private void updateEntityWalking(Entity entity) {
		if (entity.hasPath()) {
			EntityGrid entityGrid = App.game.getEntityGrid();

			short nextX = entity.getNextX();
			short nextY = entity.getNextY();
			short gridX = entity.getGridX();
			short gridY = entity.getGridY();
			short registeredX = entity.getRegisteredX();
			short registeredY = entity.getRegisteredY();
			Entity entityOnNext = entityGrid.get(nextX, nextY);
			if (entityOnNext != null) {
				if (entityOnNext.equals(entity)) {
					// TODO
				} else {
					boolean isEntityOnNextMovable = entityOnNext.getDefinition().isMoveable();
					if (isEntityOnNextMovable) {
						boolean hasEntityOnNextAPath = entityOnNext.hasPath();
						if (hasEntityOnNextAPath) {
							short entityOnNextNextX = entityOnNext.getNextX();
							short entityOnNextNextY = entityOnNext.getNextY();
							if (entityOnNextNextX == gridX && entityOnNextNextY == gridY) {
								// swap
								entityGrid.swap(entity, entityOnNext);
							} else {
								// wait
								return;
							}
						} else {
							// verdängen
							// TODO Sync ?!
							// entityGrid.swap(entity, entityOnNext);
							App.entityFunction.goAway(entityOnNext);
							return;
						}
					} else {
						// rerequest path
						int entityId = entity.getId();
						short pathGoalX = entity.getPathGoalX();
						short pathGoalY = entity.getPathGoalY();
						EventRequestGoto eventRequestGoto = new EventRequestGoto(Arrays.asList(entityId), pathGoalX,
								pathGoalY);
						NetworkModule.instance.sendOrSelf(eventRequestGoto);
						return;
					}
				}
			} else {
				boolean isEntityOnCurrentPosition = entityGrid.isEntity(registeredX, registeredY, entity);
				if (isEntityOnCurrentPosition) {
					entityGrid.set(nextX, nextY, entity);
				}
			}

			E_Orientation orientationTo = entity.getOrientationToNext();
			entity.move(orientationTo);
			boolean isMissTheMark = isMissTheMark(entity, orientationTo);
			if (isMissTheMark) {
				if (entity.isNextEndOfPath()) {
					entity.unsetPath();
					entity.setActivity(E_Activity.IDLE);
				} else {
					entity.nextWaypoint();
				}
			}
		}
	}

	private void updateEntityWorking(Entity entity) {

	}

	private void updateEntityDestroy(Entity entity) {

	}

	private boolean isMissTheMark(Entity entity, E_Orientation orientationTo) {
		boolean isMissTheMark = false;
		if (orientationTo.getOffsetX() != 0) {
			float nextWorldX = entity.getNextWorldX();
			float renderX = entity.getWorldX();
			isMissTheMark = nextWorldX - renderX * orientationTo.getOffsetX() < 0;
		}
		if (orientationTo.getOffsetY() != 0) {
			float nextWorldY = entity.getNextWorldY();
			float renderY = entity.getWorldY();
			isMissTheMark = nextWorldY - renderY * orientationTo.getOffsetY() < 0;
		}
		return isMissTheMark;
	}

}
