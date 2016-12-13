package de.thorbenkuck.netcom.network.server;

import de.thorbenkuck.netcom.DisconnectedHandler;
import de.thorbenkuck.netcom.central.CommandRegistration;
import de.thorbenkuck.netcom.datatypes.AcknowledgeTimeout;
import de.thorbenkuck.netcom.datatypes.abstracts.StoppableRunnable;
import de.thorbenkuck.netcom.datatypes.interfaces.exceptions.ExceptionHandler;
import de.thorbenkuck.netcom.datatypes.interfaces.Handler;
import de.thorbenkuck.netcom.central.NetComCommand;
import de.thorbenkuck.netcom.network.serial.DeserializerAdapter;
import de.thorbenkuck.netcom.network.serial.SerializerAdapter;
import de.thorbenkuck.netcom.network.shared.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectingService<UserType extends User> extends StoppableRunnable {

	private Logger logger = LogManager.getLogger();

	private ServerSocket serverSocket;
	private ClientList<ServerClient<UserType>> clientList;
	private final SerializerAdapter<NetComCommand> serializerAdapter;
	private final DeserializerAdapter<NetComCommand> deserializerAdapter;
	private final CommandRegistration commandRegistration;
	private DisconnectedHandler defaultServerDisconnectedHandler;
	private ExceptionHandler<IOException> doOnIOException = e -> LogManager.getLogger().warn("catches Exception!", e);
	private final AcknowledgeTimeout acknowledgeTimeout;
	private Handler<Socket> clientHandler = (socket ->
		logger.info("Client connected: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort())
	);

	public ConnectingService(ServerSocket serverSocket, ClientList<ServerClient<UserType>> clientList,
							 SerializerAdapter<NetComCommand> serializerAdapter,
							 DeserializerAdapter<NetComCommand> deserializerAdapter,
							 CommandRegistration commandRegistration,
							 AcknowledgeTimeout acknowledgeTimeout, DefaultServerDisconnectedHandler defaultServerDisconnectedHandler) {
		this.serverSocket = serverSocket;
		this.clientList = clientList;
		this.serializerAdapter = serializerAdapter;
		this.deserializerAdapter = deserializerAdapter;
		this.commandRegistration = commandRegistration;
		this.acknowledgeTimeout = acknowledgeTimeout;
		this.defaultServerDisconnectedHandler = defaultServerDisconnectedHandler;
	}

	@Override
	public void run() {
		while(isRunning()) {
			try {
				logger.info("Waiting for new Client...");
				handleClient(acceptNextClient());
			} catch (IOException e) {
				handleIOException(e);
			}
		}
	}

	private Socket acceptNextClient() throws IOException {
		return serverSocket.accept();
	}

	private void handleClient(Socket socket) throws IOException {
		clientHandler.handle(socket);
		ServerClient<UserType> client = new ServerClient<>(socket, serializerAdapter, deserializerAdapter, commandRegistration, acknowledgeTimeout, defaultServerDisconnectedHandler);
		client.setDisconnectedHandler(defaultServerDisconnectedHandler);
		clientList.add(client);
	}

	private void handleIOException(IOException e) {
		doOnIOException.handle(e);
	}

	public void setDoOnIOException(ExceptionHandler<IOException> exceptionHandler) {
		this.doOnIOException = exceptionHandler;
	}

	public ClientList<ServerClient<UserType>> getClientList() {
		return clientList;
	}

//	// ===========================
//
//	private ExceptionHandler<ConnectException> doOnConnectException = Throwable::printStackTrace;
//
//	public void setDoOnConnectException(ExceptionHandler<ConnectException> exceptionHandler) {
//		this.doOnConnectException = exceptionHandler;
//	}
//
//	private void handleConnectException(ConnectException e) {
//		doOnConnectException.handle(e);
//	}
}
