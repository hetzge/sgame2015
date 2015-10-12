package de.hetzge.sgame.render;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.world.World;

public class WorldRenderer implements IF_Renderer {

	private List<Entity> visibleEntities = Collections.emptyList();

	public void render(World world) {
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

		Set<Entity> duplicates = new HashSet<>();
		List<Entity> entitiesToRender = new LinkedList<>();
		for (short x = minX; x < maxX; x++) {
			for (short y = minY; y < maxY; y++) {
				renderTile(world, x, y);
				Entity entity = App.game.getEntityGrid().get(x, y);
				if (entity != null && duplicates.add(entity)) {
					entitiesToRender.add(entity);
				}
			}
		}

		if (App.game.getLocalGameState().isShowCollisions()) {
			for (short x = minX; x < maxX; x++) {
				for (short y = minY; y < maxY; y++) {
					renderCollision(world, x, y);
				}
			}
		}

		visibleEntities = entitiesToRender;
	}

	public void renderTile(World world, short x, short y) {
		short tileId = world.getTileGrid().get(x, y);
		TextureRegion tileTextureRegion = App.ressources.getTileTextureRegion(tileId);
		getSpriteBatch().draw(tileTextureRegion, x * Constant.TILE_SIZE, -(y * Constant.TILE_SIZE));
	}

	public void renderCollision(World world, short x, short y) {
		boolean collision = world.getFixedCollisionGrid().is(x, y);
		if (collision) {
			getShapeRenderer().setColor(Color.YELLOW);
			getShapeRenderer().rect(x * Constant.TILE_SIZE, -y * Constant.TILE_SIZE, Constant.TILE_SIZE, Constant.TILE_SIZE);
		}
	}

	public List<Entity> getVisibleEntities() {
		return visibleEntities;
	}

}
