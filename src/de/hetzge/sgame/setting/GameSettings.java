package de.hetzge.sgame.setting;

import java.io.Serializable;

public class GameSettings implements Serializable {

	private short worldSizeX = 100;
	private short worldSizeY = 100;
	private short maxPlayers = 4;

	public short getWorldSizeX() {
		return worldSizeX;
	}

	public void setWorldSizeX(short worldSizeX) {
		this.worldSizeX = worldSizeX;
	}

	public short getWorldSizeY() {
		return worldSizeY;
	}

	public void setWorldSizeY(short worldSizeY) {
		this.worldSizeY = worldSizeY;
	}

	public short getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(short maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

}
