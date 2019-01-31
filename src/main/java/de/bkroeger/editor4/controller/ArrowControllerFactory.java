package de.bkroeger.editor4.controller;

import java.util.List;

import de.bkroeger.editor4.model.IArrowModel;
import javafx.scene.Node;

public class ArrowControllerFactory {

	public static IArrowController getController(IArrowModel shapeModel, EditorController parentCtrl, List<Node> connectors) {
		
		switch (shapeModel.getArrowType()) {
		case straightArrow:
			IArrowController ctrl = (IArrowController) new StraightArrowController(shapeModel, parentCtrl, connectors);
			return ctrl;
			
		default:
			return null;
		}
	}
}
