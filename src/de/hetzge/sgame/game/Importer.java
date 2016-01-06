package de.hetzge.sgame.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.game.format.GameFormat;
import de.hetzge.sgame.game.format.GameFormat.ContainerFormat;
import de.hetzge.sgame.game.format.GameFormat.EntityFormat;
import de.hetzge.sgame.misc.Util;

public class Importer {

	public GameFormat importGameFormatFromTiled(File tiledJsonFile) throws ImportExportException {

		try {
			String jsonString = IOUtils.toString(new FileInputStream(tiledJsonFile));
			JSONObject root = new JSONObject(jsonString);
			JSONArray layers = root.getJSONArray("layers");
			JSONObject tileLayer = layers.getJSONObject(0);
			JSONArray datas = tileLayer.getJSONArray("data");
			JSONObject objectLayer = layers.getJSONObject(1);
			JSONArray objects = objectLayer.getJSONArray("objects");

			int tileSize = root.getInt("tilewidth");

			int worldWidth = root.getInt("width");
			int worldHeight = root.getInt("height");
			int length = datas.length();

			short[] tiles = new short[length];
			boolean[] collision = new boolean[length];
			EntityFormat[] entityFormats = new EntityFormat[length];
			ContainerFormat[][] containerFormats = new ContainerFormat[length][];

			for (int i = 0; i < datas.length(); i++) {
				int data = datas.getInt(i);
				tiles[i] = (short) (data - 1);
			}

			for (int i = 0; i < objects.length(); i++) {
				JSONObject object = objects.getJSONObject(i);
				JSONObject properties = object.getJSONObject("properties");
				String typeString = properties.getString("type");
				E_EntityType type = E_EntityType.valueOf(typeString);
				int owner = properties.getInt("owner");
				int x = object.getInt("x");
				int y = object.getInt("y");
				int ix = (int) Math.floor(x / tileSize);
				int iy = (int) Math.floor(y / tileSize);
				int index = Util.index(ix, iy, worldWidth);
				entityFormats[index] = new EntityFormat(type, (byte) owner);
			}

			// TODO container

			return new GameFormat(worldWidth, worldHeight, tiles, collision, entityFormats, containerFormats);
		} catch (IOException e) {
			throw new ImportExportException("Can't import game format from tiled json file.", e);
		}
	}

}
