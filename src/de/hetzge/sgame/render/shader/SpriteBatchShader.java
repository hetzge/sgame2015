package de.hetzge.sgame.render.shader;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class SpriteBatchShader {

	private final SpriteBatch spriteBatch;
	private final ShaderProgram shaderProgram;

	public SpriteBatchShader(SpriteBatch spriteBatch, ShaderProgram shaderProgram) {
		this.spriteBatch = spriteBatch;
		this.shaderProgram = shaderProgram;
	}

	public void begin() {
		spriteBatch.setShader(shaderProgram);
	}

	public void end() {
		spriteBatch.setShader(null);
	}

	protected ShaderProgram getShaderProgram() {
		return shaderProgram;
	}
	
}
