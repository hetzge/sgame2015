package de.hetzge.sgame.game;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.Entity;
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
