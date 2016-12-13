package de.thorbenkuck.netcom.exceptions;

public class SerialisationFailedException extends RuntimeException {
	public SerialisationFailedException(Throwable throwable) {
		super(throwable);
	}
}
