package de.hetzge.sgame.function;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.world.EntityGrid;
import de.hetzge.sgame.world.GridPosition;

public class EntityFunction implements IF_EntityFunction {

	@Override
	public void goAway(Entity entity) {
		goAway(entity, new LinkedList<>());
	}

	public void goAway(Entity entity, List<GridPosition> blacklist) {
		EntityGrid entityGrid = App.game.getEntityGrid();
		short gridX = entity.getGridX();
		short gridY = entity.getGridY();

		Stream<GridPosition> aroundStream = entityGrid.getAroundStream(gridX, gridY);
		Stream<GridPosition> notBlacklistedAroundStream = aroundStream.filter(gridPosition -> !blacklist.contains(gridPosition));
		Stream<GridPosition> emptyNotBlacklistedAroundStream = notBlacklistedAroundStream.filter(entityGrid::isNot);
		GridPosition goalGridPosition = emptyNotBlacklistedAroundStream.findFirst().orElse(aroundStream.findFirst().get());
		Entity entityOnGoal = entityGrid.get(goalGridPosition.getGridX(), goalGridPosition.getGridY());
		if (entityOnGoal != null) {
			blacklist.add(goalGridPosition);
			goAway(entityOnGoal, blacklist);
		}
		gotoGridPosition(entity, goalGridPosition.getGridX(), goalGridPosition.getGridY());
	}

	@Override
	public void gotoGridPosition(Entity entity, short x, short y) {
		// TODO
	}

}
