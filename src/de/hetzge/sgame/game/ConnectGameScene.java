package de.hetzge.sgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import de.hetzge.sgame.App;
import de.hetzge.sgame.network.NetworkModule;

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

		if (!NetworkModule.instance.isConnected()) {
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
		NetworkModule.instance.connect();
		if (isClient()) {
			App.function.sendHandshake();
		}
	}

	private boolean isHost() {
		return NetworkModule.networkSettings.isHost();
	}

	private boolean isClient() {
		return NetworkModule.networkSettings.isClient();
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
