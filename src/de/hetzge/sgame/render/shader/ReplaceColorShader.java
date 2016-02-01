package de.hetzge.sgame.render.shader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.hetzge.sgame.App;

public class ReplaceColorShader extends SpriteBatchShader {

	private static final String UNIFORM_REPLACE_COLOR = "replace_color";
	private static final String UNIFORM_REPLACE_WITH = "replace_with";

	public ReplaceColorShader(SpriteBatch spriteBatch) {
		super(spriteBatch, App.ressources.getReplaceColorShader());
	}

	public void setReplaceWithColor(Color color) {
		getShaderProgram().setUniformf(UNIFORM_REPLACE_WITH, color);
	}

	public void setReplaceColor(Color color) {
		getShaderProgram().setUniformf(UNIFORM_REPLACE_COLOR, color);
	}

}
