package de.hetzge.sgame.entity.definition;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.main.MineProviderJob;
import de.hetzge.sgame.entity.job.main.MinerJob;
import de.hetzge.sgame.entity.job.main.NoJob;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;

public abstract class EntityDefinition {

	private static final EntityJob NO_JOB = new NoJob();

	protected boolean moveable = false;
	protected short width = 1;
	protected short height = 1;
	protected int energie = 1000;
	protected int buildTime = 3000;
	protected float speed = 30f;
	protected short mineSpeed = 1;
	protected short doorOffsetX = 0;
	protected short doorOffsetY = 1;
	protected int updateEveryFrames = 100; // TODO
	protected Map<E_Item, Integer> mineProvides = new HashMap<>();
	protected Map<E_Item, Integer> provides = new HashMap<>();
	protected Map<E_Item, Integer> needs = new HashMap<>();
	protected Function<Entity, EntityJob> jobSupplier = (entity) -> NO_JOB;

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

	public short getMineSpeed() {
		return mineSpeed;
	}

	public short getDoorOffsetX() {
		return doorOffsetX;
	}

	public short getDoorOffsetY() {
		return doorOffsetY;
	}

	public boolean doProvide(E_Item item) {
		return provides.containsKey(item);
	}

	public boolean doNeeds(E_Item item) {
		return needs.containsKey(item);
	}

	public EntityJob createJob(Entity entity) {
		return jobSupplier.apply(entity);
	}

	public static class Dummy extends EntityDefinition {
		public Dummy() {
			moveable = true;
		}
	}

	public static class Miner extends EntityDefinition {
		public Miner() {
			jobSupplier = (entity) -> new MinerJob();
		}
	}

	public static class Provider extends EntityDefinition {
		public Provider() {
			Map<E_Item, Integer> provides = new HashMap<>();
			provides.put(E_Item.WOOD, 200);
			mineProvides = provides;
			jobSupplier = (entity) -> new MineProviderJob(entity);
		}
	}

	public static class Workstation extends EntityDefinition {
		public Workstation() {
			Map<E_Item, Integer> needs = new HashMap<>();
			needs.put(E_Item.WOOD, 10);
			this.needs = needs;
		}
	}

	public Container createDefaultMineProvideContainer(int entityId) {
		Container container = new Container(entityId);
		for (Entry<E_Item, Integer> entry : mineProvides.entrySet()) {
			E_Item item = entry.getKey();
			Integer value = entry.getValue();
			container.set(item, value);
		}
		return container;
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
