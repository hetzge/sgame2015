package de.hetzge.sgame.game.input;

public interface IF_InputMode {

	void onMouseDown(int button, MouseEventPosition downPosition);

	void onMouseUp(int button, MouseEventPosition downPosition, MouseEventPosition upPosition);

}
