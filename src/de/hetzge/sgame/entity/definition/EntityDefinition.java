package de.hetzge.sgame.entity.definition;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.main.MineProviderJob;
import de.hetzge.sgame.entity.job.main.MinerJob;
import de.hetzge.sgame.entity.job.main.WorkstationJob;
import de.hetzge.sgame.item.Container;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.misc.Constant;

public abstract class EntityDefinition {

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
	protected int destroyTimeInFrames = 250;
	protected Map<E_Item, Integer> mineProvides = new HashMap<>();
	protected Map<E_Item, Integer> provides = new HashMap<>();
	protected Map<E_Item, Integer> needs = new HashMap<>();
	protected Function<Entity, EntityJob> jobSupplier = (entity) -> Constant.NO_JOB;

	public boolean isMoveable() {
		return this.moveable;
	}

	public short getWidth() {
		return this.width;
	}

	public short getHeight() {
		return this.height;
	}

	public int getEnergie() {
		return this.energie;
	}

	public int getBuildTime() {
		return this.buildTime;
	}

	public float getSpeed() {
		return this.speed;
	}

	public short getMineSpeed() {
		return this.mineSpeed;
	}

	public short getDoorOffsetX() {
		return this.doorOffsetX;
	}

	public short getDoorOffsetY() {
		return this.doorOffsetY;
	}

	public int getDestroyTimeInFrames() {
		return this.destroyTimeInFrames;
	}

	public boolean doProvide(E_Item item) {
		return this.provides.containsKey(item);
	}

	public boolean doNeeds(E_Item item) {
		return this.needs.containsKey(item);
	}

	public EntityJob createJob(Entity entity) {
		return this.jobSupplier.apply(entity);
	}

	public static class Dummy extends EntityDefinition {
		public Dummy() {
			this.moveable = true;
		}
	}

	public static class Miner extends EntityDefinition {
		public Miner() {
			this.moveable = true;
			this.jobSupplier = entity -> new MinerJob(entity);
		}
	}

	public static class Provider extends EntityDefinition {
		public Provider() {
			Map<E_Item, Integer> provides = new HashMap<>();
			provides.put(E_Item.WOOD, 10);
			this.mineProvides = provides;
			this.jobSupplier = entity -> new MineProviderJob(entity);
		}
	}

	public static class Workstation extends EntityDefinition {
		public Workstation() {
			Map<E_Item, Integer> needs = new HashMap<>();
			needs.put(E_Item.WOOD, 3);
			this.needs = needs;
			this.jobSupplier = entity -> new WorkstationJob(entity);
		}
	}

	public Container createDefaultMineProvideContainer(Entity entity) {
		Container container = new Container(entity);
		for (Entry<E_Item, Integer> entry : this.mineProvides.entrySet()) {
			E_Item item = entry.getKey();
			Integer value = entry.getValue();
			container.set(item, value);
		}
		return container;
	}

	public Container createDefaultProvideContainer(Entity entity) {
		Container container = new Container(entity);
		for (Entry<E_Item, Integer> entry : this.provides.entrySet()) {
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

	public Container createDefaultNeedContainer(Entity entity) {
		Container container = new Container(entity);
		for (Entry<E_Item, Integer> entry : this.needs.entrySet()) {
			E_Item item = entry.getKey();
			Integer value = entry.getValue();
			container.set(item, 0, value);
		}
		return container;
	}

}
