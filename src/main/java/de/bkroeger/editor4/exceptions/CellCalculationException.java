package de.bkroeger.editor4.exceptions;

/**
 * Exception, wenn sich ein Zellwert nicht berechnen lässt
 *
 * @author berthold.kroeger@gmx.de
 */
public class CellCalculationException extends Exception {

	private static final long serialVersionUID = 3066917450961742516L;

	public CellCalculationException(String message) {
		super(message);
	}

	public CellCalculationException(String message, Throwable e) {
		super(message, e);
	}
}
