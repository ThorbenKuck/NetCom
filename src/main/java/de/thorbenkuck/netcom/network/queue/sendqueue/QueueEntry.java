package de.thorbenkuck.netcom.network.queue.sendqueue;

import de.thorbenkuck.netcom.central.NetComMessage;

public interface QueueEntry<T extends NetComMessage> {
	T toSend();
}
