package de.bkroeger.editor4.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.bkroeger.editor4.controller.FileController.PageComparator;
import de.bkroeger.editor4.controller.PageController;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * Definiert die Daten für eine Diagrammdatei.
 * </p>
 */
@Getter
@Setter
@ToString(callSuper=true)
public class FileModel extends SectionModel implements IModel {

	private static final Logger logger = LogManager.getLogger(FileModel.class.getName());
	
	private static final String LOCALE_KEY = "locale";

	/**
	 * Input File
	 */
    private File inFile;
    
    /**
     * Sprach-Locale
     */
    private Locale locale = new Locale("en-us");
    
    /**
     * Liste der Seiten-Controller
     */
    private SortedSet<PageController> pageControllers = new TreeSet<>(new PageComparator());

    /**
     * Default constructor
     */
    public FileModel() {
    	super(SectionModelType.File);
    }
    
    /**
     * Constructor with file path
     * @param inFilePath
     * @throws InputFileException 
     */
    public FileModel(String inFilePath) throws InputFileException {
    	super(SectionModelType.File);
    	
    	if (inFilePath != null && !inFilePath.isEmpty()) {
	    	File file = new File(inFilePath);
	    	if (!file.exists()) {
	    		throw new InputFileException("File does not exist: "+inFilePath);
	    	}
	    	this.inFile = file;
    	} else {
    		this.inFile = null;
    	}
    }
    
    /**
     * Lädt das Datenmodell aus einer Json-Datei oder bildet ein Default-Datenmodell
     * @throws TechnicalException 
     * @throws InputFileException 
     * @throws CellCalculationException 
     */
    @Override
    public FileModel loadModel(JSONObject jsonSection, IModel parentSection) 
    		throws TechnicalException, InputFileException {
    	
    	logger.debug("Load file model from JSON: "+
    			(this.inFile != null ? this.inFile.getAbsolutePath() : "No infile"));
    	
    	try {
    		
    		if (inFile != null) {
    			
	    		// Erstelle das File-Datenmodell aus der angegebenen Json-Datei
		    	JSONParser parser = new JSONParser();
		    	JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(inFile.getAbsolutePath()));
		    	
		    	// JSON-Objekt "file"
		    	JSONObject jsonFile = null;
		    	if (jsonObject.containsKey("file")) {
		    		jsonFile = (JSONObject) jsonObject.get("file");
		    	} else {
		    		throw new InputFileException("Invalid json file: file object missing");
		    	}
		    	
		    	for (Object key : jsonFile.keySet()) {
		    		String k = key.toString();
		    		switch (k) {   			
		    		case LOCALE_KEY:
			    		this.locale = Locale.forLanguageTag((String)jsonFile.get(LOCALE_KEY));
			    		break;
		    		default:
		    			// skip
		    		}
		    	}

		    	
		    	// die Standard-Elemente laden
		    	super.loadModel(jsonFile, this);
		    	
		    	logger.debug("File model loaded");
		    
    		} else {
    			
    			// ein leeres File-Datenmodell erstellen
    			buildInitialFileModel();
    			
    			logger.debug("File model initialized");
    		}
    		
    		return this;
	    	
    	} catch(IOException e) {
    		logger.error(e.getMessage(), e);
    		throw new InputFileException("IO-Error parsing Json file="+inFile.getAbsolutePath());
    		
    	} catch(ParseException e) {
    		logger.error(e.getMessage(), e);
    		throw new InputFileException("Parse-Error parsing Json file="+inFile.getAbsolutePath());
    	}
    }
    
    /**
     * Ein leeres File-Datenmodell erstellen
     * @return
     */
    private FileModel buildInitialFileModel() {
		// TODO Auto-generated method stub
		return null;
	}
}