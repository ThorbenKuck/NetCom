package de.thorbenkuck.netcom.central;

import java.io.Serializable;

public abstract class NetComCommand implements Serializable {
	protected NetComCommand() {
	}
	public abstract boolean getDefaultAcknowledge();
}
