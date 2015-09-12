package de.hetzge.sgame.game.event;

import org.nustaq.net.TCPObjectSocket;

import de.hetzge.sgame.App;
import de.hetzge.sgame.game.event.setup.EventSetupEntities;
import de.hetzge.sgame.game.event.setup.EventSetupPlayer;
import de.hetzge.sgame.game.event.setup.EventSetupWorld;
import de.hetzge.sgame.network.IF_ConnectionEvent;
import de.hetzge.sgame.setting.PlayerSettings;

public class EventPlayerHandshake implements IF_ConnectionEvent {

	private final String playerName;

	public EventPlayerHandshake(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public void execute(TCPObjectSocket tcpObjectSocket) {
		PlayerSettings playerSettings = new PlayerSettings(App.game.getPlayers().nextPlayerId(), playerName);
		App.function.send(tcpObjectSocket, new EventSetupPlayer(playerSettings));
		App.function.send(tcpObjectSocket, new EventSetupEntities(App.game.getEntityGrid()));
		App.function.send(tcpObjectSocket, new EventSetupWorld(App.game.getWorld()));
	}

}
