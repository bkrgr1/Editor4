package de.bkroeger.editor4.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.Handler.MouseDraggedCommand;
import de.bkroeger.editor4.Handler.MousePressedCommand;
import de.bkroeger.editor4.Handler.MouseReleasedCommand;
import de.bkroeger.editor4.Handler.ShapeDialogCommand;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.ConnectorModel;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.PathModel;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.SectionModelType;
import de.bkroeger.editor4.model.ShapeModel;
import de.bkroeger.editor4.runtime.PathElemRuntime;
import de.bkroeger.editor4.runtime.ShapeRuntime;
import de.bkroeger.editor4.view.GroupView;
import de.bkroeger.editor4.view.PathView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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
	private ContextMenu contextMenu;
	
	private Map<ConnectorController, ConnectorModel> connectors = new HashMap<>();
	
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
	public Shape2DController(ShapeModel model) {
		super(model);
			
			for (ConnectorModel m : model.getConnectors()) {
				
				// a connector is displayed as a cross
				ConnectorController c = new ConnectorController(m);
				connectors.put(c, m);
			}

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
    public GroupView buildView(ShapeRuntime shapeRuntime) 
    		throws TechnicalException, InputFileException, CellCalculationException {

		ShapeModel model = (ShapeModel) this.getModel();

		// eine Shape-Group erzeugen
		GroupView shapeGroup = new GroupView(model);
		shapeGroup.prefWidthProperty().bind(model.getWidthProperty());
		shapeGroup.prefHeightProperty().bind(model.getHeightProperty());
		
		// TEST TEST TEST
		shapeGroup.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		for (Entry<ConnectorController, ConnectorModel> entry : connectors.entrySet()) {
			ConnectorController ctl = entry.getKey();
			ctl.buildView();
		}

		shapeRuntime.setView(shapeGroup);
		
		// TODO: wie berechnet sich die Grösse des Pane?
		// muss das vor dem Hinzufügen der Pathes erfolgen?
		
		// die Location der Shape-Group festlegen		
		// die Position des Shapes ist an den angegebenen Layout-Properties
		// aber nicht mit der linken, oberen Ecke, sondern der definierte
		// Center-Point ist an diesen Koordinaten
		// z.B. setLayoutX( layoutX - centerX )
		shapeGroup.layoutXProperty().bind(
				Bindings.subtract((DoubleProperty)model.getLayoutXProperty(), (DoubleProperty)model.getCenterXProperty()));
		shapeGroup.layoutYProperty().bind(
				Bindings.subtract(model.getLayoutYProperty(), model.getCenterYProperty()));
		
		// alle Path-Sections ermitteln und die Pfade zeichnen
		List<SectionModel> pathSections = model.selectSections(SectionModelType.Path);
		for (SectionModel pathSection : pathSections) {

			PathElemRuntime pathElemRuntime = new PathElemRuntime(shapeRuntime);
			pathElemRuntime.setModel((PathModel)pathSection);
			
        	// Path zeichnen
			PathModel pathModel = (PathModel) pathSection;
			PathElemController pathElemController = new PathElemController(pathModel);
			pathElemRuntime.setController(pathElemController);
			
        	PathView pathView = pathElemController.buildView(pathElemRuntime);
        	
        	// und zur Gruppe hinzufügen
        	shapeGroup.getChildren().add((Node) pathView);
        	
        	shapeRuntime.addPathElem(pathElemRuntime);
		}
		
		// rotate
		
		// scale
    	 
        // Create ContextMenu
        contextMenu = buildShapeContextMenu();    
    	
    	/**========================================================================
    	 * Event handlers
    	 *=======================================================================*/
        
    	// when user presses a mouse key over the PathView call command
    	shapeGroup.setOnMousePressed(event -> {
    		logger.debug("Pressed on shape group");
    		mousePressedCommand.execute(event);
    		event.consume();
    	});
		
    	// when user clicks on PathView
    	shapeGroup.setOnMouseClicked(event -> {
    		
    		logger.debug("Clicked on shape group");
    		if (event.getButton() == MouseButton.PRIMARY) {
    			// ???
    		} else if (event.getButton() == MouseButton.SECONDARY) {
    			// Context-Menü anzeigen
    			contextMenu.show(shapeGroup, event.getScreenX(), event.getScreenY());
    		}
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
    	
    	// when mouse enters a shape
    	// and the ToolMode = Connector
    	shapeGroup.setOnMouseEntered(event -> {
    		
    		EditorModel editorModel = EditorModel.getEditorModel();
    		// is connector mode active?
    		if (editorModel.isToolConnector()) {
    			
    			// show the connector points
    			shapeGroup.showConnectorPoints(true);
    		}
    	});
    	
    	// when mouse exits a shape
    	shapeGroup.setOnMouseExited(event -> {
    		
    		// don't show the connector points
    		shapeGroup.showConnectorPoints(false);
    	});
				
    	return shapeGroup;
    }
	
	/**
	 * <p>generiert ein Context-Menu für ein Shape.</p>
	 * @return das {@link ContextMenu}
	 */
	private ContextMenu buildShapeContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
 
		// ein Menuitem für die Copy-Funktion
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
//                label.setText("Select Menu Item 1");
            }
        });
        
        // ein Menuitem für die Cut-Funktion
        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
//                label.setText("Select Menu Item 1");
            }
        });
        
        // ein Menuitem für die Paste-Funktion
        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setDisable(true);
        pasteItem.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
//                label.setText("Select Menu Item 1");
            }
        });
        
        // eine Trennlinie
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        
        // Menuitem für die Anzeige des Shape-Sheet
        MenuItem shapeSheetItem = new MenuItem("Show Shape Sheet");
        shapeSheetItem.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
    			// Shape-Sheet-Dialog anzeigen
    			try {
					shapeDialogCommand.execute(event);
				} catch (CellCalculationException | TechnicalException e) {
					logger.error(e.getMessage(), e);
				}
            }
        });
 
        // Add MenuItems to ContextMenu
        contextMenu.getItems().addAll(copyItem, cutItem, pasteItem, separatorMenuItem, shapeSheetItem);
        return contextMenu;
	}
}
