package de.thorbenkuck.netcom.network.client;

import de.thorbenkuck.netcom.SocketFactoryAdapter;
import de.thorbenkuck.netcom.datatypes.interfaces.Factory;
import de.thorbenkuck.netcom.network.shared.ConnectTo;

import java.io.IOException;
import java.net.Socket;

public class DefaultClientSocketFactoryAdapter extends SocketFactoryAdapter<Socket> {

	private Factory<ConnectTo, Socket> socketFactory = connectTo -> {
		try {
			return new Socket(connectTo.getHost(), connectTo.getPort());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	};

	@Override
	public Factory<ConnectTo, Socket> work(Void v) {
		return socketFactory;
	}
}
