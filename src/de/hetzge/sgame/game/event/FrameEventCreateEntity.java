package de.hetzge.sgame.game.event;

import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.frame.FrameEvent;
import de.hetzge.sgame.game.event.request.EventRequestCreateEntity;
import de.hetzge.sgame.world.GridPosition;

public class FrameEventCreateEntity extends FrameEvent {

	private final E_EntityType entityType;
	private final short x;
	private final short y;
	private final int entityId;
	private final byte playerId;

	public FrameEventCreateEntity(int frameId, E_EntityType entityType, short x, short y, int entityId, byte playerId) {
		super(frameId);
		this.entityType = entityType;
		this.x = x;
		this.y = y;
		this.entityId = entityId;
		this.playerId = playerId;
	}

	@Override
	public void execute() {

		boolean checkSpaceForEntity = App.worldFunction.checkSpaceForEntity(entityType, x, y);
		if (!checkSpaceForEntity) {
			EntityDefinition definition = entityType.getEntityDefinition();
			boolean moveable = definition.isMoveable();
			if (moveable) {
				GridPosition freeGridPosition = App.worldFunction.findEmptyGridPositionAround(entityType, x, y);
				if (freeGridPosition != null) {
					short newX = freeGridPosition.getGridX();
					short newY = freeGridPosition.getGridY();
					App.network.sendOrSelf(new EventRequestCreateEntity(newX, newY, entityType, playerId));
				} else {
					Logger.info("Didn't created entity from " + toString() + " caused by no space around found.");
				}
			} else {
				Logger.info("Didn't created entity from " + toString() + " caused by no space.");
			}
			return;
		}

		App.entityFunction.createEntity(entityType, x, y, entityId, playerId);
	}

	@Override
	public String toString() {
		return "FrameEventCreateEntity [entityType=" + entityType + ", x=" + x + ", y=" + y + ", entityId=" + entityId + ", playerId=" + playerId + "]";
	}

}
