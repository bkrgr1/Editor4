package de.bkroeger.editor4.view;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.PathElementModel;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

public class PathElementFactory {

	public static PathElement buildPathElement(PathElementModel elemModel) 
			throws TechnicalException, CellCalculationException {
		
		switch (elemModel.getElemType()) {
		case MoveTo:
			MoveTo moveTo = new MoveTo();
			moveTo.xProperty().bind(elemModel.getXProperty());
			moveTo.yProperty().bind(elemModel.getYProperty());
			return moveTo;
			
		case LineTo:
			LineTo lineTo = new LineTo();
			lineTo.xProperty().bind(elemModel.getXProperty());
			lineTo.yProperty().bind(elemModel.getYProperty());
			return lineTo;
			
		case Close:
			return new ClosePath();
			
		default:
			throw new TechnicalException("Invalid path element type: "+elemModel.getElemType().toString());
		}
	}

}
