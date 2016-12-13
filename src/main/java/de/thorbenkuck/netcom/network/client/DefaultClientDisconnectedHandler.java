package de.thorbenkuck.netcom.network.client;

import de.thorbenkuck.netcom.DisconnectedHandler;
import de.thorbenkuck.netcom.network.shared.Client;

public class DefaultClientDisconnectedHandler extends DisconnectedHandler {
	@Override
	public void handle(Client client) {
		System.out.println(client.toString() + " disconnected!");
		System.exit(1);
	}
}
