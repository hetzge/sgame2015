package de.hetzge.sgame.network;

import java.io.Serializable;

import de.hetzge.sgame.App;
import de.hetzge.sgame.setting.NetworkSettings;

public class Network implements IF_Network {

	private IF_Network network = null;
	private E_NetworkRole networkRole = null;

	public void connect(NetworkSettings networkSettings) {
		networkRole = networkSettings.getNetworkRole();
		switch (networkRole) {
		case CLIENT:
			network = new TcpClient().connect(networkSettings);
			break;
		case HOST:
			network = new TcpServer().start();
			break;
		}
	}

	@Override
	public void send(Serializable object) {
		network.send(object);
	}

	public void sendAndSelf(Serializable object) {
		send(object);
		App.function.dispatch(object, null);
	}

	public void sendOrSelf(Serializable object) {
		if (networkRole == E_NetworkRole.CLIENT) {
			send(object);
		} else {
			App.function.dispatch(object, null);
		}
	}

	@Override
	public void disconnect() {
		network.disconnect();
		network = null;
		networkRole = null;
	}

	public boolean isConnected() {
		return network != null;
	}

	public E_NetworkRole getNetworkRole() {
		return networkRole;
	}

}
