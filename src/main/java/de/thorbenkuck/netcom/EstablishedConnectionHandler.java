package de.thorbenkuck.netcom;

import de.thorbenkuck.netcom.datatypes.interfaces.Handler;
import de.thorbenkuck.netcom.network.shared.Client;
import org.apache.logging.log4j.LogManager;

public class EstablishedConnectionHandler implements Handler<Client> {
	@Override
	public void handle(Client client) {
		LogManager.getLogger().info(" connected to " + client.addressToString());
	}
}
