package de.hetzge.sgame.game;

import de.hetzge.sgame.entity.EntityManager;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.setting.PlayerSettings;
import de.hetzge.sgame.world.EntityGrid;
import de.hetzge.sgame.world.World;

public class Game {

	private boolean started = false;
	private long seed = 0;
	private World world = null;
	private EntityGrid entityGrid = null;
	private Players players = null;
	private PlayerSettings self = null;
	private final EntityManager entityManager = new EntityManager();

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public EntityGrid getEntityGrid() {
		return entityGrid;
	}

	public void setEntityGrid(EntityGrid entityGrid) {
		this.entityGrid = entityGrid;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public Players getPlayers() {
		return players;
	}

	public void setPlayers(Players players) {
		this.players = players;
	}

	public PlayerSettings getSelf() {
		return self;
	}

	public void setSelf(PlayerSettings self) {
		this.self = self;
	}

	public boolean isStarted() {
		return started;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void start() {
		started = true;
		if (!isReadyToStart()) {
			throw new InvalidGameStateException("Try to start non ready game.");
		}
	}

	public boolean isComplete() {
		return world != null && entityGrid != null && self != null && players != null;
	}

	public boolean isReadyToStart() {
		return isComplete() && isStarted();
	}

}
