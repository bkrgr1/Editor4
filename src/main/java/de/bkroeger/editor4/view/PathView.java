package de.bkroeger.editor4.view;

import java.util.List;

import de.bkroeger.editor4.controller.PathElemController;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.CellModel;
import de.bkroeger.editor4.model.PathElementModel;
import de.bkroeger.editor4.model.PathModel;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.SectionModelType;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.StrokeType;

public class PathView extends Path implements IView {
	
	private PathElemController controller;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public PathView(PathElemController controller) {
		super();
		this.controller = controller;
	}
	
	/**
	 * <p>Zeichnet den Pfad aus den einzelnen Sections.</p>
	 * @param model das {@link PathModel Modell} des PathView
	 * @return dieses PathView
	 * @throws TechnicalException
	 * @throws CellCalculationException
	 * @throws InputFileException
	 */
	public PathView draw() 
			throws TechnicalException, CellCalculationException, InputFileException {
		
		// alle untergeordneten Sections verarbeiten
		PathModel model = (PathModel) controller.getModel();
		List<SectionModel> elemSections = model.selectSections(SectionModelType.PathElement);
		for (SectionModel elemSection : elemSections) {
			
			PathElement elem = PathElementFactory.buildPathElement((PathElementModel) elemSection);
			this.getElements().add(elem);
		}
		
		// die Optionen aus der Style-Section verarbeiten
			
		// fill color
		CellModel colorCell = model.getCellByName("FillColor");
		if (colorCell != null) {				
			Paint paint = (Paint) colorCell.getObjectValue();
			this.setFill(paint);
			// TODO: ObjectChangeListener ergänzen
		}
		
		// stroke type
		this.setStrokeType(StrokeType.INSIDE);
		
		// stroke color
		CellModel colorStroke = model.getCellByName("StrokeColor");
		if (colorStroke != null) {				
			Paint paint = (Paint) colorStroke.getObjectValue();
			this.setStroke(paint);
		}
		
		// stroke width
		CellModel strokeWidthCell = model.getCellByName("StrokeWidth");
		if (strokeWidthCell != null) {				
			this.strokeWidthProperty().bind(strokeWidthCell.getDoubleProperty());
		}
		
		// transparency
		CellModel transparencyCell = model.getCellByName("Transparency");
		if (transparencyCell != null) {				
			this.opacityProperty().bind(transparencyCell.getDoubleProperty());
		}
		
		return this;
	}
}
