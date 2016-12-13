package de.thorbenkuck.netcom.datatypes;

import java.util.concurrent.TimeUnit;

public class AcknowledgeTimeout {

	private long timeoutStart;
	private long timeoutIncrement;
	private int triesTillAbort;
	private int tries = 0;
	private TimeUnit timeoutUnit;

	public AcknowledgeTimeout() {
		this(1, 1, TimeUnit.SECONDS, 4);
	}

	public AcknowledgeTimeout(long timeoutStart, TimeUnit timeoutUnit) {
		this(timeoutStart, 1, timeoutUnit, 4);
	}

	public AcknowledgeTimeout(long timeoutStart, long timeoutIncrement, TimeUnit timeoutUnit) {
		this(timeoutStart, timeoutIncrement, timeoutUnit, 4);
	}

	public AcknowledgeTimeout(long timeoutStart, long timeoutIncrement, TimeUnit timeoutUnit, int triesTillAbort) {
		this.timeoutStart = timeoutStart;
		this.timeoutIncrement = timeoutIncrement;
		this.timeoutUnit = timeoutUnit;
		this.triesTillAbort = triesTillAbort;
	}

	public boolean hasNextStep() {
		return tries < triesTillAbort;
	}

	public long nextTimeout() {
		if(!hasNextStep()) {
			// TODO ExceptionHandling
			return -1;
		}
		long nextTimeout = timeoutStart + (tries * timeoutIncrement);
		++tries;
		return nextTimeout;
	}

	public void reset() {
		tries = 0;
	}

	public TimeUnit getTimeoutUnit() {
		return timeoutUnit;
	}
}
