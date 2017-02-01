package de.thorbenkuck.netcom.network.client;

import de.thorbenkuck.netcom.DisconnectedHandler;
import de.thorbenkuck.netcom.central.CommandRegistration;
import de.thorbenkuck.netcom.central.NetComCommand;
import de.thorbenkuck.netcom.datatypes.AcknowledgeTimeout;
import de.thorbenkuck.netcom.datatypes.interfaces.Factory;
import de.thorbenkuck.netcom.network.shared.Client;
import de.thorbenkuck.netcom.network.serial.DeserializerAdapter;
import de.thorbenkuck.netcom.network.serial.SerializerAdapter;
import de.thorbenkuck.netcom.network.shared.ConnectTo;

import java.io.IOException;
import java.net.Socket;

public class LocalClient extends Client {

	protected String host;
	protected int port;
	protected static Factory<ConnectTo, Socket> socketFactory = new DefaultClientSocketFactoryAdapter().work(null);

	public LocalClient(ConnectTo connectTo, SerializerAdapter<NetComCommand> serializerAdapter,
					   DeserializerAdapter<NetComCommand> deserializerAdapter, final CommandRegistration commandRegistration,
					   AcknowledgeTimeout acknowledgeTimeout, DisconnectedHandler disconnectedHandler) throws IOException {
		super(socketFactory.create(connectTo), serializerAdapter, deserializerAdapter, commandRegistration, acknowledgeTimeout, disconnectedHandler);
		this.host = host;
		this.port = port;
	}

	@Override
	public String toString() {
		return "Server: " + addressToString();
	}

	public void setDisconnectedHandler(DisconnectedHandler newDisconnectedHandler) {
		disconnectedHandler = newDisconnectedHandler;
	}

	public static void setSocketFactory(Factory<ConnectTo, Socket> factory) {
		socketFactory = factory;
	}

	public void sendToServer(NetComCommand command) {
		getSendQueue().add(command);
	}
}
