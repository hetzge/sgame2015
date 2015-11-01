package de.hetzge.sgame.world;

import java.io.Serializable;

import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.ContainerWithoutLimit;
import de.hetzge.sgame.item.E_Item;

public class ContainerGrid implements IF_Grid, Serializable {

	private final short width;
	private final short height;

	private final Container[] containers;

	public ContainerGrid(short width, short height) {
		this.width = width;
		this.height = height;

		this.containers = new Container[width * height];
	}

	public Container get(GridPosition gridPosition) {
		return get(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public Container get(short x, short y) {
		Container container = this.containers[index(x, y)];
		if (container == null) {
			this.containers[index(x, y)] = new ContainerWithoutLimit(new GridPosition(x, y));
			container = get(x, y);
		}
		return container;
	}

	public boolean isContainer(short x, short y) {
		Container container = this.containers[index(x, y)];
		return container != null;
	}

	public boolean hasItemAvailable(GridPosition gridPosition, E_Item item) {
		return hasItemAvailable(gridPosition.getGridX(), gridPosition.getGridY(), item);
	}

	public boolean hasItemAvailable(short x, short y, E_Item item) {
		Container container = this.containers[index(x, y)];
		if (container != null) {
			return container.hasAmountAvailable(item, 1);
		} else {
			return false;
		}
	}

	@Override
	public short getWidth() {
		return this.width;
	}

	@Override
	public short getHeight() {
		return this.height;
	}

}
