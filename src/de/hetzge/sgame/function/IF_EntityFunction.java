package de.hetzge.sgame.function;

import de.hetzge.sgame.entity.Entity;

public interface IF_EntityFunction {

	void goAway(Entity entity);
	
	void gotoGridPosition(Entity entity, short x, short y);
	
}
