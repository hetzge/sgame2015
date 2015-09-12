package de.hetzge.sgame.game;

import com.badlogic.gdx.Screen;

import de.hetzge.sgame.App;

public class LoadGameScene implements Screen {

	@Override
	public void show() {
		App.ressources.init();
	}

	@Override
	public void render(float delta) {
		App.libGdxApplication.switchGameScene(E_GameScene.CONNECT);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

}
