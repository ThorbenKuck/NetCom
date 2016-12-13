package de.thorbenkuck.netcom.datatypes;

import java.util.concurrent.CountDownLatch;

public class AcknowledgeHanger {

	private CountDownLatch countDownLatch;

	public AcknowledgeHanger() {
		countDownLatch = new CountDownLatch(1);
	}

	public AcknowledgeHanger(AcknowledgeHanger toCopy) {
		countDownLatch = toCopy.countDownLatch;
	}

	public void reset() {
		countDownLatch = new CountDownLatch(0);
	}

	public void acknowledge() {
		countDownLatch.countDown();
	}

	public void await() throws InterruptedException {
		countDownLatch.await();
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	public boolean isAcknowledged() {
		return countDownLatch.getCount() <= 0;
	}
}
