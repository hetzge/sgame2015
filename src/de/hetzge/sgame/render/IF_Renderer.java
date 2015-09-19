package de.hetzge.sgame.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.hetzge.sgame.App;

public interface IF_Renderer {
	public default OrthographicCamera getCamera() {
		return App.libGdxApplication.getCamera();
	}

	public default SpriteBatch getSpriteBatch() {
		return App.libGdxApplication.getSpriteBatch();
	}

	public default ShapeRenderer getShapeRenderer() {
		return App.libGdxApplication.getShapeRenderer();
	}

	public default BitmapFont getBitmapFont() {
		return App.ressources.getBitmapFont();
	}
}
