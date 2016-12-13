package de.thorbenkuck.netcom.exceptions;

public class DeSerialisationFailedException extends RuntimeException {
	public DeSerialisationFailedException(Throwable throwable) {
		super(throwable);
	}
}
