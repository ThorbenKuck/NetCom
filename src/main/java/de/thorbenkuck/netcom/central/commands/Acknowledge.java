package de.thorbenkuck.netcom.central.commands;

import de.thorbenkuck.netcom.central.NetComCommand;
import de.thorbenkuck.netcom.central.NetComCommandObject;

public final class Acknowledge extends NetComCommand {
	public Acknowledge() {
		super(new NetComCommandObject() {});
	}

	@Override
	public boolean getDefaultAcknowledge() {
		return false;
	}
}
