package de.hetzge.sgame.game.event.request;

import org.nustaq.net.TCPObjectSocket;
import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.frame.FrameModule;
import de.hetzge.sgame.game.event.FrameEventCreateEntity;
import de.hetzge.sgame.game.event.IF_ConnectionEvent;
import de.hetzge.sgame.misc.Util;
import de.hetzge.sgame.network.NetworkModule;

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
		boolean hasSpace = App.worldFunction.checkSpaceForEntity(this.entityType, this.x, this.y);
		if (hasSpace) {
			int nextEntityId = App.getGame().getEntityManager().getNextId();
			FrameEventCreateEntity frameEventCreateEntity = new FrameEventCreateEntity(
					FrameModule.instance.getDefaultNextFrameId(), this.entityType, this.x, this.y, nextEntityId,
					this.playerId);
			NetworkModule.instance.sendAndSelf(frameEventCreateEntity);
		} else {
			Logger.info("There is no space for " + this.entityType + " at " + Util.toString(this.x, this.y));
		}
	}

}
