package de.bkroeger.editor4.controller;

import java.util.List;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.PathModel;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.SectionModelType;
import de.bkroeger.editor4.model.ShapeModel;
import de.bkroeger.editor4.view.GroupView;
import javafx.scene.Node;

/**
 * <p>Dies ist der Controller für 2-dimensionale Shapes.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
public class Shape2DController extends ShapeController {

	/**
	 * Constructor
	 * @param model
	 */
	public Shape2DController(SectionModel model) {
		super(model);
	}
    
	/**
	 * <p>Generiert die Nodes für die Darstellung des Shapes.</p>
	 * @throws InputFileException 
	 * @throws CellCalculationException 
	 */
	@Override
    public ControllerResult buildView(ControllerResult parentController) 
    		throws TechnicalException, InputFileException, CellCalculationException {
    	
		ControllerResult result = new ControllerResult();
		
		// eine Shape-Group erzeugen
		GroupView shapeGroup = new GroupView();
		result.setView(shapeGroup);
		
		// die Location der Shape-Group festlegen
//		SectionModel locationSection = model.getSection(SectionModelType.Location);
//		LocationModel locationModel = (LocationModel) locationSection;
		// die Properties sind jetzt im Shape-Model
		ShapeModel model = (ShapeModel) this.getModel();
		shapeGroup.layoutXProperty().bind(model.getLayoutXProperty());
		shapeGroup.layoutYProperty().bind(model.getLayoutXProperty());
		
		// alle Path-Sections ermitteln und die Pfade zeichnen
		List<SectionModel> pathSections = model.selectSections(SectionModelType.Path);
		for (SectionModel pathSection : pathSections) {

        	// Path zeichnen
			PathModel pathModel = (PathModel) pathSection;
			PathController pathController = new PathController(pathModel);
        	ControllerResult pathResult = pathController.buildView(result);
        	
        	// und zur Gruppe hinzufügen
        	shapeGroup.getChildren().add((Node) pathResult.getView());
		}
		
		// rotate
		
		// scale
		
		
    	return result;
    }
}
