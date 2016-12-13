package de.thorbenkuck.netcom.network.server;

import de.thorbenkuck.netcom.DisconnectedHandler;
import de.thorbenkuck.netcom.network.shared.Client;
import org.apache.logging.log4j.LogManager;

public class DefaultServerDisconnectedHandler extends DisconnectedHandler {

	private ClientList clientList;

	public DefaultServerDisconnectedHandler(ClientList clientList) {
		this.clientList = clientList;
	}

	@Override
	public void handle(Client client) {
		clientList.remove(client);
		LogManager.getLogger().info(client.getClientToken() + " disconnected.");
	}
}
