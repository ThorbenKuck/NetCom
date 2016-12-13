package de.thorbenkuck.netcom.network.serial.concrete;

import de.thorbenkuck.netcom.central.NetComCommand;
import de.thorbenkuck.netcom.network.serial.SerializerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class DefaultJavaSerializerAdapter implements SerializerAdapter<NetComCommand> {
	@Override
	public String work(NetComCommand command) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(command);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}
}
