package de.hetzge.sgame.game.event.request;

import org.nustaq.net.TCPObjectSocket;
import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.game.event.FrameEventCreateEntity;
import de.hetzge.sgame.misc.Util;
import de.hetzge.sgame.network.IF_ConnectionEvent;

public class EventRequestCreateEntity implements IF_ConnectionEvent {

	private final short x;
	private final short y;
	private final E_EntityType entityType;
	private final byte playerId;

	public EventRequestCreateEntity(short x, short y, E_EntityType entityType, byte playerId) {
		this.x = x;
		this.y = y;
		this.entityType = entityType;
		this.playerId = playerId;
	}

	@Override
	public void execute(TCPObjectSocket tcpObjectSocket) {
		boolean hasSpace = App.worldFunction.checkSpaceForEntity(entityType, x, y);
		if (hasSpace) {
			int nextEntityId = App.game.getEntityManager().getNextId();
			FrameEventCreateEntity frameEventCreateEntity = new FrameEventCreateEntity(App.timing.getDefaultNextFrameId(), entityType, x, y, nextEntityId, playerId);
			App.network.sendAndSelf(frameEventCreateEntity);
		} else {
			Logger.info("There is no space for " + entityType + " at " + Util.toString(x, y));
		}
	}

}
