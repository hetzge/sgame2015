package de.hetzge.sgame.render;

import java.util.List;

import com.badlogic.gdx.graphics.FPSLogger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.world.World;

public class Renderer {

	private FPSLogger fpsLogger = new FPSLogger();

	public void render() {
		World world = App.game.getWorld();

		App.worldRenderer.render(world);
		List<Entity> visibleEntities = App.worldRenderer.getVisibleEntities();
		for (Entity entity : visibleEntities) {
			App.entityRenderer.render(entity);
		}

		fpsLogger.log();
	}

}
