package de.hetzge.sgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import de.hetzge.sgame.App;

public class IngameScene implements Screen {

	private IngameInputProcessor ingameInputProcessor = new IngameInputProcessor();;

	@Override
	public void show() {
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(ingameInputProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		App.renderer.renderWorld();
		App.updater.update();
		ingameInputProcessor.render();
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

}
