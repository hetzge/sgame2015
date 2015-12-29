package de.hetzge.sgame;

import java.util.Objects;

import org.nustaq.net.TCPObjectSocket;
import org.pmw.tinylog.Logger;

import de.hetzge.sgame.frame.FrameModule;
import de.hetzge.sgame.frame.IF_FrameEvent;
import de.hetzge.sgame.game.event.IF_ConnectionEvent;
import de.hetzge.sgame.game.event.IF_Event;
import de.hetzge.sgame.network.IF_Dispatch;

public class Dispatcher implements IF_Dispatch {

	@Override
	public void dispatch(Object object, TCPObjectSocket socket) {
		if (object instanceof IF_FrameEvent) {
			IF_FrameEvent frameEvent = (IF_FrameEvent) object;
			FrameModule.instance.addFrameEvent(frameEvent);
		} else if (object instanceof IF_ConnectionEvent) {
			IF_ConnectionEvent connectionEvent = (IF_ConnectionEvent) object;
			Objects.requireNonNull(socket);
			connectionEvent.execute(socket);
		} else if (object instanceof IF_Event) {
			IF_Event event = (IF_Event) object;
			event.execute();
		} else {
			Logger.warn("unhandelt object: " + object);
		}
	}

}
