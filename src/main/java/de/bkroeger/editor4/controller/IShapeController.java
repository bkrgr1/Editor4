package de.bkroeger.editor4.controller;

import java.util.List;

import javafx.scene.Node;

public interface IShapeController {

	public Node getView();
	
	public List<Node> getConnectors();
}
