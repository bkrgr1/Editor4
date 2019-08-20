package de.bkroeger.editor4.Handler;

import de.bkroeger.editor4.controller.IMouseHandlerData;
import de.bkroeger.editor4.model.IModel;
import javafx.scene.input.MouseEvent;

public class MouseReleasedCommand implements IMouseCommand {
	
	private IMouseHandlerData data;
	private IModel model;
	
	public MouseReleasedCommand(IMouseHandlerData data, IModel model) {
		this.data = data;
		this.model = model;
	}

	public void execute(MouseEvent event) {
		
		data.setMouseX(event.getX());
		data.setMouseY(event.getY());
	}
}
