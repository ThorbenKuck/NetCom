package de.thorbenkuck.netcom.network.shared;

public class ConnectTo {

	private String host;
	private int port;

	public ConnectTo(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
}
