package example;

import de.thorbenkuck.netcom.central.NetComCommandObject;

public class TestCommandObject extends NetComCommandObject {
	private String message;

	public TestCommandObject(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
