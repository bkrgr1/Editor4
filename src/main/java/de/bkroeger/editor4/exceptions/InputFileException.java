package de.bkroeger.editor4.exceptions;

public class InputFileException extends Exception {

	private static final long serialVersionUID = 3066917450961742516L;

	public InputFileException(String message) {
		super(message);
	}

	public InputFileException(String message, Throwable e) {
		super(message, e);
	}
}
