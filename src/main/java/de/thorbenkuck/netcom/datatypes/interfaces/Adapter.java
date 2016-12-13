package de.thorbenkuck.netcom.datatypes.interfaces;

public interface Adapter<A, R> {
	R work(A a);
}
