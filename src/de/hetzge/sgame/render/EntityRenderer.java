package de.hetzge.sgame.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.misc.Constant;

public class EntityRenderer implements IF_Renderer {

	public void render(Entity entity) {
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

		renderId(entity);
		renderPath(entity);
		renderRegistration(entity);
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
}
