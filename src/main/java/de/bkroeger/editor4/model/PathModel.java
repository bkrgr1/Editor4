package de.bkroeger.editor4.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.beans.property.DoubleProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Model of a shape path.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString(callSuper=true)
public class PathModel extends SectionModel implements IModel {

	private static final Logger logger = LogManager.getLogger(PathModel.class.getName());
		
	/**========================================================================
	 * Fields
	 *=======================================================================
	 * @throws TechnicalException 
	 * @throws CellCalculationException */
	
	public DoubleProperty getXProperty() throws CellCalculationException, TechnicalException {
		
		return getDoubleCellProperty("x");
	}
	
	public DoubleProperty getYProperty() throws CellCalculationException, TechnicalException {
		
		return getDoubleCellProperty("y");
	}

	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public PathModel() {
		super(SectionModelType.Path);
	}
	
	private PathType pathType;
	
	public PathModel(PathType pt) {
		super(SectionModelType.Path);
		this.pathType = pt;
	}
	
	public PathModel(PathModel model) {
		super(SectionModelType.Path);
		this.pathType = model.pathType;
	}
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/
	
	@Override
	public SectionModel loadModel(JSONObject jsonSection, IModel parentSection) 
			throws TechnicalException, InputFileException {
    
    	super.loadModel(jsonSection, this);
    	
    	logger.debug(String.format("Pathmodel has %d cells and %d sections",
    			this.cells.size(), this.sections.size()));
    	
		return this;
	}
	
	/**========================================================================
	 * Private methods
	 *=======================================================================*/

	private DoubleProperty getDoubleCellProperty(String cellName) 
			throws CellCalculationException, TechnicalException {
		
		CellModel cell = this.cells.get(cellName.toLowerCase());
		if (cell != null) {
			return cell.getDoubleProperty();
		} else {
			throw new TechnicalException("Cell with type '"+cellName+"' not found");
		}
	}
}
