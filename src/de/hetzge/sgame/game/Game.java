package de.hetzge.sgame.game;

import de.hetzge.sgame.entity.EntityManager;
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
	private final LocalGameState localGameState = new LocalGameState();

	public World getWorld() {
		return this.world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public EntityGrid getEntityGrid() {
		return this.entityGrid;
	}

	public void setEntityGrid(EntityGrid entityGrid) {
		this.entityGrid = entityGrid;
	}

	public long getSeed() {
		return this.seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public Players getPlayers() {
		return this.players;
	}

	public void setPlayers(Players players) {
		this.players = players;
	}

	public PlayerSettings getSelf() {
		return this.self;
	}

	public void setSelf(PlayerSettings self) {
		this.self = self;
	}

	public boolean isStarted() {
		return this.started;
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public void start() {
		this.started = true;
		if (!isReadyToStart()) {
			throw new IllegalStateException("Try to start non ready game.");
		}
	}

	public boolean isComplete() {
		return this.world != null && this.entityGrid != null && this.self != null && this.players != null;
	}

	public boolean isReadyToStart() {
		return isComplete() && isStarted();
	}

	public LocalGameState getLocalGameState() {
		return this.localGameState;
	}

}
