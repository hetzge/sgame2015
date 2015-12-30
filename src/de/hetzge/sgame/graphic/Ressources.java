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
import de.hetzge.sgame.booking.IF_Item;
import de.hetzge.sgame.error.InvalidGameStateException;

public class Ressources {

	private Skin skin;
	private List<TextureRegion> tiles;
	private IntMap<Animation> graphics;
	private TextureRegion[] items;
	private BitmapFont bitmapFont;

	private Pixmap cursor;

	public void init() {
		this.skin = new Skin(Gdx.files.internal("asset/skin/uiskin.json"));
		this.bitmapFont = this.skin.getFont("default-font");
		this.tiles = App.ressourceFunction.loadTiles();
		this.graphics = App.ressourceFunction.loadGraphics();
		this.items = App.ressourceFunction.loadItems();

		TextureData cursorTextureData = new Texture(Gdx.files.internal("asset/cursor.png")).getTextureData();
		cursorTextureData.prepare();
		this.cursor = cursorTextureData.consumePixmap();
	}

	public void registerGraphic(IF_GraphicKey graphicKey, Animation animation) {
		this.graphics.put(graphicKey.hashGraphicKey(), animation);
	}

	public Animation getGraphic(IF_GraphicKey graphicKey) {
		Animation graphic = this.graphics.get(graphicKey.hashGraphicKey());
		if (graphic == null) {
			throw new InvalidGameStateException("Try to access non existing graphic: " + graphicKey.hashGraphicKeyString());
		}
		return graphic;
	}

	public TextureRegion getItemTextureRegion(IF_Item item) {
		return this.items[item.ordinal()];
	}

	public TextureRegion getTileTextureRegion(short tileId) {
		return this.tiles.get(tileId);
	}

	public Skin getSkin() {
		return this.skin;
	}

	public Pixmap getCursor() {
		return this.cursor;
	}

	public BitmapFont getBitmapFont() {
		return this.bitmapFont;
	}

}
