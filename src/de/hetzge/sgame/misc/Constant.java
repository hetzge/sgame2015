package de.hetzge.sgame.misc;

import java.util.Arrays;
import java.util.List;

import de.hetzge.sgame.entity.job.main.NoJob;

public final class Constant {

	public static final int NO_ENTITY_ID = -1;
	public static final byte GAIA_PLAYER_ID = -1;
	public static final byte DEATH_PLAYER_ID = -2;
	public static final short TILE_SIZE = 24;
	public static final short HALF_TILE_SIZE = TILE_SIZE / 2;
	public static final List<String> IMAGE_FILE_ENDINGS = Arrays.asList("png", "jpg");
	public static final int DEFAULT_MAX_ITEMS = 8;
	public static final int ITEM_WIDTH = 12;
	public static final int ITEM_HEIGHT = 12;
	public static final short DEFAULT_MINE_TIME_IN_FRAMES = 100;
	public static final int DEFAULT_PRODUCTION_TIME_IN_FRAMES = 100;
	public static final NoJob NO_JOB = new NoJob(null);

	// performance
	public static final int MAX_A_STAR_DEPTH = 100;
	public static final int DO_JOB_EVERY_XTH_FRAMES = 1;

	private Constant() {
	}

}
