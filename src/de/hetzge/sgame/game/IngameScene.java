package de.hetzge.sgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import de.hetzge.sgame.App;
import de.hetzge.sgame.game.input.IngameInputProcessor;

public class IngameScene implements Screen {

	private final IngameInputProcessor ingameInputProcessor = new IngameInputProcessor();
	private final Thread updateThread = new Thread(this::update, "update");

	@Override
	public void show() {
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this.ingameInputProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);
		this.updateThread.start();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		App.renderer.render();

		this.ingameInputProcessor.render();
	}

	public void update() {
		boolean running = true;
		while (running) {
			App.timing.update();
		}
	}

	@Override
	public void hide() {
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

	public IngameInputProcessor getIngameInputProcessor() {
		return this.ingameInputProcessor;
	}

}
