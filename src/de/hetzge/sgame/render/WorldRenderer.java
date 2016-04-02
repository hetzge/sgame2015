package de.hetzge.sgame.render;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import de.hetzge.sgame.App;
import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.render.shader.ReplaceColorShader;
import de.hetzge.sgame.world.ContainerGrid;
import de.hetzge.sgame.world.GridPosition;
import de.hetzge.sgame.world.OwnerGrid;
import de.hetzge.sgame.world.World;

public class WorldRenderer implements IF_Renderer {

	private List<Entity> visibleEntities = Collections.emptyList();

	public void render() {
		render(App.getGame().getWorld());
	}

	public void render(World world) {
		OrthographicCamera camera = getCamera();

		short width = world.getWidth();
		short height = world.getHeight();

		float effectivCameraWidth = camera.viewportWidth * camera.zoom;
		float effectivCameraHeight = camera.viewportHeight * camera.zoom;

		short _minX = (short) ((camera.position.x - effectivCameraWidth / 2) / Constant.TILE_SIZE);
		short _maxX = (short) ((_minX + effectivCameraWidth / Constant.TILE_SIZE) + 3);
		short minX = MathUtils.clamp(_minX, (short) 0, (short) (width - 1));
		short maxX = MathUtils.clamp(_maxX, (short) 1, width);

		short _minY = (short) ((-camera.position.y - effectivCameraHeight / 2) / Constant.TILE_SIZE);
		short _maxY = (short) ((_minY + effectivCameraHeight / Constant.TILE_SIZE) + 3);
		short minY = MathUtils.clamp(_minY, (short) 0, (short) (height - 1));
		short maxY = MathUtils.clamp(_maxY, (short) 1, height);

		Set<Entity> duplicates = new HashSet<>();
		List<Entity> entitiesToRender = new LinkedList<>();
		for (short x = minX; x < maxX; x++) {
			for (short y = minY; y < maxY; y++) {
				renderTile(world, x, y);
				Entity entity = App.getGame().getWorld().getEntityGrid().get(x, y);
				if (entity != null && duplicates.add(entity)) {
					entitiesToRender.add(entity);
				}
			}
		}

		if (App.getGame().getLocalGameState().isShowWorldOwnerBorder()) {
			ReplaceColorShader shader = new ReplaceColorShader(getSpriteBatch());
			for (short x = minX; x < maxX; x++) {
				for (short y = minY; y < maxY; y++) {
					renderBorder(world, shader, x, y);
				}
			}
		}

		if (App.getGame().getLocalGameState().isShowCollisions()) {
			for (short x = minX; x < maxX; x++) {
				for (short y = minY; y < maxY; y++) {
					renderCollision(world, x, y);
				}
			}
		}

		if (App.getGame().getLocalGameState().isShowWorldOwnerColor()) {
			for (short x = minX; x < maxX; x++) {
				for (short y = minY; y < maxY; y++) {
					renderWorldOwnerColor(world, x, y);
				}
			}
		}

		if (App.getGame().getLocalGameState().isShowWorldOwner()) {
			for (short x = minX; x < maxX; x++) {
				for (short y = minY; y < maxY; y++) {
					renderWorldOwner(world, x, y);
				}
			}
		}

		ContainerGrid containerGrid = App.getGame().getWorld().getContainerGrid();
		for (short x = minX; x < maxX; x++) {
			for (short y = minY; y < maxY; y++) {
				Container<E_Item> container = containerGrid.get(x, y);
				if (container != null) {
					App.itemRenderer.renderContainer(container, x, y);
				}
			}
		}

		this.visibleEntities = entitiesToRender;
	}

	private void renderBorder(World world, ReplaceColorShader shader, short x, short y) {
		OwnerGrid ownerGrid = world.getOwnerGrid();
		GridPosition gridPosition = new GridPosition(x, y);
		boolean isBorder = ownerGrid.isBorder(gridPosition);
		if (isBorder) {
			byte ownership = ownerGrid.getOwnership(gridPosition);
			Color color = Constant.COLORS[ownership];

			shader.begin();
			shader.setReplaceColor(Constant.DEFAULT_REPLACEMENT_COLOR);
			shader.setReplaceWithColor(color);

			Texture border = App.ressources.getBorder();
			getSpriteBatch().draw(border, x * Constant.TILE_SIZE, -y * Constant.TILE_SIZE);
			shader.end();
		}
	}

	private void renderTile(World world, short x, short y) {
		short tileId = world.getTileGrid().get(x, y);
		TextureRegion tileTextureRegion = App.ressources.getTileTextureRegion(tileId);
		getSpriteBatch().draw(tileTextureRegion, x * Constant.TILE_SIZE, -(y * Constant.TILE_SIZE), Constant.TILE_SIZE,
				Constant.TILE_SIZE);
	}

	private void renderCollision(World world, short x, short y) {
		boolean collision = world.getFixedCollisionGrid().is(x, y);
		if (collision) {
			getShapeRenderer().setColor(Color.YELLOW);
			getShapeRenderer().rect(x * Constant.TILE_SIZE, -y * Constant.TILE_SIZE, Constant.TILE_SIZE,
					Constant.TILE_SIZE);
		}
	}

	private void renderWorldOwner(World world, short x, short y) {
		byte ownership = world.getOwnerGrid().getOwnership(x, y);
		if (ownership != Constant.GAIA_PLAYER_ID) {
			getBitmapFont().draw(getSpriteBatch(), "" + ownership, x * Constant.TILE_SIZE, -y * Constant.TILE_SIZE);
		}
	}

	private void renderWorldOwnerColor(World world, short x, short y) {
		byte ownership = world.getOwnerGrid().getOwnership(x, y);
		if (ownership != Constant.GAIA_PLAYER_ID) {
			getShapeRenderer().setColor(Constant.COLORS[ownership]);
			getShapeRenderer().rect(x * Constant.TILE_SIZE, -y * Constant.TILE_SIZE, Constant.TILE_SIZE,
					Constant.TILE_SIZE);
		}
	}

	public List<Entity> getVisibleEntities() {
		return this.visibleEntities;
	}

}
