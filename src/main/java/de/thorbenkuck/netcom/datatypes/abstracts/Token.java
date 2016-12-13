package de.thorbenkuck.netcom.datatypes.abstracts;

import java.util.UUID;

public abstract class Token {

	private final UUID id;

	public Token() {
		id = UUID.randomUUID();
	}

	@Override
	public final String toString() {
		return id.toString();
	}

	@Override
	public final boolean equals(Object o) {
		if(!o.getClass().equals(getClass())) {
			return false;
		}
		if(o == this) {
			return true;
		}
		return id.equals(((Token) o).id);
	}

	@Override
	public int hashCode() {
		int hash = 67;
		hash = 73 * hash + id.hashCode();
		return hash;
	}
}
