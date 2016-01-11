package de.hetzge.sgame.game.input;

import com.badlogic.gdx.math.Vector2;

import de.hetzge.sgame.App;
import de.hetzge.sgame.world.World;

public class MouseEventPosition {

	private final float screenX;
	private final float screenY;
	private final float x;
	private final float y;
	private short gridX;
	private short gridY;

	public MouseEventPosition(int x, int y) {
		Vector2 worldPosition = App.libGdxApplication.unproject(x, y);
		this.screenX = x;
		this.screenY = y;
		this.x = worldPosition.x;
		this.y = worldPosition.y;
		World world = App.getGame().getWorld();
		this.gridX = world.toGridX(this.x);
		this.gridY = world.toGridY(this.y);
	}

	public MouseEventPosition(float screenX, float screenY, float x, float y, short gridX, short gridY) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.x = x;
		this.y = y;
		this.gridX = gridX;
		this.gridY = gridY;
	}

	public short getGridX() {
		return this.gridX;
	}

	public void setGridX(short gridX) {
		this.gridX = gridX;
	}

	public short getGridY() {
		return this.gridY;
	}

	public void setGridY(short gridY) {
		this.gridY = gridY;
	}

	public float getScreenX() {
		return this.screenX;
	}

	public float getScreenY() {
		return this.screenY;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

}
