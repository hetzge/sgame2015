package de.hetzge.sgame.setting;

public class Settings {

	private SystemSettings systemSettings = new SystemSettings();
	private GameSettings gameSettings = new GameSettings();

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
