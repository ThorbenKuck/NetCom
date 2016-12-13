package de.thorbenkuck.netcom.datatypes.abstracts;

public abstract class StoppableRunnable extends AwaitingRunnable {

	private boolean running;

	public StoppableRunnable() {
		this(true);
	}

	public StoppableRunnable(boolean running) {
		this.running = running;
	}

	public void start() {
		// TODO Wenn es bereits läuft = Exception
		running = true;
	}

	public boolean isRunning() {
		return running;
	}

	public void softStop() {
		running = false;
	}
}
