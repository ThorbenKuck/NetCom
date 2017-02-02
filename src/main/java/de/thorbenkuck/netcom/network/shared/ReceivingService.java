package de.thorbenkuck.netcom.network.shared;

import de.thorbenkuck.netcom.DisconnectedHandler;
import de.thorbenkuck.netcom.central.CommandRegistration;
import de.thorbenkuck.netcom.central.NetComCommand;
import de.thorbenkuck.netcom.central.commands.Acknowledge;
import de.thorbenkuck.netcom.datatypes.abstracts.StoppableRunnable;
import de.thorbenkuck.netcom.exceptions.DeSerialisationFailedException;
import de.thorbenkuck.netcom.network.serial.DeserializerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReceivingService extends StoppableRunnable {

	private DisconnectedHandler disconnectedHandler;
	private CommandRegistration commandRegistration;
	private DeserializerAdapter<NetComCommand> deserializerAdapter;
	private Client client;
	private Scanner in;
	private final Logger logger = LogManager.getLogger();

	public ReceivingService(DisconnectedHandler disconnectedHandler,
							Client client, final CommandRegistration commandRegistration,
							DeserializerAdapter<NetComCommand> deserializerAdapter) throws IOException {
		this.disconnectedHandler = disconnectedHandler;
		this.client = client;
		this.commandRegistration = commandRegistration;
		this.deserializerAdapter = deserializerAdapter;
		in = new Scanner(client.getSocket().getInputStream());
	}

	public void setDisconnectedHandler(DisconnectedHandler disconnectedHandler) {
		this.disconnectedHandler = disconnectedHandler;
	}

	public void onDisconnect() {
		disconnectedHandler.handle(client);
	}

	@Override
	public void run() {
		while (isRunning()) {
			try {
				String string = in.nextLine();
				NetComCommand command = deserialize(string);
				logger.debug("received: " + command);
				runCommand(command);
			} catch (NoSuchElementException e) {
				// Client disconnected
				softStop();
			}
		}
		onDisconnect();
	}

	private void runCommand(NetComCommand command) {
		// If == Ack do nothing.
		if(command.getClass().equals(Acknowledge.class)) {
			client.getSendingService().getAcknowledgeHanger().acknowledge();
		} else {
			if(command.getDefaultAcknowledge()) {
				client.getSendQueue().add(new Acknowledge());
			}
			commandRegistration.executeDoOnCommand(command, client);
		}
	}

	private NetComCommand deserialize(String string) {
		try {
			return deserializerAdapter.work(string);
		} catch(Throwable throwable) {
			throw new DeSerialisationFailedException(throwable);
		}
	}

	public Scanner getScanner() {
		return in;
	}
}
