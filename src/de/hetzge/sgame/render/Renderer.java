package de.hetzge.sgame.render;

import java.util.List;

import com.badlogic.gdx.graphics.FPSLogger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.world.World;

public class Renderer {

	private FPSLogger fpsLogger = new FPSLogger();

	private final WorldRenderer worldRenderer = new WorldRenderer();
	private final EntityRenderer entityRenderer = new EntityRenderer();

	public void render() {
		World world = App.game.getWorld();

		worldRenderer.render(world);
		List<Entity> visibleEntities = worldRenderer.getVisibleEntities();
		for (Entity entity : visibleEntities) {
			entityRenderer.render(entity);
		}

		fpsLogger.log();
	}

}
