package de.hetzge.sgame.entity;

import de.hetzge.sgame.entity.definition.EntityDefinition;
import de.hetzge.sgame.item.E_Item;

public enum E_EntityType {

	WORKER_LUMBERJACK(new EntityDefinition.Miner(E_Item.WOOD)), WORKER_MASON(new EntityDefinition.Miner(E_Item.STONE)),

	TREE(new EntityDefinition.Provider(E_Item.WOOD)), CAIRN(new EntityDefinition.Provider(E_Item.STONE)),

	BUILDING_LUMBERJACK(new EntityDefinition.Workstation(E_Item.WOOD)), BUILDING_QUARRY(new EntityDefinition.Workstation(E_Item.STONE)),

	CARRIER(new EntityDefinition.Carrier()),

	FACTORY(new EntityDefinition.Factory());

	public final static E_EntityType[] values = values();

	private final EntityDefinition entityDefinition;

	private E_EntityType(EntityDefinition entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	public EntityDefinition getEntityDefinition() {
		return this.entityDefinition;
	}

}
