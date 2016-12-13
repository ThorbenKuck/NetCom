package example;

import de.thorbenkuck.netcom.central.NetComCommand;

public class TestCommand extends NetComCommand<TestCommandObject> {

	public TestCommand(TestCommandObject t) {
		super(t);
	}

	@Override
	public boolean getDefaultAcknowledge() {
		return true;
	}
}
