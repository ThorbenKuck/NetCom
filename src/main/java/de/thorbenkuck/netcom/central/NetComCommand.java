package de.thorbenkuck.netcom.central;

import java.io.Serializable;

public abstract class NetComCommand<T extends NetComCommandObject> implements Serializable {

	private T t;

	public NetComCommand(T t) {
		this.t = t;
	}

	public T getCommandObject() {
		return t;
	}

	public abstract boolean getDefaultAcknowledge();
}
