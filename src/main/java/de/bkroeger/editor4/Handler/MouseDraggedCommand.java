package de.bkroeger.editor4.Handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.controller.IMouseHandlerData;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.scene.input.MouseEvent;

public class MouseDraggedCommand implements IMouseCommand {

	private static final Logger logger = LogManager.getLogger(MouseDraggedCommand.class.getName());
	
	private IMouseHandlerData data;
	
	public MouseDraggedCommand(IMouseHandlerData data) {
		this.data = data;
	}

	public void execute(MouseEvent event) throws TechnicalException, CellCalculationException {
		
		double deltaX = event.getScreenX() - data.getMouseX();
		logger.debug(String.format("%f = %f - %f", deltaX, event.getScreenX(), data.getMouseX()));
		double deltaY = event.getScreenY() - data.getMouseY();
		logger.debug(String.format("%f = %f - %f", deltaY, event.getScreenY(), data.getMouseY()));
		
		data.setMouseX(event.getScreenX());
		data.setMouseY(event.getScreenY());
		
		data.setDeltaX(deltaX);
		data.setDeltaY(deltaY);
	}
}
