package de.thorbenkuck.netcom.network.server;

import de.thorbenkuck.netcom.central.NetComCentral;
import de.thorbenkuck.netcom.network.shared.ConnectTo;
import de.thorbenkuck.netcom.network.shared.User;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerStart<UserType extends User> extends NetComCentral {

	protected ServerSocket serverSocket;
	protected ConnectingService<UserType> connectingService;
	protected Thread connectionThread;
	protected ClientList<ServerClient<UserType>> clientList = new ClientList<>();
	protected DefaultServerDisconnectedHandler serverDisconnectedHandler;
	protected DefaultServerSocketFactoryAdapter serverSocketFactoryAdapter = new DefaultServerSocketFactoryAdapter();
	private final ConnectTo connectTo;

	public ServerStart(int port) {
		connectTo = new ConnectTo("", port);
	}

	@Override
	public void launch() throws IOException {
		serverDisconnectedHandler = new DefaultServerDisconnectedHandler(clientList);
		startUp();
	}

	private void startUp() throws IOException {
		serverSocket = serverSocketFactoryAdapter.work(null).create(connectTo);
		connectingService = new ConnectingService<>(serverSocket, clientList, serializerAdapter, deserializerAdapter,
				commandRegistration, acknowledgeTimeout, serverDisconnectedHandler);
		connectionThread = new Thread(connectingService);
		connectionThread.start();
	}

	@Override
	public void shutDown() {
		connectingService.softStop();
		connectionThread.interrupt();
	}

	public ClientList<ServerClient<UserType>> getClientList() {
		return clientList;
	}
}
