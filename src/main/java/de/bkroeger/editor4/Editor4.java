package de.bkroeger.editor4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.bkroeger.editor4.controller.EditorController;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.runtime.EditorRuntime;
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
		"de.bkroeger.editor4.controller",
		"de.bkroeger.editor4.functions",
		"de.bkroeger.editor4.services"})
public class Editor4 extends Application {

	private static final int PANEL_WIDTH = 1200;
	private static final int PANEL_HEIGHT = 800;

	private static final Logger logger = LogManager.getLogger(Editor4.class.getName());

	private ConfigurableApplicationContext context;
	
  	private static CommandOptions commandOptions;
  	
  	private static EditorRuntime editorRuntime;
  	public static EditorRuntime getEditorRuntime() {
  		return editorRuntime;
  	}
  	
  	/**
  	 * <p>Spring-Bean-Definition der Command-Options.</p>
  	 * @return die {@link CommandOptions}
  	 */
  	@Bean
  	public CommandOptions commandOptions() {
  		return new CommandOptions();
  	}

	/**
	 * Starts the application
	 *
	 * @param args
	 */
	public static void main(String[] args) {

	    commandOptions = new CommandOptions();
	    commandOptions.parse(args);
	    
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

	/**
	 * <p>JavaFX start method.</p>
	 * @throws TechnicalException 
	 */
	@Override
	public void start(Stage primaryStage) throws TechnicalException {
		
		logger.debug("JavaFX start:");
		
		try {
			commandOptions.addOption("panelWidth", ""+PANEL_WIDTH);
			commandOptions.addOption("panelHeight", ""+PANEL_HEIGHT);
			
			// den Haupt-Controller als Bean generieren lassen
			EditorController editorController = 
					context.getBean(EditorController.class, commandOptions);
			// der Controller erstellt die View und liefert die Runtime-Struktur
			Editor4.editorRuntime = editorController.buildView();
	
			// erzeugt das Root-Stackpane und fügt die Hauptansicht
			// alls einziges Child hinzu
			StackPane root = new StackPane();
			root.getChildren().add((Node) editorRuntime.getView());
	
			// erzeugt eine Szene und zeigt sie auf der Bühne
			Scene scene = new Scene(root, PANEL_WIDTH, PANEL_HEIGHT);
			// setzt den Titel
			primaryStage.titleProperty().bind(editorRuntime.getTitleProperty());
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
