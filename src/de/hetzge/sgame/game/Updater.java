package de.hetzge.sgame.game;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;

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
				break;
			case WALKING:
				updateEntityWalking(entity);
				break;
			case WORKING:
				break;
			default:
				break;
			}

		}
	}

	private void updateEntityWalking(Entity entity) {
		if (entity.hasPath()) {
			short nextX = entity.getNextX();
			short nextY = entity.getNextY();
			short gridX = entity.getGridX();
			short gridY = entity.getGridY();

			E_Orientation orientationTo = E_Orientation.orientationTo(gridX, gridY, nextX, nextY);
			entity.move(orientationTo);

			boolean isMissTheMark = isMissTheMark(entity, nextX, nextY, orientationTo);
			if (isMissTheMark) {
				if (entity.isNextEndOfPath()) {
					entity.unsetPath();
				} else {
					entity.nextWaypoint();
				}
			}
		}
	}

	private boolean isMissTheMark(Entity entity, short nextX, short nextY, E_Orientation orientationTo) {
		boolean isMissTheMark = false;
		if (orientationTo.getOffsetX() != 0) {
			float nextRealX = nextX * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
			float renderX = entity.getRenderX();
			isMissTheMark = nextRealX - renderX * orientationTo.getOffsetX() < 0;

		}
		if (orientationTo.getOffsetY() != 0) {
			float nextRealY = nextY * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
			float renderY = entity.getRenderY();
			isMissTheMark = nextRealY - renderY * orientationTo.getOffsetY() < 0;

		}
		return isMissTheMark;
	}

}
