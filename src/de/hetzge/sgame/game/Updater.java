package de.hetzge.sgame.game;

import java.util.Arrays;
import java.util.stream.Stream;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.game.event.request.EventRequestGoto;
import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.world.EntityGrid;
import de.hetzge.sgame.world.GridPosition;

public class Updater {

	public void update() {
		updateEntities();
	}

	private void updateEntities() {
		Iterable<Entity> entities = App.game.getEntityManager().getEntities();
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
			default:
				break;
			}
		}
	}

	private void updateEntityIdle(Entity entity) {

	}

	private void updateEntityWalking(Entity entity) {
		if (entity.hasPath()) {
			EntityGrid entityGrid = App.game.getEntityGrid();

			short nextX = entity.getNextX();
			short nextY = entity.getNextY();
			short gridX = entity.getGridX();
			short gridY = entity.getGridY();
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
							App.entityFunction.goAway(entityOnNext);
							return;
						}
					} else {
						// rerequest path
						int entityId = entity.getId();
						short pathGoalX = entity.getPathGoalX();
						short pathGoalY = entity.getPathGoalY();
						EventRequestGoto eventRequestGoto = new EventRequestGoto(Arrays.asList(entityId), pathGoalX, pathGoalY);
						App.network.sendOrSelf(eventRequestGoto);
						return;
					}
				}
			} else {
				boolean isEntityOnCurrentPosition = entityGrid.isEntity(gridX, gridY, entity);
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

					// reregister on grid

				}
			}
		}
	}

	private void updateEntityWorking(Entity entity) {

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
