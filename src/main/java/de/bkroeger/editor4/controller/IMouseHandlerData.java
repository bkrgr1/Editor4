package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;

public interface IMouseHandlerData {

	public double getMouseX();
	public void setMouseX(double value);
	public double getMouseY();
	public void setMouseY(double value);
	public void setDeltaX(double value) throws TechnicalException, CellCalculationException;
	public void setDeltaY(double value) throws TechnicalException, CellCalculationException;
}
