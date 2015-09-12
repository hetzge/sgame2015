package de.hetzge.sgame.misc;

public class Timer {

	private boolean started = false;
	private final short fps;
	private final short everyMilliseconds;
	private long timestamp = 0;
	private final Runnable execute;

	public Timer(short fps, Runnable execute) {
		this.fps = fps;
		this.execute = execute;
		this.everyMilliseconds = (short) (1000 / fps);
	}

	public void call() {
		if (started) {
			long currentTimeMillis = System.currentTimeMillis();
			if (currentTimeMillis > timestamp + everyMilliseconds) {
				timestamp = timestamp == 0 ? currentTimeMillis : timestamp + everyMilliseconds;
				execute.run();
			}
		}
	}

	public short getFps() {
		return fps;
	}

	public short getEveryMilliseconds() {
		return everyMilliseconds;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public boolean isStarted() {
		return started;
	}

	public void start() {
		started = true;
		timestamp = 0;
	}

	public void stop() {
		started = false;
	}

	public float delta() {

		System.out.println((float) (System.currentTimeMillis() - timestamp) + " / " + everyMilliseconds);

		return (float) (System.currentTimeMillis() - timestamp) / everyMilliseconds;
	}

}
