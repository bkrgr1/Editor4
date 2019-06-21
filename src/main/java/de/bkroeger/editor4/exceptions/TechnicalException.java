package de.bkroeger.editor4.exceptions;

public class TechnicalException extends Exception {

	private static final long serialVersionUID = 3066917450961742516L;

	public TechnicalException(String message) {
		super(message);
	}

	public TechnicalException(String message, Throwable e) {
		super(message, e);
	}
}
