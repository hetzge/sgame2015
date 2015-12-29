package de.hetzge.sgame.game.input;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.Input;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.game.LocalGameState;
import de.hetzge.sgame.game.event.request.EventRequestGoto;
import de.hetzge.sgame.network.NetworkModule;

public class InputModeGoto implements IF_InputMode {

	@Override
	public void onMouseDown(int button, MouseEventPosition downPosition) {
	}

	@Override
	public void onMouseUp(int button, MouseEventPosition downPosition, MouseEventPosition upPosition) {
		if (button == Input.Buttons.RIGHT) {
			LocalGameState localGameState = App.game.getLocalGameState();
			boolean hasSelection = localGameState.hasSelection();
			if (hasSelection) {
				Set<Entity> selectionEntities = localGameState.getSelection();
				List<Integer> selectionEntityIds = selectionEntities.stream().map(Entity::getId).collect(Collectors.toList());
				short gridX = upPosition.getGridX();
				short gridY = upPosition.getGridY();
				EventRequestGoto eventRequestGoto = new EventRequestGoto(selectionEntityIds, gridX, gridY);
				NetworkModule.instance.sendOrSelf(eventRequestGoto);
			}
		}
	}

}
