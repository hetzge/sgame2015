package de.hetzge.sgame.graphic;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.IntMap;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.misc.E_Orientation;

public class Ressources {

	private Skin skin;
	private List<TextureRegion> tiles;
	private final IntMap<Animation> graphics = new IntMap<>();
	private BitmapFont bitmapFont;

	private Pixmap cursor;

	public void init() {
		skin = new Skin(Gdx.files.internal("asset/skin/uiskin.json"));
		bitmapFont = skin.getFont("default-font");
		tiles = App.ressourceFunction.loadTiles();

		TextureData cursorTextureData = new Texture(Gdx.files.internal("asset/cursor.png")).getTextureData();
		cursorTextureData.prepare();
		cursor = cursorTextureData.consumePixmap();

		Texture test1Texture = new Texture(Gdx.files.internal("asset/sprites/test1.png"));
		TextureRegion test1TextureRegion = new TextureRegion(test1Texture);

		Animation test1Animation = new Animation(1, test1TextureRegion);

		Texture testTexture = new Texture(Gdx.files.internal("asset/sprites/test.png"));
		TextureRegion[][] textureRegions = TextureRegion.split(testTexture, 16, 16);

		Animation idleWestAnimation = new Animation(1, new TextureRegion[] { textureRegions[1][0] });
		Animation ideSouthAnimation = new Animation(1, new TextureRegion[] { textureRegions[1][1] });
		Animation idleNorthAnimation = new Animation(1, new TextureRegion[] { textureRegions[1][2] });
		TextureRegion idleWestTextureRegion = new TextureRegion(textureRegions[1][0]);
		idleWestTextureRegion.flip(true, false);
		Animation idleEastAnimation = new Animation(1, new TextureRegion[] { idleWestTextureRegion });

		registerGraphic(new GraphicKey(E_Orientation.SOUTH, E_Activity.IDLE, E_EntityType.PROVIDER), test1Animation);
		registerGraphic(new GraphicKey(E_Orientation.SOUTH, E_Activity.IDLE, E_EntityType.WORKSTATION), test1Animation);

		registerGraphic(new GraphicKey(E_Orientation.EAST, E_Activity.IDLE, E_EntityType.MINER), idleEastAnimation);
		registerGraphic(new GraphicKey(E_Orientation.NORTH, E_Activity.IDLE, E_EntityType.MINER), idleNorthAnimation);
		registerGraphic(new GraphicKey(E_Orientation.WEST, E_Activity.IDLE, E_EntityType.MINER), idleWestAnimation);
		registerGraphic(new GraphicKey(E_Orientation.SOUTH, E_Activity.IDLE, E_EntityType.MINER), ideSouthAnimation);

		loadLine(textureRegions, 2, E_Activity.WALKING);
		loadLine(textureRegions, 7, E_Activity.WORKING);
	}

	private void loadLine(TextureRegion[][] textureRegions, int line, E_Activity activity) {
		final float FRAME_TIME = 0.5f;

		Animation walkEastAnimation = new Animation(FRAME_TIME, new TextureRegion[] { textureRegions[line][0], textureRegions[line][1] });
		Animation walkNorthAnimation = new Animation(FRAME_TIME, new TextureRegion[] { textureRegions[line][2], textureRegions[line][3] });
		Animation walkWestAnimation = new Animation(FRAME_TIME, new TextureRegion[] { textureRegions[line][4], textureRegions[line][5] });
		Animation walkSouthAnimation = new Animation(FRAME_TIME, new TextureRegion[] { textureRegions[line][6], textureRegions[line][7] });

		registerGraphic(new GraphicKey(E_Orientation.EAST, activity, E_EntityType.MINER), walkEastAnimation);
		registerGraphic(new GraphicKey(E_Orientation.NORTH, activity, E_EntityType.MINER), walkNorthAnimation);
		registerGraphic(new GraphicKey(E_Orientation.WEST, activity, E_EntityType.MINER), walkWestAnimation);
		registerGraphic(new GraphicKey(E_Orientation.SOUTH, activity, E_EntityType.MINER), walkSouthAnimation);
	}

	private void registerGraphic(IF_GraphicKey graphicKey, Animation animation) {
		graphics.put(graphicKey.hashGraphicKey(), animation);
	}

	public Animation getGraphic(IF_GraphicKey graphicKey) {
		Animation graphic = graphics.get(graphicKey.hashGraphicKey());
		if (graphic == null) {
			throw new InvalidGameStateException("Try to access non existing graphic: " + graphicKey.hashGraphicKeyString());
		}
		return graphic;
	}

	public TextureRegion getTileTextureRegion(short tileId) {
		return tiles.get(tileId);
	}

	public Skin getSkin() {
		return skin;
	}

	public Pixmap getCursor() {
		return cursor;
	}

	public BitmapFont getBitmapFont() {
		return bitmapFont;
	}

}
