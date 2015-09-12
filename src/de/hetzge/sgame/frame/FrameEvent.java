package de.hetzge.sgame.frame;

public abstract class FrameEvent implements IF_FrameEvent {

	private final int frameId;

	public FrameEvent(int frameId) {
		this.frameId = frameId;
	}

	@Override
	public int getFrameId() {
		return frameId;
	}

}
