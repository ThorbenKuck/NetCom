package de.thorbenkuck.netcom.network.server;

import de.thorbenkuck.netcom.SocketFactoryAdapter;
import de.thorbenkuck.netcom.datatypes.interfaces.Factory;
import de.thorbenkuck.netcom.network.shared.ConnectTo;

import java.io.IOException;
import java.net.ServerSocket;

public class DefaultServerSocketFactoryAdapter extends SocketFactoryAdapter<ServerSocket> {

	private Factory<ConnectTo, ServerSocket> socketFactory = connectTo -> {
		try {
			return new ServerSocket(connectTo.getPort());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	};

	@Override
	public Factory<ConnectTo, ServerSocket> work(Void aVoid) {
		return socketFactory;
	}
}
