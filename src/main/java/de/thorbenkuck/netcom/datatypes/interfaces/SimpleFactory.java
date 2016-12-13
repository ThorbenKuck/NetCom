package de.thorbenkuck.netcom.datatypes.interfaces;

public interface SimpleFactory<P> extends Factory<Void, P> {
	P create();
}
