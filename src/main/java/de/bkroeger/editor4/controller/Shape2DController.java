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
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

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

		ShapeModel model = (ShapeModel) this.getModel();

		// eine Shape-Group erzeugen
		GroupView shapeGroup = new GroupView();
		shapeGroup.prefWidthProperty().bind(model.getWidthProperty());
		shapeGroup.prefHeightProperty().bind(model.getHeightProperty());
		
		// TEST TEST TEST
		shapeGroup.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		result.setView(shapeGroup);
		
		// TODO: wie berechnet sich die Grösse des Pane?
		// muss das vor dem Hinzufügen der Pathes erfolgen?
		
		// die Location der Shape-Group festlegen
		
		// TODO: die Position des Shapes ist an den angegebenen Layout-Properties
		// aber nicht mit der linken, oberen Ecke, sondern der definierte
		// Center-Point ist an diesen Koordinaten
		// z.B. setLayoutX( layoutX - centerX )
		shapeGroup.layoutXProperty().bind(
				Bindings.subtract(model.getLayoutXProperty(), model.getCenterXProperty()));
		shapeGroup.layoutYProperty().bind(
				Bindings.subtract(model.getLayoutXProperty(), model.getCenterYProperty()));
		
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
