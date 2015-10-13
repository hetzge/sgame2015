package de.hetzge.sgame.game;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.game.event.request.EventRequestGoto;
import de.hetzge.sgame.misc.Constant;

public class IngameInputProcessor implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		LocalGameState localGameState = App.game.getLocalGameState();
		switch (keycode) {
		case Keys.F1:
			localGameState.toggleShowCollision();
			break;
		case Keys.F2:
			localGameState.toggleShowRegistrations();
			break;
		case Keys.F3:
			localGameState.toggleShowPaths();
			break;
		case Keys.F4:
			localGameState.toggleShowIds();
			break;
		case Keys.F5:
			localGameState.toggleShowWorldOwner();
			break;
		case Keys.F6:
			localGameState.toggleShowDoors();
			break;
		case Keys.DEL:
			// TEMP
			Entity entity = App.game.getEntityManager().get(0);
			App.entityFunction.destroyEntity(entity);

			break;
		}

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

		if (button == Input.Buttons.LEFT) {
			LocalGameState localGameState = App.game.getLocalGameState();
			boolean hasSelection = localGameState.hasSelection();
			if (hasSelection) {
				Vector2 worldPosition = App.libGdxApplication.unproject(x, y);
				Set<Entity> selectionEntities = localGameState.getSelection();
				List<Integer> selectionEntityIds = selectionEntities.stream().map(Entity::getId).collect(Collectors.toList());
				EventRequestGoto eventRequestGoto = new EventRequestGoto(selectionEntityIds, (short) (worldPosition.x / Constant.TILE_SIZE), (short) (worldPosition.y / Constant.TILE_SIZE));
				App.network.sendOrSelf(eventRequestGoto);
			}
		}

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

		if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)) {
			camera.zoom -= cameraZoomSpeed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)) {
			camera.zoom += cameraZoomSpeed;
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
		if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0)) {
			camera.zoom = 1;
		}

		camera.position.x = MathUtils.clamp(camera.position.x, 0, App.game.getWorld().getPixelWidth());
		camera.position.y = MathUtils.clamp(camera.position.y, -App.game.getWorld().getPixelHeight(), 0);
	}

}
