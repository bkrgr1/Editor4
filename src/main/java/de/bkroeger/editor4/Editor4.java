package de.bkroeger.editor4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import de.bkroeger.editor4.controller.ControllerResult;
import de.bkroeger.editor4.controller.EditorController;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * <p>Dies ist die Hauptklasse eines graphischen Editors.</p>
 * 
 * @author bk
 */
@SpringBootApplication
public class Editor4 extends Application {

	private static final int PANEL_WIDTH = 800;
	private static final int PANEL_HEIGHT = 600;

	private static final Logger logger = LogManager.getLogger(Editor4.class.getName());

	private ConfigurableApplicationContext context;
	
	private static CommandOptions cmd;

	/**
	 * Starts the application
	 *
	 * @param args
	 */
	public static void main(String[] args) {

	    cmd = new CommandOptions(args);
	    
		// launch the application with given arguments
		logger.debug("Starting Java FX application...");
		launch(args);
	}

	/**
	 * <p>JavaFX init method.</p>
	 */
	@Override
	public void init() throws Exception {
		
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Editor4.class);
		context = builder.run(getParameters().getRaw().toArray(new String[0]));
		logger.debug("JavaFX init():");
	}
	
	@Bean
	public EditorController editorController() {
		return new EditorController(cmd, PANEL_WIDTH, PANEL_HEIGHT);
	}

	/**
	 * <p>JavaFX start method.</p>
	 * @throws TechnicalException 
	 */
	@Override
	public void start(Stage primaryStage) throws TechnicalException {
		
		logger.debug("JavaFX start:");
		try {
			EditorController editorController = editorController();
			
			ControllerResult result = editorController.buildView();
	
			// add the view of the first/only page as pane to the root layout
			StackPane root = new StackPane();
			root.getChildren().add((Node) result.getView());
	
			// create a scene
			Scene scene = new Scene(root, PANEL_WIDTH, PANEL_HEIGHT);
			// and show it on the stage
			primaryStage.setTitle(editorController.getTitle());
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.show();
			
		} catch(TechnicalException | InputFileException e) {
			logger.error(e.getMessage(), e);
			System.exit(22);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			System.exit(44);
		}
	}

	@Override
	public void stop() throws Exception {
		logger.debug("JavaFX stop():");
		context.close();
	}
}
