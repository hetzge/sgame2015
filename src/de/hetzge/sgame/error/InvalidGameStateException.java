package de.hetzge.sgame.error;

public class InvalidGameStateException extends RuntimeException {

	public InvalidGameStateException() {
		super();
	}

	public InvalidGameStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidGameStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidGameStateException(String message) {
		super(message);
	}

	public InvalidGameStateException(Throwable cause) {
		super(cause);
	}

}
