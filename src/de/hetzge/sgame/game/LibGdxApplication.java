package de.hetzge.sgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.hetzge.sgame.App;
import de.hetzge.sgame.network.E_NetworkRole;

public class LibGdxApplication extends com.badlogic.gdx.Game {

	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;

	public static void main(String[] args) {
		handleArgs(args);
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "Game";
		configuration.width = App.settings.getSystemSettings().getResolutionX();
		configuration.height = App.settings.getSystemSettings().getResolutionY();
		new LwjglApplication(App.libGdxApplication, configuration);
	}

	private static void handleArgs(String[] args) {
		if (args.length >= 1) {
			E_NetworkRole networkRole = E_NetworkRole.valueOf(args[0]);
			App.settings.getNetworkSettings().setNetworkRole(networkRole);
		}
	}

	public void switchGameScene(E_GameScene gameScene) {
		setScreen(gameScene.getScreen());
	}

	@Override
	public void create() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch = new SpriteBatch();
		switchGameScene(E_GameScene.LOAD);
	}

	@Override
	public void render() {
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		super.render();
		spriteBatch.end();
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

}
