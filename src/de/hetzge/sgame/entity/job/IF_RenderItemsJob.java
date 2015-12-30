package de.hetzge.sgame.entity.job;

import de.hetzge.sgame.booking.Container;

public interface IF_RenderItemsJob {

	/**
	 * Needs
	 */
	Container getRenderLeftContainer();

	/**
	 * Provides
	 */
	Container getRenderRightContainer();

}
