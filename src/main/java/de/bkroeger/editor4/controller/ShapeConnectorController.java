package de.bkroeger.editor4.controller;

import java.util.logging.Logger;

import de.bkroeger.editor4.model.IConnectorModel;
import de.bkroeger.editor4.view.IConnector;
import de.bkroeger.editor4.view.IShapeView;
import de.bkroeger.editor4.view.StraightArrowView;
import de.bkroeger.editor4.view.StraightConnectorView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Dieser Controller steuert die Bewegungen eines geraden Pfeils ohne Kurven
 * oder Ecken.
 * 
 * @author bk
 */
public class ShapeConnectorController implements IConnectorController {

	private static final Logger logger = Logger.getLogger(StraightConnectorController.class.getName());

	private static final double radiusX = 5.0;
	private static final double radiusY = 5.0;

	private static final DoubleProperty nullProperty = new SimpleDoubleProperty(0.0);

	private Double mouseX;
	private Double mouseY;

	private IConnector view;
	private IConnectorModel model;

	@Override
	public IConnectorModel getModel() {
		return model;
	}

	@Override
	public Node getView() {
		return (Node) view;
	}

	/**
	 * Constructor
	 * 
	 * @param model
	 * @param parentController
	 * @param connectors
	 */
	public ShapeConnectorController(IConnectorModel model, IShapeController parentController) {

		this.model = (IConnectorModel) model;
		IShapeView parentView = (IShapeView) parentController.getView();

		// den Connector zeichnen
		view = new StraightConnectorView(Bindings.add(radiusX / 2.0, model.xProperty()),
				Bindings.add(radiusY / 2.0, model.yProperty()));
		((Node) view).setVisible(false);

		// Eventhandler für Connectoren
		((Node) view).setOnMouseEntered(new ConnectorEnteredEventHandler(view));
		((Node) view).setOnMouseExited(new ConnectorExitedEventHandler(view));
		((Node) view).setOnMousePressed(new ConnectorPressedEventHandler(view, model));
		((Node) view).setOnMouseDragged(new ConnectorDraggedEventHandler(view, model));
		((Node) view).setOnMouseReleased(new ConnectorReleasedEventHandler(view, model));
	}

	/**
	 * Selektiert oder deselektiert die View.
	 */
	@Override
	public void setSelected(boolean isSelected) {

		this.view.setSelected(isSelected);
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird ausgeführt, wenn die Mouse über einem
	 * Arrow-Connector ist. In diesem Fall, wird der Cursor in einen Hand-Cursor
	 * geändert.
	 *
	 * @author bk
	 */
	class ConnectorEnteredEventHandler implements EventHandler<MouseEvent> {

		private IConnector connector;

		public ConnectorEnteredEventHandler(IConnector connector) {
			this.connector = connector;
		}

		@Override
		public void handle(MouseEvent event) {
			((Node) connector).getScene().setCursor(Cursor.HAND);
			connector.setSelected(true);
			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird ausgeführt, wenn die Mouse den Arrow-Connector
	 * verlässt. In diesem Fall wird der Default-Cursor wieder angezeigt.
	 *
	 * @author bk
	 */
	class ConnectorExitedEventHandler implements EventHandler<MouseEvent> {

		private IConnector connector;

		public ConnectorExitedEventHandler(IConnector connector) {
			this.connector = connector;
		}

		@Override
		public void handle(MouseEvent event) {
			((Node) connector).getScene().setCursor(Cursor.DEFAULT);
			connector.setSelected(false);
			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse über dem Arrow-Connector
	 * gedrückt wird. Die aktuelle Position der Mouse wird gespeichert.
	 *
	 * @author bk
	 */
	class ConnectorPressedEventHandler implements EventHandler<MouseEvent> {

		@SuppressWarnings("unused")
		private IConnector connector;
		private IConnectorModel model;

		public ConnectorPressedEventHandler(IConnector connector, IConnectorModel model) {
			this.connector = connector;
			this.model = model;
		}

		@Override
		public void handle(MouseEvent event) {
			mouseX = event.getSceneX();
			mouseY = event.getSceneY();
			logger.info(String.format("Connector point pressed at: %f/%f", model.xProperty().get(),
					model.yProperty().get()));
			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse über einem
	 * Arrow-Connector wieder freigegeben wird. Die Position des Konnektors wird auf
	 * die letzte Mausposition gesetzt.
	 *
	 * @author bk
	 */
	class ConnectorReleasedEventHandler implements EventHandler<MouseEvent> {

		@SuppressWarnings("unused")
		private IConnector connector;
		private IConnectorModel model;

		public ConnectorReleasedEventHandler(IConnector connector, IConnectorModel model) {
			this.connector = connector;
			this.model = model;
		}

		@Override
		public void handle(MouseEvent event) {

			if (mouseX != null && mouseY != null) {

				double deltaX = event.getSceneX() - mouseX;
				double deltaY = event.getSceneY() - mouseY;

				mouseX += deltaX;
				mouseY += deltaY;

				model.xProperty().set(model.xProperty().get() + deltaX);
				model.yProperty().set(model.yProperty().get() + deltaY);
				logger.info(String.format("Connector point released at: %f/%f", model.xProperty().get(),
						model.yProperty().get()));
			}

			event.consume();
		}
	}

	// ===============================================================

	/**
	 * Dieser EventHandler wird aufgerufen, wenn die Mouse mit dem Arrow-Connector
	 * verschoben wird. Dazu wird die Differenz zwischen der aktuellen
	 * Mouse-Position und der gespeicherten Mouse-Position berechnet und die
	 * Connector-Position entsprechend verschoben. Die neue Position wird dann
	 * wieder gespeichert.
	 *
	 * @author bk
	 */
	class ConnectorDraggedEventHandler implements EventHandler<MouseEvent> {

		@SuppressWarnings("unused")
		private IConnector connector;
		private IConnectorModel model;

		public ConnectorDraggedEventHandler(IConnector connector, IConnectorModel model) {
			this.connector = connector;
			this.model = model;
		}

		@Override
		public void handle(MouseEvent event) {

			if (mouseX != null && mouseY != null) {

				double deltaX = event.getSceneX() - mouseX;
				double deltaY = event.getSceneY() - mouseY;

				mouseX += deltaX;
				mouseY += deltaY;

				model.xProperty().set(model.xProperty().get() + deltaX);
				model.yProperty().set(model.yProperty().get() + deltaY);
				logger.info(String.format("Connector point moved to: %f/%f", model.xProperty().get(),
						model.yProperty().get()));
			}

			event.consume();
		}
	}
}
