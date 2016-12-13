package de.thorbenkuck.netcom.network.queue.sendqueue;

import de.thorbenkuck.netcom.central.NetComCommand;

import java.util.concurrent.LinkedBlockingQueue;

public class SendQueue<T extends NetComCommand> extends LinkedBlockingQueue<T> {
}
