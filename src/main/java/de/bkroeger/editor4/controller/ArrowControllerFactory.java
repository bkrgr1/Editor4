package de.bkroeger.editor4.controller;

import java.util.List;

import de.bkroeger.editor4.model.IArrowModel;

public class ArrowControllerFactory {

	public static IArrowController getController(IArrowModel shapeModel, 
			EditorController parentCtrl, 
			List<IShapeController> shapes) {
		
		switch (shapeModel.getArrowType()) {
		case straightArrow:
			IArrowController ctrl = (IArrowController) new StraightArrowController(shapeModel, parentCtrl, shapes);
			return ctrl;
			
		default:
			return null;
		}
	}
}
