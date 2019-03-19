package de.bkroeger.editor4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import de.bkroeger.editor4.controller.FileController;
import de.bkroeger.editor4.model.FileModel;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Dieser Test simuliert die Annäherung eines Pfeils an ein Rechteck-Shape. Wenn
 * beide Shapes sich überlagern (intersects), beginnt der Rahmen des Rechtecks
 * zu blinken. Wenn dann die Maus dann losgelassen wird, wird die Position der
 * Mausspitze mit der Position des Rechteck-Mittelpunktes verknüpft.
 * 
 * @author bk
 *
 */
@SpringBootApplication
public class Editor4 extends Application {

	private static final int PANEL_WIDTH = 800;
	private static final int PANEL_HEIGHT = 600;

	private static final Logger logger = LogManager.getLogger(Editor4.class.getName());

	private ConfigurableApplicationContext context;


	/**
	 * Starts the application
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		// launch the application with given arguments
		logger.debug("Starting Java FX application...");
		launch(args);
	}

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Editor4.class);
		context = builder.run(getParameters().getRaw().toArray(new String[0]));
		logger.debug("JavaFX init():");
	}

	/**
	 * Override the JavaFX start method
	 */
	@Override
	public void start(Stage primaryStage) {
		logger.debug("JavaFX start:");

		// create a {@link FileModel} for a new empty file
		FileModel fileModel = new FileModel();

		// create a {@link FileController} for the file model
		FileController fileController = new FileController(fileModel, PANEL_WIDTH, PANEL_HEIGHT);

		// add the view of the first/only page as pane to the root layout
		StackPane root = new StackPane();
		root.getChildren().add((Node) fileController.getView());

		// create a scene
		Scene scene = new Scene(root, PANEL_WIDTH, PANEL_HEIGHT);
		// and show it on the stage
		primaryStage.setTitle(fileController.getTitle());
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		logger.debug("JavaFX stop():");
		context.close();
	}
}
