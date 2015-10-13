package de.hetzge.sgame.function;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;

import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.graphic.GraphicKey;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;

public class RessourceFunction {

	public List<TextureRegion> loadTiles() {
		List<TextureRegion> tiles = new ArrayList<>();

		FileHandle tilesFolder = Gdx.files.internal("asset/tiles");
		FileHandle[] tileFiles = tilesFolder.list();
		for (FileHandle fileHandle : tileFiles) {
			if (Constant.IMAGE_FILE_ENDINGS.contains(fileHandle.extension())) {
				Texture texture = new Texture(fileHandle);
				int width = texture.getWidth();
				int height = texture.getHeight();
				for (int x = 0; x < width; x += Constant.TILE_SIZE) {
					for (int y = 0; y < height; y += Constant.TILE_SIZE) {
						tiles.add(new TextureRegion(texture, x, y, Constant.TILE_SIZE, Constant.TILE_SIZE));
					}
				}
			}
		}

		return tiles;
	}

	public TextureRegion[] loadItems() {
		Texture texture = new Texture(Gdx.files.internal("asset/icons/icons.png"));
		TextureRegion[] items = new TextureRegion[E_Item.values.length];

		items[E_Item.BREAD.ordinal()] = new TextureRegion(texture, 1 * Constant.ITEM_WIDTH, 1 * Constant.ITEM_HEIGHT, Constant.ITEM_WIDTH, Constant.ITEM_HEIGHT);
		items[E_Item.FISCH.ordinal()] = new TextureRegion(texture, 2 * Constant.ITEM_WIDTH, 2 * Constant.ITEM_HEIGHT, Constant.ITEM_WIDTH, Constant.ITEM_HEIGHT);
		items[E_Item.STONE.ordinal()] = new TextureRegion(texture, 3 * Constant.ITEM_WIDTH, 3 * Constant.ITEM_HEIGHT, Constant.ITEM_WIDTH, Constant.ITEM_HEIGHT);
		items[E_Item.WOOD.ordinal()] = new TextureRegion(texture, 4 * Constant.ITEM_WIDTH, 4 * Constant.ITEM_HEIGHT, Constant.ITEM_WIDTH, Constant.ITEM_HEIGHT);

		return items;
	}

	public IntMap<Animation> loadGraphics() {
		IntMap<Animation> result = new IntMap<>();

		Texture test1Texture = new Texture(Gdx.files.internal("asset/sprites/test1.png"));
		TextureRegion test1TextureRegion = new TextureRegion(test1Texture);

		Animation test1Animation = new Animation(1, test1TextureRegion);

		Texture testTexture = new Texture(Gdx.files.internal("asset/sprites/test.png"));
		TextureRegion[][] textureRegions = TextureRegion.split(testTexture, 16, 16);

		Animation idleWestAnimation = new Animation(1, new TextureRegion[] { textureRegions[1][0] });
		Animation idleSouthAnimation = new Animation(1, new TextureRegion[] { textureRegions[1][1] });
		Animation idleNorthAnimation = new Animation(1, new TextureRegion[] { textureRegions[1][2] });
		TextureRegion idleWestTextureRegion = new TextureRegion(textureRegions[1][0]);
		idleWestTextureRegion.flip(true, false);
		Animation idleEastAnimation = new Animation(1, new TextureRegion[] { idleWestTextureRegion });

		result.put(new GraphicKey(E_Orientation.SOUTH, E_Activity.IDLE, E_EntityType.PROVIDER).hashGraphicKey(), test1Animation);
		result.put(new GraphicKey(E_Orientation.SOUTH, E_Activity.IDLE, E_EntityType.WORKSTATION).hashGraphicKey(), test1Animation);

		// MINER

		E_EntityType entityType = E_EntityType.MINER;

		result.put(new GraphicKey(E_Orientation.EAST, E_Activity.IDLE, entityType).hashGraphicKey(), idleEastAnimation);
		result.put(new GraphicKey(E_Orientation.NORTH, E_Activity.IDLE, entityType).hashGraphicKey(), idleNorthAnimation);
		result.put(new GraphicKey(E_Orientation.WEST, E_Activity.IDLE, entityType).hashGraphicKey(), idleWestAnimation);
		result.put(new GraphicKey(E_Orientation.SOUTH, E_Activity.IDLE, entityType).hashGraphicKey(), idleSouthAnimation);

		loadLine(result, textureRegions, 2, E_Activity.WALKING, entityType);
		loadLine(result, textureRegions, 3, E_Activity.CARRY, entityType);
		loadLine(result, textureRegions, 7, E_Activity.WORKING, entityType);
		loadLine(result, textureRegions, 13, E_Activity.DESTROY, entityType);

		// CARRIER

		entityType = E_EntityType.CARRIER;

		result.put(new GraphicKey(E_Orientation.EAST, E_Activity.IDLE, entityType).hashGraphicKey(), idleEastAnimation);
		result.put(new GraphicKey(E_Orientation.NORTH, E_Activity.IDLE, entityType).hashGraphicKey(), idleNorthAnimation);
		result.put(new GraphicKey(E_Orientation.WEST, E_Activity.IDLE, entityType).hashGraphicKey(), idleWestAnimation);
		result.put(new GraphicKey(E_Orientation.SOUTH, E_Activity.IDLE, entityType).hashGraphicKey(), idleSouthAnimation);

		loadLine(result, textureRegions, 2, E_Activity.WALKING, entityType);
		loadLine(result, textureRegions, 3, E_Activity.CARRY, entityType);
		loadLine(result, textureRegions, 7, E_Activity.WORKING, entityType);
		loadLine(result, textureRegions, 13, E_Activity.DESTROY, entityType);

		return result;
	}

	private void loadLine(IntMap<Animation> result, TextureRegion[][] textureRegions, int line, E_Activity activity, E_EntityType entityType) {
		final float FRAME_TIME = 0.5f;

		Animation walkEastAnimation = new Animation(FRAME_TIME, new TextureRegion[] { textureRegions[line][0], textureRegions[line][1] });
		Animation walkNorthAnimation = new Animation(FRAME_TIME, new TextureRegion[] { textureRegions[line][2], textureRegions[line][3] });
		Animation walkWestAnimation = new Animation(FRAME_TIME, new TextureRegion[] { textureRegions[line][4], textureRegions[line][5] });
		Animation walkSouthAnimation = new Animation(FRAME_TIME, new TextureRegion[] { textureRegions[line][6], textureRegions[line][7] });

		result.put(new GraphicKey(E_Orientation.EAST, activity, entityType).hashGraphicKey(), walkEastAnimation);
		result.put(new GraphicKey(E_Orientation.NORTH, activity, entityType).hashGraphicKey(), walkNorthAnimation);
		result.put(new GraphicKey(E_Orientation.WEST, activity, entityType).hashGraphicKey(), walkWestAnimation);
		result.put(new GraphicKey(E_Orientation.SOUTH, activity, entityType).hashGraphicKey(), walkSouthAnimation);
	}

}
