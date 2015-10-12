package de.hetzge.sgame.world;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Path {

	private final List<GridPosition> gridPositions = new LinkedList<>();

	public void add(GridPosition gridPosition) {
		gridPositions.add(gridPosition);
	}

	public ListIterator<GridPosition> listIterator(int index) {
		return gridPositions.listIterator(index);
	}

	public int pathSize() {
		return gridPositions.size();
	}

	public void removeFirst() {
		if (!gridPositions.isEmpty()) {
			gridPositions.remove(0);
		}
	}

	public void removeLast() {
		if (!gridPositions.isEmpty()) {
			gridPositions.remove(gridPositions.size() - 1);
		}
	}

	public short[] getXPath() {
		short[] xPath = new short[pathSize()];
		int i = 0;
		for (GridPosition gridPosition : gridPositions) {
			xPath[i++] = gridPosition.getGridX();
		}
		return xPath;
	}

	public short[] getYPath() {
		short[] yPath = new short[pathSize()];
		int i = 0;
		for (GridPosition gridPosition : gridPositions) {
			yPath[i++] = gridPosition.getGridY();
		}
		return yPath;
	}

	public Path reverse() {
		Path newPath = new Path();
		ListIterator<GridPosition> listIterator = listIterator(pathSize());
		while (listIterator.hasPrevious()) {
			GridPosition previous = listIterator.previous();
			newPath.add(previous);
		}
		return newPath;
	}

	@Override
	public String toString() {
		return "Path [gridPositions=" + gridPositions + "]";
	}

}
