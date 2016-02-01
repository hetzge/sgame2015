package de.hetzge.sgame.render;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderTest extends Game  {

	public static void main(String[] args) {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		new LwjglApplication(new ShaderTest(), configuration);
	}
	
	private SpriteBatch spriteBatch;
	private Texture texture;
	private ShaderProgram shaderProgram;
	private Sprite sprite;

	@Override
	public void create() {
		this.spriteBatch = new SpriteBatch();
		this.texture = new Texture(Gdx.files.internal("asset/border.png"));
		
	    this.sprite = new Sprite(this.texture);
        this.sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		String vertexShaderString = Gdx.files.internal("asset/shader/vertex.glsl").readString();
		String fragmentShaderString = Gdx.files.internal("asset/shader/fragment_owner.glsl").readString();
		this.shaderProgram = new ShaderProgram(vertexShaderString, fragmentShaderString);
	
		
		
		ShaderProgram.pedantic = false;
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		this.spriteBatch.begin();
//		this.texture.bind(1);
		
		this.spriteBatch.setShader(this.shaderProgram);
		this.spriteBatch.draw(this.texture, 0f, 0f, 40, 40);
		this.spriteBatch.setShader(null);
		this.spriteBatch.draw(this.texture, 40f, 0f, 40, 40);
		this.spriteBatch.end();
	}

}
