package de.hetzge.sgame.function;

import java.io.Serializable;
import java.util.Objects;

import org.nustaq.net.TCPObjectSocket;
import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.frame.IF_FrameEvent;
import de.hetzge.sgame.game.Players;
import de.hetzge.sgame.game.event.EventPlayerHandshake;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.network.IF_ConnectionEvent;
import de.hetzge.sgame.network.IF_Event;
import de.hetzge.sgame.setting.GameSettings;
import de.hetzge.sgame.setting.PlayerSettings;
import de.hetzge.sgame.setting.SystemSettings;
import de.hetzge.sgame.world.EntityGrid;
import de.hetzge.sgame.world.World;

public class Function {

	public void initGame() {
		GameSettings gameSettings = App.settings.getGameSettings();
		SystemSettings systemSettings = App.settings.getSystemSettings();
		Players players = new Players();
		PlayerSettings playerSettings = new PlayerSettings(players.nextPlayerId(), systemSettings.getPlayerName());
		players.addPlayer(playerSettings);

		App.game.setWorld(new World(gameSettings.getWorldSizeX(), gameSettings.getWorldSizeY()));

		App.game.setEntityGrid(new EntityGrid(gameSettings.getWorldSizeX(), gameSettings.getWorldSizeY()));
		App.game.setSelf(playerSettings);
		App.game.setPlayers(players);

		// int id = 1;
		// for (int x = 0; x < 10; x++) {
		// for (int y = 0; y < 10; y++) {
		// App.entityFunction.createEntity(E_EntityType.DUMMY, (short) (3 + x),
		// (short) (3 + y), id++, (byte) 0);
		// }
		// }

		App.entityFunction.createEntity(E_EntityType.PROVIDER, (short) 5, (short) 5, (short) 1, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.WORKSTATION, (short) 10, (short) 10, (short) 2, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.WORKSTATION, (short) 10, (short) 30, (short) 4, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.MINER, (short) 1, (short) 2, (short) 3, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.MINER, (short) 1, (short) 1, (short) 0, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.MINER, (short) 4, (short) 2, (short) 5, (byte) 0);
		// for (int i = 0; i < 100; i++) {
		// App.game.getLocalGameState().addSelection(App.game.getEntityManager().get(i));
		// }

		App.game.getWorld().getContainerGrid().get((short) 5, (short) 7).set(E_Item.WOOD, 4);
		App.game.getWorld().getContainerGrid().get((short) 5, (short) 7).set(E_Item.STONE, 4);
		App.game.getWorld().getContainerGrid().get((short) 5, (short) 7).set(E_Item.FISCH, 4);
		App.game.getWorld().getContainerGrid().get((short) 5, (short) 7).set(E_Item.BREAD, 2);

	}

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

	public void send(TCPObjectSocket tcpObjectSocket, Serializable object) {
		try {
			tcpObjectSocket.writeObject(object);
			tcpObjectSocket.flush();
		} catch (Exception e) {
			Logger.error(e, "error while sending object");
		}
	}

	public void sendHandshake() {
		App.network.send(new EventPlayerHandshake(App.settings.getSystemSettings().getPlayerName()));
	}

}
