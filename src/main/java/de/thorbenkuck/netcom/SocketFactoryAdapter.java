package de.thorbenkuck.netcom;

import de.thorbenkuck.netcom.datatypes.interfaces.Factory;
import de.thorbenkuck.netcom.datatypes.interfaces.ProducerAdapter;
import de.thorbenkuck.netcom.network.shared.ConnectTo;

public abstract class SocketFactoryAdapter<T extends java.io.Closeable> implements ProducerAdapter<Factory<ConnectTo, T>> {
}
