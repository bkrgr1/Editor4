package de.bkroeger.editor4.runtime;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bkroeger.editor4.controller.EditorController;
import de.bkroeger.editor4.controller.FileController;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.model.FileModel;
import de.bkroeger.editor4.view.EditorView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Runtime-Datenstruktur auf Editor-Ebene.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString
@Component
public class EditorRuntime extends BaseRuntime implements IRuntime {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(EditorRuntime.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Die Daten für diese Ebene
	 */
	protected EditorModel model;
	
	/**
	 * Der verantwortliche Controller
	 */
	protected EditorController controller;
	
	/**
	 * Die generierte View
	 */
	protected EditorView view;
	
	/**
	 * Eine Map der View-Teile
	 */
	protected Map<String, IRuntime> viewParts = new HashMap<>();
	public void putViewPart(String key, IRuntime value) {
		viewParts.put(key, value);
	}
		
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	/**
	 * Default constructor for Spring Bean
	 */
	public EditorRuntime() { }
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/
	
	/**
	 * Initialisiert den Controller und generiert die Runtime-Struktur
	 * @throws InputFileException
	 */
	public void init() 
			throws InputFileException {
        
	    FileModel fileModel = null;
		
		// wurde eine Eingabedatei angegeben?
	    String inFilePath = null;
	    if (this.getModel().getParameters().getNamed().containsKey("file")) {
	    	
	    	// Pfad zur Eingabedatei ermitteln u
	        inFilePath = getModel().getParameters().getNamed().get("file");
	        File inFile = new File(inFilePath);
	        if (!inFile.exists()) {
	        	throw new InputFileException("File '"+inFilePath+"' does not exist!");
	        }
		    		
    		// JSON-Input-Datei parsen
		    ObjectMapper objectMapper = new ObjectMapper();
	    	try {
			    fileModel = objectMapper.readValue(inFile, FileModel.class);
	    	} catch(IOException | ParseException e) {
	    		throw new InputFileException(e.getMessage(), e);
	    	}
		    
	    } else {
	    	
	    	// leeres FileModel
	    	fileModel = new FileModel(this.model);
	    }
    	
    	fileModel.setParentModel(this.model);
    	FileController fileController = applicationContext.getBean(FileController.class);
    	
    	// die Runtime-Umgebung für die Anzeige der Datei
    	CenterRuntime centerRuntime = applicationContext.getBean(CenterRuntime.class);
    	centerRuntime.setParent(this);
    	centerRuntime.setModel(fileModel);
    	centerRuntime.setController(fileController);
    	centerRuntime.init();
	}
}
