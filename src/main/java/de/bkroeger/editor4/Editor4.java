package de.bkroeger.editor4;

import java.util.logging.Logger;

import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.view.EditorPane;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Dieser Test simuliert die Annäherung eines Pfeils an ein Rechteck-Shape.
 * Wenn beide Shapes sich überlagern (intersects), beginnt der Rahmen des Rechtecks zu blinken.
 * Wenn dann die Maus dann losgelassen wird, wird die Position der Mausspitze mit
 * der Position des Rechteck-Mittelpunktes verknüpft.
 * @author bk
 *
 */
public class Editor4 extends Application {

	private static final Logger logger = Logger.getLogger(Editor4.class.getName());

	private static final Color BACKGROUND_COLOR = Color.ALICEBLUE;

	private static final int PANEL_WIDTH = 800;
	private static final int PANEL_HEIGHT = 600;

	private DoubleProperty arrowStartXProperty = new SimpleDoubleProperty(160.0);
	private DoubleProperty arrowStartYProperty = new SimpleDoubleProperty(100.0);
	private DoubleProperty arrowEndXProperty = new SimpleDoubleProperty(200.0);
	private DoubleProperty arrowEndYProperty = new SimpleDoubleProperty(100.0);

	private DoubleProperty rectXProperty = new SimpleDoubleProperty(100.0);
	private DoubleProperty rectYProperty = new SimpleDoubleProperty(100.0);
	private DoubleProperty rectWidthProperty = new SimpleDoubleProperty(20.0);
	private DoubleProperty rectHeightProperty = new SimpleDoubleProperty(20.0);

	private Double mouseX;
	private Double mouseY;

	/**
	 * Starts the application
	 *
	 * @param args
	 */
	public static void main(String[] args) {
    // launch the application with given arguments
		launch(args);
	}

