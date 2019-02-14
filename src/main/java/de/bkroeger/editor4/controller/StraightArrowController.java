package de.bkroeger.editor4.controller;

import java.util.List;
import java.util.logging.Logger;

import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.model.StraightArrowModel;
import de.bkroeger.editor4.view.IArrowView;
import de.bkroeger.editor4.view.IConnector;
import de.bkroeger.editor4.view.StraightArrowView;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

/**
 * Dieser Controller steuert die Bewegungen eines geraden Pfeils ohne Kurven oder Ecken.
 * 
 * @author bk
 */
public class StraightArrowController implements IArrowController {

	private static final Logger logger = Logger.getLogger(StraightArrowController.class.getName());
	
	private static final int START_CONNECTOR = 0;
	@SuppressWarnings("unused")
	private static final int END_CONNECTOR = 1;

	private Double mouseX;
	private Double mouseY;
	
	private StraightArrowModel arrowModel;
	private IArrowView arrowView;
	
	@SuppressWarnings("unused")
	private List<IShapeController> shapes;

	@Override
	public Node getView() { return (Node) arrowView; }
	
	/**
	 * Constructor
	 * @param model
	 * @param parentController
	 * @param connectors
	 */
	public StraightArrowController(IArrowModel model, EditorController parentController, List<IShapeController> shapes) {
		
		this.arrowModel = (StraightArrowModel) model;
		this.shapes = shapes;
		
		// den Pfeil zeichnen
		arrowView = new StraightArrowView(arrowModel.x1Property(), arrowModel.y1Property(),
				arrowModel.x2Property(), arrowModel.y2Property(),
				arrowModel.rotateProperty(),
				arrowModel.colorProperty(),
				arrowModel.lineStartTypeProperty(),
				arrowModel.lineEndTypeProperty(),
				arrowModel.strokeTypeProperty(),
				arrowModel.strokeWidthProperty());
		
		// EventHandler zuordnen
		((Node)arrowView).setOnMouseEntered(new MouseEnteredEventHandler(arrowView));
		((Node)arrowView).setOnMouseExited(new MouseExitedEventHandler(arrowView));
		((Node)arrowView).setOnMousePressed(new MousePressedEventHandler(arrowView));
		((Node)arrowView).setOnMouseDragged(new MouseDraggedEventHandler(arrowView, shapes));
		((Node)arrowView).setOnMouseReleased(new MouseReleasedEventHandler(arrowView, shapes));
		
		// Eventhandler für Connectoren
		for (IConnector connector : arrowView.getConnectors()) {
			
			((Node)connector).setOnMouseEntered(new ConnectorEnteredEventHandler(connector));
			((Node)connector).setOnMouseExited(new ConnectorExitedEventHandler(connector));
			((Node)connector).setOnMousePressed(new ConnectorPressedEventHandler(connector));
			((Node)connector).setOnMouseDragged(new ConnectorDraggedEventHandler(connector, arrowView));
			((Node)connector).setOnMouseReleased(new ConnectorReleasedEventHandler(connector, arrowView));
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird ausgeführt, wenn die Mouse über dem Arrow ist.
	 * In diesem Fall, wird der Cursor in einen Hand-Cursor geändert.
	 *
	 * @author bk
	 */
	class MouseEnteredEventHandler implements EventHandler<MouseEvent> {
		
		private IArrowView arrowView;

		public MouseEnteredEventHandler(IArrowView arrowView) {
			this.arrowView = arrowView;
		}

		
		@Override
		public void handle(MouseEvent event) {			
			((Node)arrowView).getScene().setCursor(Cursor.HAND);
			arrowView.setSelected(true);
			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird ausgeführt, wenn die Mouse den Arrow verlässt.
	 * In diesem Fall wird der Default-Cursor wieder angezeigt.
	 *
	 * @author bk
	 */
	class MouseExitedEventHandler implements EventHandler<MouseEvent> {
		
		private IArrowView arrowView;

		public MouseExitedEventHandler(IArrowView arrowView) {
			this.arrowView = arrowView;
		}

		@Override
		public void handle(MouseEvent event) {
			((Node)arrowView).getScene().setCursor(Cursor.DEFAULT);
			arrowView.setSelected(false);
			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse über dem Pfeil gedrückt wird.
	 * Die aktuelle Position der Mouse wird gespeichert.
	 *
	 * @author bk
	 */
	class MousePressedEventHandler implements EventHandler<MouseEvent> {
		
		@SuppressWarnings("unused")
		private IArrowView arrowView;

		public MousePressedEventHandler(IArrowView arrowView) {
			this.arrowView = arrowView;
		}

		@Override
		public void handle(MouseEvent event) {
			mouseX = event.getSceneX();
			mouseY = event.getSceneY();
			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse über dem Pfeil wieder freigegeben wird.
	 * Wenn dies über einem Connector geschiet, wird der Pfeil an den Connector gebunden.
	 *
	 * @author bk
	 */
	class MouseReleasedEventHandler implements EventHandler<MouseEvent> {

		private IArrowView arrow;
		private List<IShapeController> shapes;

		public MouseReleasedEventHandler(IArrowView arrowView, List<IShapeController> shapes) {
			this.arrow = arrowView;
			this.shapes = shapes;
		}

		@Override
		public void handle(MouseEvent event) {

			if (mouseX != null && mouseY != null) {

				double deltaX = event.getSceneX() - mouseX;
				double deltaY = event.getSceneY() - mouseY;
				
				mouseX += deltaX;
				mouseY += deltaY;

				arrowModel.x1Property().set(arrowModel.x1Property().get() + deltaX);
				arrowModel.y1Property().set(arrowModel.y1Property().get() + deltaY);
				arrowModel.x2Property().set(arrowModel.x2Property().get() + deltaX);
				arrowModel.y2Property().set(arrowModel.y2Property().get() + deltaY);
				//arrow.setStroke(Color.BLUE);

				// ist eine Pfeilspitze in der Nähe eines Connectors von einem der Shapes?
				for (int shapeConnectorIndex=0; shapeConnectorIndex<shapes.size(); shapeConnectorIndex++) {
					IShapeController shape = shapes.get(shapeConnectorIndex);
					for (int arrowConnectorIndex=0; arrowConnectorIndex<shape.getConnectors().size(); arrowConnectorIndex++) {
						IConnector shapeConnector = shape.getConnectors().get(arrowConnectorIndex);
						Node shapeConnectorNode = (Node) shapeConnector;
						for (IConnector arrowConnector : arrow.getConnectors()) {
							Node arrowConnectorNode = (Node) arrowConnector;
							if (intersectsWithConnectorPoint(shapeConnectorNode, arrowConnectorNode)) {
								// ja,
								logger.info("Arrow intersects with connector!");
		//						connector.setStroke(Color.RED);
			
								// Connector-Mittelpunkt in Scene-Koordinaten umrechnen
								Point2D localConnectorPoint = new Point2D(
										((Circle)shapeConnector).centerXProperty().get(), 
										((Circle)shapeConnector).centerYProperty().get());
								Point2D sceneConnectorPoint = shapeConnectorNode.localToScene(localConnectorPoint);
								
								// Koordinaten in lokale Arrow-Koordinaten umrechnen
								Point2D arrowConnectorPoint = ((Node)arrowView).sceneToLocal(sceneConnectorPoint);
								
								if (arrowConnectorIndex == START_CONNECTOR) {
									// Anfangspunkt verschieben
									arrowModel.x1Property().set(arrowModel.x1Property().get() + arrowConnectorPoint.getX());
									arrowModel.y1Property().set(arrowModel.y1Property().get() + arrowConnectorPoint.getY());
								} else {
									// Endpunkt verschieben
									arrowModel.x2Property().set(arrowModel.x2Property().get() + arrowConnectorPoint.getX());
									arrowModel.y2Property().set(arrowModel.y2Property().get() + arrowConnectorPoint.getY());
								}
							}
						}
					}
				}

				event.consume();
			}
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse mit dem Pfeil verschoben wird.
	 * Dazu wird die Differenz zwischen der aktuellen Mouse-Position und der gespeicherten
	 * Mouse-Position berechnet und der Pfeil entsprechend verschoben.
	 * Die neue Position wird dann wieder gespeichert.
	 *
	 * @author bk
	 */
	class MouseDraggedEventHandler implements EventHandler<MouseEvent> {

		private IArrowView arrow;
		private List<IShapeController> shapes;

		public MouseDraggedEventHandler(IArrowView arrowView, List<IShapeController> shapes) {
			this.arrow = arrowView;
			this.shapes = shapes;
		}

		@Override
		public void handle(MouseEvent event) {
			
			double deltaX = event.getSceneX() - mouseX;
			double deltaY = event.getSceneY() - mouseY;
			
			mouseX += deltaX;
			mouseY += deltaY;
			
			// Pfeil verschieben
			arrowModel.x1Property().set(arrowModel.x1Property().get() + deltaX);
			arrowModel.y1Property().set(arrowModel.y1Property().get() + deltaY);
			arrowModel.x2Property().set(arrowModel.x2Property().get() + deltaX);
			arrowModel.y2Property().set(arrowModel.y2Property().get() + deltaY);

			// ist eine Pfeilspitze in der Nähe eines Connectors von einem der Shapes?
			for (int j=0; j<shapes.size(); j++) {
				IShapeController shape = shapes.get(j);
				for (int i=0; i<shape.getConnectors().size(); i++) {
					IConnector shapeConnector = shape.getConnectors().get(i);
					Node shapeConnectorNode = (Node) shapeConnector;
					for (IConnector arrowConnector : arrow.getConnectors()) {
						Node arrowConnectorNode = (Node) arrowConnector;
						if (intersectsWithConnectorPoint(shapeConnectorNode, arrowConnectorNode)) {
							logger.info("Arrow intersects with shape "+j+" connector "+i);
						}
					}
				}
			}
			event.consume();
		}
	}

	/**
	 * Prüft, ob ein Pfeil mit dem Connector überschneidet.
	 *
	 * @param shapeConnectorNode
	 * @param arrowConnectorNode
	 * @return
	 */
	private boolean intersectsWithConnectorPoint(Node shapeConnectorNode, Node arrowConnectorNode) {

		Bounds connectorBounds = shapeConnectorNode.sceneToLocal(arrowConnectorNode.localToScene(arrowConnectorNode.getBoundsInLocal()));
		Bounds nodeBounds = shapeConnectorNode.getBoundsInLocal();
		logger.fine("Connector bounds = " + connectorBounds.toString() + " Node bounds = " + nodeBounds);
		if (shapeConnectorNode.intersects(connectorBounds)) {
			return true;
		}
		return false;
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird ausgeführt, wenn die Mouse über dem Arrow ist.
	 * In diesem Fall, wird der Cursor in einen Hand-Cursor geändert.
	 *
	 * @author bk
	 */
	class ConnectorEnteredEventHandler implements EventHandler<MouseEvent> {
		
		private IConnector arrowView;

		public ConnectorEnteredEventHandler(IConnector arrowView) {
			this.arrowView = arrowView;
		}

		
		@Override
		public void handle(MouseEvent event) {			
			((Node)arrowView).getScene().setCursor(Cursor.HAND);
			arrowView.setSelected(true);
			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird ausgeführt, wenn die Mouse den Arrow verlässt.
	 * In diesem Fall wird der Default-Cursor wieder angezeigt.
	 *
	 * @author bk
	 */
	class ConnectorExitedEventHandler implements EventHandler<MouseEvent> {
		
		private IConnector arrowView;

		public ConnectorExitedEventHandler(IConnector arrowView) {
			this.arrowView = arrowView;
		}

		@Override
		public void handle(MouseEvent event) {
			((Node)arrowView).getScene().setCursor(Cursor.DEFAULT);
			arrowView.setSelected(false);
			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse über dem Pfeil gedrückt wird.
	 * Die aktuelle Position der Mouse wird gespeichert.
	 *
	 * @author bk
	 */
	class ConnectorPressedEventHandler implements EventHandler<MouseEvent> {
		
		@SuppressWarnings("unused")
		private IConnector arrowView;

		public ConnectorPressedEventHandler(IConnector arrowView) {
			this.arrowView = arrowView;
		}

		@Override
		public void handle(MouseEvent event) {
			mouseX = event.getSceneX();
			mouseY = event.getSceneY();
			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse über dem Pfeil wieder freigegeben wird.
	 * Wenn dies über einem Connector geschiet, wird der Pfeil an den Connector gebunden.
	 *
	 * @author bk
	 */
	class ConnectorReleasedEventHandler implements EventHandler<MouseEvent> {

		private IConnector connector;
		private IArrowView arrow;

		public ConnectorReleasedEventHandler(IConnector connector, IArrowView arrow) {
			this.connector = connector;
			this.arrow = arrow;
		}

		@Override
		public void handle(MouseEvent event) {

			if (mouseX != null && mouseY != null) {

				double deltaX = event.getSceneX() - mouseX;
				double deltaY = event.getSceneY() - mouseY;
				
				mouseX += deltaX;
				mouseY += deltaY;

				arrowModel.x1Property().set(arrowModel.x1Property().get() + deltaX);
				arrowModel.y1Property().set(arrowModel.y1Property().get() + deltaY);
				arrowModel.x2Property().set(arrowModel.x2Property().get() + deltaX);
				arrowModel.y2Property().set(arrowModel.y2Property().get() + deltaY);
				//arrow.setStroke(Color.BLUE);

				event.consume();
			}
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse mit dem Pfeil verschoben wird.
	 * Dazu wird die Differenz zwischen der aktuellen Mouse-Position und der gespeicherten
	 * Mouse-Position berechnet und der Pfeil entsprechend verschoben.
	 * Die neue Position wird dann wieder gespeichert.
	 *
	 * @author bk
	 */
	class ConnectorDraggedEventHandler implements EventHandler<MouseEvent> {

		private IConnector connector;
		private IArrowView arrow;

		public ConnectorDraggedEventHandler(IConnector connector, IArrowView arrow) {
			this.connector = connector;
			this.arrow = arrow;
		}

		@Override
		public void handle(MouseEvent event) {
			
			double deltaX = event.getSceneX() - mouseX;
			double deltaY = event.getSceneY() - mouseY;
			
			mouseX += deltaX;
			mouseY += deltaY;
			
			// Pfeil verschieben
			arrowModel.x1Property().set(arrowModel.x1Property().get() + deltaX);
			arrowModel.y1Property().set(arrowModel.y1Property().get() + deltaY);
			arrowModel.x2Property().set(arrowModel.x2Property().get() + deltaX);
			arrowModel.y2Property().set(arrowModel.y2Property().get() + deltaY);
			event.consume();
		}
	}
}
