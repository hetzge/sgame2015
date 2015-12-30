package de.hetzge.sgame.item;

import java.util.ArrayList;
import java.util.List;

import de.hetzge.sgame.booking.Booking;
import de.hetzge.sgame.booking.Container;

public class Receipt {

	private final List<Ingredient> ingredients;
	private final E_Item result;

	public Receipt(List<Ingredient> ingredients, E_Item result) {
		this.ingredients = ingredients;
		this.result = result;
	}

	/**
	 * @return null if not possible
	 */
	public E_Item build(Container container) {
		if (!possible(container)) {
			return null;
		}
		List<Booking> bookings = book(container);
		if (bookings == null) {
			return null;
		}
		for (Booking booking : bookings) {
			booking.transfer();
		}
		return this.result;
	}

	public boolean possible(Container container) {
		for (Ingredient ingredient : this.ingredients) {
			if (!ingredient.available(container)) {
				return false;
			}
		}
		return true;
	}

	private List<Booking> book(Container container) {
		List<Booking> bookings = new ArrayList<>(this.ingredients.size());
		GridEntityContainerWithoutLimit receiptContainer = new GridEntityContainerWithoutLimit(null);
		for (Ingredient ingredient : this.ingredients) {
			Booking booking = container.book(ingredient.item, ingredient.amount, receiptContainer);
			if (booking == null) {
				// rollback
				bookings.stream().forEach(Booking::rollback);
				return null;
			}
			bookings.add(booking);
		}
		return bookings;
	}

	public E_Item getResult() {
		return this.result;
	}

}
