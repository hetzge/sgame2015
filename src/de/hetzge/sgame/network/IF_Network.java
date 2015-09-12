package de.hetzge.sgame.network;

import java.io.Serializable;

public interface IF_Network {

	void send(Serializable object);
	
	void disconnect();

}
