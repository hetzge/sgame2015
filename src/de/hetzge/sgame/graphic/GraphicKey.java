package de.hetzge.sgame.graphic;

import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.misc.E_Orientation;

/**
 * Descripes which graphic should be rendered for a entity. This includes the
 * orientation, the activity and the entity type.
 * 
 * @author hetzge
 */
public class GraphicKey implements IF_GraphicKey {

	private byte orientation;
	private byte activity;
	private byte entityType;

	public GraphicKey() {
	}

	public GraphicKey(E_Orientation orientation, E_Activity activity, E_EntityType entityType) {
		setOrientation(orientation);
		setActivity(activity);
		setEntityType(entityType);
	}

	@Override
	public E_Orientation getOrientation() {
		return E_Orientation.values[orientation];
	}

	public IF_GraphicKey setOrientation(E_Orientation orientation) {
		this.orientation = (byte) orientation.ordinal();
		return this;
	}

	@Override
	public E_Activity getActivity() {
		return E_Activity.values[activity];
	}

	public IF_GraphicKey setActivity(E_Activity activity) {
		this.activity = (byte) activity.ordinal();
		return this;
	}

	@Override
	public E_EntityType getEntityType() {
		return E_EntityType.values[entityType];
	}

	public IF_GraphicKey setEntityType(E_EntityType entityType) {
		this.entityType = (byte) entityType.ordinal();
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + activity;
		result = prime * result + entityType;
		result = prime * result + orientation;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphicKey other = (GraphicKey) obj;
		if (activity != other.activity)
			return false;
		if (entityType != other.entityType)
			return false;
		if (orientation != other.orientation)
			return false;
		return true;
	}

	@Override
	public int hashGraphicKey() {
		return hashCode();
	}

}
