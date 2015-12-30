package de.hetzge.sgame.item;

import de.hetzge.sgame.booking.Container;

public class Ingredient {
	protected final E_Item item;
	protected final int amount;

	public Ingredient(E_Item item, int amount) {
		this.item = item;
		this.amount = amount;
	}

	public boolean available(Container container) {
		return container.hasAmountAvailable(this.item, this.amount);
	}

}