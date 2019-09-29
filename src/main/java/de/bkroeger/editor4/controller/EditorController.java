package de.bkroeger.editor4.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.CommandOptions;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.view.EditorView;
import javafx.geometry.Dimension2D;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

/**
 * <p>Dies ist der zentrale Controller für die Editor4-Anwendung.</p>
 * <p>In der Methode {@link EditorController#buildView()} wird die gesamte View der 
 * Editor-Anwendung gebildet.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EditorController extends BaseController implements IController {

    private static final Logger logger = LogManager.getLogger(EditorController.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
   
    @Autowired
    private ApplicationContext appContext;
    
    private CommandOptions commandOptions;
    
    private int panelWidth;
    private int panelHeight;
    
    private TopController topController;
    private FileController fileController;
    private FooterController footerController;
    private NavigatorController navigatorController;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	public EditorController(CommandOptions cmd) {
		super();
		
		this.commandOptions = cmd;
		this.panelWidth = Integer.parseInt(cmd.valueOf("panelWidth"));
		this.panelHeight = Integer.parseInt(cmd.valueOf("panelHeight"));
	}
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/
	
	@Override
	public IModel getModel() {
		return (IModel) EditorModel.getEditorModel();
	}

	/**
	 * <p>Erzeugt die Ansicht der Editor-Anwendung.</p>
	 *
	 * @return ein {@link ControllerResult}
	 * @throws TechnicalException
	 * @throws InputFileException
	 * @throws CellCalculationException
	 */
	public ControllerResult buildView() 
			throws TechnicalException, InputFileException, CellCalculationException {

        logger.debug("Creating editor view...");

        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setController(this);
        
        // die Editor-Ansicht erstellen
        EditorView editorView = new EditorView();
        this.view = editorView;
        controllerResult.setView(editorView);
		
		// Top-View erzeugen
		topController = new TopController(EditorModel.getEditorModel());
		ControllerResult topResult = topController.buildView(controllerResult);
		
		// wurde eine Eingabedatei angegeben?
	    String inFilePath = null;
	    if (commandOptions.hasOption("-f")) {
	    	
	    	// Pfad zur Eingabedatei ermitteln u
	        inFilePath = commandOptions.valueOf("-f");
	        if (!new File(inFilePath).exists()) {
	        	throw new InputFileException("File '"+inFilePath+"' does not exist!");
	        }
	    }
	    
		
		// create a {@link FileController} for the file model
	    // wenn keine Eingabedatei angegeben wird, wird ein leers FileModel generiert
		fileController = appContext.getBean(FileController.class, panelWidth, panelHeight, inFilePath);
				//new FileController(panelWidth, panelHeight, inFilePath);
		
		// Variablen berechnen
		fileController.calculate(); // Querreferenzen berechnen
		
		// das Top-View oben anzeigen
		((BorderPane)editorView).setTop((Node) topResult.getView());
		
		// Center-View erzeugen
        ControllerResult result = fileController.buildView(controllerResult);
        
        // die File-View in der Mitte des BorderPane anzeigen
        ((BorderPane)editorView).setCenter((Node) result.getView());
        
        // Navigator-View erzeugen
        navigatorController = new NavigatorController(EditorModel.getEditorModel());
      
        // Footer-View erzeugen
		footerController = new FooterController(EditorModel.getEditorModel());
		ControllerResult footerResult = footerController.buildView(controllerResult);
		
		// die Footer-View unten anzeigen
		((BorderPane)editorView).setBottom((Node) footerResult.getView());
        
        return controllerResult;
	}
	
	/**
	 * Liefert den Titel für die Editor-Ansicht
	 * @return der Titel des Editors
	 */
	public String getTitle() {
		
		return fileController.getTitle();
	}
	
	/**========================================================================
	 * Private methods
	 *=======================================================================*/
}