	/**
	 * Override the JavaFX start method
	 */
	@Override
	public void start(Stage primaryStage) {

	    // create the editorModel
	    EditorModel editorModel = new EditorModel();

		// create a drawing canvas
		EditorPane editorPane = new EditorPane(PANEL_WIDTH, PANEL_HEIGHT);

		// draw a fixed rectangle
		Rectangle rect = drawRectangle();

		// draw a moveable Arrow
		Path arrow = drawArrow();

		Timeline timeline = new Timeline();
		Timeline timeline2 = new Timeline();

		timeline.setCycleCount(Animation.INDEFINITE);

		// The following EventHandler specifies what will be done
		// after the animation specified by the KeyFrame is finished.
		// The arrow token -> identifies a Lambda expression.
		EventHandler<ActionEvent> on_finished = (ActionEvent event) -> {
			if (arrow.getStroke() == Color.BLUE) {
				arrow.setStroke(Color.TRANSPARENT);
			} else {
				arrow.setStroke(Color.BLUE);
			}
		};
		EventHandler<ActionEvent> on_finished2 = (ActionEvent event) -> {
			if (rect.getStroke() == Color.BLUE) {
				rect.setStroke(Color.BLACK);
			} else {
				rect.setStroke(Color.BLUE);
			}
		};

		// Next we specify a KeyFrame whose execution takes 1 second.
		// The EventHandler will be executed after that.
		// No actual modification of values is specified by this KeyFrame.
		// This can thus be considered a misuse of a KeyFrame.
		KeyFrame keyframe = new KeyFrame(Duration.millis(100), on_finished);
		KeyFrame keyframe2 = new KeyFrame(Duration.millis(100), on_finished2);

		timeline.getKeyFrames().add(keyframe);
		timeline2.getKeyFrames().add(keyframe2);

		arrow.setOnMousePressed(new MousePressedEventHandler(arrow, timeline));
		arrow.setOnMouseDragged(new MouseDraggedEventHandler(arrow, timeline2, rect));
		arrow.setOnMouseReleased(new MouseReleasedEventHandler(arrow, timeline, timeline2, rect));

		// add rectangle and path to the pane
		editorPane.getChildren().addAll(rect, arrow);

		// add the pane to the root layout
		StackPane root = new StackPane();
		root.getChildren().add(editorPane);



		// create a scene
		Scene scene = new Scene(root, PANEL_WIDTH, PANEL_HEIGHT);
		// and show it on the stage
		primaryStage.setTitle("Coming near");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private Rectangle drawRectangle() {
		Rectangle rect = new Rectangle();
		rect.xProperty().bind(rectXProperty);
		rect.yProperty().bind(rectYProperty);
		rect.widthProperty().bind(rectWidthProperty);
		rect.heightProperty().bind(rectHeightProperty);
		rect.setFill(Color.YELLOW);
		rect.setStroke(Color.BLACK);
		return rect;
	}

	private Path drawArrow() {
		Path path = new Path();
		path.setStroke(Color.BLUE);
		MoveTo moveto1 = new MoveTo();
		moveto1.xProperty().bind(arrowStartXProperty);
		moveto1.yProperty().bind(arrowStartYProperty);
		LineTo lineto1 = new LineTo();
		lineto1.xProperty().bind(arrowEndXProperty);
		lineto1.yProperty().bind(arrowEndYProperty);
		LineTo lineto2 = new LineTo();
		lineto2.xProperty().bind(arrowEndXProperty.add(-10.0));
		lineto2.yProperty().bind(arrowEndYProperty.add(-5.0));
		MoveTo moveto2 = new MoveTo();
		moveto2.xProperty().bind(arrowEndXProperty);
		moveto2.yProperty().bind(arrowEndYProperty);
		LineTo lineto3 = new LineTo();
		lineto3.xProperty().bind(arrowEndXProperty.add(-10.0));
		lineto3.yProperty().bind(arrowEndYProperty.add(+5.0));
		path.getElements().addAll(moveto1, lineto1, lineto2, moveto2, lineto3);
		return path;
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

	// ===============================================================

	/**
	 * Diese Klasse behandelt das Drücker der linken Maustaste über dem Kreuz.
	 *
	 * @author bk
	 */
	class MousePressedEventHandler implements EventHandler<MouseEvent> {

		private Timeline timeline;

		public MousePressedEventHandler(Path path, Timeline timeline) {
			this.timeline = timeline;
		}

		@Override
		public void handle(MouseEvent event) {
			mouseX = event.getSceneX();
			mouseY = event.getSceneY();
			Point2D pointScene = new Point2D(mouseX, mouseY);
			logger.info("Mouse pressed at: " + pointScene.toString());
			timeline.play();
			event.consume();
		}
	}

	// ===============================================================

	class MouseReleasedEventHandler implements EventHandler<MouseEvent> {

		private Timeline timeline;
		private Timeline timeline2;
		private Path arrow;
		private Rectangle rect;

		public MouseReleasedEventHandler(Path path, Timeline timeline, Timeline timeline2, Rectangle rect) {
			this.timeline = timeline;
			this.timeline2 = timeline2;
			this.arrow = path;
			this.rect = rect;
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

				arrowStartXProperty.set(arrowStartXProperty.get() + deltaX);
				arrowStartYProperty.set(arrowStartYProperty.get() + deltaY);
				arrowEndXProperty.set(arrowEndXProperty.get() + deltaX);
				arrowEndYProperty.set(arrowEndYProperty.get() + deltaY);
				timeline.stop();
				arrow.setStroke(Color.BLUE);

				// ist die Pfeilspitze in der Nähe des Rechtecks?
				if (intersectsWithConnectorPoint(rect, arrow)) {
					// ja,
					logger.info("Arrow intersects with rectangle!");
					timeline2.stop();
					rect.setStroke(Color.BLACK);

					// den Endpunkt des Pfeils mit der Mitte des Rechtecks verknüpfen
//					Double oldEndX = arrowEndXProperty.get();
//					arrowEndXProperty.bind(rectXProperty.add(rectWidthProperty.get()/2));
//					Double oldEndY = arrowEndYProperty.get();
//					arrowEndYProperty.bind(rectYProperty.add(rectHeightProperty.get()/2));
					// den Anfangspunkt analog verschieben
//					arrowStartXProperty.set(arrowStartXProperty.get()+arrowEndXProperty.get()-oldEndX);
//					arrowStartYProperty.set(arrowStartYProperty.get()+arrowEndYProperty.get()-oldEndY);
				}

				event.consume();
			}
		}
	}

	// ===============================================================

	class MouseDraggedEventHandler implements EventHandler<MouseEvent> {

		private Timeline timeline;
		private Path arrow;
		@SuppressWarnings("unused")
		private Rectangle rect;

		public MouseDraggedEventHandler(Path path, Timeline timeline, Rectangle rect) {
			this.timeline = timeline;
			this.arrow = path;
			this.rect = rect;
		}

		@Override
		public void handle(MouseEvent event) {
			double deltaX = event.getSceneX() - mouseX;
			double deltaY = event.getSceneY() - mouseY;
			mouseX += deltaX;
			mouseY += deltaY;
			Point2D pointScene = new Point2D(mouseX, mouseY);
			logger.info("Mouse moved to: " + pointScene.toString());
			arrowStartXProperty.set(arrowStartXProperty.get() + deltaX);
			arrowStartYProperty.set(arrowStartYProperty.get() + deltaY);
			arrowEndXProperty.set(arrowEndXProperty.get() + deltaX);
			arrowEndYProperty.set(arrowEndYProperty.get() + deltaY);

			// ist die Pfeilspitze in der Nähe des Rechtecks?
			if (intersectsWithConnectorPoint(rect, arrow)) {
				logger.info("Arrow intersects with rectangle!");
				timeline.play();
			} else {
				timeline.stop();
			}

			event.consume();
		}
	}
}
