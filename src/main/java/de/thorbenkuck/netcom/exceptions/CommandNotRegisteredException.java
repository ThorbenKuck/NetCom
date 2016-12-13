package de.thorbenkuck.netcom.exceptions;

public class CommandNotRegisteredException extends RuntimeException {
	public CommandNotRegisteredException(Class clazz) {
		super("The command: " + clazz.getSimpleName() + " is not registered!");
	}
}
