package de.bkroeger.editor4.controller;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.CommandOptions;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.view.EditorView;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class EditorController extends BaseController implements IController {

    private static final Logger logger = LogManager.getLogger(EditorController.class.getName());
    
    private CommandOptions commandOptions;
    
    private int panelWidth;
    private int panelHeight;
    
    private TopController topController;
    private FileController fileController;

	public EditorController(CommandOptions cmd, int panelWidth, int panelHeight) {
		super();
		this.commandOptions = cmd;
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
	}

	public ControllerResult buildView() 
			throws TechnicalException, InputFileException, CellCalculationException {

        logger.debug("Creating editor view...");

        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setController(this);
        
        EditorView editorView = new EditorView();
        this.view = editorView;
        controllerResult.setView(editorView);
		
		// wurde eine Eingabedatei angegeben?
	    String inFilePath = null;
	    if (commandOptions.hasOption("-f")) {
	    	// Pfad zur Eingabedatei
	        inFilePath = commandOptions.valueOf("-f");
	        if (!new File(inFilePath).exists()) {
	        	throw new InputFileException("File '"+inFilePath+"' does not exist!");
	        }
	    }
		
		// create a {@link FileController} for the file model
		fileController = new FileController(panelWidth, panelHeight, inFilePath);
		
		// Variablen berechnen
		fileController.calculate(); // Querreferenzen berechnen
		
		// Top-View erzeugen
		topController = new TopController();
		ControllerResult topResult = topController.buildView(controllerResult);
		
		// das Top-View oben anzeigen
		((BorderPane)editorView).setTop((Node) topResult.getView());
		
		// Center-View erzeugen
        ControllerResult result = fileController.buildView(controllerResult);
        
        // die File-View in der Mitte des BorderPane anzeigen
        ((BorderPane)editorView).setCenter((Node) result.getView());
        
        return controllerResult;
	}
	
	/**
	 * Liefert den Titel für die Editor-Ansicht
	 * @return der Titel des Editors
	 */
	public String getTitle() {
		
		return fileController.getTitle();
	}
}
