package de.hetzge.sgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import de.hetzge.sgame.App;

public class IngameInputProcessor implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public void render() {
		OrthographicCamera camera = App.libGdxApplication.getCamera();
		float cameraZoomSpeed = App.settings.getSystemSettings().getCameraZoomSpeed();
		float cameraMoveSpeed = App.settings.getSystemSettings().getCameraMoveSpeed();

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.zoom += cameraZoomSpeed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			camera.zoom -= cameraZoomSpeed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-cameraMoveSpeed, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(cameraMoveSpeed, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -cameraMoveSpeed, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, cameraMoveSpeed, 0);
		}
		
		camera.position.x = MathUtils.clamp(camera.position.x, 0, App.game.getWorld().getPixelWidth());
		camera.position.y = MathUtils.clamp(camera.position.y, -App.game.getWorld().getPixelHeight(), 0);
	}

}
