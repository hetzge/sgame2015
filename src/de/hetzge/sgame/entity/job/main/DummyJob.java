package de.hetzge.sgame.entity.job.main;

import java.util.List;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.World;

public class DummyJob extends EntityJob {

	public DummyJob(Entity entity) {
		super(entity);
	}

	@Override
	protected void work() {
		World world = App.getGame().getWorld();

		boolean hasPath = this.entity.hasPath();
		short pathGoalX = this.entity.getPathGoalX();
		short pathGoalY = this.entity.getPathGoalY();

		if (hasPath) {
			List<GridPosition> aroundOnMap = world.getAroundOnMap(pathGoalX, pathGoalY);
			for (GridPosition gridPosition : aroundOnMap) {
			}
		}

		System.out.println("Ich bin doof!");

		// pr�fe ob entity geht
		// zu gesuchtem entity
		// ansonsten
		// finde entity
		// gehe zu entity
		// wenn da
		// sammle
		// wenn voll
		// pr�fe ob nach hause geht
		// suche zuhause
		// gehe nach hause
		// wenn da
		// abladen

	}

}
