package de.bkroeger.editor4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.bkroeger.editor4.controller.EditorController;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.runtime.EditorRuntime;
import de.bkroeger.editor4.view.EditorView;
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
@Configuration
@ComponentScan(basePackages = {
		"de.bkroeger.editor4",
		"de.bkroeger.editor4.calculation",
		"de.bkroeger.editor4.controller",
		"de.bkroeger.editor4.exceptions",
		"de.bkroeger.editor4.functions",
		"de.bkroeger.editor4.Handler",
		"de.bkroeger.editor4.model",
		"de.bkroeger.editor4.utils",
		"de.bkroeger.editor4.view",
		"de.bkroeger.editor4.services"})
public class Editor4 extends Application {

	private static final Logger logger = LogManager.getLogger(Editor4.class.getName());

	private static final int PANEL_WIDTH = 1200;
	private static final int PANEL_HEIGHT = 800;
	
	private ConfigurableApplicationContext context;

	/**
	 * Starts the application
	 *
	 * @param args
	 */
	public static void main(String[] args) {
	    
		// launch the application with given arguments
		logger.debug("Starting Java FX application...");
		Application.launch(Editor4.class, args);
	}

	/**
	 * <p>JavaFX init method.</p>
	 * <p>Erstellt eine Spring-Application.</p>
	 */
	@Override
	public void init() throws Exception {
		
		logger.debug("JavaFX init():");
		context = new SpringApplicationBuilder(Editor4.class).run();
	}

	/**
	 * <p>JavaFX start method.</p>
	 * @throws TechnicalException 
	 */
	@Override
	public void start(Stage primaryStage) throws TechnicalException {
		
		logger.debug("JavaFX start:");
		
		try {
			// den Haupt-Controller als Bean generieren lassen
			EditorController editorController = context.getBean(EditorController.class);
			EditorModel editorModel = context.getBean(EditorModel.class);
			editorModel.setParameters(getParameters());
			
			EditorRuntime editorRuntime = context.getBean(EditorRuntime.class);
			editorRuntime.setModel(editorModel);
			editorRuntime.setController(editorController);
			editorRuntime.init();
			
			EditorView editorView = editorController.buildView(editorRuntime);
	
			// erzeugt das Root-Stackpane und fügt die Hauptansicht
			// alls einziges Child hinzu
			StackPane root = new StackPane();
			root.getChildren().add((Node) editorView);
	
			// erzeugt eine Szene und zeigt sie auf der Bühne
			Scene scene = new Scene(root, PANEL_WIDTH, PANEL_HEIGHT);
			// setzt den Titel
			primaryStage.titleProperty().bind(editorController.getModel().getTitleProperty());
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.show();
					
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
