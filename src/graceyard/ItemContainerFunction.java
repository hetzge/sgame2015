package graceyard;

import de.hetzge.sgame.item.E_Item;

public class ItemContainerFunction {

	public static final int MAX_ITEMS = 9;

	public E_Item getUseItem(byte[] container) {
		if (getValue(container, E_ContainerType.USE.FIRST_INDEX) == 0) {
			return null;
		} else {
			return getItem(container, E_ContainerType.USE.FIRST_INDEX);
		}
	}

	public void setUseItem(byte[] container, E_Item item) {
		set(container, E_ContainerType.USE.FIRST_INDEX, item, 1);
	}

	public void unsetUseItem(byte[] container, E_Item item) {
		set(container, E_ContainerType.USE.FIRST_INDEX, item, 0);
	}

	public boolean canItem(byte[] container, E_ContainerType containerType, E_Item item) {
		for (int i = 0; i < containerType.COUNT; i++) {
			E_Item item2 = getItem(container, containerType.FIRST_INDEX + i);
			if (item2 == item) {
				return true;
			}
		}
		return false;
	}

	public int sum(byte[] container, E_ContainerType containerType, E_Item item) {
		int sum = 0;
		for (int i = 0; i < containerType.COUNT; i++) {
			int slotIndex = containerType.FIRST_INDEX + i;
			E_Item item2 = getItem(container, slotIndex);
			if (item2 == item) {
				byte value = getValue(container, slotIndex);
				sum += value;
			}
		}
		return sum;
	}

	public boolean transfer(byte[] fromContainer, byte[] toContainer, E_ContainerType fromContainerType, E_ContainerType toContainerType, E_Item item) {
		boolean canFromItem = canItem(fromContainer, fromContainerType, item);
		boolean canToItem = canItem(toContainer, toContainerType, item);
		if (!canFromItem || !canToItem) {
			return false;
		}
		int available = sum(fromContainer, fromContainerType, item);
		int space = sum(toContainer, toContainerType, item) % MAX_ITEMS - 1;
		if (available == 0 || space == 0) {
			return false;
		}
		boolean add = add(fromContainer, fromContainerType, item);
		boolean reduce = reduce(toContainer, toContainerType, item);
		if (!add || !reduce) {
			throw new IllegalStateException("Item transaction not valid");
		}
		return true;
	}

	private int shortToOrdinal(int shortValue) {
		return (int) Math.floor((shortValue - 1) / MAX_ITEMS);
	}

	private int ordinalToShort(int ordinal) {
		return 1 + (ordinal * MAX_ITEMS);
	}

	private boolean reduce(byte[] container, E_ContainerType containerType, E_Item item) {
		return add(container, containerType, item, -1);
	}

	private boolean add(byte[] container, E_ContainerType containerType, E_Item item) {
		return add(container, containerType, item, 1);
	}

	private boolean add(byte[] container, E_ContainerType containerType, E_Item item, int value) {
		for (int i = 0; i < containerType.COUNT; i++) {
			int slotIndex = containerType.FIRST_INDEX + i;
			E_Item item2 = getItem(container, slotIndex);
			if (item2 == item) {
				byte value2 = getValue(container, slotIndex);
				if (value2 < MAX_ITEMS - 1) {
					int newValue = value2 + value;
					if (newValue >= MAX_ITEMS) {
						throw new IllegalStateException("Exceed max item value");
					}
					if (newValue < 0) {
						throw new IllegalStateException("Exceed min item value");
					}
					set(container, slotIndex, item, newValue);
					return true;
				}
			}
		}
		return false;
	}

	private void set(byte[] container, int slotIndex, E_Item item, int value) {
		if (value > MAX_ITEMS) {
			throw new IllegalAccessError("value is bigger then max value");
		}
		container[slotIndex] = (byte) (ordinalToShort(item.ordinal()) + value);
	}

	private byte getValue(byte[] container, int slotIndex) {
		return (byte) ((container[slotIndex] - 1) % MAX_ITEMS);
	}

	private E_Item getItem(byte[] container, int slotIndex) {
		return E_Item.values[shortToOrdinal(container[slotIndex])];
	}

}
