package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.ShapeModel;
import de.bkroeger.editor4.runtime.ShapeRuntime;

/**
 * <p>Die ShapeControllerFactory erzeugt einen passenden Controller für das jeweilige Shape.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
public class ShapeControllerFactory {
	
	private ShapeControllerFactory() { }

	public static ShapeController getShapeController(ShapeRuntime shapeRuntime) throws TechnicalException {
		
		ShapeModel shapeModel = shapeRuntime.getModel();
		if (shapeModel.getShapeDimension().equals("2D")) {
			
			return new Shape2DController(shapeRuntime);
		} else {
			
			return new Shape1DController(shapeRuntime);
		}
	}
}
