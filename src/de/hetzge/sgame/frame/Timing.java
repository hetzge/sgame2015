package de.hetzge.sgame.frame;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.pmw.tinylog.Logger;

import com.badlogic.gdx.Gdx;

import de.hetzge.sgame.App;
import de.hetzge.sgame.misc.Timer;
import de.hetzge.sgame.misc.Util;

public class Timing {

	private final static short FRAMES_PER_SECOND = 30;
	private final static short DEFAULT_NEXT_FRAMES = FRAMES_PER_SECOND;
	private final static float FRAME_DELTA = 1f / (float)FRAMES_PER_SECOND;

	private float stateTime = 0f;
	private float delta = 0f;
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
		App.updater.update();
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

	public void update() {
		Util.sleep(10);
		frameTimer.call();
		delta = Gdx.graphics.getDeltaTime();
		stateTime += delta; 
	}

	public int getNextFrameId(int frames) {
		return current.getId() + frames;
	}

	public int getDefaultNextFrameId() {
		return current.getId() + DEFAULT_NEXT_FRAMES;
	}

	public float getDelta() {
		return delta;
	}

	public float getFrameDelta(){
		return FRAME_DELTA;
	}
	
	public float getStateTime() {
		return stateTime;
	}
}
