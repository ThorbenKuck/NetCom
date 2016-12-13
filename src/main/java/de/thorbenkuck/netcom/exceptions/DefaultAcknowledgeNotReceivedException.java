package de.thorbenkuck.netcom.exceptions;

import de.thorbenkuck.netcom.central.NetComCommand;

public class DefaultAcknowledgeNotReceivedException extends RuntimeException {
	public DefaultAcknowledgeNotReceivedException(NetComCommand netComCommand) {
		super("Did not receive the default acknowledge for the NetComCommand " + netComCommand.toString());
	}
}
