package de.bkroeger.editor4.Handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.controller.IMouseHandlerData;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;

/**
 * Dieser Command wird aufgerufen, wenn die über einem Shape gedrückt und gehalten wird.
 * Es wird die aktuellen Mouse-Position gespeichert.
 *
 * @author berthold.kroeger@gmx.de
 */
public class MousePressedCommand implements IMouseCommand {

	private static final Logger logger = LogManager.getLogger(MousePressedCommand.class.getName());
	
	private IMouseHandlerData data;
	
	/**
	 * Constructor
	 * @param data die {@link IMouseHandlerData Mouse-Daten}
	 */
	public MousePressedCommand(IMouseHandlerData data) {
		this.data = data;
	}

	/**
	 * Event ausführen
	 */
	public void execute(Event event) {
		
		if (event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) event;
			data.setMouseX(mouseEvent.getScreenX());
			logger.debug(String.format("%f", mouseEvent.getScreenX()));
			data.setMouseY(mouseEvent.getScreenY());
			logger.debug(String.format("%f", mouseEvent.getScreenY()));
		}
	}
}
