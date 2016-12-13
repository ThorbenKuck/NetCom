package de.thorbenkuck.netcom.datatypes.interfaces;

public interface Factory<E, P> {
	P create(E e);
}
