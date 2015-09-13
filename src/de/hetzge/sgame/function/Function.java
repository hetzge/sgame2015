package de.hetzge.sgame.function;

import java.io.Serializable;
import java.util.Objects;

import org.nustaq.net.TCPObjectSocket;
import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
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

		int id = 1;
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				App.entityFunction.createEntity(E_EntityType.DUMMY, (short) (3 + x), (short) (3 + y), id++, (byte) 0);
			}
		}

		App.entityFunction.createEntity(E_EntityType.DUMMY, (short) 0, (short) 0, 0, (byte) 0);
		Entity entity = App.game.getEntityManager().get(0);

		entity.setPath(new short[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 }, new short[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 });
		entity.setActivity(E_Activity.WALKING);

		// TODO TEMP
		App.game.getLocalGameState().addSelection(entity);
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
