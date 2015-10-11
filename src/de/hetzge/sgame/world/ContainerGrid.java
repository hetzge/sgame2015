package de.hetzge.sgame.world;

import java.io.Serializable;

import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.misc.Constant;

public class ContainerGrid implements IF_Grid, Serializable {

	private final short width;
	private final short height;

	private final Container[] containers;

	public ContainerGrid(short width, short height) {
		this.width = width;
		this.height = height;

		containers = new Container[width * height];
	}

	public Container get(GridPosition gridPosition) {
		return get(gridPosition.getGridX(), gridPosition.getGridY());
	}

	public Container get(short x, short y) {
		Container container = containers[index(x, y)];
		if (container == null) {
			containers[index(x, y)] = new Container(Constant.NO_ENTITY_ID);
			container = get(x, y);
		}
		return container;
	}

	@Override
	public short getWidth() {
		return width;
	}

	@Override
	public short getHeight() {
		return height;
	}

}
