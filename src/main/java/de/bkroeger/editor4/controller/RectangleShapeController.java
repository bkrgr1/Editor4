package de.bkroeger.editor4.controller;

import java.util.List;

import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.model.RectangleShapeModel;
import de.bkroeger.editor4.view.IConnector;
import de.bkroeger.editor4.view.IShapeView;
import de.bkroeger.editor4.view.RectangleShapeView;
import javafx.scene.Node;

public class RectangleShapeController implements IShapeController {
	
	private IShapeView view;
	@Override
	public Node getView() { return (Node) view; }

	
	public RectangleShapeController(IShapeModel model, EditorController parentController) {
		
		RectangleShapeModel rectModel = (RectangleShapeModel) model;
		view = new RectangleShapeView(rectModel.getXProperty(), rectModel.getYProperty(),
				rectModel.getWidthProperty(), rectModel.getHeightProperty());
	}

	@Override
	public List<IConnector> getConnectors() { return view.getConnectors(); }
}
