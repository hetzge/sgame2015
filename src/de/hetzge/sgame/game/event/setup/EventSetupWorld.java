package de.hetzge.sgame.game.event.setup;

import de.hetzge.sgame.App;
import de.hetzge.sgame.network.IF_Event;
import de.hetzge.sgame.world.World;

public class EventSetupWorld implements IF_Event {

	private final World world;
	
	public EventSetupWorld(World world) {
		this.world = world;
	}

	@Override
	public void execute() {
		App.game.setWorld(world);
	}
	
}
