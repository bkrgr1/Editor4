package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.ShapeModel;

/**
 * <p>Die ShapeControllerFactory erzeugt einen passenden Controller für das jeweilige Shape.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
public class ShapeControllerFactory {

	public static ShapeController getShapeController(SectionModel model) throws TechnicalException {
		
		if (model instanceof ShapeModel) {
			ShapeModel shapeModel = (ShapeModel) model;
			
			if (shapeModel.getShapeDimension().equals("2D")) {
				
				return new Shape2DController(shapeModel);
			} else {
				
				return new Shape1DController(model);
			}
		} else {
			throw new TechnicalException("Model is not of type 'ShapeModel'");
		}
	}
}
