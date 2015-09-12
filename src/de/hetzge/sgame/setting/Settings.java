package de.hetzge.sgame.setting;

import de.hetzge.sgame.network.E_NetworkRole;

public class Settings {

	private SystemSettings systemSettings = new SystemSettings();
	private NetworkSettings networkSettings = new NetworkSettings();
	private GameSettings gameSettings = new GameSettings();

	public NetworkSettings getNetworkSettings() {
		return networkSettings;
	}

	public void setClientSettings(NetworkSettings clientSettings) {
		this.networkSettings = clientSettings;
	}

	public GameSettings getGameSettings() {
		return gameSettings;
	}

	public void setGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	public SystemSettings getSystemSettings() {
		return systemSettings;
	}


}
