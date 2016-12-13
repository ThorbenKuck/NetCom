package de.thorbenkuck.netcom.central;

import de.thorbenkuck.netcom.SocketFactoryAdapter;
import de.thorbenkuck.netcom.datatypes.AcknowledgeTimeout;
import de.thorbenkuck.netcom.logging.Logging;
import de.thorbenkuck.netcom.network.serial.DeserializerAdapter;
import de.thorbenkuck.netcom.network.serial.SerializerAdapter;
import de.thorbenkuck.netcom.network.serial.concrete.DefaultJavaDeserializerAdapter;
import de.thorbenkuck.netcom.network.serial.concrete.DefaultJavaSerializerAdapter;

import java.io.*;

public abstract class NetComCentral {

	public NetComCentral() {
		Logging.initLog();
	}

	protected SerializerAdapter<NetComCommand> serializerAdapter = new DefaultJavaSerializerAdapter();
	protected DeserializerAdapter<NetComCommand> deserializerAdapter = new DefaultJavaDeserializerAdapter();
	protected final CommandRegistration commandRegistration = new CommandRegistration();
	protected AcknowledgeTimeout acknowledgeTimeout = new AcknowledgeTimeout();
	protected SocketFactoryAdapter socketFactoryAdapter;

	public CommandRegistration getCommandRegistration() {
		return commandRegistration;
	}

	public SerializerAdapter<NetComCommand> getSerializerAdapter() {
		return serializerAdapter;
	}

	public DeserializerAdapter<NetComCommand> getDeserializerAdapter() {
		return deserializerAdapter;
	}

	public AcknowledgeTimeout getAcknowledgeTimeout() {
		return acknowledgeTimeout;
	}

	public SocketFactoryAdapter getSocketFactoryAdapter() {
		return socketFactoryAdapter;
	}

	public abstract void launch() throws IOException;

	public abstract void shutDown();
}
