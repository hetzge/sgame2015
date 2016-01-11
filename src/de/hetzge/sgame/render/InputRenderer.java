package de.hetzge.sgame.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.game.input.MouseEventPosition;
import de.hetzge.sgame.misc.Constant;
import de.hetzge.sgame.misc.Util;
import de.hetzge.sgame.world.World;

public class InputRenderer implements IF_Renderer {

	private static final Color COLOR_GREEN_TRANSPARENT = new Color(0f, 1f, 0f, 0.5f);
	private static final Color COLOR_RED_TRANSPARENT = new Color(1f, 0f, 0f, 0.5f);

	public void render() {
		renderBuild();
		renderSelectionRect();
	}

	public void renderBuild() {
		E_EntityType buildEntityType = App.getGame().getLocalGameState().getBuildEntityType();
		if (buildEntityType != null) {
			World world = App.getGame().getWorld();
			EntityDefinition entityDefinition = buildEntityType.getEntityDefinition();
			short width = entityDefinition.getWidth();
			short height = entityDefinition.getHeight();
			Vector2 unproject = App.libGdxApplication.unproject(Gdx.input.getX(), Gdx.input.getY());
			short gridX = Util.worldToGrid(unproject.x);
			short gridY = Util.worldToGrid(unproject.y);
			int offsetX = Util.offset(width);
			int offsetY = Util.offset(height);

			for (short x = (short) (gridX - offsetX); x < gridX - offsetX + width; x++) {
				for (short y = (short) (gridY - offsetY); y < gridY - offsetY + height; y++) {
					boolean isOnGrid = world.isOnGrid(x, y);
					if (isOnGrid) {
						boolean canBuildHere = App.worldFunction.checkSpace(x, y);
						ShapeRenderer shapeRenderer = getShapeRenderer();
						if (canBuildHere) {
							shapeRenderer.setColor(COLOR_GREEN_TRANSPARENT);
						} else {
							shapeRenderer.setColor(COLOR_RED_TRANSPARENT);
						}
						float rectX = Util.gridToWorld(x);
						float rectY = Util.gridToWorld(y);
						shapeRenderer.set(ShapeType.Filled);
						shapeRenderer.rect(rectX, -rectY, Constant.TILE_SIZE, Constant.TILE_SIZE);
					}
				}
			}
		}
	}

	public void renderSelectionRect() {
		MouseEventPosition mouseDownEventPosition = App.getGame().getLocalGameState().getMouseDownEventPosition();
		if (mouseDownEventPosition != null) {
			Vector2 unproject = App.libGdxApplication.unproject(Gdx.input.getX(), Gdx.input.getY());
			float x1 = unproject.x;
			float y1 = unproject.y;
			float x2 = mouseDownEventPosition.getX();
			float y2 = mouseDownEventPosition.getY();
			float width = Math.abs(x1 - x2);
			float height = Math.abs(y1 - y2);
			float x = Math.min(x1, x2);
			float y = Math.min(y1, y2) - Constant.TILE_SIZE;
			ShapeRenderer shapeRenderer = getShapeRenderer();
			shapeRenderer.setColor(Color.YELLOW);
			shapeRenderer.rect(x, -y, width, -height);
		}
	}

}
