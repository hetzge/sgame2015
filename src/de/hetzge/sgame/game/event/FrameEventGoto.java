package de.hetzge.sgame.game.event;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.frame.FrameEvent;

public class FrameEventGoto extends FrameEvent {

	private final int[] entityIds;
	private final short[][] xPaths;
	private final short[][] yPaths;

	public FrameEventGoto(int frameId, int[] entityIds, short[][] xPaths, short[][] yPaths) {
		super(frameId);
		this.entityIds = entityIds;
		this.xPaths = xPaths;
		this.yPaths = yPaths;
	}

	@Override
	public void execute() {
		for (int i = 0; i < this.entityIds.length; i++) {
			int entityId = this.entityIds[i];
			short[] xPath = this.xPaths[i];
			short[] yPath = this.yPaths[i];
			if (xPath != null && yPath != null) {
				Entity entity = App.getGame().getEntityManager().get(entityId);
				entity.setPath(xPath, yPath);
				System.out.println(entity.getWorldX() + " <-> " + entity.getWorldY());
			}
		}
	}

}
