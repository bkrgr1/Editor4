package de.bkroeger.editor4.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.Handler.MouseDraggedCommand;
import de.bkroeger.editor4.Handler.MousePressedCommand;
import de.bkroeger.editor4.Handler.MouseReleasedCommand;
import de.bkroeger.editor4.Handler.ShapeDialogCommand;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.CellModel;
import de.bkroeger.editor4.model.ConnectorModel;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.model.PathModel;
import de.bkroeger.editor4.model.BaseModel;
import de.bkroeger.editor4.model.ModelType;
import de.bkroeger.editor4.model.ShapeModel;
import de.bkroeger.editor4.runtime.PageRuntime;
import de.bkroeger.editor4.runtime.PathElemRuntime;
import de.bkroeger.editor4.runtime.Shape1DRuntime;
import de.bkroeger.editor4.runtime.ShapeRuntime;
import de.bkroeger.editor4.view.ConnectorPointView;
import de.bkroeger.editor4.view.GroupView;
import de.bkroeger.editor4.view.PathView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseButton;

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
	
	@Resource
	private EditorModel editorModel;
	
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
		if (this.getModel() == null) return;
		ShapeModel shapeModel = (ShapeModel) this.getModel();
		double x = shapeModel.getLayoutX();
		shapeModel.setLayoutX(x + value);
	}
	
	public void setDeltaY(double value) throws TechnicalException, CellCalculationException {
		// Daten ins Modell übernehmen
		if (this.getModel() == null) return;
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
	public Shape2DController(ShapeRuntime runtime) {
		super(runtime);
		
		this.model = runtime.getModel();
			
		for (ConnectorModel m : runtime.getModel().getConnectors()) {
			
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

		// eine Shape-Group erzeugen
		GroupView shapeGroup = new GroupView((ShapeModel)model);
		shapeGroup.prefWidthProperty().bind(((ShapeModel)model).getWidthProperty());
		shapeGroup.prefHeightProperty().bind(((ShapeModel)model).getHeightProperty());
		
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
				Bindings.subtract((DoubleProperty)((ShapeModel)model).getLayoutXProperty(), 
						(DoubleProperty)((ShapeModel)model).getCenterXProperty()));
		shapeGroup.layoutYProperty().bind(
				Bindings.subtract(((ShapeModel)model).getLayoutYProperty(), 
						((ShapeModel)model).getCenterYProperty()));
		
		// alle Path-Sections ermitteln und die Pfade zeichnen
		List<BaseModel> pathSections = model.selectChildMotel(ModelType.Path);
		for (BaseModel pathSection : pathSections) {

			PathElemRuntime pathElemRuntime = new PathElemRuntime(shapeRuntime);
			pathElemRuntime.setModel((PathModel)pathSection);
			
        	// Path zeichnen
			PathModel pathModel = (PathModel) pathSection;
			PathElemController pathElemController = new PathElemController(pathModel);
			pathElemRuntime.setController(pathElemController);
			
			// fehlt hier eine PathRuntime?
//			pathModel.setRuntime(pathRuntime);
			
        	PathView pathView = pathElemController.buildView(pathElemRuntime);
        	
        	// und zur Gruppe hinzufügen
        	shapeGroup.getChildren().add((Node) pathView);
        	
        	shapeRuntime.addPathElem(pathElemRuntime);
		}
		
		// rotate
		
		// scale
    	 
        // Create ContextMenu
        contextMenu = buildShapeContextMenu();   
        
        // Connectors (unsichtbar) zeichnen
        List<ConnectorPointView> connectors = shapeGroup.buildConnectorViews(shapeRuntime.getModel().getConnectors());
        shapeGroup.getChildren().addAll(connectors);
    	
    	/**========================================================================
    	 * Event handlers for shapeGroup
    	 *=======================================================================*/
        
    	/**
    	 * wenn der Mouse-Button über einem Shape gedrückt wird...
    	 */
    	shapeGroup.setOnMousePressed(event -> {
    		logger.debug("Mouse pressed on shape group");
    		
    		if (editorModel.isToolConnector()) {
    			
    			// Line-Shape erzeugen und mit dem Mittelpunkt des Shapes verbinden
    			
    		} else {
    			mousePressedCommand.execute(event);
    		}
    		event.consume();
    	});
		
    	// when user clicks on PathView
    	shapeGroup.setOnMouseClicked(event -> {
    		
    		logger.debug("Mouse clicked on shape group");
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
    		
    		// is connector mode active?
    		if (editorModel.isToolConnector()) {
    			
    			// show the connector points
    			shapeGroup.showConnectorPoints(true);
    		}
    		event.consume();
    	});
    	
    	// when mouse exits a shape
    	shapeGroup.setOnMouseExited(event -> {
    		
    		// don't show the connector points
    		shapeGroup.showConnectorPoints(false);
    		event.consume();
    	});
    	
    	/**========================================================================
    	 * Event handlers for connectors
    	 *=======================================================================*/
    	
    	for (ConnectorPointView connector : shapeGroup.getConnectors()) {
    		
    		connector.setOnMouseEntered(event -> {
    			// highlight the connector
    			connector.setHighlight(true);
        		event.consume();
    		});
    		
    		connector.setOnMouseExited(event -> {
    			// switch off highlighting
    			connector.setHighlight(false);
        		event.consume();
    		});
    		
    		/**
    		 * wenn über einem Connector-Point der Mouse-Button gedrückt wird...
    		 */
    		connector.setOnMousePressed(event -> {
    			
    			// create a new Line-Shape wenn der Connector-Mode aktiv ist
    			if (editorModel.isToolConnector()) {
    				
    				// Line-Shape erzeugen und mit dem Connector-Point verbinden
    				Shape1DRuntime lineRuntime = new Shape1DRuntime(runtime);
    				
    				ShapeModel lineModel = 
    						ShapeModel.buildShapeFromTemplate(
    								editorModel.getLineTemplates().get(editorModel.getLineType()),
    								(PageModel)model.getParentModel());
    				lineRuntime.setModel(lineModel);
    				ConnectorPointView connectorView = (ConnectorPointView)event.getSource();
    				ConnectorModel connectorModel = connectorView.getModel();
    				CellModel cell = new CellModel(
    						"LayoutX", 
    						"${"+shapeRuntime.getModel().getNameU()+"."+
    								connectorModel.getNameU()+".ConnectorX}", lineModel);
    				lineModel.addCell(cell);
    				cell = new CellModel(
    						"LayoutY", 
    						"${"+shapeRuntime.getModel().getNameU()+"."+
    								connectorModel.getNameU()+".ConnectorY}", lineModel);
    				lineModel.addCell(cell);
    				
    				try {
						lineModel.calculate(false);
						
						// TODO: die Shape-lokalen Koordinaten des Connectors
						// müssen in Page-Koordinaten umgerechnet werden.
						// Dies sollte nicht statisch erfolgen, sondern muss
						// als Funktion eingefügt werden.						
						CellModel cell1 = lineModel.getCellByName("LayoutX");
						logger.info("LineModel.layoutX="+cell1.getDoubleValue());
						CellModel cell2 = lineModel.getCellByName("LayoutY");
						logger.info("LineModel.layoutY="+cell2.getDoubleValue());
						
						// Beispiel für custom Binding
						@SuppressWarnings("unused")
						DoubleBinding area = new DoubleBinding() {
					 
				            {
				                super.bind(cell1.getDoubleProperty(), cell2.getDoubleProperty());
				            }
				 
				            @Override
				            protected double computeValue() {
				                System.out.println("computeValue() is called.");
				                try {
				                	return cell1.getDoubleProperty().get() * cell2.getDoubleProperty().get();
				                } catch(Exception e) {
				                	return 0.0D;
				                }
				            }
				        };
						
					} catch (CellCalculationException e1) {
						logger.error(e1.getMessage(), e1);
						return;
					}
    				
    				Shape1DController lineController = new Shape1DController(lineRuntime);
    				lineRuntime.setController(lineController);
    				
    				GroupView lineView = null;
    				try {
						lineView = lineController.buildView(lineRuntime);
					} catch (TechnicalException | InputFileException | CellCalculationException e) {
						logger.error(e.getMessage(), e);
						return;
					}
    				lineRuntime.setView(lineView);
    				
    				PageRuntime pageRuntime = (PageRuntime) shapeRuntime.getParent();
    				pageRuntime.getView().getChildren().add(lineView);
    			}
        		event.consume();
    		});
        	
    		connector.setOnMouseDragged(event -> {
    			
    			// TODO: die LayoutX/Y-Werte abhängig vom Drag-Fortschritt setzen
    			
    			event.consume();
    		});
        	
    		connector.setOnMouseReleased(event -> {
    			
    			// TODO: prüfen, ob ein anderer Connector in der Nähe ist
    			
    			event.consume();
    		});
    	}
				
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
