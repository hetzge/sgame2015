package de.hetzge.sgame.misc;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

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
	public static final int DEFAULT_PRODUCTION_TIME_IN_FRAMES = 500;
	public static final NoJob NO_JOB = new NoJob(null);

	public static final Color[] COLORS = new Color[Byte.MAX_VALUE];

	static {
		for (int i = 0; i < Byte.MAX_VALUE; i++) {
			float c1 = (1f / 3f * 1 + i / (float) Byte.MAX_VALUE) % 1;
			float c2 = (1f / 3f * 2 + i / (float) Byte.MAX_VALUE) % 1;
			float c3 = (1f / 3f * 3 + i / (float) Byte.MAX_VALUE) % 1;
			float[] values = new float[] { c1, c2, c3 };
			float r = values[(i + 1) % 3];
			float g = values[(i + 2) % 3];
			float b = values[(i + 3) % 3];
			COLORS[i] = new Color(r, g, b, 1.0f);
		}
	}

	public static Color parseColor(String hex) {
		String s1 = hex.substring(0, 2);
		int v1 = Integer.parseInt(s1, 16);
		float f1 = v1 / 255f;
		String s2 = hex.substring(2, 4);
		int v2 = Integer.parseInt(s2, 16);
		float f2 = v2 / 255f;
		String s3 = hex.substring(4, 6);
		int v3 = Integer.parseInt(s3, 16);
		float f3 = v3 / 255f;
		return new Color(f1, f2, f3, 1);
	}

	public static final Color DEFAULT_REPLACEMENT_COLOR = new Color(1.0f, 0f, 1.0f, 1.0f);

	// performance
	public static final int MAX_A_STAR_DEPTH = 100;
	public static final int DO_JOB_EVERY_XTH_FRAMES = 10;

	private Constant() {
	}

}
