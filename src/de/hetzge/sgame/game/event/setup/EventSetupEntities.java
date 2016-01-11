package de.hetzge.sgame.game.event.setup;

import java.util.Arrays;
import java.util.HashSet;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.game.event.IF_Event;

public class EventSetupEntities implements IF_Event {

	private final Entity[] entities;

	public EventSetupEntities(Entity[] entities) {
		this.entities = entities;
	}

	@Override
	public void execute() {
		App.getGame().getWorld().getEntityGrid().set(this.entities);
		new HashSet<>(Arrays.asList(this.entities)).stream().filter(entity -> entity != null)
				.forEach(App.getGame().getEntityManager()::register);
	}

}
