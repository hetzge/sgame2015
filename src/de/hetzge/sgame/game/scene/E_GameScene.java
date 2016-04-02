package de.hetzge.sgame.game.scene;

import com.badlogic.gdx.Screen;

/**
 * The game is splited in different scenes which are described with this enum. A
 * scene is differentiated by the input, the update loop and the rendered
 * content.
 * 
 * @author hetzge
 */
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
