package de.hetzge.sgame.function;

import java.io.Serializable;
import java.util.Objects;

import org.nustaq.net.TCPObjectSocket;
import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.frame.IF_FrameEvent;
import de.hetzge.sgame.game.Players;
import de.hetzge.sgame.game.event.EventPlayerHandshake;
import de.hetzge.sgame.network.IF_ConnectionEvent;
import de.hetzge.sgame.network.IF_Event;
import de.hetzge.sgame.setting.GameSettings;
import de.hetzge.sgame.setting.PlayerSettings;
import de.hetzge.sgame.world.EntityGrid;
import de.hetzge.sgame.world.World;

public class Function implements IF_Function {

	@Override
	public void initGame() {
		GameSettings gameSettings = App.settings.getGameSettings();
		Players players = new Players();
		PlayerSettings playerSettings = new PlayerSettings(players.nextPlayerId(), App.settings.getSystemSettings().getPlayerName());
		players.addPlayer(playerSettings);

		App.game.setWorld(new World(gameSettings.getWorldSizeX(), gameSettings.getWorldSizeY()));
		App.game.setEntityGrid(new EntityGrid(gameSettings.getWorldSizeX(), gameSettings.getWorldSizeY()));
		App.game.setSelf(playerSettings);
		App.game.setPlayers(players);
	}

	@Override
	public void dispatch(Object object, TCPObjectSocket tcpObjectSocket) {
		if (object instanceof IF_FrameEvent) {
			IF_FrameEvent frameEvent = (IF_FrameEvent) object;
			App.timing.addFrameEvent(frameEvent);
		} else if (object instanceof IF_ConnectionEvent) {
			IF_ConnectionEvent connectionEvent = (IF_ConnectionEvent) object;
			Objects.requireNonNull(tcpObjectSocket);
			connectionEvent.execute(tcpObjectSocket);
		} else if (object instanceof IF_Event) {
			IF_Event event = (IF_Event) object;
			event.execute();
		} else {
			Logger.warn("unhandelt object: " + object);
		}
	}

	@Override
	public void send(TCPObjectSocket tcpObjectSocket, Serializable object) {
		try {
			tcpObjectSocket.writeObject(object);
			tcpObjectSocket.flush();
		} catch (Exception e) {
			Logger.error(e, "error while sending object");
		}
	}

	@Override
	public void sendHandshake() {
		App.network.send(new EventPlayerHandshake(App.settings.getSystemSettings().getPlayerName()));
	}

}
