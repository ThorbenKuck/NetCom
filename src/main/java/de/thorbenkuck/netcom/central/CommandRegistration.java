package de.thorbenkuck.netcom.central;

import de.thorbenkuck.netcom.datatypes.interfaces.DoOnCommand;
import de.thorbenkuck.netcom.exceptions.CommandAlreadyRegisteredException;
import de.thorbenkuck.netcom.exceptions.CommandNotRegisteredException;
import de.thorbenkuck.netcom.network.shared.Client;

import java.util.HashMap;

public class CommandRegistration {

	private HashMap<Class<? extends NetComCommand>, DoOnCommand<? extends NetComCommand>> registration = new HashMap<>();

	public <T extends NetComCommand> void forceRegister(Class<T> command, DoOnCommand<T> doOnCommand) {
		registration.put(command, doOnCommand);
	}

	public <T extends NetComCommand> void register(Class<T> command, DoOnCommand<T> doOnCommand) {
		if(isCommandRegistered(command)) {
			throw new CommandAlreadyRegisteredException(command);
		}
		registration.put(command, doOnCommand);
	}

	private  <T extends NetComCommand> DoOnCommand<? extends NetComCommand> accessCertainCommand(Class<T> command) {
		if(!isCommandRegistered(command)) {
			throw new CommandNotRegisteredException(command);
		}
		return registration.get(command);
	}

	public <T extends NetComCommand> boolean isCommandRegistered(Class<T> command) {
		return registration.containsKey(command);
	}

	public <T extends NetComCommand> void executeDoOnCommand(T command, Client client) {
		DoOnCommand doOnCommand = accessCertainCommand(command.getClass());
		doOnCommand.run(command, client);
	}
}
