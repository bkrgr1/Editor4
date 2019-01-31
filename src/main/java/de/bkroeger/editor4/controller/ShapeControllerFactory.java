package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.model.IShapeModel;

public class ShapeControllerFactory {

	public static IShapeController getController(IShapeModel shapeModel, EditorController parentCtrl) {
		
		switch (shapeModel.getShapeType()) {
		case Rectangle:
			IShapeController ctrl = (IShapeController) new RectangleShapeController(shapeModel, parentCtrl);
			return ctrl;
			
		default:
			return null;
		}
	}
}
