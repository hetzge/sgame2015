package de.hetzge.sgame.item;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.error.InvalidGameStateException;
import de.hetzge.sgame.world.IF_GridEntity;

public class Container implements Serializable {

	private class Value implements Serializable {
		private int max;
		private int amount;
	}

	private final IF_GridEntity entity;

	private final Map<E_Item, Value> items = new HashMap<>();
	private final List<Booking> bookings = new LinkedList<>();

	public Container(Entity entity) {
		this.entity = entity;
	}

	public synchronized void transfer(Booking booking) {
		if (booking.from != this) {
			throw new IllegalArgumentException("You can only transfer bookings from self container.");
		}
		if (!this.hasBooking(booking) || !booking.to.hasBooking(booking)) {
			throw new IllegalArgumentException("Try to transfer a booking that not exists for container.");
		}
		synchronized (booking.to) {
			boolean transferSuccessful = this.transfer(booking.item, booking.amount, booking.to, true);
			if (transferSuccessful) {
				this.bookings.remove(booking);
				booking.to.bookings.remove(booking);
			} else {
				throw new IllegalStateException("Transfer booking failed.");
			}
		}
	}

	public synchronized boolean transfer(E_Item item, int amount, Container to) {
		return this.transfer(item, amount, to, false);
	}

	public synchronized boolean transfer(E_Item item, int amount, Container to, boolean booking) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Only positive amounts can be transfered");
		}
		synchronized (to) {
			if (!this.can(item) || !to.can(item) || !this.has(item) || (booking ? !this.hasBooking(item, amount, this, to) : !to.canAddAmount(item, amount)) || (booking ? !this.hasBooking(item, amount, this, to) : !this.hasAmountAvailable(item, amount))) {
				return false;
			}
			Value value = this.items.get(item);
			value.amount -= amount;

			Value toValue = to.items.get(item);
			if (toValue == null) {
				to.createUnlimitedDefaultValue(item);
				toValue = to.items.get(item);
			}
			toValue.amount += amount;
		}

		return true;
	}

	private void createUnlimitedDefaultValue(E_Item item) {
		Value newValue = new Value();
		newValue.max = Integer.MAX_VALUE;
		this.items.put(item, newValue);
	}

	private boolean hasBooking(E_Item item, int amount, Container from, Container to) {
		return this.hasBooking(new Booking(item, amount, from, to));
	}

	private boolean hasBooking(Booking booking) {
		return this.bookings.contains(booking);
	}

	public synchronized Booking book(E_Item item, int amount, Container to) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Only positive amounts can be booked");
		}
		synchronized (to) {
			if (!this.has(item) || !this.hasAmountAvailable(item, amount)) {
				return null;
			}
			if (!to.can(item) || !to.canAddAmount(item, amount)) {
				return null;
			}

			Booking booking = new Booking(item, amount, this, to);
			this.addBooking(booking);
			to.addBooking(booking);

			return booking;
		}
	}

	protected synchronized void addBooking(Booking booking) {
		this.bookings.add(booking);
	}

	protected synchronized void removeBooking(Booking booking) {
		this.bookings.remove(booking);
	}

	public synchronized boolean hasAmountAvailable(E_Item item, int amount) {
		return this.has(item) && this.amount(item) - this.bookedFromAmount(item) >= amount;
	}

	public synchronized boolean canAddAmount(E_Item item, int amount) {
		Value value = this.items.get(item);
		return this.can(item) && this.amount(item) + this.bookedToAmount(item) + amount <= value.max;
	}

	/**
	 * Returns the amount of a given item is booked from this container.
	 */
	private int bookedFromAmount(E_Item item) {
		int amount = 0;
		for (Booking booking : this.bookings) {
			if (booking.from == this && booking.item.equals(item)) {
				amount += booking.amount;
			}
		}
		return amount;
	}

	/**
	 * Returns the amount of a given item is reserved to bring to this
	 * container.
	 */
	private int bookedToAmount(E_Item item) {
		int amount = 0;
		for (Booking booking : this.bookings) {
			if (booking.to == this && booking.item.equals(item)) {
				amount += booking.amount;
			}
		}
		return amount;
	}

	public synchronized boolean can(E_Item item) {
		return this.items.get(item) != null;
	}

	public synchronized boolean has(E_Item item) {
		return this.amount(item) > 0;
	}

	public synchronized int amount(E_Item item) {
		Value value = this.items.get(item);
		if (value == null) {
			return 0;
		}
		return value.amount;
	}

	public synchronized Set<E_Item> getItems() {
		return this.items.keySet();
	}

	public synchronized void set(E_Item item, int amount, int max) {
		Value value = this.getOrCreateValue(item);
		value.amount = amount;
		value.max = max;
	}

	public synchronized void set(E_Item item, int amountAndMax) {
		this.set(item, amountAndMax, amountAndMax);
	}

	public synchronized void setAmount(E_Item item, int amount) {
		Value value = this.getOrCreateValue(item);
		value.amount = amount;
	}

	public synchronized void setMax(E_Item item, int max) {
		Value value = this.getOrCreateValue(item);
		value.max = max;
	}

	private Value getOrCreateValue(E_Item item) {
		Value value = this.items.get(item);
		if (value == null) {
			value = new Value();
			this.items.put(item, value);
		}
		return value;
	}

	public synchronized void unchain() {
		for (Booking booking : this.bookings) {
			booking.to.removeBooking(booking);
		}
		this.bookings.clear();
	}

	/*
	 * TODO bisschen unsauber hier
	 */

	public IF_GridEntity getObject() {
		return this.entity;
	}

	public Entity getEntity() {
		if (this.entity instanceof Entity) {
			return (Entity) this.entity;
		} else {
			throw new InvalidGameStateException();
		}
	}

}