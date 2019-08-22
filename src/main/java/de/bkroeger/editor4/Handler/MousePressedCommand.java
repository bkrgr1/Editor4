package de.bkroeger.editor4.Handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.controller.IMouseHandlerData;
import javafx.scene.input.MouseEvent;

public class MousePressedCommand implements IMouseCommand {

	private static final Logger logger = LogManager.getLogger(MousePressedCommand.class.getName());
	
	private IMouseHandlerData data;
	
	public MousePressedCommand(IMouseHandlerData data) {
		this.data = data;
	}

	public void execute(MouseEvent event) {
		
		data.setMouseX(event.getScreenX());
		logger.debug(String.format("%f", event.getScreenX()));
		data.setMouseY(event.getScreenY());
		logger.debug(String.format("%f", event.getScreenY()));
	}
}
