package de.bkroeger.editor4.Handler;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.scene.input.MouseEvent;

@FunctionalInterface
public interface IMouseCommand {

	public void execute(MouseEvent event) throws TechnicalException, CellCalculationException;
}
