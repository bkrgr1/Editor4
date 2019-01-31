package de.bkroeger.editor4.controller;

import java.util.List;
import java.util.logging.Logger;

import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.model.StraightArrowModel;
import de.bkroeger.editor4.view.StraightArrowView;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

/**
 * Dieser Controller steuert die Bewegungen des Pfeils.
 * 
 * @author bk
 */
public class StraightArrowController implements IArrowController {

	private static final Logger logger = Logger.getLogger(StraightArrowController.class.getName());

	private Double mouseX;
	private Double mouseY;
	
	private StraightArrowModel arrowModel;
	private StraightArrowView arrowView;

	@Override
	public Node getView() { return arrowView; }

	
	public StraightArrowController(IArrowModel model, EditorController parentController, List<Node> connectors) {
		
		arrowModel = (StraightArrowModel) model;
		arrowView = new StraightArrowView(arrowModel.getX1Property(), arrowModel.getY1Property(),
				arrowModel.getX2Property(), arrowModel.getY2Property());
		
		arrowView.setOnMousePressed(new MousePressedEventHandler(arrowView));
		arrowView.setOnMouseDragged(new MouseDraggedEventHandler(arrowView, connectors));
		arrowView.setOnMouseReleased(new MouseReleasedEventHandler(arrowView, connectors));
	}

	// ===============================================================

	/**
	 * Diese Klasse behandelt das Drücker der linken Maustaste über dem Kreuz.
	 *
	 * @author bk
	 */
	class MousePressedEventHandler implements EventHandler<MouseEvent> {
		
		@SuppressWarnings("unused")
		private Path path;

		public MousePressedEventHandler(Path path) {
			this.path = path;
		}

		@Override
		public void handle(MouseEvent event) {
			mouseX = event.getSceneX();
			mouseY = event.getSceneY();
			Point2D pointScene = new Point2D(mouseX, mouseY);
			logger.info("Mouse pressed at: " + pointScene.toString());
			event.consume();
		}
	}

	// ===============================================================

	class MouseReleasedEventHandler implements EventHandler<MouseEvent> {

		private Path arrow;
		private List<Node> connectors;

		public MouseReleasedEventHandler(Path path, List<Node> connectors) {
			this.arrow = path;
			this.connectors = connectors;
		}

		@Override
		public void handle(MouseEvent event) {

			if (mouseX != null && mouseY != null) {

				double deltaX = event.getSceneX() - mouseX;
				double deltaY = event.getSceneY() - mouseY;
				mouseX += deltaX;
				mouseY += deltaY;
				Point2D pointScene = new Point2D(mouseX, mouseY);
				logger.info("Mouse released at: " + pointScene.toString());

				arrowModel.getX1Property().set(arrowModel.getX1Property().get() + deltaX);
				arrowModel.getY1Property().set(arrowModel.getY1Property().get() + deltaY);
				arrowModel.getX2Property().set(arrowModel.getX2Property().get() + deltaX);
				arrowModel.getY2Property().set(arrowModel.getY2Property().get() + deltaY);
				arrow.setStroke(Color.BLUE);

				// ist die Pfeilspitze in der Nähe eines Connectors?
				// TODO: Woher weiss ich, ob der Anfang oder das Ende des Arrows in der Nähe ist?
				for (Node connector : connectors) {
					if (intersectsWithConnectorPoint(connector, arrow)) {
						// ja,
						logger.info("Arrow intersects with connector!");
//						connector.setStroke(Color.RED);
	
						// Connector-Mittelpunkt in Scene-Koordinaten umrechnen
						Point2D localConnectorPoint = new Point2D(connector.getLayoutX(), connector.getLayoutY());
						Point2D sceneConnectorPoint = connector.localToScene(localConnectorPoint);
						
						// Koordinaten in lokale Arrow-Koordinaten umrechnen
						Point2D arrowConnectorPoint = arrow.sceneToLocal(sceneConnectorPoint);
						
						// Delta zwischen dem ConnectorPoint und dem Arrow-Anfang berechnen
						Point2D deltaStart = new Point2D(arrowModel.getX1Property().get() - arrowConnectorPoint.getX(),
								arrowModel.getY1Property().get() - arrowConnectorPoint.getY());
						
						// Anfangspunkt verschieben
						arrowModel.getX1Property().set(arrowConnectorPoint.getX());
						arrowModel.getY1Property().set(arrowConnectorPoint.getY());
						
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

	class MouseDraggedEventHandler implements EventHandler<MouseEvent> {

		private Path arrow;
		private List<Node> connectors;

		public MouseDraggedEventHandler(Path path, List<Node> connectors) {
			this.arrow = path;
			this.connectors = connectors;
		}

		@Override
		public void handle(MouseEvent event) {
			double deltaX = event.getSceneX() - mouseX;
			double deltaY = event.getSceneY() - mouseY;
			mouseX += deltaX;
			mouseY += deltaY;
			Point2D pointScene = new Point2D(mouseX, mouseY);
			logger.info("Mouse moved to: " + pointScene.toString());
			arrowModel.getX1Property().set(arrowModel.getX1Property().get() + deltaX);
			arrowModel.getY1Property().set(arrowModel.getY1Property().get() + deltaY);
			arrowModel.getX2Property().set(arrowModel.getX2Property().get() + deltaX);
			arrowModel.getY2Property().set(arrowModel.getY2Property().get() + deltaY);

			// ist die Pfeilspitze in der Nähe des Rechtecks?
			for (Node connector : connectors) {
				if (intersectsWithConnectorPoint(connector, arrow)) {
					logger.info("Arrow intersects with connector!");
				}
			}
			event.consume();
		}
	}

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
