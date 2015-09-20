package de.hetzge.sgame.entity.definition;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.hetzge.sgame.entity.Container;
import de.hetzge.sgame.entity.E_Item;
import de.hetzge.sgame.misc.Constant;

public abstract class EntityDefinition {

	protected boolean moveable = false;
	protected short width = 1;
	protected short height = 1;
	protected int energie = 1000;
	protected int buildTime = 3000;
	protected float speed = 30f;
	protected Map<E_Item, Integer> provides = new HashMap<>();
	protected Map<E_Item, Integer> needs = new HashMap<>();

	public boolean isMoveable() {
		return moveable;
	}

	public short getWidth() {
		return width;
	}

	public short getHeight() {
		return height;
	}

	public int getEnergie() {
		return energie;
	}

	public int getBuildTime() {
		return buildTime;
	}

	public float getSpeed() {
		return speed;
	}

	public boolean doProvide(E_Item item) {
		return provides.containsKey(item);
	}

	public boolean doNeeds(E_Item item) {
		return needs.containsKey(item);
	}

	public static class Dummy extends EntityDefinition {
		public Dummy() {
			moveable = true;
		}
	}

	public Container createDefaultProvideContainer(int entityId) {
		Container container = new Container(entityId);
		for (Entry<E_Item, Integer> entry : provides.entrySet()) {
			E_Item item = entry.getKey();
			Integer value = entry.getValue();
			if (value == 0) {
				container.set(item, value, Constant.DEFAULT_MAX_ITEMS);
			} else {
				container.set(item, value);
			}
		}
		return container;
	}

	public Container createDefaultNeedContainer(int entityId) {
		Container container = new Container(entityId);
		for (Entry<E_Item, Integer> entry : provides.entrySet()) {
			E_Item item = entry.getKey();
			Integer value = entry.getValue();
			container.set(item, 0, value);
		}
		return container;
	}

}
