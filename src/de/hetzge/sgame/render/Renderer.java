package de.hetzge.sgame.render;

import com.badlogic.gdx.graphics.FPSLogger;

import de.hetzge.sgame.App;

public class Renderer {

	private FPSLogger fpsLogger = new FPSLogger();

	public void render() {
		App.worldRenderer.render();
		App.entityRenderer.render();

		this.fpsLogger.log();
	}

}
