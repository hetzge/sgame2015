package de.hetzge.sgame.graphic;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.IntMap;

import de.hetzge.sgame.App;
import de.hetzge.sgame.error.InvalidGameStateException;

public class Ressources {

	private Skin skin;
	private List<TextureRegion> tiles;
	private final IntMap<Animation> graphics = new IntMap<>();

	public void init() {
		skin = new Skin(Gdx.files.internal("asset/skin/uiskin.json"));
		tiles = App.ressourceFunction.loadTiles();
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

}
