package de.hetzge.sgame.network;

import org.nustaq.net.TCPObjectSocket;
import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;

public class AcceptMessageThread extends Thread {

	private final TCPObjectSocket tcpObjectSocket;

	public AcceptMessageThread(TCPObjectSocket tcpObjectSocket) {
		super("AcceptMessageThread");
		this.tcpObjectSocket = tcpObjectSocket;
	}

	@Override
	public void run() {
		Logger.info("start accepting message thread");

		boolean running = true;
		while (running) {
			try {
				Object object = tcpObjectSocket.readObject();
				App.function.dispatch(object, tcpObjectSocket);
			} catch (Exception e) {
				Logger.error(e, "error while read object");
			}
		}
	}

}