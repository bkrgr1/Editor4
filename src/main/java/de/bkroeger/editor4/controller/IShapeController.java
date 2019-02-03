package de.bkroeger.editor4.controller;

import java.util.List;

import de.bkroeger.editor4.view.IConnector;
import javafx.scene.Node;

public interface IShapeController {

	public Node getView();
	
	public List<IConnector> getConnectors();
}
