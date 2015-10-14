package de.hetzge.sgame.entity;

import de.hetzge.sgame.entity.definition.EntityDefinition;

public enum E_EntityType {

	DUMMY(new EntityDefinition.Dummy()), MINER(new EntityDefinition.Miner()), PROVIDER(new EntityDefinition.Provider()), WORKSTATION(new EntityDefinition.Workstation()), CARRIER(new EntityDefinition.Carrier()), FACTORY(new EntityDefinition.Factory());

	public final static E_EntityType[] values = values();

	private final EntityDefinition entityDefinition;

	private E_EntityType(EntityDefinition entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	public EntityDefinition getEntityDefinition() {
		return this.entityDefinition;
	}

}
