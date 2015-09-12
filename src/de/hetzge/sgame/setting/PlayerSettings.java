package de.hetzge.sgame.setting;

import java.io.Serializable;

public class PlayerSettings implements Serializable {

	private byte playerId;
	private String playerName;

	public PlayerSettings() {
	}

	public PlayerSettings(byte playerId, String playerName) {
		this.playerId = playerId;
		this.playerName = playerName;
	}

	public byte getPlayerId() {
		return playerId;
	}

	public void setPlayerId(byte playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
