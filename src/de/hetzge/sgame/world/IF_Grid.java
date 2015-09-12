package de.hetzge.sgame.world;

import java.util.Iterator;
import java.util.stream.Stream;

import de.hetzge.sgame.misc.E_Orientation;
import de.hetzge.sgame.misc.Util;

public interface IF_Grid {

	short getWidth();

	short getHeight();

	public default int index(short x, short y) {
		return Util.index(x, y, getWidth());
	}

	public default boolean isOnGrid(GridPosition gridPosition) {
		return isOnGrid(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public default boolean isOnGrid(short x, short y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}

	public default Stream<GridPosition> getAroundStream(final short x, final short y) {
		GridPosition gridPosition = new GridPosition((short) 0, (short) 0);
		return E_Orientation.orientations.stream().map((orientation) -> gridPosition.set((short) (x + orientation.getOffsetX()), (short) (y + orientation.getOffsetY()))).filter((newGridPosition) -> isOnGrid(newGridPosition));
	}

	public default Iterator<GridPosition> getSpiralIterator(short x, short y, short limit) {
		return new Iterator<GridPosition>() {

			final GridPosition gridPosition = new GridPosition(x, y);

			/**
			 * How many times the phase has repeated
			 */
			short adderI = 0;

			/**
			 * How many times the phase repeats
			 */
			short adder = 1;

			/**
			 * Main counter: counts every step
			 */
			short i1 = 0;

			/**
			 * Phase counter: counts the phases
			 */
			short i2 = 0;

			@Override
			public boolean hasNext() {
				GridPosition next;
				while ((next = searchNext()) == null && i1 <= limit) {
				}
				return next != null;
			}

			@Override
			public GridPosition next() {

				return gridPosition;
			}

			private GridPosition searchNext() {
				int orientationOrdinal = i2 % E_Orientation.values.length;
				E_Orientation orientation = E_Orientation.values[orientationOrdinal];

				gridPosition.set((short) (gridPosition.getGridX() + orientation.getOffsetX()), (short) (gridPosition.getGridY() + orientation.getOffsetY()));

				i1++;
				adderI++;

				if (adderI == adder) {
					if (i2 % 2 == 1) {
						adder++;
					}
					i2++;
					adderI = 0;
				}

				if (isOnGrid(gridPosition)) {
					return gridPosition;
				} else {
					return null;
				}
			}

		};
	}

	public static void main(String[] args) {
		Iterator<GridPosition> spiralIterator = new IF_Grid() {

			@Override
			public short getWidth() {
				return 100;
			}

			@Override
			public short getHeight() {
				return 100;
			}

		}.getSpiralIterator((short) 2, (short) 2, (short) 30);

		while (spiralIterator.hasNext()) {
			GridPosition gridPosition = spiralIterator.next();
			System.out.println(gridPosition);
		}
	}

}
