package de.hetzge.sgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import de.hetzge.sgame.App;

public class ConnectGameScene implements Screen {

	private ConnectGameGui connectGameGui;

	public ConnectGameScene() {
	}

	@Override
	public void show() {
		connectGameGui = new ConnectGameGui();
		connectGameGui.show();
		Gdx.input.setInputProcessor(connectGameGui);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		connectGameGui.act();
		connectGameGui.draw();

		if (!App.network.isConnected()) {
			newGame();
		}
		if (App.game.isReadyToStart()) {
			startGame();
		} else if (isHost() && App.game.isComplete()) {
			connectGameGui.showStartButton();
		}
	}

	private void startGame() {
		App.libGdxApplication.switchGameScene(E_GameScene.INGAME);
	}

	private void newGame() {
		App.game = new Game();
		if (isHost()) {
			App.function.initGame();
		}
		App.network.connect(App.settings.getNetworkSettings());
		if (isClient()) {
			App.function.sendHandshake();
		}
	}

	private boolean isHost() {
		return App.settings.getNetworkSettings().isHost();
	}

	private boolean isClient() {
		return App.settings.getNetworkSettings().isClient();
	}

	@Override
	public void hide() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
