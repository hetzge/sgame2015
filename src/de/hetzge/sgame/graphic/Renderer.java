package de.hetzge.sgame.graphic;

import java.util.LinkedList;
import java.util.List;

import org.pmw.tinylog.Logger;

import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.world.World;

public class Renderer {

	private FPSLogger fpsLogger = new FPSLogger();

	public void renderWorld() {
		World world = App.game.getWorld();
		OrthographicCamera camera = getCamera();

		short width = world.getWidth();
		short height = world.getHeight();

		float effectivCameraWidth = camera.viewportWidth * camera.zoom;
		float effectivCameraHeight = camera.viewportHeight * camera.zoom;

		short minX = (short) ((camera.position.x - effectivCameraWidth / 2) / Constant.TILE_SIZE);
		short maxX = (short) ((minX + effectivCameraWidth / Constant.TILE_SIZE) + 3);
		minX = MathUtils.clamp(minX, (short) 0, (short) (width - 1));
		maxX = MathUtils.clamp(maxX, (short) 1, width);

		short minY = (short) ((-camera.position.y - effectivCameraHeight / 2) / Constant.TILE_SIZE);
		short maxY = (short) ((minY + effectivCameraHeight / Constant.TILE_SIZE) + 3);
		minY = MathUtils.clamp(minY, (short) 0, (short) (height - 1));
		maxY = MathUtils.clamp(maxY, (short) 1, height);

		List<Entity> entitiesToRender = new LinkedList<>();
		for (short x = minX; x < maxX; x++) {
			for (short y = minY; y < maxY; y++) {
				short tileId = world.getTileGrid().get(x, y);
				TextureRegion tileTextureRegion = App.ressources.getTileTextureRegion(tileId);
				getSpriteBatch().draw(tileTextureRegion, x * Constant.TILE_SIZE, -(y * Constant.TILE_SIZE));

				Entity entity = App.game.getEntityGrid().get(x, y);
				if (entity != null) {
					entitiesToRender.add(entity);
				}
			}
		}
		for (Entity entity : entitiesToRender) {
			renderEntity(entity);
		}

		fpsLogger.log();
	}

	public void renderEntity(Entity entity) {
		float stateTime = App.timing.getStateTime();
		float renderX = entity.getRenderX();
		float renderY = entity.getRenderY();
		Animation animation = App.ressources.getGraphic(entity);
		TextureRegion keyFrame = animation.getKeyFrame(stateTime, true);
		EntityDefinition definition = entity.getDefinition();
		int regionWidth = keyFrame.getRegionWidth();
		int regionHeight = keyFrame.getRegionHeight();
		int entityWidth = definition.getWidth() * Constant.TILE_SIZE;
		int entityHeight = definition.getHeight() * Constant.TILE_SIZE;
		int offsetX = (entityWidth - regionWidth) / 2;
		int offsetY = (entityHeight - regionHeight) / 2;
		getSpriteBatch().draw(keyFrame, renderX + offsetX, -renderY - offsetY);
	}

	private OrthographicCamera getCamera() {
		return App.libGdxApplication.getCamera();
	}

	private SpriteBatch getSpriteBatch() {
		return App.libGdxApplication.getSpriteBatch();
	}

}
