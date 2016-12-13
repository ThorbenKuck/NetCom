package de.thorbenkuck.netcom.network.serial.concrete;

import de.thorbenkuck.netcom.central.NetComCommand;
import de.thorbenkuck.netcom.exceptions.DeSerialisationFailedException;
import de.thorbenkuck.netcom.network.serial.DeserializerAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

public class DefaultJavaDeserializerAdapter implements DeserializerAdapter<NetComCommand> {
	@Override
	public NetComCommand work(String string) {
		byte[] data = Base64.getDecoder().decode(string);
		ObjectInputStream ois = null;
		Object o = null;
		try {
			ois = new ObjectInputStream(
					new ByteArrayInputStream(data));
			o = ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(o == null) {
			throw new DeSerialisationFailedException(new Throwable("Error while reading the given serialized NetComCommand .. " +
					"\nGiven serialized NetComCommand: " + string));
		}
		return (NetComCommand) o;
	}
}
