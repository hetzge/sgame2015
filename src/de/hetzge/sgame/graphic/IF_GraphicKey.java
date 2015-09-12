package de.hetzge.sgame.graphic;

import de.hetzge.sgame.entity.E_Activity;
import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.misc.E_Orientation;

public interface IF_GraphicKey {

	E_Orientation getOrientation();

	E_Activity getActivity();

	E_EntityType getEntityType();

	int hashGraphicKey();

	public default String hashGraphicKeyString() {
		return "(orientation: " + getOrientation() + ", activity: " + getActivity() + ", entityType: " + getEntityType() + ")";
	}

}