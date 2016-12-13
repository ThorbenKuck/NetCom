package example;

import de.thorbenkuck.netcom.network.client.ClientStart;
import de.thorbenkuck.netcom.network.shared.ConnectTo;

import java.io.IOException;

public class ClientActor extends ClientStart {
	public ClientActor() {
		super(new ConnectTo("localhost", 3141));
		commandRegistration.register(TestCommand.class, (command, client) -> {
			System.out.println(command.getMessage());
		});
	}

	public static void main(String[] args) throws IOException {
		ClientActor clientActor = new ClientActor();
		clientActor.launch();
		clientActor.getLocalClient().getSendQueue().add(new TestCommand("ping"));
	}
}
