package de.bkroeger.editor4.Handler;

import de.bkroeger.editor4.controller.IMouseHandlerData;
import javafx.scene.input.MouseEvent;

public class MousePressedCommand implements IMouseCommand {
	
	private IMouseHandlerData data;
	
	public MousePressedCommand(IMouseHandlerData data) {
		this.data = data;
	}

	public void execute(MouseEvent event) {
		
		data.setMouseX(event.getX());
		data.setMouseY(event.getY());
	}
}
