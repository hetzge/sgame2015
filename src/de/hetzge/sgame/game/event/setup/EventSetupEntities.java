package de.hetzge.sgame.game.event.setup;

import de.hetzge.sgame.App;
import de.hetzge.sgame.game.event.IF_Event;
import de.hetzge.sgame.world.EntityGrid;

public class EventSetupEntities implements IF_Event {

	private final EntityGrid entityGrid;

	public EventSetupEntities(EntityGrid entityGrid) {
		this.entityGrid = entityGrid;
	}

	@Override
	public void execute() {
		App.game.setEntityGrid(this.entityGrid);
		this.entityGrid.getAllEntities().stream().forEach(App.game.getEntityManager()::register);
	}

}
