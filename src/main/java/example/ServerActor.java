package example;

import de.thorbenkuck.netcom.network.server.ServerStart;
import de.thorbenkuck.netcom.network.shared.User;

import java.io.IOException;

public class ServerActor extends ServerStart<User> {
	public ServerActor() {
		super(3141);
	}

	public static void main(String[] args) throws IOException {
		ServerActor actor = new ServerActor();
		actor.launch();

		actor.commandRegistration.register(TestCommand.class, (command, client) -> {
			System.out.println(command.getCommandObject().getMessage());
			client.getSendQueue().add(new TestCommand(new TestCommandObject("pong")));
		});
	}
}
