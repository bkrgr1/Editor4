package de.bkroeger.editor4.controller;

import java.util.List;
import java.util.logging.Logger;

import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.model.StraightArrowModel;
import de.bkroeger.editor4.view.IArrowView;
import de.bkroeger.editor4.view.StraightArrowView;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Dieser Controller steuert die Bewegungen eines geraden Pfeils ohne Kurven oder Ecken.
 * 
 * @author bk
 */
public class StraightArrowController implements IArrowController {

	private static final Logger logger = Logger.getLogger(StraightArrowController.class.getName());

	private Double mouseX;
	private Double mouseY;
	
	private StraightArrowModel arrowModel;
	private IArrowView arrowView;

	@Override
	public Node getView() { return (Node) arrowView; }
	
	/**
	 * Constructor
	 * @param model
	 * @param parentController
	 * @param connectors
	 */
	public StraightArrowController(IArrowModel model, EditorController parentController, List<Node> connectors) {
		
		arrowModel = (StraightArrowModel) model;
		
		// den Pfeil zeichnen
		arrowView = new StraightArrowView(arrowModel.getX1Property(), arrowModel.getY1Property(),
				arrowModel.getX2Property(), arrowModel.getY2Property(),
				arrowModel.getRotateProperty());
		
		// EventHandler zuordnen
		((Node)arrowView).setOnMouseEntered(new MouseEnteredEventHandler(arrowView));
		((Node)arrowView).setOnMouseExited(new MouseExitedEventHandler(arrowView));
		((Node)arrowView).setOnMousePressed(new MousePressedEventHandler(arrowView));
		((Node)arrowView).setOnMouseDragged(new MouseDraggedEventHandler(arrowView, connectors));
		((Node)arrowView).setOnMouseReleased(new MouseReleasedEventHandler(arrowView, connectors));
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
		private List<Node> connectors;

		public MouseReleasedEventHandler(IArrowView arrowView, List<Node> connectors) {
			this.arrow = arrowView;
			this.connectors = connectors;
		}

		@Override
		public void handle(MouseEvent event) {

			if (mouseX != null && mouseY != null) {

				double deltaX = event.getSceneX() - mouseX;
				double deltaY = event.getSceneY() - mouseY;
				
				mouseX += deltaX;
				mouseY += deltaY;

				arrowModel.getX1Property().set(arrowModel.getX1Property().get() + deltaX);
				arrowModel.getY1Property().set(arrowModel.getY1Property().get() + deltaY);
				arrowModel.getX2Property().set(arrowModel.getX2Property().get() + deltaX);
				arrowModel.getY2Property().set(arrowModel.getY2Property().get() + deltaY);
				//arrow.setStroke(Color.BLUE);

				// ist die Pfeilspitze in der Nähe eines Connectors?
				// TODO: Woher weiss ich, ob der Anfang oder das Ende des Arrows in der Nähe ist?
				for (Node shapeConnector : connectors) {
					if (intersectsWithConnectorPoint(shapeConnector, (Node)arrow)) {
						// ja,
						logger.info("Arrow intersects with connector!");
//						connector.setStroke(Color.RED);
	
						// Connector-Mittelpunkt in Scene-Koordinaten umrechnen
						Point2D localConnectorPoint = new Point2D(shapeConnector.getLayoutX(), shapeConnector.getLayoutY());
						Point2D sceneConnectorPoint = shapeConnector.localToScene(localConnectorPoint);
						
						// Koordinaten in lokale Arrow-Koordinaten umrechnen
						Point2D arrowConnectorPoint = ((Node)arrow).sceneToLocal(sceneConnectorPoint);
						
						// Delta zwischen dem ConnectorPoint und dem Arrow-Anfang berechnen
						Point2D deltaStart = new Point2D(arrowConnectorPoint.getX(),
								arrowConnectorPoint.getY());
						
						// Anfangspunkt verschieben
						arrowModel.getX1Property().set(arrowModel.getX1Property().get() + deltaStart.getX());
						arrowModel.getY1Property().set(arrowModel.getY1Property().get() + deltaStart.getY());
						
						// Endpunkt verschieben
						arrowModel.getX2Property().set(arrowModel.getX2Property().get() + deltaStart.getX());
						arrowModel.getY2Property().set(arrowModel.getY2Property().get() + deltaStart.getY());
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
		private List<Node> connectors;

		public MouseDraggedEventHandler(IArrowView arrowView, List<Node> connectors) {
			this.arrow = arrowView;
			this.connectors = connectors;
		}

		@Override
		public void handle(MouseEvent event) {
			
			double deltaX = event.getSceneX() - mouseX;
			double deltaY = event.getSceneY() - mouseY;
			
			mouseX += deltaX;
			mouseY += deltaY;
			
			// Pfeil verschieben
			arrowModel.getX1Property().set(arrowModel.getX1Property().get() + deltaX);
			arrowModel.getY1Property().set(arrowModel.getY1Property().get() + deltaY);
			arrowModel.getX2Property().set(arrowModel.getX2Property().get() + deltaX);
			arrowModel.getY2Property().set(arrowModel.getY2Property().get() + deltaY);

			// ist die Pfeilspitze in der Nähe des Rechtecks?
			for (int i=0; i<connectors.size(); i++) {
				Node connector = connectors.get(i);
				if (intersectsWithConnectorPoint(connector, (Node)arrow)) {
					logger.info("Arrow intersects with connector "+i);
				}
			}
			event.consume();
		}
	}

	/**
	 * Prüft, ob ein Pfeil mit dem Connector überschneidet.
	 * @param node
	 * @param connector
	 * @return
	 */
	private boolean intersectsWithConnectorPoint(Node node, Node connector) {

		Bounds connectorBounds = node.sceneToLocal(connector.localToScene(connector.getBoundsInLocal()));
		Bounds nodeBounds = node.getBoundsInLocal();
		logger.fine("Connector bounds = " + connectorBounds.toString() + " Node bounds = " + nodeBounds);
		if (node.intersects(connectorBounds)) {
			return true;
		}
		return false;
	}
}
