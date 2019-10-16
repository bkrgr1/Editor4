package de.bkroeger.editor4.controller;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.CommandOptions;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.FileModel;
import de.bkroeger.editor4.runtime.CenterRuntime;
import de.bkroeger.editor4.runtime.EditorRuntime;
import de.bkroeger.editor4.runtime.FooterRuntime;
import de.bkroeger.editor4.runtime.HeaderRuntime;
import de.bkroeger.editor4.runtime.NavigatorRuntime;
import de.bkroeger.editor4.view.EditorView;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

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

    /**
     * Constructor
     * @param cmd
     */
	public EditorController(CommandOptions cmd) {
		super();
		
		this.commandOptions = cmd;
		this.panelWidth = Integer.parseInt(cmd.valueOf("panelWidth"));
		this.panelHeight = Integer.parseInt(cmd.valueOf("panelHeight"));
		this.model = EditorModel.getEditorModel();
	}
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/

	/**
	 * <p>Erzeugt die Ansicht der Editor-Anwendung.</p>
	 *
	 * @return ein {@link EditorRuntime}
	 * @throws TechnicalException
	 * @throws InputFileException
	 * @throws CellCalculationException
	 */
	public EditorRuntime buildView() 
			throws TechnicalException, InputFileException, CellCalculationException {

        logger.debug("Creating editor view...");

        EditorRuntime editorRuntime = new EditorRuntime((EditorModel) model, this);
        editorRuntime.setController(this);
        
        // die Editor-Ansicht erstellen
        EditorView editorView = new EditorView();
        this.view = editorView;
        editorRuntime.setView(editorView);
		
		// Top-View erzeugen
		topController = new TopController(EditorModel.getEditorModel());
		HeaderRuntime headerRuntime = topController.buildView(editorRuntime);
		
		// das Top-View oben anzeigen
		editorView.setTop((Node) headerRuntime.getView());
		editorRuntime.putViewPart("TOP", headerRuntime);
		
		// wurde eine Eingabedatei angegeben?
	    String inFilePath = null;
	    if (commandOptions.hasOption("-f")) {
	    	
	    	// Pfad zur Eingabedatei ermitteln u
	        inFilePath = commandOptions.valueOf("-f");
	        if (!new File(inFilePath).exists()) {
	        	throw new InputFileException("File '"+inFilePath+"' does not exist!");
	        }
	    }	    
		
	    CenterRuntime centerRuntime = new CenterRuntime(editorRuntime);
	    
	    FileModel fileModel = new FileModel(inFilePath);
	    centerRuntime.setModel(fileModel);
	    
		// create a {@link FileController} for the file model
	    // wenn keine Eingabedatei angegeben wird, wird ein leers FileModel generiert
		fileController = new FileController(panelWidth, panelHeight, fileModel);
		centerRuntime.setController(fileController);
		
		// Variablen berechnen
		fileController.calculate(); // Querreferenzen berechnen
		
		// Center-View erzeugen
        centerRuntime.setView(fileController.buildView(centerRuntime));
        
        // die File-View in der Mitte des BorderPane anzeigen
        editorView.setCenter((Node) centerRuntime.getView());
        editorRuntime.putViewPart("CENTER", centerRuntime);
        
        // Navigator-View erzeugen
        navigatorController = new NavigatorController(EditorModel.getEditorModel());
        NavigatorRuntime navigatorRuntime = navigatorController.buildView(editorRuntime);
        editorRuntime.putViewPart("LEFT", navigatorRuntime);
      
        // Footer-View erzeugen
		footerController = new FooterController(EditorModel.getEditorModel());
		FooterRuntime footerRuntime = footerController.buildView(editorRuntime);
		editorRuntime.putViewPart("BOTTOM", footerRuntime);
		
		// die Footer-View unten anzeigen
		editorView.setBottom((Node) footerRuntime.getView());
        
        return editorRuntime;
	}
	
	/**
	 * Liefert den Titel für die Editor-Ansicht
	 * @return der Titel des Editors
	 */
	public StringProperty getTitleProperty() {
		
		return fileController.getTitleProperty();
	}
	
	/**========================================================================
	 * Private methods
	 *=======================================================================*/
}
