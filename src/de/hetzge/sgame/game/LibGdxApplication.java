package de.hetzge.sgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.hetzge.sgame.App;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.network.E_NetworkRole;
import de.hetzge.sgame.network.NetworkModule;

public class LibGdxApplication extends com.badlogic.gdx.Game {

	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private Viewport viewport;

	public static void main(String[] args) {
		handleArgs(args);
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "Game";
		configuration.width = App.settings.getSystemSettings().getResolutionX();
		configuration.height = App.settings.getSystemSettings().getResolutionY();
		ShaderProgram.pedantic = false;
		
		new LwjglApplication(App.libGdxApplication, configuration);
	}

	private static void handleArgs(String[] args) {
		if (args.length >= 1) {
			E_NetworkRole networkRole = E_NetworkRole.valueOf(args[0]);
			NetworkModule.settings.setNetworkRole(networkRole);
		}
	}

	public void switchGameScene(E_GameScene gameScene) {
		setScreen(gameScene.getScreen());
	}

	@Override
	public void create() {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		this.camera = new OrthographicCamera(width, height);
		this.spriteBatch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		this.shapeRenderer.setAutoShapeType(true);
		this.viewport = new ScreenViewport(this.camera);
		switchGameScene(E_GameScene.LOAD);
	}

	@Override
	public void render() {
		this.camera.update();
		this.spriteBatch.setProjectionMatrix(this.camera.combined);
		this.shapeRenderer.setProjectionMatrix(this.camera.combined);
		this.spriteBatch.begin();
		this.shapeRenderer.begin();
		super.render();
		this.spriteBatch.end();
		this.shapeRenderer.end();
	}

	@Override
	public void resize(int width, int height) {
		this.viewport.update(width, height);
	}

	public OrthographicCamera getCamera() {
		return this.camera;
	}

	public SpriteBatch getSpriteBatch() {
		return this.spriteBatch;
	}

	public ShapeRenderer getShapeRenderer() {
		return this.shapeRenderer;
	}

	public Vector2 unproject(int x, int y) {
		Vector3 project = this.camera.unproject(new Vector3(x, y, 0));
		return new Vector2(project.x, -project.y + Constant.TILE_SIZE);
	}

}
