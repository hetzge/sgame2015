package de.hetzge.sgame.entity.job;

import de.hetzge.sgame.entity.Entity;

public abstract class Job {

	private Job child;

	protected abstract void work(Entity entity);

	public void doWork(Entity entity) {
		if (child != null) {
			child.doWork(entity);
		} else {
			work(entity);
		}
	}

	public void pop() {
		if (child != null) {
			if (child.child == null) {
				child = null;
			}
		}
	}

	public void setChild(Job job) {
		child = job;
	}

	public void unsetChild() {
		child = null;
	}

	public void addChild(Job job) {
		if (child == null) {
			child = job;
		} else {
			child.addChild(job);
		}
	}

}
