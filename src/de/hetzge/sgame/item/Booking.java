package de.hetzge.sgame.item;

public class Booking {
	final E_Item item;
	final int amount;
	final Container from;
	Container to;

	Booking(E_Item item, int amount, Container from, Container to) {
		if (item == null || from == null || to == null) {
			throw new IllegalArgumentException("Invalid null parameter. No null values allowed: item->" + item + ", from->" + from + ", to->" + to);
		}
		if (from == to) {
			throw new IllegalArgumentException("No booking to self container allowed.");
		}
		if (amount <= 0) {
			throw new IllegalArgumentException("No amount smaller or equal 0 allowed: " + amount);
		}
		this.item = item;
		this.amount = amount;
		this.from = from;
		this.to = to;
	}

	public Container getFrom() {
		return from;
	}

	public Container getTo() {
		return to;
	}

	public void changeTo(Container container) {
		to.removeBooking(this);
		to = container;
	}

	public E_Item getItem() {
		return item;
	}

	public void rollback() {
		from.removeBooking(this);
		to.removeBooking(this);
	}

	public boolean transfer() {
		return from.transfer(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Booking other = (Booking) obj;
		if (amount != other.amount) {
			return false;
		}
		if (from == null) {
			if (other.from != null) {
				return false;
			}
		} else if (!from.equals(other.from)) {
			return false;
		}
		if (item == null) {
			if (other.item != null) {
				return false;
			}
		} else if (!item.equals(other.item)) {
			return false;
		}
		if (to == null) {
			if (other.to != null) {
				return false;
			}
		} else if (!to.equals(other.to)) {
			return false;
		}
		return true;
	}

}