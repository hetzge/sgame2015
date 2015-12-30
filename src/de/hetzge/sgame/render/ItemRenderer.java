package de.hetzge.sgame.render;

import java.util.Set;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hetzge.sgame.App;
import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.E_Orientation;

public class ItemRenderer implements IF_Renderer {

	public void renderContainer(Container<E_Item> container, short gridX, short gridY) {
		float worldX = gridX * Constant.TILE_SIZE + Constant.HALF_TILE_SIZE;
		float worldY = gridY * Constant.TILE_SIZE - Constant.TILE_SIZE;
		renderContainer(container, worldX, worldY);
	}

	public void renderContainer(Container<E_Item> container, float worldX, float worldY) {
		int orientationsCount = E_Orientation.values.length;

		int i = 0;
		Set<E_Item> items = container.getItems();
		for (E_Item item : items) {
			int round = Math.floorDiv(i, orientationsCount) + 1;
			E_Orientation orientation = E_Orientation.values[i % orientationsCount];
			E_Orientation nextOrientation = E_Orientation.values[(i + 1) % orientationsCount];

			float xOffset = round * 6 * (orientation.getOffsetX() + nextOrientation.getOffsetX());
			float yOffset = round * 6 * (orientation.getOffsetY() + nextOrientation.getOffsetY());

			int amount = container.amountWithoutHidden(item);
			renderItems(item, amount, worldX + xOffset, worldY + yOffset);
			renderItems(item, amount, worldX, worldY);
			i++;
		}
	}

	private void renderItems(E_Item item, int amount, float worldX, float worldY) {
		SpriteBatch spriteBatch = getSpriteBatch();
		int orientationsCount = E_Orientation.values.length;
		TextureRegion itemTextureRegion = App.ressources.getItemTextureRegion(item);
		for (int i = 0; i < amount; i++) {
			int round = Math.floorDiv(i, orientationsCount) + 1;
			E_Orientation orientation = E_Orientation.values[i % orientationsCount];
			E_Orientation nextOrientation = E_Orientation.values[(i + 1) % orientationsCount];

			float xOffset = round * (orientation.getOffsetX() + nextOrientation.getOffsetX());
			float yOffset = round * (orientation.getOffsetY() + nextOrientation.getOffsetY());

			float rotation = 360f / amount * i;

			spriteBatch.draw(itemTextureRegion, worldX + xOffset, -(worldY + yOffset) - Constant.HALF_TILE_SIZE, 0, 0,
					Constant.ITEM_WIDTH, Constant.ITEM_HEIGHT, 0.75f, 0.75f, rotation);
		}
	}

}
