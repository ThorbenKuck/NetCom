package de.thorbenkuck.netcom.central.commands;

import de.thorbenkuck.netcom.central.NetComCommand;

public class Acknowledge extends NetComCommand {
	@Override
	public boolean getDefaultAcknowledge() {
		return false;
	}
}
