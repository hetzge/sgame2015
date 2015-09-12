package de.hetzge.sgame.frame;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.pmw.tinylog.Logger;

import com.badlogic.gdx.Gdx;

import de.hetzge.sgame.misc.Timer;

public class Timing {

	private final static short FRAMES_PER_SECOND = 5;
	private final static short DEFAULT_NEXT_FRAMES = FRAMES_PER_SECOND;

	private float stateTime = 0f;
	private final Timer frameTimer;

	private Frame current = new Frame(0);
	private Queue<IF_FrameEvent> buffer = new ConcurrentLinkedQueue<>();

	public Timing() {
		this.frameTimer = new Timer(FRAMES_PER_SECOND, this::onFrame);
	}

	public void addFrameEvent(IF_FrameEvent frameEvent) {
		buffer.add(frameEvent);
	}

	private void onFrame() {
		flushBuffer();
		long before = System.currentTimeMillis();
		current.execute();
		long executionTime = System.currentTimeMillis() - before;
		if (executionTime > frameTimer.getEveryMilliseconds()) {
			Logger.warn("frame executiontime is longer then frametime: " + executionTime);
		}
		current = current.next();
	}

	private void flushBuffer() {
		int size = buffer.size();
		for (int i = 0; i < size; i++) {
			IF_FrameEvent frameEvent = buffer.poll();
			current.addFrameEvent(frameEvent);
		}
	}

	public void startFrameTimer() {
		frameTimer.start();
	}

	public void stopFrameTimer() {
		frameTimer.stop();
	}

	public void gameLoop() {
		frameTimer.call();

		stateTime += Gdx.graphics.getDeltaTime();
	}

	public float getStateTime() {
		return stateTime;
	}

	public int getNextFrameId(int frames) {
		return current.getId() + frames;
	}

	public int getDefaultNextFrameId() {
		return current.getId() + DEFAULT_NEXT_FRAMES;
	}

}
