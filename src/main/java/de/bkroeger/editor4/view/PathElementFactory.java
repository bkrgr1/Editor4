package de.bkroeger.editor4.view;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.PathElementModel;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

/**
 * <p>Diese Factory erzeugt für jedes {@link PathElementModel} 
 * ein {@link javafx.scene.shape.PathElement}.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
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
			
		case ClosePath:
			return new ClosePath();
			
			// TODO: weitere Typen ergänzen
//		case HLineTo:
//		case VLineTo:
//		case CubicCurveTo:
//		case QuadCurveTo:
		case ArcTo:
			ArcTo arcTo = new ArcTo();
			arcTo.xProperty().bind(elemModel.getXProperty());
			arcTo.yProperty().bind(elemModel.getYProperty());
			arcTo.radiusXProperty().bind(elemModel.getRadiusXProperty());
			arcTo.radiusYProperty().bind(elemModel.getRadiusYProperty());
			arcTo.largeArcFlagProperty().bind(elemModel.getLargeArcFlagProperty());
			arcTo.sweepFlagProperty().bind(elemModel.getSweepFlagProperty());
			return arcTo;
			
//		case SubPath:
			
		default:
			throw new TechnicalException("Invalid path element type: "+elemModel.getElemType().toString());
		}
	}

}
