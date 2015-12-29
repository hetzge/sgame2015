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
import de.hetzge.sgame.game.event.IF_ConnectionEvent;
import de.hetzge.sgame.game.event.IF_Event;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.network.NetworkModule;
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

		App.entityFunction.createEntity(E_EntityType.TREE, (short) 0, (short) 0, (short) 1, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.TREE, (short) 5, (short) 4, (short) 8, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.TREE, (short) 5, (short) 3, (short) 9, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.CAIRN, (short) 6, (short) 5, (short) 10, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.CAIRN, (short) 6, (short) 4, (short) 11, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.CAIRN, (short) 6, (short) 3, (short) 12, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.BUILDING_QUARRY, (short) 10, (short) 10, (short) 2, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.BUILDING_LUMBERJACK, (short) 10, (short) 30, (short) 4, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.WORKER_MASON, (short) 1, (short) 2, (short) 3, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.WORKER_LUMBERJACK, (short) 1, (short) 1, (short) 0, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.WORKER_LUMBERJACK, (short) 12, (short) 3, (short) 5, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.FACTORY, (short) 15, (short) 15, (short) 7, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.CARRIER, (short) 12, (short) 12, (short) 6, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.CARRIER, (short) 12, (short) 13, (short) 13, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.CARRIER, (short) 12, (short) 14, (short) 14, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.CARRIER, (short) 12, (short) 15, (short) 15, (byte) 0);
		App.entityFunction.createEntity(E_EntityType.CARRIER, (short) 12, (short) 16, (short) 16, (byte) 0);

		// for (int i = 0; i < 100; i++) {
		// App.game.getLocalGameState().addSelection(App.game.getEntityManager().get(i));
		// }

		App.game.getWorld().getContainerGrid().get((short) 3, (short) 3).set(E_Item.WOOD, 8);
		App.game.getWorld().getContainerGrid().get((short) 3, (short) 3).set(E_Item.STONE, 8);
		App.game.getWorld().getContainerGrid().get((short) 3, (short) 3).set(E_Item.FISCH, 8);
		App.game.getWorld().getContainerGrid().get((short) 3, (short) 3).set(E_Item.BREAD, 8);

	}

	public void sendHandshake() {
		NetworkModule.instance.send(new EventPlayerHandshake(App.settings.getSystemSettings().getPlayerName()));
	}

}
