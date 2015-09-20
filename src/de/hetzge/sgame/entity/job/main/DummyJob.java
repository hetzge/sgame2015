package de.hetzge.sgame.entity.job.main;

import java.util.List;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.Job;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.World;

public class DummyJob extends Job {

	@Override
	protected void work(Entity entity) {
		World world = App.game.getWorld();

		boolean hasPath = entity.hasPath();
		short pathGoalX = entity.getPathGoalX();
		short pathGoalY = entity.getPathGoalY();

		if (hasPath) {
			List<GridPosition> aroundOnMap = world.getAroundOnMap(pathGoalX, pathGoalY);
			for (GridPosition gridPosition : aroundOnMap) {
				if()
			}
		}

		// prüfe ob entity geht
		// zu gesuchtem entity
		// ansonsten
		// finde entity
		// gehe zu entity
		// wenn da
		// sammle
		// wenn voll
		// prüfe ob nach hause geht
		// suche zuhause
		// gehe nach hause
		// wenn da
		// abladen

	}

	private boolean isWalking(Entity entity) {

	}

	private boolean isWalkingTo

}
