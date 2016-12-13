package de.thorbenkuck.netcom.datatypes.interfaces;

import de.thorbenkuck.netcom.network.shared.Client;
import de.thorbenkuck.netcom.central.NetComCommand;

public interface DoOnCommand<T extends NetComCommand> {
	void run(T command, Client client);
}
