package de.bkroeger.editor4;

import java.util.logging.Logger;

import de.bkroeger.editor4.controller.EditorController;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.model.LineEndingType;
import de.bkroeger.editor4.model.RectangleShapeModel;
import de.bkroeger.editor4.model.StraightArrowModel;
import de.bkroeger.editor4.view.EditorPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Dieser Test simuliert die Annäherung eines Pfeils an ein Rechteck-Shape.
 * Wenn beide Shapes sich überlagern (intersects), beginnt der Rahmen des Rechtecks zu blinken.
 * Wenn dann die Maus dann losgelassen wird, wird die Position der Mausspitze mit
 * der Position des Rechteck-Mittelpunktes verknüpft.
 * @author bk
 *
 */
public class Editor4 extends Application {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Editor4.class.getName());

	private static final int PANEL_WIDTH = 800;
	private static final int PANEL_HEIGHT = 600;

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
	    
	    IShapeModel rectModel = new RectangleShapeModel(100.0, 100.0, 100.0, 100.0);
	    editorModel.addShapeModel(rectModel);
	    
	    StraightArrowModel arrowModel = new StraightArrowModel(250.0, 100.0, 350.0, 50.0, -15.0);
	    arrowModel.strokeWidthProperty().set(2.0);
	    arrowModel.lineStartTypeProperty().set(LineEndingType.OpenArrow);
	    editorModel.addArrowModel(arrowModel);

		// create a drawing canvas
		EditorPane editorPane = new EditorPane(PANEL_WIDTH, PANEL_HEIGHT);
		
		EditorController editorCtrl = new EditorController(editorModel, editorPane);

		// add the pane to the root layout
		StackPane root = new StackPane();
		root.getChildren().add(editorCtrl.getView());

		// create a scene
		Scene scene = new Scene(root, PANEL_WIDTH, PANEL_HEIGHT);
		// and show it on the stage
		primaryStage.setTitle(editorCtrl.getModel().getTitle());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
