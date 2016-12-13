package de.thorbenkuck.netcom.exceptions;

public class CommandAlreadyRegisteredException extends RuntimeException {
	public CommandAlreadyRegisteredException(Class clazz) {
		super("The command: " + clazz.getSimpleName() + " is already registered!");
	}
}
