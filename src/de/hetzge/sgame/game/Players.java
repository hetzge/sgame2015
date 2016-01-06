package de.hetzge.sgame.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hetzge.sgame.setting.PlayerSettings;

public class Players implements Serializable {

	private final List<PlayerSettings> players = new ArrayList<>();
	private transient byte nextPlayerId = 0;
	
	public void addPlayer(PlayerSettings playerSettings){
		this.players.add(playerSettings);
	}
	
	public synchronized byte nextPlayerId(){
		return this.nextPlayerId++;
	}
	
}
