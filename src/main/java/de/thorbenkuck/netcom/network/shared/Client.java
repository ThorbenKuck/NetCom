package de.thorbenkuck.netcom.network.shared;

import de.thorbenkuck.netcom.DisconnectedHandler;
import de.thorbenkuck.netcom.EstablishedConnectionHandler;
import de.thorbenkuck.netcom.central.CommandRegistration;
import de.thorbenkuck.netcom.central.NetComCommand;
import de.thorbenkuck.netcom.datatypes.AcknowledgeTimeout;
import de.thorbenkuck.netcom.datatypes.ClientToken;
import de.thorbenkuck.netcom.network.queue.sendqueue.SendQueue;
import de.thorbenkuck.netcom.network.serial.DeserializerAdapter;
import de.thorbenkuck.netcom.network.serial.SerializerAdapter;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client extends Observable {

	protected boolean connected;
	protected final SendQueue<NetComCommand> sendQueue;
	protected static final ExecutorService clientsThreadPool = Executors.newCachedThreadPool();
	protected EstablishedConnectionHandler establishedConnectionHandler = new EstablishedConnectionHandler();
	protected DisconnectedHandler disconnectedHandler;

	private final Socket socket;
	private ReceivingService receivingService;
	private SendingService sendingService;
	private final SerializerAdapter<NetComCommand> serializerAdapter;
	private final DeserializerAdapter<NetComCommand> deserializerAdapter;
	private final CommandRegistration commandRegistration;
	private final ClientToken clientToken;
	private final AcknowledgeTimeout acknowledgeTimeout;

	public Client(final Socket socket, SerializerAdapter<NetComCommand> serializerAdapter, DeserializerAdapter<NetComCommand> deserializerAdapter,
				  CommandRegistration commandRegistration, AcknowledgeTimeout acknowledgeTimeout, DisconnectedHandler disconnectedHandler) throws IOException {
		this.socket = socket;
		this.clientToken = new ClientToken();
		this.serializerAdapter = serializerAdapter;
		this.deserializerAdapter = deserializerAdapter;
		this.commandRegistration = commandRegistration;
		this.disconnectedHandler = disconnectedHandler;
		this.acknowledgeTimeout = acknowledgeTimeout;
		sendQueue = new SendQueue<>();
		invoke();
	}

	private void invoke() throws IOException {
		startReceivingService();
		startSendingService();
		establishedConnectionHandler.handle(this);
		connected = true;
	}

	private void startSendingService() throws IOException {
		sendingService = new SendingService(new PrintWriter(socket.getOutputStream()), sendQueue, serializerAdapter, acknowledgeTimeout);
		clientsThreadPool.execute(sendingService);
	}

	private void startReceivingService() throws IOException {
		receivingService = new ReceivingService(disconnectedHandler, this, commandRegistration, deserializerAdapter);
		clientsThreadPool.execute(receivingService);
	}

	public void setDisconnectedHandler(DisconnectedHandler disconnectedHandler) {
		if(receivingService != null) {
			LogManager.getLogger().debug("Overriding existing disconnectedHandler with " + disconnectedHandler);
			receivingService.setDisconnectedHandler(disconnectedHandler);
		} else {
			this.disconnectedHandler = disconnectedHandler;
		}
	}

	protected void tryStop() {
		receivingService.softStop();
		sendingService.softStop();
		connected = false;
	}

	public void disconnect() throws IOException {
		tryStop();
		clientsThreadPool.shutdownNow();
		socket.close();
	}

	Socket getSocket() {
		return socket;
	}

	public SendQueue<NetComCommand> getSendQueue() {
		return sendQueue;
	}

	public void setEstablishedConnectionHandler(EstablishedConnectionHandler handler) {
		this.establishedConnectionHandler = handler;
	}

	ReceivingService getReceivingService() {
		return receivingService;
	}

	SendingService getSendingService() {
		return sendingService;
	}

	@Override
	public String toString() {
		return "Client " + addressToString();
	}

	public String addressToString() {
		return socket.getInetAddress() + ":" + socket.getPort();
	}

	public ClientToken getClientToken() {
		return clientToken;
	}

	@Override
	public final boolean equals(Object o) {
		if(!(o instanceof Client)) {
			return false;
		}
		if(o == this) {
			return true;
		}
		return clientToken.equals(((Client) o).getClientToken()) && socket.equals(((Client) o).getSocket()) ;
	}
}
