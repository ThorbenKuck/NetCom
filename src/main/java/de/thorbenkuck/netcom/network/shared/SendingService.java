package de.thorbenkuck.netcom.network.shared;

import de.thorbenkuck.netcom.central.NetComCommand;
import de.thorbenkuck.netcom.datatypes.AcknowledgeHanger;
import de.thorbenkuck.netcom.datatypes.AcknowledgeTimeout;
import de.thorbenkuck.netcom.datatypes.abstracts.StoppableRunnable;
import de.thorbenkuck.netcom.exceptions.DefaultAcknowledgeNotReceivedException;
import de.thorbenkuck.netcom.exceptions.SerialisationFailedException;
import de.thorbenkuck.netcom.network.queue.sendqueue.SendQueue;
import de.thorbenkuck.netcom.network.serial.SerializerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;

public class SendingService extends StoppableRunnable {

	private final PrintWriter out;
	private SendQueue toSend;
	private SerializerAdapter<NetComCommand> serializerAdapter;
	private volatile AcknowledgeHanger acknowledge;
	private AcknowledgeTimeout acknowledgeTimeout;
	private Logger logger = LogManager.getLogger();

	public SendingService(PrintWriter printWriter, SendQueue toSend, SerializerAdapter<NetComCommand> serializerAdapter,
						  AcknowledgeTimeout acknowledgeTimeout) {
		out = printWriter;
		this.toSend = toSend;
		this.serializerAdapter = serializerAdapter;
		this.acknowledge = new AcknowledgeHanger();
		this.acknowledgeTimeout = acknowledgeTimeout;
	}

	@Override
	public void run() {
		while(isRunning()) {
			synchronized (out) {
				try {
					NetComCommand comCommand = (NetComCommand) toSend.take();
					boolean defaultAcknowledge = comCommand.getDefaultAcknowledge();
					send(comCommand);
					if (defaultAcknowledge) {
						processAck(comCommand);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void processAck(NetComCommand netComCommand) {
		try {
			startWaitForAck(acknowledgeTimeout);
			if (!acknowledge.isAcknowledged()) {
				while (!acknowledge.isAcknowledged() && acknowledgeTimeout.hasNextStep()) {
					send(netComCommand);
					waitForAck(acknowledgeTimeout);
				}
			}
			processEnd(netComCommand);
		} finally {
			acknowledgeTimeout.reset();
		}
	}

	private void processEnd(NetComCommand netComCommand) {
		if(!acknowledge.isAcknowledged()) {
			logger.warn("Did not receive the ACK for " + netComCommand.toString());
			throw new DefaultAcknowledgeNotReceivedException(netComCommand);
		}
	}

	private void send(NetComCommand comCommand) {
		out.println(deserialize(comCommand));
		out.flush();
	}

	private void startWaitForAck(AcknowledgeTimeout acknowledgeTimeout) {
		acknowledge.reset();
		waitForAck(acknowledgeTimeout);
	}

	private void waitForAck(AcknowledgeTimeout acknowledgeTimeout) {
		try {
			acknowledge.getCountDownLatch().await(acknowledgeTimeout.nextTimeout(), acknowledgeTimeout.getTimeoutUnit());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String deserialize(NetComCommand command) {
		try {
			return serializerAdapter.work(command);
		} catch(Throwable throwable) {
			throw new SerialisationFailedException(throwable);
		}
	}

	public AcknowledgeHanger getAcknowledgeHanger() {
		return new AcknowledgeHanger(acknowledge);
	}
}
