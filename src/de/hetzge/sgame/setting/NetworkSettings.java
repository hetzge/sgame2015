package de.hetzge.sgame.setting;

import java.io.Serializable;

import de.hetzge.sgame.network.E_NetworkRole;

public class NetworkSettings implements Serializable {

	private String host = "127.0.0.1";
	private short port = 15234;
	private E_NetworkRole networkRole = E_NetworkRole.CLIENT;

	public String getHost() {
		return host;
	}

	public short getPort() {
		return port;
	}

	public E_NetworkRole getNetworkRole() {
		return networkRole;
	}
	
	public boolean isHost(){
		return networkRole == E_NetworkRole.HOST;
	}
	
	public boolean isClient(){
		return networkRole == E_NetworkRole.CLIENT;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(short port) {
		this.port = port;
	}

	public void setNetworkRole(E_NetworkRole networkRole) {
		this.networkRole = networkRole;
	}

}
