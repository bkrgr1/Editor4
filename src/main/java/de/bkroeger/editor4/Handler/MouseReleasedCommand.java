package de.bkroeger.editor4.Handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.controller.IMouseHandlerData;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.scene.input.MouseEvent;

/**
 * Dieser Command wird aufgerufen, wenn die Mouse released wird.
 * Es wird das Delta zwischen der vorherigen Position und der aktuellen Mouse-Position berechnet
 * und an den Command-Receiver gemeldet.
 *
 * @author berthold.kroeger@gmx.de
 */
public class MouseReleasedCommand implements IMouseCommand {

	private static final Logger logger = LogManager.getLogger(MouseReleasedCommand.class.getName());
	
	private IMouseHandlerData data;
	
	/**
	 * Constructor
	 * @param data die {@link IMouseHandlerData Mouse-Daten}
	 */
	public MouseReleasedCommand(IMouseHandlerData data) {
		this.data = data;
	}

	/**
	 * Event ausführen
	 */
	public void execute(MouseEvent event) throws TechnicalException, CellCalculationException {
		
		double deltaX = event.getScreenX() - data.getMouseX();
		logger.debug(String.format("%f = %f - %f", deltaX, event.getScreenX(), data.getMouseX()));
		double deltaY = event.getScreenY() - data.getMouseY();
		logger.debug(String.format("%f = %f - %f", deltaY, event.getScreenY(), data.getMouseY()));
		
		data.setMouseX(0.0);
		data.setMouseY(0.0);
		
		data.setDeltaX(deltaX);
		data.setDeltaY(deltaY);
	}
}
