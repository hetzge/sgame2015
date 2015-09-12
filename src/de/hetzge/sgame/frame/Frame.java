package de.hetzge.sgame.frame;

import java.util.LinkedList;

public class Frame {

	private final int id;
	private final LinkedList<IF_FrameEvent> frameEvents = new LinkedList<>();
	private Frame next;

	public Frame(int id) {
		this.id = id;
	}

	public Frame next() {
		if (next == null) {
			next = new Frame(id + 1);
		}
		return next;
	}

	public void addFrameEvent(IF_FrameEvent frameEvent) {
		int frameId = frameEvent.getFrameId();
		if (frameId < id) {
			if (frameId == 0) {
				next().addFrameEvent(frameEvent);
			} else {
				throw new IllegalStateException("frame event reached to late");
			}
		}
		if (frameId == id) {
			frameEvents.add(frameEvent);
		} else {
			next().addFrameEvent(frameEvent);
		}
	}

	public void execute() {
		for (IF_FrameEvent frameEvent : frameEvents) {
			frameEvent.execute();
		}
	}
	
	public int getId() {
		return id;
	}

}
