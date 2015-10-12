package de.hetzge.sgame.misc;

import java.util.Arrays;
import java.util.List;

public final class Constant {

	public static final int NO_ENTITY_ID = -1;
	public static final byte GAIA_PLAYER_ID = -1;
	public static final short TILE_SIZE = 24;
	public static final short HALF_TILE_SIZE = TILE_SIZE / 2;
	public static final List<String> IMAGE_FILE_ENDINGS = Arrays.asList("png", "jpg");
	public static final int DEFAULT_MAX_ITEMS = 8;
	public static final short MINE_VALUE = 100;
	public static final int ICON_WIDTH = 12;
	public static final int ICON_HEIGHT = 12;

	private Constant() {
	}

}
