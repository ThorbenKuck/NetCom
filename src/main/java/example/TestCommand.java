package example;

import de.thorbenkuck.netcom.central.NetComCommand;

public class TestCommand extends NetComCommand {

	private String message;

	public TestCommand(String message) {
		this.message = message;
	}

	@Override
	public boolean getDefaultAcknowledge() {
		return true;
	}

	public String getMessage() {
		return message;
	}
}
