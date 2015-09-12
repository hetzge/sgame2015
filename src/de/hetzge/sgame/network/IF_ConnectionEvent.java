package de.hetzge.sgame.network;

import java.io.Serializable;

import org.nustaq.net.TCPObjectSocket;

public interface IF_ConnectionEvent extends Serializable {

	void execute(TCPObjectSocket tcpObjectSocket);
	
}
