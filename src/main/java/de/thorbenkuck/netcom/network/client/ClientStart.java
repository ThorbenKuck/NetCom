package de.thorbenkuck.netcom.network.client;

import de.thorbenkuck.netcom.central.NetComCentral;
import de.thorbenkuck.netcom.network.shared.ConnectTo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ClientStart extends NetComCentral {

	private LocalClient localClient;
	private Logger logger = LogManager.getLogger();
	private DefaultClientDisconnectedHandler defaultClientDisconnectedHandler = new DefaultClientDisconnectedHandler();
	private ConnectTo connectTo;

	public ClientStart(String host, int port) {
		this(new ConnectTo(host, port));
	}

	public ClientStart(ConnectTo connectTo) {
		this.connectTo = connectTo;
	}

	@Override
	public final void launch() throws IOException {
		connect();
	}

	private void connect() {
		logger.info("connecting to server ...");
		socketFactoryAdapter = new DefaultClientSocketFactoryAdapter();
		try {
			localClient = new LocalClient(connectTo, serializerAdapter, deserializerAdapter, commandRegistration,
					acknowledgeTimeout, defaultClientDisconnectedHandler);
		} catch (IOException e) {
			// Connection Failed
			e.printStackTrace();
		}
	}

	public LocalClient getLocalClient() {
		return localClient;
	}

	@Override
	public final void shutDown() {
		try {
			localClient.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
