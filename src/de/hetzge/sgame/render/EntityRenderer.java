package de.hetzge.sgame.render;

import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.IF_RenderItemsJob;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.Util;

public class EntityRenderer implements IF_Renderer {

	public void render() {
		List<Entity> entities = App.worldRenderer.getVisibleEntities();
		for (Entity entity : entities) {
			render(entity);
		}

		Set<Entity> selection = App.game.getLocalGameState().getSelection();
		for (Entity entity : selection) {
			App.entityRenderer.renderSelected(entity);
		}
	}

	public void render(Entity entity) {
		float stateTime = App.timing.getStateTime();
		float renderX = entity.getRenderX();
		float renderY = entity.getRenderY();
		Animation animation = App.ressources.getGraphic(entity);
		TextureRegion keyFrame = animation.getKeyFrame(stateTime, true);
		int regionWidth = keyFrame.getRegionWidth();
		// int regionHeight = keyFrame.getRegionHeight();
		int entityWidth = entity.getWidth() * Constant.TILE_SIZE;
		// int entityHeight = entity.getHeight() * Constant.TILE_SIZE;
		int offsetX = (entityWidth - regionWidth) / 2;
		// int offsetY = (entityHeight - regionHeight) / 2;
		getSpriteBatch().draw(keyFrame, renderX + offsetX, -renderY);

		renderItems(entity);
		renderItem(entity);
		renderId(entity);
		renderPath(entity);
		renderRegistration(entity);
		renderDoor(entity);
	}

	public void renderSelected(Entity entity) {
		float renderX = entity.getRenderX();
		float renderY = entity.getRenderY();
		float width = Util.gridToWorld(entity.getDefinition().getWidth());
		ShapeRenderer shapeRenderer = getShapeRenderer();
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.line(renderX, -renderY, renderX + width, -renderY);
	}

	public void renderItems(Entity entity) {
		EntityJob job = entity.getJob();
		if (job instanceof IF_RenderItemsJob) {
			IF_RenderItemsJob renderItemsJob = (IF_RenderItemsJob) job;
			Container left = renderItemsJob.getRenderLeftContainer();
			Container right = renderItemsJob.getRenderRightContainer();

			short doorX = entity.getDoorX();
			short doorY = entity.getDoorY();

			float doorXLeft = doorX * Constant.TILE_SIZE;
			float doorYLeft = doorY * Constant.TILE_SIZE - Constant.TILE_SIZE - Constant.HALF_TILE_SIZE;

			float doorXRight = doorXLeft + Constant.TILE_SIZE;
			float doorYRight = doorYLeft;

			if (left != null) {
				App.itemRenderer.renderContainer(left, doorXLeft, doorYLeft);
			}
			if (right != null) {
				App.itemRenderer.renderContainer(right, doorXRight, doorYRight);
			}
		}
	}

	public void renderItem(Entity entity) {
		E_Item item = entity.getItem();
		if (item != null) {
			float renderX = entity.getRenderX();
			float renderY = entity.getRenderY();
			TextureRegion itemTextureRegion = App.ressources.getItemTextureRegion(item);
			getSpriteBatch().draw(itemTextureRegion, renderX, -renderY);
		}
	}

	public void renderId(Entity entity) {
		if (App.game.getLocalGameState().isShowIds()) {
			float renderX = entity.getRenderX();
			float renderY = entity.getRenderY();
			int id = entity.getId();
			getShapeRenderer().setColor(Color.WHITE);
			getBitmapFont().draw(getSpriteBatch(), id + "", renderX, -renderY);
		}
	}

	public void renderPath(Entity entity) {
		if (App.game.getLocalGameState().isShowPaths() && entity.hasPath()) {
			short pathGoalX = entity.getPathGoalX();
			short pathGoalY = entity.getPathGoalY();
			float renderX = entity.getRenderX();
			float renderY = entity.getRenderY();
			getShapeRenderer().setColor(Color.WHITE);
			getShapeRenderer().line(renderX, -renderY, pathGoalX * Constant.TILE_SIZE, -pathGoalY * Constant.TILE_SIZE);
		}
	}

	public void renderRegistration(Entity entity) {
		if (App.game.getLocalGameState().isShowRegistrations()) {
			short registeredX = entity.getRegisteredX();
			short registeredY = entity.getRegisteredY();
			float renderX = entity.getRenderX();
			float renderY = entity.getRenderY();
			getShapeRenderer().setColor(Color.RED);
			getShapeRenderer().line(renderX, -renderY, registeredX * Constant.TILE_SIZE, -registeredY * Constant.TILE_SIZE);
		}
	}

	public void renderDoor(Entity entity) {
		if (App.game.getLocalGameState().isShowDoors()) {
			if (!entity.getDefinition().isMoveable()) {
				short doorX = entity.getDoorX();
				short doorY = entity.getDoorY();
				getShapeRenderer().setColor(Color.GREEN);
				getShapeRenderer().rect(doorX * Constant.TILE_SIZE, -doorY * Constant.TILE_SIZE, Constant.TILE_SIZE, Constant.TILE_SIZE);
			}
		}
	}
}
