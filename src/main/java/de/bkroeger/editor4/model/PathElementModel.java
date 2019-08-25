package de.bkroeger.editor4.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Model of a PathElement</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString(callSuper=true)
public class PathElementModel extends SectionModel {

	private static final Logger logger = LogManager.getLogger(PathElementModel.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/

    /**
     * Art des PathElement
     */
	private PathElementType elemType;
	
	public DoubleProperty getXProperty() throws TechnicalException, CellCalculationException {
		
		return getDoubleCellProperty("X");
	}
	
	public DoubleProperty getYProperty() throws TechnicalException, CellCalculationException {
		
		return getDoubleCellProperty("Y");
	}
	
	public DoubleProperty getRadiusXProperty() throws TechnicalException, CellCalculationException {
		
		return getDoubleCellProperty("radiusX");
	}
	
	public DoubleProperty getRadiusYProperty() throws TechnicalException, CellCalculationException {
		
		return getDoubleCellProperty("radiusY");
	}
	
	public BooleanProperty getLargeArcFlagProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("largeArcFlag");
		if (cell != null) {
			return cell.getBooleanProperty();
		} else {
			return new SimpleBooleanProperty(false);
		}
	}
	
	public BooleanProperty getSweepFlagProperty() throws TechnicalException, CellCalculationException {
		
		CellModel cell = this.cells.get("sweepFlag");
		if (cell != null) {
			return cell.getBooleanProperty();
		} else {
			return new SimpleBooleanProperty(false);
		}
	}
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	/**
	 * Constructor
	 * @param pt
	 */
	public PathElementModel(PathElementType pt) {
		super(SectionModelType.PathElement);
		this.elemType = pt;
	}

	public PathElementModel(PathElementModel model) {
		super(SectionModelType.PathElement);
		this.elemType = model.elemType;
	}

	/**========================================================================
	 * Public methods
	 *=======================================================================*/
	
	/**
	 * Load the section data from JSON
	 * @throws TechnicalException 
	 * @throws InputFileException 
	 */
	public SectionModel loadModel(JSONObject jsonSection, IModel parentModel) 
			throws TechnicalException, InputFileException {
    
    	super.loadModel(jsonSection, this);
    	
    	logger.debug(String.format("PathElement model "+this.nameU+" has %d cells and %d sections",
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
