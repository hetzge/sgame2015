package de.hetzge.sgame.game.input;

public class InputModeCommandUnits implements IF_InputMode {

	private final InputModeGoto inputModeGoto = new InputModeGoto();
	private final InputModeSelect inputModeSelect = new InputModeSelect();

	@Override
	public void onMouseDown(int button, MouseEventPosition downPosition) {
		this.inputModeGoto.onMouseDown(button, downPosition);
		this.inputModeSelect.onMouseDown(button, downPosition);
	}

	@Override
	public void onMouseUp(int button, MouseEventPosition downPosition, MouseEventPosition upPosition) {
		this.inputModeGoto.onMouseUp(button, downPosition, upPosition);
		this.inputModeSelect.onMouseUp(button, downPosition, upPosition);
	}

}
