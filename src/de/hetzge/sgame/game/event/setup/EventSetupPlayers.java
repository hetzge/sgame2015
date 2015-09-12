package de.hetzge.sgame.game.event.setup;

import de.hetzge.sgame.App;
import de.hetzge.sgame.game.Players;
import de.hetzge.sgame.network.IF_Event;

public class EventSetupPlayers implements IF_Event {

	private final Players players;

	public EventSetupPlayers(Players players) {
		this.players = players;
	}

	@Override
	public void execute() {
		App.game.setPlayers(players);
	}

}
