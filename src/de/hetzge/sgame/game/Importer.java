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
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Util;

public class Importer {

	public GameFormat importGameFormatFromTiled(File tiledJsonFile) throws ImportExportException {

		try {
			String jsonString = IOUtils.toString(new FileInputStream(tiledJsonFile));
			JSONObject root = new JSONObject(jsonString);
			JSONArray layers = root.getJSONArray("layers");
			JSONObject tileLayer = layers.getJSONObject(0);
			JSONArray datas = tileLayer.getJSONArray("data");
			JSONObject entityLayer = getEntityLayer(layers);
			JSONArray entities = entityLayer.getJSONArray("objects");
			JSONObject containerLayer = getContainerLayer(layers);
			JSONArray containers = containerLayer.getJSONArray("objects");

			int tileSize = root.getInt("tilewidth");

			int worldWidth = root.getInt("width");
			int worldHeight = root.getInt("height");
			int length = datas.length();

			short[] tiles = new short[length];
			boolean[] collision = new boolean[length];
			EntityFormat[] entityFormats = new EntityFormat[length];
			ContainerFormat[][] containerFormats = new ContainerFormat[length][];

			// tiles
			for (int i = 0; i < datas.length(); i++) {
				int data = datas.getInt(i);
				tiles[i] = (short) (data - 1);
			}

			// entities
			for (int i = 0; i < entities.length(); i++) {
				JSONObject entity = entities.getJSONObject(i);
				JSONObject properties = entity.getJSONObject("properties");
				String typeString = properties.getString("type");
				E_EntityType type = E_EntityType.valueOf(typeString);
				int owner = properties.getInt("owner");
				double x = entity.getDouble("x");
				double y = entity.getDouble("y");
				int ix = (int) Math.floor(x / tileSize);
				int iy = (int) Math.floor(y / tileSize);
				int index = Util.index(ix, iy, worldWidth);
				entityFormats[index] = new EntityFormat(type, (byte) owner);
			}

			// container
			for (int i = 0; i < containers.length(); i++) {
				JSONObject container = containers.getJSONObject(i);
				JSONObject properties = container.getJSONObject("properties");
				String itemString = properties.getString("item");
				E_Item item = E_Item.valueOf(itemString);
				int value = properties.getInt("value");
				double x = container.getDouble("x");
				double y = container.getDouble("y");
				int ix = (int) Math.floor(x / tileSize);
				int iy = (int) Math.floor(y / tileSize);
				int index = Util.index(ix, iy, worldWidth);

				containerFormats[index] = new ContainerFormat[] { new ContainerFormat(item, value) };
			}

			return new GameFormat(worldWidth, worldHeight, tiles, collision, entityFormats, containerFormats);
		} catch (IOException e) {
			throw new ImportExportException("Can't import game format from tiled json file.", e);
		}
	}

	private JSONObject getEntityLayer(JSONArray layers) throws ImportExportException {
		return getLayer(layers, "entity");
	}

	private JSONObject getContainerLayer(JSONArray layers) throws ImportExportException {
		return getLayer(layers, "container");
	}

	private JSONObject getLayer(JSONArray layers, String layerName) throws ImportExportException {
		for (int i = 0; i < layers.length(); i++) {
			JSONObject layer = layers.getJSONObject(i);
			String name = layer.getString("name");
			if (name.equals(layerName)) {
				return layer;
			}
		}

		throw new ImportExportException("No object layer with name '" + layerName + "' found.");
	}

}
