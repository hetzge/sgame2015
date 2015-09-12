package de.hetzge.sgame.function;

import java.io.Serializable;

import org.nustaq.net.TCPObjectSocket;

public interface IF_Function {
	
	void initGame();

	void dispatch(Object object, TCPObjectSocket tcpObjectSocket);

	void send(TCPObjectSocket tcpObjectSocket, Serializable object);
	
	void sendHandshake();
	
}
