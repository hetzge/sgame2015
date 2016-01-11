package de.hetzge.sgame.game.input;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Input;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.world.EntityGrid;

public class InputModeSelect implements IF_InputMode {

	@Override
	public void onMouseDown(int button, MouseEventPosition downPosition) {
	}

	@Override
	public void onMouseUp(int button, MouseEventPosition downPosition, MouseEventPosition upPosition) {
		if (button == Input.Buttons.LEFT) {
			int downX = downPosition.getGridX();
			int downY = downPosition.getGridY();
			int upX = upPosition.getGridX();
			int upY = upPosition.getGridY();

			int startX = Math.min(downX, upX);
			int startY = Math.min(downY, upY);
			int endX = Math.max(downX, upX);
			int endY = Math.max(downY, upY);

			List<Entity> selection = new LinkedList<>();

			EntityGrid entityGrid = App.getGame().getWorld().getEntityGrid();
			for (short x = (short) startX; x <= endX; x++) {
				for (short y = (short) startY; y <= endY; y++) {
					Entity entity = entityGrid.get(x, y);
					if (entity == null) {
						continue;
					} else if (!entity.getDefinition().isSelectable()) {
						continue;
					} else if (entity.getOwner() != App.getGame().getSelf().getPlayerId()) {
						continue;
					} else {
						selection.add(entity);
					}
				}
			}

			App.getGame().getLocalGameState().setSelection(selection);
		}
	}

}
