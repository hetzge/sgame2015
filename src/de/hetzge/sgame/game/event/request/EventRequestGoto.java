package de.hetzge.sgame.game.event.request;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.game.event.FrameEventGoto;
import de.hetzge.sgame.network.IF_Event;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.Path;

public class EventRequestGoto implements IF_Event {

	private final List<Integer> entityIds;
	private final short goalX;
	private final short goalY;

	public EventRequestGoto(List<Integer> entityIds, short goalX, short goalY) {
		this.entityIds = entityIds;
		this.goalX = goalX;
		this.goalY = goalY;
	}

	@Override
	public void execute() {
		List<Entity> entities = App.game.getEntityManager().get(entityIds);
		Map<Entity, Path> paths = App.entityFunction.findPath(entities, new GridPosition(goalX, goalY));

		int[] entityIds = new int[paths.size()];
		short[][] xPaths = new short[paths.size()][];
		short[][] yPaths = new short[paths.size()][];

		int i = 0;
		for (Entry<Entity, Path> entry : paths.entrySet()) {
			Entity entity = entry.getKey();
			Path path = entry.getValue();

			entityIds[i] = entity.getId();
			xPaths[i] = path.getXPath();
			yPaths[i] = path.getYPath();
			i++;
		}

		App.network.sendAndSelf(new FrameEventGoto(App.timing.getDefaultNextFrameId(), entityIds, xPaths, yPaths));
	}

}
