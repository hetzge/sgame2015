package de.hetzge.sgame.function;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hetzge.sgame.misc.Constant;

public class RessourceFunction {

	public List<TextureRegion> loadTiles() {
		List<TextureRegion> result = new ArrayList<>();

		FileHandle tilesFolder = Gdx.files.internal("asset/tiles");
		FileHandle[] tileFiles = tilesFolder.list();
		for (FileHandle fileHandle : tileFiles) {
			if (Constant.IMAGE_FILE_ENDINGS.contains(fileHandle.extension())) {
				Texture texture = new Texture(fileHandle);
				int width = texture.getWidth();
				int height = texture.getHeight();
				for (int x = 0; x < width / Constant.TILE_SIZE; x++) {
					for (int y = 0; y < height / Constant.TILE_SIZE; y++) {
						result.add(new TextureRegion(texture, x * Constant.TILE_SIZE, y * Constant.TILE_SIZE, Constant.TILE_SIZE, Constant.TILE_SIZE));
					}
				}
			}
		}

		return result;
	}

}
