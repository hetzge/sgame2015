package de.hetzge.sgame.entity.definition;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.hetzge.sgame.booking.Container;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.entity.job.EntityJob;
import de.hetzge.sgame.entity.job.main.CarrierJob;
import de.hetzge.sgame.entity.job.main.FactoryJob;
import de.hetzge.sgame.entity.job.main.MineProviderJob;
import de.hetzge.sgame.entity.job.main.MinerJob;
import de.hetzge.sgame.entity.job.main.WorkstationJob;
import de.hetzge.sgame.item.E_Item;
import de.hetzge.sgame.item.GridEntityContainer;
import de.hetzge.sgame.item.Ingredient;
import de.hetzge.sgame.item.Receipt;
import de.hetzge.sgame.misc.Constant;

public abstract class EntityDefinition {

	protected boolean selectable = false;
	protected boolean moveable = false;
	protected short width = 1;
	protected short height = 1;
	protected int energie = 1000;
	protected int buildTime = 3000;
	protected float speed = 30f;
	protected short mineSpeed = 1;
	protected E_Item mineItem = null;
	protected short doorOffsetX = 0;
	protected short doorOffsetY = 1;
	protected int updateEveryFrames = 100; // TODO
	protected int destroyTimeInFrames = 250;
	protected Map<E_Item, Integer> mineProvides = new HashMap<>();
	protected Map<E_Item, Integer> provides = new HashMap<>();
	protected Map<E_Item, Integer> needs = new HashMap<>();
	protected Function<Entity, EntityJob> jobSupplier = (entity) -> Constant.NO_JOB;
	protected List<Receipt> receipts = Collections.emptyList();

	public boolean isSelectable() {
		return this.selectable;
	}

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

	public E_Item getMineItem() {
		return this.mineItem;
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

	public List<Receipt> getReceipts() {
		return this.receipts;
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

	public Container createDefaultMineProvideContainer(Entity entity) {
		Container container = new GridEntityContainer(entity);
		for (Entry<E_Item, Integer> entry : this.mineProvides.entrySet()) {
			E_Item item = entry.getKey();
			Integer value = entry.getValue();
			container.set(item, value);
		}
		return container;
	}

	public Container createDefaultProvideContainer(Entity entity) {
		Container container = new GridEntityContainer(entity);
		for (Entry<E_Item, Integer> entry : this.provides.entrySet()) {
			E_Item item = entry.getKey();
			Integer value = entry.getValue();
			if (value == 0) {
				container.set(item, 0, Constant.DEFAULT_MAX_ITEMS);
			} else {
				container.set(item, 0, value);
			}
		}
		return container;
	}

	public Container createDefaultNeedContainer(Entity entity) {
		Container container = new GridEntityContainer(entity);
		for (Entry<E_Item, Integer> entry : this.needs.entrySet()) {
			E_Item item = entry.getKey();
			Integer value = entry.getValue();
			container.set(item, 0, value);
		}
		return container;
	}

	public static class Dummy extends EntityDefinition {
		public Dummy() {
			this.moveable = true;
		}
	}

	public static class Miner extends EntityDefinition {
		public Miner(E_Item item) {
			this.moveable = true;
			this.jobSupplier = entity -> new MinerJob(entity);
			this.mineItem = item;
		}
	}

	public static class Provider extends EntityDefinition {
		public Provider(E_Item item) {
			Map<E_Item, Integer> provides = new HashMap<>();
			provides.put(item, 1);
			this.mineProvides = provides;
			this.jobSupplier = entity -> new MineProviderJob(entity);
		}
	}

	public static class Workstation extends EntityDefinition {
		public Workstation(E_Item item) {
			Map<E_Item, Integer> provides = new HashMap<>();
			provides.put(item, Constant.DEFAULT_MAX_ITEMS);
			this.provides = provides;
			this.mineItem = item;
			this.jobSupplier = entity -> new WorkstationJob(entity);
			this.width = 2;
			this.height = 2;
		}
	}

	public static class Carrier extends EntityDefinition {
		public Carrier() {
			this.moveable = true;
			this.jobSupplier = entity -> new CarrierJob(entity);
		}
	}

	public static class Factory extends EntityDefinition {
		public Factory() {
			HashMap<E_Item, Integer> needs = new HashMap<>();
			needs.put(E_Item.WOOD, 8);
			needs.put(E_Item.STONE, 8);
			HashMap<E_Item, Integer> provides = new HashMap<>();
			provides.put(E_Item.FISCH, 8);
			this.needs = needs;
			this.provides = provides;
			this.jobSupplier = entity -> new FactoryJob(entity);
			this.receipts = Arrays.asList(new Receipt(Arrays.asList(new Ingredient(E_Item.WOOD, 2), new Ingredient(E_Item.STONE, 2)), E_Item.FISCH));
		}
	}

}
