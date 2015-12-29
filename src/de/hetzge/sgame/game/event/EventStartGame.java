package de.hetzge.sgame.game.event;

import de.hetzge.sgame.App;
import de.hetzge.sgame.frame.FrameModule;

public class EventStartGame implements IF_Event {

	@Override
	public void execute() {
		App.game.start();
		FrameModule.instance.startFrameTimer();
	}

}
