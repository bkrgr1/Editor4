package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.IConnectorModel;
import javafx.scene.Node;

public interface IConnectorController {

	public Node getView();
	
	public IConnectorModel getModel();
}
