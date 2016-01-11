package de.hetzge.sgame.function;

import de.hetzge.sgame.App;
import de.hetzge.sgame.game.Players;
import de.hetzge.sgame.game.event.EventPlayerHandshake;
import de.hetzge.sgame.game.format.GameFormat;
import de.hetzge.sgame.game.format.GameFormat.ContainerFormat;
import de.hetzge.sgame.game.format.GameFormat.EntityFormat;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Util;
import de.hetzge.sgame.network.NetworkModule;
import de.hetzge.sgame.setting.PlayerSettings;
import de.hetzge.sgame.setting.SystemSettings;
import de.hetzge.sgame.world.World;

public class Function {

	public void initGame(GameFormat gameFormat) {
		int worldWidth = gameFormat.getWorldWidth();
		int worldHeight = gameFormat.getWorldHeight();
		short[] tiles = gameFormat.getTiles();
		boolean[] collision = gameFormat.getCollision();
		EntityFormat[] entityFormats = gameFormat.getEntities();
		ContainerFormat[][] containerFormatss = gameFormat.getContainers();

		SystemSettings systemSettings = App.settings.getSystemSettings();
		Players players = new Players();
		PlayerSettings playerSettings = new PlayerSettings(players.nextPlayerId(), systemSettings.getPlayerName());
		players.addPlayer(playerSettings);

		World world = new World((short) worldWidth, (short) worldHeight);
		world.getTileGrid().set(tiles);
		world.getFixedCollisionGrid().set(collision);
		App.getGame().setWorld(world);
		App.getGame().setSelf(playerSettings);
		App.getGame().setPlayers(players);

		// TODO set containers

		int entityId = 0;

		for (int index = 0; index < entityFormats.length; index++) {
			EntityFormat entityFormat = entityFormats[index];
			if (entityFormat != null) {
				int x = Util.unIndexX(index, worldWidth);
				int y = Util.unIndexY(index, worldWidth);
				App.entityFunction.createEntity(entityFormat.getEntityType(), (short) x, (short) y, entityId++,
						(byte) 0);
			}
		}

		for (int index = 0; index < containerFormatss.length; index++) {
			ContainerFormat[] containerFormats = containerFormatss[index];
			if (containerFormats != null) {
				int x = Util.unIndexX(index, worldWidth);
				int y = Util.unIndexY(index, worldWidth);
				for (int i = 0; i < containerFormats.length; i++) {
					ContainerFormat containerFormat = containerFormats[i];
					if (containerFormat != null) {
						E_Item item = containerFormat.getItem();
						int count = containerFormat.getCount();
						App.getGame().getWorld().getContainerGrid().get((short) x, (short) y).set(item, count);
					}
				}
			}
		}
	}

	public void sendHandshake() {
		NetworkModule.instance.send(new EventPlayerHandshake(App.settings.getSystemSettings().getPlayerName()));
	}

}
