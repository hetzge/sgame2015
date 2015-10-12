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
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.item.E_Item;

public class Ressources {

	private Skin skin;
	private List<TextureRegion> tiles;
	private IntMap<Animation> graphics;
	private TextureRegion[] items;
	private BitmapFont bitmapFont;

	private Pixmap cursor;

	public void init() {
		skin = new Skin(Gdx.files.internal("asset/skin/uiskin.json"));
		bitmapFont = skin.getFont("default-font");
		tiles = App.ressourceFunction.loadTiles();
		graphics = App.ressourceFunction.loadGraphics();
		items = App.ressourceFunction.loadItems();

		TextureData cursorTextureData = new Texture(Gdx.files.internal("asset/cursor.png")).getTextureData();
		cursorTextureData.prepare();
		cursor = cursorTextureData.consumePixmap();
	}

	public void registerGraphic(IF_GraphicKey graphicKey, Animation animation) {
		graphics.put(graphicKey.hashGraphicKey(), animation);
	}

	public Animation getGraphic(IF_GraphicKey graphicKey) {
		Animation graphic = graphics.get(graphicKey.hashGraphicKey());
		if (graphic == null) {
			throw new InvalidGameStateException("Try to access non existing graphic: " + graphicKey.hashGraphicKeyString());
		}
		return graphic;
	}

	public TextureRegion getItemTextureRegion(E_Item item) {
		return items[item.ordinal()];
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
