package de.hetzge.sgame.game.event;

import de.hetzge.sgame.App;
import de.hetzge.sgame.network.IF_Event;

public class EventStartGame implements IF_Event {

	@Override
	public void execute() {
		App.game.start();
		App.timing.startFrameTimer();
	}

}
