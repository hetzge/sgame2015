package de.hetzge.sgame.network;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import org.nustaq.net.TCPObjectSocket;
import org.pmw.tinylog.Logger;

import de.hetzge.sgame.App;
import de.hetzge.sgame.setting.NetworkSettings;

class TcpClient implements IF_Network {

	private TCPObjectSocket tcpObjectSocket;

	public TcpClient connect(NetworkSettings clientSettings) {
		try {
			String host = clientSettings.getHost();
			short port = clientSettings.getPort();
			Socket socket = new Socket(host, port);
			tcpObjectSocket = new TCPObjectSocket(socket, App.fstConfiguration);
			new AcceptMessageThread(tcpObjectSocket).start();
			Logger.info("Connected as client");
		} catch (IOException e) {
			Logger.error(e, "error while connecting to server");
		}
		return this;
	}

	@Override
	public void send(Serializable object) {
		App.function.send(tcpObjectSocket, object);
	}

	@Override
	public void disconnect() {
		try {
			tcpObjectSocket.close();
		} catch (IOException e) {
			Logger.error(e, "error while disconnect");
		}
	}

}
