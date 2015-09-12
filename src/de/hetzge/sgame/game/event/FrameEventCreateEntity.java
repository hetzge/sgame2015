package de.hetzge.sgame.game.event;

import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
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
		EntityDefinition definition = entityType.getEntityDefinition();
		boolean moveable = definition.isMoveable();

		boolean checkSpaceForEntity = App.worldFunction.checkSpaceForEntity(entityType, x, y);
		if (!checkSpaceForEntity) {
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

		Entity entity = new Entity(entityId, playerId, entityType);
		App.game.getEntityManager().register(entity);
		App.game.getEntityGrid().set(x, y, entity);
		if (!moveable) {
			short width = definition.getWidth();
			short height = definition.getHeight();
			App.game.getWorld().getFixedCollisionGrid().setCollision(x, y, width, height);
		}
	}

	@Override
	public String toString() {
		return "FrameEventCreateEntity [entityType=" + entityType + ", x=" + x + ", y=" + y + ", entityId=" + entityId + ", playerId=" + playerId + "]";
	}

}
