package de.bkroeger.editor4.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.model.IConnectorModel;
import de.bkroeger.editor4.model.StraightArrowModel;
import de.bkroeger.editor4.view.IArrowView;
import de.bkroeger.editor4.view.IConnector;
import de.bkroeger.editor4.view.StraightArrowView;
import de.bkroeger.editor4.view.StraightConnectorView;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

/**
 * Dieser Controller steuert die Bewegungen eines geraden Pfeils ohne Kurven
 * oder Ecken.
 * 
 * @author bk
 */
public class StraightArrowController extends BaseArrowController {

	private static final Logger logger = LogManager.getLogger(StraightArrowController.class.getName());

	private static final int START_CONNECTOR = 0;
	@SuppressWarnings("unused")
	private static final int END_CONNECTOR = 1;

	private Double mouseX;
	private Double mouseY;

	@SuppressWarnings("unused")
	private List<IShapeController> shapes;

	private List<IConnectorController> connectorControllers = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param model
	 * @param parentController
	 * @param connectors
	 */
	public StraightArrowController(IArrowModel model, PageController parentController, List<IShapeController> shapes) {

		logger.debug("Creating arrow controller...");

		this.model = (StraightArrowModel) model;
		this.shapes = shapes;

		// den Pfeil zeichnen
		this.view = new StraightArrowView(model.x1Property(), model.y1Property(), model.x2Property(),
				model.y2Property(), model.rotateProperty(), model.colorProperty(), model.lineStartTypeProperty(),
				model.lineEndTypeProperty(), model.strokeTypeProperty(), model.strokeWidthProperty());

		// Connectoren zeichnen
		boolean isStart = true;
		for (IConnectorModel connectorModel : model.getConnectorModels()) {

			// einen Controller für den Connector-Punkt erzeugen
			IConnectorController connCtrl = new StraightConnectorController(connectorModel, this, isStart);
			connectorControllers.add(connCtrl);
			isStart = false;
			StraightConnectorView connView = (StraightConnectorView) connCtrl.getView();
			((StraightArrowView) this.view).getChildren().add(connView);
		}

		// EventHandler zuordnen
		((Node) this.view).setOnMouseEntered(new MouseEnteredEventHandler(this.view));
		((Node) this.view).setOnMouseExited(new MouseExitedEventHandler(this.view));
		((Node) this.view).setOnMousePressed(new MousePressedEventHandler(this.view));
		((Node) this.view).setOnMouseDragged(new MouseDraggedEventHandler(this.view, shapes));
		((Node) this.view).setOnMouseReleased(new MouseReleasedEventHandler(this.view, shapes));

		logger.debug("Arrow controller created for straight arrow");
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird ausgeführt, wenn die Mouse über dem Arrow ist. In
	 * diesem Fall, wird der Cursor in einen Hand-Cursor geändert.
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
			arrowView.setSelected(true);

			// alle Konnektoren anzeigen
			for (IConnectorController connectorController : connectorControllers) {
				connectorController.setSelected(true);
			}

			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird ausgeführt, wenn die Mouse den Arrow verlässt. In
	 * diesem Fall wird der Default-Cursor wieder angezeigt.
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
			arrowView.setSelected(false);

			// alle Konnektoren anzeigen
			for (IConnectorController connectorController : connectorControllers) {
				connectorController.setSelected(false);
			}

			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse über dem Pfeil gedrückt
	 * wird. Die aktuelle Position der Mouse wird gespeichert.
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
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse über dem Pfeil wieder
	 * freigegeben wird. Wenn dies über einem Connector geschiet, wird der Pfeil an
	 * den Connector gebunden.
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

				model.x1Property().set(model.x1Property().get() + deltaX);
				model.y1Property().set(model.y1Property().get() + deltaY);
				model.x2Property().set(model.x2Property().get() + deltaX);
				model.y2Property().set(model.y2Property().get() + deltaY);
				// arrow.setStroke(Color.BLUE);

				// ist eine Pfeilspitze in der Nähe eines Connectors von einem der Shapes?
				for (int shapeConnectorIndex = 0; shapeConnectorIndex < shapes.size(); shapeConnectorIndex++) {
					IShapeController shape = shapes.get(shapeConnectorIndex);
					for (int arrowConnectorIndex = 0; arrowConnectorIndex < shape.getConnectors()
							.size(); arrowConnectorIndex++) {
						IConnector shapeConnector = shape.getConnectors().get(arrowConnectorIndex);
						Node shapeConnectorNode = (Node) shapeConnector;
						for (IConnector arrowConnector : arrow.getConnectors()) {
							Node arrowConnectorNode = (Node) arrowConnector;
							if (intersectsWithConnectorPoint(shapeConnectorNode, arrowConnectorNode)) {
								// ja,
								logger.info("Arrow intersects with connector!");
								// connector.setStroke(Color.RED);

								// Connector-Mittelpunkt in Scene-Koordinaten umrechnen
								Point2D localConnectorPoint = new Point2D(
										((Circle) shapeConnector).centerXProperty().get(),
										((Circle) shapeConnector).centerYProperty().get());
								Point2D sceneConnectorPoint = shapeConnectorNode.localToScene(localConnectorPoint);

								// Koordinaten in lokale Arrow-Koordinaten umrechnen
								Point2D arrowConnectorPoint = ((Node) arrow).sceneToLocal(sceneConnectorPoint);

								if (arrowConnectorIndex == START_CONNECTOR) {
									// Anfangspunkt verschieben
									model.x1Property().set(model.x1Property().get() + arrowConnectorPoint.getX());
									model.y1Property().set(model.y1Property().get() + arrowConnectorPoint.getY());
								} else {
									// Endpunkt verschieben
									model.x2Property().set(model.x2Property().get() + arrowConnectorPoint.getX());
									model.y2Property().set(model.y2Property().get() + arrowConnectorPoint.getY());
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
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse mit dem Pfeil verschoben
	 * wird. Dazu wird die Differenz zwischen der aktuellen Mouse-Position und der
	 * gespeicherten Mouse-Position berechnet und der Pfeil entsprechend verschoben.
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
			model.x1Property().set(model.x1Property().get() + deltaX);
			model.y1Property().set(model.y1Property().get() + deltaY);
			model.x2Property().set(model.x2Property().get() + deltaX);
			model.y2Property().set(model.y2Property().get() + deltaY);

			// ist eine Pfeilspitze in der Nähe eines Connectors von einem der Shapes?
			for (int j = 0; j < shapes.size(); j++) {
				IShapeController shape = shapes.get(j);
				for (int i = 0; i < shape.getConnectors().size(); i++) {
					IConnector shapeConnector = shape.getConnectors().get(i);
					Node shapeConnectorNode = (Node) shapeConnector;
					for (IConnector arrowConnector : arrow.getConnectors()) {
						Node arrowConnectorNode = (Node) arrowConnector;
						if (intersectsWithConnectorPoint(shapeConnectorNode, arrowConnectorNode)) {
							logger.info("Arrow intersects with shape " + j + " connector " + i);
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

		Bounds connectorBounds = shapeConnectorNode
				.sceneToLocal(arrowConnectorNode.localToScene(arrowConnectorNode.getBoundsInLocal()));
		Bounds nodeBounds = shapeConnectorNode.getBoundsInLocal();
		logger.debug("Connector bounds = " + connectorBounds.toString() + " Node bounds = " + nodeBounds);
		if (shapeConnectorNode.intersects(connectorBounds)) {
			return true;
		}
		return false;
	}

	// ===============================================================
}
