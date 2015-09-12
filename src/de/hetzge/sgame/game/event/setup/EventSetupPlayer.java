package de.hetzge.sgame.game.event.setup;

import de.hetzge.sgame.App;
import de.hetzge.sgame.network.IF_Event;
import de.hetzge.sgame.setting.PlayerSettings;

public class EventSetupPlayer implements IF_Event {

	private final PlayerSettings playerSettings;

	public EventSetupPlayer(PlayerSettings playerSettings) {
		this.playerSettings = playerSettings;
	}

	@Override
	public void execute() {
		App.game.setSelf(playerSettings);
	}

}
