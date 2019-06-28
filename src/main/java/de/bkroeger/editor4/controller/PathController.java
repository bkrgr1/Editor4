package de.bkroeger.editor4.controller;

import java.util.List;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.CellModel;
import de.bkroeger.editor4.model.PathElementModel;
import de.bkroeger.editor4.model.PathModel;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.SectionModelType;
import de.bkroeger.editor4.model.StyleModel;
import de.bkroeger.editor4.view.PathElementFactory;
import de.bkroeger.editor4.view.PathView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.PathElement;

public class PathController extends BaseController {
	
    public PathController(PathModel pathModel) throws TechnicalException {
        super();
        this.model = pathModel; 
        if (pathModel == null) throw new TechnicalException("Path model is NULL");
    }

	public ControllerResult buildView(ControllerResult shapeResult) 
			throws TechnicalException, CellCalculationException, InputFileException {
		
		ControllerResult result = new ControllerResult();
    	result.setController(this);
		
		// Pfad zeichnen
		PathView pathView = drawPath((PathModel)this.model);
		result.setView(pathView);
			
		return result;
	}
	
	private PathView drawPath(PathModel model) 
			throws TechnicalException, CellCalculationException, InputFileException {
		
		PathView path = new PathView();
		
		List<SectionModel> elemSections = model.selectSections(SectionModelType.PathElement);
		for (SectionModel elemSection : elemSections) {
			
			PathElementModel elemModel = (PathElementModel) elemSection;
			PathElement elem = PathElementFactory.buildPathElement(elemModel);
			path.getElements().add(elem);
		}
		
		StyleModel styleModel = (StyleModel) model.getSection(SectionModelType.Style);
		if (styleModel != null) {
			
			// fill color
			CellModel colorCell = styleModel.getCell("FillColor");
			if (colorCell != null) {				
				Paint paint = (Paint) colorCell.getObjectValue();
				path.setFill(paint);
			}
			
			// stroke width
			CellModel strokeWidthCell = styleModel.getCell("StrokeWidth");
			if (strokeWidthCell != null) {				
				path.strokeWidthProperty().bind(strokeWidthCell.getDoubleProperty());
			}
			
			// transparency
			CellModel transparencyCell = styleModel.getCell("Transparency");
			if (transparencyCell != null) {				
				
			}
		}
		
		return path;
	}
}
