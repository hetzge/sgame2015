package de.hetzge.sgame.world;

public class PathGridPosition extends GridPosition {

	private PathGridPosition next;

	public PathGridPosition(GridPosition gridPosition) {
		this(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public PathGridPosition(short x, short y) {
		super(x, y);
	}

	public boolean hasNext() {
		return next != null;
	}

	public PathGridPosition getNext() {
		return next;
	}

	public void setNext(PathGridPosition next) {
		this.next = next;
	}
	
	public void setNext(GridPosition next){
		setNext(new PathGridPosition(next));
	}

}
