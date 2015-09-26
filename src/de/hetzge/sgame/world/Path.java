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

	@Override
	public String toString() {
		return "Path [gridPositions=" + gridPositions + "]";
	}

}
