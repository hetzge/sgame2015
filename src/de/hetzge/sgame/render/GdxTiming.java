package de.hetzge.sgame.render;

import com.badlogic.gdx.Gdx;

public class GdxTiming {

	private float stateTime = 0f;
	private float delta = 0f;

	public void update() {
		this.delta = Gdx.graphics.getDeltaTime();
		this.stateTime += this.delta;
	}

	public float getStateTime() {
		return this.stateTime;
	}

	public float getDelta() {
		return this.delta;
	}

}
