package de.hetzge.sgame.frame;

import java.io.Serializable;

import de.hetzge.sgame.network.IF_Event;

public interface IF_FrameEvent extends IF_Event, Serializable {

	int getFrameId();

	void execute();

}
