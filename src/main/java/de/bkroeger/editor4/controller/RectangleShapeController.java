package de.bkroeger.editor4.controller;

import java.util.List;
import java.util.logging.Logger;

import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.model.RectangleShapeModel;
import de.bkroeger.editor4.view.IConnector;
import de.bkroeger.editor4.view.IShapeView;
import de.bkroeger.editor4.view.RectangleShapeView;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class RectangleShapeController implements IShapeController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(RectangleShapeController.class.getName());

	private IShapeView view;
	@Override
	public Node getView() { return (Node) view; }

	private Double mouseX;
	private Double mouseY;

	private RectangleShapeModel rectModel;
	
	public RectangleShapeController(IShapeModel model, EditorController parentController) {
		
		rectModel = (RectangleShapeModel) model;
		view = new RectangleShapeView(rectModel.xProperty(), rectModel.yProperty(),
				rectModel.widthProperty(), rectModel.heightProperty());
		
		// EventHandler zuordnen
		((Node)view).setOnMouseEntered(new MouseEnteredEventHandler(view));
		((Node)view).setOnMouseExited(new MouseExitedEventHandler(view));
		((Node)view).setOnMousePressed(new MousePressedEventHandler(view));
		((Node)view).setOnMouseDragged(new MouseDraggedEventHandler(view));
		((Node)view).setOnMouseReleased(new MouseReleasedEventHandler(view));
	}

	@Override
	public List<IConnector> getConnectors() { return view.getConnectors(); }

	// ===============================================================

	/**
	 * Dieser EventHandler wird ausgeführt, wenn die Mouse über dem Arrow ist.
	 * In diesem Fall, wird der Cursor in einen Hand-Cursor geändert.
	 *
	 * @author bk
	 */
	class MouseEnteredEventHandler implements EventHandler<MouseEvent> {
		
		private IShapeView shapeView;

		public MouseEnteredEventHandler(IShapeView shapeView) {
			this.shapeView = shapeView;
		}
		
		@Override
		public void handle(MouseEvent event) {			
			((Node)shapeView).getScene().setCursor(Cursor.HAND);
			shapeView.setSelected(true);
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
		
		private IShapeView shapeView;

		public MouseExitedEventHandler(IShapeView shapeView) {
			this.shapeView = shapeView;
		}

		@Override
		public void handle(MouseEvent event) {
			((Node)shapeView).getScene().setCursor(Cursor.DEFAULT);
			shapeView.setSelected(false);
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
		private IShapeView shapeView;

		public MousePressedEventHandler(IShapeView shapeView) {
			this.shapeView = shapeView;
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

		@SuppressWarnings("unused")
		private IShapeView arrow;

		public MouseReleasedEventHandler(IShapeView shapeView) {
			this.arrow = shapeView;
		}

		@Override
		public void handle(MouseEvent event) {

			if (mouseX != null && mouseY != null) {

				double deltaX = event.getSceneX() - mouseX;
				double deltaY = event.getSceneY() - mouseY;
				
				mouseX += deltaX;
				mouseY += deltaY;

				rectModel.xProperty().set(rectModel.xProperty().get() + deltaX);
				rectModel.yProperty().set(rectModel.yProperty().get() + deltaY);

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

		@SuppressWarnings("unused")
		private IShapeView arrow;

		public MouseDraggedEventHandler(IShapeView shapeView) {
			this.arrow = shapeView;
		}

		@Override
		public void handle(MouseEvent event) {
			
			double deltaX = event.getSceneX() - mouseX;
			double deltaY = event.getSceneY() - mouseY;
			
			mouseX += deltaX;
			mouseY += deltaY;
			
			// Shape verschieben
			rectModel.xProperty().set(rectModel.xProperty().get() + deltaX);
			rectModel.yProperty().set(rectModel.yProperty().get() + deltaY);

			event.consume();
		}
	}
}
