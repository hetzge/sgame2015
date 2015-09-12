package de.hetzge.sgame.game;

import com.badlogic.gdx.Screen;

public enum E_GameScene {

	LOAD(new LoadGameScene()), CONNECT(new ConnectGameScene()), INGAME(new IngameScene());

	public static final E_GameScene[] values = values();

	private final Screen screen;

	private E_GameScene(Screen screen) {
		this.screen = screen;
	}

	public Screen getScreen() {
		return screen;
	}

}
