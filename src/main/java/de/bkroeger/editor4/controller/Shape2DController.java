package de.bkroeger.editor4.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.Handler.MouseDraggedCommand;
import de.bkroeger.editor4.Handler.MousePressedCommand;
import de.bkroeger.editor4.Handler.MouseReleasedCommand;
import de.bkroeger.editor4.Handler.ShapeDialogCommand;
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
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * <p>Dies ist der Controller für 2-dimensionale Shapes.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
public class Shape2DController extends ShapeController implements IMouseHandlerData {

	private static final Logger logger = LogManager.getLogger(Shape2DController.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private double mouseX;
	private double mouseY;
	
	private MousePressedCommand mousePressedCommand;
	private MouseDraggedCommand mouseMovedCommand;
	private MouseReleasedCommand mouseReleasedCommand;
	private ShapeDialogCommand shapeDialogCommand;
	
	/**========================================================================
	 * Methods of IMouseHandlerData interface
	 *=======================================================================*/
	
	public double getMouseX() { return mouseX; }
	public void setMouseX(double value) { this.mouseX = value; }
	public double getMouseY() { return mouseY; }
	public void setMouseY(double value) { this.mouseY = value; }
	public void setDeltaX(double value) throws TechnicalException, CellCalculationException {
		// Daten ins Modell übernehmen
		ShapeModel shapeModel = (ShapeModel) this.getModel();
		double x = shapeModel.getLayoutX();
		shapeModel.setLayoutX(x + value);
	}
	
	public void setDeltaY(double value) throws TechnicalException, CellCalculationException {
		// Daten ins Modell übernehmen
		ShapeModel shapeModel = (ShapeModel) this.getModel();
		double y = shapeModel.getLayoutY();
		shapeModel.setLayoutY(y + value);
	}
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	/**
	 * Constructor
	 * @param model
	 */
	public Shape2DController(SectionModel model) {
		super(model);

        // create the commands
		mousePressedCommand = new MousePressedCommand(this);
		mouseMovedCommand = new MouseDraggedCommand(this);
		mouseReleasedCommand = new MouseReleasedCommand(this);
		
		shapeDialogCommand = new ShapeDialogCommand((ShapeModel)model);
	}
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/
    
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
		// die Position des Shapes ist an den angegebenen Layout-Properties
		// aber nicht mit der linken, oberen Ecke, sondern der definierte
		// Center-Point ist an diesen Koordinaten
		// z.B. setLayoutX( layoutX - centerX )
		shapeGroup.layoutXProperty().bind(
				Bindings.subtract(model.getLayoutXProperty(), model.getCenterXProperty()));
		shapeGroup.layoutYProperty().bind(
				Bindings.subtract(model.getLayoutYProperty(), model.getCenterYProperty()));
		
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
		
    	// when user clicks on PathView
    	shapeGroup.setOnMouseClicked(event -> {
    		logger.debug("Clicked on shape group");
    		if (event.getButton() == MouseButton.PRIMARY) {
    			// ???
    		} else if (event.getButton() == MouseButton.SECONDARY) {
    			// Shape-Dialog anzeigen
    			try {
					shapeDialogCommand.execute(event);
				} catch (CellCalculationException | TechnicalException e) {
					logger.error(e.getMessage(), e);
				}
    		}
    		event.consume();
    	});
    	
    	// when user presses a mouse key over the PathView call command
    	shapeGroup.setOnMousePressed(event -> {
    		logger.debug("Pressed on shape group");
    		mousePressedCommand.execute(event);
    		event.consume();
    	});
    	
    	// when user moves the PathView with key pressed call command
    	shapeGroup.setOnMouseDragged(event -> {
    		logger.debug("Drag shape group");
    		try {
				mouseMovedCommand.execute(event);
			} catch (TechnicalException | CellCalculationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		event.consume();
    	});
    	
    	// when user releases the mouse key call command
    	shapeGroup.setOnMouseReleased(event -> {
    		logger.debug("Released shape group");
    		try {
				mouseReleasedCommand.execute(event);
			} catch (TechnicalException | CellCalculationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		event.consume();
    	});
				
    	return result;
    }
}
