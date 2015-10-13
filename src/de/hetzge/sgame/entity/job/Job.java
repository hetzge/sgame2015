package de.hetzge.sgame.entity.job;

import java.io.Serializable;

import de.hetzge.sgame.App;
import de.hetzge.sgame.entity.Entity;

public abstract class Job implements Serializable {

	private Job child;
	private int pauseTillFrame = 0;

	protected abstract void work(Entity entity);

	public void destroy() {
		// override
	}

	public void doWork(Entity entity) {
		if (this.child != null) {
			this.child.doWork(entity);
		} else {
			if (App.timing.isCurrentOrPast(this.pauseTillFrame)) {
				work(entity);
			}
		}
	}

	public void pop() {
		if (this.child != null) {
			if (this.child.child == null) {
				this.child = null;
			}
		}
	}

	public void setChild(Job job) {
		this.child = job;
	}

	public void unsetChild() {
		this.child = null;
	}

	public void addChild(Job job) {
		if (this.child == null) {
			this.child = job;
		} else {
			this.child.addChild(job);
		}
	}

	public void pauseSmall() {
		pause(10);
	}

	public void pauseMedium() {
		pause(50);
	}

	public void pauseLong() {
		pause(100);
	}

	public void pause(int frames) {
		this.pauseTillFrame = App.timing.getNextFrameId(frames);
	}
}
