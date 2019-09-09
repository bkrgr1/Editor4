package de.bkroeger.editor4.Handler;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.event.Event;

@FunctionalInterface
public interface IMouseCommand {

	public void execute(Event event) throws TechnicalException, CellCalculationException;
}
