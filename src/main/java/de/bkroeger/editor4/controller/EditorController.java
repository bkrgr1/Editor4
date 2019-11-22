package de.bkroeger.editor4.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.runtime.EditorRuntime;
import de.bkroeger.editor4.runtime.FooterRuntime;
import de.bkroeger.editor4.runtime.HeaderRuntime;
import de.bkroeger.editor4.runtime.NavigatorRuntime;
import de.bkroeger.editor4.view.EditorView;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Dies ist der zentrale Controller für die Editor4-Anwendung.</p>
 * <p>In der Methode {@link EditorController#buildView()} wird die gesamte View der 
 * Editor-Anwendung gebildet.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString(callSuper=true)
@Component
public class EditorController extends BaseController implements IController {

    private static final Logger logger = LogManager.getLogger(EditorController.class.getName());

	/**========================================================================
	 * Fields
	 *=======================================================================*/
    
    private TopController topController;
    private FileController fileController;
    private FooterController footerController;
    private NavigatorController navigatorController;
    
    public EditorModel getModel() { return (EditorModel) this.model; }
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
    
    /**
     * Default Constructor for Spring
     */
	public EditorController() {
		super();
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
	public EditorView buildView(EditorRuntime editorRuntime) 
			throws TechnicalException, InputFileException, CellCalculationException {

        logger.debug("Creating editor view...");
        
        // die Editor-Ansicht erstellen
        EditorView editorView = new EditorView();
        this.view = editorView;
        editorRuntime.setView(editorView);
		
		// Top-View erzeugen
		topController = new TopController((EditorModel)this.model);
		HeaderRuntime headerRuntime = topController.buildView(editorRuntime);
		
		// das Top-View oben anzeigen
		editorView.setTop((Node) headerRuntime.getView());
		editorRuntime.putViewPart("TOP", headerRuntime);
	    
//		// create a {@link FileController} for the file model
//	    // wenn keine Eingabedatei angegeben wird, wird ein leers FileModel generiert
//		fileController = new FileController(panelWidth, panelHeight, fileModel);
//		centerRuntime.setController(fileController);
//		
//		// Variablen berechnen
//		fileController.calculate(); // Querreferenzen berechnen
//		
//		// Center-View erzeugen
//        centerRuntime.setView(fileController.buildView(centerRuntime));
//        
//        // die File-View in der Mitte des BorderPane anzeigen
//        editorView.setCenter((Node) centerRuntime.getView());
//        editorRuntime.putViewPart("CENTER", centerRuntime);
        
        // Navigator-View erzeugen
        navigatorController = new NavigatorController((EditorModel)this.model);
        NavigatorRuntime navigatorRuntime = navigatorController.buildView(editorRuntime);
        editorRuntime.putViewPart("LEFT", navigatorRuntime);
      
        // Footer-View erzeugen
		footerController = new FooterController((EditorModel)this.model);
		FooterRuntime footerRuntime = footerController.buildView(editorRuntime);
		editorRuntime.putViewPart("BOTTOM", footerRuntime);
		
		// die Footer-View unten anzeigen
		editorView.setBottom((Node) footerRuntime.getView());
        
        return editorView;
	}
	
	/**========================================================================
	 * Private methods
	 *=======================================================================*/
}
