package de.thorbenkuck.netcom.network.shared;

import java.util.Observable;

public class User extends Observable {

	private boolean online = false;

	public User() {
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline() {
		online = true;
	}

	public void setOffline() {
		online = false;
	}
}
