package de.bkroeger.editor4.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class FileModel extends BaseModel implements IModel {

	private static final Logger logger = LogManager.getLogger(FileModel.class.getName());

	/**
	 * Input File
	 */
	@JsonIgnore
    protected File inFile;
    
    /**
     * Sprach-Locale
     */
    protected Locale locale = new Locale("en-us");
    
    /**
     * Liste der Seiten
     */
    protected List<PageModel> pages = new ArrayList<>();
    
    protected List<CellModel> cells = new ArrayList<>();
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
    
    public FileModel() {
    	super(ModelType.File);
    }

    /**
     * Default constructor
     */
    public FileModel(IModel parentModel) {
    	super(ModelType.File);
    	this.parentModel = parentModel;
    }
    
    /**
     * Constructor with file path
     * @param inFilePath
     * @throws InputFileException 
     */
    public FileModel(IModel parentModel, String inFilePath) throws InputFileException {
    	super(ModelType.File);
    	this.parentModel = parentModel;
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