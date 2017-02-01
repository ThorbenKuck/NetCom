package de.thorbenkuck.netcom.network.server;

import de.thorbenkuck.netcom.DisconnectedHandler;
import de.thorbenkuck.netcom.central.CommandRegistration;
import de.thorbenkuck.netcom.central.NetComCommand;
import de.thorbenkuck.netcom.datatypes.AcknowledgeTimeout;
import de.thorbenkuck.netcom.network.shared.Client;
import de.thorbenkuck.netcom.network.serial.DeserializerAdapter;
import de.thorbenkuck.netcom.network.serial.SerializerAdapter;
import de.thorbenkuck.netcom.network.shared.User;

import java.io.IOException;
import java.net.Socket;

public class ServerClient<UserType extends User> extends Client {

	private UserType loggedInAs;

	public ServerClient(Socket socket, SerializerAdapter<NetComCommand> serializerAdapter,
						DeserializerAdapter<NetComCommand> deserializerAdapter, CommandRegistration commandRegistration,
						AcknowledgeTimeout acknowledgeTimeout, DisconnectedHandler disconnectedHandler) throws IOException {
		super(socket, serializerAdapter, deserializerAdapter, commandRegistration, acknowledgeTimeout, disconnectedHandler);
	}

	public UserType getLoggedInAs() {
		return loggedInAs;
	}

	public void setDisconnectedHandler(DisconnectedHandler newDisconnectedHandler) {
		disconnectedHandler = newDisconnectedHandler;
	}

	public void setLoggedInAs(UserType user) {
		this.loggedInAs = user;
	}
}
